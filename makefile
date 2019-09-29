JFLEX=./packages/jflex-1.7.0/bin/jflex

program: flex compile run

compile:
	javac -Xlint:unchecked ./src/*.java -d ./bin/

run:
	java -cp ./bin Main "$(file)"

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
