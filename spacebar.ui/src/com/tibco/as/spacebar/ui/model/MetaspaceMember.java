package com.tibco.as.spacebar.ui.model;

public class MetaspaceMember extends Member {

	private MetaspaceMembers members;

	@Override
	public MetaspaceMembers getParent() {
		return members;
	}

	public void setMembers(MetaspaceMembers members) {
		this.members = members;
	}

	@Override
	public Member clone() {
		MetaspaceMember member = new MetaspaceMember();
		copyTo(member);
		return member;
	}

	@Override
	public void copyTo(IElement element) {
		super.copyTo(element);
		((MetaspaceMember) element).setMembers(members);
	}

}
