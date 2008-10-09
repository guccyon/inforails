package jp.co.infonic.inforails.framework.presentation.fileupload;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 
 * �T�v�F�ꎞ�t�@�C���p�V���A��ID<br>
 * �ڍׁF<br>
 *<pre>
 * [�ύX����]
 * ���t        �A��   ���O      ���e
 * --------------------------------------------------
 * 2006/07/20  VL000  ���      �V�K
 *</pre>
 * @author ���
 */
public class SerialFileIdGenerator {
	
	private static final int MAX_SEQUENCE = 99;
	
	private static int sequence = 0;
	
	public static String generatePath(String dir) {
		
		Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String ymd = sdf.format(cal.getTime());
		sdf.applyPattern("HHmmssSSS");
		String time = sdf.format(cal.getTime());
        
		String targetDir = dir + File.separator + ymd;
		if (!new File(targetDir).exists()) {
			new File(targetDir).mkdirs();
		}
		
        String fullpath = targetDir + File.separator + time + getNextval() + ".dat";
		
		return fullpath;
	}

    /**
     * 01�`99�܂ł̘A�ԕ�������擾����
     * �X���b�h�Z�[�t
     * @return �A�Ԃ̕�����\��
     */
    private static synchronized String getNextval() {

        if (sequence > MAX_SEQUENCE) {
        	sequence = 0;
        }

        DecimalFormat df = new DecimalFormat("00");
        return df.format(sequence++);
    }
}
