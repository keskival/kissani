package fi.neter.kissani.dao;

import java.io.Serializable;
import java.util.ArrayList;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Person implements Serializable {
	private static final long serialVersionUID = 1L;

	@PrimaryKey
    @Persistent
    private Long id;
    
    @Persistent
    private ArrayList<Long> cats = new ArrayList<Long>();

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setCats(ArrayList<Long> cats) {
        this.cats = cats;
    }

    public ArrayList<Long> getCats() {
        return cats;
    }
    
    @Override
    public boolean equals(Object rhs) {
    	if (rhs == this) {
    		return true;
    	}
    	if (rhs instanceof Person) {
    		Person rhsPerson = (Person) rhs;
    		if (rhsPerson.getId() == getId()) {
    			return true;
    		}
    	}
    	return false;
    }
    
    @Override
    public int hashCode() {
    	if (getId() == null) {
    		return 0;
    	}
    	return getId().intValue();
    }
}
 