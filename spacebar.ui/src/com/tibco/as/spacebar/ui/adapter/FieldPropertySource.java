package com.tibco.as.spacebar.ui.adapter;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.tibco.as.spacebar.ui.model.Field;

public class FieldPropertySource implements IPropertySource {

	private static final String PROPERTY_NAME = "name";
	private static final String PROPERTY_TYPE = "type";
	private static final String PROPERTY_NULLABLE = "nullable";
	private static final String PROPERTY_ENCRYPTED = "encrytped";
	private static final String PROPERTY_KEY = "key";
	private static final String PROPERTY_DISTRIBUTION = "distribution";

	private Field field;

	public FieldPropertySource(Field field) {
		this.field = field;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return new IPropertyDescriptor[] {
				new PropertyDescriptor(PROPERTY_NAME, "Name"),
				new PropertyDescriptor(PROPERTY_TYPE, "Type"),
				new PropertyDescriptor(PROPERTY_NULLABLE, "Nullable"),
				new PropertyDescriptor(PROPERTY_ENCRYPTED, "Encrypted"),
				new PropertyDescriptor(PROPERTY_KEY, "Key"),
				new PropertyDescriptor(PROPERTY_DISTRIBUTION, "Distribution") };
	}

	@Override
	public Object getPropertyValue(Object name) {
		if (PROPERTY_NAME.equals(name)) {
			return field.getName();
		}
		if (PROPERTY_TYPE.equals(name)) {
			return field.getType();
		}
		if (PROPERTY_NULLABLE.equals(name)) {
			return field.isNullable();
		}
		if (PROPERTY_ENCRYPTED.equals(name)) {
			return field.isEncrypted();
		}
		if (PROPERTY_KEY.equals(name)) {
			return field.isKey();
		}
		if (PROPERTY_DISTRIBUTION.equals(name)) {
			return field.isDistribution();
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
