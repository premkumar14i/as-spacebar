package com.tibco.as.spacebar.ui.editor.display;

import static org.eclipse.nebula.widgets.nattable.util.ObjectUtils.isNotEmpty;
import static org.eclipse.nebula.widgets.nattable.util.ObjectUtils.isNotNull;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.eclipse.nebula.widgets.nattable.Messages;
import org.eclipse.nebula.widgets.nattable.data.convert.ConversionFailedException;
import org.eclipse.nebula.widgets.nattable.data.convert.DisplayConverter;

import com.tibco.as.convert.ConverterFactory;

public class DateTimeFilterDisplayConverter extends DisplayConverter {

	private DateTimeConverter converter = new DateTimeConverter();

	private SimpleDateFormat format;

	public DateTimeFilterDisplayConverter(TimeZone timeZone) {
		format = new SimpleDateFormat(ConverterFactory.DEFAULT_PATTERN_DATE);
		format.setTimeZone(timeZone);
	}

	public Object canonicalToDisplayValue(Object canonicalValue) {
		try {
			if (isNotNull(canonicalValue)) {
				return format.format(converter.dateTimeToDate(canonicalValue));
			}
			return null;
		} catch (Exception e) {
			return canonicalValue;
		}
	}

	public Object displayToCanonicalValue(Object displayValue) {
		try {
			if (isNotNull(displayValue) && isNotEmpty(displayValue.toString())) {
				return converter.dateToDateTime(format.parse(
						displayValue.toString(), new ParsePosition(0)));
			}
			return null;
		} catch (Exception e) {
			throw new ConversionFailedException(Messages.getString(
					"DefaultDateDisplayConverter.failure", //$NON-NLS-1$
					new Object[] { displayValue, format.toPattern() }), e);
		}
	}

}
