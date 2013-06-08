package fi.neter.kissani.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.ImageCell;
import com.google.gwt.cell.client.TextCell;
import fi.neter.kissani.shared.ProfileTO;

public class PersonCell extends CompositeCell<ProfileTO> {
	final ImageCell photo = new ImageCell();
	final TextCell name = new TextCell();
	final static List<HasCell<ProfileTO, ?>> cellList = new ArrayList<HasCell<ProfileTO, ?>>();

	private static PersonCell instance = null;
	
	static PersonCell getInstance() {
		if (instance == null) {
			instance = new PersonCell();
		}
		return instance;
	}
	
	private PersonCell() {
		super(cellList);
		cellList.add(new HasCell<ProfileTO, String>() {
			@Override
			public Cell<String> getCell() {
				return photo;
			}
			@Override
			public FieldUpdater<ProfileTO, String> getFieldUpdater() {
				return new FieldUpdater<ProfileTO, String>() {

					@Override
					public void update(int index, ProfileTO object, String value) {
					}
				};
			}
			@Override
			public String getValue(ProfileTO profile) {
				return profile.getPhotoUrl();
			}
		});
		cellList.add(new HasCell<ProfileTO, String>() {

			@Override
			public Cell<String> getCell() {
				return name;
			}

			@Override
			public FieldUpdater<ProfileTO, String> getFieldUpdater() {
				return new FieldUpdater<ProfileTO, String>() {
					@Override
					public void update(int index, ProfileTO object, String value) {
					}
				};
			}

			@Override
			public String getValue(ProfileTO profile) {
				return profile.getName();
			}
		});
	}
}
