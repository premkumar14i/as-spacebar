package com.tibco.as.spacebar.ui.model;

import com.tibco.as.space.IndexDef.IndexType;

public class Index extends Fields implements Cloneable {

	private IndexType type;

	public Index(Indexes indexes, String name) {
		super(indexes, name);
	}

	@Override
	public Index clone() {
		Index index = new Index(getParent(), getName());
		copyTo(index);
		return index;
	}

	@Override
	public Indexes getParent() {
		return (Indexes) super.getParent();
	}

	public void copyTo(Index index) {
		super.copyTo(index);
		index.type = type;
	}

	public IndexType getType() {
		return type;
	}

	public void setType(IndexType type) {
		firePropertyChange("type", this.type, this.type = type);
	}

}
