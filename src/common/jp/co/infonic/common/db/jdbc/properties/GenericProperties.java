package jp.co.infonic.common.db.jdbc.properties;

import jp.co.infonic.common.util.Assertion;
import jp.co.infonic.common.util.StringUtil;


public class GenericProperties implements ConnectPropeties {
	
	private String driverName;
	
	private String userName;
	
	private String password;
	
	private String url;
	
	public GenericProperties(String driverName, String url, String userName, String password) {
		this.driverName = driverName;
		this.userName = userName;
		this.password = password;
		this.url = url;
		valid();
	}
	
	private void valid() {
		Assertion.isExist(driverName);
		Assertion.isExist(url);
		Assertion.isExist(userName);
		this.password = StringUtil.interpret(this.password);
	}

	public String getDriverName() {
		return driverName;
	}

	public String getPassword() {
		return password;
	}

	public String getURL() {
		return url;
	}

	public String getUserName() {
		return userName;
	}
}
