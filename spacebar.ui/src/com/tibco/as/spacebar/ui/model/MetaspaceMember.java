package com.tibco.as.spacebar.ui.model;

public class MetaspaceMember extends Member {

	private MetaspaceMembers members;

	@Override
	public Members getParent() {
		return members;
	}

	public MetaspaceMembers getMembers() {
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

	public void copyTo(MetaspaceMember target) {
		super.copyTo(target);
		target.setMembers(members);
	}

}
