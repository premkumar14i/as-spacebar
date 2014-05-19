package com.tibco.as.spacebar.ui.editor.display;

import static org.eclipse.nebula.widgets.nattable.util.ObjectUtils.isNotEmpty;
import static org.eclipse.nebula.widgets.nattable.util.ObjectUtils.isNotNull;

import java.text.Format;

import org.eclipse.nebula.widgets.nattable.Messages;
import org.eclipse.nebula.widgets.nattable.data.convert.ConversionFailedException;
import org.eclipse.nebula.widgets.nattable.data.convert.DisplayConverter;

/**
 * Converts the display value to a number and vice versa.
 */
public class FormatDisplayConverter extends DisplayConverter {

	private Format format;

	public FormatDisplayConverter(Format format) {
		this.format = format;
	}

	@Override
	public Object canonicalToDisplayValue(Object canonicalValue) {
		try {
			if (isNotNull(canonicalValue)) {
				return format.format(canonicalValue);
			}
			return null;
		} catch (Exception e) {
			return canonicalValue;
		}
	}

	@Override
	public Object displayToCanonicalValue(Object displayValue) {
		try {
			if (isNotNull(displayValue) && isNotEmpty(displayValue.toString())) {
				return format.parseObject(displayValue.toString());
			}
			return null;
		} catch (Exception e) {
			throw new ConversionFailedException(Messages.getString(
					"FormatDisplayConverter.failure", //$NON-NLS-1$
					new Object[] { displayValue }), e);
		}
	}

}
