import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Transaction extends Thread{
	DBconnection data = new DBconnection();
	LockTable lock; 
	Connection local;
	Connection remote;
	Boolean flag = false;
	String city;
	double freightValue;
	SqlFile sql = new SqlFile();

	public Transaction(Connection localConnection,Connection remoteConnection,String cityName,double freightValue,LockTable lock) {
		this.local = localConnection;
		this.remote = remoteConnection;
		this.city = cityName;
		this.freightValue = freightValue;
		this.lock = lock;
	}

	public void run() {
		System.out.println(Thread.currentThread().getName()+" Started Performing Operation");
		updateCustomerTable();
		updateGeoLocationTable();
		updateFreightValue();
		deleteOrderForProduct();
		updateSellerTable();
		System.out.println(Thread.currentThread().getName()+" Finished Performing Operation");
		commitTransaction();
		System.out.println(Thread.currentThread().getName()+" Committed Transaction");
	}
	
	//Updating Customer City
	public void updateCustomerTable() {
		synchronized (this) {
			System.out.println("Updating CUSTOMERTABLE---------------------------");
			while(lock.getCustomerTable()) {
				System.out.println(Thread.currentThread().getName()+" Waiting for resource");
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
					closeConnection();
				}
			}
			lock.setCustomerTable(true);
			SqlFile sql = new SqlFile();
			List<HashMap<String, Object>> listOne = new ArrayList<HashMap<String, Object>>();
			ConvertResultSetToHashMap rsToList = new ConvertResultSetToHashMap();
			//Fetching Data from Customer Table
			try {
				System.out.println(Thread.currentThread().getName()+" selecting data from Customer Table");
				PreparedStatement ps = local.prepareStatement(sql.loadCustomer);
				ResultSet res = ps.executeQuery();
				listOne = rsToList.resultSetToArrayList(res);
			} catch (SQLException e1) {
				e1.printStackTrace();
				closeConnection();
			}
			//Updating record in the customer table
			for(HashMap<String, Object> record : listOne) {
				String customerId = record.get("customer_id").toString();
				try {
					PreparedStatement update = local.prepareStatement(sql.updateCustomer);
					update.setString(1, city);
					update.setString(2,customerId);
					System.out.println(Thread.currentThread().getName()+" Updating Customer Table");
					int value = update.executeUpdate();
					System.out.println(value+" record updated");
				} catch (SQLException e) {
					System.out.println("Update Operation is failed");
					e.printStackTrace();
					closeConnection();
					break;
				}
			}
			System.out.println(Thread.currentThread().getName()+" Successfully updated Customer Table");
		}
	}
	
	//Updating the Geolocation Table city
	public void updateGeoLocationTable() {
		synchronized (this) {
			System.out.println("Updating GEOLOCATION---------------------------");
			SqlFile sql = new SqlFile();
			System.out.println(Thread.currentThread().getName()+" performing operation on GeoLocation");
			while(lock.getLocationTable()) {
				System.out.println(Thread.currentThread().getName()+" waiting for resource GEOLOCATION");
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
					closeConnection();
				}
			}
			lock.setLocationTable(true);
			//Updating the City for certain Area
			try {
				PreparedStatement update = local.prepareStatement(sql.updateLocation);
				update.setString(1, city);
				System.out.println(Thread.currentThread().getName()+" updating GeoLocation Table");
				int value = update.executeUpdate();
				System.out.println(value+" record updated");
			} catch (SQLException e) {
				System.out.println("GeoLocation Update Operation is failed");
				e.printStackTrace();
				closeConnection();
			}
			System.out.println(Thread.currentThread().getName()+" successfully updated GeoLocation Table");
		}
	}
	
	//Update Freight Value for customer in certain area
	private void updateFreightValue() {
		synchronized (this) {
			System.out.println(Thread.currentThread().getName()+" performing operation on OrderItem Table");
			while(lock.getOrderItemsTable()) {
				System.out.println(Thread.currentThread().getName()+" waiting for Resource OrderItem Table");
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
					closeConnection();
				}
			}
			lock.setOrderItemsTable(true);
			List<HashMap<String, Object>> listOne = new ArrayList<HashMap<String, Object>>();
			ConvertResultSetToHashMap rsToList = new ConvertResultSetToHashMap();
			List<String> orderValue = new ArrayList<String>();
			//Selecting customer_id for zipcode '75265'
			try {
				System.out.println(Thread.currentThread().getName()+" selecing Customer Order Data");
				PreparedStatement ps = local.prepareStatement(sql.loadCustomerOrder);
				ResultSet res = ps.executeQuery();
				listOne = rsToList.resultSetToArrayList(res);
			} catch (SQLException e1) {
				e1.printStackTrace();
				closeConnection();
			}
			//For the customer_id corresponding order_id is selected
			for(HashMap<String, Object> record : listOne) {
				try {
					String customerId = record.get("customer_id").toString();
					System.out.println(Thread.currentThread().getName()+" selecting order_id from order table");
					PreparedStatement ps = local.prepareStatement(sql.loadOrder);
					ps.setString(1, customerId);
					ResultSet res = ps.executeQuery();
					if(res.next()) {
						orderValue.add(res.getString(1));
					}
				}catch (SQLException e1) {
					System.out.println(Thread.currentThread().getName()+" failed updating records from order Table");
					e1.printStackTrace();
					closeConnection();
				}
			}
			//Upadting records in orderItem
			for(String order : orderValue) {
				try {
					PreparedStatement update = remote.prepareStatement(sql.updateFreightVal);
					update.setDouble(1,freightValue);
					update.setString(2,order);
					System.out.println(Thread.currentThread().getName()+" Updating ORDER ITEMS table ");
					int value = update.executeUpdate();
					System.out.println(value+" record update");
				} catch (SQLException e) {
					System.out.println(" ORDER ITEMS Update Operation is failed");
					e.printStackTrace();
					closeConnection();
					break;
				}
			}
			System.out.println(Thread.currentThread().getName()+" Successfully Updated ORDER ITEMS Table");
		}

	}

	//Perfrom Delete Operation for specified Product_id
	private void deleteOrderForProduct() {
		synchronized (this) {
			System.out.println("Updating CUSTOMERTABLE---------------------------");
			System.out.println(Thread.currentThread().getName()+" performing operation for deleting records");
			while(lock.getOrderTable()) {
				System.out.println(Thread.currentThread().getName()+" waiting for Resource Order Table");
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
					closeConnection();
				}
			}
			lock.setOrderTable(true);
			SqlFile sql = new SqlFile();
			List<HashMap<String, Object>> listOne = new ArrayList<HashMap<String, Object>>();
			ConvertResultSetToHashMap rsToList = new ConvertResultSetToHashMap();
			//fetching data for a particular product
			try {
				System.out.println(Thread.currentThread().getName()+" performing select operation on Product Table");
				PreparedStatement ps = remote.prepareStatement(sql.loadProduct);
				ResultSet res = ps.executeQuery();
				listOne = rsToList.resultSetToArrayList(res);
			} catch (SQLException e1) {
				System.out.println(Thread.currentThread().getName()+" failed selecting records from Product Table");
				e1.printStackTrace();
				closeConnection();
			}
			//Deleting records in order table
			for(HashMap<String, Object> record : listOne) {
				try {
					String orderId = record.get("order_id").toString();
					System.out.println(Thread.currentThread().getName()+" deleting records from Order Table");
					PreparedStatement ps = local.prepareStatement(sql.deleteOrder);
					ps.setString(1, orderId);
					int value = ps.executeUpdate();
					System.out.println(value+"customer");
				}catch (SQLException e1) {
					System.out.println(Thread.currentThread().getName()+" failed deleting records from Order Table");
					e1.printStackTrace();
					closeConnection();
				}
			}
			//Deleting records in orderItem table
			for(HashMap<String, Object> record : listOne) {
				try {
					String orderId = record.get("order_id").toString();
					System.out.println(Thread.currentThread().getName()+" deleting records from Order Items Table");
					PreparedStatement ps = remote.prepareStatement(sql.deleteOrderItems);
					ps.setString(1, orderId);
					int value = ps.executeUpdate();
					System.out.println(value+"customer");
				}catch (SQLException e1) {
					System.out.println(Thread.currentThread().getName()+" failed deleting records from Order Items Table");
					e1.printStackTrace();
					closeConnection();
				}
			}
		}
		System.out.println(Thread.currentThread().getName()+" successfully completed delete operation");
	}

	//Performs Update Operation in Seller Table
	private void updateSellerTable() {
		synchronized (this) {
			System.out.println("Updating SELLERTABLE---------------------------");
			System.out.println(Thread.currentThread().getName()+" performing operation on Seller Table");
			while(lock.getSellerTable()) {
				System.out.println(Thread.currentThread().getName()+" waiting for resource Seller Table");
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
					closeConnection();
				}
			}
			lock.setSellerTable(true);
			SqlFile sql = new SqlFile();
			List<HashMap<String, Object>> listOne = new ArrayList<HashMap<String, Object>>();
			ConvertResultSetToHashMap rsToList = new ConvertResultSetToHashMap();
			//Fetching data from seller table
			try {
				System.out.println(Thread.currentThread().getName()+" Selecting data from Seller Table");
				PreparedStatement ps = remote.prepareStatement(sql.loadSeller);
				ResultSet res = ps.executeQuery();
				listOne = rsToList.resultSetToArrayList(res);
			} catch (SQLException e1) {
				System.out.println(" Selecting Seller Table Operation is failed");
				e1.printStackTrace();
				closeConnection();
			}
			//Updating zip code for the data fetched
			for(HashMap<String, Object> record : listOne) {
				String customerId = record.get("customer_id").toString();
				try {
					PreparedStatement update = local.prepareStatement(sql.updateSeller);
					update.setString(1, city);
					update.setString(2,customerId);
					System.out.println(Thread.currentThread().getName()+" Updating data on Seller Table");
					int value = update.executeUpdate();
					System.out.println(value+"customer");
				} catch (SQLException e) {
					System.out.println(" Updating Seller Table Operation is failed");
					e.printStackTrace();
					closeConnection();
					break;
				}
			}
			System.out.println(Thread.currentThread().getName()+" Seller Table Update Done");
		}
	}
	
	private void closeConnection() {
		try {
			System.out.println(Thread.currentThread().getName()+" Suspensed due to failure");
			local.rollback();
			remote.rollback();
			lock.setCustomerTable(false);
			lock.setLocationTable(false);
			lock.setOrderItemsTable(false);
			lock.setOrderTable(false);
			lock.setSellerTable(false);
			System.out.println(Thread.currentThread().getName()+" is rolled back");
			Thread.currentThread().suspend();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void commitTransaction() {
		try {
			local.commit();
			remote.commit();
			lock.setLocationTable(false);
			lock.setCustomerTable(false);
			lock.setOrderItemsTable(false);
			lock.setOrderTable(false);
			lock.setSellerTable(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
