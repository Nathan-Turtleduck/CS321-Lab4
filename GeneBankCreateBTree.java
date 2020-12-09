import java.io.File;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class GeneBankCreateBTree {

	static BTree tree;
	static int seqSize; //sub sequence length
	static int degree; //degree of tree
	static File file;
	static File dump;
	
	
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
				throw new Exception();
			}
			
			//degree of tree
			if(Integer.parseInt(args[1]) == 0) {
				//FIND OPTIMAL DEGREE
			} else {
				degree = Integer.parseInt(args[1]); 
			}
			
			//length of subsequences
			if(Integer.parseInt(args[3]) >= 1 || Integer.parseInt(args[3]) <= 31) {
				
				seqSize = Integer.parseInt(args[3]); 
			} else {
				System.out.println("Subsequence length is not within domain of 1 - 31 (inclusive)");
				printUsage();
			}
			
			//create new files
			file = new File(args[2] + ".btree.data." + seqSize + "." + degree);
			RandomAccessFile RAF = new RandomAccessFile(file, "rw");
			tree = new BTree(degree, RAF);
			
			//START TO SCAN THE GBK FILE
			
			
			
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
	

}
