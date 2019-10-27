import java.util.Scanner;
import opennlp.tools.stemmer.PorterStemmer;

public class Normalize {
    /**
     * Init the debugger to show debug prints
     */
    private static Debugger debug = Debugger.init().showDebugPrint(true).setClassName("Normalize");

    /**
     * Main functions to run the program
     * @param args
     * @throws Exception
     */
    public static void main(String args[]) throws Exception {
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
}
