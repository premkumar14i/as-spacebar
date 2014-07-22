package com.tibco.as.spacebar.ui.model;

import com.tibco.as.space.Member.DistributionRole;

public class SpaceMember extends Member {

	private SpaceMembers members;
	private DistributionRole distributionRole;

	@Override
	public Members getParent() {
		return members;
	}

	public SpaceMembers getMembers() {
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

	public void copyTo(SpaceMember target) {
		super.copyTo(target);
		target.setMembers(members);
		target.setDistributionRole(distributionRole);
	}

	public boolean isSeeder() {
		return distributionRole == DistributionRole.SEEDER;
	}

}
