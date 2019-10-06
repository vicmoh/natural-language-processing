import lib.*;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.LinkedList;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

/**
 * Class to make the paragraph into sentences
 */
public class Sentencer {
    /**
     * The file output path
     */
    public static final String FILE_OUTPUT_PATH = "../output/data.splitted";
    /**
     * The path for the OpenNLP models.
     */
    public static final String OPEN_NLP_MODELS_PATH = "../packages/OpenNLP_models/en-sent.bin";

    /**
     * Set up a debugger for debug printing.
     */
    private static Debugger debug = Debugger.init().showDebugPrint(false).setClassName("SentenceDetectionME");

    /**
     * Main for running the sentences detection program.
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String args[]) throws Exception {

        if (args.length <= 0)
            throw new Exception("Sorry, no file detected.");

        // init
        String fileName = args[0];
        debug.setFunctionName("main").print("Starting sentence detector...");
        debug.print("File name is \"" + fileName + "\"");

        // Run
        LinkedList<Document> docs = new Sentencer().run(fileName);
        Util.writeFile(FILE_OUTPUT_PATH, Document.stringify(docs));
    }

    /**
     * Construct sentencer to split into sentence
     * 
     * @param fileName to be checked
     * @return The linked list documents
     * @throws Exception
     */
    public LinkedList<Document> run(String fileName) throws Exception {
        // Load sentence detector model and instantiate SentenceDetectorME
        InputStream modelData = new FileInputStream(OPEN_NLP_MODELS_PATH);
        SentenceModel model = new SentenceModel(modelData);
        SentenceDetectorME detector = new SentenceDetectorME(model);
        LinkedList<Document> docs = new LinkedList<Document>();

        // Run detector
        try {
            docs = Document.parse(Util.readFile(fileName), text -> {
                String paragraph = "";
                String sentence[] = detector.sentDetect((String) text);
                for (String each : sentence)
                    paragraph += each + "\n";

                // Return paragraph
                return paragraph.trim();
            });
        } catch (Exception err) {
            modelData.close();
            throw new Exception(err.getMessage());
        }

        // Return
        modelData.close();
        return docs;
    }
}
