import java.net.*;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
/**
 * Used to mine data off the web
 * @author Steven Bedoya
 *
 */
public class GetURLInfo {
	private static String CategoryList[] = {"Business", "Culture", "Gear", "Ideas", "Science", "Security", "Transportation", "Popular"};
	private static DataNode NewData;
	private static DataNode NewDataSet[];
	private static Documenter DataOutPut;	//Logs what has been searched

    // Create a URL from the specified address, open a connection to it,
    // and then display information about the URL.
    public GetURLInfo(){
    	NewData = new DataNode();
    }
/**
 * Used when only one URL is given   
 * @param URL
 * @return
 */
    public DataNode MineWeb(String URL, Documenter DataOutPut){
    	MineTheData(URL);
    	return NewData;
    }
    
 /**
  * Used for doing Advance Searches   
  * @param Search
  * @return
  */
    public DataNode[] AdvanceSearch(String Search, int Pages, Documenter DataOutPut){
    	GetURLInfo.DataOutPut = DataOutPut;
    	AdvanceSearchMine(Search, Pages);
    	return NewDataSet;
    }
    
 /**
  * Used to seach through categorys   
  * @param Category
  * @return
  */
    public DataNode[] CategorySearch(String Category, Documenter DataOutPut){
    	GetURLInfo.DataOutPut = DataOutPut;
    	Category(Category);
    	return NewDataSet;
    }
  
/**
 * Connect to the website using the given URL and scrape the html data off of it
 * @param URL
 * @return
 * @throws IOException
 */
    private static String GetHTML(String URL) throws IOException{
		URL url;
		try {
			url = new URL(URL);							//Create URL
			URLConnection connection;					
			connection = url.openConnection();			//Connect to the URL
			printinfo(connection);						//Get the information of the URL
		} catch (IOException e) {						//Used when it could not connect to the url
			System.out.println("Could not connect to " + URL);
			return null;
		}
	
	    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));	//Prepare to read url data
	    String inputLine, DivWanted = " ";													//Used to hold the data
	    
	    //Gets all of the data from the web page
	    while ((inputLine = in.readLine()) != null)	//While more data exist
	            DivWanted += inputLine + "\n";
	    return DivWanted;							//Return the data that has been mined
    }
