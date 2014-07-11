package com.tibco.as.spacebar.ui.editor.continuous.coder;

public class BlobHashCoder extends FieldHashCoder {

	public BlobHashCoder(String fieldName) {
		super(fieldName);
	}

	@Override
	public int getHashCode(Object object) {
		int result = 1;
		for (byte element : (byte[]) object) {
			result = 31 * result + element;
		}
		return result;
	}
}
