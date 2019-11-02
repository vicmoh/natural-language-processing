import lib.*;
import opennlp.tools.stemmer.PorterStemmer;
import java.lang.Math;
import java.util.HashMap;
import java.util.TreeMap;

import javax.management.Query;

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
import java.math.BigDecimal;
import java.text.DecimalFormat;

class DictionaryData {
    public String word;
    public int df = 0;
    public Term term;

    /**
     * Class for the data on dictionary in each line
     * 
     * @param word
     * @param df
     */
    DictionaryData(String word, int df) {
        this.word = word;
        this.df = df;
        this.term = new Term(word);
        this.term.setDf(df);
    }

    @Override
    public String toString() {
        return "DictionaryData -> word: " + this.word + ", df: " + this.df + ", term: " + this.term;
    }
}

class PostingData {
    public int did = -1;
    public int tf = 0;

    /**
     * Class for data on posting in each line
     * 
     * @param did
     * @param tf
     */
    PostingData(int did, int tf) {
        this.did = did;
        this.tf = tf;
    }
}

class DocIdsData {
    public String docId;
    public int lineNumber;
    public String title;

    /**
     * The doc id data for each line
     * 
     * @param docId
     * @param lineNumber
     * @param title
     */
    DocIdsData(String docId, int lineNumber, String title) {
        this.docId = docId;
        this.lineNumber = lineNumber;
        this.title = title;
    }

    @Override
    public String toString() {
        return "DocIdsData -> docId: " + this.docId + ", lineNumber: " + this.lineNumber + ", title: " + this.title;
    }
}

class QueryResult {
    public String docId;
    public double rank;

    /**
     * Query result
     * 
     * @param docId
     * @param rank
     */
    QueryResult(String docId, double rank) {
        this.docId = docId;
        this.rank = rank;
    }
}

public class OnlineProcess {
    // File path
    private static final String POSTING_PATH = "../output/posting.txt";
    private static final String DICTIONARY_PATH = "../output/dictionary.txt";
    private static final String DOC_IDS_PATH = "../output/docids.txt";

    /// This variables are used as file data of each lines.
    public ArrayList<DocIdsData> docIdsList = new ArrayList<DocIdsData>();
    public ArrayList<PostingData> postingList = new ArrayList<PostingData>();
    public ArrayList<DictionaryData> dictionaryList = new ArrayList<DictionaryData>();
    public TreeMap<String, DictionaryData> dictionaryMap = new TreeMap<String, DictionaryData>();
    public TreeMap<String, Term> weightMap = new TreeMap<String, Term>();
    public TreeMap<String, Integer> queriesFrequencies = new TreeMap<String, Integer>();
    public ArrayList<QueryResult> queryResults = new ArrayList<QueryResult>();

    /**
     * Used for the weight matrix for the vector model
     */
    public double[][] weightMatrix = new double[0][0];

    /**
     * List of offsets in the dictionary for the online process of the inverted
     * files
     */
    public ArrayList<Term> dictionaryOffsets = new ArrayList<Term>();

    /**
     * Print the content of the inverted dictionary from the dictionary Stem and
     * dictionaryOffset
     */
    public void printInvertedDictionary() {
        for (Term each : dictionaryOffsets)
            System.out.println("Word: " + each.getToken() + ", Offset: " + each.getOffset());
    }

    /**
     * Print the docIdsList
     */
    public void printDocIdsList() {
        for (DocIdsData each : docIdsList)
            System.out.println(each.toString());
    }

