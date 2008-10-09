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
 * �T�v�F���[�����M�o�b�`<br>
 * �ڍׁF���[�����M���e�[�u����薢���M���[���f�[�^��<br>
 * �@�@�@�擾���A���M���܂��B<br>
 *<pre>
 * [�ύX����]
 * ���t        �A��   ���O      ���e
 * --------------------------------------------------
 * 2006/09/02  VL000  ���      �V�K
 *</pre>
 * @author ���
 */
public class MailSendTask extends CnkBatch {
	
	private BatchLogger logger = BatchLogger.getLogger(this.getClass());
	
	private static final String NAME = "���[�����M�o�b�`";
	
	// �f�B�t�H���g���s�Ԋu�F�T��
	private static int DEFAULT_PERIOD = 5;
	
	public MailSendTask(int period) {
		super(period);
	}
	
	public MailSendTask() {
		this(DEFAULT_PERIOD);
	}

	protected int execute() throws SQLException {
		logger.debug("���[�����M���s���܂��B");
		int result = STATUS_NORMAL;
		
		DBQuery query = new DBQuery(this.getClass(), "GetWaitingMail");
		List waitingList = query.executeQueryForList(null);
		logger.debug("���M�҂����[���F"+ waitingList.size() + "��");
		if (waitingList.size() == 0) {
			return result;
		}

		Iterator iter = waitingList.iterator();
		while(iter.hasNext()) {
			Map waiting = (Map)iter.next();
			// ���[���쐬
			Mail mail = createNewMail(waiting);
			
			// �{���Z�b�g
			setContents(mail);
			
			// ���M��Z�b�g
			setToAddress(mail);
			
			// �Y�t�t�@�C���Z�b�g
			attachFilePath(mail);
			
			// MIME�w�b�_���Z�b�g
			setHeader(mail);
			
			// ���M�@��ʃ��[���z�M�p�̃��\�b�h���g�p
			sendMailExecute(mail);
			
			// ���M�f�[�^�X�V
			if (!updateSendFlag(mail)) {
				result = STATUS_ERROR;
			}
		}
		
		return STATUS_NORMAL;
	}

	/**
	 * 
	 * �T�v: ���[���I�u�W�F�N�g�쐬<br>
	 * �ڍ�: ���[���I�u�W�F�N�g�쐬<br>
	 * ���l: �Ȃ�<br>
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
	 * �T�v: ���M��̃Z�b�g<br>
	 * �ڍ�: ���M��̃Z�b�g<br>
	 * ���l: �Ȃ�<br>
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
	 * �T�v: �Y�t�t�@�C���Z�b�g<br>
	 * �ڍ�: �Y�t�t�@�C���Z�b�g<br>
	 * ���l: �Ȃ�<br>
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
	 * �T�v: ���[���w�b�_���Z�b�g<br>
	 * �ڍ�: ���[���w�b�_���Z�b�g<br>
	 * ���l: �Ȃ�<br>
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
	 * �T�v: <br>
	 * �ڍ�: <br>
	 * ���l: �Ȃ�<br>
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
	 * �T�v: ���[���̑��M<br>
	 *       Transport#send(Message msg)���g�p���āA���[�����ꊇ���M����B<br>
	 * �@�@�@�������ꊇ���M���ɕs���ȃA�h���X������ƁA���ׂđ��M�s�ɂȂ�̂Œ��ӁB<br>
	 * �ڍ�: <br>
	 * ���l: �Ȃ�<br>
	 *
	 * @param mail
	 * @return
	 */
	private boolean sendMailExecute(Mail mail) {
		String smtpName	= SystemProperty.getSystemProperty("MAIL.SMTPServerName");
		String authUser		= SystemProperty.getSystemProperty("MAIL.AuthPassword");
		String authPassword	= SystemProperty.getSystemProperty("MAIL.AuthUser");
		if (smtpName == null) {
			logger.info("SMTP�T�[�o�̃A�h���X���擾�ł��܂���ł����B");
			return false;
		}
		logger.info("SMTP�T�[�o >> " + smtpName);
		
		try {
			//smtp�T�[�o�[����ݒ�
			Session mailSession       = Session.getDefaultInstance(System.getProperties(), null);
			Properties mailProperties = mailSession.getProperties();
			mailProperties.put("mail.smtp.host", smtpName);	
			mailProperties.put("mail.smtp.auth", "false");
			mailProperties.put("mail.smtp.sendpartial", "true");
				
			MimeMessage msg = new MimeMessage(mailSession);
			
			//���M��A�h���X
			MailDestination[] destAry = mail.getDestination();
			logger.info("���M���� >> " + destAry.length);
			
			InternetAddress[] toList = getAddress(destAry, MailDestination.ADD_TYPE_TO);
			msg.setRecipients(Message.RecipientType.TO, toList);
			printLogSend("���M��(to) >> ", toList);
			
			InternetAddress[] ccList = getAddress(destAry, MailDestination.ADD_TYPE_CC);
			msg.setRecipients(Message.RecipientType.CC, ccList);
			printLogSend("���M��(cc) >> ", ccList);
			
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
				// �{��(�e�L�X�g��)�쐬
				MimeBodyPart bodyPart = new MimeBodyPart();
				bodyPart.setText(mail.getHonbun(), "iso-2022-jp");
				content.addBodyPart(bodyPart);
				// �Y�t�t�@�C�����쐬
				for (int i = 0; i < mail.getAttachFile().length; i++) {
					MimeBodyPart attachmentFile = new MimeBodyPart();
					File file = mail.getAttachFile()[i];

					FileDataSource fds = new FileDataSource(file);
					attachmentFile.setDataHandler(new DataHandler(fds));

					attachmentFile.setFileName(file.getName());
					content.addBodyPart(attachmentFile);
					logger.info("�Y�t�t�@�C���� >> " + file.getName());
				}
				
				// ���[���Ƀ}���`�p�[�g���b�Z�[�W(�Y�t�t�@�C��)���Z�b�g
				msg.setContent(content);
			} else {
				//�{��
				msg.setText(mail.getHonbun() ,"iso-2022-jp");
			
				//���M����
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
			// ���[���̑��M
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
		sb.append("����I�Ɏ��s����A���[�����M�e�[�u����著�M���[�������݂����");
		sb.append("���M����B");
		
		return sb.toString();
	}
}
