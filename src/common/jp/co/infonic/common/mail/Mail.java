package jp.co.infonic.common.mail;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 
 * 概要：メールを表すオブジェクト<br>
 * 詳細：メールを表すオブジェクト
 *       送り先と添付ファイルは複数保持します。<br>
 *<pre>
 * [変更履歴]
 * 日付        連番   名前      内容
 * --------------------------------------------------
 * 2006/09/02  VL000  樋口      新規
 *</pre>
 * @author 樋口
 */
public class Mail {
	
	// 送信元アドレス
	private String fromAddress;
	
	// 送信元名
	private String fromName;
	
	// 返信先アドレス
	private String replyAddress;
	
	// 返信先名
	private String replyName;
	
	// 送り先
	private List destination;
	
	// 件名
	private String kenmei;
	
	// 本文
	private String honbun;
	
	// メールＩＤ
	private String mailId;
	
	// 添付ファイルリスト
	private List attachFile;
	
	private String attachFileId;
	
	private Map header;
	
	Mail(String from, String subject) {
		
		if (subject == null || from == null) {
			throw new IllegalArgumentException("パラメータを正しく設定してください。");
		}
		
		fromAddress = from;
		fromName = "";
		replyAddress = from;
		replyName = "";
		
		destination = new LinkedList();
		
		kenmei = subject;
		honbun = "";
		
		attachFile = new LinkedList();
		attachFileId = "";
		
		header = new HashMap();
	}
	
	/**
	 * 
	 * 概要: 送り先アドレスを追加します。<br>
	 * 詳細: 送り先アドレスを追加します。<br>
	 * 備考: なし<br>
	 *
	 * @param dest
	 */
	public void addDestination(MailDestination dest) {
		 destination.add(dest);
	}
	
	/**
	 * 
	 * 概要: 添付ファイルを追加します。<br>
	 * 詳細: 添付ファイルを追加します。<br>
	 * 備考: なし<br>
	 *
	 * @param file
	 */
	public void addAttachFile(File file) {
		attachFile.add(file);
	}
	
	/**
	 * 
	 * 概要: 送信元名をセットします。<br>
	 * 詳細: 送信元名をセットします。<br>
	 * 備考: なし<br>
	 *
	 * @param name
	 */
	public void setFromName(String name) {
		fromName = name;
	}
	
	/**
	 * 
	 * 概要: 返信先アドレスをセットします。<br>
	 * 詳細: 返信先アドレスをセットします。<br>
	 * 備考: なし<br>
	 *
	 * @param reply
	 */
	public void setReplyAddress(String reply) {
		replyAddress = reply;
	}
	
	/**
	 * 
	 * 概要: メールIDをセットします。<br>
	 * 詳細: メールIDをセットします。<br>
	 * 備考: なし<br>
	 *
	 * @param id
	 */
	public void setMailId(String id) {
		mailId = id;
	}

	public File[] getAttachFile() {
		return (File[])attachFile.toArray(new File[0]);
	}

	public MailDestination[] getDestination() {
		return (MailDestination[])destination.toArray(new MailDestination[0]);
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public String getFromName() {
		return fromName;
	}

	public String getHonbun() {
		return honbun;
	}
	
	public void setHonbun(String contents) {
		this.honbun = contents;
	}

	public String getKenmei() {
		return kenmei;
	}

	public String getMailId() {
		return mailId;
	}

	public String getReplyAddress() {
		return replyAddress;
	}

	public String getReplyName() {
		return replyName;
	}

	public void setReplyName(String replayName) {
		this.replyName = replayName;
	}

	String getAttachFileId() {
		return attachFileId;
	}

	void setAttachFileId(String attachFileId) {
		this.attachFileId = attachFileId;
	}
	
	public void addHeader(String headerName, String value) {
		this.header.put(headerName, value);
	}
	
	public Map getHeaders() {
		return header;
	}
}
