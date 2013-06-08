package fi.neter.kissani.fb;

import java.util.List;

import com.google.api.client.util.Key;

public class Friends {
    @Key
    private List<Friend> data;

    public void setData(List<Friend> data) {
        this.data = data;
    }

    public List<Friend> getData() {
        return data;
    }
}
