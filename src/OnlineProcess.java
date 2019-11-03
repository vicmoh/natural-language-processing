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
import java.util.Collections;

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

    @Override
    public String toString() {
        return "DocId: " + this.docId + ", Cosine Similarity: " + this.rank;
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
     * Used for the weight matrix for the vector model First array is the document
     * and the second the the term
     */
    public double[][] weightMatrix = new double[0][0];

    /**
     * The first is the document array and the second is the query term frequency
     */
    public Integer[][] queryMatrixFrequency = new Integer[0][0];

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
        isFirst = true;
        for (String line : docIdsFileData) {
            if (isFirst) {
                isFirst = false;
                continue;
            }
            // Init
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
     * Get the document term frequency offset of a specific doc did.
     * 
     * @param docNum     the doc number id
     * @param wordOffset you are looking for
     * @return the frequency
     */
    public int getTermFreqFromOffset(int docNum, int wordOffset) {
        int tf = 0;
        for (int i = 0; i < wordOffset; i++)
            if (wordOffset + i < this.postingList.size())
                if (this.postingList.get(wordOffset + i).did == docNum)
                    tf = this.postingList.get(wordOffset + i).tf;
        return tf;
    }

    /**
     * Calculate the matrix weights
     */
    @Deprecated
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
                int tf = getTermFreqFromOffset(y, wordOffset);

                // Set the weight
                double weight = calcWeight(tf, df, docIdsList.size());
                weightMatrix[y][x] = weight;
                if (SHOW_PRINT)
                    System.out.println("word: " + word + ", weight: " + weight);
            }
        }
    }

    /**
     * Calculate the query query frequency relative to the document
     * 
     * @param queries
     */
    public void calcMatrixQueryFrequency(String[] queries) {
        final boolean SHOW_PRINT = false;
        if (SHOW_PRINT)
            System.out.println("--------------------------- calcMatrixWeight() ---------------------------");

        // Init matrix
        weightMatrix = new double[docIdsList.size()][dictionaryList.size()];
        this.queryMatrixFrequency = new Integer[docIdsList.size()][dictionaryList.size()];
        for (int y = 0; y < this.queryMatrixFrequency.length; y++)
            for (int x = 0; x < this.queryMatrixFrequency[y].length; x++)
                this.queryMatrixFrequency[y][x] = 0;

        // Run the matrix
        for (int y = 0; y < docIdsList.size(); y++) {
            if (SHOW_PRINT)
                System.out.println(
                        "---------------------------------- Doc#: " + y + " ----------------------------------");
            // Go through the term
            for (int x = 0; x < dictionaryList.size(); x++) {
                String word = dictionaryList.get(x).word;
                int wordOffset = getWordOffset(word);
                int df = calcOffset(word);
                int tf = getTermFreqFromOffset(y, wordOffset);

                // Set the weight
                double weight = calcWeight(tf, df, docIdsList.size());
                this.weightMatrix[y][x] = weight;

                // Count the frequency using the
                for (Map.Entry<String, Integer> entry : this.queriesFrequencies.entrySet())
                    if (entry.getKey().trim().equalsIgnoreCase(word.trim())) {
                        int freq = this.queryMatrixFrequency[y][x];
                        this.queryMatrixFrequency[y][x] = this.getTermFreqFromOffset(y, this.getWordOffset(word));
                    }
                if (SHOW_PRINT)
                    System.out.println("word: " + word + ", weight: " + weight);
            }
        }
    }

    /**
     * s Calculate the q value which is the query Idf of the frequency term.
     */
    public double calcQueryIdf(int frequency) {
        int df = frequency;
        if (df == 0)
            return 0;
        System.out.println("size: " + this.queryMatrixFrequency.length + " / " + df);
        return Math.log((double) (this.queryMatrixFrequency.length / df));
    }

    /**
     * Load the query and create frequency table.
     * 
     * @param queryString
     * @return Stemmed version of the query
     */
    public void runQueryString(String queryString) {
        final boolean SHOW_PRINT = true;
        PorterStemmer stemmer = new PorterStemmer();
        String[] toks = queryString.split("[ ]");
        String[] queryTerms = new String[toks.length];
        for (int x = 0; x < toks.length; x++)
            queryTerms[x] = stemmer.stem(toks[x]);

        // Get the frequency
        this.queriesFrequencies.clear();
        for (int x = 0; x < queryTerms.length; x++) {
            System.out.println("tok: " + queryTerms[x]);
            if (this.queriesFrequencies.containsKey(queryTerms[x])) {
                int freq = this.queriesFrequencies.get(queryTerms[x]);
                this.queriesFrequencies.put(queryTerms[x], ++freq);
            } else
                this.queriesFrequencies.put(queryTerms[x], 1);
        }

        // Calculate similarities
        calcMatrixQueryFrequency(queryTerms);
        calcCosSim(queryTerms);

        // Print the token
        if (SHOW_PRINT) {
            System.out.print("Stemmed query: ");
            for (String each : queryTerms)
                System.out.print(each + " ");
            System.out.println("");
        }
        printQueryResult();
    }

    /**
     * Calculate cosine similarities
     * 
     * @param queries
     */
    public void calcCosSim(String[] queries) {
        // Init
        this.queryResults.clear();
        double[][] matrix = weightMatrix;
        double[] sims = new double[matrix.length];
        TreeMap<String, QueryResult> uniqueRes = new TreeMap<String, QueryResult>();
        for (int i = 0; i < sims.length; i++)
            sims[i] = 0;
        // Go to each query
        for (String query : queries)
            // Calculate the similarity
            for (int y = 0; y < matrix.length; y++) {
                // Record the number of unique words
                for (int x = 0; x < matrix[y].length; x++) {
                    int freq = this.queryMatrixFrequency[y][x];
                    sims[y] += matrix[y][x] * freq;
                    System.out.println("weight: " + matrix[y][x] + ", freq: " + freq);
                }
                uniqueRes.put(this.docIdsList.get(y).docId, new QueryResult(this.docIdsList.get(y).docId, sims[y]));
            }
        uniqueRes.forEach((docId, res) -> this.queryResults.add(res));
        // Short query result
        Collections.sort(this.queryResults, new Comparator<QueryResult>() {
            @Override
            public int compare(QueryResult a, QueryResult b) {
                if (a.rank > b.rank)
                    return -1;
                else
                    return 1;
            }
        });
    }

    /**
     * Print the query result
     */
    public void printQueryResult() {
        System.out.println("\n----------------- Query Result -----------------");
        for (QueryResult res : this.queryResults)
            System.out.println(res.toString());
    }

    /**
     * Function to run the online process
     */
    public void run() {
        this.loadInvertedFile();

        // Run command
        String inputString = "";
        while (!inputString.equalsIgnoreCase("q")) {
            System.out.print("\nEnter a query: ");
            Scanner scanner = new Scanner(System.in);
            inputString = scanner.nextLine();
            if (inputString.equalsIgnoreCase("q"))
                break;
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
        Preprocessed.run("../output/data.tokenized");
        new OnlineProcess().run();
    }
}