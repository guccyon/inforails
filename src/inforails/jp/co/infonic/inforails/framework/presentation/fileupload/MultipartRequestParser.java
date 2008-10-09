package jp.co.infonic.inforails.framework.presentation.fileupload;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jp.co.infonic.common.util.ArrayUtil;
import jp.co.infonic.common.util.ConditionSupport;
import jp.co.infonic.common.util.FileUtil;
import jp.co.infonic.inforails.base.property.SystemProperty;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * 
 * 概要：ファイルアップロード時のリクエスト解析クラス<br>
 * 詳細：ファイルアップロード時のリクエスト解析クラス<br>
 * 
 * <pre>
 *  [変更履歴]
 *  日付        連番   名前      内容
 *  --------------------------------------------------
 *  2006/07/20  VL000  樋口      新規
 * </pre>
 * 
 * @author 樋口
 * @since 2006/07/20
 */
public class MultipartRequestParser {

	private static final String TMP_FILE_IDENTIFY = "$TMP_FILE_PATH$";
	
	private Map<String, File> successes = new HashMap<String, File>();
	
	private Map<String, Object> errors = new HashMap<String, Object>();
	
	public static File getTmpFile(Map params, String paramName) {
		return (File) params.get(paramName + TMP_FILE_IDENTIFY);
	}
	
	private static final Object ERROR_DUPLICATE_PARAM_NAME = new Object();
	private static final Object ERROR_SAVE_MISSING = new Object();
	private static final Object ERROR_INVALID_FILENAME = new Object();

	/**
	 * 概要: マルチパート形式で送信されたリクエストを解析し、リクエストパラメータを取得する。<br>
	 * 詳細: マルチパート形式で送信されたリクエストを解析し、リクエストパラメータを取得する。<br>
	 * 対象がファイルの場合、一時作業域にファイルを保存し、そのファイルへのフルパスを<br>
	 * CommonBeanにセットする。またオリジナルのファイル名は、パスをキーにCommonBeanにセットする。<br>
	 * 備考: なし<br>
	 * 
	 * @param request
	 * @return
	 * @throws Exception 
	 * @throws Exception
	 * @throws FileUploadException
	 * @throws IOException
	 * @since 2006/03/02
	 */
	public Map<String, Object> parse(HttpServletRequest request, String encode)
			throws Exception {

		Map<String, Object> result = new HashMap<String, Object>();

		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(4096);

		ServletFileUpload sfu = new ServletFileUpload(factory);
		sfu.setSizeMax(SystemProperty.uploadMaxSize());
		sfu.setHeaderEncoding(encode);
		
		for (Object obj : sfu.parseRequest(request)) {
			FileItem item = (FileItem) obj;
			String paramName = item.getFieldName();
			
			if (result.containsKey(paramName) && !item.isFormField()) {
				System.err.println("already exist paramName :" + paramName);
				errors.put(paramName, ERROR_DUPLICATE_PARAM_NAME);
				continue;
			}
			
			if (item.isFormField()) {
				parseFormField(result, item, paramName, encode);
			} else {
				parseFileField(result, item, paramName);
			}
		}
		return result;
	}
	
	public boolean isSuccess(String paramName) {
		return successes.containsKey(paramName);
	}

	public File getTmpFile(String paramName) {
		return successes.get(paramName);
	}
	
	public Map getErrorFiles() {
		return errors;
	}
	
	private void parseFormField(Map<String, Object> result, FileItem item,
			String paramName, String encode)
			throws UnsupportedEncodingException {
		
		if (result.containsKey(paramName) && 
				result.get(paramName) instanceof Object[]) {
			
			ArrayUtil.add((Object[])result.get(paramName), item.getString(encode));
			
		} else {
			result.put(paramName, item.getString(encode));
		}
	}
	
	private void parseFileField(Map<String, Object> result, FileItem item, String paramName) throws Exception {

		String fileName = new File(item.getName()).getName();
		if(!ConditionSupport.isExist(fileName)) {
			return;
		}
		if(!FileUtil.isCorrectFileName(fileName)) {
			errors.put(paramName, ERROR_INVALID_FILENAME);
			return;
		}

		String path = SerialFileIdGenerator.generatePath(getTmpDirectory().getPath());

		File tmpFile = new File(path);
		item.write(tmpFile);
		if (tmpFile.exists()) {
			result.put(paramName, fileName);
			result.put(paramName + TMP_FILE_IDENTIFY, tmpFile);
			successes.put(paramName, tmpFile);
		} else {
			errors.put(paramName, ERROR_SAVE_MISSING);
		}
	}

	private static File getTmpDirectory() {
		return FileUtil.mkdir(SystemProperty.fileUploadTmpDirectory());
	}
}