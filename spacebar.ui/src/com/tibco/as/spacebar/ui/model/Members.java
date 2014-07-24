package com.tibco.as.spacebar.ui.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Members extends AbstractElement {

	private List<Member> members = new ArrayList<Member>();

	@Override
	public void copyTo(IElement element) {
		Members target = (Members) element;
		target.setMembers(new ArrayList<Member>(members));
	}

	@Override
	protected boolean add(IElement child) {
		return members.add((Member) child);
	}

	@Override
	protected boolean remove(IElement child) {
		return members.remove(child);
	}

	@Override
	protected void copyChild(IElement source, IElement target) {
		((Member) source).copyTo((Member) target);
	}

	@Override
	public List<Member> getChildren() {
		return members;
	}

	@Override
	public String getName() {
		return "Members";
	}

	public void copyTo(Members clone) {
		clone.setMembers(new ArrayList<Member>(members));
	}

	public void setMembers(List<Member> members) {
		this.members = members;
	}

	public Member getMemberById(String id) {
		for (Member member : getChildren()) {
			if (member.getId().equals(id)) {
				return member;
			}
		}
		return null;
	}

}
