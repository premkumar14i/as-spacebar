package com.tibco.as.spacebar.ui.model;

public class MetaspaceMembers extends Members {

	private Metaspace metaspace;

	public MetaspaceMembers() {
	}

	public MetaspaceMembers(Metaspace metaspace) {
		this.metaspace = metaspace;
	}

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

}
