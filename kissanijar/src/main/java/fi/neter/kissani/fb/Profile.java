package fi.neter.kissani.fb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Transient;

import com.google.api.client.util.Key;

import fi.neter.kissani.dao.Cat;
import fi.neter.kissani.shared.CatTO;
import fi.neter.kissani.shared.ProfileTO;


public class Profile {
    @Key
    private Long id;
    
    @Key
    private String name;
    
    @Key
    private String link;

    @Key
    private Friends friends;

	@Transient
    private Collection<Cat> cats;
	
    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Friends getFriends() {
        return friends;
    }

    public void setFriends(Friends friends) {
        this.friends = friends;
    }

    @Override
    public String toString() {
        return "[" + this.getId() + ":" + this.getName() + "," + this.getLink() + "]";
    }

	public String getPhotoUrl() {
		return "https://graph.facebook.com/" + getId() + "/picture";
	}

	public Collection<Cat> getCats() {
		return cats;
	}

	public void setCats(Collection<Cat> cats) {
		this.cats = cats;
	}

	@Transient
	public ProfileTO getProfileTO() {
		ProfileTO profileTO = new ProfileTO();
		
		List<CatTO> catsTO = new ArrayList<CatTO>();
		if (cats != null) {
			for (Cat cat : cats) {
				catsTO.add(cat.getCatTO());
			}
		}
		
		profileTO.setCats(catsTO);
		profileTO.setId(id);
		profileTO.setLink(link);
		profileTO.setName(name);
		
		return profileTO;
	}
}
