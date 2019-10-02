import lib.*;

import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.InputStream;

public class Tokenizer {
    private Lexer scanner = null;

    /**
     * Debugger for printing.
     */
    private static Debugger debug = Debugger.init().setClassName("Tokenizer").showDebugPrint(true);

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
     * Main function to run the program.
     * 
     * @param argv
     */
    public static void main(String argv[]) throws Exception {
        System.out.println("Starting program...");
        if (argv.length < 1)
            throw new Exception("Could not find file path.");

        // Init setup for file name and path
        final String fileName = argv[0];
        final String OUTPUT_PATH = "../output/data.tokenized";

        // Try to parse
        InputStream inFile = null;
        try {
            inFile = new FileInputStream(fileName);
            Tokenizer scanner = new Tokenizer(new Lexer(new InputStreamReader(inFile)));
            Token tok = null;
            String toBeOutput = "";
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
                    toBeOutput += " " + tok.m_value;
                } else {
                    lastTokType = false;
                    toBeOutput += tok.m_value;
                }
            }
            Util.writeFile(OUTPUT_PATH, toBeOutput);
        } catch (Exception err) {
            System.out.println("Unexpected exception:");
            err.printStackTrace();
        }

        // Close the file and end
        if (inFile != null)
            inFile.close();
        System.out.println("Program ended.");
    }
}
