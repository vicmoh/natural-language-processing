package lib;

import java.lang.RuntimeException;

public class Document {
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

    @Override
    public String toString() {
        this.docId = "$DOC" + docId + "\n";
        this.title = "$TITLE\n" + title + "\n";
        this.text = "$TEXT\n" + text;
        return this.docId + this.title + this.text;
    }
}