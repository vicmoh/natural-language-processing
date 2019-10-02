/*
  File Name: tiny.flex
  JFlex specification for the TINY language
*/
   
%%
   
%class Lexer
%type Token
%line
%column
    
%eofval{
  return null;
%eofval};


/* A line terminator is a \r (carriage return), \n (line feed), or
   \r\n. */
LineTerminator = \r|\n|\r\n
   
/* White space is a line terminator, space, tab, or form feed. */
WhiteSpace     = {LineTerminator} | [ \t\f]
   
/* A literal integer is is a number beginning with a number between
   one and nine followed by zero or more numbers between zero and nine
   or just a zero.  */
digit = [0-9]
number = {digit}+
   
/* A identifier integer is a word beginning a letter between A and
   Z, a and z, or an underscore followed by zero or more letters
   between A and Z, a and z, zero and nine, or an underscore. */
letter = [a-zA-Z]
identifier = {letter}+

// Apostrophized cases: "John's", "O'Reily", "O'Reily's".
aposCase1 = [\w]+['][sS]|[\w]['][\w]+|[\w]['][\w]+['][\w]
// Apostrophized cases: "You 're", "I 've".
aposCase2 = ['][\w][\w]|['][\w]
// Apostrophized case: "world ' cup"
aposCase3 = [']

%%
   
/*
   This section contains regular expressions and actions, i.e. Java
   code, that will be executed when the scanner matches the associated
   regular expression. */
   
// [$][a-zA-Z]+                   { return new Token(Token.LABEL, yytext(), yyline, yycolumn); }
\$DOC                           { return new Token(Token.LABEL, yytext(), yyline, yycolumn); }
\$TITLE                         { return new Token(Token.LABEL, yytext(), yyline, yycolumn); }
\$TEXT                          { return new Token(Token.LABEL, yytext(), yyline, yycolumn); }
{aposCase1}                     { return new Token(Token.APOSTROPHIZED, yytext(), yyline, yycolumn); }
{aposCase2}                     { return new Token(Token.APOSTROPHIZED, yytext(), yyline, yycolumn); }
{aposCase3}                     { return new Token(Token.APOSTROPHIZED, yytext(), yyline, yycolumn); }
[\w]+                           { return new Token(Token.WORD, yytext(), yyline, yycolumn); }
[0-9]|[-+]?[0-9]+[.]?[0-9]+     { return new Token(Token.NUMBER, yytext(), yyline, yycolumn); }
[\w]+[\-][\w?]+                 { return new Token(Token.HYPHENATED, yytext(), yyline, yycolumn); }
[\"][\w]+[\"]                   { return new Token(Token.PUNCTUATION, yytext(), yyline, yycolumn); }
{LineTerminator}+               { return new Token(Token.NEWLINE, yytext(), yyline, yycolumn); }

/* Other attribute  */
{identifier}                    { return new Token(Token.ID, yytext(), yyline, yycolumn); }
{WhiteSpace}+                   {  }
.                               { return new Token(Token.ERROR, yytext(), yyline, yycolumn); }
