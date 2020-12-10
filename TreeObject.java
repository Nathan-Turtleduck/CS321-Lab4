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
	private int longLength;
	
	public TreeObject(long key, int length) {
		this.key = key;
		duplicateCount = 0;
		longLength = length;
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
	
	public void setDupes(int dupes) {
		this.duplicateCount = dupes;
	}
	
	/**
	 * Converts the key back to a regular string and returns the key 
	 * with its duplicate count
	 */
	public String toString() {
		
		String retString = "";
		
		String longString = Long.toBinaryString(key);
		
		
		if(longString.length() != longLength) {
			int difference = longLength - longString.length();
			
			for(int i = 0; i < difference; i++) {
				longString = "0" + longString;
			}
		}
		
		for(int i = 0; i < longLength; i += 2) {
			String sub = longString.substring(i, i + 2);
			
			if(sub.equals("00")) {
				retString += "a";
			}
			else if(sub.equals("01")) {
				retString += "c";
			}
			else if(sub.equals("10")) {
				retString += "g";
			}
			else if(sub.equals("11")) {
				retString += "t";
			}
		}
		
		retString += ": " + duplicateCount;
		
		return retString;
		
	}
}
