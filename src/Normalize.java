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
     * Function to set each token to lower case
     * 
     * @param string to be lowered case
     * @return the string
     */
    public static String setTextToLowerCase(String string) {
        debug.setFunctionName("setTextToLowerCase");
        String toBeLowered = string;
        String res = toBeLowered.toLowerCase();
        debug.print(res);
        return res;
    }

    /**
     * Main functions to run the program
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String args[]) throws Exception {
        debug.setFunctionName("main").print("Invoked");
        String fileName = args[0];
        LinkedList<Document> docs = Document.parse(Util.readFileWithNewLine(fileName), text -> {
            return ((String) (text)).split("[ ]");
        }, text -> {
            return (Object) setTextToLowerCase((String) text);
        }, text -> {
            return (Object) setTextToLowerCase((String) text);
        });
        /// Output
        String outString = "";
        for (Document doc : docs)
            outString += doc.toString();
        Util.writeFile("../output/data.normalize", outString);
    }
}
