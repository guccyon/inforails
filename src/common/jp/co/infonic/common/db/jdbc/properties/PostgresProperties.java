package jp.co.infonic.common.db.jdbc.properties;

import jp.co.infonic.common.module.ex.ExMap;
import jp.co.infonic.common.module.ex.ExString;

public class PostgresProperties extends GenericProperties {
	
	public static final String DRIVER = "org.postgresql.Driver";
	public static final String URL = "jdbc:postgresql://${HOST}:5432/${DATABASE}";

	public PostgresProperties(String host, String db, String userName, String password) {
		super(DRIVER, buildUrl(host, db), userName, password);
	}
	
	private static String buildUrl(String host, String db) {
		return new ExString(URL).tmplString(new ExMap<String,String>(
			new String[]{"HOST", "DATABASE"},	new String[]{host, db}
		));
	}
}
