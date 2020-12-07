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
	
	/**
	 * Splits a given node where x is a nonfull node
	 * i - y = x.c[i] is a full child
	 * @param x
	 * @param i
	 * @throws Exception
	 */
	public void BTreeSplitChild(BTreeNode x, int i) throws Exception {
		
		BTreeNode z = new BTreeNode(t, currentByte, raf);
		BTreeNode y = x.diskRead(x.childrenRef[i]);
		z.leaf = y.leaf;
		z.n = t - 1;
		
		for(int j = 1; j != (t-1); j++) {
			z.keys[j] = y.keys[j+t];
		}
		if(!y.leaf) {
			for(int j = 1; j <= t; j++) {
				z.childrenRef[j] = y.childrenRef[j+t];
			}
		}
		y.n = t-1;
		
		for(int j = x.n + 1; j >= i +1; j--) {
			x.childrenRef[j+1] = x.childrenRef[j];
		}
		x.childrenRef[i+1] = z.start;
		
		for(int j = x.n; j >= i; j--) {
			x.keys[j+1] = x.keys[j];
		}
		x.keys[i+1] = y.keys[t];
		x.n = x.n + 1;
		
		y.diskWrite();
		x.diskWrite();
		currentByte = z.diskWrite();
		
	}
	
	
	
}
