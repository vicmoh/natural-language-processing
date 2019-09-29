JFLEX=./packages/jflex-1.7.0/bin/jflex

run_program: flex compile run

run_debug: flex compile debug

#-------------------------------------------------
# Below are sub commands
#-------------------------------------------------

compile:
	javac -Xlint:unchecked ./src/*.java -d ./bin/

run:
	java -cp ./bin Main "$(file)"

debug:
	java -ea -cp ./bin Main "$(file)"

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

flex: src/dummy.flex
	$(JFLEX) src/dummy.flex 
