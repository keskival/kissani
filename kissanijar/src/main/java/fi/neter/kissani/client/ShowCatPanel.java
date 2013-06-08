package fi.neter.kissani.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

import fi.neter.kissani.shared.CatTO;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ShowCatPanel extends VerticalLayoutContainer {

	private final KissaniServiceAsync kissaniService = GWT
			.create(KissaniService.class);
	
	/**
	 * This is the entry point method.
	 */
	public ShowCatPanel(final Kissani kissani, Long catId) {
		// From JSP:
		// Top menu
		// Ad
		
		// FIXME
		// Cat info (name + photo)
		final ContentPanel catPanelContainer = new ContentPanel();
		final CatPanel catPanel = new CatPanel();
		catPanelContainer.add(catPanel);

		add(catPanelContainer);
		
		// Gallery
		
		// Cat info
		
		// List of owners
		
		// Menu
		
		// Cat friends

		final ContentPanel catFriendsPanelContainer = new ContentPanel();
		catFriendsPanelContainer.setTitle("Kissakaverit");
		final VerticalLayoutContainer catFriendsPanel = new VerticalLayoutContainer();
		catFriendsPanelContainer.add(catFriendsPanel);
		
		final CatList catCellList = new CatList(kissani);
		
		//catFriendsPanel.add(catCellList);
		
		add(catFriendsPanelContainer);

		kissaniService.getCat(catId, new AsyncCallback<CatTO>() {

			@Override
			public void onFailure(Throwable caught) {
				JS.debug("Failed getProfile.");
			}

			@Override
			public void onSuccess(final CatTO profile) {
				JS.debug("Getprofile success.");

				catPanelContainer.setTitle(profile.getName());
				catPanel.setName(profile.getName());
				catPanel.setPhotoUrl(profile.getDefaultPhoto());

				// List of cat friends
				//catCellList.setRowData(profile.getPrefetchedFriends());
				//catCellList.redraw();
				
				JS.debug("Getprofile success done.");
			}
		});
	}
}
