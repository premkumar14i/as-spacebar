package com.tibco.as.spacebar.ui.editor.snapshot;

import com.tibco.as.space.Tuple;

public class Change {

	public enum Type {
		PUT, TAKE
	}

	private Type type;

	private Tuple tuple;

	public Change(Type type, Tuple tuple) {
		this.type = type;
		this.tuple = tuple;
	}

	public Tuple getTuple() {
		return tuple;
	}
	
	public Type getType() {
		return type;
	}

}
