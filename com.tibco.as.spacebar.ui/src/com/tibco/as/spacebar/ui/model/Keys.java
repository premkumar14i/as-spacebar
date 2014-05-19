package com.tibco.as.spacebar.ui.model;

public class Keys extends Fields implements Cloneable {

	public Keys(Space space, String name) {
		super(space, name);
	}

	@Override
	public Keys clone() {
		Keys clone = new Keys((Space) getParent(), getName());
		copyTo(clone);
		return clone;
	}

}
