package cpsc4620;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import init.DBIniter;
import cpsc4620.DBNinja;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;   
import java.text.DateFormat;  
import java.text.SimpleDateFormat;  
import java.util.Date;  
import java.util.Calendar;  

/*
 * This file is where the front end magic happens.
 * 
 * You will have to write the functionality of each of these menu options' respective functions.
 * 
 * This file should need to access your DB at all, it should make calls to the DBNinja that will do all the connections.
 * 
 * You can add and remove functions as you see necessary. But you MUST have all 8 menu functions (9 including exit)
 * 
 * Simply removing menu functions because you don't know how to implement it will result in a major error penalty (akin to your program crashing)
 * 
 * Speaking of crashing. Your program shouldn't do it. Use exceptions, or if statements, or whatever it is you need to do to keep your program from breaking.
 * 
 * 
 */
//private static Connection connection = null;
public class Menu {
	public static void main(String[] args) throws SQLException, IOException {
		
		System.out.println("Welcome to Taylor's Pizzeria!");
		
		int menu_option = 0;

		// present a menu of options and take their selection
		
		PrintMenu();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		DBIniter.init();
		String option = reader.readLine();
		menu_option = Integer.parseInt(option);

		while (menu_option != 9) {
			switch (menu_option) {
			case 1:// enter order
				EnterOrder();
				break;
			case 2:// view customers
				viewCustomers();
				break;
			case 3:// enter customer
				EnterCustomer();
				break;
			case 4:// view order
				// open/closed/date
				ViewOrders();
				break;
			case 5:// mark order as complete
				MarkOrderAsComplete();
				break;
			case 6:// view inventory levels
				ViewInventoryLevels();
				break;
			case 7:// add to inventory
				AddInventory();
				break;
			case 8:// view reports
				PrintReports();
				break;
			}
			PrintMenu();
			option = reader.readLine();
			menu_option = Integer.parseInt(option);
		}

	}

	public static void PrintMenu() {
		System.out.println("\n\nPlease enter a menu option:");
		System.out.println("1. Enter a new order");
		System.out.println("2. View Customers ");
		System.out.println("3. Enter a new Customer ");
		System.out.println("4. View orders");
		System.out.println("5. Mark an order as completed");
		System.out.println("6. View Inventory Levels");
		System.out.println("7. Add Inventory");
		System.out.println("8. View Reports");
		System.out.println("9. Exit\n\n");
		System.out.println("Enter your option: ");
	}

