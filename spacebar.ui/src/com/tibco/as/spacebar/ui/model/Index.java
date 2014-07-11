package com.tibco.as.spacebar.ui.model;

import com.tibco.as.space.IndexDef.IndexType;

public class Index extends Fields implements Cloneable {

	private Indexes indexes;
	private String name;
	private IndexType type;

	public Index(Indexes indexes) {
		this.indexes = indexes;
	}

	public Index(Indexes indexes, String name) {
		this(indexes);
		this.name = name;
	}

	@Override
	public Index clone() {
		Index index = new Index(indexes);
		copyTo(index);
		return index;
	}

	@Override
	public Indexes getParent() {
		return indexes;
	}

	public void copyTo(Index index) {
		super.copyTo(index);
		index.name = name;
		index.type = type;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		fireNameChange(this.name, this.name = name);
	}

	public IndexType getType() {
		return type;
	}

	public void setType(IndexType type) {
		firePropertyChange("type", this.type, this.type = type);
	}

}
