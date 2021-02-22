
public class SqlFile {

	public String loadCustomer = "Select * from e_commerece.customers where customer_zip_code_prefix =	'01151'";
	public String updateCustomer = "Update e_commerece.customers set customer_city=? where customer_id =?";
	public String updateLocation = "Update e_commerece.geolocation set geolocation_city=? where geolocation_zip_code_prefix ='01151'";
	public String insertCustomer = "Insert into e_commerece.customer values('inserting data','1151','Sao palo','SP')";
	public String loadCustomerOrder = "Select * from e_commerece.customers where customer_zip_code_prefix =	'75265'";
	public String loadOrder = "Select order_id from e_commerece.orders where customer_id = ?";
	public String updateFreightVal = "Update e_commerece.order_items set freight_value=? where order_id = ?";
	public String loadProduct = "Select order_id from e_commerece.order_items where product_id ='7634da152a4610f1595efa32f14722fc'";
	public String deleteOrderItems = "Delete from e_commerece.order_items where order_id = ?";
	public String deleteOrder = "Delete from e_commerece.orders where order_id=?";
	public String loadSeller = "Select * from e_commerece.sellers where seller_zip_code_prefix =	'01151'";
	public String updateSeller = "Update e_commerece.customers set seller_city=? where seller_id =?";
	public String loadProductDetails = "Select product_id from e_commerece.products where product_category_name = 'musica'";
	public String updateProdcutDetails = "Update e_commerece.products set product_height_cm = ? where product_id=?";
	
}