    /**
     * Get the data for the posting, dictionary, and doc ids file data.
     */
    public void loadInvertedFile() {
        String[] postingFileData = new String[0];
        String[] dictionaryFileData = new String[0];
        String[] docIdsFileData = new String[0];
        final String newLineRegex = "[\r\n]+|[\n]+";

        // For the dictionary file -------------------------------------
        String dictionaryData = Util.readFileWithNewLine(DICTIONARY_PATH);
        dictionaryFileData = dictionaryData.split(newLineRegex);
        Boolean isFirst = true;
        int count = 0;
        int offsetCounter = 0;
        for (String line : dictionaryFileData) {
            /// Init the array's
            if (isFirst) {
                isFirst = false;
                continue;
            }
            // Add to the lists
            String[] data = line.split("[ ]");
            int df = Integer.parseInt(data[1].trim());
            DictionaryData dicData = new DictionaryData(data[0], df);
            dictionaryList.add(dicData);
            dictionaryMap.put(dicData.word, dicData);
            // Create offset for dictionary
            dictionaryOffsets.add(new Term(data[0], offsetCounter));
            offsetCounter += df;
            count++;
        }

        // For the posting file --------------------------------------
        isFirst = true;
        String postingData = Util.readFileWithNewLine(POSTING_PATH);
        postingFileData = postingData.split(newLineRegex);
        for (String line : postingFileData) {
            if (isFirst) {
                isFirst = false;
                continue;
            }
            String[] data = line.split("[ ]");
            int tf = Integer.parseInt(data[1].trim());
            int did = Integer.parseInt(data[0]);
            postingList.add(new PostingData(did, tf));
        }

        // For the docids file --------------------------------------
        String docIdsData = Util.readFileWithNewLine(DOC_IDS_PATH);
        docIdsFileData = docIdsData.split(newLineRegex);
        docIdsFileData = docIdsData.split(newLineRegex);
        isFirst = true;
        for (String line : docIdsFileData) {
            if (isFirst) {
                isFirst = false;
                continue;
            }
            // Init
            System.out.println(line);
            String[] toks = line.split("[ ]");
            String docId = toks[0];
            int startLine = Integer.parseInt(toks[1]);
            String title = "";
            // Get the title
            boolean isFirstTitleWord = true;
            for (int x = 2; x < toks.length; x++) {
                if (isFirstTitleWord) {
                    isFirstTitleWord = false;
                    title = toks[x];
                    continue;
                }
                title += " " + toks[x];
            }
            // Added tto the document
            docIdsList.add(new DocIdsData(docId, startLine, title));
        }
    }

    /**
     * Function for calculating the weight.
     * 
     * @param tf
     * @param df
     * @param totalDoc
     * @return weight
     */
    public double calcWeight(int tf, int df, int totalDoc) {
        if (df == 0)
            return 0;
        return tf * Math.log(totalDoc / df);
    }

    /**
     * Get the word offset
     * 
     * @param word
     * @return offset of the word
     */
    public int getWordOffset(String word) {
        for (int x = 0; x < dictionaryList.size(); x++) {
            if (dictionaryList.get(x).word.trim().equalsIgnoreCase(word.trim()))
                return dictionaryOffsets.get(x).getOffset();
        }
        return 0;
    }

    /**
     * calculate offset for the df.
     * 
     * @param word
     * @return the df
     */
    public int calcOffset(String word) {
        int df = 0;
        int wordOffset = 0;
        for (int x = 0; x < dictionaryList.size(); x++) {
            if (dictionaryList.get(x).word.trim().equalsIgnoreCase(word.trim())) {
                wordOffset = dictionaryOffsets.get(x).getOffset();
                if (postingList.size() - 1 == wordOffset)
                    df = postingList.size() - 1 - wordOffset;
                else
                    df = dictionaryOffsets.get(x + 1).getOffset() - wordOffset;
                return df;
            }
        }
        return df;
    }

    /**
     * Print weight matrix in a table format
     */
    public void printWeightMatrix() {
        String offsetTab = "";
        String terms = "\t\t";
        String freq = "";
        for (DictionaryData dicData : dictionaryList)
            terms += String.format("%13s", dicData.word) + "\t" + offsetTab;
        terms += "\n";
        for (int y = 0; y < weightMatrix.length; y++) {
            freq += "Doc[" + y + "]" + "\t" + offsetTab;
            for (int x = 0; x < weightMatrix[y].length; x++)
                freq += new DecimalFormat("        0.000").format(weightMatrix[y][x]) + "\t" + offsetTab;
            freq += "\n";
        }
        System.out.println(terms + freq);
    }

