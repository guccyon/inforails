package jp.co.infonic.common.db;



/**
 * 
 * �T�v�FSQL�t�@�C���I�u�W�F�N�g<br>
 * �ڍׁFSQL�t�@�C���ƂP�΂P�Ń}�b�s���O����܂��B<br>
 *<pre>
 * [�ύX����]
 * ���t        �A��   ���O      ���e
 * --------------------------------------------------
 * 2006/03/17  VL000  ���      �V�K
 *</pre>
 * @author higuchit
 */
public class SqlObj {
	
	// �t�@�C�����擾�����r�p�k��
	private String content;
	
	// ���t�@�C���̂h�c
	private String sqlId;
	
	private int queryType;
	
	public int getQueryType() {
		return queryType;
	}
	public void setQueryType(int queryType) {
		this.queryType = queryType;
	}
	/**
	 * �R���X�g���N�^
	 * @param query SQL������
	 */
	SqlObj(String query) {
		content = query;
		sqlId = "";
	}
	/**
	 * �R���X�g���N�^
	 * @param query SQL������
	 */
	SqlObj(String query, String id) {
		content = query;
		this.sqlId = id;
	}
	
	/**
	 * 
	 * �T�v: �r�p�k����Ԃ��܂��B<br>
	 * �ڍ�: �r�p�k����Ԃ��܂��B<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @return
	 */
	public String getSqlString() {
		return content.toString();
	}
	
	/**
	 * 
	 * �T�v: �r�p�k������ {n} ��u��������ɒu�������r�p�k����Ԃ��܂��B
	 * ���l: �Ȃ�<br>
	 *
	 * @param replace
	 * @return
	 */
	public String getSqlString(String[] replace) {

		return replace(content.toString(), replace);
	}
	
	/**
	 * 
	 * �T�v: �h�c��Ԃ��܂��B<br>
	 * �ڍ�: �h�c��Ԃ��܂��B<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @return
	 */
	public String getId() {
		return sqlId;
	}
	
	/**
	 * 
	 * �T�v: ��Q�����̒u���������SQL������{0},{1},({0} -> [0])�̕�����Ƃ��ꂼ��u������B<br>
	 * �ڍ�: ��Q�����̒u���������SQL������{0},{1},({0} -> [0])�̕�����Ƃ��ꂼ��u������B<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @param targetStr �Ώە�����
	 * @param replaceStr �u��������
	 * @return �u����̕�����
	 */
    private String replace(String targetStr, String[] replaceStr) {
    	
    	if (replaceStr == null || targetStr == null) {
    		throw new IllegalArgumentException("������null�ł��B arg1 :" + targetStr + " // arg2 :" + replaceStr);
    	}
    	
        for (int i = 0; i < replaceStr.length; i++) {
            String replace = "{" + (i) + "}";
            int index;
            while ((index = targetStr.indexOf(replace)) != -1) {
                String before = targetStr.substring(0, index);
                String left = targetStr.substring(index + replace.length());
                targetStr = before + replaceStr[i] + left;
            }
        }
        return targetStr;
    }
	
	public String toString() {
		return content.toString();
	}
}
