
public class BTreeNode {
	
	private int t; // Minimum degree
	private int n; // Number of keys in subtree
	private TreeObject[] keys; // Number of keys per Node
	private BTreeNode[] children; // Number of children connections
	private boolean leaf; //Boolean of whether its a leaf or not
	
	public BTreeNode(int t, boolean leaf) {
		this.t = t;
		this.leaf = leaf;
		this.keys = new TreeObject[(2 * t)-1];
		this.children = new BTreeNode[2 * t];
		this.n = 0;
	}
	
	public boolean leaf() {
		return leaf;
	}
	
	public int numKeys() {
		return n;
	}
	
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	
	
	
	
}
