
import java.io.File;

public class GeneBankSearch {

	static int cacheUse;
	static File bTreeFile;
	static File queryFile;
	static int sequenceLength;
	
	public static void main(String[] args) {
		
		if(args.length < 3 || args.length > 5) {
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
		
		sequenceLength = Integer.parseInt(args[3]);
		

	}

	private static void printUsage() {
		
		System.out.println("Invalid usage:");
		System.out.println("java GeneBankSearch <0/1(no/with Cache)> <btree file> <query file> [<cache size>]\r\n" + 
				"[<debug level>]\r\n" + 
				"");
		
	}
}
