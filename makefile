NLP=.:./packages/opennlp-tools-1.9.1.jar
JFLEX=./packages/jflex-1.7.0/bin/jflex
SRC_PATH=./src/*.java ./src/lib/*.java ./src/flex/*.java
INPUT=../assets/samples.txt

all: flex compile sentencer tokenizer tagger analyzer

compile_all: flex compile

#-------------------------------------------------
# Below are sub commands
#-------------------------------------------------

flex: src/flex/doc.flex
	$(JFLEX) src/flex/doc.flex 

compile:
	javac -Xlint:unchecked -classpath $(NLP) $(SRC_PATH) -d ./bin/

sentencer:
	cd ./bin && java -classpath ./opennlp-tools-1.9.1.jar:. Sentencer $(INPUT)

tokenizer:
	cd ./bin && java -classpath ./opennlp-tools-1.9.1.jar:. Tokenizer ../output/data.splitted

tagger:
	cd ./bin && java -classpath ./opennlp-tools-1.9.1.jar:. Tagger ../output/data.tokenized

analyzer:
	cd ./bin && java -classpath ./opennlp-tools-1.9.1.jar:. Analyzer $(INPUT)

#-------------------------------------------------
# Check commands
#-------------------------------------------------

check_all: check_split check_token check_tag

check_split:
	diff ./output/data.splitted ./examples/samples/samples.splitted

check_token:
	diff ./output/data.tokenized ./examples/samples/samples.tokenized

check_tag:
	diff ./output/data.tagged ./examples/samples/samples.tagged

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

install:
	cd ./packages && unzip ./jflex-1.7.0.zip

clean:
	rm ./bin/*.class
