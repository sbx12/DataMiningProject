import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Stores all the data based on their hash key which is based on the month's date and category
 * @author Sbzees
 *
 */
public class HashTable implements java.io.Serializable {
	private int Size = 85;
	private LinkedList[] Table;
	//private ScanOBJ ConsoleReader = new ScanOBJ();
	private DataBucket DataContainer;
 	
/**
 * A constructor for the hash table of size 100	
 */
	public HashTable(DataBucket DataContainer){
		this.DataContainer = DataContainer;
		Table = new LinkedList[Size];
		for(int i = 0; i < Size; i++)
			Table[i] = new LinkedList();
	}
	
	public HashTable(){
		Table = new LinkedList[Size];
		for(int i = 0; i < Size; i++)
			Table[i] = new LinkedList();
	}
/**
 * Used to create a hash key from the C number of the Data
 * @param id DATA ID
 * @return the hash key
 */
	public int HashKey(int id){
		return id % Size;
	}
/**
 * Uses the category and Date(month) to produce a hash	
 * @param Category
 * @param Date
 * @return A Hash Key
 */
	private int HashKey(String Category, String Date){
		//Category.replaceAll("\\s+","");							//Remove any possible spaces
		String CompareHash = Category;
		if(Date == null || Category == null)					//If no category exist or date place it in the unkwon location 
			return 84;
		int Hash = Character.getNumericValue(Date.charAt(1)) - 1;
		if(CompareHash.equalsIgnoreCase("business "))				//Hash where the business data will be located
			Hash += 0;
		else if(CompareHash.equalsIgnoreCase("culture "))			//Hash where the Culture data will be located
			Hash += 12;
		else if(CompareHash.equalsIgnoreCase("gear "))				//Hash where the Gear data will be located
			Hash += 24;
		else if(CompareHash.equalsIgnoreCase("ideas "))				//Hash where the Ideas data will be located
			Hash += 36;
		else if(CompareHash.equalsIgnoreCase("science "))			//Hash where the Science data will be located
			Hash += 48;
		else if(CompareHash.equalsIgnoreCase("security "))			//Hash where the Security data will be located
			Hash += 60;
		else if(CompareHash.equalsIgnoreCase("transportation "))	//Hash where the Transportation data will be located
			Hash += 72;
		else if(CompareHash.equalsIgnoreCase("Text File") || CompareHash.equalsIgnoreCase("Picture"))
			return 83;
		return Hash % Size;
	}
/**
 * Adds new data to the hashtable
 * @param Data
 */
	public void Add(DataNode Data){
		DataNode NewData = Data;
		int HashKey = HashKey(Data.getCategory(), Data.getDate());
		NewData.setOwner(DataContainer.GetUserName());
		NewData.setKey(HashKey);
		NewData.setDateTime();
		Table[HashKey].insert(NewData);
		System.out.println("The Data has been entered");
	}
/**
 * Used to search for an exact id number
 * @param Data
 * @param id
 * @return
 */
	public DataNode Find(int id){
		DataNode found = null;
		for(int i = 0; i < Size; i++){
			if(Table[i].CountList() != 0)	//Don't bother reading hash columns that don't have any data in them
				found = Table[i].Find(id);	//Find the data with this id
			if(found != null)
				return found;
		}
		return found;
	}

/**
 * Return a list of related searches to the searched term
 * @param Search
 * @return
 */
	public DataNode[][] AdvanceFind(String Search){
		int col = 0;
		DataNode FoundData[][] = new DataNode[Table.length][];
		for(int i = 0; i < FoundData.length; i++)
			if(Table[i].CountList() != 0){
				System.out.println(Table[i].CountList());
				DataNode Temp[] = Table[i].Find(Search);
				if(Temp[0] != null){
					System.out.println(i + " " + Temp.length);
					FoundData[col++] = Temp;
				}
			}
		System.out.println(col);
		return FoundData;
	}
/**
 * Deletes the Data from the database
 * @param Data
 * @param id
 * @return
 */
	public DataNode Delete(int id){
		DataNode found = Find(id);
		if(found != null)
			found = Table[found.getKey()].Delete(found.getId());
		return found;
	}
/**
 * Edits the Data in the database
 * @param Data
 * @param id
 * @return
 */
	public DataNode Edit(int id){
		DataNode found = Find(id);
		if(found != null){
			System.out.println("Insert new Data");
			found.setData("Edited");
			found.setDateTime();
		}
		return found;
	}
	
/**
 * Replaces datanodes in the database
 * @param Targetnode
 * @param Newnode
 * @return
 */
	public void EditReplace(DataNode Targetnode, DataNode Newnode){
		//If the category is not the same delete the node and add the new one but ID is saved
		if(!Targetnode.equalsCategory(Newnode.getCategory())){
			Delete(Targetnode.getId());
			Add(Newnode);
		}
		//Replaces node info with the new one keeps the orginal ID
		else{
			DataNode Target = Find(Targetnode.getId());
			Target = Newnode;
		}
	}

/**
 * List all the data in the users DataBase
 */
	public void ListAllData(){
		for(int i = 0; i < Size; i++){
			if(Table[i].CountList() != 0)	//Don't bother reading hash columns that don't have any data in them
				Table[i].ListData();
		}
	}
	
}



