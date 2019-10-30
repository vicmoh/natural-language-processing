import lib.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.InputStream;

public class TreeMap {
    /**
     * The tree map for the dictionary
     */
    public HashMap<String, LinkedList<Term>> dictionary = new HashMap<String, LinkedList<Term>>();

    /**
     * The tree map for the posting
     */
    public HashMap<String, LinkedList<Term>> posting = new HashMap<String, LinkedList<Term>>();

    /**
     * Temp variable for storing the print statement.
     */
    private String toBePrint = "";

    /**
     * Process the frequency of the terms
     * 
     * @param paragraph
     * @param docNum
     */
    public void processFrequency(String paragraph, int docNum) {
        String temp = paragraph;
        String[] lines = paragraph.split("[\n]|[\r\n]");
        for (int i = 0; i < lines.length; i++) {
            String[] tokens = lines[i].split("[ ]");
            for (String token : tokens) {
                // filter out irrelevant lines
                if (token.matches("\\([0-9]+\\)"))
                    continue;
                // update relevant token on TreeMap
                if (this.posting.containsKey(token)) {
                    // Add frequency or add as new list
                    Term term = this.posting.get(token).get(0);
                    if (term != null && term.getDid() == docNum)
                        this.posting.get(token).get(0).incrementFrequency();
                    else
                        this.posting.get(token).add(0, new Term(docNum, token));
                } else {
                    LinkedList<Term> terms = new LinkedList<Term>();
                    terms.add(new Term(docNum, token));
                    this.posting.put(token, terms);
                }
            }
        }
    }

    /**
     * Print the process output
     */
    public void printProcess() {
        try {
            this.toBePrint = "";
            Set<String> keys = this.posting.keySet();
            Iterator<String> iter = keys.iterator();
            System.out.println("*** Total entries: " + keys.size());
            while (iter.hasNext()) {
                String key = iter.next();
                for (Term term : this.posting.get(key))
                    this.toBePrint += term.toString() + "\n";
            }
            Util.writeFile("../output/data.posting", this.toBePrint);
        } catch (Exception err) {
        }
    }

    /**
     * Function for processing the posting file.
     * 
     * @throws Exception
     */
    public void postingProcess() throws Exception {
        final String path = "../output/data.stemmed";
        LinkedList<Document> docs = Document.parse(Util.readFileWithNewLine(path), null, null, null);
        LinkedList<Document> resDocs = new LinkedList<Document>();
        int count = 0;
        for (Document doc : docs) {
            this.processFrequency(doc.title, count);
            this.processFrequency(doc.text, count);
            count++;
        }
        printProcess();
    }

    public static void main(String args[]) {
        try {
            new TreeMap().postingProcess();
        } catch (Exception err) {
        }
    }
}