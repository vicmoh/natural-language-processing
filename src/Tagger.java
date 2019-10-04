import lib.*;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.io.BufferedReader;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

public class Tagger {

   /**
    * Debugger for print
    */
   static final Debugger debug = Debugger.init().setClassName("Scanner").showDebugPrint(true);

   /**
    * 
    * @param arg
    * @return
    * @throws Exception
    */
   public static String readString(String arg) throws Exception {
      debug.setFunctionName("readString");
      InputStream inFile = new FileInputStream(arg);
      BufferedReader buf = new BufferedReader(new InputStreamReader(inFile));
      StringBuilder sb = new StringBuilder();
      String line = buf.readLine();
      String toBeReturn = "";
      while (line != null) {
         sb.append(line + " ");
         line = buf.readLine();
         toBeReturn = toBeReturn + line + "\n";
         debug.print(toBeReturn);
      }
      buf.close();
      return toBeReturn;
   }

   public static void main(String args[]) throws Exception {
      debug.setFunctionName("main():");
      debug.print("Invoked");
      // Init
      InputStream posTagStream = new FileInputStream("../packages/OpenNLP_models/en-pos-maxent.bin");
      POSModel posModel = new POSModel(posTagStream);
      POSTaggerME posTagger = new POSTaggerME(posModel);
      // Parse
      LinkedList<Document> docs = Document.parse(Util.readFile(args[0]), text -> {
         debug.print((String) text);
         String toBeSplit = (String)text;
         String toBeReturn = "";
         String tokens[] = toBeSplit.split("[ \n]+");    
         String[] tags = posTagger.tag(tokens);
         for (int i = 0; i < tokens.length; i++) {
            toBeReturn += tags[i] + "/" + tokens[i] + " ";
         }
         return toBeReturn;
      });
      // Output
      Util.writeFile("../output/data.tagged", Document.stringify(docs));
   }
}
