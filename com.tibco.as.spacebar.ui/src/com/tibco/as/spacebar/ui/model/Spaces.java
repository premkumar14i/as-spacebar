package com.tibco.as.spacebar.ui.model;

public class Spaces extends Element {

	public Spaces(Metaspace metaspace, String name) {
		super(metaspace, name);
	}

	public Metaspace getMetaspace() {
		return (Metaspace) getParent();
	}

	@Override
	public Spaces clone() {
		Spaces spaces = new Spaces(getMetaspace(), getName());
		copyTo(spaces);
		return spaces;
	}
	
	@Override
	public Space getChild(String name) {
		return (Space) super.getChild(name);
	}

}
