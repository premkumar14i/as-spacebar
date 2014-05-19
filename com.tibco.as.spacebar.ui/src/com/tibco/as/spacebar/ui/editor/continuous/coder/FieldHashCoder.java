package com.tibco.as.spacebar.ui.editor.continuous.coder;

public class FieldHashCoder implements IFieldHashCoder {

	private String fieldName;

	public FieldHashCoder(String fieldName) {
		this.fieldName = fieldName;
	}

	@Override
	public int getHashCode(Object object) {
		return object.hashCode();
	}

	@Override
	public String getFieldName() {
		return fieldName;
	}

}
