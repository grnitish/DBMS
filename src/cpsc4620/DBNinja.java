package cpsc4620;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.text.SimpleDateFormat; 
import java.text.*;  
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.text.SimpleDateFormat;
import java.util.Date;
//import java.sql.Date;


/*
 * This file is where most of your code changes will occur You will write the code to retrieve
 * information from the database, or save information to the database
 * 
 * The class has several hard coded static variables used for the connection, you will need to
 * change those to your connection information
 * 
 * This class also has static string variables for pickup, delivery and dine-in. If your database
 * stores the strings differently (i.e "pick-up" vs "pickup") changing these static variables will
 * ensure that the comparison is checking for the right string in other places in the program. You
 * will also need to use these strings if you store this as boolean fields or an integer.
 * 
 * 
 */

/**
 * A utility class to help add and retrieve information from the database
 */

public final class DBNinja {
	private static Connection conn;

	// Change these variables to however you record dine-in, pick-up and delivery, and sizes and crusts
	public final static String pickup = "pickup";
	public final static String delivery = "delivery";
	public final static String dine_in = "dinein";

	public final static String size_s = "small";
	public final static String size_m = "medium";
	public final static String size_l = "Large";
	public final static String size_xl = "XLarge";

	public final static String crust_thin = "Thin";
	public final static String crust_orig = "Original";
	public final static String crust_pan = "Pan";
	public final static String crust_gf = "Gluten-Free";



	
	private static boolean connect_to_db() throws SQLException, IOException {

		try {
			conn = DBConnector.make_connection();
			return true;
		} catch (SQLException e) {
			return false;
		} catch (IOException e) {
			return false;
		}

	}

	
	public static void addOrder(Order o) throws SQLException, IOException 
	{
		connect_to_db();
		/*
		 * add code to add the order to the DB. Remember that we're not just
		 * adding the order to the order DB table, but we're also recording
		 * the necessary data for the delivery, dinein, and pickup tables
		 */
		//insert INTO `order` (OrderOrder_ID,OrderCus_ID,OrderCus_Price,
			//	OrderBus_Price,OrderTime_Date,OrderPizza_Quant,OrderPizza_Status,
				//OrderOrder_Type)
				//VALUES 
				//(3, 1002,  64.5,19.8 ,'2022-03-03 21:30:00' ,6,"Completed","PickUp");
		String query= "insert INTO `order` (OrderOrder_ID,OrderCus_ID,OrderCus_Price,"
				+ "	OrderBus_Price,OrderTime_Date,OrderPizza_Quant,OrderPizza_Status,"
				+ "	OrderOrder_Type)"
				+ "VALUES (?,?,?,?,?,?,?,?) ;";
				//+ " PizzaCrust_Name,PizzaSize_Name,PizzaOrder_ID) VALUES"+ "(?,?,?,?,?,?,?);";
		//System.out.println(o.getCustID());
		//System.out.println(o.getDate());
		int list=o.getPizzaList().size();
		PreparedStatement statement = conn.prepareStatement(query);
	    statement.setInt(1,o.getOrderID());
	    statement.setInt(2,o.getCustID());
	    statement.setDouble(3,o.getCustPrice());
	    statement.setDouble(4,o.getBusPrice());
	    //java.util.Date utilDate = new java.util.Date();
	   // java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

	    // now pass the sqlDate object to the PreparedStatement's setDate method
	    //PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO my_table (my_date_column) VALUES (?)");
	    //preparedStatement.setDate(1, sqlDate);
	    //String dateString = "2023-04-21";
	    java.sql.Date sqldate = java.sql.Date.valueOf(o.getDate());
	    //java.sql.Date sqldate;
		//SimpleDateFormat formatter = new SimpleDateFormat("yyyy-dd-MM");
	    //Date date= "2022-03-1997";
	    //java.sql.Date sqlDate  = formatter.parse(o.getDate());
	    statement.setDate(5, sqldate);
	    
	    statement.setInt(6,list);
	    
	    statement.setString(7,"Not-Completed");
	    
	    statement.setString(8,o.getOrderType());
	    statement.executeUpdate();
		

		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}
	
	public static void addPizza(Pizza p) throws SQLException, IOException
	{
		connect_to_db();
		/*
		 * Add the code needed to insert the pizza into into the database.
		 * Keep in mind adding pizza discounts to that bridge table and 
		 * instance of topping usage to that bridge table if you have't accounted
		 * for that somewhere else.
		 */
		String query= "INSERT INTO `pizza` (PizzaPizza_ID,PizzaStatus,PizzaCus_Price,PizzaBus_Price,"
				+ " PizzaCrust_Name,PizzaSize_Name,PizzaOrder_ID) VALUES"+ "(?,?,?,?,?,?,?);";
		
		
		PreparedStatement statement = conn.prepareStatement(query);
		//System.out.println(p.getOrderID());
		//System.out.println(p.getCrustType());
		//System.out.println(p.getSize());
	    statement.setInt(1,p.getPizzaID() );
	    statement.setString(2,p.getPizzaState());
	    statement.setDouble(3,p.getCustPrice());
	    statement.setDouble(4,p.getBusPrice());
	    statement.setString(5,p.getCrustType());
	    statement.setString(6,p.getSize());
	    statement.setDouble(7,p.getOrderID());
	    statement.executeUpdate();
	    //System.out.println(p.getOrderID());
		
		conn.close();
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}
	
