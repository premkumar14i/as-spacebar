package com.tibco.as.spacebar.ui.model;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;

import com.tibco.as.space.Tuple;

public class TupleSelection implements IStructuredSelection {

	private Space space;

	private List<Tuple> tuples;
	
	public TupleSelection(Space space, List<Tuple> tuples) {
		this.space = space;
		this.tuples = tuples;
	}

	public Space getSpace() {
		return space;
	}

	public void setSpace(Space space) {
		this.space = space;
	}

	public List<Tuple> getTuples() {
		return tuples;
	}

	public void setTuples(List<Tuple> tuples) {
		this.tuples = tuples;
	}

	@Override
	public boolean isEmpty() {
		return tuples.isEmpty();
	}

	@Override
	public Object getFirstElement() {
		return tuples.get(0);
	}

	@Override
	public Iterator<Tuple> iterator() {
		return tuples.iterator();
	}

	@Override
	public int size() {
		return tuples.size();
	}

	@Override
	public Object[] toArray() {
		return tuples.toArray();
	}

	@Override
	public List<Tuple> toList() {
		return tuples;
	}
}
