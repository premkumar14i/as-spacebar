package com.tibco.as.spacebar.ui.editor;

import org.eclipse.nebula.widgets.nattable.data.convert.ConversionFailedException;
import org.eclipse.nebula.widgets.nattable.data.convert.DisplayConverter;
import org.eclipse.osgi.util.NLS;

import com.tibco.as.convert.Attributes;
import com.tibco.as.convert.ConverterFactory;
import com.tibco.as.convert.IConverter;
import com.tibco.as.convert.UnsupportedConversionException;

/**
 * Converts a byte array to a string and vice versa
 */
public class FieldDisplayConverter<T> extends DisplayConverter {

	private IConverter<T, String> formatter;
	private IConverter<String, T> parser;
	private String typeName;

	private FieldDisplayConverter(String typeName,
			IConverter<T, String> formatter, IConverter<String, T> parser) {
		this.typeName = typeName;
		this.formatter = formatter;
		this.parser = parser;
	}

	@SuppressWarnings("unchecked")
	public static <T> FieldDisplayConverter<T> create(Class<T> clazz,
			String typeName, Attributes attributes)
			throws UnsupportedConversionException {
		ConverterFactory factory = new ConverterFactory();
		factory.setAttributes(attributes);
		return new FieldDisplayConverter<T>(typeName, factory.getConverter(
				clazz, String.class), factory.getConverter(String.class, clazz));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object canonicalToDisplayValue(Object canonicalValue) {
		try {
			return formatter.convert((T) canonicalValue);
		} catch (Exception e) {
			return canonicalValue;
		}
	}

	@Override
	public Object displayToCanonicalValue(Object displayValue) {
		String string = displayValue.toString();
		try {
			return parser.convert(string);
		} catch (Exception e) {
			throw new ConversionFailedException(NLS.bind(
					"[{0}] is not a valid {1}", string, typeName), e);
		}
	}

}
