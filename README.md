
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

NOTE FOR A2: If you want to run from a file that has been tokenized such as `data.tokenized`
without having to use default file that has never been processed. You can run by typing:
 * Type `make A2 TOKEN_FILE=../output/data.tokenized` to run an already tokenized file.

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

## Assumption

Some assumptions:
 * The data analysis includes the `text` and `title`.
 * Number containing `comma` or `period` between them for example. `1,000,000` is count as one token.