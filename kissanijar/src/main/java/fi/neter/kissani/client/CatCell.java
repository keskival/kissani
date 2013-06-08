package fi.neter.kissani.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.ImageCell;
import com.google.gwt.cell.client.TextCell;

import fi.neter.kissani.shared.CatTO;

public class CatCell extends CompositeCell<CatTO> {
	final ImageCell catPic = new ImageCell();
	final TextCell catLabel = new TextCell();
	final static List<HasCell<CatTO, ?>> cellList = new ArrayList<HasCell<CatTO, ?>>();

	private static CatCell instance = null;
	
	static CatCell getInstance() {
		if (instance == null) {
			instance = new CatCell();
		}
		return instance;
	}
	
	private CatCell() {
		super(cellList);
		
		HasCell<CatTO, String> picCell = new HasCell<CatTO, String>() {
			@Override
			public Cell<String> getCell() {
				return catPic;
			}
			@Override
			public FieldUpdater<CatTO, String> getFieldUpdater() {
				return new FieldUpdater<CatTO, String>() {

					@Override
					public void update(int index, CatTO object, String value) {
					}
				};
			}
			@Override
			public String getValue(CatTO cat) {
				String url = "https://graph.facebook.com/" + cat.getDefaultPhoto() + "/picture?access_token=" + JS.getAccessToken();
				return url;
			}
		};
		
		HasCell<CatTO, String> nameCell = new HasCell<CatTO, String>() {

			@Override
			public Cell<String> getCell() {
				return catLabel;
			}

			@Override
			public FieldUpdater<CatTO, String> getFieldUpdater() {
				return new FieldUpdater<CatTO, String>() {
					@Override
					public void update(int index, CatTO object, String value) {
					}
				};
			}

			@Override
			public String getValue(CatTO cat) {
				return cat.getName();
			}
		};
				
		cellList.add(picCell);
		cellList.add(nameCell);
	}
}
