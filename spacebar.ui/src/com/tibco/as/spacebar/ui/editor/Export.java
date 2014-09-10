package com.tibco.as.spacebar.ui.editor;

import com.tibco.as.io.AbstractExport;

public class Export extends AbstractExport {

	@Override
	public Export clone() {
		Export export = new Export();
		copyTo(export);
		return export;
	}

}
