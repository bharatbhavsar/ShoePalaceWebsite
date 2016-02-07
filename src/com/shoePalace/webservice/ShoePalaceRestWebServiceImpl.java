package com.shoePalace.webservice;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jettison.json.JSONObject;

import com.shoePalace.pojos.CustomerOrderSubmit;
import com.shoePalace.pojos.DatabaseConnection;
import com.shoePalace.pojos.SendObjectClass;
import com.shoePalace.pojos.UserDetails;





@Path("/Shoe")
public class ShoePalaceRestWebServiceImpl {
	

	
	@POST
	@Path("/Login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String loginVerification(JSONObject json) throws Exception {
		// TODO Auto-generated method stub

		String email = json.optString("email");
		String password = json.optString("password");
		
		

		DatabaseConnection db = new DatabaseConnection();
		boolean success = db.checkCustomerDetails(email, password);
		
		
		if(success)
		{
			return "Login successful";
		}
		else
			return "Login failure";
		
		
	}

	@POST
	@Path("/Register")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String registerUser(JSONObject json) {
		// TODO Auto-generated method stub
		
		UserDetails ud = new UserDetails();
		
		ud.setEmailAddress(json.optString("email"));
		ud.setPassword(json.optString("password"));
		ud.setName(json.optString("name"));
		
		DatabaseConnection dc = new DatabaseConnection();
		String output = dc.newUserDetails(ud);
		
		return output;
		
	}

	
	@GET
	@Path("/Homepage")	
	@Produces(MediaType.APPLICATION_JSON)
	public String homepage() {
		String json1 = "";
		MemcachedClient cache = null;
		try {
			cache = new MemcachedClient(AddrUtil.getAddresses("127.0.0.1:11211"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		SendObjectClass os = (SendObjectClass) cache.get("all");
		if(os!=null)
		{
			System.out.println("hello");
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			json1 = new String();;
			try {
				json1 = ow.writeValueAsString(os);
				
			} catch (JsonGenerationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else
		{
			System.out.println("hi");
			
			DatabaseConnection db = new DatabaseConnection();
			SendObjectClass so = db.ProductListAll();
			cache.set("all",3600,so);
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			try {
				json1 = ow.writeValueAsString(so);
				
			} catch (JsonGenerationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			

		return json1;
		
	}

	@GET
	@Path("Homepage/{productID}")
	@Produces(MediaType.APPLICATION_JSON)
	public String prodcutDescription(@PathParam("productID") int productID) {
		String json1 = "";
		
		MemcachedClient cache = null;
		try {
			cache = new MemcachedClient(AddrUtil.getAddresses("127.0.0.1:11211"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		SendObjectClass os = (SendObjectClass) cache.get(productID+"");
		if(os!=null)
		{
			System.out.println("hello");
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			try {
				json1 = ow.writeValueAsString(os);
				
			} catch (JsonGenerationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			System.out.println("hi");
			DatabaseConnection db = new DatabaseConnection();
			SendObjectClass so = db.ProductListOneItem(productID);
			cache.set(productID+"",3600,so);
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			try {
				json1 = ow.writeValueAsString(so);
				
			} catch (JsonGenerationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		return json1;
		
		
	}

	@POST
	@Path("/MakePayment")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String paymentConfirmation(JSONObject json) {
		// TODO Auto-generated method stub
		
		CustomerOrderSubmit co = new CustomerOrderSubmit();
		
		co.setName(json.optString("name"));
		co.setProductID(json.optString("productID"));
		co.setAddress(json.optString("address"));
		co.setCardHolderName(json.optString("cardHolderName"));
		co.setCardNumber(json.optString("cardNumber"));
		co.setCity(json.optString("city"));
		co.setCountry(json.optString("country"));
		co.setProductSize(json.optString("productSize"));
		co.setState(json.optString("state"));
		co.setTotalPrize(json.optString("totalPrize"));
		co.setZipCode(json.optString("zipCode"));
		co.setEmail(json.optString("email"));
		
		String cardExpiryDate = "20"+json.optString("expiryYear")+"-"+json.optString("expiryMonth")+"-15";
		
		
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
		
	 
		try {
	 
			Date date = formatter1.parse(cardExpiryDate);
			
			co.setCardExpiryDate(date);
			
			
			DatabaseConnection db = new DatabaseConnection();
			boolean success = db.submitCustomerOrder(co);
			
			
			
			if(success)
			{
				return "Payment successful";
			}
			else
			{
				return "Payment failure";
			}
		
			
			
	 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "Payment successful";
		
		
		
		
	}


}
