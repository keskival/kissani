package fi.neter.kissani.client;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

public class PersonPanel extends VerticalLayoutContainer {
	private final Label nameLabel = new Label();
	private final Image image = new Image();

	public PersonPanel() {
		super();
		// - Name
		add(nameLabel);
		// - Photo
		add(image);
	}

	public void setName(String name) {
		nameLabel.setText(name);
	}

	public void setPhotoUrl(String photoUrl) {
		image.setUrl(photoUrl);
	}
}