	// allow for a new order to be placed
	public static void EnterOrder() throws SQLException, IOException 
	{	
		ArrayList<Pizza> pizzalist = new ArrayList<Pizza>();
		System.out.println("Are you existing customer? Answer: y/n");
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		DBNinja dbninja = new DBNinja();
		String option = reader.readLine();
		int ordernum = dbninja.getNextOrderID()+1;
		String ordertype="";
		String address="";
		int morePizzas =1;
		String option2="";
		int newcustomer=0;
		if (option.equals("y")) {
			System.out.println("Loading Customers data....");
			viewCustomers();
			System.out.println("Please select your customer ID? Enter your ID:");
			
			option2=reader.readLine();
			//int custID = Integer.parseInt(option2);
			System.out.println("Is the Order for: \n 1. Dine-in 2. Pick-up 3. Delivery");
			 ordertype = reader.readLine();
			if(ordertype.equals("1")) {
				ordertype = "DineIn";
			}
			else if(ordertype.equals("2")) {
				ordertype = "PickUp";
			}
			else {
				ordertype ="Delivery";
				System.out.println("Enter your address:");
				address = reader.readLine();
			}
			//-------------------------------------------------------------------------------------
			while(morePizzas!=-1) {
			
				
				Pizza p = buildPizza(ordernum);
				
			
				int flag =1;
				int value = 1;
			while(value!=-1) {//outer loop for addition
				System.out.println("Do you want to add PIZZA discounts? Enter y/n:");
				
				String discount =reader.readLine();
				if(discount.equals("y")) {
					while(flag!=-1) {
					for (Discount d:dbninja.getDiscountList()) {
					
					System.out.println(d.toString());//displays the output of discounts.
					}
				//	public Discount(int discountID, String discountName, double amount, boolean isPercent)
				
			//dbninja.usePizzaDiscount(p, 1);
				System.out.println("Enter the DiscountID or Enter -1 to stop adding discounts");
				String discountid = reader.readLine();
				int disid = Integer.parseInt(discountid);
				Discount d = dbninja.getdiscount(disid);
				
				
				if(discountid.equals("-1")) {
					flag=-1;
					//p.addDiscounts(d);
					
						}
				else {
					if(d.isPercent()) {
						d.setAmount(d.getAmount()*0.01);
						p.addDiscounts(d);
					}
					else {
						p.addDiscounts(d);
					}
					
				}
				
					}
				}
				else {value=-1;}
				}
					pizzalist.add(p);
					
					System.out.println("Enter -1 to stop adding pizzas... Enter anything else to continue adding more pizza");
					String stoppizzadisc = reader.readLine();
					if(stoppizzadisc.equals("-1")) {
						value = -1;
						morePizzas = -1;}

				}
			

		}
		
		//----------------------------------------------------------------------	
			
		
		else {
			EnterCustomer();
			newcustomer=1;
			System.out.println("Is the Order for: \n 1. Dine-in 2. Pick-up 3. Delivery");
			 ordertype = reader.readLine();
			if(ordertype.equals("1")) {
				ordertype = "DineIn";
			}
			else if(ordertype.equals("2")) {
				ordertype = "PickUp";
			}
			else {
				ordertype ="Delivery";
				System.out.println("Enter your address:");
				address = reader.readLine();
			}
			//-------------------------------------------------------------------------------------
			while(morePizzas!=-1) {
			
				
				Pizza p = buildPizza(ordernum);
				
			
				int flag =1;
				int value = 1;
			while(value!=-1) {//outer loop for addition
				System.out.println("Do you want to add PIZZA discounts? Enter y/n:");
				
				String discount =reader.readLine();
				if(discount.equals("y")) {
					while(flag!=-1) {
					for (Discount d:dbninja.getDiscountList()) {
					
					System.out.println(d.toString());//displays the output of discounts.
					}
				//	public Discount(int discountID, String discountName, double amount, boolean isPercent)
				
			//dbninja.usePizzaDiscount(p, 1);
				System.out.println("Enter the DiscountID or Enter -1 to stop adding discounts");
				String discountid = reader.readLine();
				int disid = Integer.parseInt(discountid);
				Discount d = dbninja.getdiscount(disid);
				
				
				if(discountid.equals("-1")) {
					flag=-1;
					//p.addDiscounts(d);
					
						}
				else {
					if(d.isPercent()) {
						d.setAmount(d.getAmount()*0.01);
						p.addDiscounts(d);
					}
					else {
						p.addDiscounts(d);
					}
				}
				
					}
				}
				else {value=-1;}
				}
					pizzalist.add(p);
					
					System.out.println("Enter -1 to stop adding pizzas... Enter anything else to continue adding more pizza");
					String stoppizzadisc = reader.readLine();
					if(stoppizzadisc.equals("-1")) {
						value = -1;
						morePizzas = -1;}

				}
			
//				
		}//------------------------------------------------------------------------------------------------
		// Order(int orderID, int custID, String orderType, String date, double custPrice, double busPrice, int iscomplete)
		
		Date date = Calendar.getInstance().getTime();  
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
        String strDate = dateFormat.format(date);  
        double final_amount_cus = 0.0;
        double final_amount_bus =0;
        for(Pizza p : pizzalist) {
        	final_amount_cus+=p.getCustPrice();
        	final_amount_bus+=p.getBusPrice();
        }
        int custID=0;
        if(newcustomer==0) {
        	custID = Integer.parseInt(option2);
        }
        else {
        	custID = dbninja.getNextCustomerID();
        }
        Order val = new Order(ordernum,custID,ordertype,strDate,final_amount_cus,final_amount_bus,0);
		for(Pizza p : pizzalist) {
    	val.addPizza(p);
    }

		int value =1;
		int flag =1;
		while(value!=-1) {//outer loop for addition
			System.out.println("Do you want to add order discounts? Enter y/n:");
			
			String discount =reader.readLine();
			if(discount.equals("y")) {
				while(flag!=-1) {
				for (Discount d:dbninja.getDiscountList()) {
				
				System.out.println(d.toString());//displays the output of discounts.
				}
			
		//dbninja.usePizzaDiscount(p, 1);
			System.out.println("Enter the DiscountID or Enter -1 to stop adding discounts");
			String discountid = reader.readLine();
			
			int disid = Integer.parseInt(discountid);
			Discount d = dbninja.getdiscount(disid);
			if(discountid.equals("-1")) {
				flag=-1;
				
					}
			else {
				val.addDiscount(d);
			}
				}
			}
			else {value=-1;}
			}
		

		dbninja.addOrder(val);
		int c =0;
		for(Pizza p : pizzalist) {
			p.setPizzaID(p.getPizzaID()+c);
		dbninja.addPizza(p);
		c+=1;
		}
		dbninja.Ordtype(val, address);
		for (Pizza p: pizzalist) {
			int pizzaid = p.getPizzaID();
			for( Topping t: p.getToppings()) {
			int topid=	t.getTopID();
			boolean[] extra = p.getIsDoubleArray();
			 boolean value1 = extra[topid-1];
			 int extra1=0;
			 			if(value1==true) {
			 					extra1=1;
			 									}
			 							else {
			 									extra1=0;
			 										}
			 			dbninja.pizzatoppings(pizzaid,topid,extra1);
			
			}
		}
		int pizzaids=0;
		int discountids=0;
		for (Pizza pizza:pizzalist) {
			pizzaids=pizza.getPizzaID();
			if(pizza.getDiscounts().size()!=0) {
				for (Discount d:pizza.getDiscounts()) {
					discountids=d.getDiscountID();
					dbninja.usePizzaDiscount(pizza,d);
				}

			}
					}
		if(val.getDiscountList().size()!=0) {
			for(Discount d:val.getDiscountList()) {
				
				dbninja.useOrderDiscount(val, d);
			}	
		}
		
		
		
	
		//dbninja.pizzatoppings();
		
		//dbninja.
		
		/*
		 * EnterOrder should do the following:
		 * Ask if the order is for an existing customer -> If yes, select the customer. If no -> create the customer (as if the menu option 2 was selected).
		 * 
		 * Ask if the order is delivery, pickup, or dinein (ask for orderType specific information when needed)
		 * 
		 * Build the pizza (there's a function for this)
		 * 
		 * ask if more pizzas should be be created. if yes, go back to building your pizza. 
		 * 
		 * Apply order discounts as needed (including to the DB)
		 * 
		 * apply the pizza to the order (including to the DB)
		 * 
		 * return to menu
		 */
		
		
		
		System.out.println("Finished adding order...Returning to menu...");
	}
	
	
	public static void viewCustomers() throws SQLException, IOException
	{
		/*
		 * Simply print out all of the customers from the database. 
		 */
		
		DBNinja dbninja =new DBNinja();
		ArrayList<Customer> custsAns = new ArrayList<Customer>();
		custsAns =	dbninja.getCustomerList();
		for (Customer customer : custsAns) {
			System.out.println(	customer.toString());
		}
		
		
		
	}
	

