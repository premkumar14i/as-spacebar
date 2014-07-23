package com.tibco.as.spacebar.ui.editor.display;

import java.text.Format;
import java.util.Date;
import java.util.TimeZone;

import org.eclipse.nebula.widgets.nattable.data.convert.ConversionFailedException;
import org.eclipse.nebula.widgets.nattable.data.convert.DisplayConverter;
import org.eclipse.nebula.widgets.nattable.util.ObjectUtils;
import org.eclipse.osgi.util.NLS;

import com.tibco.as.convert.ConverterFactory;
import com.tibco.as.space.DateTime;
import com.tibco.as.spacebar.ui.SpaceBarPlugin;

public class DateTimeDisplayConverter extends DisplayConverter {

	private Format format;

	public DateTimeDisplayConverter(String pattern, TimeZone timeZone) {
		this.format = ConverterFactory.getDateFormat(pattern, timeZone);
	}

	@Override
	public Object canonicalToDisplayValue(Object canonicalValue) {
		try {
			if (ObjectUtils.isNotNull(canonicalValue)) {
				DateTime dateTime = (DateTime) canonicalValue;
				return format.format(dateTime.getTime().getTime());
			}
		} catch (Exception e) {
			SpaceBarPlugin.logException(e);
		}
		return canonicalValue;
	}

	@Override
	public Object displayToCanonicalValue(Object displayValue) {
		try {
			Date date = (Date) format.parseObject(displayValue.toString());
			return DateTime.create(date.getTime());
		} catch (Exception e) {
			throw new ConversionFailedException(NLS.bind(
					"[{0}] is not a valid date", displayValue), e);
		}
	}

}
