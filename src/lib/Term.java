package lib;

public class Term {
    private String docId = "";
    private int did = 0;
    private int tf = 1;
    private String token = "(NULL)";
    private int offset = 0;

    /**
     * Object for creating the docId and tf
     * 
     * @param docId
     * @param tokens
     */
    public Term(String docId, String token) {
        this.docId = docId;
        this.token = token;
    }

    /**
     * Object for creating term used for the loading of the inverted file
     * 
     * @param token
     * @param offset
     */
    public Term(String token, int offset) {
        this.token = token;
        this.offset = offset;
    }

    /**
     * @param did the did to set
     */
    public void setDid(int did) {
        this.did = did;
    }

    /**
     * @return the did
     */
    public int getDid() {
        return this.did;
    }

    /**
     * Increment the frequency of the token.
     */
    public void incrementFrequency() {
        this.tf++;
    }

    /**
     * Get the doc id.
     * 
     * @return return the doc id.
     */
    public String getDocId() {
        return this.docId;
    }

    /**
     * Get the token string also known as term or word.
     * 
     * @return the token
     */
    public String getToken() {
        return this.token;
    }

    /**
     * Get term frequency.
     * 
     * @return the term frequency.
     */
    public int getTf() {
        return this.tf;
    }

    /**
     * Get the offset.
     * 
     * @return the offset
     */
    public int getOffset() {
        return this.offset;
    }

    @Override
    public String toString() {
        return this.docId + " " + Integer.toString(this.tf);
    }
}