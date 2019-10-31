package lib;

import lib.*;
import java.lang.RuntimeException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.List;
import java.util.ArrayList;

public class Document {
    // Debugger
    private static final Debugger debug = Debugger.init().showDebugPrint(false).setClassName("Document");
    // Data tag
    private static final String[] TAGS = { "$DOC", "$TITLE", "$TEXT" };
    // The attribute containing in the document.
    private String docId = "";
    private String title = "";
    private String text = "";
    private int startPosition = 0;// The line number
    // Counting
    public int numOfSentences = 0;
    public int numOfTokens = 0;
    public double avgTokInSentences = 0;
    public double avgSenInTok = 0;

    /**
     * Create a document model object.
     * 
     * @param docId is the "$DOC" id, for ex. "LA010190-0001".
     * @param title is the "$TITLE" content.
     * @param text  is the "$TEXT" content.
     */
    Document(String docId, String title, String text, int pos) throws Exception {
        assert (docId != null);
        assert (title != null);
        assert (text != null);
        if (docId == null || title == null || text == null)
            throw new Exception("Could not create document due to missing content.");
        // Init
        this.docId = docId;
        this.title = title;
        this.text = text;
        this.startPosition = pos;
        // Count
        String docString = title + "\n" + text;
        String[] sentences = docString.split("[\n]+");
        for (String eachSen : sentences) {
            numOfSentences++;
            String[] tokens = eachSen.split("[ \r\n\t]");
            for (String eachTok : tokens)
                numOfTokens++;
        }
        this.avgTokInSentences = (double) numOfTokens / (double) numOfSentences;
        this.avgSenInTok = (double) numOfSentences / (double) numOfTokens;
    }

    /**
     * Get the id of the document.
     * 
     * @return the id of the document.
     */
    public String getID() {
        return this.docId;
    }

    /**
     * Get title of the document.
     * 
     * @return the title.
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Get the body of the text document.
     * 
     * @return the body text.
     */
    public String getText() {
        return this.text;
    }

    /**
     * Get the start position.
     * 
     * @return the position.
     */
    public int getStartPos() {
        return this.startPosition;
    }

    /**
     * Check if the string is a tag.
     * 
     * @param tag to be checked.
     * @return true if it is a tag, and false if it is not.
     */
    private static boolean isATag(String toBeChecked) {
        for (String tag : TAGS)
            if (toBeChecked.equals(tag))
                return true;
        return false;
    }

    /**
     * Print the documents. TODO: Buggy, could not call twice, need to check why.
     * 
     * @param documents to be stringify
     */
    public static String stringify(LinkedList<Document> documents) {
        debug.setFunctionName("stringify").print("Invoked.");
        String tempDocs = "";
        ListIterator<Document> iter = documents.listIterator(0);
        debug.print("documents.size() " + documents.size());
        while (iter.hasNext())
            tempDocs += iter.next().toString();
        debug.print("tempDocs = " + tempDocs);
        return tempDocs;
    }

    /**
     * Parse from one full string of the document data.
     * 
     * @param data
     * @param splitLambda callback for how you want data to be split. By Default
     *                    data is split with new line.
     * @param titleLambda callback for processing the title
     * @param bodyLambda  callback for processing the body
     * @return list of document object.
     * @throws Exception
     */
    public static LinkedList<Document> parse(String data, Lambda splitLambda, Lambda titleLambda, Lambda bodyLambda)
            throws Exception {
        debug.setFunctionName("parse").print("Invoked.");
        // Init needed vars
        LinkedList<Document> docs = new LinkedList<Document>();
        boolean isPassedFirstDoc = false;
        String[] toBeParsed = new String[0];

        // Run lambda calls
        if (splitLambda == null)
            toBeParsed = data.split("[ \r\n]|[ \n]");
        else
            toBeParsed = (String[]) splitLambda.callback((Object) data);

        // Setup
        String lastTag = "";
        String docId = "";
        String title = "";
        String text = "";
        int lineNum = 1;
        final int lastIndex = toBeParsed.length - 1;

        // Start parsing
        try {
            String newLineRegex = "[\r\n]+|[\n]+";
            String spaceNewLineRegex = "[ ]+([\n]|[\r\n])";
            int startPos = 1;
            int lastPos = 1;
            for (int x = 0; x < toBeParsed.length; x++) {
                String curWord = toBeParsed[x];
                // Count the line number
                if (curWord.contains("\n"))
                    lineNum++;
                // Case for the doc name.
                if (curWord.trim().replaceAll(newLineRegex, "").equals(TAGS[0])) {
                    lastTag = TAGS[0];
                    lastPos = lineNum;
                }
                // Case for the title
                if (curWord.trim().replaceAll(newLineRegex, "").equals(TAGS[1]))
                    lastTag = TAGS[1];
                // Case for the text
                if (curWord.trim().replaceAll(newLineRegex, "").equals(TAGS[2]))
                    lastTag = TAGS[2];
                // Last case, assign and go to next
                if (isATag(curWord.trim().replaceAll(newLineRegex, "")) || lastIndex <= x) {
                    if (((curWord.trim().replaceAll(newLineRegex, "").equals(TAGS[0]) || lastIndex <= x))
                            && isPassedFirstDoc) {
                        debug.print("text = " + text);
                        if (lastIndex <= x)
                            text += curWord;
                        if (titleLambda != null)
                            title = (String) titleLambda.callback(title.trim());
                        if (bodyLambda != null)
                            text = (String) bodyLambda.callback(text.trim());
                        // Add to doc object
                        docs.addLast(new Document(docId.trim(), title.trim().replaceAll(spaceNewLineRegex, "\n"),
                                text.trim().replaceAll(spaceNewLineRegex, "\n"), startPos));
                        docId = "";
                        title = "";
                        text = "";
                    }
                    isPassedFirstDoc = true;
                    continue;
                }

                // Case for putting the data in document
                if (lastTag.equals(TAGS[0])) {
                    docId += curWord + " ";
                    startPos = lastPos;
                }
                if (lastTag.equals(TAGS[1]))
                    title += curWord + " ";
                if (lastTag.equals(TAGS[2]))
                    text += curWord + " ";
            }
        } catch (Exception err) {
            throw new Exception("Could not parse file data. Invalid Format.");
        }

        // Return Docs
        // TODO: Buggy, could not call twice, need to check why.
        // debug.print("---------------------------------->\n" + stringify(docs));
        return docs;
    }

    /**
     * Create string of the document format.
     */
    @Override
    public String toString() {
        debug.setFunctionName("toString").print("Invoked.");
        if (this.docId.equals("") || this.title.equals("") || this.text.equals(""))
            return "";
        String docId = "$DOC " + this.docId + "\n";
        String text = "$TEXT\n" + this.text + "\n";
        String title = "$TITLE\n" + this.title + "\n";
        return docId + title + text;
    }
}
