package com.tibco.as.spacebar.ui.model;

import java.util.ArrayList;
import java.util.List;

public class Spaces extends AbstractElement {

	private Metaspace metaspace;

	private List<Space> spaces = new ArrayList<Space>();

	public Spaces() {
	}

	public Spaces(Metaspace metaspace) {
		this.metaspace = metaspace;
	}

	public Metaspace getMetaspace() {
		return metaspace;
	}

	public void setMetaspace(Metaspace metaspace) {
		this.metaspace = metaspace;
	}

	public Metaspace getParent() {
		return metaspace;
	}

	@Override
	public Space getChild(String name) {
		return (Space) super.getChild(name);
	}

	@Override
	public String getName() {
		return "Spaces";
	}

	@Override
	public List<Space> getChildren() {
		return spaces;
	}

	@Override
	protected boolean add(IElement child) {
		return spaces.add((Space) child);
	}

	@Override
	protected boolean remove(IElement child) {
		return spaces.remove(child);
	}

	@Override
	protected void copyChild(IElement source, IElement target) {
		((Space) source).copyTo((Space) target);
	}

}
