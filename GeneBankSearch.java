/**
 * 
 * @author Team 4, Ben Davies, Nathan Maroko, Jacob Godby
 *
 *This class takes in a query file and searches a given
 *BTree file for various queries
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class GeneBankSearch {

	static int cacheUse;
	static File bTreeFile;
	static File queryFile;
	static int cacheSize;
	static int debug;
	static FileWriter fileWriter;
	static BTree thisTree;
	static Cache cache;
	static boolean cacheFlag;
	
	public static void main(String[] args) throws Exception {
		
		if(args.length < 3 || args.length > 5) {
			printUsage();
			System.exit(1);
		}
		
		//cache information
		if(Integer.parseInt(args[0]) == 0 || Integer.parseInt(args[0]) == 1) {
			if(Integer.parseInt(args[0]) == 1) {
				cacheFlag = true;
			}
		} else {
			System.out.println("Invalid argument given for cache value");
			System.exit(1);
		}
		
		bTreeFile = new File(args[1]);
		if(!bTreeFile.exists()) {
			System.out.println("Failed to find file with path: " + args[1]);
			System.exit(1);
		}
		
		queryFile = new File(args[2]);
		if(!queryFile.exists()) {
			System.out.println("Failed to find file with path: " + args[2]);
			System.exit(1);
		}
		
		if(args.length > 3) {
			cacheSize = Integer.parseInt(args[3]);
			if(cacheSize <= 0) {
				System.out.println("CacheSize must be bigger than 0");
				printUsage();
			}
			cache = new Cache(cacheSize);
		}
		
		if(args.length == 5) {
			debug = Integer.parseInt(args[4]);
			if(debug != 0 || debug != 1) {
				System.out.println("Debug can only be 0 or 1");
				printUsage();
			}
		}
		
		//Create a RAF from the passed in BTreeFile and create a BTree from it
		RandomAccessFile raf = new RandomAccessFile(bTreeFile, "r");
		
		thisTree = new BTree(raf);
		
		//Create Scanner to read the queries and create result file
		Scanner queryScanner = new Scanner(queryFile);
		int startIndex = args[1].indexOf("test");
		int queryIndex = args[2].indexOf("query");
		String resultName = args[1].substring(startIndex, startIndex + 5) + "_" + args[2].substring(queryIndex, queryIndex + 6) + "_" + "result"; // This needs to be fixed. Files names are weird
		File resultFile = new File(resultName);
		fileWriter = new FileWriter(resultFile);
		
		while(queryScanner.hasNext()) {
			
			String query = queryScanner.next();
			if(cacheFlag == true) {
				searchBTreeCache(query);
			}else {
				searchBTree(query);
			}
		}
		
		fileWriter.close();
		queryScanner.close();
		System.out.println("Query has been successfully executed.");
	}

	private static void printUsage() {
		
		System.out.println("Invalid usage:");
		System.out.println("java GeneBankSearch <0/1(no/with Cache)> <btree file> <query file> [<cache size>]\r\n" + 
				"[<debug level>]\r\n" + 
				"");
		
	}
	
	private static void searchBTree(String query) throws Exception {
		
		query = query.toLowerCase();
		//Convert subsequence into 2-bit binary
		String binarySeq = "";
		
		for(int i = 0; i < query.length(); i++) {
			
			if(query.charAt(i) == 'a') {
				binarySeq = binarySeq + "00";
			}
			else if(query.charAt(i) == 'c') {
				binarySeq = binarySeq + "01";
			}
			else if(query.charAt(i) == 'g') {
				binarySeq = binarySeq + "10";
			}
			else if(query.charAt(i) == 't') {
				binarySeq = binarySeq + "11";
			}
			
		}
		
		//Long value to be searched for in the tree
		Long value = Long.parseLong(binarySeq, 2);
		TreeObject key = new TreeObject(value, 0);
		
		BTreeNode foundNode = thisTree.BTreeSearch(thisTree.getRoot(), value);
		
		if(foundNode == null) {
			fileWriter.write(query.toLowerCase() + ": 0\n");
		}
		else {
			for(int i = 0; i < foundNode.n; i++) {
				if(foundNode.keys[i].compareTo(key) == 0) {
					fileWriter.write(foundNode.keys[i].toString() + "\n");
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private static void searchBTreeCache(String query) throws Exception {
			
			query = query.toLowerCase();
			//Convert subsequence into 2-bit binary
			String binarySeq = "";
			
			for(int i = 0; i < query.length(); i++) {
				
				if(query.charAt(i) == 'a') {
					binarySeq = binarySeq + "00";
				}
				else if(query.charAt(i) == 'c') {
					binarySeq = binarySeq + "01";
				}
				else if(query.charAt(i) == 'g') {
					binarySeq = binarySeq + "10";
				}
				else if(query.charAt(i) == 't') {
					binarySeq = binarySeq + "11";
				}
				
			}
			
			//Long value to be searched for in the tree
			Long value = Long.parseLong(binarySeq, 2);
			TreeObject key = new TreeObject(value, 0);
			
			if(cache.getObject(key)) {
				cache.addToTop(key);
			}else {
				cache.addObject(key);
			}
			
			BTreeNode foundNode = thisTree.BTreeSearch(thisTree.getRoot(), value);
			
			if(foundNode == null) {
				fileWriter.write(query.toLowerCase() + ": 0\n");
			}
			else {
				for(int i = 0; i < foundNode.n; i++) {
					if(foundNode.keys[i].compareTo(key) == 0) {
						fileWriter.write(foundNode.keys[i].toString() + "\n");
					}
				}
			}
		}
}
