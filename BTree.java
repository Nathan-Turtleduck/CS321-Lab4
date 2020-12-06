
public class BTree {
	
	private BTreeNode root;
	private int t;
	
	/** Constructor for BTree (essentially it's "B-Tree-Create" **/ 
	public BTree(int t) {
		this.t = t;
		BTreeNode x = new BTreeNode(t);
		x.leaf = true;
		x.n = 0;
		root = x;
	}
	
	
	
}
