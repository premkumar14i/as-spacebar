package com.tibco.as.spacebar.ui.editor.continuous.coder;

import java.util.Collection;
import java.util.Map;

public class TupleHashCoder implements IHashCoder {

	private Collection<IFieldHashCoder> coders;

	public TupleHashCoder(Collection<IFieldHashCoder> coders) {
		this.coders = coders;
	}

	@Override
	public int getHashCode(Object object) {
		int result = 1;
		@SuppressWarnings("unchecked")
		Map<String, Object> rowObject = (Map<String, Object>) object;
		for (IFieldHashCoder coder : coders) {
			Object element = rowObject.get(coder.getFieldName());
			if (element == null) {
				result = 31 * result;
			} else {
				result = 31 * result + coder.getHashCode(element);
			}
		}
		return result;
	}

}
