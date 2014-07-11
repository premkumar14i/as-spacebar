package com.tibco.as.spacebar.ui.adapter;

import java.lang.reflect.Method;
import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.tibco.as.util.Property;

import com.tibco.as.space.SpaceDef;

public class SpacePropertyGroup implements IPropertySource {

	private SpaceDef spaceDef;
	private List<Property> properties;

	public SpacePropertyGroup(SpaceDef spaceDef, List<Property> properties) {
		this.spaceDef = spaceDef;
		this.properties = properties;
	}

	private Method getReadMethod(String propertyName) throws SecurityException,
			NoSuchMethodException {
		try {
			return SpaceDef.class.getMethod("is" + propertyName);
		} catch (NoSuchMethodException e) {
			return SpaceDef.class.getMethod("get" + propertyName);
		}
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		IPropertyDescriptor[] descriptors = new IPropertyDescriptor[properties
				.size()];
		for (int index = 0; index < descriptors.length; index++) {
			Property property = properties.get(index);
			descriptors[index] = new PropertyDescriptor(property.getId(),
					property.getDisplayName().getValue());
		}
		return descriptors;
	}

	@Override
	public Object getPropertyValue(Object id) {
		try {
			return getReadMethod((String) id).invoke(spaceDef);
		} catch (Exception e) {
			return null;
		}
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
