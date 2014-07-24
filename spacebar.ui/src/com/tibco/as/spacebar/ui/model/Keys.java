package com.tibco.as.spacebar.ui.model;

public class Keys extends Fields implements Cloneable {

	private Space space;

	@Override
	public Keys clone() {
		Keys clone = new Keys();
		copyTo(clone);
		return clone;
	}

	@Override
	public void copyTo(IElement element) {
		Keys keys = (Keys) element;
		super.copyTo(keys);
		keys.setSpace(space);
	}

	public void setSpace(Space space) {
		this.space = space;
	}

	@Override
	public String getName() {
		return "Keys";
	}

	@Override
	public Space getParent() {
		return space;
	}

}
