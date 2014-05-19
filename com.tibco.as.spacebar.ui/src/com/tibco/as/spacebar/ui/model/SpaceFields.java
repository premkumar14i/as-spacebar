package com.tibco.as.spacebar.ui.model;

public class SpaceFields extends Fields implements Cloneable {

	public SpaceFields(Space space, String name) {
		super(space, name);
	}

	@Override
	public SpaceFields clone() {
		SpaceFields clone = new SpaceFields(getParent(), getName());
		copyTo(clone);
		return clone;
	}

	@Override
	public Space getParent() {
		return (Space) super.getParent();
	}

}
