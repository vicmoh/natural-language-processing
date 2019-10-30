package lib;

public class Term {
    private String did = "";
    private int tf = 1;
    private String token = "(NULL)";

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

    @Override
    public String toString() {
        return this.token + ": " + this.did + " " + this.tf;
    }
}