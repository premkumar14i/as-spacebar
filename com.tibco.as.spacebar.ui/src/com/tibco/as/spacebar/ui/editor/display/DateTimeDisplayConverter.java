package com.tibco.as.spacebar.ui.editor.display;

import java.util.Date;
import java.util.TimeZone;

import org.eclipse.nebula.widgets.nattable.data.convert.DefaultDateDisplayConverter;

public class DateTimeDisplayConverter extends DefaultDateDisplayConverter {

	DateTimeConverter converter = new DateTimeConverter();

	public DateTimeDisplayConverter() {
		super();
	}

	public DateTimeDisplayConverter(String dateFormat) {
		super(dateFormat);
	}

	public DateTimeDisplayConverter(String dateFormat, TimeZone timeZone) {
		super(dateFormat, timeZone);
	}

	public DateTimeDisplayConverter(TimeZone timeZone) {
		super(timeZone);
	}

	@Override
	public Object canonicalToDisplayValue(Object canonicalValue) {
		return super.canonicalToDisplayValue(converter
				.dateTimeToDate(canonicalValue));
	}

	@Override
	public Object displayToCanonicalValue(Object displayValue) {
		return converter.dateToDateTime((Date) super
				.displayToCanonicalValue(displayValue));
	}

}
