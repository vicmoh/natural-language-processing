import lib.*;
import java.util.Scanner;
import opennlp.tools.stemmer.PorterStemmer;
import java.util.*;

public class Normalize {
    /**
     * Init the debugger to show debug prints
     */
    private static Debugger debug = Debugger.init().showDebugPrint(true).setClassName("Normalize");

    /**
     * List of stop words to be removed
     */
    private static String[] listOfStopWords = null;

    /**
     * Function that comes from the example
     */
    private static void example() {
        // Create an instance of stemmer
        PorterStemmer stemmer = new PorterStemmer();

        // Stemming a file line by line
        Scanner input = new Scanner(System.in);
        while (input.hasNextLine()) {
            String line = input.nextLine();
            String[] tokens = line.split("[ \t]+");
            System.out.print(stemmer.stem(tokens[0]));
            for (int i = 1; i < tokens.length; i++)
                System.out.print(" " + stemmer.stem(tokens[i]));
            System.out.println();
        }
    }

    /**
     * Function to set each token to lower case.
     * 
     * @param string to be lowered case.
     * @return the string.
     */
    public static String setTextToLowerCase(String string) {
        debug.setFunctionName("setTextToLowerCase");
        String toBeLowered = string;
        String res = toBeLowered.toLowerCase();
        debug.print(res);
        return res;
    }

    /**
     * Remove the number, punctuation and stop words in the data.
     * 
     * @param val to be removed.
     * @return String of the removed data.
     */
    public static String removeNumPuncAndStopWords(String val) {
        String toBeEdited = val;
        String[] splitted = toBeEdited.split("[ ]");
        String res = "";
        boolean isFirst = true;
        for (String each : splitted) {
            if (each.matches("[-+]?[0-9]*[\\.,]?[0-9]+") || each.matches("[\\.,!?:;]") || isStopWord(each)) {
            } else {
                if (isFirst) {
                    isFirst = false;
                    res += each;
                } else if (!isFirst) {
                    res += " " + each;
                }
            }
        }
        return res;
    }

    /**
     * Read the stop words list file and saved it.
     * 
     * @param filePath of the stop words, by default it is "../output/stopwords.txt"
     */
    public static void readStopWordsListFile(String filePath) throws Exception {
        String path = "../output/stopwords.txt";
        if (filePath != null)
            path = filePath;
        String words = Util.readFileWithNewLine(path);
        String[] list = words.split("[\n ]+|[\r\n ]+");
        listOfStopWords = list;
    }

    /**
     * Check if it is stop word.
     * 
     * @param isStopWord val to be checked.s
     * @return Boolean whether it is stop word or not.
     */
    public static boolean isStopWord(String val) throws Exception {
        if (listOfStopWords == null)
            throw new Exception("Stop words is empty. Read the file and re-run.");
        for (String stopWord : listOfStopWords)
            if (stopWord.equals(val))
                return true;
        return false;
    }

    /**
     * Main functions to run the program
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String args[]) throws Exception {
        debug.setFunctionName("main").print("Invoked");
        readStopWordsListFile(null);

        // Read the tokenized file.
        String fileName = args[0];
        LinkedList<Document> docs = Document.parse(Util.readFileWithNewLine(fileName), text -> {
            return ((String) (text)).split("[ ]");
        }, text -> {
            return (Object) removeNumPuncAndStopWords(setTextToLowerCase((String) text));
        }, text -> {
            return (Object) removeNumPuncAndStopWords(setTextToLowerCase((String) text));
        });

        /// Output
        String outString = "";
        for (Document doc : docs)
            outString += doc.toString();
        Util.writeFile("../output/data.normalize", outString);
    }
}
