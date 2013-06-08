package fi.neter.kissani.fb;

import com.google.api.client.util.Key;

public class Paging {
    @Key
    private String previous;

    @Key
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
