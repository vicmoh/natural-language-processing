package lib;

public class Term {
    private int did = 0;
    private int tf = 1;
    private String token = "(NULL)";

    /**
     * Object for creating the did and tf
     * 
     * @param did
     * @param token
     */
    public Term(int did, String token) {
        this.did = did;
        this.token = token;
    }

    public void incrementFrequency() {
        this.tf++;
    }

    public int getDid() {
        return this.did;
    }

    public int getTf() {
        return this.did;
    }

    @Override
    public String toString() {
        return this.token + ": " + this.did + " " + this.tf;
    }
}