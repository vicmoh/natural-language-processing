public class Token {
  // Main
  public final static int ID = 1;
  public final static int NUM = 2;
  public final static int ERROR = 3;
  // Word type
  public final static int LABEL = 4;
  public final static int WORD = 5;
  public final static int NUMBER = 6;
  public final static int APOSTROPHIZED = 7;
  public final static int HYPHENATED = 8;
  public final static int NEWLINE = 9;
  public final static int PUNCTUATION = 10;
  // Other
  public final static int WHITE_SPACES = 11;

  public int m_type;
  public String m_value;
  public int m_line;
  public int m_column;

  Token(int type, String value, int line, int column) {
    m_type = type;
    m_value = value;
    m_line = line;
    m_column = column;
  }

  public String toString() {
    switch (m_type) {
    // Tokens
    case LABEL:
      return "LABEL";
    case WORD:
      return "WORD";
    case NUMBER:
      return "NUMBER";
    case APOSTROPHIZED:
      return "APOSTROPHIZED";
    case HYPHENATED:
      return "HYPHENATED";
    case NEWLINE:
      return "NEWLINE";
    case PUNCTUATION:
      return "PUNCTUATION";
    // Other
    case WHITE_SPACES:
      return "WHITE_SPACES";
    // For testing
    case ID:
      return "ID(" + m_value + ")";
    case NUM:
      return "NUM(" + m_value + ")";
    case ERROR:
      return "ERROR(" + m_value + ")";
    default:
      return "UNKNOWN(" + m_value + ")";
    }
  }
}
