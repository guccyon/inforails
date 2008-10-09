package jp.co.infonic.common.mail;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 
 * �T�v�F���[����\���I�u�W�F�N�g<br>
 * �ڍׁF���[����\���I�u�W�F�N�g
 *       �����ƓY�t�t�@�C���͕����ێ����܂��B<br>
 *<pre>
 * [�ύX����]
 * ���t        �A��   ���O      ���e
 * --------------------------------------------------
 * 2006/09/02  VL000  ���      �V�K
 *</pre>
 * @author ���
 */
public class Mail {
	
	// ���M���A�h���X
	private String fromAddress;
	
	// ���M����
	private String fromName;
	
	// �ԐM��A�h���X
	private String replyAddress;
	
	// �ԐM�於
	private String replyName;
	
	// �����
	private List destination;
	
	// ����
	private String kenmei;
	
	// �{��
	private String honbun;
	
	// ���[���h�c
	private String mailId;
	
	// �Y�t�t�@�C�����X�g
	private List attachFile;
	
	private String attachFileId;
	
	private Map header;
	
	Mail(String from, String subject) {
		
		if (subject == null || from == null) {
			throw new IllegalArgumentException("�p�����[�^�𐳂����ݒ肵�Ă��������B");
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
	 * �T�v: �����A�h���X��ǉ����܂��B<br>
	 * �ڍ�: �����A�h���X��ǉ����܂��B<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @param dest
	 */
	public void addDestination(MailDestination dest) {
		 destination.add(dest);
	}
	
	/**
	 * 
	 * �T�v: �Y�t�t�@�C����ǉ����܂��B<br>
	 * �ڍ�: �Y�t�t�@�C����ǉ����܂��B<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @param file
	 */
	public void addAttachFile(File file) {
		attachFile.add(file);
	}
	
	/**
	 * 
	 * �T�v: ���M�������Z�b�g���܂��B<br>
	 * �ڍ�: ���M�������Z�b�g���܂��B<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @param name
	 */
	public void setFromName(String name) {
		fromName = name;
	}
	
	/**
	 * 
	 * �T�v: �ԐM��A�h���X���Z�b�g���܂��B<br>
	 * �ڍ�: �ԐM��A�h���X���Z�b�g���܂��B<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @param reply
	 */
	public void setReplyAddress(String reply) {
		replyAddress = reply;
	}
	
	/**
	 * 
	 * �T�v: ���[��ID���Z�b�g���܂��B<br>
	 * �ڍ�: ���[��ID���Z�b�g���܂��B<br>
	 * ���l: �Ȃ�<br>
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
