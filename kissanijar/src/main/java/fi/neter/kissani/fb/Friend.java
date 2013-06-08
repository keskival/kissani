package fi.neter.kissani.fb;

import javax.persistence.Transient;

import com.google.api.client.util.Key;

import fi.neter.kissani.shared.FriendTO;

public class Friend {
    @Key
    private Long id;
    
    @Key
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
    
    @Transient
    public FriendTO getFriendTo() {
    	FriendTO to = new FriendTO();
    	to.setId(id);
    	to.setName(name);
    	return to;
    }
}