	// Enter a new customer in the database
	public static void EnterCustomer() throws SQLException, IOException 
	{
		/*
		 * Ask what the name of the customer is. YOU MUST TELL ME (the grader) HOW TO FORMAT THE FIRST NAME, LAST NAME, AND PHONE NUMBER.
		 * If you ask for first and last name one at a time, tell me to insert First name <enter> Last Name (or separate them by different print statements)
		 * If you want them in the same line, tell me (First Name <space> Last Name).
		 * 
		 * same with phone number. If there's hyphens, tell me XXX-XXX-XXXX. For spaces, XXX XXX XXXX. For nothing XXXXXXXXXXXX.
		 * 
		 * I don't care what the format is as long as you tell me what it is, but if I have to guess what your input is I will not be a happy grader
		 * 
		 * Once you get the name and phone number (and anything else your design might have) add it to the DB
		 */
		BufferedReader reader1 = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Please Enter your First Name and Last Name? (First Name <space> Last Name)");
		String input = reader1.readLine();
		String FirstName = "";
		String LastName = "";
		String array[]= input.split("\\s+"); 
		FirstName = array[0];// Splitting the input String with spaces
		LastName = array[1];
		String PhoneNum;
		System.out.println("Please Provide your phone number(format xxxxxxxxxx)");
		//int flagphone=0;
		
		input = reader1.readLine();
		PhoneNum = input;
		//System.out.println("First Name:"+FirstName+" Last Name:"+LastName);
		//System.out.println("Phone Number"+PhoneNum);
		//public Customer(int custID, String fName, String lName, String phone) {
		
		DBNinja dbninja = new DBNinja();
		int custID = dbninja.getNextCustomerID()+1;
		Customer customer = new Customer(custID, FirstName, LastName, PhoneNum);
		dbninja.addCustomer(customer);
		
		
		//Customer c= new Customer();
		
		
		
		

	}

