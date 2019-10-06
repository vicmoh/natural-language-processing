import lib.*;

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
     * Default constructor
     */
    private Tokenizer() {
    }

    /**
     * Construct a scanner.
     * 
     * @param lexer
     */
    private Tokenizer(Lexer lexer) {
        scanner = lexer;
    }

    /**
     * Get the next token.
     */
    private Token getNextToken() throws java.io.IOException {
        return scanner.yylex();
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
}
