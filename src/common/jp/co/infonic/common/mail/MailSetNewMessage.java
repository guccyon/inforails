package jp.co.infonic.common.mail;

import java.io.File;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jp.co.infonic.common.db.SqlManager;
import jp.co.infonic.common.db.SqlObj;
import jp.co.infonic.common.db.jdbc.DBConnection;
import jp.co.infonic.common.db.jdbc.DBPreparedStatement;
import jp.co.infonic.common.db.jdbc.DataSourceManager;
import jp.co.infonic.common.module.log.DebugLogger;
import jp.co.infonic.inforails.base.exception.SystemException;

import org.apache.log4j.Logger;

/**
 * 
 * 概要：新規メールをセットするクラス<br>
 * 詳細：<br>
 * 
 * <pre>
 *  [変更履歴]
 *  日付        連番   名前      内容
 *  --------------------------------------------------
 *  2006/10/05  VL000  樋口      新規
 * </pre>
 * 
 * @author 樋口
 */
public class MailSetNewMessage {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

	private static Logger logger = DebugLogger
			.getLogger(MailSetNewMessage.class);

	/**
	 * 
	 * 概要: 直ぐにメールを送信する<br>
	 * 詳細: 直ぐにメールを送信する<br>
	 * 実際にはメールバッチの実行間隔でタイムラグがあります。<br>
	 * 備考: なし<br>
	 * 
	 * @param mail
	 * @return
	 */
	public static boolean setNewMessage(Mail mail) {

		return setNewMessage(mail, Calendar.getInstance(), 5);
	}

	public static boolean setNewMessage(Mail mail, Calendar sendDate) {

		return setNewMessage(mail, sendDate, 5);
	}