    /**
     * Calculate the matrix weights
     */
    public void calcMatrixWeight() {
        final boolean SHOW_PRINT = false;
        if (SHOW_PRINT)
            System.out.println("--------------------------- calcMatrixWeight() ---------------------------");
        weightMatrix = new double[docIdsList.size()][dictionaryList.size()];
        for (int y = 0; y < docIdsList.size(); y++) {
            if (SHOW_PRINT)
                System.out.println(
                        "---------------------------------- Doc#: " + y + " ----------------------------------");
            // Go through the term
            for (int x = 0; x < dictionaryList.size(); x++) {
                String word = dictionaryList.get(x).word;
                int wordOffset = getWordOffset(word);
                int df = calcOffset(word);
                int tf = 0;
                // Get the correct doc number
                // Because the tf is based on the doc
                for (int i = 0; i < wordOffset; i++)
                    if (wordOffset + i < postingList.size())
                        if (postingList.get(wordOffset + i).did == y)
                            tf = postingList.get(wordOffset + i).tf;

                // Set the weight
                double weight = calcWeight(tf, df, docIdsList.size());
                weightMatrix[y][x] = weight;
                if (SHOW_PRINT)
                    System.out.println("word: " + word + ", weight: " + weight);
            }
        }
    }

    /**
     * Stemmed the string.
     * 
     * @param val
     * @return stemmed words.
     */
    public static String stem(String val) {
        return Preprocessed.stemmed(Preprocessed.removeNumPuncAndStopWords(Preprocessed.setTextToLowerCase(val)));
    }

    /**
     * Calculate the q value which is the query Idf of the frequency term.
     */
    public double calcQueryIdf(String query) {
        return Math.log((double) (this.docIdsList.size() / calcOffset(query)));
    }

    /**
     * Load the query and create frequency table.
     * 
     * @param queryString
     * @return Stemmed version of the query
     */
    public void runQueryString(String queryString) {
        final boolean SHOW_PRINT = false;
        PorterStemmer stemmer = new PorterStemmer();
        String[] toks = stem(queryString).split("[ ]");
        for (int x = 0; x < toks.length; x++)
            if (this.queriesFrequencies.containsKey(toks[x])) {
                int freq = this.queriesFrequencies.get(toks[x]);
                this.queriesFrequencies.put(toks[x], ++freq);
            } else
                this.queriesFrequencies.put(toks[x], 1);
        // Calculate similarities
        calcCosSim(toks);
        // Print the token
        if (SHOW_PRINT) {
            System.out.print("Stemmed query: ");
            for (String each : toks)
                System.out.print(each + " ");
            System.out.println("");
        }
    }

    /**
     * Calculate cosine similarities
     * 
     * @param queries
     */
    public void calcCosSim(String[] queries) {
        // Init
        double[][] matrix = weightMatrix;
        double[] sims = new double[matrix.length];
        for (int i = 0; i < sims.length; i++)
            sims[i] = 0;
        // Go to each query
        for (String query : queries)
            // Calculate the similarity
            for (int y = 0; y < matrix.length; y++) {
                // Record the number of unique words
                for (int x = 0; x < matrix[y].length; x++) {
                    sims[y] += matrix[y][x] * this.calcQueryIdf(query);
                }
                this.queryResults.add(new QueryResult(this.docIdsList.get(y).docId, sims[y]));
            }
    }

    /**
     * Function to run the online process
     */
    public void run() {
        this.loadInvertedFile();
        this.calcMatrixWeight();

        // Run command
        String inputString = "";
        while (!inputString.equalsIgnoreCase("q")) {
            System.out.print("Enter a query: ");
            Scanner scanner = new Scanner(System.in);
            inputString = scanner.nextLine();
            this.runQueryString(inputString);
        }
    }

    /**
     * Main function to run the program .
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        new OnlineProcess().run();
    }
}