package jp.co.infonic.inforails.framework.presentation.fileupload;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 
 * 概要：一時ファイル用シリアルID<br>
 * 詳細：<br>
 *<pre>
 * [変更履歴]
 * 日付        連番   名前      内容
 * --------------------------------------------------
 * 2006/07/20  VL000  樋口      新規
 *</pre>
 * @author 樋口
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
     * 01～99までの連番文字列を取得する
     * スレッドセーフ
     * @return 連番の文字列表現
     */
    private static synchronized String getNextval() {

        if (sequence > MAX_SEQUENCE) {
        	sequence = 0;
        }

        DecimalFormat df = new DecimalFormat("00");
        return df.format(sequence++);
    }
}
