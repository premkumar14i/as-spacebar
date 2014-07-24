package com.tibco.as.spacebar.ui.model;

public class Distribution extends Fields {

	private Space space;

	@Override
	public Distribution clone() {
		Distribution distribution = new Distribution();
		copyTo(distribution);
		return distribution;
	}

	@Override
	public void copyTo(IElement element) {
		Distribution distribution = (Distribution) element;
		super.copyTo(distribution);
		distribution.setSpace(space);
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
