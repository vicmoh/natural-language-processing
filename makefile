NLP=.:./packages/opennlp-tools-1.9.1.jar
JFLEX=./packages/jflex-1.7.0/bin/jflex
SRC_PATH=./src/*.java ./src/lib/*.java ./src/flex/*.java
INPUT=../assets/sample.txt
OUPUT=../out/sample.splitted

run: flex compile sentence

#-------------------------------------------------
# Below are sub commands
#-------------------------------------------------

compile:
	javac -Xlint:unchecked -classpath $(NLP) $(SRC_PATH) -d ./bin/

sentence:
	cd ./bin && java -classpath ./opennlp-tools-1.9.1.jar:. SentenceDetection $(INPUT)

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

flex: src/flex/dummy.flex
	$(JFLEX) src/flex/dummy.flex 