	/**
	 * 
	 * 概要: 送信時間を指定でメールをセットする。<br>
	 * 詳細: 送信時間を指定でメールをセットする。<br>
	 * 送信時間はカレンダーオブジェクトで指定します。<br>
	 * メールをセットすると設定時間以降の最初のメール送信バッチ 起動時にメールが送信されます。<br>
	 * 備考: なし<br>
	 * 
	 * @param mail
	 *            メールオブジェクト
	 * @param sendDate
	 * @param priority
	 * @return
	 */
	public static boolean setNewMessage(Mail mail, Calendar sendDate,
			int priority) {

		if (priority < 0 || priority > 9) {
			throw new IllegalArgumentException("優先度は0～9で指定してください。");
		}
		DBConnection conn = null;
		try {
			conn = DataSourceManager.getConnection(MailSetNewMessage.class, false);
			conn.transactionStart();

			// 添付ファイル情報のセット
			String attachId = insertAttachFile(conn, mail);

			// メールメッセージ情報のセット
			int result = insertNewMailMessage(conn, mail, attachId);

			if (result == 0) {
				String info = "メールを登録するのに失敗しました。";
				throw new SystemException(null, "MailSetNewMessage",
						"insertNewMailMessage", info, true, info);
			}
			insertMailHeader(conn, mail);

			Date date = new Date(sendDate.getTime().getTime());
			// 送信先アドレス情報セット
			result = insertMailAddress(conn, mail, String.valueOf(priority),
					date);

			conn.commit();

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error(e);
			}
			throw new SystemException(e, "MailSetNewMessage", "setNewMessage",
					"メールの登録に失敗しました。");
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error(e);
				}
			}
		}

		return false;
	}

	private static final int CONTENT_LENGTH_PER_ONE = 2000;


	public static String nextValue(DBConnection conn, String sequenceName) throws SQLException {
		SqlObj sql = SqlManager.getSql("SELECT NEXT VALUE FOR "+ sequenceName +" ID FROM DUAL");
		
		DBPreparedStatement pstmt = conn.prepareStatement(sql);
		List list = pstmt.executeQueryForList(null);
		if (list.size() > 0) {
			Map record = (Map) list.get(0);
			return record.get("ID").toString();
		} else {
			throw new RuntimeException("");
		}
	}
	
	/**
	 * 
	 * 概要: メールメッセージテーブルにメールをセットする。<br>
	 * 詳細: <br>
	 * 備考: なし<br>
	 * 
	 * @param conn
	 * @param mail
	 * @param attachId
	 * @return
	 * @throws SQLException
	 */
	private static int insertNewMailMessage(DBConnection conn, Mail mail,
			String attachId) throws SQLException {
		String newId = nextValue(conn, "MAILID_SEQUENCE");
		mail.setMailId(newId);

		SqlObj sql = SqlManager.getSql(MailSetNewMessage.class, "SetNewMail");
		DBPreparedStatement pstmt = null;
		try {
			List paramList = new LinkedList();

			paramList.add(newId);
			paramList.add(mail.getKenmei());
			paramList.add(attachId);

			// 1カラム当たりの文字数が２０００文字までの為、
			// セットされた本文を２０００文字毎に分解して
			// パラメータにセット
			for (int i = 0; i < 10; i++) {
				int startIndex = i * CONTENT_LENGTH_PER_ONE;
				int endIndex = (i + 1) * CONTENT_LENGTH_PER_ONE;

				if (mail.getHonbun().length() > startIndex) {
					if (mail.getHonbun().length() < endIndex) {
						paramList.add(mail.getHonbun().substring(startIndex));
					} else {
						paramList.add(mail.getHonbun().subSequence(startIndex,
								endIndex));
					}
				} else {
					paramList.add("");
				}
			}

			pstmt = conn.prepareStatement(sql);
			pstmt.setBindDataAll(paramList);
			return pstmt.executeUpdate();

		} finally {
			if (pstmt != null) {
				pstmt.close();
			}
		}
	}

	/**
	 * 
	 * 概要: メールヘッダをセットします。<br>
	 * 詳細: メールヘッダをセットします。<br>
	 * 備考: なし<br>
	 *
	 * @param conn
	 * @param mail
	 * @return
	 * @throws SQLException
	 */
	private static boolean insertMailHeader(DBConnection conn, Mail mail)
			throws SQLException {
		int result = 0;
		Map map = mail.getHeaders();
		int keysize = map.keySet().size();
		if (keysize == 0) {
			return false;
		}
		Iterator iter = map.keySet().iterator();
		DBPreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(SqlManager.getSql(
					MailSetNewMessage.class, "SetMailHeader"));
			while (iter.hasNext()) {
				String header = (String) iter.next();
				String value = (String) map.get(header);

				pstmt.setBindDataAll(new Object[] { mail.getMailId(), header,
						value });
				result += pstmt.executeUpdate();
			}
		} finally {
			if (pstmt != null) {
				pstmt.close();
			}
		}
		return result == keysize;
	}

	/**
	 * 
	 * 概要: メール送信先アドレステーブルに送信データを登録する。<br>
	 * 詳細: <br>
	 * 備考: なし<br>
	 * 
	 * @param conn
	 * @param mail
	 * @param priority
	 * @param date
	 * @return
	 * @throws SQLException
	 */
	private static int insertMailAddress(DBConnection conn, Mail mail,
			String priority, Date date) throws SQLException {
		int result = 0;
		SqlObj sql = SqlManager.getSql(MailSetNewMessage.class,
				"SetMailAddress");
		DBPreparedStatement pstmt = null;
		try {
			MailDestination[] dest;
			//if (SystemInfo.isDebugMode()) {
			//	dest = getTestMailDetination();
			//} else {
//
//			}
			dest = mail.getDestination();

			pstmt = conn.prepareStatement(sql);

			Object[] paramAry = new Object[11];
			paramAry[0] = mail.getMailId();

			for (int i = 0; i < dest.length; i++) {
				paramAry[1] = dest[i].getToAddress();
				paramAry[2] = chkNull(dest[i].getToName());
				paramAry[3] = dest[i].getType() + "";

				if (i == 0) {
					int index = 4;
					paramAry[index++] = mail.getFromAddress();
					paramAry[index++] = chkNull(mail.getFromName());
					paramAry[index++] = mail.getReplyAddress();
					paramAry[index++] = chkNull(mail.getReplyName());
					paramAry[index++] = priority;
					paramAry[index++] = date;
					paramAry[index++] = "0";
				}
				pstmt.setBindDataAll(paramAry);
				int tmp = pstmt.executeUpdate();
				if (tmp == 1) {
					result++;
				}
			}

		} finally {
			if (pstmt != null) {
				pstmt.close();
			}
		}

		return result;
	}

	/**
	 * 
	 * 概要: メール添付ファイルテーブルに添付ファイル情報をセットする。<br>
	 * 詳細: <br>
	 * 備考: なし<br>
	 * 
	 * @param conn
	 * @param mail
	 * @return
	 * @throws SQLException
	 */
	private static String insertAttachFile(DBConnection conn, Mail mail)
			throws SQLException {
		File[] files = mail.getAttachFile();
		if (files != null && files.length > 0) {
			String newId = createNewMailAttachId(conn);

			SqlObj sql = SqlManager.getSql(MailSetNewMessage.class,
					"SetMailAttach");
			DBPreparedStatement pstmt = null;
			try {
				pstmt = conn.prepareStatement(sql);

				for (int i = 0; i < files.length; i++) {
					pstmt.setBindDataAll(new Object[] { newId,
							files[i].getPath() });
					int reuslt = pstmt.executeUpdate();
					if (reuslt == 0) {
						logger.debug("添付ファイルのセットに失敗しました。 mailId:"
								+ mail.getMailId() + " path:"
								+ files[i].getPath());
					}
				}

			} finally {

				pstmt.close();
			}

			return newId;
		} else {
			return "";
		}
	}

	/**
	 * 
	 * 概要: <br>
	 * 詳細: <br>
	 * 備考: なし<br>
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	private static String createNewMailAttachId(DBConnection conn)
			throws SQLException {
		SqlObj sql = SqlManager
				.getSql("SELECT MAIL_ATTACH_ID_SEQUENCE.NEXTVAL ID FROM DUAL");
		DBPreparedStatement pstmt = conn.prepareStatement(sql);
		List list = pstmt.executeQueryForList(null);
		if (list.size() > 0) {
			Map record = (Map) list.get(0);
			return record.get("ID").toString();
		} else {
			throw new RuntimeException("");
		}
	}

	private static String chkNull(String value) {
		if (value == null) {
			return "";
		} else {
			return value;
		}
	}
}
