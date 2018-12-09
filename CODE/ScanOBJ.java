import java.util.Scanner;

/**
 * Used to read inputed data from the user
 * @author Steven Bedoya
 *
 */
public class ScanOBJ implements java.io.Serializable{
	private Scanner Scan;
	
	public ScanOBJ(){
		this.Scan = new Scanner(System.in);
	}
	
	public String READ(){
		return Scan.nextLine();
	}
	
}
