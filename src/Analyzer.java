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
        LinkedList<Document> docs = sen.run(fileName);

        // File?
        String fileBeingCheck = "File: " + fileName + "\n";
        // (1) How many documents are there in the data collection?
        String numOfDocs = "Number of documents: " + Integer.toString(docs.size()) + "\n";
        // (2) What are the min, avg, and max document lengths of sentences?
        String statsResult = sen.toStatsString() + "\n";

        String result = fileBeingCheck + numOfDocs + statsResult;
        Util.writeFile("../output/data.stats", result);
    }
}