package com.tibco.as.spacebar.ui.editor.display;

import java.util.Calendar;

import com.tibco.as.space.DateTime;

public class DateTimeConverter {

	public Object calendarToDateTime(Calendar calendar) {
		if (calendar == null) {
			return null;
		}
		return DateTime.create(calendar);
	}

	public Calendar dateTimeToCalendar(Object dateTime) {
		if (dateTime == null) {
			return null;
		}
		return ((DateTime) dateTime).getTime();
	}
}
