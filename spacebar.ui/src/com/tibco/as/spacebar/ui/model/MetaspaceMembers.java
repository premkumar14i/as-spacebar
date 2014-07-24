package com.tibco.as.spacebar.ui.model;

public class MetaspaceMembers extends Members implements Cloneable {

	private Metaspace metaspace;

	public void setMetaspace(Metaspace metaspace) {
		this.metaspace = metaspace;
	}

	@Override
	public Metaspace getParent() {
		return metaspace;
	}

	@Override
	public MetaspaceMembers clone() {
		MetaspaceMembers clone = new MetaspaceMembers();
		copyTo(clone);
		return clone;
	}

	@Override
	public void copyTo(IElement element) {
		MetaspaceMembers members = (MetaspaceMembers) element;
		super.copyTo(members);
		members.setMetaspace(metaspace);
	}

}
