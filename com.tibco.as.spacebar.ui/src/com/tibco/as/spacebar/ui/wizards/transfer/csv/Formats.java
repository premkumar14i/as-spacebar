package com.tibco.as.spacebar.ui.wizards.transfer.csv;

import com.tibco.as.convert.Attribute;
import com.tibco.as.convert.Attributes;
import com.tibco.as.convert.ConverterFactory.Blob;

public class Formats {

	private Attributes conversion;
	
	public Formats() {
		this(new Attributes());
	}

	public Formats(Attributes conversion) {
		this.conversion = conversion;
	}

	public Blob getBlobFormat() {
		return conversion.get(Attribute.FORMAT_BLOB);
	}

	public void setBlobFormat(Blob blobFormat) {
		conversion.put(Attribute.FORMAT_BLOB, blobFormat);
	}

	public String getBooleanFormat() {
		return conversion.get(Attribute.FORMAT_BOOLEAN);
	}

	public void setBooleanFormat(String booleanFormat) {
		conversion.put(Attribute.FORMAT_BOOLEAN, booleanFormat);
	}

	public String getDateFormat() {
		return conversion.get(Attribute.FORMAT_DATE);
	}

	public void setDateFormat(String dateFormat) {
		conversion.put(Attribute.FORMAT_DATE, dateFormat);
	}

	public String getNumberFormat() {
		return conversion.get(Attribute.FORMAT_NUMBER);
	}

	public void setNumberFormat(String numberFormat) {
		conversion.put(Attribute.FORMAT_NUMBER, numberFormat);
	}

}
