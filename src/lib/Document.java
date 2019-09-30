package lib;

import lib.*;
import java.lang.RuntimeException;
import java.util.LinkedList;
import java.util.ListIterator;

public class Document {
    // Debugger
    private static final Debugger debug = Debugger.init().showDebugPrint(true).setClassName("Document");
    // Data tag
    private static final String[] TAG = { "$DOC", "$TITLE", "$TEXT" };
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
     * Parse from one full string of the document data.
     * 
     * @param data string to be converted to list of documents
     * @return list of document object
     */
    public static LinkedList<Document> parse(String data) throws Exception {
        debug.setFunctionName("parse").print("Invoked.");

        // Init needed vars
        LinkedList<Document> docs = new LinkedList<Document>();
        String[] toBeParsed = data.split(" ");
        String lastTag = "";
        String docId = "";
        String title = "";
        String text = "";

        // Start parsing
        for (int x = 0; x < toBeParsed.length; x++) {
            String curWord = toBeParsed[x];

            // Case for the doc name.
            if (curWord.equals(TAG[0]))
                lastTag = TAG[0];
            // Case for the title
            if (curWord.equals(TAG[1]))
                lastTag = TAG[1];
            // Case for the text
            if (curWord.equals(TAG[2]))
                lastTag = TAG[2];
            // Last case, assign and go to next
            if (curWord.equals(TAG[0]) || curWord.equals(TAG[1]) || curWord.equals(TAG[2])) {
                debug.print(docId);
                debug.print(title);
                debug.print(text);
                if (curWord.equals(TAG[0]) || toBeParsed.length - 1 == x) {
                    docs.push(new Document(docId, title, text));
                    docId = "";
                    title = "";
                    text = "";
                }
                continue;
            }

            // Case for putting the data in document
            if (lastTag.equals(TAG[0]))
                docId += curWord + " ";
            if (lastTag.equals(TAG[1]))
                title += curWord + " ";
            if (lastTag.equals(TAG[2]))
                text += curWord + " ";
        }

        // Debug print
        ListIterator<Document> iter = docs.listIterator();
        while (iter.hasNext()) {
            Document doc = iter.next();
            debug.print("------------------------------>\n" + doc.toString());
        }

        // Return Docs
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