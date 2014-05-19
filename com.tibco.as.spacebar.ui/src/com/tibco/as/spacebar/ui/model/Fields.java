package com.tibco.as.spacebar.ui.model;

import java.util.ArrayList;
import java.util.List;

public class Fields extends Element {

	public Fields(Element parent, String name) {
		super(parent, name);
	}
	
	@Override
	public Field getChild(String name) {
		return (Field) super.getChild(name);
	}

	public List<Field> getFields() {
		List<Field> fields = new ArrayList<Field>();
		for (Element child : getChildren()) {
			fields.add((Field) child);
		}
		return fields;
	}

}
