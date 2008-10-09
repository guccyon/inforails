package jp.co.infonic.common.mail;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import jp.co.infonic.common.batch.CnkBatch;
import jp.co.infonic.common.db.DBQuery;
import jp.co.infonic.common.db.DBUpdate;
import jp.co.infonic.common.module.log.BatchLogger;
import jp.co.infonic.common.util.ConditionSupport;
import jp.co.infonic.inforails.base.property.SystemProperty;

/**
 * 
 * 概要：メール送信バッチ<br>
 * 詳細：メール送信情報テーブルより未送信メールデータを<br>
 * 　　　取得し、送信します。<br>
 *<pre>
 * [変更履歴]
 * 日付        連番   名前      内容
 * --------------------------------------------------
 * 2006/09/02  VL000  樋口      新規
 *</pre>
 * @author 樋口
 */
public class MailSendTask extends CnkBatch {
	
	private BatchLogger logger = BatchLogger.getLogger(this.getClass());
	
	private static final String NAME = "メール送信バッチ";
	
	// ディフォルト実行間隔：５分
	private static int DEFAULT_PERIOD = 5;
	
	public MailSendTask(int period) {
		super(period);
	}
	
	public MailSendTask() {
		this(DEFAULT_PERIOD);
	}

	protected int execute() throws SQLException {
		logger.debug("メール送信実行します。");
		int result = STATUS_NORMAL;
		
		DBQuery query = new DBQuery(this.getClass(), "GetWaitingMail");
		List waitingList = query.executeQueryForList(null);
		logger.debug("送信待ちメール："+ waitingList.size() + "件");
		if (waitingList.size() == 0) {
			return result;
		}

		Iterator iter = waitingList.iterator();
		while(iter.hasNext()) {
			Map waiting = (Map)iter.next();
			// メール作成
			Mail mail = createNewMail(waiting);
			
			// 本文セット
			setContents(mail);
			
			// 送信先セット
			setToAddress(mail);
			
			// 添付ファイルセット
			attachFilePath(mail);
			
			// MIMEヘッダ情報セット
			setHeader(mail);
			
			// 送信　大量メール配信用のメソッドを使用
			sendMailExecute(mail);
			
			// 送信データ更新
			if (!updateSendFlag(mail)) {
				result = STATUS_ERROR;
			}
		}
		
		return STATUS_NORMAL;
	}

	/**
	 * 
	 * 概要: メールオブジェクト作成<br>
	 * 詳細: メールオブジェクト作成<br>
	 * 備考: なし<br>
	 *
	 * @param pstmt
	 * @param record
	 * @return
	 * @throws SQLException
	 */
	private Mail createNewMail(Map record) throws SQLException {
		Mail mail = new Mail(record.get("FROM_ADDR").toString(), record.get("SUBJECT").toString());
		
		mail.setMailId(record.get("MAIL_ID").toString());
		
		String fromName = (String)record.get("FROM_NAME");
		if (fromName != null) {
			mail.setFromName(fromName);
		}
		
		String reply = (String)record.get("REPLY_TO");
		if (reply != null) {
			mail.setReplyAddress(reply);
		}
		
		String replyName = (String)record.get("REPLY_NAME");
		if (replyName != null) {
			mail.setReplyName(replyName);
		}
		
		String attachFileId = (String)record.get("ATTACH_FILE_ID");
		if (attachFileId != null) {
			mail.setAttachFileId(attachFileId);
		}
		
		return mail;
	}
	
	private void setContents(Mail mail) throws SQLException {
		DBQuery query = new DBQuery(this.getClass(), "GetMailContents");
		Map record = query.executeQueryOneRow(new Object[]{ mail.getMailId() });

		StringBuffer contents = new StringBuffer();
		for (int i = 0; i < 10; i++) {
			String content = (String)record.get("CONTENTS0" + i);
			if (ConditionSupport.isExist(content)) {
				contents.append(content);
			}
		}
		mail.setHonbun(contents.toString());
	}
	
