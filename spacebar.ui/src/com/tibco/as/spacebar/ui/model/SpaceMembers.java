package com.tibco.as.spacebar.ui.model;

public class SpaceMembers extends Members {
	
	private Space space;

	public SpaceMembers(Space space) {
		this.space = space;
	}

	@Override
	public Space getParent() {
		return space;
	}

}
