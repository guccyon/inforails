package jp.co.infonic.common.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;

/**
 * 
 * �T�v�F�r�p�k�I�u�W�F�N�g�Ǘ��N���X<br>
 * �ڍׁF�r�p�k�I�u�W�F�N�g�Ǘ��N���X<br>
 *       �r�p�k�𔭍s����N���X�Ɠ����p�X���.sql��<br>
 *       �z�u���邱�ƂŁA�g���q���������t�@�C�������L�[��<br>
 *       �r�p�k�t�@�C����ǂݍ���SqlObj�C���X�^���X�𐶐���<br>
 *       �Ԃ��܂��B<br>
 *       ��SQL�t�@�C���͕����R�[�h��Shift_JIS���w�肷��K�v������܂��B
 *       
 *<pre>
 * [�ύX����]
 * ���t        �A��   ���O      ���e
 * --------------------------------------------------
 * 2006/03/17  VL000  ���      �V�K
 *</pre>
 * @author higuchit
 */
public class SqlManager {
	
	// ��x�ǂݍ���SQL�̓L���b�V������
	private static Map sqlCache = new HashMap();
	
	// �����R�[�h
	private static String ENCODE = "Shift_JIS";
	
	/** �N�G����ʁF�s�� */
	public static final int TYPE_UNKNOWN = 0;
	
	/** �N�G����ʁF���� */
	public static final int TYPE_SELECT = 1;
	
	/** �N�G����ʁF�o�^ */
	public static final int TYPE_INSERT = 2;
	
	/** �N�G����ʁF�X�V  */
	public static final int TYPE_UPDATE = 3;
	
	/** �N�G����ʁF�폜  */
	public static final int TYPE_DELETE = 4;
	
	/**
	 * 
	 * �T�v: SQL�t�@�C���̕����R�[�h���w�肵�܂��B<br>
	 * �ڍ�: SQL�t�@�C���̕����R�[�h���w�肵�܂��B
	 *       �f�B�t�H���g�ł�Shift_JIS�ɂȂ�܂��B<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @param newEncode
	 * @throws UnsupportedEncodingException
	 */
    public static void setSqlFileEncode(String newEncode) throws UnsupportedEncodingException {
    	"dummy".getBytes(newEncode);
    	
    	ENCODE = newEncode;
    }
	
	/**
	 * 
	 * �T�v: �h�c�ɑΉ�����r�p�k�I�u�W�F�N�g��Ԃ��܂��B<br>
	 * �ڍ�: �h�c�ɑΉ�����r�p�k�I�u�W�F�N�g��Ԃ��܂��B<br>
	 *       �Y��SQL�t�@�C���͗��p�N���X�Ɠ����p�b�P�[�W��<br>
	 *       �z�u����K�v������܂��B<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @param selfClass
	 * @param sqlId
	 * @return
	 */
	public static SqlObj getSql(Class selfClass, String sqlId) {
		
		String id = selfClass.getPackage().getName() + "." + sqlId;
		
		id = id.replace('.', '/');
		
		
		if (!sqlCache.containsKey(id)) {
			InputStream in = selfClass.getClassLoader().getResourceAsStream(id + ".sql");
			if (in == null) {
				throw new MissingResourceException("�Ώۂ�SQL�t�@�C����������܂���B -> " + id, "", "");
			}
			
			try {
				SqlReader reader = new SqlReader(in);

				for(int i = 0;reader.hasNext();i++) {
					String sqlstr = reader.nextSQL();
					
					SqlObj sql = new SqlObj(sqlstr, id + "[" + i + "]");

					if (sql != null) {
						sqlCache.put(sql.getId(), sql);
						if (i == 0) sqlCache.put(id, sql);
					}
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return (SqlObj)sqlCache.get(id);
	}
	
	/**
	 * 
	 * �T�v: ���̓X�g���[������r�p�k�I�u�W�F�N�g���쐬���܂��B<br>
	 * �ڍ�: ���̓X�g���[������r�p�k�I�u�W�F�N�g���쐬���܂��B<br>
	 *       �����œn���ꂽ���̓X�g���[���͓����ŃN���[�Y����܂��B<br>
	 *       �ʏ��getSql(class, sqlId)�𗘗p���Ă��������B<br>
	 *       ���p�����ꍇ�A�r�p�k�I�u�W�F�N�g���L���b�V�������̂�<br>
	 *       �p�t�H�[�}���X�̌���ɂ��Ȃ���܂��B<br>
	 * ���l: �Ȃ�<br>
	 * @param in
	 * @return
	 * @throws IOException 
	 * @since 2006/04/15
	 */
	public static SqlObj getSql(InputStream in) throws IOException {
		SqlReader reader = new SqlReader(in, ENCODE);
		
		return reader.hasNext() ? new SqlObj(reader.nextSQL()) : null;
	}
	
	/**
	 * 
	 * �T�v: SQL�����񂩂璼�ڂr�p�k�I�u�W�F�N�g���쐬���܂��B<br>
	 * �ڍ�: SQL�����񂩂璼�ڂr�p�k�I�u�W�F�N�g���쐬���܂��B<br>
	 *       �ʏ��getSql(class, sqlId)�𗘗p���Ă��������B<br>
	 *       ���p�����ꍇ�A�r�p�k�I�u�W�F�N�g���L���b�V�������̂�<br>
	 *       �p�t�H�[�}���X�̌���ɂ��Ȃ���܂��B<br>
	 * ���l: �Ȃ�<br>
	 * @param query
	 * @return
	 * @since 2006/04/15
	 */
	public static SqlObj getSql(String query) {
		return new SqlObj(query);
	}
	
	/**
	 * 
	 * �T�v: ���̓X�g���[������SQL��ǂݍ��݁ASQLObj�C���X�^���X�𐶐�����B<br>
	 * �ڍ�: ���SQL�ɕ����̖��߂��L�q����Ă���ꍇ�ɓ����\�b�h�𗘗p���܂��B<br>
	 *       �����̖��߂̋�؂蕶���́A�s�̐擪��/(���p�X���b�V��)<br>
	 *       �܂��͈�̖��ߕ��̍Ō��;(�Z�~�R����)��u�����ŁA<br>
	 *       ���ߕ��̋�؂蕶���Ɖ��߂��A��؂�ꂽ������SqlObj�C���X�^���X���쐬��<br>
	 *       �z��ɂ��ĕԂ��܂��B<br>
	 * ���l: �Ȃ�<br>
	 *
	 * @param in
	 * @return
	 */
	public static SqlObj[] getSqls(InputStream in) {

		BufferedReader br = null;
		List list = new ArrayList();
		try {
			SqlReader reader = new SqlReader(in);
			while(reader.hasNext()) {
				list.add(new SqlObj(reader.nextSQL()));
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return (SqlObj[])list.toArray(new SqlObj[0]);
	}
}