	public static int getMaxPizzaID() throws SQLException, IOException
	{
		connect_to_db();
		/*
		 * A function I needed because I forgot to make my pizzas auto increment in my DB.
		 * It goes and fetches the largest PizzaID in the pizza table.
		 * You wont need to implement this function if you didn't forget to do that
		 */
		Statement statement = conn.createStatement();
		//String ret = "";
		String query = "Select * From `pizza` order by PizzaPizza_ID desc"+";";
		ResultSet rset = statement.executeQuery(query);
		int pizzaid =0 ;
		while(rset.next()) {
			pizzaid = rset.getInt("PizzaPizza_ID");
			//return pizzaid;
			break;
		}
		
		
		
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		return pizzaid;
	}
	
	public static void useTopping(Pizza p, Topping t, boolean isDoubled) throws SQLException, IOException //this function will update toppings inventory in SQL and add entities to the Pizzatops table. Pass in the p pizza that is using t topping
	{
		connect_to_db();
		/*
		 * This function should 2 two things.
		 * We need to update the topping inventory every time we use t topping (accounting for extra toppings as well)
		 * and we need to add that instance of topping usage to the pizza-topping bridge if we haven't done that elsewhere
		 * Ideally, you should't let toppings go negative. If someone tries to use toppings that you don't have, just print
		 * that you've run out of that topping.
		 */
		
		
		
		
		
		
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}
	
	public static Order typeoforder(Order o) throws SQLException,IOException{
		if(connect_to_db()) {
		String type=o.getOrderType();

		if (type.equals("DineIn")) {
			
			//public DineinOrder(int orderID, int custID, String date, double custPrice, double busPrice, int isComplete, int tablenum) {
			Statement statement = conn.createStatement();
			//String ret = "";
			System.out.println(o.getOrderID()+"|"+o.getCustID()+" | "+o.getDate()+" |"+o.getCustPrice()+"| "+o.getBusPrice()+" |"+o.getIsComplete());
			String query = "Select * From `dine_in` WHERE Dine_InOrder_ID="+o.getOrderID()+";";
			ResultSet rset = statement.executeQuery(query);
			while(rset.next()) {
				int table_num;
				table_num=rset.getInt("Dine_InTable_Number");
				
				DineinOrder dinein= new DineinOrder(o.getOrderID(),o.getCustID(),o.getDate(),o.getCustPrice(),o.getBusPrice(),o.getIsComplete(),table_num);
				
				return dinein;
			}
			
		}
		else if (type.equals("PickUp")) {
			Statement statement = conn.createStatement();
			//String ret = "";
			String query = "Select * From `pick_up` WHERE Pick_UpOrder_ID="+o.getOrderID()+";";
			ResultSet rset = statement.executeQuery(query);
			while(rset.next()) {
				//int table_num;
				int ispick=rset.getInt("Pick_UpIsPicked_Up");
				PickupOrder pickup= new PickupOrder(o.getOrderID(),o.getCustID(),o.getDate(),o.getCustPrice(),o.getBusPrice(),o.getIsComplete(),ispick);
				return pickup;
			}
		}
		else {
			//public DeliveryOrder(int orderID, int custID, String date, double custPrice, double busPrice, int isComplete, String address) 
			Statement statement = conn.createStatement();
			//String ret = "";
			String query = "Select * From `delivery` WHERE DeliveryOrder_ID="+o.getOrderID()+";";
			ResultSet rset = statement.executeQuery(query);
			while(rset.next()) {
				String address="";
				String street = rset.getString("DeliveryStreet_Name");
				String DeliveryCity = rset.getString("DeliveryCity");
				String DeliveryState = rset.getString("DeliveryState");
				String zip = "" + rset.getInt("DeliveryZip_Code");
				address= street +" "+DeliveryCity+" "+DeliveryState+" "+zip;
				DeliveryOrder delivery= new DeliveryOrder(o.getOrderID(),o.getCustID(),o.getDate(),o.getCustPrice(),o.getBusPrice(),o.getIsComplete(),address);
				return delivery;
			}
			
		}
	}
		conn.close();
	return null;
	}
	
	public static void usePizzaDiscount(Pizza p, Discount d) throws SQLException, IOException
	{
		connect_to_db();
		/*
		 * Helper function I used to update the pizza-discount bridge table. 
		 * You might use this, you might not depending on where / how to want to update
		 * this table
		 */
		String query = "INSERT INTO `discount_on_pizza` "
				+ " (Discount_On_PizzaDisc_ID,Discount_On_PizzaPizza_ID) Values (?,?);";
				//+ "( " + c.getCustID()+", '" + c.getFName()+"', "+"'" + c.getLName()+"', "+"'" + c.getPhone()+"')"+";";
		PreparedStatement statement = conn.prepareStatement(query);
	    statement.setInt(1, d.getDiscountID());
	    statement.setInt(2, p.getPizzaID());
	    
	    statement.executeUpdate();
		
		
		
		
		
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}
	
