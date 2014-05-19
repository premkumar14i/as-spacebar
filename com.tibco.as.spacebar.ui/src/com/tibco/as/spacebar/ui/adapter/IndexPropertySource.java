package com.tibco.as.spacebar.ui.adapter;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.tibco.as.spacebar.ui.model.Index;

public class IndexPropertySource implements IPropertySource {

	private static final String PROPERTY_NAME = "name";
	private static final String PROPERTY_TYPE = "type";

	private Index index;

	public IndexPropertySource(Index index) {
		this.index = index;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return new IPropertyDescriptor[] {
				new PropertyDescriptor(PROPERTY_NAME, "Name"),
				new PropertyDescriptor(PROPERTY_TYPE, "Type") };
	}

	@Override
	public Object getPropertyValue(Object name) {
		if (PROPERTY_NAME.equals(name)) {
			return index.getName();
		}
		if (PROPERTY_TYPE.equals(name)) {
			return index.getType();
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
