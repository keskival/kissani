package fi.neter.kissani.client;

import com.google.gwt.user.cellview.client.CellList;

import fi.neter.kissani.shared.ProfileTO;

public class PersonList extends CellList<ProfileTO> {
	public PersonList() {
		super(PersonCell.getInstance());
	}
}
