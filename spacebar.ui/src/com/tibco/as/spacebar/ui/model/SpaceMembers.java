package com.tibco.as.spacebar.ui.model;

public class SpaceMembers extends Members {

	private Space space;

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

}
