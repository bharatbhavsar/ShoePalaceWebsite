package com.shoePalace.pojos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;



public class DatabaseConnection {
	
	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	private ResultSet rs1 = null;
	private ResultSet rs2 = null;
	private java.sql.Date currentDate;
	
	public DatabaseConnection()
	{
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "root");
			
			stmt = conn.createStatement();
			stmt.execute("use ShoePalace;");
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	public boolean checkCustomerDetails(String username, String password)
	{
		boolean usernameMatch = false;
		boolean passwordMatch = false;
		boolean result = false;
		try {
			
			
			
			
			rs = stmt.executeQuery("select Email, Password from Customer_Information where Email = \""+username+"\";");
			
			
			int count =0;
			
			while(rs.next())
			{
				if(username.equals(rs.getString("Email")))
				{
					usernameMatch = true;
				}
				if(password.equals(rs.getString("password")))
				{
					passwordMatch = true;
				}
				
				count++;
			}
			
			if(count>0)
			{
				
				if(usernameMatch && passwordMatch)
			
				{	
					
					result = true;
					
				}
			}
			else
			{
				
				result = false;
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} 
		
		
		
		return result;
		
	}
	
	public String newUserDetails(UserDetails ud)
	{
		try {
			rs = stmt.executeQuery("select Email from Customer_Information where Email = \""+ud.getEmailAddress()+"\";");
			while(rs.next())
			{
				if(ud.getEmailAddress().equals(rs.getString("Email")));
				{
					return "Username already exists";
				}
			}
			System.out.println(ud.getPassword());
			
			
			PreparedStatement preparedStatement = conn.prepareStatement("Insert into Customer_Information (Email,Name,Password) values (?,?,?);");
			preparedStatement.setString(1, ud.getEmailAddress());
			preparedStatement.setString(2, ud.getName());
			preparedStatement.setString(3, ud.getPassword());
			preparedStatement.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Register unsuccessful";
		}
		
		
		return "Successfully Register";
	}
	
	
	
	
	//public ArrayList<UserLogin> ProductListAll()
	public SendObjectClass ProductListAll()
	{
		ArrayList<ProductList> productList = new ArrayList<ProductList>();
		
		
		ProductList pl = null;
		
		String name = null;
		SendObjectClass so = new SendObjectClass();
		
		try {
			rs = stmt.executeQuery("select Product_ID, Product_Brand, Product_Name, Product_Image, Product_Price from Product_Details;");
			while(rs.next())
			{
				pl = new ProductList();
				
				pl.setProductID(rs.getInt("Product_ID"));
				pl.setProductBrand(rs.getString("Product_Brand"));
				pl.setProductName(rs.getString("Product_Name"));
				pl.setProductImage(rs.getString("Product_Image"));
				pl.setProductPrize(rs.getInt("Product_Price"));
				
				productList.add(pl);
					
			}
			
			
			so.setName(name);
			so.setShoe(productList);
			
			
			return so;
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//return productList;
		return so;
		
	}
	
	public SendObjectClass ProductListOneItem(int productID)
	{
		ArrayList<ProductListWithDescrition> productList = new ArrayList<ProductListWithDescrition>();
		
		ProductListWithDescrition pl = null; 
		boolean comma = true;
		String productSize = new String();
		String productQuantity = new String();
		String name = null;
		SendObjectClass so = new SendObjectClass();
		
		try {
			rs = stmt.executeQuery("select Product_ID, Product_Brand, Product_Name, Product_Image, Product_Price, Product_Description from Product_Details where Product_ID = "+productID+";");
			
			while(rs.next())
			{
				
				pl = new ProductListWithDescrition();
				
				pl.setProductID(rs.getInt("Product_ID"));
				pl.setProductBrand(rs.getString("Product_Brand"));
				pl.setProductName(rs.getString("Product_Name"));
				pl.setProductImage(rs.getString("Product_Image"));
				pl.setProductPrize(rs.getInt("Product_Price"));
				pl.setProductDescription(rs.getString("Product_Description"));
			}
			
				rs1 = stmt.executeQuery("select Product_ID, Product_Size from Product_size_quantity_Detail where Product_ID = "+ pl.getProductID() +";");
				
				while(rs1.next())
				{
					if(!comma)
					{
						productSize = productSize + ",";
						
					}
					
					productSize = productSize + Integer.toString(rs1.getInt("Product_Size"));
					
					comma = false;
				}
				
				
				
				
				
				pl.setProductSize(productSize);
				
				productList.add(pl);
				
				so.setName(name);
				so.setShoe(productList);
				
				
			
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return so;
		
	}
	
	
	public boolean submitCustomerOrder(CustomerOrderSubmit co)
	{
		PreparedStatement preparedStatement;
		int orderID=0;
		try {
				String productID = new String(co.getProductID());
				String productSize = new String(co.getProductSize());
				
				String[] arrpID = productID.split(",");
				String[] arrpSize = productSize.split(",");
				
				for(int i=0;i<arrpID.length;i++)
				{
					rs = stmt.executeQuery("select Product_Quantity from temp where Product_ID ="+Integer.parseInt(arrpID[i])+" and Product_Size = "+Integer.parseInt(arrpSize[i])+";");
					
					int count = 0;
					while(rs.next())
					{
						int quantity = rs.getInt("Product_Quantity");
						quantity++;
						
						preparedStatement = conn.prepareStatement("Update temp set Product_Quantity = "+quantity+"");
						preparedStatement.executeUpdate();
						count++;	
					}
					if(count==0)
					{
						preparedStatement = conn.prepareStatement("Insert into temp (Product_ID, Product_Size, Product_Quantity) values (?,?,?)");
						preparedStatement.setInt(1, Integer.parseInt(arrpID[i]));
						preparedStatement.setInt(2, Integer.parseInt(arrpSize[i]));
						preparedStatement.setInt(3, 1);
						preparedStatement.executeUpdate();
					}
					
				}
				
			
			rs = stmt.executeQuery("select MAX(Order_ID) as Order_ID from Customer_Order;");
			//int count = 0;
			while(rs.next())
			{
				
				if(rs.getInt("Order_ID")>0)
				{
					orderID = rs.getInt("Order_ID") + 1;
				}
				else
					orderID = 1;
				
			}
			
			rs = stmt.executeQuery("select Product_ID, Product_Size, Product_Quantity from temp;");
			while(rs.next())
			{
				preparedStatement = conn.prepareStatement("Insert into Customer_Order (Order_ID, Email, Product_ID, Product_Size, Quantity, Order_Date, Price) values (?,?,?,?,?,?,?)");
				preparedStatement.setInt(1, orderID);
				preparedStatement.setString(2, co.getEmail());
				preparedStatement.setInt(3, rs.getInt("Product_ID"));
				preparedStatement.setInt(4, rs.getInt("Product_Size"));
				preparedStatement.setInt(5, rs.getInt("Product_Quantity"));
				preparedStatement.setDate(6, currentDate());
				preparedStatement.setString(7, co.getTotalPrize());
				
				preparedStatement.executeUpdate();
			}
				
				preparedStatement = conn.prepareStatement("Insert into Customer_Order_Delivery_Details (Order_ID, Name, Address, City, ZipCode, State, Country) values (?,?,?,?,?,?,?)");
				preparedStatement.setInt(1, orderID);
				preparedStatement.setString(2, co.getName());
				preparedStatement.setString(3, co.getAddress());
				preparedStatement.setString(4, co.getCity());
				preparedStatement.setString(5, co.getZipCode());
				preparedStatement.setString(6, co.getState());
				preparedStatement.setString(7, co.getCountry());
				
				
				preparedStatement.executeUpdate();
				
				
				preparedStatement = conn.prepareStatement("Insert into Payment_Details (Order_ID, Card_Number, Card_Holder, Card_Expiry_Date, Payment_Date, Total_Amount) values (?,?,?,?,?,?)");
				preparedStatement.setInt(1, orderID);
				preparedStatement.setString(2, co.getCardNumber());
				preparedStatement.setString(3, co.getCardHolderName());
				preparedStatement.setDate(4, (new java.sql.Date(co.getCardExpiryDate().getTime())));
				preparedStatement.setDate(5, currentDate());
				preparedStatement.setString(6, co.getTotalPrize());
				
				preparedStatement.executeUpdate();
				
				preparedStatement = conn.prepareStatement("delete from temp;");
				preparedStatement.executeUpdate();
				
				return true;
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return false;
		}
		
	
	}
	
	
	
	
	public java.sql.Date currentDate()
	{
		
		try {
			rs = stmt.executeQuery("select curdate() curdate;");
			while(rs.next())
			{
				 currentDate = rs.getDate("curdate");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return currentDate;
		
		
	}
	
	
	public void rsCloseConnection()
	{
		try {
			if(rs!=null)
				rs.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void rs1CloseConnection()
	{
		try {
			if(rs1!=null)
				rs1.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

}
