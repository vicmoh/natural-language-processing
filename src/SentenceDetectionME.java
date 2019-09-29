import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

public class SentenceDetectionME {
   /**
    * The path for the OpenNLP models.
    */
   private static final String OPEN_NLP_MODELS_PATH = "./packages/OpenNLP_models/en-sent.bin";

   /**
    * Read each arg string.
    * 
    * @param arg of the detected sentences.
    * @return the string builder.
    * @throws Exception.
    */
   public static String readString(String arg) throws Exception {
      InputStream inFile = new FileInputStream(arg);
      BufferedReader buffer = new BufferedReader(new InputStreamReader(inFile));
      StringBuilder sb = new StringBuilder();
      String line = buffer.readLine();
      while (line != null) {
         sb.append(line + " ");
         line = buffer.readLine();
      }
      buffer.close();
      return sb.toString();
   }

   /**
    * Main for running the sentences detection program.
    * 
    * @param args
    * @throws Exception
    */
   public static void main(String args[]) throws Exception {
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