	public static void useOrderDiscount(Order o, Discount d) throws SQLException, IOException
	{
		connect_to_db();
		/*
		 * Helper function I used to update the pizza-discount bridge table. 
		 * You might use this, you might not depending on where / how to want to update
		 * this table
		 */
		String query = "INSERT INTO `discount_on_order` "
				+ " (Discount_On_OrderDisc_ID,Discount_On_OrderOrder_ID) Values (?,?);";
				//+ "( " + c.getCustID()+", '" + c.getFName()+"', "+"'" + c.getLName()+"', "+"'" + c.getPhone()+"')"+";";
		PreparedStatement statement = conn.prepareStatement(query);
	    statement.setInt(1, d.getDiscountID());
	    statement.setInt(2, o.getOrderID());
	    
	    statement.executeUpdate();
		
		
		
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}
	


	
	public static void addCustomer(Customer c) throws SQLException, IOException {
		
		connect_to_db();
			
			//int nextid = getNextCustomerID();
			//System.out.println(c.getCustID());
			//String query = "Select * From `order` Order by OrderTime_Date desc;";
		

			String query = "INSERT INTO `customer` "
					+ " (CustomerCus_ID,CustomerFirst_Name,CustomerLast_Name,CustomerPhone_Num) Values (?,?,?,?);";
					//+ "( " + c.getCustID()+", '" + c.getFName()+"', "+"'" + c.getLName()+"', "+"'" + c.getPhone()+"')"+";";
			PreparedStatement statement = conn.prepareStatement(query);
		    statement.setInt(1, c.getCustID());
		    statement.setString(2, c.getFName());
		    statement.setString(3,c.getLName() );
		    statement.setString(4, c.getPhone());
		    statement.executeUpdate();
			
		
		/*
		 * This should add a customer to the database
		 */
				
		
		conn.close();
		
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}


	
	public static void CompleteOrder(Order o) throws SQLException, IOException {
		connect_to_db();
		/*
		 * add code to mark an order as complete in the DB. You may have a boolean field
		 * for this, or maybe a completed time timestamp. However you have it.
		 */
		
		String query =  "UPDATE `order` SET `OrderPizza_Status` = ? WHERE `OrderOrder_ID` = ?;"; 
		
		PreparedStatement statement = conn.prepareStatement(query);
	    statement.setString(1, "Completed");
	    statement.setInt(2, o.getOrderID());
	    statement.executeUpdate();
	    query = "UPDATE `pizza` set PizzaStatus = ? WHERE `PizzaOrder_ID` = ?";
	    statement = conn.prepareStatement(query);
	    statement.setString(1, "Completed");
	    statement.setInt(2, o.getOrderID());
	    statement.executeUpdate();
        //statement.setInt(3, idToUpdate);
		//statement.executeUpdate(query);
		//statement.close();
		conn.close();	
		
		
		
		
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}


	
	
	public static void AddToInventory(Topping t, double toAdd) throws SQLException, IOException {
		connect_to_db();
		/*
		 * Adds toAdd amount of topping to topping t.
		 */
		

		
		
		int result = (int) toAdd; // Type casting double to int
		 // Output: 10
		int value = t.getCurINVT()+result;
		t.setCurINVT(value);
		
		//Statement statement = conn.createStatement();
		double curr = t.getCurINVT();
		int topid = t.getTopID();
		//System.out.println(curr+" "+topid);
		String query =  "UPDATE `topping` SET `ToppingCurrent_Inv_Level` = ? WHERE `ToppingTop_ID` = ?;"; 
		
		PreparedStatement statement = conn.prepareStatement(query);
	    statement.setDouble(1, curr);
	    statement.setInt(2, topid);
	    statement.executeUpdate();
        //statement.setInt(3, idToUpdate);
		//statement.executeUpdate(query);
		//statement.close();
		conn.close();
		
		
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}

	

	public static void printInventory() throws SQLException, IOException {
		connect_to_db();
		
		/*
		 * I used this function to PRINT (not return) the inventory list.
		 * When you print the inventory (either here or somewhere else)
		 * be sure that you print it in a way that is readable.
		 * 
		 * 
		 * 
		 * The topping list should also print in alphabetical order
		 */
		
		/*
		 * return an arrayList of all the customers. These customers should
		 *print in alphabetical order, so account for that as you see fit.
		*/
		
		Statement statement = conn.createStatement();
		String ret = "";
		String query = "SELECT * FROM `topping` order by ToppingTop_Name ;" ;
		ResultSet rset = statement.executeQuery(query);
		int customerId = 0;
		//String leftAligned = "Left Aligned";
		//String rightAligned = "Right Aligned";
		//String centerAligned = "Center Aligned";


		//customerId1=0;
		System.out.println(String.format("%-20s%20s%20s","ID" , "ToppingName", "Current_Level"));
		while(rset.next())
		{
			customerId = rset.getInt("ToppingTop_ID");
            String tname = rset.getString("ToppingTop_Name");
            String currlevel = rset.getString("ToppingCurrent_Inv_Level");
            //String phonenum	= rset.getString("CustomerPhone_Num");
            // ... additional fields as per your 'Customer' class definition
            //Customer(int custID, String fName, String lName, String phone)
            // Create a new Customer object and add it to the ArrayList
            //return customerId;
            
            //return customerId;
            System.out.println(String.format("%-20s%20s%20s",customerId , tname, currlevel));

            
		}
		
		
		conn.close();

		
		
		
		
		
		
		
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION		
	}
	
	
	public static ArrayList<Topping> getInventory() throws SQLException, IOException {
		connect_to_db();
		/*
		 * This function actually returns the toppings. The toppings
		 * should be returned in alphabetical order if you don't
		 * plan on using a printInventory function
		 */
		Statement statement = conn.createStatement();
		String ret = "";
		String query = "SELECT * FROM `topping` order by ToppingTop_Name ;" ;
		ResultSet rset = statement.executeQuery(query);
		int customerId = 0;
		//String leftAligned = "Left Aligned";
		//String rightAligned = "Right Aligned";
		//String centerAligned = "Center Aligned";
		ArrayList<Topping> top= new ArrayList<Topping>();
		
		//customerId1=0;
		//System.out.println(String.format("%-20s%20s%20s","ID" , "ToppingName", "Current_Level"));
		while(rset.next())
		{
			int ToppingTop_ID = rset.getInt("ToppingTop_ID");
            String tname = rset.getString("ToppingTop_Name");
            int ToppingCurrent_Inv_Level = rset.getInt("ToppingCurrent_Inv_Level");
            double ToppingCus_Price = rset.getDouble("ToppingCus_Price");
            double ToppingBus_Price = rset.getDouble("ToppingBus_Price");
            int ToppingMin_Inv_Level = rset.getInt("ToppingMin_Inv_Level");
            double ToppingPersonal_Amt = rset.getDouble("ToppingPersonal_Amt");
            //ToppingTop_ID int not null AUTO_INCREMENT,
            //ToppingTop_Name varchar(30) not null,
//            ToppingCus_Price numeric(5,2) not null,
//            ToppingBus_Price numeric(5,2) not null,
//            ToppingMin_Inv_Level numeric(5,2) not null,
//            ToppingCurrent_Inv_Level numeric(5,2) not null,
//            ToppingPersonal_Amt numeric(5,2) not null,
//            ToppingMedium_Amt numeric(5,2) not null,
//            ToppingLarge_Amt numeric(5,2) not null,
//            ToppingXL_Amt numeric(5,2) not null,
            double ToppingMedium_Amt =rset.getDouble("ToppingMedium_Amt");
            double ToppingLarge_Amt = rset.getDouble("ToppingLarge_Amt");
            double ToppingXL_Amt = rset.getDouble("ToppingXL_Amt");
            //String phonenum	= rset.getString("CustomerPhone_Num");
            // ... additional fields as per your 'Customer' class definition
            //Customer(int custID, String fName, String lName, String phone)
            // Create a new Customer object and add it to the ArrayList
            //return customerId;
            
            //return customerId;
            //System.out.println(String.format("%-20s%20s%20s",customerId , tname, currlevel));
//            Topping(int topID, String topName, double perAMT, double medAMT, double lgAMT, double xLAMT,
//        			double custPrice, double busPrice, int minINVT, int curINVT)
            Topping t = new Topping(ToppingTop_ID,tname,ToppingPersonal_Amt,ToppingMedium_Amt,
            		ToppingLarge_Amt,ToppingXL_Amt,ToppingCus_Price,
            		ToppingBus_Price,ToppingMin_Inv_Level,ToppingCurrent_Inv_Level);
            top.add(t);
            
		}
		
		
		conn.close();

		

		
		
		
		
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		return top;
	}

