import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
/**
 * Hanldes all of the Command flags arguements passed by the user
 * @author Steven Bedoya
 *
 */
public class CommandFlag {
	private String Args[];						//Contains all the arguements passed by the user
	private static String OutputText;			//The name of the output textfiles
	private static int TextNumber; 				//Number of the output text file
	private static Boolean oFlag = false;		//Passes down if an output file should be made
	private int NumArgs = 0;						//Number of args passed down
	private Scanner scan;						//Reads the string one by one
	private static HashTable Table;				//Table	
	private static ConsoleCalls ConCall;		//Allows to call the methods from the console call class
	private static DataBucket UserContainer;	//Contains User info 
	private static Documenter Logger;			//Records and logs all actions from user
	
	public CommandFlag(String data, HashTable Table, DataBucket Data, Documenter Logger) throws FileNotFoundException{
		UserContainer = Data;
		CommandFlag.Logger = Logger;
		CommandFlag.Table = Table;
		ConCall = new ConsoleCalls(this.Table, UserContainer);
		NumArgs = CountArgs(data);		//Count the number of arguements passed down
		ResetScanner(data);				//reset reader
		
		//Insert all Args into the Array
		Args = new String[NumArgs];
		for(int i = 0; i < NumArgs; i++){
			//Used so that the program does not crash
			try{													
				Args[i] = scan.next();
			}
			catch(Exception e){
				System.out.println(data + " Sorry Error Occured");
				return;
			}
		}
		
		FlagChoice();	//Where all the operations will take place
	}
/**
 * This is where the flap commands will be called out on 
 * @throws FileNotFoundException
 */
	private void FlagChoice() throws FileNotFoundException{
		for(int i = 0; i < Args.length; i++){
			if(Args[i].startsWith("-")){
				//OutPut file 
				if(Args[i].equalsIgnoreCase("-o")){
					System.out.println("-o exec");
					int NextPos = i + 1;
					//Check if there is an outputted text file name
					if(ValidArgs(NextPos)){
						OutputText = Args[NextPos]; 
						System.out.println(OutputText +  " Outputtextfile");
						oFlag = true;
						Logger.AddLog("-o Flag command with " + OutputText);
					}
					else{
						System.out.println("No inputted file or search term submitted");
					}
				}
				
				//Input text file or search term
				else if(Args[i].equalsIgnoreCase("-i")){
					int NextPos = i + 1;
					//Check if there is a file to check 
					if(ValidArgs(NextPos)){
						String File = Args[NextPos]; 
						System.out.println("Task: -i " + File);
						ADD("File", File, oFlag, OutputText);
						Logger.AddLog("-i Flag command with " + File);
					}
					else{
						System.out.println("No inputted file or search term submitted");
					}
				}
				
				//Input text file or search term
				else if(Args[i].equalsIgnoreCase("-f")){
					int NextPos = i + 1;
					if(ValidArgs(NextPos)){
						//Find all data in the database
						if(Args[NextPos].equals("ALL")){
							System.out.println("Task: -f " + Args[NextPos]);
							FindAll();
							Logger.AddLog("-f ALL Flag command");
						}
						//Find by ID
						else if(Args[NextPos].equalsIgnoreCase("ID") || Args[NextPos].equalsIgnoreCase("Advance")){
							NextPos++;
							//Make sure there is a and id to check with
							if(ValidArgs(NextPos)){
								String File = Args[NextPos]; 				
								System.out.println("Task: -f " + Args[NextPos]);
								//Use the file to search by ID
								if(Args[NextPos].equalsIgnoreCase("ID"))		
									FIND("ID", File, oFlag, OutputText);
								//Use the file to do an advance search
								else	
									FIND("Advance", File, oFlag, OutputText);
								Logger.AddLog("-f Flag command with " + Args[NextPos]);
							}
							//Nothing inputted
							else
								System.out.println("No inputted file or search term submitted");
						}
						else
							System.out.println("Invalid command! 2nd command must be ALL|ID|Advance");
					}
					else{
						System.out.println("No inputted file or search term submitted");
					}
				}
				
				//Delete data from the Database
				else if(Args[i].equalsIgnoreCase("-d")){
					int NextPos = i + 1;
					//Check if there is a file to check 
					if(ValidArgs(NextPos)){
						String File = Args[NextPos]; 
						System.out.println("Task: -d " + File);
						DELETE(File);
						Logger.AddLog("-d Flag command with " + File);
					}
					else{
						System.out.println("No inputted file or search term submitted");
					}
				}
				
				//Invalid Command flag
				else
					System.out.println(Args[i] + " is not recognized as an internal or external command!");
			}

		}
	}
	
	
//////////////////////////////////////////Flags
/**
 * Find and add the data into the database
 * @param Type
 * @param Search
 * @param oFlag
 * @param OutputText
 */
	private void ADD(String Type, String Search, Boolean oFlag, String OutputText){
		//Increase text file number
		if(oFlag)
			TextNumber++;
		try {
			ConsoleCalls.ADD(Type, Search, oFlag, OutputText + TextNumber);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Searched failed!");
		}
	}

/**
 * Find the data in the database
 * @param Type
 * @param Search
 * @param oFlag
 * @param OutputText
 */
	private void FIND(String Type, String Search, Boolean oFlag, String OutputText){
		//Increase text file number
		if(oFlag)
			TextNumber++;
		try {
			ConsoleCalls.FIND(Type, Search, oFlag, OutputText + TextNumber);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Searched failed!");
		}
	}
/**
 * Finds everything in the database
 */
	private void FindAll(){
		Table.ListAllData();
		Logger.AddLog("FOUND - Data Searched: All of Database");
		System.out.println("All data Searched");
	}

/**
 * Deleted choosen data from the database
 */
	private void DELETE(String ID){
		ConsoleCalls.DELETE(ID);
	}
	
	private void Edit(){
		//System.out.println(Table.Edit(Args[1], Integer.parseInt(Args[2])));
	}
	
	
//////////////////////////////////////////Tools
/**
 * Used to count the amount of Arguments 
 * @param data
 * @return
 */
	private int CountArgs(String data){
		scan = new Scanner(data);
		int temp = 0;
		while(scan.hasNext()){
			temp++;
			scan.next();
		}
		return temp;
	}

/**
 * Resets Scanner when needed
 * @param data
 */
	private void ResetScanner(String data){
		scan.close();
		scan = new Scanner(data);
	}

/**
 * Used to find if there is enough inputted data to complete the command
 * @param Limit
 * @return
 */
	private Boolean ValidArgs(int Position){
		if(Position < NumArgs){
			if(!Args[Position].startsWith("-"))
				return true;
		}
		return false;
	}
	
}
