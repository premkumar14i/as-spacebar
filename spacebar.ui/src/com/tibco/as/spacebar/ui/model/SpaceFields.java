package com.tibco.as.spacebar.ui.model;

public class SpaceFields extends Fields implements Cloneable {

	private Space space;

	public SpaceFields(Space space) {
		this.space = space;
	}

	@Override
	public SpaceFields clone() {
		SpaceFields clone = new SpaceFields(space);
		copyTo(clone);
		return clone;
	}

	@Override
	public Space getParent() {
		return space;
	}

	public Field getField(String name) {
		return (Field) getChild(name);
	}

}
