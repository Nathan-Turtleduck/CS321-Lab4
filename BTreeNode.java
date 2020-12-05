
public class BTreeNode {
	
	private int t; // Minimum degree
	private int n; // Number of keys in subtree
	private TreeObject[] keys; // Number of keys per Node
	private BTreeNode[] children; // Number of children connections
	private int numChildren;
	private boolean leaf; //Boolean of whether its a leaf or not
	
	public BTreeNode(int t) {
		this.t = t;
		this.keys = new TreeObject[(2 * t)-1];
		this.children = new BTreeNode[2 * t];
		this.n = 0;
		this.numChildren = 0;
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
	
	public void setN(int n) {
		this.n = n;
	}
	
	
	
}
