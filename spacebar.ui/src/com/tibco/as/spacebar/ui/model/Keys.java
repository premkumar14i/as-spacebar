package com.tibco.as.spacebar.ui.model;

public class Keys extends Fields implements Cloneable {

	private Space space;

	@Override
	public Keys clone() {
		Keys clone = new Keys();
		copyTo(clone);
		return clone;
	}

	public void copyTo(Keys keys) {
		super.copyTo(keys);
		keys.setSpace(space);
	}

	public Space getSpace() {
		return space;
	}

	public void setSpace(Space space) {
		this.space = space;
	}

	@Override
	public String getName() {
		return "Keys";
	}

	@Override
	public IElement getParent() {
		return space;
	}

}
