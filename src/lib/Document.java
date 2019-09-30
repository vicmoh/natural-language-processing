package lib;

import lib.*;
import java.lang.RuntimeException;
import java.util.LinkedList;
import java.util.ListIterator;

public class Document {
    // Debugger
    private static final Debugger debug = Debugger.init().showDebugPrint(true).setClassName("Document");
    // Data tag
    private static final String[] TAGS = { "$DOC", "$TITLE", "$TEXT" };
    // The attribute containing in the document.
    private String docId;
    private String title;
    private String text;

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
    }

    /**
     * Check if the string is a tag.
     * 
     * @param tag to be checked.
     * @return true if it is a tag, and false if it is not.
     */
    private static boolean isATag(String tag) {
        for (int x = 0; x < TAGS.length; x++)
            if (tag.equals(TAGS[x]))
                return true;
        return false;
    }

    /**
     * Print the documents.
     * 
     * @param documents to be debugged and printed
     */
    private static void debugDocuments(LinkedList<Document> documents) {
        debug.setFunctionName("debugDocuments").print("Invoked.");
        // Documents debug print
        ListIterator<Document> iter = documents.listIterator();
        while (iter.hasNext()) {
            Document doc = iter.next();
            debug.print("------------------------------>\n" + doc.toString());
        }
    }

    /**
     * Parse from one full string of the document data.
     * 
     * @param data string to be converted to list of documents
     * @return list of document object
     */
    public static LinkedList<Document> parse(String data) throws Exception {
        debug.setFunctionName("parse").print("Invoked.");
        // Init needed vars
        LinkedList<Document> docs = new LinkedList<Document>();
        boolean isPassedFirstDoc = false;
        String[] toBeParsed = data.split(" ");
        String lastTag = "";
        String docId = "";
        String title = "";
        String text = "";

        // Start parsing
        for (int x = 0; x < toBeParsed.length; x++) {
            String curWord = toBeParsed[x];
            // Case for the doc name.
            if (curWord.equals(TAGS[0]))
                lastTag = TAGS[0];
            // Case for the title
            if (curWord.equals(TAGS[1]))
                lastTag = TAGS[1];
            // Case for the text
            if (curWord.equals(TAGS[2]))
                lastTag = TAGS[2];
            // Last case, assign and go to next
            if (isATag(curWord) && isPassedFirstDoc) {
                if (curWord.equals(TAGS[0]) || toBeParsed.length - 1 == x) {
                    docs.push(new Document(docId.trim(), title.trim(), text.trim()));
                    docId = "";
                    title = "";
                    text = "";
                }
                continue;
            }
            isPassedFirstDoc = true;
            // Case for putting the data in document
            if (lastTag.equals(TAGS[0]))
                docId += curWord + " ";
            if (lastTag.equals(TAGS[1]))
                title += curWord + " ";
            if (lastTag.equals(TAGS[2]))
                text += curWord + " ";
        }

        // Return Docs
        debugDocuments(docs);
        return docs;
    }

    /**
     * Create string of the document format.
     */
    @Override
    public String toString() {
        this.docId = "$DOC " + docId + "\n";
        this.title = "$TITLE\n" + title + "\n";
        this.text = "$TEXT\n" + text;
        return this.docId + this.title + this.text;
    }
}