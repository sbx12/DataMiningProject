import java.io.PrintWriter;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
/**
 * Handles the login system of the project
 * @author Steven Bedoya
 *
 */
public class Login{
	private PrintWriter OUTPUT;
	private File FILE = new File("DatabaseLogin.txt");
	private Scanner FileReader;
	private DataBucket DataContainer = new DataBucket();
	private DataBucket USERDATA;
	private DataBucket[] UserTable = new DataBucket[100];
	private static ScanOBJ ConsoleReader = new ScanOBJ();
	private int Top = 0;
	
/**
 * Sets up the login by getting all the usernames and password into an array so that it can be read from at the login function
 * @throws FileNotFoundException
 */
	public Login() throws FileNotFoundException{
		FileReader = new Scanner(FILE);
		while(FileReader.hasNext()){
			USERDATA = new DataBucket();
			USERDATA.SetUserName(FileReader.next());
			USERDATA.SetPassword(FileReader.next());
			USERDATA.SetPermission(FileReader.next());
			UserTable[Top++] = USERDATA;
		}
		FileReader.close();
	}

/**
 * Checks if the username and password is coorect
 * @param CHECKTHIS
 * @return True if it is part of the user database, False if not
 */
	public Boolean LoginCheck(DataBucket CHECKTHIS){
		int Column = 0;
		while(UserTable[Column] != null){
			DataContainer = CHECKTHIS;
			if(CheckUsername(Column) && CheckPassword(Column)){
				return true;
			}
			Column++;
		}
		System.out.println("Sorry that username or password is incorrect please try agian");
		return false;
	}
	
/**
 * Registers new users into user database and outputs the data into the DatabaseLogin.txt and adds the new user into the array
 * @param NEWUSER the new user to into the ui
 * @throws IOException
 */
	public void LoginRegister(DataBucket NEWUSER) throws IOException{
		USERDATA = new DataBucket();
		USERDATA.SetUserName(NEWUSER.GetUserName());
		USERDATA.SetPassword(NEWUSER.GetPassword());
		System.out.print("Do you want to be a Admin | User | Guest: ");
		//User Permissions
		String Choice = "";
		Choice = ConsoleReader.READ();
		while(!Choice.equalsIgnoreCase("Admin") && !Choice.equalsIgnoreCase("User") && !Choice.equalsIgnoreCase("Guest")){
			System.out.print("Do you want to be a Admin | User | Guest: ");
			Choice = ConsoleReader.READ();
		}
		//Insert permissions
		if(Choice.equalsIgnoreCase("Admin"))
			USERDATA.SetPermission(Choice);
		else if(Choice.equalsIgnoreCase("User"))
			USERDATA.SetPermission(Choice);
		else if(Choice.equalsIgnoreCase("Guest"))
			USERDATA.SetPermission(Choice);
		//Display choose
		System.out.println("You have choosen: " + USERDATA.GetPermission());
		
		OUTPUT = new PrintWriter(new FileWriter(FILE, true));
		OUTPUT.println(NEWUSER.GetUserName() + " " + NEWUSER.GetPassword() + " " + USERDATA.GetPermission());
		OUTPUT.close();
		
		
		//Insert into table
		UserTable[Top++] = USERDATA;
		File dir = new File("DataText/" + USERDATA.GetUserName() + "Data");
		dir.mkdir();
		System.out.println(NEWUSER.GetUserName() + " has been registerd!");
	}
	
/**
 * Checks if the username is in the database
 * @param Column
 * @return
 */
	private Boolean CheckUsername(int Column){
		return DataContainer.GetUserName().equals(UserTable[Column].GetUserName());
	}
	
/**
 * Checks if the password is correct
 * @param Column
 * @return
 */
	private Boolean CheckPassword(int Column){
		return DataContainer.GetPassword().equals(UserTable[Column].GetPassword());
	}
	
	public DataBucket GetUser(DataBucket USER){
		int Column = 0;
		while(UserTable[Column] != null){
			DataContainer = USER;
			if(CheckUsername(Column) && CheckPassword(Column)){
				return UserTable[Column];
			}
			Column++;
		}
		System.out.println("Sorry that username or password is incorrect please try agian");
		return null;
	}
	
	
	
}
