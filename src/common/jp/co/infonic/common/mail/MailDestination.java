package jp.co.infonic.common.mail;

/**
 * 
 * 概要：メールの送信先オブジェクト<br>
 * 詳細：メールの送信先オブジェクト
 *       通常の宛先もCCも当オブジェクトを利用します。<br>
 *<pre>
 * [変更履歴]
 * 日付        連番   名前      内容
 * --------------------------------------------------
 * 2006/09/02  VL000  樋口      新規
 *</pre>
 * @author 樋口
 */
public class MailDestination {
	
	public static final int ADD_TYPE_CC = 0;
	
	public static final int ADD_TYPE_TO = 1;
	
	public static final int ADD_TYPE_BCC = 2;
	
	// 宛先アドレス
	private String toAddress;
	
	// 宛先名
	private String toName;
	
	// 宛先タイプ　false: 通常の宛先(To) true: CC
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
