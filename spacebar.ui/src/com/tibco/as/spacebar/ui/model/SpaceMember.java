package com.tibco.as.spacebar.ui.model;

import com.tibco.as.space.Member.DistributionRole;

public class SpaceMember extends Member {

	private SpaceMembers members;
	private DistributionRole distributionRole;

	public SpaceMember(SpaceMembers members) {
		super(members);
		this.members = members;
	}

	public DistributionRole getDistributionRole() {
		return distributionRole;
	}

	public void setDistributionRole(DistributionRole distributionRole) {
		firePropertyChange("distributionRole", this.distributionRole,
				this.distributionRole = distributionRole);
	}
	
	@Override
	public Member clone() {
		SpaceMember member = new SpaceMember(members);
		copyTo(member);
		return member;
	}

	public void copyTo(SpaceMember target) {
		super.copyTo(target);
		target.setDistributionRole(distributionRole);
	}

}
