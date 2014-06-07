package com.tibco.as.spacebar.ui.editor.snapshot;

import java.util.List;

import com.tibco.as.spacebar.ui.editor.StringPropertyAccessor;

import com.tibco.as.space.Tuple;

public class EditablePropertyAccessor extends StringPropertyAccessor<Tuple> {

	private SnapshotBrowser editor;

	public EditablePropertyAccessor(List<String> fieldNames, SnapshotBrowser editor) {
		super(fieldNames);
		this.editor = editor;
	}

	@Override
	public void setDataValue(Tuple rowObject, int columnIndex, Object newValue) {
		String fieldName = getColumnProperty(columnIndex);
		Object oldValue = rowObject.get(fieldName);
		boolean same = newValue == null ? oldValue == null : newValue
				.equals(oldValue);
		if (!same) {
			rowObject.put(fieldName, newValue);
			editor.addChanged(rowObject);
		}
	}

}
