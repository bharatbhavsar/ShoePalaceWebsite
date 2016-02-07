package com.shoePalace.pojos;

import java.io.Serializable;

public class ProductList implements Serializable{
	private int productID;
	private String productBrand;
	private String productName;
	private String productImage;
	private int productPrize;
	
	
	public int getProductID() {
		return productID;
	}
	public void setProductID(int productID) {
		this.productID = productID;
	}
	public String getProductBrand() {
		return productBrand;
	}
	public void setProductBrand(String productBrand) {
		this.productBrand = productBrand;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductImage() {
		return productImage;
	}
	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}
	public int getProductPrize() {
		return productPrize;
	}
	public void setProductPrize(int productPrize) {
		this.productPrize = productPrize;
	}
	
	
	
}
