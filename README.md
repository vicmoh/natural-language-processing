
# Machine Learning for Natural Language Processing

## Author

Name: `Vicky Mohammad`
Email: `mohammav@uoguelph.ca`

If you need help compiling and running the program, or has some questions, please do contact me.

## General description

For many NLP tasks, a document collection often needs to be preprocessed and analyzed in order to know the characteristics of the data set and apply suitable machine learning techniques. As described in the lectures, there can be many steps involved in the process, depending on the tasks to be performed as well.

## How to run

Typing the `make clean` will clean files that are in `output/` and `bin/` folder.

Typing `make` or `make all` in program directory will 
run default the '../assets/samples.txt' file as input. 
It will create the `.class` file to `bin/` folder.
all process output will be on `output/` folder.

To run other input file by assigning `INPUT` argument:
 * type: `make INPUT="../assets/articles.txt"`
 * This will run `../assets/articles.txt` as input file instead.

The `output/` after process will contain:
 * `data.splitted` - created from `Sentencer.java`
 * `data.stats` - created from `Analyzer.java`
 * `data.tagged` - created from `Tagger.java`
 * `data.tokenized` - created from `Tokenizer.java`

The output for the information retrieval will in the `output/` file after
it has been processed:
 * `data.stemmed`
 * `dictionary.txt`
 * `docids.txt`
 * `posting.txt`

NOTE FOR A2: If you want to run from a file that has been tokenized such as `data.tokenized`
without having to use default file that has never been processed. You can run by typing:
 * Type `make a2 TOKEN_FILE=../output/data.tokenized` to run an already tokenized file.
 * By typing the command above it will generate 3 files to `output/` folder and run the query program.

## File and folder structure

The program file directories is organized by:
 *  `assets/` - Where the input files are going to be.
 *  `bin/` - Where the `.class` file are going to be.
 *  `doc/` - Task, assignments, and project information.
 *  `output/` -  The output file after process when `make` is called.
 *  `packages/` -  All the dependencies needed to run the program such as `OpenNLP`.
 *  `src/` - The `.java` file containing the 4 program and helper classes.
    * `flex/` - flex library for parsing the documents.
    * `lib/` - Helper function folder to help code the 4 program.
        * `Analyzer.java` - The program to create the statistics
        * `Sentencer.java` - The program to create the sentence splitting
        * `Tagger.java` - The program to create the tagging with the NLP model.
        * `Tokenizer.java` - The program to create the tokenized data.
        * `Preproccessed.java`  - The preproccessed that that create the `data.stemmed`.
        * `OfflineProcessed.java`  - The offline processed that that creates the three file `dictionary.txt` , `docids.txt` and `posting.txt`.
        * `OnlineProcessed.java`  - The online processed that to run the query.

## Assumption

Some assumptions:
 * The data analysis includes the `text` and `title`.
 * Number containing `comma` or `period` between them for example. `1,000,000` is count as one token.
 * Please not that the posting file is value might be different due to how I ordered the document frequency but functionality should be the same.

## Testing plan

I created a test plan by using the same test through an example video:
`https://www.youtube.com/watch?v=hc3DCn8viWs`.

The first test of the query: `The best italian restaurant enjoy the best pasta`.
Should output with the following order: `1, 2, 3, 4`.

The second test of the query: `American restaurant enjoy the best hamburger`.
Should output with the following order: `2, 4, 1, 3`.

The third test of the query: `Korean restaurant enjoy the best bibimbap`.
Should output with the following order: `3, 1, 2, 4`.

The third test of the query: `The best the best American restaurant`.
Should output with the following order: `2, 4, 1, 3`.

Based on his example the resulting output seems to be valid and passed.
I created a `test.txt` in the `assets/` folder. To see if it works within
the test case. From this I also did multiple extra random test cases and counted
manually on my own to see if the resulting output is correct.

```

Enter a query: The best italian restaurant enjoy the best pasta

---------------------- Query Result ----------------------
Document ID: LA010190-0001
Title: test
Similarity Value: 5.388322273422942
----------------------------------------------------------
Document ID: LA010190-0002
Title: test
Similarity Value: 0.9260128682630195
----------------------------------------------------------
Document ID: LA010190-0003
Title: test
Similarity Value: 0.9260128682630195
----------------------------------------------------------
Document ID: LA010190-0004
Title: test
Similarity Value: 0.0
----------------------------------------------------------

Enter a query: American restaurant enjoy the best hamburger

---------------------- Query Result ----------------------
Document ID: LA010190-0002
Title: test
Similarity Value: 2.0415902195530005
----------------------------------------------------------
Document ID: LA010190-0004
Title: test
Similarity Value: 1.1155773512899807
----------------------------------------------------------
Document ID: LA010190-0001
Title: test
Similarity Value: 0.9260128682630195
----------------------------------------------------------
Document ID: LA010190-0003
Title: test
Similarity Value: 0.9260128682630195
----------------------------------------------------------

Enter a query: Korean restaurant enjoy the best bibimbap

---------------------- Query Result ----------------------
Document ID: LA010190-0003
Title: test
Similarity Value: 5.388322273422942
----------------------------------------------------------
Document ID: LA010190-0001
Title: test
Similarity Value: 0.9260128682630195
----------------------------------------------------------
Document ID: LA010190-0002
Title: test
Similarity Value: 0.9260128682630195
----------------------------------------------------------
Document ID: LA010190-0004
Title: test
Similarity Value: 0.0
----------------------------------------------------------

Enter a query: The best the best American restaurant

---------------------- Query Result ----------------------
Document ID: LA010190-0002
Title: test
Similarity Value: 1.077551070443845
----------------------------------------------------------
Document ID: LA010190-0004
Title: test
Similarity Value: 0.761500010418809
----------------------------------------------------------
Document ID: LA010190-0001
Title: test
Similarity Value: 0.3160510600250359
----------------------------------------------------------
Document ID: LA010190-0003
Title: test
Similarity Value: 0.3160510600250359
----------------------------------------------------------

```