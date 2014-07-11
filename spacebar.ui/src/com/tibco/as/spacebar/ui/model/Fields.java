package com.tibco.as.spacebar.ui.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Fields extends AbstractElement {

	private List<Field> fields = new ArrayList<Field>();

	public void copyTo(Fields target) {
		target.fields = new ArrayList<Field>(fields);
	}

	@Override
	protected boolean add(IElement child) {
		return fields.add((Field) child);
	}

	@Override
	protected boolean remove(IElement child) {
		return fields.remove(child);
	}

	@Override
	public List<Field> getChildren() {
		return fields;
	}

	@Override
	protected void copyChild(IElement source, IElement target) {
		((Field) source).copyTo((Field) target);
	}

	@Override
	public String getName() {
		return "Fields";
	}

}
