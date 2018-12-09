import java.util.Date;
import javax.swing.ImageIcon;
/**
 * Used tocontain the data to be put into the database
 * @author Steven Bedoya
 *
 */
public class DataNode implements java.io.Serializable{
	private String Filename;		//Name of the file
	private String Owner;			//Name of the user that added the data
	private String Title;			//Title for the data
	private String Category;		//Category of the article
	private String Author;			//Author of the article
	private String Date;			//The date the article was created
	private String PicInfo;			//The link or info of the picture
	private String Data;			//The Data in string format(Text from the article)
	private String URLlinks;		//Wired URL links that were found in the article
	private ImageIcon Image;		//Can store Images
	private String ConnectionInfo;	//URL connection information
	private int ID;					//ID number
	private Date DateTime;			//The time and date of data creation
	private int Key;				//HashKey
	public DataNode next;			//Point to next node
	private String HasImage;		//Shows if it has an image

	/**
	 * The DataNode Constructor 
	 * @param Data
	 * @param ID
	 */
	//Inserting data into the object
	public DataNode(String Data, int ID){
		HasImage = "No Images";
		this.Data = Data;
		this.ID = ID;
	}
	
	public DataNode(){
		HasImage = "No Images";
	}
/////////////////////////////////////////////Set Data Section	
	public void setFilename(String Filename){
		this.Filename = Filename;
	}
	
	public void setOwner(String Owner){
		this.Owner = Owner;
	}
	
	public void setTitle(String Title){
		this.Title = Title;
	}
	
	public void setCategory(String Category){
		this.Category = Category;
	}
	
	public void setAuthor(String Author){
		this.Author = Author;
	}
	
	public void setDate(String Date){
		this.Date = Date;
	}
	
	public void setPicInfo(String PicInfo){
		this.PicInfo = PicInfo;
	}
	
	public void setData(String Data){
		this.Data = Data;
	}
	
	public void setURLlinks(String URLlinks){
		this.URLlinks = URLlinks;
	}
	
	public void setImage(ImageIcon Image){
		this.Image = Image;
	}
	
	public void setConnectionInfo(String ConnectionInfo){
		this.ConnectionInfo = ConnectionInfo;
	}
	
	public void setID(int ID){
		this.ID = ID;
	}

	public void setDateTime(){
		DateTime = new Date();
	}
	
	public void setHasImageInfo(){
		HasImage = "Has Image";
	}
	
	public void setKey(int Key){
		this.Key = Key;
	}
	
	
/////////////////////////////////////////////ToString and GetMethods to access data in the object
	public String toString(){
		return "Created By: "+ Owner + "\n" + "Date Created: " + DateTime + "\n" + "Key: " + Key + "\n" +
				"ID: " + ID + "\n"  + "URL: " + Filename + "\n" + ConnectionInfo + "Title: " + Title + "\n" +
				"Author: " + Author + "\n" + "Category: " + Category + "\n" + "Date: " + Date + "\n" +
				"PicInfo: " + PicInfo + "\n"  +"URL Links: \n" + URLlinks + "\n" + "Data: \n" + Data + "\n";
	}
	
	public String toStringContentInfo(){
		return "Created By: "+ Owner + "\n" + "Date Created: " + DateTime + "\n" + "Key: " + Key + "\n" +
				"ID: " + ID + "\n"  + "URL: " + Filename + "\n" + "Title: " + Title + "\n" +
				"Author: " + Author + "\n" + "Category: " + Category + "\n" + "Date: " + Date + "\n" +
				"PicInfo: " + PicInfo + "\n"  +"URL Links: \n" + URLlinks + "\n";
	}
	
	public String getFilename(){
		return Filename;
	}
	
	public String getOwner(){
		return Owner;
	}
	
	public String getTitle(){
		return Title;
	}
	
	public String getCategory(){
		return Category;
	}
	
	public String getAuthor(){
		return Author;
	}
	
	public String getDate(){
		return Date;
	}
	
	public String getPicInfo(){
		return PicInfo;
	}
	
	public String getData(){
		return Data;
	}
	
	public String getURLlinks(){
		return URLlinks;
	}
	
	public ImageIcon getImage(){
		return Image;
	}
	
	public String getImageDescription(){
		return Image.getDescription();
	}
	
	public String getConnectionInfo(){
		return ConnectionInfo;
	}
	
	public int getId(){
		return ID;
	}
	
	public String getDateTime(){
		return DateTime.toString(); 
	}
	
	public int getKey(){
		return Key; 
	}
	
/////////////////////////////////////////////Compare methods
	public Boolean equalsCategory(String DataToCompare){
		return Data.equalsIgnoreCase(DataToCompare);
	}
	
	public Boolean equalsTitle(String DataToCompare){
		return Title.equalsIgnoreCase(DataToCompare);
	}
	

}