	public static ArrayList<Order> DateFormatOrders(String dat) throws SQLException, IOException{
		
		ArrayList<Order> orders = new ArrayList<Order>();
		if(connect_to_db()) {
			
			// Order(int orderID, int custID, String orderType, String date, double custPrice, double busPrice, int iscomplete)
			Statement statement = conn.createStatement();
			//String ret = "";
			String query = "select * from `order` where OrderTime_Date >" + dat + "order by OrderTime_Date;";
			ResultSet rset = statement.executeQuery(query);
			while(rset.next()) {
				
				int orderID = rset.getInt("OrderOrder_ID");
				int custID = rset.getInt("OrderCus_ID");
				String orderType = rset.getString("OrderOrder_Type");
	            Date date = rset.getDate("OrderTime_Date");
	            double custPrice = rset.getDouble("OrderCus_Price");
	            double busPrice = rset.getDouble("OrderBus_Price");
	            String iscomplete = rset.getString("OrderPizza_Status");
	            //String str = "Completed"; // The string to compare
	            //String res="";
	            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss"); 
	            String strDate = dateFormat.format(date); 
	            int a; // The integer variable to set
	            
	            if (iscomplete.equals("Completed")) {
	                a = 1; // Set a to 0 if str is equal to "completed"
	            } else {
	                a = 0; // Set a to 1 if str is not equal to "completed"
	            }
	            //String lname = rset.getString("CustomerLast_Name");
	            //String phonenum		= rset.getString("CustomerPhone_Num");
	            Order orderobj = new Order(orderID, custID, orderType, strDate, custPrice, busPrice, a);
				// Statement statement = conn.createStatement();
	            // Customer customer = new Customer(customerId, fname, lname,phonenum);
	            orders.add(orderobj);
	            
			}
				
			}
		
		return orders;
	}
	
	
	
	public static ArrayList<Order> getCurrentOrders() throws SQLException, IOException {
		//connect_to_db();
		
		
		ArrayList<Order> orders = new ArrayList<Order>();
		if(connect_to_db()) {
			
			// Order(int orderID, int custID, String orderType, String date, double custPrice, double busPrice, int iscomplete)
			Statement statement = conn.createStatement();
			//String ret = "";
			String query = "Select * From `order` Order by OrderTime_Date desc;";
			ResultSet rset = statement.executeQuery(query);
			while(rset.next())
			{
				int orderID = rset.getInt("OrderOrder_ID");
				int custID = rset.getInt("OrderCus_ID");
				String orderType = rset.getString("OrderOrder_Type");
	            Date date = rset.getDate("OrderTime_Date");
	            double custPrice = rset.getDouble("OrderCus_Price");
	            double busPrice = rset.getDouble("OrderBus_Price");
	            String iscomplete = rset.getString("OrderPizza_Status");
	            //String str = "Completed"; // The string to compare
	            //String res="";
	            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
	            String strDate = dateFormat.format(date); 
	            int a; // The integer variable to set
	            
	            if (iscomplete.equals("Completed")) {
	                a = 1; // Set a to 0 if str is equal to "completed"
	            } else {
	                a = 0; // Set a to 1 if str is not equal to "completed"
	            }
	            //String lname = rset.getString("CustomerLast_Name");
	            //String phonenum		= rset.getString("CustomerPhone_Num");
	            Order orderobj = new Order(orderID, custID, orderType, strDate, custPrice, busPrice, a);
				// Statement statement = conn.createStatement();
	            // Customer customer = new Customer(customerId, fname, lname,phonenum);
	            orders.add(orderobj);
	            
				//ret = rset.getString(1) + " " + rset.getString(2);
	            
			}
			}
		
		/*
		 * This function should return an arraylist of all of the orders.
		 * Remember that in Java, we account for supertypes and subtypes
		 * which means that when we create an arrayList of orders, that really
		 * means we have an arrayList of dineinOrders, deliveryOrders, and pickupOrders.
		 * 
		 * Also, like toppings, whenever we print out the orders using menu function 4 and 5
		 * these orders should print in order from newest to oldest.
		 */


		conn.close();
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		return orders;
	}
	
