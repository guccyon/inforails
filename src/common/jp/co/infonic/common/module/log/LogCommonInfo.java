package jp.co.infonic.common.module.log;

/**
 * 
 * �T�v�F���O�o�͎��̋��ʏ���ێ����钊�ۃN���X<br>
 * �ڍׁF���O�o�͎��̋��ʏ���ێ����钊�ۃN���X<br>
 *<pre>
 * [�ύX����]
 * ���t        �A��   ���O      ���e
 * --------------------------------------------------
 * 2006/03/23  VL000  ���      �V�K
 *</pre>
 * @author higuchit
 */
public abstract class LogCommonInfo {
	
	protected static final String COMMA = ",";
	
	/**
	 * 
	 * �T�v: ���O�o�͎��̃��O���}���������Ԃ��B<br>
	 * �ڍ�: ���O�o�͎��̃��O���}���������Ԃ��B<br>
	 *       �ʏ�͏o�͂������t�B�[���h�����J���}��؂�Ōq����<br>
	 *       �Ԃ��B<br>
	 *       ���̎��A�쐬������̐擪�ƍŌ���ɃJ���}��t���Ă͂Ȃ�Ȃ��B<br>
	 *       �J���}�����͏o�͑��ōs�����̂Ƃ���B<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @return
	 */
	public abstract String toLogFormat();

}
