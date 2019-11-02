import lib.*;
import java.lang.Math;
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

class DictionaryData {
    public String word;
    public int df;

    /**
     * Class for the data on dictionary in each line
     * 
     * @param word
     * @param df
     */
    DictionaryData(String word, int df) {
        this.word = word;
        this.df = df;
    }
}

class PostingData {
    public String did;
    public int tf;

    /**
     * Class for data on posting in each line
     * 
     * @param did
     * @param tf
     */
    PostingData(String did, int tf) {
        this.did = did;
        this.tg = tf;
    }
}

public class PostProcess {
    /// This variables are used as temporary file data of each lines.
    public static ArrayList<PostingData> postingList = new ArrayList<PostingData>();
    public static ArrayList<DictionaryData> dictionaryList = new ArrayList<DictionaryData>();
    public static TreeMap<String, Term> weightMap = new TreeMap<String, Term>();
    /// This variables are used as temporary file data of each lines.
    public static String[] postingFileData = new String[0];
    public static String[] dictionaryFileData = new String[0];
    public static String[] docIdsFileData = new String[0];

    /**
     * List of offsets in the dictionary for the online process of the inverted
     * files
     */
    public static ArrayList<Term> dictionaryOffsets = new ArrayList<Term>();

    /**
     * The tree map for the dictionary. Key string is the doc and Integer is doc
     * frequency
     */
    public TreeMap<String, Integer> dictionary = new TreeMap<String, Integer>();

    /**
     * The tree map for the posting. Key string is the term token.
     * 
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
    public void processFrequency(String paragraph, String docId, int docNum) {
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
                    if (term != null && term.getDocId() == docId) {
                        int curTotal = this.dictionary.get(token);
                        this.dictionary.put(token, ++curTotal);
                        this.posting.get(token).get(0).incrementFrequency();
                    } else {
                        int curTotal = this.dictionary.get(token);
                        this.dictionary.put(token, ++curTotal);
                        Term termToBeAdded = new Term(docId, token);
                        termToBeAdded.setDid(docNum);
                        this.posting.get(token).add(0, termToBeAdded);
                    }
                } else {
                    // Add for Dictionary
                    this.dictionary.put(token, 1);
                    // Add for posting
                    LinkedList<Term> terms = new LinkedList<Term>();
                    Term termToBeAdded = new Term(docId, token);
                    termToBeAdded.setDid(docNum);
                    terms.add(termToBeAdded);
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
            String totalEntriesStr = "";
            this.totalEntries = 0;
            this.posting.forEach((key, terms) -> {
                for (Term term : terms) {
                    this.outputString += term.getDid() + " " + term.toString() + "\n";
                    this.totalEntries++;
                }
            });
            totalEntriesStr += Integer.toString(this.totalEntries) + "\n";
            this.outputString = totalEntriesStr + this.outputString;
            Util.writeFile(POSTING_PATH, this.outputString);

            // Output the dictionary file
            this.outputString = "" + this.dictionary.size() + "\n";
            this.dictionary.forEach((key, array) -> {
                this.outputString += key + " " + this.posting.get(key).size() + "\n";
            });
            Util.writeFile(DICTIONARY_PATH, this.outputString);

            // Output the doc ids
            this.outputString = "" + docs.size() + "\n";
            for (Document doc : docs)
                this.outputString += doc.getID() + " " + Integer.toString(doc.getStartPos()) + " " + doc.getTitle()
                        + "\n";
            Util.writeFile(DOC_IDS_PATH, this.outputString);
        } catch (Exception err) {
            System.out.println("Exception: Could not write to a file: " + err.toString());
        }
    }

    /**
     * Print the content of the inverted dictionary from the
     * PostProcess.dictionaryStem and PostProcess.dictionaryOffset
     */
    public void printInvertedDictionary() {
        for (Term each : PostProcess.dictionaryOffsets)
            System.out.println("Word: " + each.getToken() + ", Offset: " + each.getOffset());
    }

    /**
     * Get the data for the posting, dictionary, and doc ids file data.
     */
    public void loadInvertedFile() {
        final String newLineRegex = "[\r\n]+|[\n]+";
        try {
            // For the dictionary file -------------------------------------
            String dictionaryData = Util.readFileWithNewLine(DICTIONARY_PATH);
            PostProcess.dictionaryFileData = dictionaryData.split(newLineRegex);
            Boolean isFirst = true;
            int count = 0;
            int offsetCounter = 0;
            for (String line : PostProcess.dictionaryFileData) {
                /// Init the array's
                if (isFirst) {
                    isFirst = false;
                    continue;
                }
                // Add to the list
                String[] data = line.split("[ ]");
                int df = Integer.parseInt(data[1].trim());
                PostProcess.dictionaryList.add(new DictionaryData(data[0], df));

                // Create offset for dictionary
                PostProcess.dictionaryOffsets.add(new Term(data[0], offsetCounter));
                offsetCounter += df;
                count++;
            }
            printInvertedDictionary();

            // For the posting file --------------------------------------
            isFirst = true;
            String postingData = Util.readFileWithNewLine(POSTING_PATH);
            PostProcess.postingFileData = postingData.split(newLineRegex);
            for (String line : PostProcess.postingFileData) {
                if (isFirst) {
                    isFirst = false;
                    continue;
                }
                String[] data = line.split("[ ]");
                int tf = Integer.parseInt(data[1].trim());
                PostProcess.postingList.add(new PostingData(data[0], tf));
            }

            // For the docids file
            String docIdsData = Util.readFileWithNewLine(DOC_IDS_PATH);
            PostProcess.docIdsFileData = docIdsData.split(newLineRegex);
        } catch (Exception err) {
            System.out.println("Exception: Could not read file: " + err.toString());
        }
    }

    /**
     * Function for calculating the weight.
     * 
     * @param tf
     * @param df
     * @param totalDoc
     * @return
     */
    public double calculateWeight(int tf, int df, int totalDoc) {
        return tf * Math.log(totalDoc / df);
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
        int docNum = 0;
        for (Document doc : docs) {
            this.processFrequency(doc.getTitle(), doc.getID(), doc.getDocNum());
            this.processFrequency(doc.getText(), doc.getID(), doc.getDocNum());
            docNum++;
        }
        this.outputProcess(docs);
        this.loadInvertedFile();
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
