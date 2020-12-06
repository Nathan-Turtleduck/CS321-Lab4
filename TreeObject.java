/**
 * 
 * @author Team 4, Ben Davies, Nathan Maroko, Jacob Godby
 *
 *This class outlines the data for a TreeObject.
 *It contains a key and a duplicate count
 */
public class TreeObject {
	
	private long key;
	private int duplicateCount;
	
	public TreeObject(long key) {
		this.key = key;
		duplicateCount = 0;
	}
	
	public void incrementDuplicate() {
		duplicateCount++;
	}
	
	public int compareTo(TreeObject object) {
		
		if(key > object.key) {
			return 1;
		}
		else if(key < object.key) {
			return -1;
		}
		else{
			return 0;
		}
	}
	
	public long getKey() {
		return key;
	}
	
	public int getDuplicateCount() {
		return duplicateCount;
	}
	
	public void setKeys(long key) {
		this.key = key;
	}
	
	public void  setDupes(int dupes) {
		this.duplicateCount = dupes;
	}
}
