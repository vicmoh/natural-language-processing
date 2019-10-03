/* The following code was generated by JFlex 1.7.0 */

/*
  File Name: tiny.flex
  JFlex specification for the TINY language
*/
   

/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.7.0
 * from the specification file <tt>src/flex/doc.flex</tt>
 */
class Lexer {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int YYINITIAL = 0;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0, 0
  };

  /** 
   * Translates characters to character classes
   */
  private static final String ZZ_CMAP_PACKED = 
    "\11\0\1\2\1\1\1\23\1\2\1\1\22\0\1\2\3\0\1\10"+
    "\2\0\1\5\3\0\1\21\1\0\1\7\1\22\1\0\12\3\7\0"+
    "\2\4\1\13\1\11\1\17\3\4\1\15\2\4\1\16\2\4\1\12"+
    "\3\4\1\6\1\14\3\4\1\20\2\4\4\0\1\4\1\0\22\4"+
    "\1\6\7\4\12\0\1\23\44\0\1\4\12\0\1\4\4\0\1\4"+
    "\5\0\27\4\1\0\37\4\1\0\u01ca\4\4\0\14\4\16\0\5\4"+
    "\7\0\1\4\1\0\1\4\21\0\165\4\1\0\2\4\2\0\4\4"+
    "\1\0\1\4\6\0\1\4\1\0\3\4\1\0\1\4\1\0\24\4"+
    "\1\0\123\4\1\0\213\4\1\0\255\4\1\0\46\4\2\0\1\4"+
    "\7\0\47\4\11\0\55\4\1\0\1\4\1\0\2\4\1\0\2\4"+
    "\1\0\1\4\10\0\33\4\5\0\3\4\35\0\13\4\5\0\112\4"+
    "\4\0\146\4\1\0\10\4\2\0\12\4\1\0\23\4\2\0\1\4"+
    "\20\0\73\4\2\0\145\4\16\0\66\4\4\0\1\4\5\0\56\4"+
    "\22\0\34\4\104\0\25\4\1\0\10\4\26\0\16\4\1\0\201\4"+
    "\2\0\12\4\1\0\23\4\1\0\10\4\2\0\2\4\2\0\26\4"+
    "\1\0\7\4\1\0\1\4\3\0\4\4\2\0\11\4\2\0\2\4"+
    "\2\0\4\4\10\0\1\4\4\0\2\4\1\0\5\4\2\0\14\4"+
    "\17\0\3\4\1\0\6\4\4\0\2\4\2\0\26\4\1\0\7\4"+
    "\1\0\2\4\1\0\2\4\1\0\2\4\2\0\1\4\1\0\5\4"+
    "\4\0\2\4\2\0\3\4\3\0\1\4\7\0\4\4\1\0\1\4"+
    "\7\0\20\4\13\0\3\4\1\0\11\4\1\0\3\4\1\0\26\4"+
    "\1\0\7\4\1\0\2\4\1\0\5\4\2\0\12\4\1\0\3\4"+
    "\1\0\3\4\2\0\1\4\17\0\4\4\2\0\12\4\11\0\1\4"+
    "\7\0\3\4\1\0\10\4\2\0\2\4\2\0\26\4\1\0\7\4"+
    "\1\0\2\4\1\0\5\4\2\0\11\4\2\0\2\4\2\0\3\4"+
    "\10\0\2\4\4\0\2\4\1\0\5\4\2\0\12\4\1\0\1\4"+
    "\20\0\2\4\1\0\6\4\3\0\3\4\1\0\4\4\3\0\2\4"+
    "\1\0\1\4\1\0\2\4\3\0\2\4\3\0\3\4\3\0\14\4"+
    "\4\0\5\4\3\0\3\4\1\0\4\4\2\0\1\4\6\0\1\4"+
    "\16\0\12\4\20\0\4\4\1\0\10\4\1\0\3\4\1\0\27\4"+
    "\1\0\20\4\3\0\10\4\1\0\3\4\1\0\4\4\7\0\2\4"+
    "\1\0\3\4\5\0\4\4\2\0\12\4\20\0\4\4\1\0\10\4"+
    "\1\0\3\4\1\0\27\4\1\0\12\4\1\0\5\4\2\0\11\4"+
    "\1\0\3\4\1\0\4\4\7\0\2\4\7\0\1\4\1\0\4\4"+
    "\2\0\12\4\1\0\2\4\16\0\3\4\1\0\10\4\1\0\3\4"+
    "\1\0\51\4\2\0\10\4\1\0\3\4\1\0\5\4\5\0\4\4"+
    "\7\0\5\4\2\0\12\4\12\0\6\4\2\0\2\4\1\0\22\4"+
    "\3\0\30\4\1\0\11\4\1\0\1\4\2\0\7\4\3\0\1\4"+
    "\4\0\6\4\1\0\1\4\1\0\10\4\6\0\12\4\2\0\2\4"+
    "\15\0\72\4\5\0\17\4\1\0\12\4\47\0\2\4\1\0\1\4"+
    "\2\0\2\4\1\0\1\4\2\0\1\4\6\0\4\4\1\0\7\4"+
    "\1\0\3\4\1\0\1\4\1\0\1\4\2\0\2\4\1\0\15\4"+
    "\1\0\3\4\2\0\5\4\1\0\1\4\1\0\6\4\2\0\12\4"+
    "\2\0\4\4\40\0\1\4\27\0\2\4\6\0\12\4\13\0\1\4"+
    "\1\0\1\4\1\0\1\4\4\0\12\4\1\0\44\4\4\0\24\4"+
    "\1\0\22\4\1\0\44\4\11\0\1\4\71\0\112\4\6\0\116\4"+
    "\2\0\46\4\1\0\1\4\5\0\1\4\2\0\53\4\1\0\u014d\4"+
    "\1\0\4\4\2\0\7\4\1\0\1\4\1\0\4\4\2\0\51\4"+
    "\1\0\4\4\2\0\41\4\1\0\4\4\2\0\7\4\1\0\1\4"+
    "\1\0\4\4\2\0\17\4\1\0\71\4\1\0\4\4\2\0\103\4"+
    "\2\0\3\4\40\0\20\4\20\0\126\4\2\0\6\4\3\0\u026c\4"+
    "\2\0\21\4\1\0\32\4\5\0\113\4\3\0\13\4\7\0\15\4"+
    "\1\0\7\4\13\0\25\4\13\0\24\4\14\0\15\4\1\0\3\4"+
    "\1\0\2\4\14\0\124\4\3\0\1\4\4\0\2\4\2\0\12\4"+
    "\41\0\3\4\2\0\12\4\6\0\130\4\10\0\53\4\5\0\106\4"+
    "\12\0\37\4\1\0\14\4\4\0\14\4\12\0\50\4\2\0\5\4"+
    "\13\0\54\4\4\0\32\4\6\0\12\4\46\0\34\4\4\0\77\4"+
    "\1\0\35\4\2\0\13\4\6\0\12\4\15\0\1\4\10\0\17\4"+
    "\101\0\114\4\4\0\12\4\21\0\11\4\14\0\164\4\14\0\70\4"+
    "\10\0\12\4\3\0\61\4\2\0\11\4\107\0\3\4\1\0\43\4"+
    "\1\0\2\4\6\0\366\4\5\0\u011b\4\2\0\6\4\2\0\46\4"+
    "\2\0\6\4\2\0\10\4\1\0\1\4\1\0\1\4\1\0\1\4"+
    "\1\0\37\4\2\0\65\4\1\0\7\4\1\0\1\4\3\0\3\4"+
    "\1\0\7\4\3\0\4\4\2\0\6\4\4\0\15\4\5\0\3\4"+
    "\1\0\7\4\53\0\1\23\1\23\25\0\2\4\23\0\1\4\34\0"+
    "\1\4\15\0\1\4\20\0\15\4\63\0\41\4\21\0\1\4\4\0"+
    "\1\4\2\0\12\4\1\0\1\4\3\0\5\4\6\0\1\4\1\0"+
    "\1\4\1\0\1\4\1\0\4\4\1\0\13\4\2\0\4\4\5\0"+
    "\5\4\4\0\1\4\21\0\51\4\u032d\0\64\4\u0716\0\57\4\1\0"+
    "\57\4\1\0\205\4\6\0\11\4\14\0\46\4\1\0\1\4\5\0"+
    "\1\4\2\0\70\4\7\0\1\4\17\0\30\4\11\0\7\4\1\0"+
    "\7\4\1\0\7\4\1\0\7\4\1\0\7\4\1\0\7\4\1\0"+
    "\7\4\1\0\7\4\1\0\40\4\57\0\1\4\u01d5\0\3\4\31\0"+
    "\17\4\1\0\5\4\2\0\5\4\4\0\126\4\2\0\2\4\2\0"+
    "\3\4\1\0\132\4\1\0\4\4\5\0\51\4\3\0\136\4\21\0"+
    "\33\4\65\0\20\4\u0200\0\u19b6\4\112\0\u51d6\4\52\0\u048d\4\103\0"+
    "\56\4\2\0\u010d\4\3\0\34\4\24\0\63\4\1\0\12\4\1\0"+
    "\163\4\45\0\11\4\2\0\147\4\2\0\44\4\1\0\10\4\77\0"+
    "\61\4\30\0\64\4\14\0\106\4\12\0\12\4\6\0\30\4\3\0"+
    "\1\4\1\0\1\4\2\0\56\4\2\0\44\4\14\0\35\4\3\0"+
    "\101\4\16\0\13\4\6\0\37\4\1\0\67\4\11\0\16\4\2\0"+
    "\12\4\6\0\27\4\3\0\111\4\30\0\3\4\2\0\20\4\2\0"+
    "\5\4\12\0\6\4\2\0\6\4\2\0\6\4\11\0\7\4\1\0"+
    "\7\4\1\0\53\4\1\0\12\4\12\0\173\4\1\0\2\4\2\0"+
    "\12\4\6\0\u2ba4\4\14\0\27\4\4\0\61\4\u2104\0\u016e\4\2\0"+
    "\152\4\46\0\7\4\14\0\5\4\5\0\14\4\1\0\15\4\1\0"+
    "\5\4\1\0\1\4\1\0\2\4\1\0\2\4\1\0\154\4\41\0"+
    "\u016b\4\22\0\100\4\2\0\66\4\50\0\14\4\4\0\20\4\20\0"+
    "\20\4\3\0\2\4\30\0\3\4\40\0\5\4\1\0\207\4\23\0"+
    "\12\4\7\0\32\4\4\0\1\4\1\0\32\4\13\0\131\4\3\0"+
    "\6\4\2\0\6\4\2\0\6\4\2\0\3\4\43\0\14\4\1\0"+
    "\32\4\1\0\23\4\1\0\2\4\1\0\17\4\2\0\16\4\42\0"+
    "\173\4\105\0\65\4\210\0\1\4\202\0\35\4\3\0\61\4\17\0"+
    "\1\4\37\0\40\4\20\0\33\4\5\0\53\4\5\0\36\4\2\0"+
    "\44\4\4\0\10\4\1\0\5\4\52\0\236\4\2\0\12\4\6\0"+
    "\44\4\4\0\44\4\4\0\50\4\10\0\64\4\234\0\u0137\4\11\0"+
    "\26\4\12\0\10\4\230\0\6\4\2\0\1\4\1\0\54\4\1\0"+
    "\2\4\3\0\1\4\2\0\27\4\12\0\27\4\11\0\37\4\101\0"+
    "\23\4\1\0\2\4\12\0\26\4\12\0\32\4\106\0\70\4\6\0"+
    "\2\4\100\0\4\4\1\0\2\4\5\0\10\4\1\0\3\4\1\0"+
    "\33\4\4\0\3\4\4\0\1\4\40\0\35\4\3\0\35\4\43\0"+
    "\10\4\1\0\36\4\31\0\66\4\12\0\26\4\12\0\23\4\15\0"+
    "\22\4\156\0\111\4\67\0\63\4\15\0\63\4\u030d\0\107\4\37\0"+
    "\12\4\17\0\74\4\25\0\31\4\7\0\12\4\6\0\65\4\1\0"+
    "\12\4\20\0\44\4\2\0\1\4\11\0\105\4\5\0\3\4\3\0"+
    "\13\4\1\0\1\4\43\0\22\4\1\0\45\4\6\0\1\4\101\0"+
    "\7\4\1\0\1\4\1\0\4\4\1\0\17\4\1\0\12\4\7\0"+
    "\73\4\5\0\12\4\6\0\4\4\1\0\10\4\2\0\2\4\2\0"+
    "\26\4\1\0\7\4\1\0\2\4\1\0\5\4\2\0\11\4\2\0"+
    "\2\4\2\0\3\4\2\0\1\4\6\0\1\4\5\0\7\4\2\0"+
    "\7\4\3\0\5\4\213\0\113\4\5\0\12\4\46\0\106\4\1\0"+
    "\1\4\10\0\12\4\246\0\66\4\2\0\11\4\27\0\6\4\42\0"+
    "\101\4\3\0\1\4\13\0\12\4\46\0\70\4\10\0\12\4\66\0"+
    "\32\4\3\0\17\4\4\0\12\4\u0166\0\112\4\25\0\1\4\u01c0\0"+
    "\71\4\u0107\0\11\4\1\0\55\4\1\0\11\4\17\0\12\4\30\0"+
    "\36\4\2\0\26\4\1\0\16\4\u0349\0\u039a\4\146\0\157\4\21\0"+
    "\304\4\u0abc\0\u042f\4\u0fd1\0\u0247\4\u21b9\0\u0239\4\7\0\37\4\1\0"+
    "\12\4\146\0\36\4\2\0\5\4\13\0\67\4\11\0\4\4\14\0"+
    "\12\4\11\0\25\4\5\0\23\4\u0370\0\105\4\13\0\57\4\20\0"+
    "\21\4\100\0\1\4\37\0\u17ed\4\23\0\u02f3\4\u250d\0\2\4\u0bfe\0"+
    "\153\4\5\0\15\4\3\0\11\4\7\0\12\4\3\0\2\4\u14c6\0"+
    "\5\4\3\0\6\4\10\0\10\4\2\0\7\4\36\0\4\4\224\0"+
    "\3\4\u01bb\0\125\4\1\0\107\4\1\0\2\4\2\0\1\4\2\0"+
    "\2\4\2\0\4\4\1\0\14\4\1\0\1\4\1\0\7\4\1\0"+
    "\101\4\1\0\4\4\2\0\10\4\1\0\7\4\1\0\34\4\1\0"+
    "\4\4\1\0\5\4\1\0\1\4\3\0\7\4\1\0\u0154\4\2\0"+
    "\31\4\1\0\31\4\1\0\37\4\1\0\31\4\1\0\37\4\1\0"+
    "\31\4\1\0\37\4\1\0\31\4\1\0\37\4\1\0\31\4\1\0"+
    "\10\4\2\0\62\4\u0200\0\67\4\4\0\62\4\10\0\1\4\16\0"+
    "\1\4\26\0\5\4\1\0\17\4\u0550\0\7\4\1\0\21\4\2\0"+
    "\7\4\1\0\2\4\1\0\5\4\u07d5\0\305\4\13\0\7\4\51\0"+
    "\113\4\5\0\12\4\u04a6\0\4\4\1\0\33\4\1\0\2\4\1\0"+
    "\1\4\2\0\1\4\1\0\12\4\1\0\4\4\1\0\1\4\1\0"+
    "\1\4\6\0\1\4\4\0\1\4\1\0\1\4\1\0\1\4\1\0"+
    "\3\4\1\0\2\4\1\0\1\4\2\0\1\4\1\0\1\4\1\0"+
    "\1\4\1\0\1\4\1\0\1\4\1\0\2\4\1\0\1\4\2\0"+
    "\4\4\1\0\7\4\1\0\4\4\1\0\4\4\1\0\1\4\1\0"+
    "\12\4\1\0\21\4\5\0\3\4\1\0\5\4\1\0\21\4\u0274\0"+
    "\32\4\6\0\32\4\6\0\32\4\u0e76\0\ua6d7\4\51\0\u1035\4\13\0"+
    "\336\4\2\0\u1682\4\u295e\0\u021e\4\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\u06ed\0"+
    "\360\4\uffff\0\uffff\0\ufe12\0";

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\1\0\1\1\1\2\1\3\2\4\1\5\2\1\2\4"+
    "\3\0\1\5\4\0\1\5\1\6\1\7\1\5\1\7"+
    "\5\0\1\10\2\0\1\6\1\0";

  private static int [] zzUnpackAction() {
    int [] result = new int[34];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\24\0\50\0\74\0\120\0\144\0\170\0\214"+
    "\0\240\0\264\0\310\0\334\0\360\0\u0104\0\u0118\0\u012c"+
    "\0\u0140\0\u0154\0\u0168\0\u017c\0\u0190\0\u0104\0\24\0\u012c"+
    "\0\u01a4\0\u01b8\0\u01cc\0\u0118\0\u01e0\0\24\0\u01f4\0\u0208"+
    "\0\u01e0\0\u021c";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[34];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\2\1\3\1\4\1\5\1\6\1\7\1\6\1\10"+
    "\1\11\10\6\1\10\1\2\26\0\1\3\1\4\22\0"+
    "\2\4\24\0\1\12\1\13\1\14\1\13\1\15\1\0"+
    "\10\13\1\0\1\16\4\0\2\13\1\14\1\13\1\15"+
    "\1\0\10\13\6\0\2\17\1\0\1\17\2\0\10\17"+
    "\6\0\1\20\31\0\1\21\2\0\1\22\12\0\1\12"+
    "\1\13\1\23\1\13\1\15\1\0\10\13\1\0\1\16"+
    "\4\0\2\13\1\23\1\13\1\15\1\0\10\13\6\0"+
    "\2\24\1\0\1\24\2\0\10\24\6\0\2\25\1\0"+
    "\1\25\2\0\10\25\6\0\1\26\23\0\2\27\1\0"+
    "\1\27\2\0\10\27\6\0\1\30\16\0\1\16\13\0"+
    "\1\31\26\0\1\32\1\0\1\33\12\0\1\27\20\0"+
    "\2\24\1\34\1\24\2\0\10\24\6\0\2\25\1\0"+
    "\1\25\1\35\1\0\10\25\16\0\1\36\24\0\1\37"+
    "\27\0\1\40\6\0\2\41\1\0\1\41\2\0\10\41"+
    "\21\0\1\42\21\0\1\36\26\0\1\36\4\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[560];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String ZZ_ERROR_MSG[] = {
    "Unknown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\1\0\1\11\11\1\3\0\1\1\4\0\3\1\1\11"+
    "\1\1\5\0\1\11\2\0\1\1\1\0";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[34];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private char zzBuffer[] = new char[ZZ_BUFFERSIZE];

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /** number of newlines encountered up to the start of the matched text */
  private int yyline;

  /** the number of characters up to the start of the matched text */
  private int yychar;

  /**
   * the number of characters from the last newline up to the start of the 
   * matched text
   */
  private int yycolumn;

  /** 
   * zzAtBOL == true iff the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true iff the scanner is at the EOF */
  private boolean zzAtEOF;

  /** denotes if the user-EOF-code has already been executed */
  private boolean zzEOFDone;
  
  /** 
   * The number of occupied positions in zzBuffer beyond zzEndRead.
   * When a lead/high surrogate has been read from the input stream
   * into the final zzBuffer position, this will have a value of 1;
   * otherwise, it will have a value of 0.
   */
  private int zzFinalHighSurrogate = 0;


  /**
   * Creates a new scanner
   *
   * @param   in  the java.io.Reader to read input from.
   */
  Lexer(java.io.Reader in) {
    this.zzReader = in;
  }


  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    char [] map = new char[0x110000];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < 2822) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }


  /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   * 
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {

    /* first: make room (if you can) */
    if (zzStartRead > 0) {
      zzEndRead += zzFinalHighSurrogate;
      zzFinalHighSurrogate = 0;
      System.arraycopy(zzBuffer, zzStartRead,
                       zzBuffer, 0,
                       zzEndRead-zzStartRead);

      /* translate stored positions */
      zzEndRead-= zzStartRead;
      zzCurrentPos-= zzStartRead;
      zzMarkedPos-= zzStartRead;
      zzStartRead = 0;
    }

    /* is the buffer big enough? */
    if (zzCurrentPos >= zzBuffer.length - zzFinalHighSurrogate) {
      /* if not: blow it up */
      char newBuffer[] = new char[zzBuffer.length*2];
      System.arraycopy(zzBuffer, 0, newBuffer, 0, zzBuffer.length);
      zzBuffer = newBuffer;
      zzEndRead += zzFinalHighSurrogate;
      zzFinalHighSurrogate = 0;
    }

    /* fill the buffer with new input */
    int requested = zzBuffer.length - zzEndRead;
    int numRead = zzReader.read(zzBuffer, zzEndRead, requested);

    /* not supposed to occur according to specification of java.io.Reader */
    if (numRead == 0) {
      throw new java.io.IOException("Reader returned 0 characters. See JFlex examples for workaround.");
    }
    if (numRead > 0) {
      zzEndRead += numRead;
      /* If numRead == requested, we might have requested to few chars to
         encode a full Unicode character. We assume that a Reader would
         otherwise never return half characters. */
      if (numRead == requested) {
        if (Character.isHighSurrogate(zzBuffer[zzEndRead - 1])) {
          --zzEndRead;
          zzFinalHighSurrogate = 1;
        }
      }
      /* potentially more input available */
      return false;
    }

    /* numRead < 0 ==> end of stream */
    return true;
  }

    
  /**
   * Closes the input stream.
   */
  public final void yyclose() throws java.io.IOException {
    zzAtEOF = true;            /* indicate end of file */
    zzEndRead = zzStartRead;  /* invalidate buffer    */

    if (zzReader != null)
      zzReader.close();
  }


  /**
   * Resets the scanner to read from a new input stream.
   * Does not close the old reader.
   *
   * All internal variables are reset, the old input stream 
   * <b>cannot</b> be reused (internal buffer is discarded and lost).
   * Lexical state is set to <tt>ZZ_INITIAL</tt>.
   *
   * Internal scan buffer is resized down to its initial length, if it has grown.
   *
   * @param reader   the new input stream 
   */
  public final void yyreset(java.io.Reader reader) {
    zzReader = reader;
    zzAtBOL  = true;
    zzAtEOF  = false;
    zzEOFDone = false;
    zzEndRead = zzStartRead = 0;
    zzCurrentPos = zzMarkedPos = 0;
    zzFinalHighSurrogate = 0;
    yyline = yychar = yycolumn = 0;
    zzLexicalState = YYINITIAL;
    if (zzBuffer.length > ZZ_BUFFERSIZE)
      zzBuffer = new char[ZZ_BUFFERSIZE];
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final String yytext() {
    return new String( zzBuffer, zzStartRead, zzMarkedPos-zzStartRead );
  }


  /**
   * Returns the character at position <tt>pos</tt> from the 
   * matched text. 
   * 
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch. 
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer[zzStartRead+pos];
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of 
   * yypushback(int) and a match-all fallback rule) this method 
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  } 


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public Token yylex() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    char [] zzBufferL = zzBuffer;
    char [] zzCMapL = ZZ_CMAP;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      boolean zzR = false;
      int zzCh;
      int zzCharCount;
      for (zzCurrentPosL = zzStartRead  ;
           zzCurrentPosL < zzMarkedPosL ;
           zzCurrentPosL += zzCharCount ) {
        zzCh = Character.codePointAt(zzBufferL, zzCurrentPosL, zzMarkedPosL);
        zzCharCount = Character.charCount(zzCh);
        switch (zzCh) {
        case '\u000B':  // fall through
        case '\u000C':  // fall through
        case '\u0085':  // fall through
        case '\u2028':  // fall through
        case '\u2029':
          yyline++;
          yycolumn = 0;
          zzR = false;
          break;
        case '\r':
          yyline++;
          yycolumn = 0;
          zzR = true;
          break;
        case '\n':
          if (zzR)
            zzR = false;
          else {
            yyline++;
            yycolumn = 0;
          }
          break;
        default:
          zzR = false;
          yycolumn += zzCharCount;
        }
      }

      if (zzR) {
        // peek one character ahead if it is \n (if we have counted one line too much)
        boolean zzPeek;
        if (zzMarkedPosL < zzEndReadL)
          zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        else if (zzAtEOF)
          zzPeek = false;
        else {
          boolean eof = zzRefill();
          zzEndReadL = zzEndRead;
          zzMarkedPosL = zzMarkedPos;
          zzBufferL = zzBuffer;
          if (eof) 
            zzPeek = false;
          else 
            zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        }
        if (zzPeek) yyline--;
      }
      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;
  
      zzState = ZZ_LEXSTATE[zzLexicalState];

      // set up zzAction for empty match case:
      int zzAttributes = zzAttrL[zzState];
      if ( (zzAttributes & 1) == 1 ) {
        zzAction = zzState;
      }


      zzForAction: {
        while (true) {
    
          if (zzCurrentPosL < zzEndReadL) {
            zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL, zzEndReadL);
            zzCurrentPosL += Character.charCount(zzInput);
          }
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL, zzEndReadL);
              zzCurrentPosL += Character.charCount(zzInput);
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMapL[zzInput] ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
        zzAtEOF = true;
          {   return null;
 }
      }
      else {
        switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
          case 1: 
            { return new Token(Token.ERROR, yytext(), yyline, yycolumn);
            } 
            // fall through
          case 9: break;
          case 2: 
            { return new Token(Token.NEWLINE, yytext(), yyline, yycolumn);
            } 
            // fall through
          case 10: break;
          case 3: 
            { 
            } 
            // fall through
          case 11: break;
          case 4: 
            { return new Token(Token.WORD, yytext(), yyline, yycolumn);
            } 
            // fall through
          case 12: break;
          case 5: 
            { return new Token(Token.APOSTROPHIZED, yytext(), yyline, yycolumn);
            } 
            // fall through
          case 13: break;
          case 6: 
            { return new Token(Token.HYPHENATED, yytext(), yyline, yycolumn);
            } 
            // fall through
          case 14: break;
          case 7: 
            { return new Token(Token.NUMBER, yytext(), yyline, yycolumn);
            } 
            // fall through
          case 15: break;
          case 8: 
            { return new Token(Token.LABEL, yytext(), yyline, yycolumn);
            } 
            // fall through
          case 16: break;
          default:
            zzScanError(ZZ_NO_MATCH);
        }
      }
    }
  }


}
