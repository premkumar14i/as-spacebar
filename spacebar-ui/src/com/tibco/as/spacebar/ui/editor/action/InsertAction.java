package com.tibco.as.spacebar.ui.editor.action;

import org.eclipse.swt.widgets.Event;

import com.tibco.as.spacebar.ui.Image;
import com.tibco.as.spacebar.ui.editor.AbstractBrowser;
import com.tibco.as.spacebar.ui.editor.snapshot.SnapshotBrowser;

public class InsertAction extends SpaceEditorAction {

	public InsertAction() {
		super("&Insert", "Insert row", Image.ADD);
	}

	@Override
	protected void runWithEvent(Event event, AbstractBrowser<?> editor) {
		((SnapshotBrowser) editor).insert();
	}

}
