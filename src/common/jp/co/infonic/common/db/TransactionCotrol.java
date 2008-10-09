package jp.co.infonic.common.db;

import java.sql.SQLException;

import jp.co.infonic.common.db.jdbc.DBConnection;
import jp.co.infonic.common.db.jdbc.DBPreparedStatement;

public abstract class TransactionCotrol extends ConnectionController {

	public TransactionCotrol() throws SQLException {
		super(false);
	}
	
	private boolean status = true;
	
	private DBConnection conn;

	@Override
	protected final void delegate(DBConnection conn) throws SQLException {
		this.conn = conn;
		
		try {
			conn.transactionStart();
			
			transaction();
			
			if (status) {
				conn.commit();
			} else {
				conn.rollback();
			}
		} catch (SQLException sqe) {
			conn.rollback();
			throw sqe;
		} finally {
			conn = null;
		}
	}
	
	protected void update(SqlObj sql, Object[] params) throws SQLException {

		DBPreparedStatement pstmt = conn.prepareStatement(sql);
		if (params != null) pstmt.setBindDataAll(params);
		
		if (pstmt.executeUpdate() == 0) {
			status = false;
		}
	}
	
	protected void update(SqlObj sql) throws SQLException {
		update(sql, null);
	}
	
	protected abstract void transaction();
}
