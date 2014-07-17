package com.tibco.as.spacebar.ui.editor;

import java.util.Map;

import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;

public class StringPropertyAccessor<T extends Map<String, Object>> implements
		IColumnPropertyAccessor<T> {

	private String[] propertyNames;

	public StringPropertyAccessor(String[] fieldNames) {
		this.propertyNames = fieldNames;
	}

	@Override
	public Object getDataValue(T rowObject, int columnIndex) {
		return rowObject.get(getColumnProperty(columnIndex));
	}

	@Override
	public void setDataValue(T rowObject, int columnIndex, Object newValue) {
		// do nothing
	}

	@Override
	public int getColumnCount() {
		return propertyNames.length;
	}

	@Override
	public String getColumnProperty(int columnIndex) {
		return propertyNames[columnIndex];
	}

	@Override
	public int getColumnIndex(String propertyName) {
		for (int index = 0; index < propertyNames.length; index++) {
			if (propertyNames[index].equals(propertyName)) {
				return index;
			}
		}
		return -1;
	}
}