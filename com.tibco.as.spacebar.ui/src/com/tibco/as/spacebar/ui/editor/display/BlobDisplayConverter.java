package com.tibco.as.spacebar.ui.editor.display;

import javax.xml.bind.DatatypeConverter;

import org.eclipse.nebula.widgets.nattable.data.convert.DisplayConverter;
import org.eclipse.nebula.widgets.nattable.util.ObjectUtils;

/**
 * Converts a byte array to a string and vice versa
 */
public class BlobDisplayConverter extends DisplayConverter {

	@Override
	public Object canonicalToDisplayValue(Object canonicalValue) {
		if (ObjectUtils.isNotNull(canonicalValue)) {
			return DatatypeConverter.printHexBinary((byte[]) canonicalValue);
		}
		return canonicalValue;
	}

	@Override
	public Object displayToCanonicalValue(Object displayValue) {
		return DatatypeConverter.parseHexBinary(displayValue.toString());
	}

}
