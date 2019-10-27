import lib.*;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

/**
 * Class for tagging the tokenized data to the tagged model data.
 */
public class Tagger {
    /**
     * Debugger for print.
     */
    static final Debugger debug = Debugger.init().setClassName("Tagger").showDebugPrint(false);

    // Init
    private static String TOKEN_NLP = "../packages/OpenNLP_models/en-token.bin";
    private static String MAXENT_NLP = "../packages/OpenNLP_models/en-pos-maxent.bin";
    private static InputStream posTagStream;
    private static POSModel posModel;
    private static POSTaggerME posTagger;

    /**
     * Function for processing the title and body
     * 
     * @param text
     * @return
     */
    private static Object process(Object text) {
        debug.print("text parse = " + (String) text);
        String toBeSplit = (String) text;
        String toBeReturn = "";
        // Process each line
        String[] lines = toBeSplit.split("[\r]?[\n]");
        for (String line : lines) {
            // Process the token
            String[] tokens = line.split("[ ]");
            String[] tags = posTagger.tag(tokens);
            for (int i = 0; i < tokens.length; i++)
                toBeReturn += tokens[i] + "/" + tags[i] + " ";
            // New line
            toBeReturn += "\n";
        }
        // To be return
        return toBeReturn.replaceAll("[ ]+[\r]*[\n]+", "\n");
    }

    public static void main(String args[]) throws Exception {
        if (args[0].length() < 1)
            throw new Exception("Argument is less than 1, no file detected.");

        // Setup
        String fileName = args[0];
        debug.setFunctionName("mains");
        debug.print("Invoked with file: " + args[0]);
        posTagStream = new FileInputStream(MAXENT_NLP);
        posModel = new POSModel(posTagStream);
        posTagger = new POSTaggerME(posModel);

        // Parse
        LinkedList<Document> docs = Document.parse(Util.readFileWithNewLine(fileName),
                text -> ((String) (text)).split("[ ]"), text -> process(text), text -> process(text));

        // Output
        String res = Document.stringify(docs);
        System.out.println(res);
        Util.writeFile("../output/data.tagged", res);
    }
}
