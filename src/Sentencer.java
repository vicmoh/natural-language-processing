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

public class Sentencer {
    /**
     * The path for the OpenNLP models.
     */
    private static final String OPEN_NLP_MODELS_PATH = "../packages/OpenNLP_models/en-sent.bin";

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
        // Check if file exist in argument.
        final String FILE_OUTPUT_PATH = "../output/data.splitted";
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
     * Min number of sentence in document.
     */
    public int minSentence = 0;

    /**
     * Max number of sentence in document
     */
    public int maxSentence = 0;

    /**
     * The avg sentence in the documents
     */
    public double avgSentence = 0;

    /**
     * The total sentence in the documents
     */
    public int totalSentence = 0;

    /**
     * To check if first doc is passed when parsing
     */
    public boolean isFirstDocPassed = false;

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
                for (String each : sentence) {
                    paragraph += each + "\n";
                    this.totalSentence++;
                }
                // Cases for min and max sentences
                if (sentence.length > this.maxSentence)
                    this.maxSentence = sentence.length;
                if (sentence.length < this.minSentence || isFirstDocPassed == false) {
                    this.minSentence = sentence.length;
                    isFirstDocPassed = true;
                }
                // Return paragraph
                return paragraph.trim();
            });
        } catch (Exception err) {
            throw new Exception(err.getMessage());
        }
        // Return
        this.isFirstDocPassed = false;
        this.avgSentence = this.totalSentence / docs.size();
        return docs;
    }

    /**
     * Get the string of the stats result.
     * 
     * @return the stats result.
     */
    public String toStatsString() {
        String min = "Min sentences: " + Integer.toString(minSentence) + "\n";
        String max = "Max sentences: " + Integer.toString(maxSentence) + "\n";
        String avg = "Avg sentences: " + Double.toString(avgSentence) + "\n";
        return min + max + avg;
    }
}
