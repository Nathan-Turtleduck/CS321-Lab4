import java.io.IOException;
import java.io.RandomAccessFile;

public class BTree {
	
	private BTreeNode root; // Root of tree
	private BTreeNode loadedNode; // Current loaded Node
	private int t; // Degree of B-tree
	private int currentByte; // Points to the end of file byte
	private RandomAccessFile raf; // Random Access File to be written to
	
	/** Constructor for BTree (essentially it's "B-Tree-Create") 
	 * @throws Exception **/ 
	public BTree(int t, RandomAccessFile raf) throws Exception {
		this.t = t;
		this.raf = raf;
		currentByte = 0;
		BTreeNode x = new BTreeNode(t, currentByte, this.raf);
		x.leaf = true;
		x.n = 0;
		root = x;
		loadedNode = x;
		currentByte = root.diskWrite();
	}
	
	/**
	 * Implements PseudoCode for BTreeSearch to the 'T'
	 * It is important to note that the user will have to search the node
	 * for the matching key since you can't return two values.
	 * @param root
	 * @param key
	 * @return Node that contains the key.
	 * @throws Exception
	 */
	public BTreeNode BTreeSearch(BTreeNode root, long key) throws Exception{
		
		int i = 0; // Current index of key
		
		while((i < root.n) && (key > root.keys[i].getKey())) {
			i++;
		}
		
		if((i <= root.n) && (key == root.keys[i].getKey())) {
			return root;
		}
		else if(root.leaf == true) {
			return null;
		}
		else {
			BTreeNode loadedNode = root.diskRead(root.childrenRef[i]);
			return BTreeSearch(loadedNode, key);
		}
		
	}
	
	
	
}
