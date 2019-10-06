# Machine Learning for Natural Language Processing

## General description:

For many NLP tasks, a document collection often needs to be preprocessed and analyzed in order to know the characteristics of the data set and apply suitable machine learning techniques. As described in the lectures, there can be many steps involved in the process, depending on the tasks to be performed as well.

## How to run:

Typing the `make clean` will clean files that are in `output/` and `bin/` folder.

Typing `make` or `make all` in program directory will 
run default the '../assets/samples.txt' file as input. 
It will create the `.class` file to `bin/` folder.
all process output will be on `output/` folder.

To run other input file by assigning `INPUT` argument:
 * `make INPUT="../assets/articles.txt"`
 * This will run `../assets/articles.txt` instead.

The `output/` after process will contain:
 * data.splitted
 * data.stats
 * data.tagged
 * data.tokenized

## File and folder structure

The program file directories is organized by:
 *  `assets/` - Where the input files are going to be.
 *  `bin/` - Where the `.class` file are going to be.
 *  `doc/` - Task, assignments, and project information.
 *  `output/` -  The output file after process when `make` is called.
 *  `packages/` -  All the dependencies needed to run the program such as `OpenNLP`.
 *  `src/` - The `.java` file containing the 4 program and helper classes.
    * -> `flex/` - flex library for parsing the documents.
    * -> `lib/` - Helper function folder to help code the 4 program.
