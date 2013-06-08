package fi.neter.kissani.shared;

import java.io.Serializable;

public class FriendTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
    
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
