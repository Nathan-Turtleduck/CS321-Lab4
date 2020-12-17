import java.io.File;
import java.io.FileWriter;
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
		
		//Checks special case that root hasn't been initialized yet
		if(root.keys[0].getKey() == -1) {
			return null;
		}
		
		int i = 0; // Current index of key
		
		while((i < root.n) && (key > root.keys[i].getKey())) {
			i++;
		}
		
		if(root.keys[i].getKey() == -1) {
			return null;
		}
		
		if((i < root.n) && (key == root.keys[i].getKey())) {
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
	private void BTreeSplitChild(BTreeNode x, int i) throws Exception {
		
		BTreeNode z = new BTreeNode(t, currentByte, raf);
		BTreeNode y = x.diskRead(x.childrenRef[i]);
		z.leaf = y.leaf;
		z.n = t - 1;
		
		for(int j = 0; j < t - 1; j++) { 
			z.keys[j] = y.keys[j+t];
			y.keys[j+t] = new TreeObject(-1, -1);
		}
		if(!y.leaf) {
			for(int j = 0; j < t; j++) {
				z.childrenRef[j] = y.childrenRef[j+t];
				y.childrenRef[j+t] = -1;
			}
		}
		y.n = t - 1;
		
		for(int j = x.n; j > i; j--) {
			x.childrenRef[j+1] = x.childrenRef[j];
		}
		x.childrenRef[i+1] = z.start;
		
		for(int j = x.n -1; j > i; j--) {
			x.keys[j+1] = x.keys[j];
		}
		x.keys[i] = y.keys[t -1];
		y.keys[t -1] = new TreeObject(-1, -1);
		x.n = x.n + 1;
		
		y.diskWrite();
		x.diskWrite();
		currentByte = z.diskWrite(); // Cursor only gets moved with this node because it has not been written before
		
	}
	
	/**
	 * Inserts a new TreeObject into a nonfull node
	 * @param parent - Node you're trying to insert into
	 * @param newObject - object you're trying to insert
	 * @throws Exception
	 */
	private void BTreeInsertNonfull(BTreeNode parent, TreeObject newObject) throws Exception {
		
		int i = parent.n - 1;
		
		if(parent.leaf) {
			while((i >= 0) && (newObject.getKey() < parent.keys[i].getKey())) { 
				parent.keys[i+1] = parent.keys[i];
				i--;
			}
			
			parent.keys[i+1] = newObject; 
			parent.n = parent.getN();
			parent.diskWrite();
		}else {
			
			while((i >= 0) && (newObject.getKey() < parent.keys[i].getKey())) {
				i--;
			}
			i++;
			BTreeNode child = parent.diskRead(parent.childrenRef[i]);
			
			if(child.n == (2*t - 1)) {
				BTreeSplitChild(parent, i);
				parent = parent.diskRead(parent.start);
				child = parent.diskRead(parent.childrenRef[i]);
				if(newObject.getKey() > parent.keys[i].getKey()) {
					i++;
				}
			}
			BTreeInsertNonfull(child, newObject);
			
		}
	}
	
	/**
	 * Insert method the user will be able to call to insert a new object.
	 * Uses helper methods to decide how to insert the object
	 * @param newObject
	 * @throws Exception
	 */
	public void BTreeInsert(TreeObject newObject) throws Exception {
		
		BTreeNode r = root;
		
		if(r.n == (2*t - 1)) {
			
			r.start = currentByte; // Readjust where the root is written
//			r.leaf = true; //This might fix something?
			currentByte = r.diskWrite();
			
			BTreeNode s = new BTreeNode(t, 0, raf); // Write the new root to be the beginning of the file
			root = s;
			s.leaf = false;
			s.n = 0;
			s.childrenRef[0] = r.start; // THIS MAY BE AN OFF BY 1 ERROR. WE WILL HAVE TO SEE
			BTreeSplitChild(s, 0);
			s = s.diskRead(0);
			BTreeInsertNonfull(s, newObject);
			
		}else {
			BTreeInsertNonfull(r, newObject);
		}
		
	}
	
	/**
	 * This method should be called when the user is done inserting
	 * items into the BTree or would like to analyze the B-Tree
	 * This rewrites the root to the beginning of the file
	 * @throws Exception
	 */
	public void finish() throws Exception {
		
		root.diskWrite();
	}
	
	/**
	 * Entry for the recursive method traverseInorder
	 * Allows the user to create a dump file from the tree from a given file
	 * @param fileName
	 * @throws Exception
	 */
	public void debugDump(File fileName) throws Exception {
		
		FileWriter fw = new FileWriter(fileName);
		traverseInOrder(root, fw);
		fw.close();
	}
	
	/**
	 * Recursive method that writes each node and the number of duplicates to a file
	 * @param node
	 * @param fw
	 * @throws Exception
	 */
	private void traverseInOrder(BTreeNode node, FileWriter fw) throws Exception {
		
		for(int i = 0; i < node.getNumChildren(); i++) {
			traverseInOrder(node.diskRead(node.childrenRef[i]), fw);
			fw.write(node.keys[i].toString() + "\n");
		}
		if(!node.leaf) {
			traverseInOrder(node.diskRead(node.childrenRef[node.getNumChildren() -1]), fw);
		}
	}
	
	public BTreeNode getRoot() {
		return root;
	}
	
	
}
