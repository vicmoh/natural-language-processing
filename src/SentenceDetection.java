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

public class SentenceDetection {
    /**
     * The path for the OpenNLP models.
     */
    private static final String OPEN_NLP_MODELS_PATH = "../packages/OpenNLP_models/en-sent.bin";

    /**
     * Set up a debugger for debug printing.
     */
    private static Debugger debug = Debugger.init().showDebugPrint(true).setClassName("SentenceDetectionME");

    /**
     * Main for running the sentences detection program.
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String args[]) throws Exception {
        // Check if file exist in argument.
        final String FILE_OUTPUT_PATH = "../output/data.splitted";
        System.out.println();
        if (args.length <= 0) {
            System.out.println("Sorry, no file detected.");
            return;
        }
        String fileName = args[0];
        debug.setFunctionName("main").print("Starting sentence detector...");
        debug.print("File name is \"" + fileName + "\"");

        // Load sentence detector model
        InputStream modelData = new FileInputStream(OPEN_NLP_MODELS_PATH);
        SentenceModel model = new SentenceModel(modelData);

        // Instantiate SentenceDetectorME
        SentenceDetectorME detector = new SentenceDetectorME(model);

        // Run detectorF
        try {
            LinkedList<Document> docs = Document.parse(Util.readFile(fileName), text -> {
                String paragraph = "";
                String sentence[] = detector.sentDetect((String) text);
                for (String each : sentence)
                    paragraph += each + "\n";
                return paragraph.trim();
            });
            Util.writeFile(FILE_OUTPUT_PATH, Document.stringify(docs));
        } catch (Exception err) {
            System.out.print(err.getMessage());
        }
    }
}