	public static ArrayList<Order> sortOrders(ArrayList<Order> list) throws SQLException, IOException 
	{
		/*
		 * This was a function that I used to sort my arraylist based on date.
		 * You may or may not need this function depending on how you fetch
		 * your orders from the DB in the getCurrentOrders function.
		 */
		ArrayList<Order> orders = new ArrayList<Order>();
		orders=list;
		if(connect_to_db()) {
			
			// Order(int orderID, int custID, String orderType, String date, double custPrice, double busPrice, int iscomplete)
			Statement statement = conn.createStatement();
			//String ret = "";
			String query = "Select * From order"+" Order by date desc;";
			ResultSet rset = statement.executeQuery(query);
			while(rset.next())
			{
				int orderID = rset.getInt("OrderOrder_ID");
				int custID = rset.getInt("OrderCus_ID");
				String orderType = rset.getString("OrderOrder_Type");
	            Date date = rset.getDate("OrderTime_Date");
	            double custPrice = rset.getDouble("OrderCus_Price");
	            double busPrice = rset.getDouble("OrderBus_Price");
	            String iscomplete = rset.getString("OrderPizza_Status");
	            //String str = "Completed"; // The string to compare
	            //String res="";
	            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss"); 
	            String strDate = dateFormat.format(date); 
	            int a; // The integer variable to set
	            
	            if (iscomplete.equals("Completed")) {
	                a = 1; // Set a to 0 if str is equal to "completed"
	            } else {
	                a = 0; // Set a to 1 if str is not equal to "completed"
	            }
	            //String lname = rset.getString("CustomerLast_Name");
	            //String phonenum		= rset.getString("CustomerPhone_Num");
	            Order orderobj = new Order(orderID, custID, orderType, strDate, custPrice, busPrice, a);
				// Statement statement = conn.createStatement();
	            // Customer customer = new Customer(customerId, fname, lname,phonenum);
	            orders.add(orderobj);
	            
				//ret = rset.getString(1) + " " + rset.getString(2);
	            
			}
			}
		
		
		
		conn.close();
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		return orders;
		
	}
	
	public static boolean checkDate(int year, int month, int day, String dateOfOrder)
	{
		//Helper function I used to help sort my dates. You likely wont need these
		
		
		
		
		
		
		
		
		return false;
	}
	
	
	/*
	 * The next 3 private functions help get the individual components of a SQL datetime object. 
	 * You're welcome to keep them or remove them.
	 */
	private static int getYear(String date)// assumes date format 'YYYY-MM-DD HH:mm:ss'
	{
		return Integer.parseInt(date.substring(0,4));
	}
	private static int getMonth(String date)// assumes date format 'YYYY-MM-DD HH:mm:ss'
	{
		return Integer.parseInt(date.substring(5, 7));
	}
	private static int getDay(String date)// assumes date format 'YYYY-MM-DD HH:mm:ss'
	{
		return Integer.parseInt(date.substring(8, 10));
	}



	
	
	
	public static double getBaseCustPrice(String size, String crust) throws SQLException, IOException {
		connect_to_db();
		double bp = 0.0;
		// add code to get the base price (for the customer) for that size and crust pizza Depending on how
		// you store size & crust in your database, you may have to do a conversion
		Statement statement = conn.createStatement();
		//String ret = "";
		String query = "Select BasePriceCus_Price From `base_price` where BaseSize_Name = '"+size+"' and  BaseCrust_Name = '"+crust+"' ;";
		ResultSet rset = statement.executeQuery(query);
		while(rset.next()) {
			double custPrice = rset.getDouble("BasePriceCus_Price");
			
			bp = custPrice;
		}
		
		
		
		
		
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		return bp;
	}
	
	public static String getCustomerName(int CustID) throws SQLException, IOException
	{
		/*
		 *This is a helper function I used to fetch the name of a customer
		 *based on a customer ID. It actually gets called in the Order class
		 *so I'll keep the implementation here. You're welcome to change
		 *how the order print statements work so that you don't need this function.
		 */
		connect_to_db();
		String ret = "";
		String query = "Select CustomerFirst_Name, CustomerLast_Name From customer WHERE CustomerCus_ID=" + CustID + ";";
		Statement stmt = conn.createStatement();
		ResultSet rset = stmt.executeQuery(query);
		
		while(rset.next())
		{
			ret = rset.getString(1) + " " + rset.getString(2);
		}
		conn.close();
		return ret;
	}
	
	public static double getBaseBusPrice(String size, String crust) throws SQLException, IOException {
		connect_to_db();
		double bp = 0.0;
		// add code to get the base cost (for the business) for that size and crust pizza Depending on how
		// you store size and crust in your database, you may have to do a conversion
		
		Statement statement = conn.createStatement();
		//String ret = "";
		//BasePriceBus_Price
		String query = "Select BasePriceBus_Price From `base_price` where BaseSize_Name = '"+size+"' and  BaseCrust_Name = '"+crust+"' ;";
		ResultSet rset = statement.executeQuery(query);
		while(rset.next()) {
			double Busprice = rset.getDouble("BasePriceBus_Price");
			
			bp = Busprice;
		}
		
		
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		return bp;
	}

	
	public static ArrayList<Discount> getDiscountList() throws SQLException, IOException {
		ArrayList<Discount> discs = new ArrayList<Discount>();
		connect_to_db();
		//returns a list of all the discounts.
		
		String query = "SELECT * FROM `discount`;";
		Statement stmt = conn.createStatement();
		ResultSet rset = stmt.executeQuery(query);
		
		while(rset.next())
		{
			//ret = rset.getString(1) + " " + rset.getString(2);
			int DiscountDisc_ID = rset.getInt("DiscountDisc_ID");
			String DiscountDisc_Name = rset.getString("DiscountDisc_Name");
			double DiscountDisc_Amt = rset.getDouble("DiscountDisc_Amt");
			double DiscountDisc_Per = rset.getDouble("DiscountDisc_Per");
			
			if (DiscountDisc_Per!=0) {
			Discount d = new Discount(DiscountDisc_ID,DiscountDisc_Name,DiscountDisc_Per,false);
			discs.add(d);
			}
			else {
				Discount d = new Discount(DiscountDisc_ID,DiscountDisc_Name,DiscountDisc_Amt,true);
				discs.add(d);
			}
			
		}
		
		
		
		
		
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		return discs;
	}