	/**
	 * 
	 * 概要: 送信先のセット<br>
	 * 詳細: 送信先のセット<br>
	 * 備考: なし<br>
	 *
	 * @param pstmt
	 * @param record
	 * @param mail
	 * @throws SQLException
	 */
	private void setToAddress(Mail mail) throws SQLException {
		DBQuery query = new DBQuery(this.getClass(), "GetSendAddressList");
		List<Map> result = query.executeQueryForList(new Object[]{ mail.getMailId() });
		
		for(Map record: result) {
			String to = (String)record.get("TO_ADDR");
			String toName = (String)record.get("TO_NAME");
			String type = (String)record.get("TO_TYPE");
			int intType = Integer.parseInt(type);
			
			MailDestination dest = new MailDestination(to, toName, intType);
			mail.addDestination(dest);
		}
	}
	
	/**
	 * 
	 * 概要: 添付ファイルセット<br>
	 * 詳細: 添付ファイルセット<br>
	 * 備考: なし<br>
	 *
	 * @param pstmt
	 * @param record
	 * @param mail
	 * @throws SQLException 
	 */
	private void attachFilePath(Mail mail) throws SQLException {
		DBQuery query = new DBQuery(this.getClass(), "GetAttachFiles");
		List<Map> result = query.executeQueryForList(new Object[]{ mail.getAttachFileId() });

		for(Map record: result) {
			String filePath = (String)record.get("ATTACH_FILE_PATH");
			File file = new File(filePath);
			if (file.exists()) {
				mail.addAttachFile(file);
			}
		}
	}
	
	/**
	 * 
	 * 概要: メールヘッダをセット<br>
	 * 詳細: メールヘッダをセット<br>
	 * 備考: なし<br>
	 *
	 * @param pstmt
	 * @param mail
	 * @throws SQLException
	 */
	private void setHeader(Mail mail) throws SQLException {
		DBQuery query = new DBQuery(this.getClass(), "GetMailHeader");
		List<Map> result = query.executeQueryForList(new Object[]{ mail.getMailId() });

		for(Map record: result) {
			String header = (String)record.get("MIME_HEADER");
			String value = (String)record.get("MIME_HEADER_VALUE");
			mail.addHeader(header, value);
		}
	}
	
	
	/**
	 * 
	 * 概要: <br>
	 * 詳細: <br>
	 * 備考: なし<br>
	 *
	 * @param pstmt
	 * @param id
	 * @param mail
	 * @return
	 * @throws SQLException
	 */
	private boolean updateSendFlag(Mail mail) throws SQLException {
		DBUpdate update = new DBUpdate(this.getClass(), "UpdateSendFlg");
		
		int result = update.execute(new Object[]{ mail.getMailId() });
		
		return mail.getDestination().length == result;
	}
	
