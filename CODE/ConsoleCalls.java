import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.MimetypesFileTypeMap;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
/**
 * This is where all commands, functions, methods take place is a main section of the project
 * @author Steven Bedoya
 *
 */
public class ConsoleCalls {
	private static ScanOBJ ConsoleReader = new ScanOBJ();
	private static HashTable Table;			//The table that stores all data
	private static CommandFlag Flag;		//Flag Commands
	private static Documenter Logger;		//Records and logs all actions from user
	private static Documenter DataOutput;	//Output the data searched into a text file
	private static String temp;				
	private static DataNode NewData;		//New data to input into the database
	private static String CategoryList[] = {"Business", "Culture", "Gear", "Ideas", "Science", "Security", "Transportation", "Popular"};	//
	private static DataBucket UserContainer;//Contains User info 
/**
 * Calls different functions based off the request of the user
 * @param choice the command the user has requested
 */
	public ConsoleCalls(HashTable Table, DataBucket UserContainer){
		ConsoleCalls.Table = Table;
		ConsoleCalls.UserContainer = UserContainer;
	}
	
	public ConsoleCalls(){
	}
/**
 * Reads the given commands from the user and call the procedures to execute them	
 * @param choice
 * @param Table
 * @param Logger
 * @throws IOException
 */
	public static void Selection(String choice, Documenter Logger, DataBucket DataContainer) throws IOException{
		UserContainer = DataContainer;
		ConsoleCalls.Logger = Logger;
		ConsoleCalls.Table = DataContainer.GetTable();
		temp = "";
		if(choice.equalsIgnoreCase("Add")){
			System.out.println("Want to insert data from: Input | File");
			temp = ConsoleReader.READ();
			if(temp.equalsIgnoreCase("input") || temp.equalsIgnoreCase("file") || temp.equalsIgnoreCase("online")){
				ADD(temp, "" ,false, "");
				System.out.println("ADD END\nSelect Add|Delete|Find|Edit");
			}
			else{
				System.out.println("Invalid option please try again");
				System.out.println("Select Add|Delete|Find|Edit");
			}

		}
		
		else if(choice.equalsIgnoreCase("Find")){
			System.out.println("How do you want to find the data: ID | All | Advance");
			temp = ConsoleReader.READ();
			if(temp.equalsIgnoreCase("ID") || temp.equalsIgnoreCase("All") || temp.equalsIgnoreCase("Advance") ){
				FIND(temp, "" ,false, "");
				System.out.println("Find END\nSelect Add|Delete|Find|Edit");
			}
			else{
				System.out.println("Invalid option please try again");
				System.out.println("Select Add|Delete|Find|Edit");
			}
		}
		
		else if(choice.equalsIgnoreCase("Delete")){
			if(UserContainer.GetPermission().equalsIgnoreCase("guest")){
				System.out.println("Sorry you don't have any permissions to Delete");
			}
			else
				DELETE("");
			System.out.println("Select Add|Delete|Find|Edit");
		}
		
		else if(choice.equalsIgnoreCase("Exit"))
			CloseProgram();
		
		else if(choice.equalsIgnoreCase("Edit")){
			if(UserContainer.GetPermission().equalsIgnoreCase("guest")){
				System.out.println("Sorry you don't have any permissions to Edit");
			}
			else{
				System.out.print("How do you want to Edit the data: Input | URL: ");
				temp = ConsoleReader.READ();
				if(temp.equalsIgnoreCase("Input") || temp.equalsIgnoreCase("URL")){
					EDIT(temp);
					System.out.println("Select Add|Delete|Find|Edit");
				}
				else{
					System.out.println("Invalid option please try again");
					System.out.println("Select Add|Delete|Find|Edit");
				}
			}
			System.out.println("Select Add|Delete|Find|Edit");
		}
		
		else if(choice.equalsIgnoreCase("Help"))
			System.out.println(ReadTextFile("Info\\Helpinfo.txt"));
		
		else if(choice.equalsIgnoreCase("Menu"))
			System.out.println("Select Add|Delete|Find|Edit");
		
		else if(choice.startsWith("-")){
			Flag = new CommandFlag(choice, Table, UserContainer, Logger);
		}
			
		else
			System.out.println(choice + " is not recognized as an internal or external command!");
			return;
	}
	
//////////////////////////////////////////////////////////////////Call Functions
/**
 * Adds data into the database
 * @throws IOException 
 */
	public static void ADD(String Type, String Search ,Boolean oFlag, String OutputText) throws IOException{
		DataOutput = new Documenter();
		NewData = new DataNode();
		temp = "";
		String Path = "";
		//Used when you want to manually add data into the data
		if(Type.equalsIgnoreCase("input")){
			System.out.println("Insert new data into table");
			System.out.print("Add title: ");
			NewData.setTitle(ConsoleReader.READ());
			System.out.print("Add Data: ");
			NewData.setData(ConsoleReader.READ());
			AddToDatabase();
		}
		
		//Used to insert data from a Files
		else if(Type.equalsIgnoreCase("File")){
			System.out.print("Input the path of the File or search term: ");
			if(!Search.equals("")){
				Path = Search;
			}
			else
				Path = ConsoleReader.READ();
			//Use offline files such as text and pictures
			if(new File(Path).isFile()){
				//Insert Data From text Files
				if(TypeOfFile(Path).equals("text/plain")){
					NewData.setData(ReadTextFile(Path));
					System.out.print("Add title: ");
					NewData.setTitle(ConsoleReader.READ());
					NewData.setCategory("Text File");
					NewData.setAuthor("Text Creator");
					NewData.setFilename(Path);
				}
				
				//Insert Data From Image Files
				else if(TypeOfFile(Path).equals("image/jpeg")){
					InsertImage(Path, ReadImageFile(Path));
				}
				
				//Makes sure no null data are inserted into the database
				AddToDatabase();
			}

			//Used to get data from online
			else if (Regex("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|].*", Path)){
				//Used to get images off the web
				if(Regex("^(https?|ftp|file):[-a-zA-Z0-9+&@#/%?=~_|!:,.;].*([jpg|gif|png]{1})", Path)){
					GetURLImage urlImage = new GetURLImage(Path);
					InsertImage(Path, urlImage.GetImage());
					NewData.setFilename(Path);
					if(UserContainer.GetPermission().equalsIgnoreCase("guest")){
						System.out.println("Sorry you don't have any permissions to Edit");
					}
					else
						AddToDatabase();
					return;
				}
				//Get the data from the given website 
				else{
					GetURLInfo urlInfo = new GetURLInfo();
					NewData = urlInfo.MineWeb(Path, DataOutput);
					DataOutput.AddLog("Searched for: " + Path);
					DataOutput.AddLog("Data Found: " + NewData);
					//Ask User if they want to enter the data into the database 
					if(UserContainer.GetPermission().equalsIgnoreCase("guest")){
						System.out.println("Sorry you don't have any permissions to Edit");
					}
					else{
						if(WantToQuestion("Do You want to enter this data into the Database? yes|no : " )){
							AddToDatabase(NewData);
						}
						else{
							System.out.println("No data inputted into the database!");
						}
					}
					
					//Ask if they want to output
					if(oFlag){
						DataOutput.PrintDataFlag(UserContainer.GetUserName(), OutputText);
					}
					
					else{
						if(WantToQuestion("Do You want to output this data? yes|no : " )){
						 DataOutput.PrintData(UserContainer.GetUserName());
						}
					}

				}
			}
			//Search Through Category
			else if(Path.equalsIgnoreCase("Category")){
				//List of wired category choices
				System.out.print("Choose a Category: Business | Culture | Gear | Ideas | Science | Security | Transportation | Popular : ");
				String TempChoice = ConsoleReader.READ();
				//Check if the user inputs a valid category and search for that data
				if(CheckCategory(TempChoice)){
					//LOG
					DataOutput.AddLog("Searched for Category: " + TempChoice);
					Logger.AddLog("ADD: Category Search of " + TempChoice);
					//
					GetURLInfo urlInfo = new GetURLInfo();
					DataNode NewDataSet[] = urlInfo.CategorySearch(TempChoice, DataOutput);		//Brings back an array of datanodes of mined data
					if(UserContainer.GetPermission().equalsIgnoreCase("guest")){
						System.out.println("Sorry you don't have any permissions to ADD data into the database");
					}
					else{
						if(WantToQuestion("Do You want to enter this data into the Database? yes|no : " )){	//Ask user if they want this data entered into the database
							for(int i = 0; i < NewDataSet.length; i++){					//Go through the array and save it into the database
								AddToDatabase(NewDataSet[i]);							//Add to Database							
							}
						}
						else{
							System.out.println("No data inputted into the database!");	
							Logger.AddLog("ADD - Data was not added to the database!");
							return;
						}
					}

					
					//OutPut Data to a text file
					if(oFlag){
						DataOutput.PrintDataFlag(UserContainer.GetUserName(), OutputText);
					}
					
					else{
						if(WantToQuestion("Do You want to output this data? yes|no : " )){
						 DataOutput.PrintData(UserContainer.GetUserName());
						}
					}

				}
				else{
					System.out.println("Sorry that is not a proper category choose");	//Invalid category inputted
					return;
				}
			}
			//Do Adavance Search
			else{
				AdvSearch(Path, oFlag, OutputText);
			}

		}
	}
	
/**
 * Finds the data in the database
 * @throws FileNotFoundException 
 */
	public static void FIND(String Type, String Search ,Boolean oFlag, String OutputText) throws FileNotFoundException{
		DataOutput = new Documenter();
		DataNode Found;
		//Find by ID Number Only
		if(Type.equalsIgnoreCase("ID")){
			System.out.print("Insert ID to Find Data: ");
			if(!Search.equals("")){
				temp = Search;
			}
			else
				temp = ConsoleReader.READ();
			//Makes sure that none int values are inputted to search for an id
			while(!Regex("\\d+", temp)){
				System.out.print("Invalid Input! Insert ID to Find Data: ");
				temp = ConsoleReader.READ();
			}
			
			Logger.AddLog("Find - Searching to find data ID: " + temp);	//log
			Found = Table.Find(Integer.parseInt(temp));					//Find the data with the given ID number
			//If data found then output it into the database
			if(Found != null){
				System.out.println(Found);								//Output the information 
				//LOG
				Logger.AddLog("FOUND - Data Searched:\n" + Found);
				DataOutput.AddLog("Searching for Data ID: " + temp);
				DataOutput.AddLog("Data Found\n" + Found);
				
				//Display Image of the data
				if(Found.getImage() != null){
					//Ask user if they want to display the picture
					if(WantToQuestion("Do You want to display the image? yes|no : " ))	
						DisplayImage(Found.getImage());
				}
				
				//Output file into a text file
				//OutPut Data to a text file
				if(oFlag){
					DataOutput.PrintDataFlag(UserContainer.GetUserName(), OutputText);
				}
				
				else{
					if(WantToQuestion("Do You want to output this data? yes|no : " )){
					 DataOutput.PrintData(UserContainer.GetUserName());
					}
				}

			}
			else
				System.out.println("Requested data could not be found");
		}
		
		//Does an advance search on the enitre database based on the inputted data
		else if(Type.equalsIgnoreCase("Advance")){
			System.out.print("Type what you want to search: ");
			temp = "";
			if(!Search.equals("")){
				temp = Search;
			}
			else
				temp = ConsoleReader.READ();
			while(temp.equals("")){
				System.out.print("No blanks allowed! Type what you want to search: ");
				temp = ConsoleReader.READ();
			}
			Logger.AddLog("FIND: Advance Search of " + temp);
			int count = 0;
			DataNode TempDataSet[][] = Table.AdvanceFind(temp);					//Fills table with all data related to what was asked
			
			//Get a proper length of data found on the TempDataSet
			for(int i = 0; i < TempDataSet.length; i++){
				if(TempDataSet[i] != null){										//Null data doesn't count
					System.out.println("Set Length: " + TempDataSet[i].length);
					count += TempDataSet[i].length;								//Add the length 
				}
			}
			
			System.out.println(count);
			DataNode DataSearched[] = new DataNode[count];				//Insert and create the array
			count = 0;	
			//Put all the data in TempDataSet to DataSearched
			for(int i = 0; i < TempDataSet.length; i++){	
				if(TempDataSet[i] != null){								//No null data allowed
					for(int j = 0; j < TempDataSet[i].length; j++){
						DataSearched[count++] = TempDataSet[i][j];		//Insert the data into the array
					}
				}
			}
			int found = 0;
			//Output all the data to the command prompt
			for(int i = 0; i < DataSearched.length; i++)
				if(DataSearched[i] != null){
					found++;
					System.out.println("Result " + found + "_________\n" + DataSearched[i].toStringContentInfo());
					DataOutput.AddLog("Data Found: " + DataSearched[i].toStringContentInfo());
				}
			System.out.println("Found " + found + " search results.....");
			
			//OutPut Data to a text file
			if(oFlag){
				DataOutput.PrintDataFlag(UserContainer.GetUserName(), OutputText);
			}
			
			else{
				if(WantToQuestion("Do You want to output this data? yes|no : " )){
				 DataOutput.PrintData(UserContainer.GetUserName());
				}
			}
		}
		
		//OutPuts all the data in the Database
		else if(Type.equalsIgnoreCase("All")){
			Table.ListAllData();
			Logger.AddLog("FOUND - Data Searched: All of Database");
		}
	}

/**
 * Deletes the data off the database
 */
	public static void DELETE(String Search){
		//Search for digits for the id only first part is meant for the flag -d
		if(!Search.equals("")){
			temp = Search;
		}
		else{
			System.out.print("Insert ID to Find Data: ");
			temp = ConsoleReader.READ();
		}
		//Makes sure only int values can pass through
		while(!Regex("\\d+", temp)){
			System.out.print("Invalid Input! Insert ID to Find Data: ");
			temp = ConsoleReader.READ();
		}
		//Log and Delete data from tab;e
		Logger.AddLog("DELETED - Searching to delete data ID: " + temp);
		DataNode Temp = Table.Delete(Integer.parseInt(temp));
		//Data has been deleted
		if(Temp != null){
			System.out.println("Data:\n" + Temp + "\n Has been deleted");
			Logger.AddLog("DELETE - Data Deleted:\n" + Temp);
		}
		//Could not find the data to delte
		else{
			System.out.println("Data not found nothing deleted.");
			Logger.AddLog("Data not Deleted");
		}
	}

/**
 * Edits the data in the database
 */
	private static void EDIT(String Type){
		DataOutput = new Documenter();
		System.out.print("Insert the ID to Edit: ");	
		//Find the ID number
		temp = ConsoleReader.READ();
		while(!Regex("\\d+", temp)){
			System.out.print("Invalid Input! Insert ID to Find Data: ");
			temp = ConsoleReader.READ();
		}
		//Find the node asked by the user
		DataNode NodeToEdit = Table.Find(Integer.parseInt(temp));
		if(NodeToEdit != null){
			//Edit with a URL Link
			if(Type.equalsIgnoreCase("URL")){
				System.out.print("Please insert a proper wired link: ");
				String URL = ConsoleReader.READ();
				//Makes sure a valid URL is passed
				while(!Regex("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|].*", URL)){
					System.out.println("Please insert a proper wired link: ");
					URL = ConsoleReader.READ();
				}
				//Get the new data from the URL
				DataNode NewNode = EditWithURL(NodeToEdit, URL);
				Table.EditReplace(NodeToEdit, NewNode);		//Replace old data
				Logger.AddLog("EDIT: The old data was replaced with a new one \n" + NewNode);
			}
			//Input manualy into the database
			else if(Type.equalsIgnoreCase("Input")){
				System.out.println("Edit the Title");
				NodeToEdit.setTitle(ConsoleReader.READ());
				System.out.println("Edit the Data");
				NodeToEdit.setData(ConsoleReader.READ());
				Table.EditReplace(NodeToEdit, NodeToEdit);
				Logger.AddLog("EDIT: The old data was replaced with a new one \n" + NodeToEdit);
			}
		}
		else{
			System.out.println("Data could not be found");
		}
	}

//////////////////////////////////////////////////////////////////FILES - Tools
/**
 * Checks if there is data to add to the database then if there is then insert it into the database creatse random id number if it can't hash from a url number
 */
	private static void AddToDatabase(){
		if(NewData.getData() != null){
			NewData.setID( (int )(Math.random() * 9000000 + 1000000));					//Add a random id number
			Table.Add(NewData);															//Add to the database
			Logger.AddLog("ADD - Data added to the database " + NewData.toString());
		}
		else{
			System.out.println("No Data Inputted Nothing entered into database.  FILE:");
			return;
		}
	}
/**
 * Used to enter advance/categroy/popular searches data into the database
 * @param Data
 */
	private static void AddToDatabase(DataNode Data){
		//Input data only if it has data from the article
		if(Data.getData() != null){
			Table.Add(Data);
			Logger.AddLog("ADD - Data added to the database " + Data.toString());
			DataOutput.AddLog("Data Found : " + Data.toString());
		}
		//Don't insert into the database and tell user
		else{
			System.out.println("No Data Inputted Nothing entered into database.  FILE:");
		}
	}

/**
 * Gets data by doing an advance search in Wired.com
 * @param Path
 * @throws FileNotFoundException
 */
	private static void AdvSearch(String Path, Boolean oFlag, String OutputText) throws FileNotFoundException{
		System.out.print("How many pages do you want to search? [1-9]: ");
		String Pages = "";
		Pages = ConsoleReader.READ();
		//See how many pages the user wants to find from the search given
		while(!Regex("[1-9]", Pages)){	//Can only check 1 - 9 pages
			System.out.print("Please inset a valid numeber! How many pages do you want to search?: ");
			Pages = ConsoleReader.READ();
		}
		//Log
		DataOutput.AddLog("Searched for " + Path);
		Logger.AddLog("ADD: Advance Search of " + Path + " and searched through " + Pages + " pages.");
		//
		GetURLInfo urlInfo = new GetURLInfo();						
		DataNode NewDataSet[] = urlInfo.AdvanceSearch(Path, Integer.parseInt(Pages), DataOutput);		//Brings back an array of datanodes of mined data

		if(WantToQuestion("Do You want to enter this data into the Database? yes|no : " ))	//Ask user if they want this data entered into the database
			for(int i = 0; i < NewDataSet.length; i++){										//Go through the array and save it into the database
				AddToDatabase(NewDataSet[i]);												//Add to the Database;
			}
		//No data inputted into the database
		else{
			System.out.println("No data inputted into the database!");
			Logger.AddLog("ADD - Data was not added to the database!");
		}
		
		//Output file into a text file
		//OutPut Data to a text file
		if(oFlag){
			DataOutput.PrintDataFlag(UserContainer.GetUserName(), OutputText);
		}
		
		else{
			if(WantToQuestion("Do You want to output this data? yes|no : " )){
			 DataOutput.PrintData(UserContainer.GetUserName());
			}
		}
	}

/**
 * Used to replace information in a node with new information
 * @param EditNode
 * @param URL
 */
	private static DataNode EditWithURL(DataNode EditNode, String URL){
		GetURLInfo urlInfo = new GetURLInfo();
		EditNode = urlInfo.MineWeb(URL, DataOutput);
		return EditNode;
	}

/**
 * Used to insert images into the database	
 * @param Path
 */
	private static void InsertImage(String Path, ImageIcon Image){
		NewData.setImage(Image);
		NewData.setData("Image");
		NewData.setHasImageInfo();
		System.out.print("Add title: ");
		NewData.setTitle(ConsoleReader.READ());
		NewData.setCategory("Picture");
	}
/**
 * Displays info on commands and instructions on how to use them	
 * @param Filename
 */
	private static String ReadTextFile(String Filename){
		String Texts = "", line = "";
		String TempArray [] = new String[numOfLines(Filename)];
		int Col = 0;
		try {
            Scanner input = new Scanner(Filename);
            File file = new File(input.nextLine());
            input = new Scanner(file);
            String Collect = "";
            while (input.hasNextLine()){
            	line = input.nextLine();
            	Collect += line + "\n";
            	TempArray[Col++] = line;
            }
            
            Texts = Collect;
            input.close();
        } 
        catch (Exception ex){
			System.out.println("Could not Find File");
			Texts = "FileNotFound";
        }
		return Texts;
	}