	public static ArrayList<Customer> getCustomerList() throws SQLException, IOException {
		//List of Customers Objects
		ArrayList<Customer> custs = new ArrayList<Customer>();
		if(connect_to_db()) {
		/*
		 * return an arrayList of all the customers. These customers should
		 *print in alphabetical order, so account for that as you see fit.
		*/
		
		Statement statement = conn.createStatement();
		String ret = "";
		String query = "Select * From customer Order by CustomerFirst_Name,CustomerLast_Name;";
		ResultSet rset = statement.executeQuery(query);
		while(rset.next())
		{
			int customerId = rset.getInt("CustomerCus_ID");
            String fname = rset.getString("CustomerFirst_Name");
            String lname = rset.getString("CustomerLast_Name");
            String phonenum		= rset.getString("CustomerPhone_Num");
            // ... additional fields as per your 'Customer' class definition
            //Customer(int custID, String fName, String lName, String phone)
            // Create a new Customer object and add it to the ArrayList
            Customer customer = new Customer(customerId, fname, lname,phonenum);
            custs.add(customer);
            
			//ret = rset.getString(1) + " " + rset.getString(2);
            
		}
		}
		
		
		conn.close();
		



		
		
		
		
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		return custs;
	}
	public static int getNextCustomerID() throws SQLException, IOException
	{
		
		connect_to_db() ;
			/*
			 * return an arrayList of all the customers. These customers should
			 *print in alphabetical order, so account for that as you see fit.
			*/
			
			Statement statement = conn.createStatement();
			String ret = "";
			String query = "Select * From customer Order by CustomerCus_ID;";
			ResultSet rset = statement.executeQuery(query);
			int customerId = 0;
			//customerId1=0;
			while(rset.next())
			{
				customerId = rset.getInt("CustomerCus_ID");
	            String fname = rset.getString("CustomerFirst_Name");
	            String lname = rset.getString("CustomerLast_Name");
	            String phonenum	= rset.getString("CustomerPhone_Num");
	            // ... additional fields as per your 'Customer' class definition
	            //Customer(int custID, String fName, String lName, String phone)
	            // Create a new Customer object and add it to the ArrayList
	            //return customerId;
	            
	            //return customerId;
			}
			
			conn.close();

			return customerId;
			
			//		customerId1 = customerId;
//		//int customerId;
//		return customerId1;
		//return 0;
		
	}
	public static int getNextOrderID() throws SQLException, IOException
	{
		/*
		 * A helper function I had to use because I forgot to make
		 * my OrderID auto increment...You can remove it if you
		 * did not forget to auto increment your orderID.
		 */
		connect_to_db() ;
		Statement statement = conn.createStatement();
		String ret = "";
		String query = "Select * From `order` Order by OrderOrder_ID desc;";
		ResultSet rset = statement.executeQuery(query);
		int OrderOrder_ID = 0;
		//customerId1=0;
		while(rset.next())
		{
			OrderOrder_ID = rset.getInt("OrderOrder_ID");
            break;
            // ... additional fields as per your 'Customer' class definition
            //Customer(int custID, String fName, String lName, String phone)
            // Create a new Customer object and add it to the ArrayList
            //return customerId;
            
            //return customerId;
		}
		
		conn.close();

		return OrderOrder_ID;
			
		
		
		
		
		
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		//return -1;
	}
	
	public static void printToppingPopReport() throws SQLException, IOException
	{
		connect_to_db();
		/*
		 * Prints the ToppingPopularity view. Remember that these views
		 * need to exist in your DB, so be sure you've run your createViews.sql
		 * files on your testing DB if you haven't already.
		 * I'm not picky about how they print (other than that it should
		 * be in alphabetical order by name), just make sure it's readable.
		 */
		Statement statement = conn.createStatement();
		String ret = "";
		String query = "Select * From `ToppingPopularity` ;";
		ResultSet rset = statement.executeQuery(query);
		
		while(rset.next())
		{
			String Topping = rset.getString("Topping");
            int ToppingCount = rset.getInt("ToppingCount");
            System.out.println("Topping Name: "+Topping+" | "+"Quantity : "+ToppingCount);
            
            // String lname = rset.getString("CustomerLast_Name");
            // String phonenum	= rset.getString("CustomerPhone_Num");
            // ... additional fields as per your 'Customer' class definition
            //Customer(int custID, String fName, String lName, String phone)
            // Create a new Customer object and add it to the ArrayList
            //return customerId;
            
            //return customerId;
		}
		
		
		
		
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}
	
