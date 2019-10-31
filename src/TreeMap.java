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
import java.util.Comparator;
import java.util.Arrays;

public class TreeMap {
    /**
     * The tree map for the dictionary
     */
    public HashMap<String, Integer> dictionary = new HashMap<String, Integer>();

    /**
     * The tree map for the posting
     */
    public HashMap<String, LinkedList<Term>> posting = new HashMap<String, LinkedList<Term>>();

    /**
     * Posting output path
     */
    private static final String POSTING_PATH = "../output/posting.txt";

    /**
     * Dictionary output path
     */
    private static final String DICTIONARY_PATH = "../output/dictionary.txt";

    /**
     * Document ids output path
     */
    private static final String DOC_IDS_PATH = "../output/docids.txt";

    /**
     * Process the frequency of the terms
     * 
     * @param paragraph
     * @param docId
     */
    public void processFrequency(String paragraph, String docId) {
        String temp = paragraph;
        String[] lines = paragraph.split("[\n]|[\r\n]");
        for (int i = 0; i < lines.length; i++) {
            System.out.println("lines[i]: " + lines[i]);
            String[] tokens = lines[i].split("[ ]");
            for (String token : tokens) {
                // filter out irrelevant lines
                if (token.matches("\\([0-9]+\\)") || token.equals(""))
                    continue;
                // update relevant token on TreeMap
                if (this.posting.containsKey(token)) {
                    // Add frequency or add as new list
                    Term term = this.posting.get(token).get(0);
                    if (term != null && term.getDid() == docId) {
                        int curTotal = this.dictionary.get(token);
                        this.dictionary.put(token, ++curTotal);
                        this.posting.get(token).get(0).incrementFrequency();
                    } else {
                        int curTotal = this.dictionary.get(token);
                        this.dictionary.put(token, ++curTotal);
                        this.posting.get(token).add(0, new Term(docId, token));
                    }
                } else {
                    // Add for Dictionary
                    this.dictionary.put(token, 1);
                    // Add for posting
                    LinkedList<Term> terms = new LinkedList<Term>();
                    terms.add(new Term(docId, token));
                    this.posting.put(token, terms);
                }
            }
        }
    }

    /**
     * Print the process output
     */
    public void outputProcess(LinkedList<Document> docs) {
        try {
            // Ordered and sort the values
            int count = 0;
            String toBePrint = "";
            String[] orderedKeys = new String[this.posting.size()];
            for (Map.Entry<String, LinkedList<Term>> entry : this.posting.entrySet()) {
                orderedKeys[count] = entry.getKey();
                count++;
            }
            Arrays.sort(orderedKeys, new Comparator<String>() {
                @Override
                public int compare(String c1, String c2) {
                    return c1.compareTo(c2);
                }
            });

            String totalEntriesStr = "Total entries: ";
            int totalEntries = 0;
            for (int x = 0; x < orderedKeys.length; x++)
                for (Term term : this.posting.get(orderedKeys[x])) {
                    toBePrint += term.toString() + "\n";
                    totalEntries++;
                }
            totalEntriesStr += Integer.toString(totalEntries) + "\n";
            toBePrint = totalEntriesStr + toBePrint;
            Util.writeFile(POSTING_PATH, toBePrint);

            // Output the dictionary file
            toBePrint = "Total stems: " + this.dictionary.size() + "\n";
            for (int x = 0; x < orderedKeys.length; x++)
                toBePrint += orderedKeys[x] + " " + this.posting.get(orderedKeys[x]).size() + "\n";
            Util.writeFile(DICTIONARY_PATH, toBePrint);

            // Output the doc ids
            toBePrint = "Total docs: " + docs.size() + "\n";
            for (Document doc : docs)
                toBePrint += doc.getID() + " " + Integer.toString(doc.getStartPos()) + " " + doc.getTitle() + "\n";
            Util.writeFile(DOC_IDS_PATH, toBePrint);
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
        LinkedList<Document> docs = Document.parse(Util.readFileWithNewLine(path),
                text -> ((String) (text)).split("[ \t]+"), null, null);
        LinkedList<Document> resDocs = new LinkedList<Document>();
        for (Document doc : docs) {
            this.processFrequency(doc.getTitle(), doc.getID());
            this.processFrequency(doc.getText(), doc.getID());
        }
        outputProcess(docs);
    }

    /**
     * @return int of the total entries
     */
    public static int totalEntries() {
        return 0;
    }

    public static void main(String args[]) {
        try {
            new TreeMap().postingProcess();
        } catch (Exception err) {
        }
    }
}
