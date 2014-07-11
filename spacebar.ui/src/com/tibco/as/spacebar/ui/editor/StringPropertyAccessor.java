package com.tibco.as.spacebar.ui.editor;

import java.util.List;
import java.util.Map;

import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;

public class StringPropertyAccessor<T extends Map<String, Object>> implements
		IColumnPropertyAccessor<T> {

	private List<String> propertyNames;

	public StringPropertyAccessor(List<String> fieldNames) {
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
		return propertyNames.size();
	}

	@Override
	public String getColumnProperty(int columnIndex) {
		return propertyNames.get(columnIndex);
	}

	@Override
	public int getColumnIndex(String propertyName) {
		return propertyNames.indexOf(propertyName);
	}
}