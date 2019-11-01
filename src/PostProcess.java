import lib.*;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputValidation;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Arrays;

public class PostProcess {
    /// This variables are used as temporary file data of each lines.
    public static String[] postingFileData = new String[0];
    public static String[] dictionaryFileData = new String[0];
    public static String[] docIdsFileData = new String[0];

    /// Offset data
    public static Integer[] postingOffset = new Integer[0];
    public static Integer[] dictionaryOffset = new Integer[0];
    public static String[] postingKey = new String[0];
    public static String[] dictionaryKey = new String[0];

    /**
     * The tree map for the dictionary
     */
    public TreeMap<String, Integer> dictionary = new TreeMap<String, Integer>();

    /**
     * The tree map for the posting
     */
    public TreeMap<String, LinkedList<Term>> posting = new TreeMap<String, LinkedList<Term>>();

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
     * Temp variable for output string
     */
    private String outputString = "";

    /**
     * Counter for the total entries
     */
    private int totalEntries = 0;

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
        this.outputString = "";
        try {
            // Output for the entries
            String totalEntriesStr = "Total entries: ";
            this.totalEntries = 0;
            this.posting.forEach((key, terms) -> {
                for (Term term : terms) {
                    this.outputString += term.toString() + "\n";
                    this.totalEntries++;
                }
            });
            totalEntriesStr += Integer.toString(this.totalEntries) + "\n";
            this.outputString = totalEntriesStr + this.outputString;
            Util.writeFile(POSTING_PATH, this.outputString);

            // Output the dictionary file
            this.outputString = "Total stems: " + this.dictionary.size() + "\n";
            this.dictionary.forEach((key, array) -> {
                this.outputString += key + " " + this.posting.get(key).size() + "\n";
            });
            Util.writeFile(DICTIONARY_PATH, this.outputString);

            // Output the doc ids
            this.outputString = "Total docs: " + docs.size() + "\n";
            for (Document doc : docs)
                this.outputString += doc.getID() + " " + Integer.toString(doc.getStartPos()) + " " + doc.getTitle()
                        + "\n";
            Util.writeFile(DOC_IDS_PATH, this.outputString);
        } catch (Exception err) {
            System.out.println("Exception: Could not write to a file: " + err.toString());
        }

    }

    /**
     * Get the data for the posting, dictionary, and doc ids file data.
     */
    public void loadingInputFile() {
        final String newLineRegex = "[\r\n]+|[\n]+";
        try {
            // For the posting file
            String postingData = Util.readFileWithNewLine(POSTING_PATH);
            PostProcess.postingFileData = postingData.split(newLineRegex);
            // For the dictionary file
            String dictionaryData = Util.readFileWithNewLine(DICTIONARY_PATH);
            PostProcess.dictionaryFileData = dictionaryData.split(newLineRegex);
            // For the docids file
            String docIdsData = Util.readFileWithNewLine(DOC_IDS_PATH);
            PostProcess.docIdsFileData = docIdsData.split(newLineRegex);
        } catch (Exception err) {
            System.out.println("Exception: Could not read file: " + err.toString());
        }
    }

    /**
     * Function for processing the posting file.
     * 
     * @throws Exception
     */
    public void run() throws Exception {
        final String path = "../output/data.stemmed";
        LinkedList<Document> docs = Document.parse(Util.readFileWithNewLine(path),
                text -> ((String) (text)).split("[ \t]+"), null, null);
        LinkedList<Document> resDocs = new LinkedList<Document>();
        for (Document doc : docs) {
            this.processFrequency(doc.getTitle(), doc.getID());
            this.processFrequency(doc.getText(), doc.getID());
        }
        this.outputProcess(docs);
    }

    /**
     * Main function to run the program
     * 
     * @param args
     */
    public static void main(String args[]) {
        try {
            new PostProcess().run();
        } catch (Exception err) {
        }
    }
}
