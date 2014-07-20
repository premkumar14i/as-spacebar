package com.tibco.as.spacebar.ui.editor.display;

import static org.eclipse.nebula.widgets.nattable.util.ObjectUtils.isNotEmpty;
import static org.eclipse.nebula.widgets.nattable.util.ObjectUtils.isNotNull;

import java.util.Calendar;
import java.util.TimeZone;

import javax.xml.bind.DatatypeConverter;

import org.eclipse.nebula.widgets.nattable.data.convert.ConversionFailedException;
import org.eclipse.nebula.widgets.nattable.data.convert.DisplayConverter;

public class DateTimeFilterDisplayConverter extends DisplayConverter {

	private DateTimeConverter converter = new DateTimeConverter();

	private TimeZone timeZone;

	public DateTimeFilterDisplayConverter(TimeZone timeZone) {
		this.timeZone = timeZone;
	}

	public Object canonicalToDisplayValue(Object canonicalValue) {
		if (isNotNull(canonicalValue)) {
			try {
				return DatatypeConverter.printDateTime(converter
						.dateTimeToCalendar(canonicalValue));
			} catch (Exception e) {
				return canonicalValue;
			}
		}
		return null;
	}

	public Object displayToCanonicalValue(Object displayValue) {
		if (isNotNull(displayValue) && isNotEmpty(displayValue.toString())) {
			try {
				Calendar calendar = DatatypeConverter
						.parseDateTime(displayValue.toString());
				if (calendar.getTimeZone() == null) {
					calendar.setTimeZone(timeZone);
				}
				return converter.calendarToDateTime(calendar);
			} catch (Exception e) {
				throw new ConversionFailedException(displayValue.toString(), e);
			}
		}
		return null;
	}

}
