import java.sql.Connection;
import java.sql.SQLException;

public class ControllerEngine {
	static DBconnection data = new DBconnection();
	static LockTable lock = new LockTable();

	public static void main(String args[]) throws SQLException, InterruptedException {

		Connection localOne = data.getLocalConnection();
		Connection remoteOne = data.getRemoteConnection();
		Connection localTwo = data.getLocalConnection();
		Connection remoteTwo = data.getRemoteConnection();
		Transaction t1 = new Transaction(localOne,remoteOne,"T1 City",100,lock);
		TransactionTwo t2 = new TransactionTwo(localTwo,remoteTwo,"T2 City",1200,2200,lock);
		t1.setName("Transaction 1");
		t2.setName("Transation 2");
		t1.start();
		t2.start();
		
	}
}