	// View any orders that are not marked as completed
	public static void ViewOrders() throws SQLException, IOException 
	{
	/*
	 * This should be subdivided into two options: print all orders (using simplified view) and print all orders (using simplified view) since a specific date.
	 * 
	 * Once you print the orders (using either sub option) you should then ask which order I want to see in detail
	 * 
	 * When I enter the order, print out all the information about that order, not just the simplified view.
	 * 
	 */
		BufferedReader reader1 = new BufferedReader(new InputStreamReader(System.in));
		
		int flag=0;
		while(flag!=1) {
			System.out.println(" (a) display all orders"+"\n (b) display orders in date");
			DBNinja dbninja =new DBNinja();
			String option = reader1.readLine();
			if (option.equals("a")){
				//toSimplePrint
				ArrayList<Order> custsAns = new ArrayList<Order>();
				custsAns =	dbninja.getCurrentOrders();
				for (Order order : custsAns) {
					System.out.println(	order.toSimplePrint());
				}
				System.out.println("Which order id you want in detail?");
				option = reader1.readLine();
				int menu_option1;
				menu_option1 = Integer.parseInt(option);
				for (Order order : custsAns) {
					if(order.getOrderID()==menu_option1) {
						
						System.out.println(dbninja.typeoforder(order).toString());
					}
					
				}
			
				flag=1;
			
			}
		else {
			System.out.println("What date you want to restrict to ? format yyyy-mm-dd");
			option = reader1.readLine();
			ArrayList<Order> custsAns = new ArrayList<Order>();
			custsAns=dbninja.DateFormatOrders(option);
			for (Order order : custsAns) {
				System.out.println(	order.toSimplePrint());
			}
			System.out.println("Which order id you want in detail?");
			option = reader1.readLine();
			int menu_option1;
			menu_option1 = Integer.parseInt(option);
			for (Order order : custsAns) {
				if(order.getOrderID()== menu_option1) {
					
					System.out.println(dbninja.typeoforder(order).toString());
				}
				
			}
		
			//flag=1;
			
			flag=1;
			
		}
		
	}
		
	}

	
	// When an order is completed, we need to make sure it is marked as complete
	public static void MarkOrderAsComplete() throws SQLException, IOException 
	{
		/*All orders that are created through java (part 3, not the 7 orders from part 2) should start as incomplete
		 * 
		 * When this function is called, you should print all of the orders marked as complete 
		 * and allow the user to choose which of the incomplete orders they wish to mark as complete
		 * 
		 */
		BufferedReader reader1 = new BufferedReader(new InputStreamReader(System.in));
		DBNinja dbninja =new DBNinja();
		//String option = reader1.readLine();
					//toSimplePrint
		int flag=1;
		
			ArrayList<Order> custsAns = new ArrayList<Order>();
			custsAns =	dbninja.getCurrentOrders();
			for (Order order : custsAns) {
				if(order.getIsComplete()==0) {
				System.out.println(	order.toSimplePrint());
				flag=0;
				}
			}
		if(flag!=0) {
			System.out.println("No order is there to update");
		}
				
		else {
		System.out.println("Enter which order you want to update?Enter Order ID:");
		
		String option = reader1.readLine();
		int option1 = Integer.parseInt(option);
		for (Order order : custsAns) {
			if(order.getOrderID()==option1) {
			dbninja.CompleteOrder(order);
			}
		}
		
		}

	}

