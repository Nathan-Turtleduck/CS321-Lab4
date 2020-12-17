/**
 * 
 * @author Team 4, Ben Davies, Nathan Maroko, Jacob Godby
 *
 *This class outlines the data for a BTreeNode
 *It contains various methods and data that a node in a BTree would contain
 */
import java.io.IOException;
import java.io.RandomAccessFile;

public class BTreeNode {
	
	protected int t; // Minimum degree
	protected int n; // Number of keys in subtree
	protected TreeObject[] keys; // Number of keys per Node
	protected int[] childrenRef; // References to the children of a node
	protected int numChildren; // Current number of children
	protected boolean leaf; // Boolean of whether its a leaf or not
	protected RandomAccessFile raf; // File to be written to / read from
	protected int start; // Starting byte within the file for the given node
	
	public BTreeNode(int t, int startByte, RandomAccessFile raf) {
		this.start = startByte;
		this.raf = raf;
		this.t = t;
		this.keys = new TreeObject[(2 * t)-1];
		this.childrenRef = new int[2 * t];
		this.n = 0;
		this.numChildren = 0;
		
		// Load dummy objects into keys so we dont get a null pointer exception
		for(int i = 0; i < keys.length; i++) {
			keys[i] = new TreeObject(-1, -1);
		}
		
		for(int i = 0; i < childrenRef.length; i++) {
			childrenRef[i] = -1;
		}
	}
	
	/**
	 * Writes the current node to it's random access file
	 * @return Next available location for writing to
	 * @throws Exception
	 */
	public int diskWrite() throws Exception {
		raf.seek(start);
		raf.writeInt(t);
		raf.writeInt(n);
		
		for(int i = 0; i < ((2 *t) -1); i++) {
			raf.writeLong(keys[i].getKey());
			raf.writeInt(keys[i].getDuplicateCount());
			raf.writeInt(keys[i].getLength());
		}
		getNumChildren(); // It wasn't loading in all of the children so I had to add this statement
		raf.writeInt(numChildren);
		
		for(int i = 0; i < (2*t); i++) {
			raf.writeInt(childrenRef[i]);
		}
		
		
		raf.writeBoolean(leaf);
		raf.writeInt(start);
//		raf.writeInt(-1); // Dummy value so we can start writing at that current offset - I think this is improper implementation
		return (int) raf.getFilePointer(); // I don't think this is a problem by casting it, but let's flag this as a potential error
	}
	
	public BTreeNode diskRead(int ref) throws Exception{
		 
		raf.seek(ref);
		BTreeNode node = new BTreeNode(t, ref, raf);
		node.t = raf.readInt();
		node.n = raf.readInt();
		
		for(int i = 0; i < ((2 *t) -1); i++) {
			node.keys[i].setKeys(raf.readLong());
			node.keys[i].setDupes(raf.readInt());
			node.keys[i].setLength(raf.readInt());
		}
		
		node.numChildren = raf.readInt();
		
		for(int i = 0; i < (2*t); i++) {
			node.childrenRef[i] = raf.readInt();
		}
		
		node.leaf = raf.readBoolean();
		node.start = raf.readInt();
		
		return node;
	}
	
	/**
	 * Returns the number of children and updates the numChildren instance variable
	 * @return numChildren
	 */
	public int getNumChildren() {
		int retVal = 0;
		
		for(int i = 0; i < childrenRef.length; i++) {
			
			if(childrenRef[i] != -1) {
				retVal++;
			}
			
		}
		numChildren = retVal;
		return retVal;
		
	}
	
	public int getN() {
		int retVal = 0;
		
		for(int i = 0; i < keys.length; i++) {
			
			if(keys[i].getKey() != -1) {
				retVal++;
			}
		}
		
		n = retVal;
		return retVal;
	}
	
	
	
	
	
}
