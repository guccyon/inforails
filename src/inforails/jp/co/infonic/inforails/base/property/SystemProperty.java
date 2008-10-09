package jp.co.infonic.inforails.base.property;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import jp.co.infonic.common.module.ex.ExArray;
import jp.co.infonic.common.module.ex.ExMap;
import jp.co.infonic.common.module.ex.ExString;
import jp.co.infonic.common.module.ex.TemplateText;
import jp.co.infonic.common.module.log.DebugLogger;
import jp.co.infonic.common.util.NumberUtil;
import jp.co.infonic.inforails.framework.presentation.control.process.ApplicationController;

import org.apache.log4j.Logger;

public class SystemProperty extends Properties {
	
	private static final long serialVersionUID = -8617916514603125594L;

	private static String RESOURCE_NAME = "application.properties";
	private static String DEFAULT_RESOURCE_NAME = "application.default.properties";
	
	// シングルトンインスタンス
	private static final SystemProperty i = new SystemProperty();
	
	private Logger logger = DebugLogger.getLogger(this.getClass());
	
	// コントローラー毎のプロパティを保持する
	private ExMap<String, ExMap<String, String>> ctrlerProps = new ExMap<String, ExMap<String, String>>();
	
	public static void changeResource(String resourceName) {
		RESOURCE_NAME = resourceName;
	}
	
	// クラスパス上のプロパティファイルを2分に一回リロードする。
	private static final int RELOAD_RATE = 1000 * 60 * 2;
	private Timer reloadTimer;
	
	private SystemProperty() {
		reloadTimer = new Timer();
		reloadTimer.schedule(new TimerTask(){
			public void run() {
				try {
					SystemProperty.i.reload();
				} catch (Exception e) {
					System.err.println("Update SystemProperty Missing");
				}
			}}, RELOAD_RATE, RELOAD_RATE);
	}
	
	public static void reloadCancel() {
		if (i.reloadTimer != null) {
			i.reloadTimer.cancel();
		}
	}
	
	public static void readProperty() {
		try {
			i.reload();
			for(Object key: i.keySet()) {
				i.logger.debug("[SystemProperty] key: " + key + " value: " + i.getProperty(key.toString()));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	
	@Override
	public String getProperty(String key) {
		return getRealValue(super.getProperty(key));
	}

	// フルネームキーでアクセス
	public static String get(String key) {
		return get(key, null);
	}

	// フルネームキーでアクセス
	public static String get(String key, String defaultValue) {
		return i.containsKey(key) ? i.getProperty(key) : defaultValue;
	}
	
	// コントローラーに関連付けされたキーに対応するプロパティ値を取得
	public static String getProperty(Class<? extends ApplicationController> clazz, String key) {
		return i.getRealValue(i.getProperties(ApplicationController.getId(clazz)).get(key));
	}
	
	// システムプロパティを取得
	public static String getSystemProperty(String key) {
		return i.getRealValue(i.getSystemProperties().get(key));
	}
	
	// システムプロパティをセット
	public static void setSystemProperty(String key, String value) {
		if(!i.getSystemProperties().containsKey(key)) {
			i.getSystemProperties().put(key, value);
		}
	}
	
	// コントローラーに関連付けされたプロパティ一覧を取得
	public static Map<String, String> getPropertiesForController(
			Class<? extends ApplicationController> clazz) {
		return i.getRealProperties(ApplicationController.getId(clazz));
	}

	
	public static String defaultCharset() {
		return i.getSystemProperties().get("DEFAULT.CHARSET");
	}
	
	public static String publicDirectory() {
		return "/public";
	}
	public static String contentsDir() {
		return "contents/";
	}
	
	public static String jspDirectory() {
		return contentsDir() + "jsp/";
	}
	
	public static String dataStore() {
		return getSystemProperty("DATABASE.STORE");
	}
	
	public static String fileUploadTmpDirectory() {
		return i.getSystemProperties().get("FILE.UPLOAD.TMP.DIR");
	}
	
	public static long uploadMaxSize() {
		String num = i.getSystemProperties().get("FILE.UPLOAD.MAXSIZE");
		return NumberUtil.GB(num != null ? Integer.parseInt(num) : 2);
	}
	
	
	
// ---------------------------------------------------------
	
	public synchronized void reload() throws IOException {
		
		Properties props = readTmpProps();
		
		checkReplaceVariable(props);
		
		// システムプロパティを上書かないよう退避
		Map <String, String> systemProperty = getSystemProperties();
		super.clear();
		ctrlerProps = new ExMap<String, ExMap<String, String>>();

		super.putAll(props);
		getSystemProperties().putAll(systemProperty);
		for(Map.Entry e: super.entrySet()) {
			String key = e.getKey().toString();
			ExArray<String> keys = new ExArray<String>(key.split("\\."));
			if (keys.last().isExist()) {
				getProperties(keys.first())
					.put(keys.last().join("."), e.getValue().toString());
			}
		}
	}
	
	// プロパティ値中に解決できない変数がないかチェック
	// 解決できない変数があった場合システムエラーを出力し処理を続ける
	private void checkReplaceVariable(final Properties props) {
		for(Object key: props.keySet()) {
			String value = props.getProperty(key.toString());
			for(String k: new TemplateText(value).findAll()) {
				if(!props.containsKey(k)) {
					System.err.println("SystemProperty#reload key:" 
							+ key + "にバインドされた値中の変数 " 
							+ k + " の定義が見つかりません。");
				}
			}
		}
	}
	
	private Properties readTmpProps() throws IOException {
		Properties props = new Properties();
		try {
			props.load(this.getClass().getClassLoader().getResourceAsStream(RESOURCE_NAME));
		} catch (Exception e) {
			
			if (e instanceof NullPointerException || e instanceof IOException) {
				System.out.println(RESOURCE_NAME + "が読み込めない為、デフォルトファイルを読み込みます");
				props.load(this.getClass().getClassLoader().getResourceAsStream(DEFAULT_RESOURCE_NAME));
			} else {
				throw new RuntimeException(e);
			}
		}
		return props;
	}
	 

	/** 変数間の参照値を実数値に置き換えます。
	 * 【概要】プロパティ値に${～～}で囲まれた値がある場合、変数とみなし、
	 * 同プロパティファイル内に定義された実際の値に置き換えます。
	 */
	private String getRealValue(String value) {
		return new ExString(value).tmplString(new ExString.Iterator<String>(){
			public String yield(String value) {	return i.getProperty(value);	}
		});
	}
	
	// コントローラー名に対応したプロパティセットを返す。
	private ExMap<String, String> getRealProperties(String ctrlerName) {
		final ExMap<String,String> result = new ExMap<String,String>();
		getProperties(ctrlerName).each(new ExMap.Iterator<String,String>(){
			public void yield(String k, String v) {
				result.put(k, getRealValue(v));
			}
		});
		return result;
	}
	
	private ExMap<String, String> getProperties(String ctrlerName) {
		if (!ctrlerProps.containsKey(ctrlerName))
			ctrlerProps.put(ctrlerName, new ExMap<String, String>());
		return ctrlerProps.get(ctrlerName);
	}
	
	private Map<String, String> getSystemProperties() {
		return getProperties("InfoRailsSystem");
	}
}
