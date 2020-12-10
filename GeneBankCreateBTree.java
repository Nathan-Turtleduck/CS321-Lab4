import java.io.File;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class GeneBankCreateBTree {

	static BTree tree;
	static int seqSize; //sub sequence length
	static int degree; //degree of tree
	static File file;
	static File gbkFile;
	static File dump;
	static boolean debug;
	static int cacheSize;
	
	public static void main(String[] args) {
		
		try {
			
			if(args.length < 4) {
				printUsage();
				System.exit(1);
			}
			
			//cache information
			if(Integer.parseInt(args[0]) == 0|| Integer.parseInt(args[0]) == 1) {
				//INSERT CACHE STUFF HERE ONCE IMPLEMENTED
			} else {
				System.out.println("Invalid argument given for cache value");
				System.exit(1);
			}
			
			//degree of tree
			if(Integer.parseInt(args[1]) == 0) {
				//FIND OPTIMAL DEGREE
			} else {
				degree = Integer.parseInt(args[1]); 
			}
			
			//Check to see if gbk file exists
			gbkFile = new File(args[2]);
			if(!gbkFile.exists()) {
				System.out.println("Failed to find file with path: " + args[2]);
				System.exit(1);
			}
			
			//length of subsequences
			if(Integer.parseInt(args[3]) >= 1 || Integer.parseInt(args[3]) <= 31) {
				
				seqSize = Integer.parseInt(args[3]); 
			} else {
				System.out.println("Subsequence length is not within domain of 1 - 31 (inclusive)");
				printUsage();
			}
			
			// Get Cache size
			if(args.length == 5) {
				cacheSize = Integer.parseInt(args[4]);
			}
			
			//See if a debug file is needed
			if(args.length == 6) {
				debug = true;
			}else {
				debug = false;
			}
			
			//create new files
			file = new File(args[2] + ".btree.data." + seqSize + "." + degree);
			file.createNewFile();
			RandomAccessFile RAF = new RandomAccessFile(file, "rw");
			tree = new BTree(degree, RAF);
			
			//START TO SCAN THE GBK FILE
			
			Scanner fileScan = new Scanner(gbkFile);
			String currentLine;
			
			//Loop to find the first readable line of data
			while(fileScan.hasNextLine()) {
				currentLine = fileScan.nextLine();
				
				//If ORIGIN is found in the line, jump to the next line and break out of the loop
				if(currentLine.contains("ORIGIN")) {
					break;
				}
			}
			currentLine = fileScan.nextLine();
			String subsequence;
			
			//Start reading data
			while(!currentLine.contains("//")) {
				
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Unexpected error occured.");
			printUsage();
					
		}

	}
	
	
	private static void printUsage() {
		System.out.println("User input does not match the parameterized domain.");
		System.out.println("Usage: java GeneBankCreateBTree <0/1> <degree> <gbk file> <sequence length> [<cache size>] [<debug level>]");
	}
	
	/**
	 * This helper method converts the sequence into 2-bit binary
	 * Then checks to see if the subsequence exists in the BTree and updates accordingly
	 * @param tree
	 * @param subsequence - Assumes that a proper subsequence has been created
	 * @throws Exception 
	 */
	private static void insertIntoTree(BTree tree, String subsequence) throws Exception {
		
		subsequence.toLowerCase();
		//Convert subsequence into 2-bit binary
		String binarySeq = "";
		
		for(int i = 0; i < subsequence.length(); i++) {
			
			if(subsequence.charAt(i) == 'a') {
				binarySeq = binarySeq + "00";
			}
			else if(subsequence.charAt(i) == 'c') {
				binarySeq = binarySeq + "01";
			}
			else if(subsequence.charAt(i) == 'g') {
				binarySeq = binarySeq + "10";
			}
			else if(subsequence.charAt(i) == 't') {
				binarySeq = binarySeq + "11";
			}
			
		}
		
		//Long value to be inserted
		Long value = Long.parseLong(binarySeq, 2);
		
		TreeObject key = new TreeObject(value , binarySeq.length());
		
		//Check to see if node is in the tree already
		BTreeNode retNode = tree.BTreeSearch(tree.getRoot(), value);
		
		if(retNode == null) {
			tree.BTreeInsert(key);
		}else {
			// If a key with the same value is found, increment that duplicate and break
			for(int i = 0; i < retNode.n; i++) {
				if(retNode.keys[i].equals(key)) {
					retNode.keys[i].incrementDuplicate();
					break;
				}
			}
			
		}
		
	}

}
