import lib.*;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.IOException;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

public class SentenceDetectionME {
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

        // Run detector
        Document.parse(Util.readFile(fileName));

        // for (String arg : args) {
        // String sentences[] = detector.sentDetect(Util.readFile(arg));
        // for (String sent : sentences)
        // System.out.pSrintln(sent);
        // }
    }
}
