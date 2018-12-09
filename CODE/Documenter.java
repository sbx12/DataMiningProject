import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Keeps the logs of the program and outputs it into a text file
 * @author Steven Bedoya
 *
 */
public class Documenter {
	private String OutputText = "";			//This shows the log of actions/data done during the program
	private Date DateTime, FirstTimeLog;	//Shows the date and time of the logs
	private DateFormat FT = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss"); //Formats the time and date
	private int NumOfData = -1;

/**
 * Documentor Constructor
 */
	public Documenter(){
		FirstTimeLog = new Date();
	}

/**
 * Reads the inputted actions and logs them into the string
 * @param Data
 */
	public void AddLog(String Data){
		NumOfData++;
		DateTime = new Date();
		OutputText += "\nLog DateTime: "+ DateTime + "\n" + Data;
	}

/**
 * Outputs the String into a text file
 * @throws FileNotFoundException
 */
	public void PrintLog() throws FileNotFoundException{
		PrintWriter  outText = new PrintWriter("LOG\\LOG" + FT.format(FirstTimeLog) + ".txt");
		outText.print(OutputText);
		outText.close();

	}
	
/**
 * Outputs the data searched into a text file
 * @throws FileNotFoundException
 */
	public void PrintData(String Username) throws FileNotFoundException{
		OutputText += "Number of Data Searched " + NumOfData;
		PrintWriter  outText = new PrintWriter("DataText\\" + Username + "Data" + "\\Data" + FT.format(FirstTimeLog) + ".txt");
		outText.print(OutputText);
		System.out.println("Data Outputted into the text file");
		outText.close();

	}

/**
 * Used to print data when using the output -o command flag
 * @param Username
 * @throws FileNotFoundException
 */
	public void PrintDataFlag(String Username, String FileName) throws FileNotFoundException{
		PrintWriter  outText = new PrintWriter("DataText\\" + Username + "Data" + "\\Data" + FileName + ".txt");
		outText.print(OutputText);
		System.out.println("Data Outputted into the text file");
		outText.close();

	}
}
