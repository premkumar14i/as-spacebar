package com.tibco.as.spacebar.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.tibco.as.spacebar.ui.model.Metaspace;

public class MetaspacePropertySource implements IPropertySource {

	private Metaspace metaspace;

	public MetaspacePropertySource(Metaspace metaspace) {
		this.metaspace = metaspace;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		List<IPropertyDescriptor> descriptors = new ArrayList<IPropertyDescriptor>();
		descriptors
				.add(new PropertyDescriptor(Metaspace.PROPERTY_NAME, "Name"));
		descriptors.add(new PropertyDescriptor(Metaspace.PROPERTY_METASPACE,
				"Metaspace"));
		descriptors.add(new PropertyDescriptor(Metaspace.PROPERTY_MEMBER,
				"Member"));
		descriptors.add(new PropertyDescriptor(Metaspace.PROPERTY_DISCOVERY,
				"Discovery"));
		descriptors.add(new PropertyDescriptor(Metaspace.PROPERTY_LISTEN,
				"Listen"));
		descriptors.add(new PropertyDescriptor(Metaspace.PROPERTY_REMOTE,
				"Remote"));
		descriptors.add(new PropertyDescriptor(Metaspace.PROPERTY_TIMEOUT,
				"Timeout"));
		descriptors.add(new PropertyDescriptor(
				Metaspace.PROPERTY_SECURITY_TOKEN_FILE, "Security token file"));
		descriptors.add(new PropertyDescriptor(Metaspace.PROPERTY_AUTOCONNECT,
				"Auto-connect"));
		return descriptors.toArray(new IPropertyDescriptor[descriptors.size()]);
	}

	@Override
	public Object getPropertyValue(Object name) {
		if (Metaspace.PROPERTY_NAME.equals(name)) {
			return metaspace.getName();
		}
		if (Metaspace.PROPERTY_DISCOVERY.equals(name)) {
			return metaspace.getDiscovery();
		}
		if (Metaspace.PROPERTY_LISTEN.equals(name)) {
			return metaspace.getListen();
		}
		if (Metaspace.PROPERTY_MEMBER.equals(name)) {
			return metaspace.getMemberName();
		}
		if (Metaspace.PROPERTY_METASPACE.equals(name)) {
			return metaspace.getMetaspaceName();
		}
		if (Metaspace.PROPERTY_TIMEOUT.equals(name)) {
			return metaspace.getTimeout();
		}
		if (Metaspace.PROPERTY_SECURITY_TOKEN_FILE.equals(name)) {
			return metaspace.getSecurityTokenFile();
		}
		if (Metaspace.PROPERTY_REMOTE.equals(name)) {
			return metaspace.isRemote();
		}
		if (Metaspace.PROPERTY_AUTOCONNECT.equals(name)) {
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
