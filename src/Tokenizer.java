import lib.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.InputStream;

public class Tokenizer {
    private Lexer scanner = null;

    /**
     * Debugger for printing.
     */
    private static Debugger debug = Debugger.init().setClassName("Tokenizer").showDebugPrint(false);

    /**
     * Main function to run the program.
     * 
     * @param argv
     */
    public static void main(String argv[]) throws Exception {
        if (argv.length < 1)
            throw new Exception("Could not find file path.");

        // Init setup for file name and path
        final String fileName = argv[0];
        final String OUTPUT_PATH = "../output/data.tokenized";

        // Run
        Tokenizer tok = new Tokenizer();
        Util.writeFile(OUTPUT_PATH, tok.run(fileName));
    }

    /**
     * Function for splitting the hyphenated.
     * 
     * @param value string to be split.
     */
    public static String splitHyphenated(String value) {
        debug.setFunctionName("splitHyphenated");
        String toBeReturn = "";
        String[] split = value.split("-");
        for (int x = 0; x < split.length; x++) {
            if (split.length - 1 <= x)
                toBeReturn += split[x];
            else
                toBeReturn += split[x] + " - ";
        }
        debug.print(toBeReturn);
        return toBeReturn;
    }

    /**
     * Reformat for the hyphenated cases.
     * 
     * @param value
     * @return the reformated string
     */
    public static String reformatHyphenatedCase(String value) {
        final String toBeReturn = "";
        String[] split = value.split("-");
        if (split.length >= 3) {
            if (split[1].length() > 2)
                return splitHyphenated(value);
            else
                return value;
        } else
            return value;
    }

    /**
     * The min tokens after processed.
     */
    public int minTokens = 0;
    /**
     * The max tokens after processed
     */
    public int maxTokens = 0;

    /**
     * The average tokens after processed
     */
    public double avgTokens = 0;

    /**
     * The total tokens after parsed.
     */
    public int totalTokens = 0;

    /**
     * To check if first doc is passed when parsing
     */
    public boolean isFirstDocPassed = false;

    /**
     * Default constructor.
     */
    public Tokenizer() {
    }

    /**
     * Construct a scanner from lexer.
     * 
     * @param lexer
     */
    public Tokenizer(Lexer lexer) {
        scanner = lexer;
    }

    /**
     * Get the next token.
     */
    private Token getNextToken() throws java.io.IOException {
        return scanner.yylex();
    }

    /**
     * Run this function to process the stats.
     * 
     * @param fileName
     * @throws Exception
     */
    public void runTokenStats() throws Exception {
        LinkedList<Document> docs = new LinkedList<Document>();
        String tokenized = new Tokenizer().run(Sentencer.FILE_OUTPUT_PATH);
        // Run detector
        try {
            docs = Document.parse(tokenized, text -> {
                String[] tok = ((String) text).split("[ \n\r\t\f]");
                String paragraph = "";
                for (String each : tok) {
                    paragraph += each + "\n";
                    this.totalTokens++;
                }
                // Cases for min and max sentences
                if (tok.length > this.maxTokens)
                    this.maxTokens = tok.length;
                if (tok.length < this.minTokens || isFirstDocPassed == false) {
                    this.minTokens = tok.length;
                    isFirstDocPassed = true;
                }
                // Return paragraph
                return paragraph.trim();
            });
        } catch (Exception err) {
            throw new Exception(err.getMessage());
        }
        // Return
        this.isFirstDocPassed = false;
        this.avgTokens = this.totalTokens / docs.size();
    }

    /**
     * Parse the file.
     * 
     * @param fileName
     * @return the parse file to the result string.
     */
    public String run(String fileName) throws Exception {
        // Try to parse
        String toBeOutput = "";
        InputStream inFile = null;
        try {
            inFile = new FileInputStream(fileName);
            Tokenizer scanner = new Tokenizer(new Lexer(new InputStreamReader(inFile)));
            Token tok = null;
            boolean lastTokType = false;
            boolean isFirstTokPassed = false;
            while ((tok = scanner.getNextToken()) != null) {
                if (isFirstTokPassed == false) {
                    isFirstTokPassed = true;
                    toBeOutput += tok.m_value;
                } else if (tok.m_type == Token.NEWLINE) {
                    lastTokType = true;
                    toBeOutput += tok.m_value;
                } else if (lastTokType == false) {
                    lastTokType = false;
                    if (tok.m_type == Token.HYPHENATED)
                        toBeOutput += " " + Tokenizer.reformatHyphenatedCase(tok.m_value);
                    else
                        toBeOutput += " " + tok.m_value;
                } else {
                    lastTokType = false;
                    if (tok.m_type == Token.HYPHENATED)
                        toBeOutput += Tokenizer.reformatHyphenatedCase(tok.m_value);
                    else
                        toBeOutput += tok.m_value;
                }
            }
        } catch (Exception err) {
            throw new Exception(err.toString());
        }
        // Close the file and end
        if (inFile != null)
            inFile.close();
        return toBeOutput;
    }

    /**
     * Get the string output for the stats.
     * 
     * @return string of the stats.
     */
    public String toStatsString() {
        String min = "Min tokens: " + Integer.toString(this.minTokens) + "\n";
        String max = "Max tokens: " + Integer.toString(this.maxTokens) + "\n";
        String avg = "Avg tokens: " + Double.toString(this.avgTokens) + "\n";
        String total = "Total tokens: " + Double.toString(this.totalTokens) + "\n";
        return min + max + avg + total;
    }
}
