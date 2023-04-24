package cs321.search;

import cs321.btree.BTree;
import cs321.common.ParseArgumentException;
import cs321.common.ParseArgumentUtils;

public class GeneBankSearchBTree
{

    public static void main(String[] args) throws Exception
    {
        System.out.println("Hello world from cs321.search.GeneBankSearchBTree.main");
        GeneBankSearchBTreeArguments geneBankSearchBTreeArguments = parseArgumentsAndHandleExceptions(args);
        //TODO: arguments should be parsed now. Now do something. Do whatever is being done here.

    }

    private static GeneBankSearchBTreeArguments parseArgumentsAndHandleExceptions(String[] args)
    {
        GeneBankSearchBTreeArguments geneBankSearchBTreeArguments = null;
        try
        {
            geneBankSearchBTreeArguments = parseArguments(args);
        }
        catch (ParseArgumentException e)
        {
            printUsageAndExit(e.getMessage());
        }
        return geneBankSearchBTreeArguments;
    }

    private static void printUsageAndExit(String errorMessage)
    {
        System.err.println(errorMessage);
        System.err.println("Usage: java -jar build/libs/GeneBankSearchBTree.jar --cache=<0/1> --degree=<btree-degree>" +
        " --btreefile=<b-tree-file> --length=<sequence-length> --queryfile=<query-file> " +
        " [--cachesize=<n>] [--debug=0|1]");
        System.exit(1);
    }

    public static GeneBankSearchBTreeArguments parseArguments(String[] args) throws ParseArgumentException
    {
        if (args.length < 5 || args.length > 7) {
            throw new ParseArgumentException("Error: Invalid number of arguments passed in.");
        }
        String[] option = new String[7];
        String[] value = new String[7];
        
        for (int i = 0; i < args.length; i++) {
            String[] tmpA = new String[2];
            tmpA = args[i].split("=");
            option[i] = tmpA[0];
            value[i] = tmpA[1];
        }

        boolean useCache = false;;
        int degree = 0;
        String btreeFileName = "";
        int subsequenceLength = 0;
        String queryFileName = "";
        int cacheSize = 0;
        int debugLevel = 0;

        for (int i = 0; i < option.length; i++) {
            switch (option[i]) {
                case "--cache":
                    if (Integer.parseInt(value[i]) == 1) {
                        useCache = true;
                    } else {
                        useCache = false;
                    }
                    break;
                case "--degree":
                    degree = Integer.parseInt(value[i]);
                    break;
                case "--btreefile":
                    btreeFileName = value[i];
                    break;
                case "--length":
                    subsequenceLength = Integer.parseInt(value[i]);
                    break;
                case "--queryfile": 
                    queryFileName = value[i];
                    break;
                case "--cachesize":
                    cacheSize = Integer.parseInt(value[i]);
                    break;
                case "--debug":
                    debugLevel = Integer.parseInt(value[i]);
                    break;
                default:
                    throw new ParseArgumentException("Error: Invalid argument(s)");
            }
        }
        if (useCache == true && cacheSize <= 0) {
            throw new ParseArgumentException("Error: Cache size must be specified if cache is being used");
        }
        else {
            GeneBankSearchBTreeArguments gBankSearchBTreeArguments= new GeneBankSearchBTreeArguments(useCache, degree, btreeFileName, subsequenceLength, queryFileName, cacheSize, debugLevel);
            return gBankSearchBTreeArguments;
        }
    }

}

