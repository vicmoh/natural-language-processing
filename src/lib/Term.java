package lib;

public class Term {
    private String docId = "";
    private int did = 0;
    private int tf = 1;
    private String token = "(NULL)";
    private int offset = 0;
    private int df = 0;
    private double weight = 0;

    /**
     * Object for creating the token
     * 
     * @param tokens
     */
    public Term(String token) {
        this.token = token;
    }

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
     * @param weight the weight to set
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * @return the weight
     */
    public double getWeight() {
        return this.weight;
    }

    /**
     * @param did the did to set
     */
    public void setDid(int did) {
        this.did = did;
    }

    /**
     * @param df the df to set
     */
    public void setDf(int df) {
        this.df = df;
    }

    /**
     * @return the df
     */
    public int getDf() {
        return this.df;
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
        return Integer.toString(this.tf);
    }
}