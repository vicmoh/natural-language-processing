JAVAC=javac
# JFLEX=jflex
#JFLEX=/home/fsong/jflex/bin/jflex
JFLEX=../../../packages/jflex-1.7.0/bin/jflex

all: Token.class Lexer.class Scanner.class

%.class: %.java
	$(JAVAC) $^

Lexer.java: tiny.flex
	$(JFLEX) tiny.flex

clean:
	rm -f Lexer.java *.class *~