	public static void printProfitByPizzaReport() throws SQLException, IOException
	{
		connect_to_db();
		/*
		 * Prints the ProfitByPizza view. Remember that these views
		 * need to exist in your DB, so be sure you've run your createViews.sql
		 * files on your testing DB if you haven't already.
		 * I'm not picky about how they print, just make sure it's readable.
		 */
		Statement statement = conn.createStatement();
		String ret = "";
		String query = "Select * From `ProfitByPizza` ;";
		ResultSet rset = statement.executeQuery(query);
		
		while(rset.next())
		{
			String PizzaCrust = rset.getString("PizzaCrust");
			String PizzaSize = rset.getString("PizzaSize");
			
            int Profit = rset.getInt("Profit");
            String LastOrderDate = rset.getString("LastOrderDate");
            System.out.println("PizzaCrust Name: "+PizzaCrust+" | "+"PizzaSize : "+PizzaSize+" | Profit: "+Profit+" | LastOrderDate: "+LastOrderDate);
            
            // String lname = rset.getString("CustomerLast_Name");
            // String phonenum	= rset.getString("CustomerPhone_Num");
            // ... additional fields as per your 'Customer' class definition
            //Customer(int custID, String fName, String lName, String phone)
            // Create a new Customer object and add it to the ArrayList
            //return customerId;
            
            //return customerId;
		}
		
		
		
		
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}
	
	public static void printProfitByOrderType() throws SQLException, IOException
	{
		connect_to_db();
		/*
		 * Prints the ProfitByOrderType view. Remember that these views
		 * need to exist in your DB, so be sure you've run your createViews.sql
		 * files on your testing DB if you haven't already.
		 * I'm not picky about how they print, just make sure it's readable.
		 */
		Statement statement = conn.createStatement();
		String ret = "";
		String query = "Select * From `ProfitByOrderType` ;";
		ResultSet rset = statement.executeQuery(query);
		
		while(rset.next())
		{
			String CustomerType =  rset.getString("CustomerType");
			String OrderMonth = rset.getString("OrderMonth");
			
            int TotalOrderPrice = rset.getInt("TotalOrderPrice");
            String Profit = rset.getString("Profit");
            System.out.println("Order Type : "+CustomerType+" | "+"OrderMonth : "+OrderMonth+" | TotalOrderPrice: "+TotalOrderPrice+" | Profit: "+Profit);
            
            // String lname = rset.getString("CustomerLast_Name");
            // String phonenum	= rset.getString("CustomerPhone_Num");
            // ... additional fields as per your 'Customer' class definition
            //Customer(int custID, String fName, String lName, String phone)
            // Create a new Customer object and add it to the ArrayList
            //return customerId;
            
            //return customerId;
		}
		
	
		
		
		
		
		//DO NOT FORGET TO CLOSE YOUR CONNECTION	
	}
	
	public static double getCusPriceTop(int topid,int extra) throws SQLException, IOException
	{
		
		connect_to_db();
		String colname = "";
		
		Statement statement = conn.createStatement();
		String ret = "";
		String query = "Select ToppingCus_Price From `topping` WHERE ToppingTop_ID = "+topid+";";
		ResultSet rset = statement.executeQuery(query);
		double ans =0;
		
		while(rset.next())
		{
			
			 ans = rset.getDouble("ToppingCus_Price");
            
		}
		if (extra ==1) {
			ans = ans *2;
		}
		
		return ans;
		//return ans;
	}
	
	public static double getBusPriceTop(int topid,int extra) throws SQLException, IOException
	{
		
		connect_to_db();
		String colname = "";
		
		Statement statement = conn.createStatement();
		String ret = "";
		String query = "Select ToppingBus_Price From `topping` WHERE ToppingTop_ID = "+topid+";";
		ResultSet rset = statement.executeQuery(query);
		double ans =0;
		
		while(rset.next())
		{
			
			 ans = rset.getDouble("ToppingBus_Price");
            
		}
		if (extra ==1) {
			ans = ans *2;
		}
		
		return ans;
	}
	
	public static Discount 	getdiscount(int discuountid) throws SQLException, IOException{
		
		connect_to_db();
		Discount d = null;
		Statement statement = conn.createStatement();
		String ret = "";
		String query = "Select * From `discount` WHERE DiscountDisc_ID = "+discuountid+";";
		ResultSet rset = statement.executeQuery(query);
		double ans =0;
		double DiscountDisc_Amt =0;
		double DiscountDisc_Per =0;
		String DiscountDisc_Name ="";
		int DiscountDisc_ID =0;
		while (rset.next()) {
			 DiscountDisc_ID = rset.getInt("DiscountDisc_ID");
			DiscountDisc_Name = rset.getString("DiscountDisc_Name");
			 DiscountDisc_Amt = rset.getDouble("DiscountDisc_Amt");
			 DiscountDisc_Per = rset.getDouble("DiscountDisc_Per");
		}
		//public Discount(int discountID, String discountName, double amount, boolean isPercent)
		if(DiscountDisc_Amt==0) {
		 d = new Discount(DiscountDisc_ID,DiscountDisc_Name,DiscountDisc_Per,false);
		}
		else {
		 d = new Discount(DiscountDisc_ID,DiscountDisc_Name,DiscountDisc_Amt,true);
		}
		return d;
		
	}
	
