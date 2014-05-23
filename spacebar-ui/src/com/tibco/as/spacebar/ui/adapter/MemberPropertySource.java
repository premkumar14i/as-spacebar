package com.tibco.as.spacebar.ui.adapter;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.tibco.as.spacebar.ui.model.Member;

public class MemberPropertySource implements IPropertySource {

	private static final String PROPERTY_NAME = "name";
	private static final String PROPERTY_HOST_ADDRESS = "hostAddress";
	private static final String PROPERTY_ID = "id";
	private static final String PROPERTY_JOIN_TIME = "joinTime";
	private static final String PROPERTY_MANAGEMENT_ROLE = "managementRole";
	private static final String PROPERTY_PORT = "port";
	private Member member;

	public MemberPropertySource(Member member) {
		this.member = member;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return new IPropertyDescriptor[] {
				new PropertyDescriptor(PROPERTY_NAME, "Name"),
				new PropertyDescriptor(PROPERTY_HOST_ADDRESS, "Host address"),
				new PropertyDescriptor(PROPERTY_ID, "ID"),
				new PropertyDescriptor(PROPERTY_JOIN_TIME, "Join time"),
				new PropertyDescriptor(PROPERTY_MANAGEMENT_ROLE,
						"Management role"),
				new PropertyDescriptor(PROPERTY_PORT, "Port") };
	}

	@Override
	public Object getPropertyValue(Object name) {
		com.tibco.as.space.Member m = member.getMember();
		if (PROPERTY_NAME.equals(name)) {
			return m.getName();
		}
		if (PROPERTY_HOST_ADDRESS.equals(name)) {
			return m.getHostAddress();
		}
		if (PROPERTY_ID.equals(name)) {
			return m.getId();
		}
		if (PROPERTY_JOIN_TIME.equals(name)) {
			return m.getJoinTime().getTime().getTime();
		}
		if (PROPERTY_MANAGEMENT_ROLE.equals(name)) {
			return m.getManagementRole();
		}
		if (PROPERTY_PORT.equals(name)) {
			return m.getPort();
		}
		return null;
	}

	@Override
	public Object getEditableValue() {
		return null;
	}

	@Override
	public boolean isPropertySet(Object id) {
		return false;
	}

	@Override
	public void resetPropertyValue(Object id) {
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
	}
}
