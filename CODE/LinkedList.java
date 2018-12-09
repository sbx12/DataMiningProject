import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Contains a list of datanodes which are placed in a hashtable
 * @author Steven Bedoya
 *
 */
public class LinkedList implements java.io.Serializable{
	
private DataNode head;
	
	public LinkedList(){
		head = null;
	}
/**
 * Used to insert the object into the List
 * @param newS The object to insert into the list
 */	
	public void insert(DataNode newData){
		if(head == null){			//Places the first object into the list
			head = newData;
			return;
		}
		DataNode current = head;
		while(current.next != null)		//Finds the next empty position
			current = current.next;
		current.next = newData;
	}
	
/**
 * Used to find the Data object in the list
 * @param Data 
 * @param ID 
 * @return the found data
 */
	public DataNode Find(int ID){
		DataNode current = head;
		while(current != null){
			if(current.getId() == ID)	
				return current;
			current = current.next;
		}
		return null;
	}

/**
 * Used for advance searches in the database
 * @param Find
 * @return
 */
	public DataNode[] Find(String Find){
		int col = 0;
		DataNode FoundData[] = new DataNode[CountList()];			//Array containing related searchees
		DataNode current = head;
		//Search through the list for related searches 
		while(current != null){										
			if(Regex(Find, String.valueOf(current.getId())))
				FoundData[col++] = current;
				
			else if(Regex(Find, String.valueOf(current.getKey())))
				FoundData[col++] = current;
			
			else if(Regex(Find, current.getTitle()))	
				FoundData[col++] = current;
			
			else if(Regex(Find, current.getCategory()))
				FoundData[col++] = current;
			
			else if(Regex(Find, current.getFilename()))
				FoundData[col++] = current;
			
			else if(Regex(Find, current.getAuthor()))
				FoundData[col++] = current;
			
			else if(Regex(Find, current.getDate()))
				FoundData[col++] = current;
			
			current = current.next;								//Move to the next node
		}
		return FoundData;	//Return the array
	}

/**
 * Displays all data in the list
 */
	public void ListData(){
		DataNode current = head;
		while(current != null){
			System.out.println(current);
			current = current.next;
		}
	}
	
/**
 * Deletes the Data from the list
 * @param Data 
 * @param ID 
 */
	public DataNode Delete(int ID){
		DataNode current = head; 
		DataNode previous = head;
		if(current == null){
			System.out.println("The Data could not be found");
			return null;
		}
		
		while(current.getId() != ID){	//Finds the location of the Data to be deleted 
			if(current.next == null){
				System.out.println("The Data could not be found");
				return null;
			}
			else{
				previous = current;
				current = current.next;
			}
		}
		
		if(current == head)					//Delete the Data that was in the head of the list
			head = head.next;
		else
			previous.next = current.next;	//Delete the Data from the list
		System.out.println("The Data has been deleted");
		return current;
	}
	
/**
 * Checks for spefics words or characters based on requested Regex
 * @param File
 * @return
 */
	private static Boolean Regex(String REGEXP, String File){
        Pattern pattern = Pattern.compile(REGEXP);	//Pattern to find
        Matcher matcher = pattern.matcher(File);	//Match with this
        if(matcher.find()){
        	System.out.println("Matached " + REGEXP + " " + File);
        	return true;
        }
        System.out.println("No Matach " + REGEXP + " " + File);
        return false;
	}

/**
 * Count how many nodes are int the list
 * @return
 */
	public int CountList(){
		int count = 0;
		DataNode current = head;
		while(current != null){
			count++;
			current = current.next;
		}
		return count;
	}
		
}