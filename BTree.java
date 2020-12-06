import java.io.IOException;
import java.io.RandomAccessFile;

public class BTree {
	
	private BTreeNode root; // Root of tree
	private BTreeNode loadedNode; // Current loaded Node
	private int t; // Degree of B-tree
	private int currentByte; // Points to the end of file byte
	private RandomAccessFile raf; // Random Access File to be written to
	
	/** Constructor for BTree (essentially it's "B-Tree-Create") **/ 
	public BTree(int t) {
		this.t = t;
//		BTreeNode x = new BTreeNode(t);
//		x.leaf = true;
//		x.n = 0;
//		root = x;
	}
	
	
	
}
