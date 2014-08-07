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
		return conversion.get(Attribute.BLOB);
	}

	public void setBlobFormat(Blob blobFormat) {
		conversion.put(Attribute.BLOB, blobFormat);
	}

	public String getBooleanFormat() {
		return conversion.get(Attribute.BOOLEAN);
	}

	public void setBooleanFormat(String booleanFormat) {
		conversion.put(Attribute.BOOLEAN, booleanFormat);
	}

	public String getDateFormat() {
		return conversion.get(Attribute.DATE);
	}

	public void setDateFormat(String dateFormat) {
		conversion.put(Attribute.DATE, dateFormat);
	}

	public String getIntegerFormat() {
		return conversion.get(Attribute.INTEGER);
	}

	public void setIntegerFormat(String format) {
		conversion.put(Attribute.INTEGER, format);
	}
	
	public String getDecimalFormat() {
		return conversion.get(Attribute.DECIMAL);
	}

	public void setDecimalFormat(String format) {
		conversion.put(Attribute.DECIMAL, format);
	}


}
