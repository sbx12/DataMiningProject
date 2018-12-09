/**
 * Used to contain a node of data to be passed around in the project
 * @author Steven Bedoya
 *
 */
public class DataBucket implements java.io.Serializable {
	private  String Username;			//Username
	private  String Password;			//Password
	private  String Permission;			//What permissions they hold such as admins/guest
	private  Boolean Feature1 = false;	//Feature one is to automatically get data from the internet at start from preferred
	private  String Feature1Choice;		//Feature onced choice;
	private  HashTable Table;			//Their Tables

///////////////////////////////////Set methods
	public void SetUserName(String Username){
		this.Username = Username;
	}
	
	public void SetPassword(String Password){
		this.Password = Password;
	}
	
	public void SetTable(HashTable Table){
		this.Table = Table;
	}
	
	public void SetPermission(String Permission){
		this.Permission = Permission;
	}
	
	public void SetFeature1(Boolean Choose){
		Feature1 = Choose;
	}
	
///////////////////////////////////Get methods
	public String GetUserName(){
		return Username;
	}
	
	public String GetPassword(){
		return Password;
	}
	
	public String GetPermission(){
		return Permission;
	}
	
	public Boolean GetFeature1(){
		return Feature1;
	}
	
	public HashTable GetTable(){
		return Table;
	}
}
