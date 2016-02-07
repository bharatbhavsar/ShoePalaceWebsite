package com.shoePalace.pojos;

import java.io.Serializable;

public class SendObjectClass implements Serializable{
	
	private String name;
	private Object shoe;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getShoe() {
		return shoe;
	}
	public void setShoe(Object shoe) {
		this.shoe = shoe;
	}
	
	
	
}
