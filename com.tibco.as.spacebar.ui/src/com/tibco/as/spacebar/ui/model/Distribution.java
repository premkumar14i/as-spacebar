package com.tibco.as.spacebar.ui.model;

public class Distribution extends Fields {

	public Distribution(Space space, String name) {
		super(space, name);
	}

	@Override
	public Distribution clone() {
		Distribution clone = new Distribution(getParent(), getName());
		copyTo(clone);
		return clone;
	}

	@Override
	public Space getParent() {
		return (Space) super.getParent();
	}

}
