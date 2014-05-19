package com.tibco.as.spacebar.ui.editor.action;

import org.eclipse.swt.widgets.Event;

import com.tibco.as.spacebar.ui.Image;
import com.tibco.as.spacebar.ui.editor.AbstractSpaceEditor;
import com.tibco.as.spacebar.ui.editor.snapshot.SpaceEditor;

public class InsertAction extends SpaceEditorAction {

	public InsertAction() {
		super("&Insert", "Insert row", Image.ADD);
	}

	@Override
	protected void runWithEvent(Event event, AbstractSpaceEditor<?> editor) {
		((SpaceEditor) editor).insert();
	}

}
