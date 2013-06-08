package fi.neter.kissani.shared;

import java.io.Serializable;

public class PhotoTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;

    private String icon;

    private String link;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

}
