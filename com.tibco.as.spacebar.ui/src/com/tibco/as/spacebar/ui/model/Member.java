package com.tibco.as.spacebar.ui.model;

public class Member extends Element implements Cloneable {

	private com.tibco.as.space.Member member;

	private boolean self;

	public Member(Members parent, com.tibco.as.space.Member member) {
		super(parent, member.getName());
		this.member = member;
	}

	public boolean isSelf() {
		return self;
	}

	public void setSelf(boolean self) {
		firePropertyChange("self", this.self, this.self = self);
	}

	@Override
	public Member clone() {
		Member member = new Member(getParent(), this.member);
		copyTo(member);
		return member;
	}

	@Override
	public Members getParent() {
		return (Members) super.getParent();
	}

	public void copyTo(Member member) {
		super.copyTo(member);
		member.member = this.member;
		member.self = this.self;
	}

	public com.tibco.as.space.Member getMember() {
		return member;
	}

	public void setMember(com.tibco.as.space.Member member) {
		firePropertyChange("member", this.member, this.member = member);
	}

}
