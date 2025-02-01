package com.rays.pro4.Bean;

import java.util.Date;

public class OrderBean  extends BaseBean{
	private String productName;
	private Date orderDate;
	private int quantity;
	private int coustomer;
	

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getCoustomer() {
		return coustomer;
	}

	public void setCoustomer(int coustomer) {
		this.coustomer = coustomer;
	}

	@Override
	public String getkey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return null;
	}

}