/**
 * Mines the data that is needed of off the website using regex to filter it out.   
 * @param URL
 */
    private static void MineTheData(String URL){
	    String DivWanted;
		try {
			DivWanted = GetHTML(URL);		//Get the data from the website
		} catch (IOException e1) {			//Used incase of problems of getting to the website
			System.out.println("Could not connect to " + URL);
			return;
		}
		if(DivWanted != null){	    		//If data was mined then begin to mine it 
		    String BodySection = "";		//Contains the article section
		    String TextData = "";			//Contains the the text of the article
		    String Title = "";				//Contains the title of the article
		    String Author = "";				//Contains the author of the article
		    String Category = "";			//Contains the category of the article
		    String Date = "";				//Contains the date of the article
		    String UrlLinks = "";			//Contains URL links found within the article	
		    String PicLinks = "";			//Contains any links to picutres in the web page
	
		    //Get the Title Section of the website
		    Title = RegexHTML("<h1 id=\"(articleTitle|articleTitleFull)\" class=\"title\">.*?</h1>", DivWanted, true);
		    //Title = RegexHTML("<title.*?>.*?</title>", DivWanted, false);
	
		    //Get the Body Section of the articles in the website
	        BodySection = RegexHTML("<article class=.*><div>.*</div></article>", DivWanted, false);
	        
	        //Get the Authors Name
	        Author = RegexHTML("\"contributor\":.*?,", DivWanted, true);
	        
	        //Get the Category of the Article
	        Category = RegexHTML("\"section\":.*?,", DivWanted, true);
	        
	        //Get the Article Date
	        Date = RegexHTML("<time class=\"date-mdy\">.*?</time>", DivWanted, true);
	        
	        //Get URL LINKS from webpage
	        UrlLinks = RegexHTML("(\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|])", BodySection, false);
	        // String x = RegexHTML("(<a href=.*>*.*</a>)", BodySection);
	        
	        //Get Picture from webpage
	        //PicLinks = RegexHTML("https://media.wired.com/photos/.*?.jpg", DivWanted, true);
	        PicLinks = RegexHTML("\"url\":\"https://media.wired.com/photos/.*?\",", DivWanted, true);
	        
	        /*try {
				GetPics(FilterData("(\"url\":|,|\")", PicLinks));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				System.out.println("Could not connect to " + URL);
			}
			*/
	        //Enter and output the Data from the website
	        System.out.println("Author: "+ FilterData("(\"|contributor|,|:)", Author));
	        NewData.setAuthor(FilterData("(\"|contributor|,|:)", Author));
	        System.out.println("Title: " + FilterData("(<.*?>|&#39;|&#x27;s|&#39;s|&quot;|&#x27;|WIRED|\\|)", Title));
	        NewData.setTitle(FilterData("(<.*?>|&#39;|&#x27;s|&#39;s|&quot;|&#x27;|WIRED|\\|)", Title));
	        System.out.println("Category: " + FilterData("(\"|section|,|:)", Category));
	        NewData.setCategory(FilterData("(\"|section|,|:)", Category));
	        System.out.println("Date: " + FilterData("<.*?>", Date));
	        NewData.setDate(FilterData("<.*?>", Date));
	        System.out.println(FilterData("(\"url\":|,|\")", PicLinks));
	        //Get the title picture from the website
	        URL ImageURL = null;
	        ImageIcon Image = null;
			try {
				ImageURL = new URL(FilterData("(\"url\":|,|\")", PicLinks));	//Filter to get the proper link
				Image = new ImageIcon(ImageURL);								//Create image using the given link
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				System.out.println("No picture to get!");
			}
	        NewData.setPicInfo(FilterData("(\"url\":|,|\")", PicLinks));
	        NewData.setImage(Image);
	        //This is the article text data
	        TextData = FilterData("(<.*?>|&#39;|&#x27;s|&#39;s|&quot;|&#x27;|(<div class=\"truncated-footer-component\">.*?</article>))", BodySection);
	        
	        //Get all the URLs that are in the given article website
	        String Links[] = GetURLs(UrlLinks, false);
	    	String LinksForDataNode = "";
	    	for(int i = 0; i < Links.length; i++){
	    		if(Links[i] != null){
	    			System.out.println(Links[i]);
	    			LinksForDataNode += Links[i] + "\n";
	    		}
	    	}
	    	//Insert the URLS Links, text data and the name of the URL link
	    	NewData.setURLlinks(LinksForDataNode);
	        System.out.println(FormatText(TextData) + "\n");
	        NewData.setData(FormatText(TextData));
	        NewData.setFilename(URL);
	        //Used incase an error occurs
	        try{
	        	DataOutPut.AddLog("Data Found: \n" + NewData);
	        }
	        catch (NullPointerException e) {
				System.out.print("");
			}
		}
		else{//Either failed to connect or bad URL given
			System.out.println("Could not connect to " + URL);
			return;
		}
    }

/**
 * Uses words to make a search via Wired's Search feature
 * @param Search
 * @param NumofSearches
 */
    private static void AdvanceSearchMine(String Search, int NumofSearches){ 
    	Search = Search.replace(" ", "+");					//This is used incase the person uses space in their advance search									
    	
    	String AdvLinksx[][] = new String[NumofSearches][];	//Used to carry links from multiple pages of the search
    	//Search is based on how many pages the user wants to search
    	for(int i = 1; i < NumofSearches + 1; i++){
    		//Uses this URL to search through multiple pages from the given search
	    	String SearchLink = "https://www.wired.com/search/?page=" + i +"&q=" + Search + "&size=10&sort=publishDate_tdt%20desc&types%5B0%5D=article";
		    String DivWanted = "";
		    //Get the URL of the articles in the search page  
			try {
				DivWanted = GetHTML(SearchLink);
			} catch (IOException e) {
				System.out.println("Could not connect to " + SearchLink);
				return;
			}
			//Displays the number of results of the given search term
		    String NumSearchResults = RegexHTML("\"numOfSearchResults\":.\\d+", DivWanted, true);
		    //Get all thew URLs to be mined from
		    String GetUrls = RegexHTML("href=\"/story/.*?<h2.*?>", DivWanted, false);
		    
		    //If no searchs are found tell user
		    if(FilterData("([a-zA-Z]|\"|\\:)", NumSearchResults).length() == 0 && i == 1){
		    	System.out.println("No search Results!");
		    	return;
		    }
		    //Output the amount of searches found
		    System.out.println(FilterData("([a-zA-Z]|\"|\\:)", NumSearchResults));
		    //System.out.println(FilterData("(<h2|\"|href=|<div.*?>|<img.*?>)", links));
		    //Put URls into the array
		    String Links[] = GetURLs(RegexHTML("/story/.*?/", GetUrls, false), true);
		    //Move to the next row which is from the next page of the search
		    AdvLinksx[i - 1] = Links;
    	}
    	//This section is to know how big should the array be  
    	int col = 0;
    	//Get the amount of valid URLS that will be mined
    	for(int i = 0; i < AdvLinksx.length; i++)
    		for(int j = 0; j < AdvLinksx[i].length; j++)
	    		if(!(AdvLinksx[i][j] == null))
	    			col++;
       	//
    	NewDataSet = new DataNode[col];
    	//End of array creation section
    	
    	col = 0;
    	for(int i = 0; i < AdvLinksx.length; i++){
    		for(int j = 0; j < AdvLinksx[i].length; j++)
	    		if(!(AdvLinksx[i][j] == null)){
	    			NewData = new DataNode();
	    			MineTheData(AdvLinksx[i][j]);
	    			NewDataSet[col] = NewData;
	    			col++;
	    		}
    		//MineTheData(AdvLinksx[i][j]);
    	}
    }

