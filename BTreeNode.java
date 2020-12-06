
public class BTreeNode {
	
	protected int t; // Minimum degree
	protected int n; // Number of keys in subtree
	protected TreeObject[] keys; // Number of keys per Node
	protected BTreeNode[] children; // Number of children connections
	protected int numChildren;
	protected boolean leaf; //Boolean of whether its a leaf or not
	
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
