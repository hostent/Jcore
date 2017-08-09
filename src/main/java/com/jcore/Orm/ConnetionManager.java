package com.jcore.Orm;

import java.sql.Connection;
import java.sql.SQLException;

import com.jcore.Frame.Action;
import com.jcore.Frame.Log;

public final class ConnetionManager {

	static ThreadLocal<Connection> currentConnMap = null;

	static ThreadLocal<String> inTran;

	public static Connection getConn(String key) {

		Connection conn = null;

		try {

			if (isInTran()) // 事务中
			{
				if (currentConnMap != null && currentConnMap.get() != null) {
					conn = currentConnMap.get();
					return conn;
				}

				if (currentConnMap == null) {
					currentConnMap = new ThreadLocal<Connection>();
				}

				conn = (Connection) DbPoolConnection.getInstance().getConnection(key);
				conn.setAutoCommit(false);
				currentConnMap.set(conn);

			} else {
				conn = (Connection) DbPoolConnection.getInstance().getConnection(key);
			}

			return conn;
		} catch (SQLException e) {
			Log.logError(e);
			return null;
		}

	}

	public static void close(Connection conn) {
		if (isInTran()) {
			return;
		}
		try {
			if (!conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			Log.logError(e);
		}
	}

	static void disposeThreadLocale() {

		if (inTran != null) {
			inTran.set("");
			inTran.remove();

		}
		if (currentConnMap != null) {
			currentConnMap.set(null);
			currentConnMap.remove();
		}
	}

	public static void tran(String key, Action act) {
		try {

			if (inTran != null && (!inTran.get().equals(key))) {
				throw new Exception("不能同时开启2种数据库事务");
			}

			if (inTran == null) {
				inTran = new ThreadLocal<String>();
			}

			inTran.set(key);

			act.doWork();

			ConnetionManager.commit();

		} catch (Exception e) {
			ConnetionManager.rollback();
		}
	}

	public static boolean rollback() {
		if (!isInTran()) {
			return false;
		}
		if (currentConnMap == null || currentConnMap.get() == null) {
			return false;
		}
		Connection con = currentConnMap.get();

		try {
			con.rollback();
		} catch (SQLException e) {
			Log.logError(e);
		} finally {
			disposeThreadLocale();
			close(con);
		}

		return true;

	}

	public static boolean commit() {
		if (!isInTran()) {
			return false;
		}
		if (currentConnMap == null || currentConnMap.get() == null) {
			return false;
		}
		Connection con = currentConnMap.get();

		try {
			con.commit();
		} catch (SQLException e) {
			Log.logError(e);
		} finally {
			disposeThreadLocale();
			close(con);
		}

		return true;

	}

	public static boolean isInTran() {
		if (inTran == null || inTran.get() == null || inTran.get().isEmpty()) {
			return false;
		}

		return true;
	}

}
