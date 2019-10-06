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
    // Counting
    public int numOfSentences = 0;
    public int numOfTokens = 0;
    public double avgTokInSentences = 0;

    /**
     * Create a document model object.
     * 
     * @param docId is the "$DOC" id, for ex. "LA010190-0001".
     * @param title is the "$TITLE" content.
     * @param text  is the "$TEXT" content.
     */
    Document(String docId, String title, String text) throws Exception {
        assert (docId != null);
        assert (title != null);
        assert (text != null);
        if (docId == null || title == null || text == null)
            throw new Exception("Could not create document due to missing content.");
        this.docId = docId;
        this.title = title;
        this.text = text;

        String docString = title + "\n" + text;
        String[] sentences = docString.split("[\n]+");

        for (String eachSen : sentences) {
            numOfSentences++;
            String[] tokens = eachSen.split("[ \r\n\t]");
            for (String eachTok : tokens)
                numOfTokens++;
        }
        this.avgTokInSentences = (double) numOfTokens / (double) numOfSentences;
    }

    /**
     * Get the title of the document.
     */
    public String getID(){
        return this.docId;
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
     * Parse from one full string of the tokenized document data.
     * 
     * @param data string to be converted to list of documents.
     * @param body is a callback of the document text. Set null if not used.
     * @return list of document object.
     */
    public static LinkedList<Document> parseToken(String data, Lambda body) throws Exception {
        debug.setFunctionName("parse").print("Invoked.");
        // Init needed vars
        LinkedList<Document> docs = new LinkedList<Document>();
        boolean isPassedFirstDoc = false;
        String[] toBeParsed = data.split("[ ]");
        String lastTag = "";
        String docId = "";
        String title = "";
        String text = "";
        final int lastIndex = toBeParsed.length - 1;

        // Start parsing
        try {
            for (int x = 0; x < toBeParsed.length; x++) {
                String curWord = toBeParsed[x];
                String trimmedCurWord = curWord.replaceAll("[ \t\n\r]", "");
                debug.print("trimmedCurWord = [" + trimmedCurWord + "]");
                debug.print("lastTag = " + lastTag);
                // Case for the doc name.
                if (trimmedCurWord.equals(TAGS[0]))
                    lastTag = TAGS[0];
                // Case for the title
                if (trimmedCurWord.equals(TAGS[1]))
                    lastTag = TAGS[1];
                // Case for the text
                if (trimmedCurWord.equals(TAGS[2]))
                    lastTag = TAGS[2];
                // Last case, assign and go to next
                if (isATag(trimmedCurWord) || lastIndex <= x) {
                    if (((trimmedCurWord.equals(TAGS[0]) || lastIndex <= x)) && isPassedFirstDoc) {
                        if (lastIndex <= x)
                            text += curWord;
                        if (body != null) {
                            title = (String) body.callback(title.trim());
                            text = (String) body.callback(text.trim());
                        }
                        docs.addLast(new Document(docId.trim(), title.trim(), text.trim()));
                        docId = "";
                        title = "";
                        text = "";
                    }
                    isPassedFirstDoc = true;
                    continue;
                }

                // Case for putting the data in document
                if (lastTag.equals(TAGS[0]))
                    docId += curWord + " ";
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
     * Parse from one full string of the document data.
     * 
     * @param data string to be converted to list of documents.
     * @param body is a callback of the document text. Set null if not used.
     * @return list of document object.
     */
    public static LinkedList<Document> parse(String data, Lambda body) throws Exception {
        debug.setFunctionName("parse").print("Invoked.");
        // Init needed vars
        LinkedList<Document> docs = new LinkedList<Document>();
        boolean isPassedFirstDoc = false;
        String[] toBeParsed = data.split("[ \r\n]|[ \n]");
        String lastTag = "";
        String docId = "";
        String title = "";
        String text = "";
        final int lastIndex = toBeParsed.length - 1;

        // Start parsing
        try {
            for (int x = 0; x < toBeParsed.length; x++) {
                String curWord = toBeParsed[x];
                // Case for the doc name.
                if (curWord.trim().equals(TAGS[0]))
                    lastTag = TAGS[0];
                // Case for the title
                if (curWord.trim().equals(TAGS[1]))
                    lastTag = TAGS[1];
                // Case for the text
                if (curWord.trim().equals(TAGS[2]))
                    lastTag = TAGS[2];
                // Last case, assign and go to next
                if (isATag(curWord.trim()) || lastIndex <= x) {
                    if (((curWord.trim().equals(TAGS[0]) || lastIndex <= x)) && isPassedFirstDoc) {
                        debug.print("text = " + text);
                        if (lastIndex <= x)
                            text += curWord + '\n';
                        if (body != null)
                            text = (String) body.callback(text);
                        docs.addLast(new Document(docId.trim(), title.trim(), text.trim()));
                        docId = "";
                        title = "";
                        text = "";
                    }
                    isPassedFirstDoc = true;
                    continue;
                }

                // Case for putting the data in document
                if (lastTag.equals(TAGS[0]))
                    docId += curWord + " ";
                if (lastTag.equals(TAGS[1]))
                    title += curWord + " ";
                if (lastTag.equals(TAGS[2]))
                    text += curWord + " ";
                else
                    text += '\n';
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
