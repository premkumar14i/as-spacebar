package com.tibco.as.spacebar.ui.model;

public class Keys extends Fields implements Cloneable {

	private Space space;
	
	public Keys(Space space) {
		this.space = space;
	}

	@Override
	public Keys clone() {
		Keys clone = new Keys(space);
		copyTo(clone);
		return clone;
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
