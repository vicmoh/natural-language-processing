NLP=.:./packages/opennlp-tools-1.9.1.jar
JFLEX=./packages/jflex-1.7.0/bin/jflex
SRC_PATH=./src/*.java ./src/lib/*.java ./src/flex/*.java

run: flex compile sentence tokenizer

compile_all: flex compile

#-------------------------------------------------
# Below are sub commands
#-------------------------------------------------

flex: src/flex/doc.flex
	$(JFLEX) src/flex/doc.flex 

compile:
	javac -Xlint:unchecked -classpath $(NLP) $(SRC_PATH) -d ./bin/

sentence:
	cd ./bin && java -classpath ./opennlp-tools-1.9.1.jar:. SentenceDetection ../assets/samples.txt

tokenizer:
	cd ./bin && java -classpath ./opennlp-tools-1.9.1.jar:. Tokenizer ../output/data.splitted

#-------------------------------------------------
# Check commands
#-------------------------------------------------

check_split:
	diff output/data.splitted ./examples/samples/samples.splitted

check_token:
	diff output/data.tokenized ./examples/samples/samples.tokenized

#-------------------------------------------------
# GitHub and cleans
#-------------------------------------------------

git:
	git add -A
	git commit -m "[AUTO]"
	git push

push:
	git add -A
	git commit -m "$(m)"
	git push

clean:
	rm ./bin/*.class
