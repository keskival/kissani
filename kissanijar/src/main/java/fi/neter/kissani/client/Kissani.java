package fi.neter.kissani.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Kissani implements EntryPoint {
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		// From JSP:
		// Top menu
		// Ad

		showMe();
	}
	
	public void showMe() {
		showPerson("me");
	}
	
	public void showPerson(String id) {
		RootPanel root = RootPanel.get("contentContainer");
		VerticalLayoutContainer panel = new ShowPersonPanel(this, id);
		root.clear();
		root.add(panel);
	}
	
	public void showCat(Long catId) {
		RootPanel root = RootPanel.get("contentContainer");
		VerticalLayoutContainer panel = new ShowCatPanel(this, catId);
		root.clear();
		root.add(panel);
	}
}
