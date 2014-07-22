package com.tibco.as.spacebar.ui.model;

import java.util.ArrayList;
import java.util.List;

public class Indexes extends AbstractElement implements Cloneable {

	private Space space;

	private List<Index> indexes = new ArrayList<Index>();

	public Space getSpace() {
		return space;
	}

	public void setSpace(Space space) {
		this.space = space;
	}

	@Override
	public List<Index> getChildren() {
		return indexes;
	}

	@Override
	public String getName() {
		return "Indexes";
	}

	@Override
	protected boolean remove(IElement child) {
		return indexes.remove(child);
	}

	@Override
	protected boolean add(IElement child) {
		return indexes.add((Index) child);
	}

	@Override
	protected void copyChild(IElement source, IElement target) {
		((Index) source).copyTo((Index) target);
	}

	@Override
	public Indexes clone() {
		Indexes clone = new Indexes();
		copyTo(clone);
		return clone;
	}

	public void setIndexes(List<Index> indexes) {
		this.indexes = indexes;
	}

	public void copyTo(Indexes indexes) {
		indexes.setSpace(space);
		indexes.setIndexes(new ArrayList<Index>(this.indexes));
	}

	@Override
	public Space getParent() {
		return space;
	}

	public List<Index> getIndexes() {
		return indexes;
	}

	public Index getIndex(String name) {
		return (Index) getChild(name);
	}

}
