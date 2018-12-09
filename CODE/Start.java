import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Start extends ConsoleCalls{
	private static String Temp;
	private static ScanOBJ ConsoleReader = new ScanOBJ();
	private static Login LOGIN;
	private static DataBucket DataContainer = new DataBucket();
	private static DataBucket UserContainer;
	private static Boolean pass = false, decide;
	private static Documenter Logger;

	public static void main(String[] args) throws IOException {
		//Begin Program and Login|Register Menu
		
		System.out.println("Program Start");
		//Login | Register
		CheckForfolers();
		LogReg(); 
		//Begin main program menu 
		while(true){
			Temp = ConsoleReader.READ();
			if(Temp.equalsIgnoreCase("Logout")){
				WantToSave();
				Logger.AddLog("LOGOUT: " + DataContainer.GetUserName() + " has Logged Out");
				Logger.PrintLog();
				LogReg(); 
			}
			else if(Temp.equalsIgnoreCase("Exit")){
				WantToSave();
				Logger.AddLog("EXIT: " + DataContainer.GetUserName() + " has Exited Out");
				Logger.PrintLog();
				Selection(Temp, Logger, DataContainer);
			}
			
			else if(Temp.equalsIgnoreCase("Save")){
				SaveTable();
			}
			
			else{
				System.out.println(UserContainer.GetPermission());
				Selection(Temp, Logger, DataContainer);
			}
		}
		
	}
/**
 * Used to setup the login and allows the user to login or register into the database
 * @throws IOException
 */
	private static void LogReg() throws IOException{
		LOGIN = new Login();
		pass = false;
		while(!pass){
			//Register a username and password into the databse
			System.out.println("Login|Register");
			Temp = ConsoleReader.READ();
			if(Temp.equalsIgnoreCase("Register")){
				System.out.println("Register");
				EnterInfo();
				LOGIN.LoginRegister(DataContainer);
			}
			
			//Login into the database
			else if(Temp.equalsIgnoreCase("Login")){
				System.out.println("Login");
				LoginNow();
				pass = true;
			}
			
			else if(Temp.equalsIgnoreCase("exit")){
				System.out.println("Program Closed");
				System.exit(1);
			}
			
			else if(Temp.equalsIgnoreCase("pass!")){
				pass = true;
			}
		}
		Logger = new Documenter();
		Logger.AddLog("LOGIN: " + DataContainer.GetUserName() + " has Logged in");
		CreateTable();
		System.out.println("Welcome " + DataContainer.GetUserName() + " to begin Select Add|Delete|Find|Edit");
	}
	
/**
 * Used to make sure the login is correct and if not it loops until it is coorect
 */
	private static void LoginNow(){
		EnterInfo();
		while(!(LOGIN.LoginCheck(DataContainer))){
			EnterInfo();
		}
		UserContainer = LOGIN.GetUser(DataContainer); //Return all the users information into the databucket
		DataContainer.SetPermission(UserContainer.GetPermission());
	}
	
/**
 * Used to enter the username and password
 */
	private static void EnterInfo(){
		System.out.print("Please enter your Username: ");
		DataContainer.SetUserName(ConsoleReader.READ());
		System.out.print("Please enter your Password: ");
		DataContainer.SetPassword(ConsoleReader.READ());
	}

/**
 * Creates a Table at start but will either find an existing table from 
 * the HashTable folder or will create a new one.
 * @throws IOException
 */
	private static void CreateTable() throws IOException{
		String filename = "HashTable\\" + DataContainer.GetUserName() + ".ser";
		if(CheckTable(filename)){
			FileInputStream fileIn = new FileInputStream(filename);
		    try {    	
		        ObjectInputStream in = new ObjectInputStream(fileIn);
		        DataContainer.SetTable((HashTable) in.readObject()); 
		        in.close();
		        fileIn.close();
		    } 
		    catch (IOException i){
		    	i.printStackTrace();
		        return;
		    } 
		    catch (ClassNotFoundException c){
		        System.out.println("Table could not be found");
		        return;
		    }
		}
		else{
	    	HashTable Table = new HashTable(DataContainer); 
	    	DataContainer.SetTable(Table);
	        System.out.println("A new Table has been created for " + DataContainer.GetUserName());
			SaveTable();
		}
	}
	
/**
 * Save the Table into a file located in the HashTable Folder
 */
	private static void SaveTable(){
		String filename = "HashTable\\" + DataContainer.GetUserName() + ".ser";
		try {
			FileOutputStream fileOut = new FileOutputStream(filename);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(DataContainer.GetTable());
			out.close();
			fileOut.close();
			System.out.println("Table Saved " + filename);
		   } 
		catch (IOException i) {
		      i.printStackTrace();
		}
	}

/**
 * Checks if there exists a table from the requested User
 * All Files Located in the HashTable Folder
 * @param Filename
 * @return
 * @throws IOException
 */
	private static Boolean CheckTable(String Filename) throws IOException{
		decide = false;
		try (Stream<Path> filePathStream=Files.walk(Paths.get("HashTable"))) {
			filePathStream.forEach(filePath -> {
				if (Files.isRegularFile(filePath)) {
					if(filePath.toString().equals(Filename))
						decide = true;
		    	}
			});
			filePathStream.close();
		}
		
		catch(Exception x){
			System.out.println("Error occured when searching for files in folder");
		}
		return decide;
	}
	
/**
 * Tells user if they want to save the database
 */
	private static void WantToSave(){
		String choice = "";
		System.out.println("Do you want to save your work? Yes | No");
		while(!choice.equalsIgnoreCase("Yes") && !choice.equalsIgnoreCase("No"))
			choice = ConsoleReader.READ();
		if(choice.equalsIgnoreCase("Yes")){
			Logger.AddLog("SAVETABLE: " + DataContainer.GetUserName() + " has saved the table" );
			SaveTable();
		}
		else
			System.out.println("Work not saved");
	}
	
/**
 * Makes sure the proper folders exist
 */
	private static void CheckForfolers(){
		String Name = "DataText";
		File folder = new File(Name);
		//Check for folder
		if (folder.exists() && folder.isDirectory())
		   System.out.println("DataText Folder Ready");
		//Create Folder
		else
			MakeDIR(Name);
		
		Name = "HashTable";
		folder = new File(Name);
		//Check for folder
		if (folder.exists() && folder.isDirectory())
		   System.out.println("HashTable Folder Ready");
		//Create Folder
		else
			MakeDIR(Name);
		
		Name = "Image";
	    folder = new File(Name);
		//Check for folder
		if (folder.exists() && folder.isDirectory())
		   System.out.println("Image Folder Ready");
		//Create Folder
		else
			MakeDIR(Name);
		
		Name = "LOG";
	    folder = new File(Name);
		//Check for folder
		if (folder.exists() && folder.isDirectory())
		   System.out.println("LOG Folder Ready");
		//Create Folder
		else
			MakeDIR(Name);

	}

/**
 * Make DIR
 * @param Name
 */
	private static void MakeDIR(String Name){
		File dir = new File(Name);
		dir.mkdir();
	}
}
	
	

