package cs321.create;

import cs321.btree.BTree;
import cs321.common.ParseArgumentException;
import cs321.common.GeneBankParser;

import java.util.Scanner;
import java.io.*;


public class GeneBankCreateBTree
{

    public static void main(String[] args) throws Exception
    {
        System.err.println("Hello world from GeneBank Create BTree!");
        GeneBankCreateBTreeArguments gbkArgs = parseArgumentsAndHandleExceptions(args);
        String BTreeFileName = gbkArgs.getGbkFileName() + ".btree.data." + 
						gbkArgs.getSubsequenceLength() + "." +
						gbkArgs.getDegree();
        System.err.println("Args parsed!");
        //TODO add cache functionality here
        BTree<Long> tree = new BTree<Long>(BTreeFileName,gbkArgs.getDegree());
        GeneBankParser parser = new GeneBankParser(gbkArgs.getSubsequenceLength(), gbkArgs.getGbkFileName());
        System.err.println("Generating Tree! This may take a moment...");
        while(parser.hasNext()) {
        	tree.insert(SequenceUtils.dnaStringToLong(parser.next()));
        }
        parser.finalize();
        System.err.println("Tree Created at " + BTreeFileName + "!");
        if(gbkArgs.getDebugLevel() == 1) {
        	File dumpFile = new File(gbkArgs.getGbkFileName() +".dump." +gbkArgs.getSubsequenceLength());
        	PrintWriter write = new PrintWriter(dumpFile);
        	Scanner scan = new Scanner(tree.toStringParseable());
        	while(scan.hasNextLine()) {
        		write.println(SequenceUtils.longToDnaString(scan.nextLong(),gbkArgs.getSubsequenceLength()) + " " + scan.nextInt());
        		scan.nextLine();
        	}
        	write.close();
        	System.err.println("Tree dumped to file " + gbkArgs.getGbkFileName() +".dump." + gbkArgs.getSubsequenceLength());
        }
        
        System.err.println("Have a great day! The great power of this tree brings great responsibility...");
    }

    /**
     * parses args and handles any ParseArgumentException thrown
     * @param args
     * @return parsed arguments
     */
    private static GeneBankCreateBTreeArguments parseArgumentsAndHandleExceptions(String[] args)
    {
        GeneBankCreateBTreeArguments geneBankCreateBTreeArguments = null;
        try
        {
            geneBankCreateBTreeArguments = parseArguments(args);
        }
        catch (ParseArgumentException e)
        {
            printUsageAndExit(e.getMessage());
        }
        return geneBankCreateBTreeArguments;
    }

    /**
     * prints program usage
     * @param errorMessage message of the given error
     */
    private static void printUsageAndExit(String errorMessage)
    {
    	System.err.println(errorMessage);
    	System.err.println("Usage: java -jar build/libs/GeneBankCreateBTree.jar --cache=<0|1>  --degree=<btree-degree>" 
    						+ "\n--gbkfile=<gbk-file> --length=<sequence-length> [--cachesize=<n>] [--debug=0|1]");
        System.exit(1);
    }

    /**
     * parses the program args 
     * @param args of the program
     * @return object storing the parsed arguments
     * @throws ParseArgumentException thrown when failures occur in parsing
     */
    public static GeneBankCreateBTreeArguments parseArguments(String[] args) throws ParseArgumentException
    {
    	boolean useCache = false;
    	boolean useCacheSet = false;
    	int degree = -1;
    	int cacheSize = -1;
    	String gbkFileName = null;
    	int length = -1;
    	int debugLevel = 0;
    	for(int i = 0; i < args.length; i++) {
	    	if(args[i].length() > 8 && args[i].substring(0,8).equals("--cache=")) {
	    		if(args[i].substring(8).equals("1")) {
	    			useCache = true;
	    		}else if(args[i].substring(8).equals("0")) {
	    			useCache = false;
	    		}else {
		    		throw new ParseArgumentException("--cache expects either 1 or 0. You entered: " + args[i].substring(8));
	    		}
	    		useCacheSet = true;
	    	}else if(args[i].length() > 9 && args[i].substring(0,9).equals("--degree=")) {
	    		if(args[i].substring(9).equals("0")) {
	    			degree = 204;
	    		}else {
	    			try {
						degree = Integer.parseInt(args[i].substring(9));
						if(degree < 0) {
				    		throw new ParseArgumentException("--degree expects a positive integer value. You entered: " + args[i].substring(9));
						}
					} catch (NumberFormatException e) {
			    		throw new ParseArgumentException("--degree expects a positive integer value. You entered: " + args[i].substring(9));
					}
	    		}
	    	}else if(args[i].length() > 10 && args[i].substring(0,10).equals("--gbkfile=")) {
	    		if(!(args[i].substring(args[i].length() - 4, args[i].length()).equals(".gbk"))) {
		    		throw new ParseArgumentException("--gbkfile expects a .gbk file. You entered: " + args[i].substring(10));
	    		}
	    		gbkFileName = args[i].substring(10);
	    		File gbkFile = new File(gbkFileName);
	    		if(!gbkFile.exists()){
		    		throw new ParseArgumentException("Given .gbk file does not exist! You entered: " + args[i].substring(10));
	    		}
	    	}else if(args[i].length() > 9 && args[i].substring(0,9).equals("--length=")) {
	    		try {
					length = Integer.parseInt(args[i].substring(9));
					if(length < 1 || length > 31) {
			    		throw new ParseArgumentException("--length expects an iteger between 1 and 31. You entered: " + args[i].substring(9));
					}
				} catch (NumberFormatException e) {
		    		throw new ParseArgumentException("--length expects an iteger. You entered: " + args[i].substring(9));
				}
	    	}else if(args[i].length() > 12 && args[i].substring(0,12).equals("--cachesize=")) {
	    		try {
					length = Integer.parseInt(args[i].substring(12));
					if(cacheSize < 0) {
			    		throw new ParseArgumentException("--cachesize expects a positive integer value. You entered: " + args[i].substring(12));
					}
				} catch (NumberFormatException e) {
		    		throw new ParseArgumentException("--length expects an iteger. You entered: " + args[i].substring(9));
				}
	    	}else if(args[i].length() > 8 && args[i].substring(0,8).equals("--debug=")) {
	    		if(args[i].substring(8).equals("1")) {
	    			debugLevel = 1;
	    		}else if(args[i].substring(8).equals("0")) {
	    			debugLevel = 0;
	    		}else {
	    			throw new ParseArgumentException("--debug expects either 1 or 0. You entered: " + args[i].substring(8));
	    		}
	    	}else {
	    		throw new ParseArgumentException("Unknown argument: " + args[i]);
	    	}
    	}
    	if(!useCacheSet || degree < 0 || gbkFileName == null || length < 0) {
    		throw new ParseArgumentException("Insufficient arguments!");
		}
    	if(useCache && cacheSize < 0) {
    		throw new ParseArgumentException("--cache expects --cachesize to be added");
    	}
    	return new GeneBankCreateBTreeArguments(useCache, degree, gbkFileName, length, cacheSize, debugLevel);
    }

}
