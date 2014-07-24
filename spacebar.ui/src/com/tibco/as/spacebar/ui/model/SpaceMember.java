package com.tibco.as.spacebar.ui.model;

import com.tibco.as.space.Member.DistributionRole;

public class SpaceMember extends Member {

	private SpaceMembers members;
	private DistributionRole distributionRole;

	@Override
	public SpaceMembers getParent() {
		return members;
	}

	public void setMembers(SpaceMembers members) {
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
		SpaceMember member = new SpaceMember();
		copyTo(member);
		return member;
	}

	@Override
	public void copyTo(IElement element) {
		super.copyTo(element);
		SpaceMember target = (SpaceMember) element;
		target.setMembers(members);
		target.setDistributionRole(distributionRole);
	}

	public boolean isSeeder() {
		return distributionRole == DistributionRole.SEEDER;
	}

}
