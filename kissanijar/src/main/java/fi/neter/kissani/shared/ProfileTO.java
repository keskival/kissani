package fi.neter.kissani.shared;

import java.io.Serializable;
import java.util.List;

public class ProfileTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
    
    private String name;
    
    private String link;

    private List<CatTO> cats;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<CatTO> getCats() {
		return cats;
	}

	public void setCats(List<CatTO> cats) {
		this.cats = cats;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getPhotoUrl() {
		return "https://graph.facebook.com/" + getId() + "/picture";
	}
}
