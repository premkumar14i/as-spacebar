package com.tibco.as.spacebar.ui.model;

public class SpaceMembers extends Members {

	private Space space;

	public void setSpace(Space space) {
		this.space = space;
	}

	@Override
	public Space getParent() {
		return space;
	}

	@Override
	public SpaceMembers clone() {
		SpaceMembers clone = new SpaceMembers();
		copyTo(clone);
		return clone;
	}

	@Override
	public void copyTo(IElement element) {
		SpaceMembers clone = (SpaceMembers) element;
		super.copyTo(clone);
		clone.setSpace(space);
	}
}
