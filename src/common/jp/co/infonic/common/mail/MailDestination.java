package jp.co.infonic.common.mail;

/**
 * 
 * �T�v�F���[���̑��M��I�u�W�F�N�g<br>
 * �ڍׁF���[���̑��M��I�u�W�F�N�g
 *       �ʏ�̈����CC�����I�u�W�F�N�g�𗘗p���܂��B<br>
 *<pre>
 * [�ύX����]
 * ���t        �A��   ���O      ���e
 * --------------------------------------------------
 * 2006/09/02  VL000  ���      �V�K
 *</pre>
 * @author ���
 */
public class MailDestination {
	
	public static final int ADD_TYPE_CC = 0;
	
	public static final int ADD_TYPE_TO = 1;
	
	public static final int ADD_TYPE_BCC = 2;
	
	// ����A�h���X
	private String toAddress;
	
	// ���於
	private String toName;
	
	// ����^�C�v�@false: �ʏ�̈���(To) true: CC
	private int typeCC;
	
	MailDestination(String address, String name, int type) {
		
		toAddress = address;
		toName = name;
		typeCC = type;
	}

	
	public String getToAddress() {
		return toAddress;
	}

	public String getToName() {
		return toName;
	}

	public int getType() {
		return typeCC;
	}
}
