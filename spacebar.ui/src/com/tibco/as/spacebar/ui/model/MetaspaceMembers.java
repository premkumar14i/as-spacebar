package com.tibco.as.spacebar.ui.model;


public class MetaspaceMembers extends Members implements Cloneable {

	private Metaspace metaspace;

	public Metaspace getMetaspace() {
		return metaspace;
	}

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
	
	public void copyTo(MetaspaceMembers target) {
		super.copyTo(target);
		target.setMetaspace(metaspace);
	}

}
