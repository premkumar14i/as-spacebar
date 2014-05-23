package com.tibco.as.spacebar.ui.model;

public class Members extends Element {

	public Members(Element parent, String name) {
		super(parent, name);
	}

	@Override
	public Members clone() {
		Members members = new Members(getParent(), getName());
		copyTo(members);
		return members;
	}

}
