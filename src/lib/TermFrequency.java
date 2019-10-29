package lib;

class TermFrequency {
    int did;
    int tf;

    /***
     * Object for creating the did and tf
     * 
     * @param did
     * @param tf
     */
    TermFrequency(int did, int tf) {
        this.did = did;
        this.tf = tf;
    }

    @Override
    public String toString() {
        return this.did + this.tf;
    }
}