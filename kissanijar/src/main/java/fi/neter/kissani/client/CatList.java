package fi.neter.kissani.client;

import java.util.List;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;

import fi.neter.kissani.shared.CatTO;

public class CatList { // extends VerticalLayoutContainer {
	
	public CatList(final Kissani kissani) {
		super();
		//		CatTO selected = selectionModel.getSelectedObject();
		//		kissani.showCat(selected.getId());
	}
	
	public void setCatList(List<CatTO> catTos) {
//		this.clear();
		for (CatTO cat : catTos) {
//			CatListItem catItem = new CatListItem(CatTO);
//			this.add(catItem);
		}
	}
}
