import lib.*;
import java.util.Scanner;
import opennlp.tools.stemmer.PorterStemmer;
import java.util.*;

public class Preprocessed {
    /**
     * Init the debugger to show debug prints
     */
    private static Debugger debug = Debugger.init().showDebugPrint(false).setClassName("Normalize");

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
        System.out.println("***********************");
        System.out.println(val);
        System.out.println("***********************");
        try {
            String regSkip = "([^'^\\s\\w])|[0-9]";
            String regPunc = "([^'^\\s\\w])|[0-9]";
            String regNum = "[-+]?[0-9]*[\\.,]?[0-9]+";
            String toBeEdited = val;
            String res = "";
            String[] lines = toBeEdited.split("[\n]+|[\r\n]+");
            for (String line : lines) {
                String[] splitted = line.trim().split("[ \t]+");
                boolean isFirst = true;
                for (String each : splitted) {
                    each = each.replaceAll(regPunc, "");
                    if (!(isStopWord(each) || each.matches(regNum) || each.matches(regPunc)
                            || (each.length() == 1 && each.equals("'")))) {
                        if (isFirst) {
                            isFirst = false;
                            res += each;
                        } else
                            res += " " + each;
                    }
                }
                res += "\n";
            }
            return res;
        } catch (Exception err) {
            return val;
        }
    }

    /**
     * Read the stop words list file and saved it.
     * 
     * @param filePath of the stop words, by default it is "../assets/stopwords.txt"
     */
    public static void readStopWordsListFile(String filePath) throws Exception {
        String path = "../assets/stopwords.txt";
        if (filePath != null)
            path = filePath;
        String words = Util.readFileWithNewLine(path);
        String[] list = words.split("[\n]|[\r\n]");
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
            if (stopWord.replaceAll("[\n ]+|[\r\n ]+", "").equals(val.replaceAll("[\n ]+|[\r\n ]+", "")))
                return true;
        return false;
    }

    /**
     * Stemmed the word in the file.
     * 
     * @param val
     * @return String
     */
    public static String stemmed(String val) {
        String toBeReturn = "";
        PorterStemmer stemmer = new PorterStemmer();
        String[] tokens = val.split("[ \t]+");
        boolean isFirst = true;
        for (String each : tokens) {
            String stemmed = stemmer.stem(each.replaceAll("['']", ""));
            if (isFirst) {
                isFirst = false;
                toBeReturn += stemmed;
            } else
                toBeReturn += " " + stemmed;
        }
        return toBeReturn;
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
        debug.print("fileName: " + fileName);
        LinkedList<Document> docs = Document.parse(Util.readFileWithNewLine(fileName), text -> {
            return ((String) (text)).split("[ \t]+");
        }, text -> {
            return (Object) stemmed(removeNumPuncAndStopWords(setTextToLowerCase((String) text)));
        }, text -> {
            return (Object) stemmed(removeNumPuncAndStopWords(setTextToLowerCase((String) text)));
        });

        /// Output
        String outString = "";
        for (Document doc : docs)
            outString += doc.toString();
        Util.writeFile("../output/data.stemmed", outString);
    }
}