	// See the list of inventory and it's current level
	public static void ViewInventoryLevels() throws SQLException, IOException 
	{
		//print the inventory. I am really just concerned with the ID, the name, and the current inventory
		
		DBNinja dbninja = new DBNinja();
		dbninja.printInventory();
		
		
		
		
		
	}

	// Select an inventory item and add more to the inventory level to re-stock the
	// inventory
	public static void AddInventory() throws SQLException, IOException 
	{
		/*
		 * This should print the current inventory and then ask the user which topping they want to add more to and how much to add
		 */
		DBNinja dbninja = new DBNinja();
		dbninja.printInventory();
		BufferedReader reader1 = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Which inventory do you want to add? ENter the number: ");
		String option = reader1.readLine();
		ArrayList<Topping> t = new ArrayList<Topping>(); 
		//DBNinja dbninja = new DBNinja();
		t = dbninja.getInventory();
		int topid = Integer.parseInt(option);
		System.out.println("Enter how many units you would like to add?");
		option = reader1.readLine();
		int numberUnits = Integer.parseInt(option);
		for (Topping top : t) {
			if(top.getTopID()== topid) {
				dbninja.AddToInventory(top,numberUnits);
			}
		}
		
		
		
		
		
		
	}

	// A function that builds a pizza. Used in our add new order function
	public static Pizza buildPizza(int orderID) throws SQLException, IOException 
	{
		
		/*
		 * This is a helper function for first menu option.
		 * 
		 * It should ask which size pizza the user wants and the crustType.
		 * 
		 * Once the pizza is created, it should be added to the DB.
		 * 
		 * We also need to add toppings to the pizza. (Which means we not only need to add toppings here, but also our bridge table)
		 * 
		 * We then need to add pizza discounts (again, to here and to the database)
		 * 
		 * Once the discounts are added, we can return the pizza
		 */
		
		DBNinja dbninja = new DBNinja();
		int ordernum = dbninja.getNextOrderID()+1;
		int pizzaid = dbninja.getMaxPizzaID()+1;
//		if(ordernum!=orderID) {
//			
//		}
//		else {
//			
//		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		int value = 1;
		int ord = orderID;
		System.out.println("What is the size of the Pizza? \n 1.Small \n 2.Medium \n 3.Large \n4.X-Large");
		String size = reader.readLine();
		System.out.println("What is the crust of the Pizza? \n 1.Thin \n 2.Original \n 3.Pan \n4.Gluten-Free");
		String crust =reader.readLine();
		ArrayList<Integer> arraytopid = new ArrayList<Integer>();
		ArrayList<Integer> extra = new ArrayList<Integer>();
		
		while(value!=-1) {
			
			System.out.println("Loading topping list....");
			ViewInventoryLevels();
			System.out.println("Which topping do you want to add to your Pizza?" +" Enter Topping ID. Enter -1 to stop adding toppings");
			String topid = reader.readLine();
			//System.out.println("Do you want extra Topping?" +"Enter y/n?");
			//String extras =reader.readLine();
//			if(extras.equals("y")) {
//				extra.add(1);
//			}
//			else {
//				extra.add(0);
//			}
			//update the topping table as the user enters the toping id and also consider the double factor and subtract twice
			//Topping tops=t(topid);
			
			int topid1 = Integer.parseInt(topid);
			int ext=0;
			//Topping tops=dbninja.t(topid1);
			
			if(topid1!=-1) {
				
				System.out.println("Do you want extra Topping?" +"Enter y/n?");
				String extras =reader.readLine();
				if(extras.equals("y")) {
					extra.add(1);
					ext=1;
				}
				else {
					extra.add(0);
					ext=1;
				}
				if(dbninja.UpdateToppings(topid1, ext) == 1) {
					arraytopid.add(topid1);
				}
				
			
			}
			else 
			{
				value=-1;
				
			}
			
			
		}
		
						
		
			//System.out.println("Do you want add")
		
		//Pizza(int pizzaID, String size, String crustType, int orderID, String pizzaState, String pizzaDate,
			//	double custPrice, double busPrice)
		//DBNinja dbninja = new DBNinja();
		double topping_price_cus = 0;
		double topping_price_bus = 0;
		if(size.equals("3")) {
			size = "large";
		}
		else if (size.equals("2")) {
			size = "medium";
		}
		else if (size.equals("1")) {
			size = "small";
		}
		else if (size.equals("4")) {
			size = "x-large";
		}
		
		if(crust.equals("4")) {
			crust = "Gluten-Free";
		}
		else if (crust.equals("2")) {
			crust ="Original";
		}
		else if (crust.equals("3")) {
			crust ="Pan";
		}
		else if (crust.equals("1")) {
			crust ="Thin";
		}
		//dbninja.getBaseCustPrice(size, crust);}
		//size and //crust
		double CusPrice = dbninja.getBaseCustPrice(size, crust);
		double BusPrice = dbninja.getBaseBusPrice(size, crust);
		Topping top = null;
		for (int i = 0; i < arraytopid.size(); i++) {
			int a = arraytopid.get(i);
			int isdouble = extra.get(i);
			//System.out.println(isdouble);
			topping_price_cus += dbninja.getCusPriceTop(a, isdouble);
			topping_price_bus += dbninja.getBusPriceTop(a,isdouble);
			 
			
			//dbninja.pizzatoppings(pizzaid,a,isdouble);
			
		}
		
		
		double final_cus_price = topping_price_cus + CusPrice;
		double final_bus_price = BusPrice + topping_price_bus;
		
		// Pizza(int pizzaID, String size, String crustType, int orderID, String pizzaState, String pizzaDate,
		//double custPrice, double busPrice)
		String pizzaState = "Not-Completed";
		Date date = Calendar.getInstance().getTime();  
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
        String strDate = dateFormat.format(date);  
		Pizza ret = new Pizza(pizzaid,size,crust,ord,pizzaState,strDate,final_cus_price,final_bus_price);
		
		ArrayList<Topping> toplist = new ArrayList<Topping>();
		for (int i = 0; i < arraytopid.size(); i++) {
			int a = arraytopid.get(i);
			int isdouble = extra.get(i);
			
			top = dbninja.t(a);
			toplist.add(top);
			if(isdouble==0) {
				ret.modifyDoubledArray(a-1, false);
			}
			
			else {
				ret.modifyDoubledArray(a-1, true);
			}
			//dbninja.pizzatoppings(pizzaid,a,isdouble);
			
		}
		ret.setToppings(toplist);
		
		
		return ret;
	}
	
