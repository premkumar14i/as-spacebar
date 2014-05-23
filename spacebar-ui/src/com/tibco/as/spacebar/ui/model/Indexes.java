package com.tibco.as.spacebar.ui.model;

import java.util.ArrayList;
import java.util.List;

public class Indexes extends Element implements Cloneable {

	public Indexes(Space space, String name) {
		super(space, name);
	}

	@Override
	public Indexes clone() {
		Indexes clone = new Indexes((Space) getParent(), getName());
		copyTo(clone);
		return clone;
	}

	public List<Index> getIndexes() {
		List<Index> indexes = new ArrayList<Index>();
		for (Element child : getChildren()) {
			indexes.add((Index) child);
		}
		return indexes;
	}

	public Index getIndex(String indexName) {
		return (Index) getChild(indexName);
	}

	@Override
	public Space getParent() {
		return (Space) super.getParent();
	}

}