	public static void Ordtype(Order o,String address) throws SQLException, IOException{
		connect_to_db();
		
		if(o.getOrderType()=="DineIn") {
//			INSERT INTO dine_in
//			(Dine_InOrder_ID,Dine_InTable_Number)
//			VALUES
//			(2,4);
			String query = "INSERT INTO dine_in (Dine_InOrder_ID,Dine_InTable_Number) VALUES (?,?);";
			PreparedStatement statement = conn.prepareStatement(query);
		    statement.setInt(1, o.getOrderID());
		    statement.setInt(2, 4);
//		    statement.setString(3,c.getLName() );
//		    statement.setString(4, c.getPhone());
		    statement.executeUpdate();
			
		
		/*
		 * This should add a customer to the database
		 */
				
		
		conn.close();
		
		}
		else if(o.getOrderType()=="Delivery"){
			//System.out.println(o.getOrderType());
			//System.out.println(o.getOrderID());
			//System.out.println(address);
			String query = "INSERT INTO delivery (DeliveryOrder_ID,DeliveryStreet_Name,DeliveryCity ,DeliveryState,DeliveryZip_Code) VALUES (?,?,?,?,?);";
			PreparedStatement statement = conn.prepareStatement(query);
			
		    statement.setInt(1, o.getOrderID());
		    statement.setString(2, address);
		    statement.setString(3, "");
		    statement.setString(4, "");
		    statement.setInt(5, 0);
//		    statement.setString(3,c.getLName() );
//		    statement.setString(4, c.getPhone());
		    
		    statement.executeUpdate();
//		    INSERT INTO delivery
//		    (DeliveryOrder_ID,DeliveryStreet_Name,DeliveryCity
//		    ,DeliveryState,DeliveryZip_Code)
//		    VALUES
//			
		
		/*
		 * This should add a customer to the database
		 */
				
		
		conn.close();
		}
		else if (o.getOrderType()=="PickUp") {
			
			String query = "INSERT INTO pick_up (Pick_UpOrder_ID,Pick_UpIsPicked_Up) VALUES (?,?);";
			PreparedStatement statement = conn.prepareStatement(query);
		    statement.setInt(1, o.getOrderID());
		    statement.setInt(2, 1);
//		    statement.setString(3,c.getLName() );
//		    statement.setString(4, c.getPhone());
		    statement.executeUpdate();
//			INSERT INTO pick_up
//			(Pick_UpOrder_ID,Pick_UpIsPicked_Up)
//			VALUES
//			(3,1);
			conn.close();
			
		}
	}
	
	public static void UpdatePizzaTopping (Pizza P) throws SQLException, IOException{
		connect_to_db();
		
		
	
	}
	
	
	public static Topping t ( int p) throws  SQLException, IOException {
		connect_to_db();
		
		
			Statement statement = conn.createStatement();
			String ret = "";
			String query = "Select * From `topping` WHERE ToppingTop_ID = "+p+";";
			ResultSet rset = statement.executeQuery(query);
			int ToppingTop_ID =0;
			String ToppingTop_Name ="";
			double ToppingCus_Price =0;
			double ToppingBus_Price=0;
			int ToppingMin_Inv_Level=0;
			int ToppingCurrent_Inv_Level=0;
			double ToppingPersonal_Amt=0;
			double ToppingMedium_Amt=0;
			double ToppingLarge_Amt=0;
			double ToppingXL_Amt=0;
			while (rset.next()) {
				ToppingTop_ID =rset.getInt("ToppingTop_ID");
				ToppingTop_Name = rset.getString("ToppingTop_Name");
				ToppingCus_Price = rset.getDouble("ToppingCus_Price");
				ToppingBus_Price = rset.getDouble("ToppingBus_Price");
				ToppingMin_Inv_Level = rset.getInt("ToppingMin_Inv_Level");
				ToppingCurrent_Inv_Level = rset.getInt("ToppingCurrent_Inv_Level");
				ToppingPersonal_Amt = rset.getDouble("ToppingPersonal_Amt");
				ToppingMedium_Amt = rset.getDouble("ToppingMedium_Amt");
				ToppingLarge_Amt = rset.getDouble("ToppingLarge_Amt");
				ToppingXL_Amt = rset.getDouble("ToppingXL_Amt");
			}
//		Topping(int topID, String topName, double perAMT, double medAMT, double lgAMT, double xLAMT,
//				double custPrice, double busPrice, int minINVT, int curINVT) 
		Topping t=null;
		 t= new Topping(ToppingTop_ID,ToppingTop_Name,ToppingPersonal_Amt,ToppingMedium_Amt,ToppingLarge_Amt,ToppingXL_Amt,ToppingCus_Price,ToppingBus_Price,ToppingMin_Inv_Level,ToppingCurrent_Inv_Level);
		conn.close();
		 return t;
		//return null;
		
		
	}
	public static void pizzatoppings(int pizzaid,int topid,int extra) throws  SQLException, IOException{
		connect_to_db();
		
		
		String query = "INSERT INTO pizzatopping (PizzaToppingPizza_ID,PizzaToppingTop_Id,PizzaToppingIs_DoubleQuantity)"
				+ " VALUES (?,?,?);";
		PreparedStatement statement = conn.prepareStatement(query);
	    statement.setInt(1, pizzaid);
	    statement.setInt(2, topid);
	    statement.setInt(3, extra);
//	    statement.setString(3,c.getLName() );
//	    statement.setString(4, c.getPhone());
	    statement.executeUpdate();
//		INSERT INTO pick_up
//		(Pick_UpOrder_ID,Pick_UpIsPicked_Up)
//		VALUES
//		(3,1);
		conn.close();
	}
	public static int UpdateToppings (int topid, int extra) throws SQLException, IOException{
		
		int flag = 0;
		Topping tops=t(topid);
		int currlevel=0;
		currlevel= tops.getCurINVT();
		if(extra==1) {
			currlevel-=2;
		}
		else {
			currlevel--;
		}
		if(currlevel>-1) {
			connect_to_db();
		String query = "Update  `topping` set ToppingCurrent_Inv_Level = ?  WHERE ToppingTop_ID = ?;";
		PreparedStatement statement = conn.prepareStatement(query);
		statement.setInt(1, currlevel);
		statement.setInt(2, topid);
		statement.executeUpdate();
		flag=1;
		conn.close();
		}
		else {
			System.out.println("Sorry we ran out of toppings. Kindly choose another");
		}
		return flag;
		
	}
	
	

}