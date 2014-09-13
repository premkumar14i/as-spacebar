package com.tibco.as.spacebar.ui.editor;

import com.tibco.as.io.AbstractExport;

public class SpaceEditorExport extends AbstractExport {

	@Override
	public SpaceEditorExport clone() {
		SpaceEditorExport export = new SpaceEditorExport();
		copyTo(export);
		return export;
	}

}