/**
 * Search the Category section of wired or the popular section
 * @param Category
 */
    private static void Category(String Category){
    	String URL = null;
    	String GetSection = null;
    	//First Check if the category exist
    	if(CheckCategory(Category)){
    		if(!Category.equalsIgnoreCase("Popular"))					//If the category exist create a url for that category
    			URL = "https://www.wired.com/category/"+ Category.toLowerCase() + "/";
    		else 														//Else seach the most popular section of the website
    			URL = "https://www.wired.com";
    		//Get the data from the webpage;
	    	String DivWanted = "";;
			try {
				DivWanted = GetHTML(URL);			//Read and mine all the data from the URL
			} catch (IOException e) {
				System.out.println("Could not connect to website!");	//If it can't connect to the website output a message
				return;
			}
			//Get a Specific Section of the data where the URLs are
			//Popular is located in a different place so the program needs to be sure where to find the proper URLs
			if(!Category.equalsIgnoreCase("Popular"))
				//Get the section for the category
				GetSection = RegexHTML("<div class=\"page-loader-component\">.*?<div class=\"most-popular-side-bar-component most-popular-side-bar-component--medium-down-show\">", DivWanted, false);
			else
				//Get the section for the most popular section
				GetSection = RegexHTML("<div class=\"most-popular-side-bar-component most-popular-side-bar-component--medium-down-show\">.*?<div class=\"video-playlist-component\">", DivWanted, false);
	    	//String links = RegexHTML("href=\"/story/.*?/", MostPopularSection, false); 
			
	    	String Links[] = GetURLs(RegexHTML("/(story|review)/.*?/", GetSection, false), true);	//Go get all the URLs from the given section
	    	
	    	//Create array with the number of searches
	    	int col = 0;
	    	for(int i = 0; i < Links.length; i++)
	    		if(Links[i] != null)		
	    			col++;
	    	NewDataSet = new DataNode[col];
	    	col = 0;
	    	
	    	//Mines the data from all the captured URLs and puts it into the datanode array
	    	for(int i = 0; i < Links.length; i++)
	    		if(Links[i] != null){		//Makes sure not to mine from empty URLs
	    			NewData = new DataNode();
	    			MineTheData(Links[i]);	//Mine the Data
	    			NewDataSet[col] = NewData;
	    			col++;
	    		}
    	}
    	//If the given choose of category is invalid then output a message to user of the problem
    	else{
    		System.out.println("Sorry that is not a valid category!");
    		return;
    	}
    }
/**
 * Checks if the category exist   
 * @param Category
 * @return
 */
    private static boolean CheckCategory(String Category){
    	for(int i = 0; i < CategoryList.length; i++)		
    		if(Category.equalsIgnoreCase(CategoryList[i]))	//Check if the category given is valid
    			return true;								//It has found a valid category
    	return false;										//No valid category found
    }
 
/**
 * Used to filter out and get a section of the mined html data    
 * @param Regex
 * @param Match
 * @param single
 * @return
 */
    private static String RegexHTML(String Regex, String Match, Boolean single){
    	String Temp = "";								//Temp string holder to return
	    Pattern p = Pattern.compile(Regex);				//The regex pattern
	    Matcher m = p.matcher(Match);					//Matches the given string
	    //Used to get only one instance of the data
	    if(single){
		    if(m.find()){								//Find a single pattern match
	        	Temp += m.group() + " ";				//Add it to the temp string
	        }
	    }
		else
		//Fill in mulitple matches 
	        while(m.find()){							//Find multiple pattern matches
	        	Temp += m.group() + " ";				//Add it to the temp string
	        }
        return Temp;									//Return the temp string
    }

/**
 * Used to replace any data that is given fia the Regex String
 * @param Regex
 * @param Data
 * @return
 */
    private static String FilterData(String Regex, String Data){
    	String Temp = "";
    	Pattern p = Pattern.compile(Regex);				//regex pattern
    	Matcher m = p.matcher(Data);					//Match the given string
        if(m.find()){									//Find multiple pattern matches 
        	Temp = m.replaceAll("");					//Replace it with a blank 
        }
        return Temp;									//Return the new filtered string data
    }
