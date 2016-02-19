import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DButils {

	private static final String DRIVE_PATH = "com.mysql.jdbc.Driver";
	private static final String URL = "jdbc:mysql://"+newServer.DB_HOST+":"+newServer.DB_PORT+"/"+newServer.DB_NAME;
	private static final String USER = newServer.DB_USER;
	private static final String PASSWORD = newServer.DB_PASS;

	static {
		try {
			Class.forName(DRIVE_PATH);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ȡ���ݿ�����
	 *
	 * @return
	 * @throws SQLException
	 */
	public static final Connection getConnection(boolean isReadOnly)
			throws SQLException {

		Connection connection = DriverManager
				.getConnection(URL, USER, PASSWORD);
		//
		connection.setAutoCommit(false);
		//
		connection.setReadOnly(isReadOnly);
		return connection;
	}

	public static final Connection getConnection() throws SQLException {
		return getConnection(false);
	}

	/**
	 * �ر�����
	 *
	 * @param connection
	 */
	public static final void close(Connection connection) throws SQLException {
		if (connection != null) {
			connection.close();
		}
	}

	/**
	 * �رս����
	 *
	 * @param resultSet
	 */
	public static final void close(ResultSet resultSet) throws SQLException {
		if (resultSet != null) {
			resultSet.close();
		}
	}

	/**
	 * �رս����
	 *
	 * @param resultSet
	 */
	public static final void close(ResultSet resultSet, Statement statement)
			throws SQLException {
		if (resultSet != null) {
			resultSet.close();
		}
	}

}
