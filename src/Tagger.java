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

public class Tagger {

    /**
     * Debugger for print
     */
    static final Debugger debug = Debugger.init().setClassName("Tagger").showDebugPrint(true);

    // /**
    // *
    // * @param arg
    // * @return
    // * @throws Exception
    // */
    // public static String readString(String arg) throws Exception {
    // debug.setFunctionName("readString");
    // InputStream inFile = new FileInputStream(arg);
    // BufferedReader buf = new BufferedReader(new InputStreamReader(inFile));
    // StringBuilder sb = new StringBuilder();
    // String line = buf.readLine();
    // String toBeReturn = "";
    // while (line != null) {
    // sb.append(line + " ");
    // line = buf.readLine();
    // toBeReturn = toBeReturn + line + "\n";
    // debug.print(toBeReturn);
    // }
    // buf.close();
    // return toBeReturn;
    // }

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
                System.out.println(line);
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

            // trimmed the token
            String[] tokens = toBeSplit.split("[ ]");
            String[] trimmedTokens = toBeSplit.split("[ ]");
            debug.print("number of tokens = " + tokens.length);
            debug.print("number of trimmed = " + trimmedTokens.length);

            for (int x = 0; x < trimmedTokens.length; x++)
                trimmedTokens[x] = trimmedTokens[x].replaceAll("[ \n\r]", "");

            // Process the token
            String[] tags = posTagger.tag(trimmedTokens);
            for (int i = 0; i < tokens.length; i++)
                toBeReturn += tokens[i] + "/" + tags[i] + " ";

            // To be return
            return toBeReturn.replaceAll("[ ]+[\r]*[\n]+", "\n");
        });

        // Output
        Util.writeFile("../output/data.tagged", Document.stringify(docs));
    }
}
