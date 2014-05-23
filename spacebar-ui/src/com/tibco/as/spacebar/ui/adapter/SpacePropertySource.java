package com.tibco.as.spacebar.ui.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.tibco.as.spacebar.core.Property;
import com.tibco.as.spacebar.ui.SpaceBarPlugin;
import com.tibco.as.spacebar.ui.model.Space;

public class SpacePropertySource implements IPropertySource {

	private Space space;
	private Map<Property, List<Property>> properties;

	public SpacePropertySource(Space space) {
		this.space = space;
		this.properties = SpaceBarPlugin.getDefault()
				.getSpaceDefProperties();
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		List<IPropertyDescriptor> descriptors = new ArrayList<IPropertyDescriptor>();
		for (Property label : properties.keySet()) {
			String displayName = label.getDisplayName().getValue();
			descriptors.add(new PropertyDescriptor(label, displayName));
		}
		return descriptors.toArray(new IPropertyDescriptor[descriptors.size()]);
	}

	@Override
	public Object getPropertyValue(Object name) {
		return new SpacePropertyGroup(space.getSpaceDef(), properties.get(name));
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
