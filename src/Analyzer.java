import java.util.LinkedList;

import lib.*;

/**
 * Class for analyzing the data of the document.
 */
class Analyzer {
    /**
     * Create a debugger to show some prints
     */
    final static Debugger debug = Debugger.init().showDebugPrint(true).setClassName("Analyzer");

    /**
     * The main function to run the program
     * 
     * @param args of the filename for the first argument
     */
    public static void main(String args[]) throws Exception {
        if (args.length < 1)
            throw new Exception("Argument for the file must be passed.");

        // Init
        String fileName = args[0];
        Sentencer sen = new Sentencer();
        Tokenizer tok = new Tokenizer();
        LinkedList<Document> docs = sen.run(fileName);
        tok.runTokenStats();

        // File?
        String fileBeingCheck = "File: " + fileName + "\n";
        // (1) How many documents are there in the data collection?
        String numOfDocs = "Number of documents: " + Integer.toString(docs.size()) + "\n";
        // (2) What are the min, avg, and max document lengths of sentences?
        String senRes = sen.toStatsString() + "\n";
        // (3) What are the min, avg, and max document lengths by the number of tokens?
        String tokRes = tok.toStatsString() + "\n";

        // Result
        String result = fileBeingCheck + numOfDocs + senRes + tokRes;
        Util.writeFile("../output/data.stats", result);
    }
}