/**
 * Get the URLS from the given String data passed down which comes from a web page of Wired  
 * @param Data
 * @param Adv
 * @return
 */
    private static String[] GetURLs(String Data, Boolean Adv){
    	System.out.println("URL Links: ");
    	int NumofLinks = NumofElements(Data);										//Contains the number of url links in the website
    	String URLList[] = new String[NumofLinks];									//This will contain a list of urls
    	String temp = "";												
    	if(NumofLinks != 0){														//Don't execute if there are no links to get
    		String DupLink = "";													//Holds a Duplicate link
    		StringTokenizer Read = new StringTokenizer(Data);						
	    	//Get all the links into the array
	    	for(int i = 0; i < URLList.length; i++){								//Go through all the urls
	    		if(Read.hasMoreTokens()){												
	    			temp = Read.nextToken();															
	    			if((temp.contains("wired") || Adv) && !temp.equals(DupLink)){	//It will try to get wired links only and in special cases it will add the url with wired.com to complete it since some url links come without it
	    																			//Also used to avoid duplicate URLs that may arise
		    			if(Adv == true)
		    				URLList[i] = "https://www.wired.com"+ temp;				//Used for adavance search links
		    			else							
		    				URLList[i] = temp;										//Regular Links
	    				DupLink = temp;												//Avoids duplicate links
	    			}
	    		}
	    	}
    	}
    	else	//Output if no linkes exist 
    		System.out.println("No Links to get!");
    	return URLList;
    }
  
/**
 * Prints out and inserts it into the datanode to display the info on the URL    
 * @param u
 * @throws IOException
 */
    public static void printinfo(URLConnection u) throws IOException {
    	String Urlinfo = "";
        // Display the URL address, and information about it.
        System.out.println(u.getURL().toExternalForm() + ":\n");
        Urlinfo += (u.getURL().toExternalForm() + ":\n");
        
        System.out.println("Content Type: " + u.getContentType());
        Urlinfo += ("Content Type: " + u.getContentType() + "\n");
        
        System.out.println("Content Length: " + u.getContentLength());
        Urlinfo += ("Content Length: " + u.getContentLength()+ "\n");
        
        System.out.println("Last Modified: " + new Date(u.getLastModified()));
        Urlinfo += ("Last Modified: " + new Date(u.getLastModified())+ "\n");
        
        System.out.println("Expiration: " + u.getExpiration());
        Urlinfo += ("Expiration: " + u.getExpiration()+ "\n");
        
        System.out.println("Content Encoding: " + u.getContent());
        Urlinfo += ("Content Encoding: " + u.getContent() + "\n");
        NewData.setConnectionInfo(Urlinfo);
        NewData.setID(Integer.parseInt((String.valueOf(u.hashCode()).substring(0, 7))));
/*        
// Read and print out the first five lines of the URL.
System.out.println("First five lines:");
DataInputStream in = new DataInputStream(u.getInputStream());
for(int i = 0; i < 5; i++) {
    String line = in.readLine();
    if (line == null) break;
    System.out.println("  " + line);
} // for
*/
    } 
    
    private static void GetPics(String Data) throws MalformedURLException{
    	 StringTokenizer Read = new StringTokenizer(Data);	
	     URL urlPic = new URL(Read.nextToken());	
	     
	     JFrame frame = new JFrame();
	     ImageIcon icon = new ImageIcon(urlPic);
	     JLabel label = new JLabel(icon);
	     frame.add(label);
	     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	     frame.pack();
	     frame.setVisible(true);
    }

/**
 * Makes the text data from the article easier to read once formatted
 * @param Data
 * @return
 */
    private static String FormatText(String Data){
    	String Temp = "	";				
    	StringTokenizer Read = new StringTokenizer(Data);
    	for(int i = 1; Read.hasMoreTokens(); i++){			//While data exist keep reading
    		if(i % 20 == 0)									//After 20 words it will make a new line so that data is easier to read
    			Temp += "\n";								//Create new line
    		Temp += Read.nextToken() + " ";					//Enter data
    	}
    	return Temp;										//Return the new formatted text 
    }

/**
 * Count the number of elements
 * @param Data
 * @return
 */
    private static int NumofElements(String Data){
    	int Counter = 0;								//Used to count how much url links there is
    	StringTokenizer Read = new StringTokenizer(Data);
    	while(Read.hasMoreTokens()){					//While data exist keep counting and move to the next one
    		Counter++;		
    		Read.nextToken();							
    	}
    	return Counter;									//Return the number of links in the website
    }
} // GetURLInfo
