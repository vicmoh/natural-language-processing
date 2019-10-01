import java.io.InputStreamReader;

public class Tokenizer {
  private Lexer scanner = null;

  /**
   * Construct a scanner
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
  public static void main(String argv[]) {
    System.out.println("Starting program...");
    try {
      Tokenizer scanner = new Tokenizer(new Lexer(new InputStreamReader(System.in)));
      Token tok = null;
      while ((tok = scanner.getNextToken()) != null) {
        System.out.println(tok);
      }
    } catch (Exception err) {
      System.out.println("Unexpected exception:");
      err.printStackTrace();
    }
    System.out.println("Program ended.");
  }
}
