/**
 * 
 * @author Team 4, Ben Davies, Nathan Maroko, Jacob Godby
 *
 *This class creates a BTree from a given gbk file
 */
import java.io.File;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class GeneBankCreateBTree {

	static BTree tree; //the BTree
	static int seqSize; //sub sequence length
	static int degree; //degree of tree
	static File file;
	static File gbkFile;
	static File dump;
	static boolean debug;
	static boolean cacheFlag;
	static int cacheSize;
	static Cache cache;
	
	public static void main(String[] args) {
		
		try {
			
			if(args.length < 4) {
				printUsage();
				System.exit(1);
			}
			
			//cache information
			if(Integer.parseInt(args[0]) == 0 || Integer.parseInt(args[0]) == 1) {
				if(Integer.parseInt(args[0]) == 1) {
					cacheFlag = true;
				}
			}else {
				System.out.println("Invalid argument given for cache value");
				System.exit(1);
			}
			
			//degree of tree
			if(Integer.parseInt(args[1]) == 0) {
				degree = (int) (Math.pow(Integer.parseInt(args[3]), 3) + 1);
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
				
				if(cacheSize <= 0) {
					System.out.println("Error: Cache Size must be greater than 0");
					printUsage();
					System.exit(1);
				}
				cache = new Cache(cacheSize);
			}
			
			//See if a debug file is needed
			if(args.length == 6) {
				debug = true;
			}else {
				debug = false;
			}
			
			//create new files
			int testIndex1 = args[2].indexOf("test");
			int testIndex2 = args[2].indexOf("gbk") + 3;
			file = new File("./" + args[2].substring(testIndex1, testIndex2) + ".btree.data." + seqSize + "." + degree);
			file.createNewFile();
			RandomAccessFile RAF = new RandomAccessFile(file, "rw");
			tree = new BTree(degree, RAF);
			
			//START TO SCAN THE GBK FILE
			
			Scanner fileScan = new Scanner(gbkFile);
			String currentLine;
			int originCount = 0;
			
			//COUNT NUMBER OF ORIGINS
			while(fileScan.hasNextLine()) {
				currentLine = fileScan.nextLine();
				if(currentLine.contains("ORIGIN")) {
					originCount++;
				}
			}
			fileScan = new Scanner(gbkFile);
			
			for(int j = 0; j < originCount; j++) {
				//Loop to find the first readable line of data
				while(fileScan.hasNextLine()) {
					currentLine = fileScan.nextLine();

					//If ORIGIN is found in the line, jump to the next line and break out of the loop
					if(currentLine.contains("ORIGIN")) {
						break;
					}
				}
//				currentLine = fileScan.nextLine();
				String subsequence = "";
				String currentToken = "";

				char currentChar = '0';
				//Start reading data
				while(currentChar != '/') {
					currentToken = fileScan.next();
					for(int i = 0; i < currentToken.length(); i++) {
						currentChar = currentToken.charAt(i);

						if(currentChar == 'a' || currentChar == 'c' || currentChar == 'g' || currentChar == 't') {
							subsequence = subsequence + currentChar;
						}else if(currentChar == 'n') {
							subsequence = "";
						}else if(currentChar == '/') {
							break;
						}

						if(subsequence.length() == seqSize) {
							if(cacheFlag == true) {
								insertIntoTreeCache(tree, subsequence);
							}else {
								insertIntoTree(tree, subsequence);
							}
							if(subsequence.length() >= 2) {
								subsequence = subsequence.substring(1);
							}else {
								subsequence = "";
							}
						}
					}

				}
			}
			
			
			
			if(debug == true) {
				dump = new File("./" + args[2].substring(testIndex1, testIndex2) + ".btree.dump." + seqSize);
				tree.debugDump(dump);
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
		
		subsequence = subsequence.toLowerCase();
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
				if(retNode.keys[i].compareTo(key) == 0) {
					retNode.keys[i].incrementDuplicate();
					retNode.diskWrite();
					break;
				}
			}
			
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private static void insertIntoTreeCache(BTree tree, String subsequence) throws Exception {
		
		subsequence = subsequence.toLowerCase();
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
		
		if(cache.getObject(key) == true) {
			cache.addToTop(key);
		}else {
			cache.addObject(key);
		}
		
		//Check to see if node is in the tree already
		BTreeNode retNode = tree.BTreeSearch(tree.getRoot(), value);
		
		if(retNode == null) {
			tree.BTreeInsert(key);
		}else {
			// If a key with the same value is found, increment that duplicate and break
			for(int i = 0; i < retNode.n; i++) {
				if(retNode.keys[i].compareTo(key) == 0) {
					retNode.keys[i].incrementDuplicate();
					retNode.diskWrite();
					break;
				}
			}
			
		}
	}
}
