package jp.co.infonic.common.db;

import java.sql.SQLException;

import jp.co.infonic.common.db.jdbc.DBConnection;
import jp.co.infonic.common.db.jdbc.DataSourceManager;

public abstract class ConnectionController {
	
	private boolean autoCommit = true;
	
	public ConnectionController() throws SQLException {
		execute();
	}
	
	public ConnectionController(boolean autoCommit) throws SQLException {
		this.autoCommit = autoCommit;
		execute();
	}
	
	protected void execute() throws SQLException {
		DBConnection conn = null;
		try {
			conn = DataSourceManager.getConnection(ConnectionController.class, autoCommit);
			
			delegate(conn);
			
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}

	protected abstract void delegate(DBConnection conn) throws SQLException;
}
