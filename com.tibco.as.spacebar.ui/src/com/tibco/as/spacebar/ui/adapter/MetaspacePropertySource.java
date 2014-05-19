package com.tibco.as.spacebar.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.tibco.as.spacebar.ui.model.Metaspace;

public class MetaspacePropertySource implements IPropertySource {

	private static final String PROPERTY_NAME = "name";
	private static final String PROPERTY_STATE = "state";
	private static final String PROPERTY_METASPACE = "metaspace";
	private static final String PROPERTY_MEMBER = "member";
	private static final String PROPERTY_DISCOVERY = "discovery";
	private static final String PROPERTY_LISTEN = "listen";
	private static final String PROPERTY_REMOTE = "remote";
	private static final String PROPERTY_AUTOCONNECT = "autoconnect";
	private static final String PROPERTY_TIMEOUT = "timeout";
	private Metaspace metaspace;

	public MetaspacePropertySource(Metaspace metaspace) {
		this.metaspace = metaspace;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		List<IPropertyDescriptor> descriptors = new ArrayList<IPropertyDescriptor>();
		descriptors.add(new PropertyDescriptor(PROPERTY_NAME, "Name"));
		descriptors
				.add(new PropertyDescriptor(PROPERTY_METASPACE, "Metaspace"));
		descriptors.add(new PropertyDescriptor(PROPERTY_MEMBER, "Member"));
		descriptors
				.add(new PropertyDescriptor(PROPERTY_DISCOVERY, "Discovery"));
		descriptors.add(new PropertyDescriptor(PROPERTY_LISTEN, "Listen"));
		descriptors.add(new PropertyDescriptor(PROPERTY_STATE, "State"));
		descriptors.add(new PropertyDescriptor(PROPERTY_REMOTE, "Remote"));
		descriptors.add(new PropertyDescriptor(PROPERTY_TIMEOUT, "Timeout"));
		descriptors.add(new PropertyDescriptor(PROPERTY_AUTOCONNECT,
				"Auto-connect"));
		return descriptors.toArray(new IPropertyDescriptor[descriptors.size()]);
	}

	@Override
	public Object getPropertyValue(Object name) {
		if (PROPERTY_NAME.equals(name)) {
			return metaspace.getName();
		}
		if (PROPERTY_DISCOVERY.equals(name)) {
			return metaspace.getDiscovery();
		}
		if (PROPERTY_LISTEN.equals(name)) {
			return metaspace.getListen();
		}
		if (PROPERTY_STATE.equals(name)) {
			return metaspace.getState();
		}
		if (PROPERTY_MEMBER.equals(name)) {
			return metaspace.getMemberName();
		}
		if (PROPERTY_METASPACE.equals(name)) {
			return metaspace.getMetaspaceName();
		}
		if (PROPERTY_TIMEOUT.equals(name)) {
			return metaspace.getTimeout();
		}
		if (PROPERTY_REMOTE.equals(name)) {
			return metaspace.isRemote();
		}
		if (PROPERTY_AUTOCONNECT.equals(name)) {
			return metaspace.isAutoconnect();
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
