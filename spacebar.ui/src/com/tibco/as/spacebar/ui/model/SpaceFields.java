package com.tibco.as.spacebar.ui.model;

public class SpaceFields extends Fields implements Cloneable {

	private Space space;

	@Override
	public SpaceFields clone() {
		SpaceFields clone = new SpaceFields();
		copyTo(clone);
		return clone;
	}

	public void copyTo(SpaceFields fields) {
		super.copyTo(fields);
		fields.setSpace(space);
	}

	public Space getSpace() {
		return space;
	}

	public void setSpace(Space space) {
		this.space = space;
	}

	@Override
	public Space getParent() {
		return space;
	}

	public Field getField(String name) {
		return (Field) getChild(name);
	}

}
