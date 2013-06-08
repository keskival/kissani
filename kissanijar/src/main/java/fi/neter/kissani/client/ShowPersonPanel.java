package fi.neter.kissani.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

import fi.neter.kissani.shared.ProfileTO;

public class ShowPersonPanel extends VerticalLayoutContainer {

	private final KissaniServiceAsync kissaniService = GWT
			.create(KissaniService.class);
	
	/**
	 * Id can be "me".
	 */
	public ShowPersonPanel(final Kissani kissani, String id) {
		// From JSP:
		// Top menu
		// Ad
		
		// Person info
		final ContentPanel personPanelContainer = new ContentPanel();
		final PersonPanel personPanel = new PersonPanel();
		personPanelContainer.add(personPanel);

		add(personPanelContainer);
		
		// - List of cats

		final ContentPanel catPanelContainer = new ContentPanel();
		catPanelContainer.setTitle("Kissat");
		final VerticalLayoutContainer catPanel = new VerticalLayoutContainer();
		catPanelContainer.add(catPanel);
		
		final CatList catCellList = new CatList(kissani);
		
		//catPanel.add(catCellList);
		
		add(catPanelContainer);
		
		kissaniService.getProfile(id, new AsyncCallback<ProfileTO>() {

			@Override
			public void onFailure(Throwable caught) {
				JS.debug("Failed getProfile.");
			}

			@Override
			public void onSuccess(final ProfileTO profile) {
				JS.debug("Getprofile success.");

				personPanelContainer.setTitle(profile.getName());
				personPanel.setName(profile.getName());
				personPanel.setPhotoUrl(profile.getPhotoUrl());

				// List of cats
				//catCellList.setRowData(profile.getCats());
				JS.debug("Setting cat list: " + profile.getCats().toString());
				//catCellList.redraw();
				
				JS.debug("Getprofile success done.");
			}
		});

		// Footer
	}
}
