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
    public String title;

    /**
     * Query result
     * 
     * @param docId
     * @param rank
     */
    QueryResult(String docId, double rank, String title) {
        this.docId = docId;
        this.rank = rank;
        this.title = title;
    }

    @Override
    public String toString() {
        String docString = "Document ID: " + this.docId + "\n";
        String titleString = "Title: " + this.title + "\n";
        String rankString = "Similarity Value: " + Double.toString(this.rank) + "\n";
        String divider = "----------------------------------------------------------";
        return docString + titleString + rankString + divider;
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
    public double[][] queryMatrixFrequency = new double[0][0];

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
        double res = (double) (tf) * Math.log((double) (totalDoc) / (double) (df));
        if (Double.isInfinite(res))
            return 0;
        else
            return Math.abs(res);
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
        final boolean SHOW_PRINT = false;
        int df = 0;
        int wordOffset = 0;
        for (int x = 0; x < dictionaryList.size(); x++) {
            if (dictionaryList.get(x).word.trim().equalsIgnoreCase(word.trim())) {
                wordOffset = dictionaryOffsets.get(x).getOffset();
                if (SHOW_PRINT)
                    System.out.println("word: " + word + ", offset: " + wordOffset);
                if (postingList.size() - 1 == wordOffset)
                    df = postingList.size() - 1 - wordOffset;
                else {
                    try {
                        df = dictionaryOffsets.get(x + 1).getOffset() - wordOffset;
                    } catch (Exception err) {
                        df = dictionaryOffsets.get(x).getOffset() - wordOffset;
                    }
                }
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
    public int getTermFreqFromOffset(int docNum, int wordOffset, String word) {
        int tf = 0;
        int df = dictionaryList.get(this.getIndexOfTerm(word)).df;
        for (int i = wordOffset; i < wordOffset + df; i++)
            if (i < this.postingList.size())
                if (this.postingList.get(i).did == docNum) {
                    tf = this.postingList.get(i).tf;
                    break;
                }
        if (Double.isInfinite(tf))
            return 0;
        else
            return Math.abs(tf);
    }

    /**
     * Calculate the matrix weights
     */
    @Deprecated
    public void calcMatrixWeight() {
        final boolean SHOW_PRINT = false;
        if (SHOW_PRINT)
            System.out.println("--------------------------- calcMatrixWeight() ---------------------------");
        this.weightMatrix = new double[docIdsList.size()][dictionaryList.size()];
        for (int y = 0; y < docIdsList.size(); y++) {
            if (SHOW_PRINT)
                System.out.println(
                        "---------------------------------- Doc#: " + y + " ----------------------------------");
            // Go through the term
            for (int x = 0; x < dictionaryList.size(); x++) {
                String word = dictionaryList.get(x).word;
                int wordOffset = getWordOffset(word);
                int df = calcOffset(word);
                int tf = getTermFreqFromOffset(y, wordOffset, word);

                // Set the weight
                double weight = calcWeight(tf, df, docIdsList.size());
                this.weightMatrix[y][x] = weight;
                if (SHOW_PRINT)
                    System.out.println("word: " + word + ", weight: " + weight);
            }
        }
    }

    /**
     * Get the index of terms.
     * 
     * @param word to get the index of.
     * @return of the index, if not found return -1.
     */
    public int getIndexOfTerm(String word) {
        for (int i = 0; i < this.dictionaryList.size(); i++)
            if (this.dictionaryList.get(i).word.trim().equalsIgnoreCase(word.trim()))
                return i;
        return -1;
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
        this.weightMatrix = new double[docIdsList.size()][dictionaryList.size()];
        this.queryMatrixFrequency = new double[docIdsList.size()][dictionaryList.size()];
        for (int y = 0; y < this.queryMatrixFrequency.length; y++)
            for (int x = 0; x < this.queryMatrixFrequency[y].length; x++) {
                this.queryMatrixFrequency[y][x] = 0;
                this.weightMatrix[y][x] = 0;
            }

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
                int tf = getTermFreqFromOffset(y, wordOffset, word);

                // Set the weight
                double weight = calcWeight(tf, df, docIdsList.size());
                if (Double.isInfinite(weight))
                    this.weightMatrix[y][x] = 0;
                else
                    this.weightMatrix[y][x] = weight;

                // Count the frequency using the
                for (Map.Entry<String, Integer> entry : this.queriesFrequencies.entrySet())
                    if (entry.getKey().trim().equalsIgnoreCase(word.trim()))
                        this.queryMatrixFrequency[y][x] = tf;
                if (SHOW_PRINT)
                    System.out.println("word: " + word + ", weight: " + weight);
            }
        }
    }

    /**
     * s Calculate the q value which is the query Idf of the frequency term.
     */
    public double calcQueryIdf(double frequency) {
        final boolean SHOW_PRINT = false;
        double df = frequency;
        if (df == 0)
            return 0;
        if (SHOW_PRINT)
            System.out.println("size: " + this.queriesFrequencies.size() + " / " + df);
        // return frequency;
        // return this.queriesFrequencies.size() / df;
        // return df / this.queriesFrequencies.size();
        double res = (1 * Math.log((double) (this.queriesFrequencies.size() / df)));
        if (Double.isInfinite(res))
            return 0;
        else
            return Math.abs(res);
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
        String[] toks = Preprocessed.removeNumPuncAndStopWords(Preprocessed.setTextToLowerCase(queryString))
                .split("[ \n]+|[ \r\n]+");
        String[] queryTerms = new String[toks.length];
        for (int x = 0; x < toks.length; x++)
            queryTerms[x] = stemmer.stem(toks[x].trim());

        // Get the frequency
        this.queriesFrequencies.clear();
        for (int x = 0; x < queryTerms.length; x++) {
            if (SHOW_PRINT)
                System.out.println("tok: " + queryTerms[x]);
            if (this.queriesFrequencies.containsKey(queryTerms[x])) {
                int freq = this.queriesFrequencies.get(queryTerms[x]);
                this.queriesFrequencies.put(queryTerms[x], ++freq);
            } else
                this.queriesFrequencies.put(queryTerms[x], 1);
        }

        // Prevent redundancy
        String[] uniqueQuery = new String[this.queriesFrequencies.size()];
        int count = 0;
        for (Map.Entry<String, Integer> each : this.queriesFrequencies.entrySet())
            uniqueQuery[count++] = each.getKey();

        // Calculate similarities
        calcMatrixQueryFrequency(uniqueQuery);
        calcSimilarity(uniqueQuery);

        // Print the token
        if (SHOW_PRINT) {
            System.out.print("Stemmed query: ");
            for (String each : uniqueQuery)
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
    public void calcSimilarity(String[] queries) {
        final boolean SHOW_PRINT = false;
        // Init
        this.queryResults.clear();
        double[] sims = new double[this.weightMatrix.length];
        TreeMap<String, QueryResult> uniqueRes = new TreeMap<String, QueryResult>();
        for (int i = 0; i < sims.length; i++)
            sims[i] = 0;
        // Calculate the similarity
        for (int y = 0; y < this.weightMatrix.length; y++) {
            // Record the number of unique words
            if (SHOW_PRINT)
                System.out.println("Doc#: " + y + " ----------");
            for (int x = 0; x < this.weightMatrix[y].length; x++) {
                double freq = this.queryMatrixFrequency[y][x];
                sims[y] += this.weightMatrix[y][x] * this.calcQueryIdf(freq);
                if (SHOW_PRINT)
                    System.out.println("weight: " + this.weightMatrix[y][x] + ", freq: " + freq + ", sim: " + sims[y]
                            + ", term: " + this.dictionaryList.get(x).word);
            }
            uniqueRes.put(this.docIdsList.get(y).docId,
                    new QueryResult(this.docIdsList.get(y).docId, sims[y], this.docIdsList.get(y).title));
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
        System.out.println("---------------------- Query Result ----------------------");
        int count = 0;
        for (QueryResult res : this.queryResults) {
            System.out.println(res.toString());
            count++;
            if (count >= 10)
                break;
        }
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
            System.out.println("");
            if (inputString.equalsIgnoreCase("q") || inputString.equalsIgnoreCase("quit"))
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
        if (args == null || args.length == 0)
            throw new Exception("No files was passed on the argument.");
        Preprocessed.run(args[0]);
        new OnlineProcess().run();
    }
}