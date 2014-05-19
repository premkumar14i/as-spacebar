package com.tibco.as.spacebar.ui.editor.display;

import java.util.Calendar;
import java.util.Date;

import com.tibco.as.space.DateTime;

public class DateTimeConverter {

	public Object dateToDateTime(Date date) {
		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return DateTime.create(calendar);
	}

	public Date dateTimeToDate(Object dateTime) {
		if (dateTime == null) {
			return null;
		}
		return ((DateTime) dateTime).getTime().getTime();
	}
}
