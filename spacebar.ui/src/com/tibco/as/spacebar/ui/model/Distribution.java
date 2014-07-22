package com.tibco.as.spacebar.ui.model;

public class Distribution extends Fields implements Cloneable {

	private Space space;

	@Override
	public Distribution clone() {
		Distribution distribution = new Distribution();
		copyTo(distribution);
		return distribution;
	}

	public void copyTo(Distribution distribution) {
		super.copyTo(distribution);
		distribution.setSpace(space);
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

	@Override
	public String getName() {
		return "Distribution";
	}

}