	/**
	 * 
	 * 概要: メールの送信<br>
	 *       Transport#send(Message msg)を使用して、メールを一括送信する。<br>
	 * 　　　ただし一括送信時に不正なアドレスがあると、すべて送信不可になるので注意。<br>
	 * 詳細: <br>
	 * 備考: なし<br>
	 *
	 * @param mail
	 * @return
	 */
	private boolean sendMailExecute(Mail mail) {
		String smtpName	= SystemProperty.getSystemProperty("MAIL.SMTPServerName");
		String authUser		= SystemProperty.getSystemProperty("MAIL.AuthPassword");
		String authPassword	= SystemProperty.getSystemProperty("MAIL.AuthUser");
		if (smtpName == null) {
			logger.info("SMTPサーバのアドレスが取得できませんでした。");
			return false;
		}
		logger.info("SMTPサーバ >> " + smtpName);
		
		try {
			//smtpサーバー名を設定
			Session mailSession       = Session.getDefaultInstance(System.getProperties(), null);
			Properties mailProperties = mailSession.getProperties();
			mailProperties.put("mail.smtp.host", smtpName);	
			mailProperties.put("mail.smtp.auth", "false");
			mailProperties.put("mail.smtp.sendpartial", "true");
				
			MimeMessage msg = new MimeMessage(mailSession);
			
			//送信先アドレス
			MailDestination[] destAry = mail.getDestination();
			logger.info("送信件数 >> " + destAry.length);
			
			InternetAddress[] toList = getAddress(destAry, MailDestination.ADD_TYPE_TO);
			msg.setRecipients(Message.RecipientType.TO, toList);
			printLogSend("送信先(to) >> ", toList);
			
			InternetAddress[] ccList = getAddress(destAry, MailDestination.ADD_TYPE_CC);
			msg.setRecipients(Message.RecipientType.CC, ccList);
			printLogSend("送信先(cc) >> ", ccList);
			
			msg.setRecipients(Message.RecipientType.BCC, getAddress(destAry, MailDestination.ADD_TYPE_BCC));
			
			msg.setFrom(new InternetAddress(mail.getFromAddress(),MimeUtility.encodeWord(mail.getFromName(),"iso-2022-jp","B")));
			
			msg.setSubject(MimeUtility.encodeWord(mail.getKenmei(),"iso-2022-jp","B"),"iso-2022-jp");
			
			if (mail.getHeaders().keySet().size() > 0) {
				Map headers = mail.getHeaders();
				Iterator iter = headers.keySet().iterator();
				while (iter.hasNext()) {
					String headerName = (String)iter.next();
					String headerValue = (String)headers.get(headerName);
					msg.addHeader(headerName, headerValue);
				}
			}
			
			if (mail.getAttachFile().length > 0) {
				MimeMultipart content = new MimeMultipart();
				// 本文(テキスト部)作成
				MimeBodyPart bodyPart = new MimeBodyPart();
				bodyPart.setText(mail.getHonbun(), "iso-2022-jp");
				content.addBodyPart(bodyPart);
				// 添付ファイル部作成
				for (int i = 0; i < mail.getAttachFile().length; i++) {
					MimeBodyPart attachmentFile = new MimeBodyPart();
					File file = mail.getAttachFile()[i];

					FileDataSource fds = new FileDataSource(file);
					attachmentFile.setDataHandler(new DataHandler(fds));

					attachmentFile.setFileName(file.getName());
					content.addBodyPart(attachmentFile);
					logger.info("添付ファイル名 >> " + file.getName());
				}
				
				// メールにマルチパートメッセージ(添付ファイル)をセット
				msg.setContent(content);
			} else {
				//本文
				msg.setText(mail.getHonbun() ,"iso-2022-jp");
			
				//送信日時
				msg.setSentDate(new Date());
			}
			
			Transport trans = null;

			try {
				trans = mailSession.getTransport("smtp");
					
				trans.connect(smtpName, authUser, authPassword);
				
				trans.sendMessage(msg, msg.getAllRecipients());	

			} finally {
				if (trans != null) {
					trans.close();
				}
			}
			// メールの送信
			//Transport.send(msg);
				
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.exception(e);
			return false;
		} catch (MessagingException e) {
			e.printStackTrace();
			logger.exception(e);
			return false;
		}
		
		return true;
	}
	
	private InternetAddress[] getAddress(MailDestination[] destAry, int type) throws AddressException, UnsupportedEncodingException {
		List result = new LinkedList();
		for(MailDestination dest: destAry) {
			if (dest.getType() == type) {
				result.add(convertIA(dest));
			}
		}
		return (InternetAddress[])result.toArray(new InternetAddress[0]);
	}
	
	private InternetAddress convertIA(MailDestination dest) throws AddressException, UnsupportedEncodingException {
		return dest.getToName() == null ? 
			new InternetAddress(dest.getToAddress()) : 
			new InternetAddress(dest.getToAddress(), MimeUtility.encodeWord(dest.getToName(),"iso-2022-jp","B"));
	}
	
	private void printLogSend(String message, InternetAddress[] adds) {

		if (adds.length == 0) return;
		
		StringBuffer sb = new StringBuffer();
		sb.append(message);
		for(InternetAddress a: adds) {
			sb.append(a.getAddress());
			sb.append(",");
		}
		logger.debug(sb.toString());
	}

	public String getName() {
		return NAME;
	}

	public String getDescription() {
		StringBuffer sb = new StringBuffer();
		sb.append("定期的に実行され、メール送信テーブルより送信メールが存在すれば");
		sb.append("送信する。");
		
		return sb.toString();
	}
}
