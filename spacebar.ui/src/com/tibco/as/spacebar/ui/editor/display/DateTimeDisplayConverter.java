package com.tibco.as.spacebar.ui.editor.display;

import java.util.Calendar;
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
		Calendar calendar = converter.dateTimeToCalendar(canonicalValue);
		return super.canonicalToDisplayValue(calendar.getTime());
	}

	@Override
	public Object displayToCanonicalValue(Object displayValue) {
		Date date = (Date) super.displayToCanonicalValue(displayValue);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return converter.calendarToDateTime(calendar);
	}

}
