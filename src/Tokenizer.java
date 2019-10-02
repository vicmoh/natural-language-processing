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
    final String outputPath = "../output/data.tokenized";

    // Try to parse
    try {
      InputStream inFile = new FileInputStream(fileName);
      Tokenizer scanner = new Tokenizer(new Lexer(new InputStreamReader(inFile)));
      Token tok = null;
      String toBeOutput = "";
      while ((tok = scanner.getNextToken()) != null)
        toBeOutput += tok.m_value;
      Util.writeFile(outputPath, toBeOutput);
    } catch (Exception err) {
      System.out.println("Unexpected exception:");
      err.printStackTrace();
    }
    System.out.println("Program ended.");
  }
}
