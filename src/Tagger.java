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

    /**
     * Read a file into one full string.
     * 
     * @param filePath
     * @return The file strings
     */
    public static String readLine(String filePath) {
        debug.setClassName("readLine");
        debug.print("File path = " + filePath);
        BufferedReader reader;
        String line = "";
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String temp = reader.readLine();
            if (temp != null)
                line += temp + " \n";
            while (temp != null) {
                temp = reader.readLine();
                if (temp == null)
                    break;
                line += temp + " \n";
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        debug.print("line = " + line);
        return line;
    }

    public static void main(String args[]) throws Exception {
        if (args[0].length() < 1)
            throw new Exception("Argument is less than 1, no file detected.");

        // Setup
        final String TOKEN_NLP = "../packages/OpenNLP_models/en-token.bin";
        final String MAXENT_NLP = "../packages/OpenNLP_models/en-pos-maxent.bin";
        String fileName = args[0];
        debug.setFunctionName("mains");
        debug.print("Invoked with file: " + args[0]);

        // Init
        InputStream posTagStream = new FileInputStream(MAXENT_NLP);
        POSModel posModel = new POSModel(posTagStream);
        POSTaggerME posTagger = new POSTaggerME(posModel);

        // Parse
        LinkedList<Document> docs = Document.parseToken(readLine(fileName), text -> {
            debug.print("text parse = " + (String) text);
            String toBeSplit = (String) text;
            String toBeReturn = "";

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
        });

        // Output
        String res = Document.stringify(docs);
        System.out.println(res);
        Util.writeFile("../output/data.tagged", res);
    }
}
