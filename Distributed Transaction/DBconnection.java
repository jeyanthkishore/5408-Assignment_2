
import java.sql.Connection;
import java.sql.DriverManager;

public class DBconnection {
	Connection connect;
	public Connection getLocalConnection() {
		try
		{
		String userName = "root";
		String password = "Jeyanth3#";
		String url = "jdbc:mysql://127.0.0.1";
		connect = DriverManager.getConnection (url, userName, password);
		System.out.println ("Database connection established" +connect);
		connect.setAutoCommit(false);
		}
		catch (Exception e)
		{
		System.err.println ("Cannot connect to database server");
		}
		return connect;
	}
	public Connection getRemoteConnection() {
		try
		{
		String userName = "root";
		String password = "Jeyanth3#";
		String url = "jdbc:mysql://35.200.237.183";
		connect = DriverManager.getConnection (url, userName, password);
		System.out.println ("Database connection established" +connect);
		connect.setAutoCommit(false);
		}
		catch (Exception e)
		{
		System.err.println ("Cannot connect to database server");
		}
		return connect;
	}

}
