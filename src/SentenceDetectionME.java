import lib.Debugger;
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
     * Read the string and return whole file into one string, without new line.
     * 
     * @param fileName to be read.
     * @return the string builder.
     * @throws Exception.
     */
    public static String readString(String fileName) throws Exception {
        debug.setFunctionName("readString").print("Invoked.");
        // Try to read the whole string
        try {
            InputStream inFile = new FileInputStream(fileName);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(inFile));
            StringBuilder sb = new StringBuilder();
            String line = buffer.readLine();
            while (line != null) {
                sb.append(line + " ");
                line = buffer.readLine();
            }
            buffer.close();
            writeData(sb.toString());
            return sb.toString();
        } catch (Exception exception) {
            throw Exception("Exception ocurred. Could not read file.");
        }
    }

    /**
     * Use Streams when you are dealing with raw data to write the data to
     * <name>.splitted.
     * 
     * @param data is the data to written.
     */
    private static void writeData(String data) {
        OutputStream os = null;
        try {
            os = new FileOutputStream("../output/data.splitted");
            os.write(data.getBytes(), 0, data.length());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

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
        debug.setFunctionName("main").print("Starting sentence detector...");
        // Load sentence detector model
        InputStream modelData = new FileInputStream(OPEN_NLP_MODELS_PATH);
        SentenceModel model = new SentenceModel(modelData);
        // Instantiate SentenceDetectorME
        SentenceDetectorME detector = new SentenceDetectorME(model);
        // Allow multiple files to be processed
        for (String arg : args) {
            // Split a file into sentences
            String sentences[] = detector.sentDetect(readString(arg));
            // Print the sentences
            for (String sent : sentences)
                System.out.println(sent);
        }
    }
}
