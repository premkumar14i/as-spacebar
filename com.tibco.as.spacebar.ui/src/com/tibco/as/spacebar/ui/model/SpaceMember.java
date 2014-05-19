package com.tibco.as.spacebar.ui.model;

import com.tibco.as.space.Member.DistributionRole;

public class SpaceMember extends Member {

	private DistributionRole distributionRole;

	public SpaceMember(Members parent, com.tibco.as.space.Member member,
			DistributionRole distributionRole) {
		super(parent, member);
		this.distributionRole = distributionRole;
	}

	public DistributionRole getDistributionRole() {
		return distributionRole;
	}

}
