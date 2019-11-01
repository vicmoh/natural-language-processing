package lib;

public class Term {
    private String did = "";
    private int tf = 1;
    private String token = "(NULL)";
    private int offset = 0;

    /**
     * Object for creating the did and tf
     * 
     * @param did
     * @param tokens
     */
    public Term(String did, String token) {
        this.did = did;
        this.token = token;
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
    public String getDid() {
        return this.did;
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
        return this.did + " " + this.tf;
    }
}