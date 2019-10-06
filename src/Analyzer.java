import java.util.LinkedList;
import java.util.ListIterator;

import lib.*;

/**
 * Class for analyzing the data of the document.
 */
class Analyzer {
    /**
     * Create a debugger to show some prints
     */
    final static Debugger debug = Debugger.init().showDebugPrint(true).setClassName("Analyzer");

    // For the statistics
    public static int minTokens = 0;
    public static int maxTokens = 0;
    public static double avgTokens = 0;
    public static int totalTokens = 0;
    public static int minSentence = 0;
    public static int maxSentence = 0;
    public static double avgSentence = 0;
    public static int totalSentence = 0;
    public static boolean isFirstDocPassedForTok = false;
    public static boolean isFirstDocPassedForSen = false;

    /**
     * The main function to run the program
     * 
     * @param args of the filename for the first argument
     */
    public static void main(String args[]) throws Exception {
        if (args.length < 1)
            throw new Exception("Argument for the file must be passed.");

        // Init
        int docsCount = 0;
        String fileName = args[0];
        Tokenizer tok = new Tokenizer();
        Sentencer sen = new Sentencer();
        LinkedList<Document> tokDocs = Document.parse(tok.run(fileName), null);
        LinkedList<Document> senDocs = sen.run(fileName);

        // Calculate for tokens
        for (Document doc : tokDocs) {
            totalTokens += doc.numOfTokens;
            if (doc.numOfTokens > maxTokens)
                maxTokens = doc.numOfTokens;
            if (doc.numOfTokens < minTokens || isFirstDocPassedForTok == false) {
                minTokens = doc.numOfTokens;
                isFirstDocPassedForTok = true;
            }
        }
        // Calculate for sentences
        for (Document doc : senDocs) {
            totalSentence += doc.numOfSentences;
            if (doc.numOfSentences > maxSentence)
                maxSentence = doc.numOfSentences;
            if (doc.numOfSentences < minSentence || isFirstDocPassedForSen == false) {
                minSentence = doc.numOfSentences;
                isFirstDocPassedForSen = true;
            }
        }
        // Count the average
        avgTokens = (double) totalTokens / (double) tokDocs.size();
        avgSentence = (double) totalSentence / (double) senDocs.size();

        // File?
        String fileBeingCheck = "File: " + fileName + "\n";
        // (1) How many documents are there in the data collection?
        String numOfDocs = "Number of documents: " + Integer.toString(tokDocs.size()) + "\n";
        // (2) What are the min, avg, and max document lengths of sentences?
        // (3) What are the min, avg, and max document lengths by the number of tokens?
        String statRes = toStatsString() + "\n";
        // (4) Average number for all
        String avgSenInAllToks = "Avg sentence lengths by the number of all tokens: ";
        String avgSenInPerDocToks = "--- Avg sentence lengths by the number of per doc tokens ---\n";
        ListIterator<Document> senIter = senDocs.listIterator();
        ListIterator<Document> tokIter = tokDocs.listIterator();
        while (tokIter.hasNext()) {
            Document senDoc = senIter.next();
            Document tokDoc = tokIter.next();
            double avg = (double) senDoc.numOfSentences / (double) tokDoc.numOfTokens;
            avgSenInPerDocToks += "Doc-" + tokDoc.getID() + ": " + avg + "\n";
        }
        avgSenInAllToks += Double.toString((double) avgSentence / (double) totalTokens) + "\n";

        // Result
        String result = fileBeingCheck + numOfDocs + statRes + avgSenInAllToks + avgSenInPerDocToks;
        System.out.println(result);
        Util.writeFile("../output/data.stats", result);
    }

    /**
     * Get the string output for the stats.
     * 
     * @return string of the stats.
     */
    public static String toStatsString() {
        String min = "Min sentences: " + Integer.toString(minSentence) + "\n";
        String max = "Max sentences: " + Integer.toString(maxSentence) + "\n";
        String avg = "Avg sentences: " + Double.toString(avgSentence) + "\n";
        String total = "Total sentences: " + Double.toString(totalSentence) + "\n";
        String minTok = "Min tokens: " + Integer.toString(minTokens) + "\n";
        String maxTok = "Max tokens: " + Integer.toString(maxTokens) + "\n";
        String avgTok = "Avg tokens: " + Double.toString(avgTokens) + "\n";
        String totalTok = "Total tokens: " + Double.toString(totalTokens) + "\n";
        return min + max + avg + total + minTok + maxTok + avgTok + totalTok;
    }
}