	private static int getTopIndexFromList(int TopID, ArrayList<Topping> tops) throws SQLException,IOException
	{
		/*
		 * This is a helper function I used to get a topping index from a list of toppings
		 * It's very possible you never need to use a function like this
		 * 
		 */
		int ret = -1;
		
		
		
		return ret;
	}
	
	
	public static void PrintReports() throws SQLException, NumberFormatException, IOException
	{
		/*
		 * This function calls the DBNinja functions to print the three reports.
		 * 
		 * You should ask the user which report to print
		 */
		DBNinja dbninja = new DBNinja();
		System.out.println("Which Reports you want to see? \n 1. Topping Report \n 2. ProfitByPizza \n 3. ProfitByOrderType");
		BufferedReader reader1 = new BufferedReader(new InputStreamReader(System.in));
		String option = reader1.readLine();
		
		if (option.equals("1")) {
		dbninja.printToppingPopReport();
		}
		else if(option.equals("2")) {
		//dbninja.printToppingPopReport());
		dbninja.printProfitByPizzaReport();
		}
		else {
		dbninja.printProfitByOrderType();
		}
//		double value = dbninja.getBaseBusPrice("medium","Original");
//		System.out.println(value);//size and crust
//		
	}

}



//Prompt - NO CODE SHOULD TAKE PLACE BELOW THIS LINE
//DO NOT EDIT ANYTHING BELOW HERE, I NEED IT FOR MY TESTING DIRECTORY. IF YOU EDIT SOMETHING BELOW, IT BREAKS MY TESTER WHICH MEANS YOU DO NOT GET GRADED (0)