	private static ImageIcon ReadImageFile(String Filename){
		ImageIcon NewImage = new ImageIcon(Filename, "Image");
		return NewImage;
	}
	
/**
 * Used to find out what type of file the requested file is
 * @param path
 * @return
 */
	private static String TypeOfFile(String path){
	      String FileType = null;
	      MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
	      try{
		      File file = new File(path);
		      FileType = mimeTypesMap.getContentType(file);
		      System.out.println(FileType);
	      }
	      catch (Exception ex){
				System.out.println("Could not Find File");
	      } 
	      return FileType;
	}

/**
 * Checks if the inputed data is a URL link
 * @param File
 * @return
 */
	private static Boolean Regex(String REGEXP, String File){
        Pattern pattern = Pattern.compile(REGEXP);
        Matcher matcher = pattern.matcher(File);
        if(matcher.matches()){
        	return true;
        }
        return false;
	}
	
/**
 * Count the number of lines in a file	
 * @param Filename
 * @return
 */
	private static int numOfLines(String Filename){
		int numlines = 0;
		try {
            Scanner input = new Scanner(Filename);
            File file = new File(input.nextLine());
            input = new Scanner(file);
            String line = "";
            while (input.hasNextLine())
            	numlines++;
            input.close();
        } 
        catch (Exception ex){
			System.out.println("Could not Find File");
        }
		return numlines;
	}

//////////////////////////////////////////////////////////////////Other - Tools
/**
 * Display the Image if it exists in the database
 * @param Image
 */
	private static void DisplayImage(ImageIcon Image){
	      JFrame frame = new JFrame();
	      JLabel label = new JLabel(Image);
	      frame.add(label);
	      frame.setDefaultCloseOperation
	             (JFrame.EXIT_ON_CLOSE);
	      frame.pack();
	      frame.setVisible(true);
	}

/**
 * Closes the program
 */
	private static void CloseProgram(){
		System.out.println("Program Closed");
		System.exit(1);
	}
	
/**
 * Checks if the category exist   
 * @param Category
 * @return
 */
    private static boolean CheckCategory(String Category){
    	for(int i = 0; i < CategoryList.length; i++){		
    		if(Category.equalsIgnoreCase(CategoryList[i])){	//Check if the category given is valid
    			return true;								//It has found a valid category
    		}
    	}
    	return false;										//No valid category found
    }
  
/**
 * Used to know if the user wants the data mined from the internet or anywhere is to be entered to the databse
 * @return
 */
    private static boolean WantToQuestion(String Question){
    	System.out.print(Question);															//Tell User question
    	String TempChoice = "";																//Used to contain the choice
    	while(!TempChoice.equalsIgnoreCase("Yes") || !TempChoice.equalsIgnoreCase("No")){	//While the user hasn't made a choice loop
    		TempChoice = ConsoleReader.READ();
    		if(TempChoice.equalsIgnoreCase("Yes")){				//Yes return true and allow data to be entered in the database
    			return true;
    		}
    		else if(TempChoice.equalsIgnoreCase("No")){			//No then data entered into the database
    			return false;
    		}
    		else if(TempChoice.equalsIgnoreCase("exit")){		//Used incase they wnat to exit 
    			 CloseProgram();
    		}
    		else												//Wrong Input
    			System.out.println("Invalid Input! Do You want to enter this data into the Database? yes|no");
    	}
    	return false;
    }
	
}
