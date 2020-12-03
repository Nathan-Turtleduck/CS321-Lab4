
public class BTreeNode {
	
	private int t; // Minimum degree
	private int n; // Number of keys in subtree
	private long[] keys; // Number of keys per Node
	private BTreeNode[] children; // Number of children connections
	private boolean leaf; //Boolean of whether its a leaf or not
	
	public BTreeNode(int t, boolean leaf) {
		this.t = t;
		this.leaf = leaf;
		this.keys = new long[(2 * t)-1];
		this.children = new BTreeNode[2 * t];
		this.n = 0;
	}
	
	public boolean leaf() {
		return leaf;
	}
	
	public int numKeys() {
		return n;
	}
	
//	public void insertKey(long key) {
//		
//	}
	
	
	
	
}
