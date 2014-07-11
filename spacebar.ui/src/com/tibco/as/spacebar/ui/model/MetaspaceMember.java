package com.tibco.as.spacebar.ui.model;

public class MetaspaceMember extends Member {

	private MetaspaceMembers members;

	public MetaspaceMember(MetaspaceMembers members) {
		super(members);
		this.members = members;
	}

	@Override
	public MetaspaceMember clone() {
		MetaspaceMember member = new MetaspaceMember(members);
		copyTo(member);
		return member;
	}

}
