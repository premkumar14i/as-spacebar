package com.tibco.as.spacebar.ui.editor.display;

import org.eclipse.nebula.widgets.nattable.data.convert.DisplayConverter;

public class StringDisplayConverter extends DisplayConverter {

	public Object canonicalToDisplayValue(Object sourceValue) {
		if (sourceValue == null) {
			return "";
		}
		return sourceValue;
	}

	public Object displayToCanonicalValue(Object destinationValue) {
		if (destinationValue == null
				|| ((String) destinationValue).length() == 0) {
			return null;
		}
		return destinationValue;
	}
}
