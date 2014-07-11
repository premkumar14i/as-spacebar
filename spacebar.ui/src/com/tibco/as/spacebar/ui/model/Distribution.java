package com.tibco.as.spacebar.ui.model;

public class Distribution extends Fields implements Cloneable {

	private Space space;

	public Distribution(Space space) {
		this.space = space;
	}

	@Override
	public Distribution clone() {
		Distribution distribution = new Distribution(space);
		copyTo(distribution);
		return distribution;
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
