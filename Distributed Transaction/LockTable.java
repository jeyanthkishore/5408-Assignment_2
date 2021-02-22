
public class LockTable {
	private Boolean customerTable;
	private Boolean locationTable;
	private Boolean orderItemsTable;
	private Boolean orderTable;
	private Boolean sellerTable;
	private Boolean productsTable;
	
	public LockTable() {
		customerTable = false;
		locationTable = false;
		orderItemsTable = false;
		orderTable = false;
		sellerTable = false;
		productsTable = false;
	}

	public Boolean getCustomerTable() {
		return customerTable;
	}

	public void setCustomerTable(Boolean customerTable) {
		this.customerTable = customerTable;
	}

	public Boolean getLocationTable() {
		return locationTable;
	}

	public void setLocationTable(Boolean locationTable) {
		this.locationTable = locationTable;
	}

	public Boolean getOrderItemsTable() {
		return orderItemsTable;
	}

	public void setOrderItemsTable(Boolean orderTable) {
		this.orderItemsTable = orderTable;
	}

	public Boolean getOrderTable() {
		return orderTable;
	}

	public void setOrderTable(Boolean orderTable) {
		this.orderTable = orderTable;
	}

	public Boolean getSellerTable() {
		return sellerTable;
	}

	public void setSellerTable(Boolean sellerTable) {
		this.sellerTable = sellerTable;
	}

	public Boolean getProductsTable() {
		return productsTable;
	}

	public void setProductsTable(Boolean productsTable) {
		this.productsTable = productsTable;
	}
}
