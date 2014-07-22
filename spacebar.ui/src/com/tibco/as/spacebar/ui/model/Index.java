package com.tibco.as.spacebar.ui.model;

import com.tibco.as.space.IndexDef.IndexType;

public class Index extends Fields implements Cloneable {

	private Indexes indexes;
	private String name;
	private IndexType type;

	@Override
	public Index clone() {
		Index index = new Index();
		copyTo(index);
		return index;
	}

	@Override
	public Indexes getParent() {
		return indexes;
	}

	public void copyTo(Index index) {
		super.copyTo(index);
		index.setIndexes(indexes);
		index.setName(name);
		index.setType(type);
	}

	public Indexes getIndexes() {
		return indexes;
	}

	public void setIndexes(Indexes indexes) {
		this.indexes = indexes;
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
