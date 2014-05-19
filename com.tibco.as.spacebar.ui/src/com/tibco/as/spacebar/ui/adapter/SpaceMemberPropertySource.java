package com.tibco.as.spacebar.ui.adapter;

import java.util.Arrays;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.tibco.as.spacebar.ui.model.SpaceMember;

public class SpaceMemberPropertySource extends MemberPropertySource {

	private static final String PROPERTY_ROLE = "distributionRole";

	private SpaceMember member;

	public SpaceMemberPropertySource(SpaceMember member) {
		super(member);
		this.member = member;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		IPropertyDescriptor[] array = super.getPropertyDescriptors();
		IPropertyDescriptor[] descriptors = Arrays.copyOf(array,
				array.length + 1);
		descriptors[descriptors.length - 1] = new PropertyDescriptor(
				PROPERTY_ROLE, "Distribution role");
		return descriptors;
	}

	@Override
	public Object getPropertyValue(Object name) {
		if (PROPERTY_ROLE.equals(name)) {
			return member.getDistributionRole();
		}
		return super.getPropertyValue(name);
	}

}