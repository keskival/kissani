package fi.kissani.fb;

public class Paging {
    private String previous;
    private String next;

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public String getPrevious() {
        return previous;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getNext() {
        return next;
    }
}
