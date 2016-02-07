package com.shoePalace.pojos;

import java.io.Serializable;

public class ProductListWithDescrition implements Serializable{
	
	private int productID;
	private String productBrand;
	private String productName;
	private String productImage;
	private float productPrize;
	private String productDescription;
	private String productSize;
	
	
	
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
	public float getProductPrize() {
		return productPrize;
	}
	public void setProductPrize(float productPrize) {
		this.productPrize = productPrize;
	}
	public String getProductDescription() {
		return productDescription;
	}
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	public String getProductSize() {
		return productSize;
	}
	public void setProductSize(String productSize) {
		this.productSize = productSize;
	}
	
	
	
	

}