/*
CPSC 4620 Project: Part 3 â€“ Java Application Due: Thursday 11/30 @ 11:59 pm 125 pts

For this part of the project you will complete an application that will interact with your database. Much of the code is already completed, you will just need to handle the functionality to retrieve information from your database and save information to the database.
Note, this program does little to no verification of the input that is entered in the interface or passed to the objects in constructors or setters. This means that any junk data you enter will not be caught and will propagate to your database, if it does not cause an exception. Be careful with the data you enter! In the real world, this program would be much more robust and much more complex.

Program Requirements:

Add a new order to the database: You must be able to add a new order with pizzas to the database. The user interface is there to handle all of the input, and it creates the Order object in the code. It then calls DBNinja.addOrder(order) to save the order to the database. You will need to complete addOrder. Remember addOrder will include adding the order as well as the pizzas and their toppings. Since you are adding a new order, the inventory level for any toppings used will need to be updated. You need to check to see if there is inventory available for each topping as it is added to the pizza. You can not let the inventory level go negative for this project. To complete this operation, DBNinja must also be able to return a list of the available toppings and the list of known customers, both of which must be ordered appropropriately.

View Customers: This option will display each customer and their associated information. The customer information must be ordered by last name, first name and phone number. The user interface exists for this, it just needs the functionality in DBNinja

Enter a new customer: The program must be able to add the information for a new customer in the database. Again, the user interface for this exists, and it creates the Customer object and passes it to DBNinja to be saved to the database. You need to write the code to add this customer to the database. You do need to edit the prompt for the user interface in Menu.java to specify the format for the phone number, to make sure it matches the format in your database.

View orders: The program must be able to display orders and be sorted by order date/time from most recent to oldest. The program should be able to display open orders, all the completed orders or just the completed order since a specific date (inclusive) The user interface exists for this, it just needs the functionality in DBNinja

Mark an order as completed: Once the kitchen has finished prepping an order, they need to be able to mark it as completed. When an order is marked as completed, all of the pizzas should be marked as completed in the database. Open orders should be sorted as described above for option #4. Again, the user interface exists for this, it just needs the functionality in DBNinja

View Inventory Levels: This option will display each topping and its current inventory level. The toppings should be sorted in alphabetical order. Again, the user interface exists for this, it just needs the functionality in DBNinja

Add Inventory: When the inventory level of an item runs low, the restaurant will restock that item. When they do so, they need to enter into the inventory how much of that item was added. They will select a topping and then say how many units were added. Note: this is not creating a new topping, just updating the inventory level. Make sure that the inventory list is sorted as described in option #6. Again, the user interface exists for this, it just needs the functionality in DBNinja

View Reports: The program must be able to run the 3 profitability reports using the views you created in Part 2. Again, the user interface exists for this, it just needs the functionality in DBNinja

Modify the package DBConnector to contain your database connection information, this is the same information you use to connect to the database via MySQL Workbench. You will use DBNinja.connect_to_db to open a connection to the database. Be aware of how many open database connections you make and make sure the database is properly closed!
Your code needs to be secure, so any time you are adding any sort of parameter to your query that is a String, you need to use PreparedStatements to prevent against SQL injections attacks. If your query does not involve any parameters, or if your queries parameters are not coming from a String variable, then you can use a regular Statement instead.

The Files: Start by downloading the starter code files from Canvas. You will see that the user interface and the java interfaces and classes that you need for the assignment are already completed. Review all these files to familiarize yourself with them. They contain comments with instructions for what to complete. You should not need to change the user interface except to change prompts to the user to specify data formats (i.e. dashes in phone number) so it matches your database. You also should not need to change the entity object code, unless you want to remove any ID fields that you did not add to your database.

You could also leave the ID fields in place and just ignore them. If you have any data types that donâ€™t match (i.e. string size options as integers instead of strings), make the conversion when you pull the information from the database or add it to the database. You need to handle data type differences at that time anyway, so it makes sense to do it then instead of making changes to all of the files to handle the different data type or format.

The Menu.java class contains the actual user interface. This code will present the user with a menu of options, gather the necessary inputs, create the objects, and call the necessary functions in DBNinja. Again, you will not need to make changes to this file except to change the prompt to tell me what format you expect the phone number in (with or without dashes).

There is also a static class called DBNinja. This will be the actual class that connects to the database. This is where most of the work will be done. You will need to complete the methods to accomplish the tasks specified.

Also in DBNinja, there are several public static strings for different crusts, sizes and order types. By defining these in one place and always using those strings we can ensure consistency in our data and in our comparisons. You donâ€™t want to have â€œSMALLâ€� â€œsmallâ€� â€œSmallâ€� and â€œPersonalâ€� in your database so it is important to stay consistent. These strings will help with that. You can change what these strings say in DBNinja to match your database, as all other code refers to these public static strings.

Start by changing the class attributes in DBConnector that contain the data to connect to the database. You will need to provide your database name, username and password. All of this is available is available in the Chapter 15 lecture materials. Once you have that done, you can begin to build the functions that will interact with the database.

The methods you need to complete are already defined in the DBNinja class and are called by Menu.java, they just need the code. Two functions are completed (getInventory and getTopping), although for a different database design, and are included to show an example of connecting and using a database. You will need to make changes to these methods to get them to work for your database.

Several extra functions are suggested in the DBNinja class. Their functionality will be needed in other methods. By separating them out you can keep your code modular and reduce repeated code. I recommend completing your code with these small individual methods and queries. There are also additional methods suggested in the comments, but without the method template that could be helpful for your program. HINT, make sure you test your SQL queries in MySQL Workbench BEFORE implementing them in codeâ€¦it will save you a lot of debugging time!

If the code in the DBNinja class is completed correctly, then the program should function as intended. Make sure to TEST, to ensure your code works! Remember that you will need to include the MySQL JDBC libraries when building this application. Otherwise you will NOT be able to connect to your database.

Compiling and running your code: The starter code that will compile and â€œrunâ€�, but it will not do anything useful without your additions. Because so much code is being provided, there is no excuse for submitting code that does not compile. Code that does not compile and run will receive a 0, even if the issue is minor and easy to correct.

Help: Use MS Teams to ask questions. Do not wait until the last day to ask questions or get started!

Submission You will submit your assignment on Canvas. Your submission must include: â€¢ Updated DB scripts from Part 2 (all 5 scripts, in a folder, even if some of them are unchanged). â€¢ All of the class code files along with a README file identifying which class files in the starter code you changed. Include the README even if it says â€œI have no special instructions to shareâ€�. â€¢ Zip the DB Scripts, the class files (i.e. the application), and the README file(s) into one compressed ZIP file. No other formats will be accepted. Do not submit the lib directory or an IntellJ or other IDE project, just the code.

Testing your submission Your project will be tested by replacing your DBconnector class with one that connects to a special test server. Then your final SQL files will be run to recreate your database and populate the tables with data. The Java application will then be built with the new DBconnector class and tested.

No late submissions will be accepted for this assignment.*/

//INITNUM: 1823753