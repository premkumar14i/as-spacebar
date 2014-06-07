package com.tibco.as.spacebar.ui.editor.action;

import java.util.List;

import org.eclipse.swt.widgets.Event;

import com.tibco.as.spacebar.ui.Image;
import com.tibco.as.spacebar.ui.editor.AbstractBrowser;
import com.tibco.as.spacebar.ui.editor.snapshot.SnapshotBrowser;

import com.tibco.as.space.Tuple;

public class TupleSizeAction extends SpaceEditorAction {

	public TupleSizeAction() {
		super("&Tuple Size", "Calculate average tuple size", Image.RULER);
	}

	@Override
	protected void runWithEvent(Event event, AbstractBrowser<?> editor) {
		SnapshotBrowser spaceEditor = (SnapshotBrowser) editor;
		List<Tuple> tuples = spaceEditor.getSelection();
		if (tuples.isEmpty()) {
			tuples = spaceEditor.getSortedList();
		}
		new TupleSizeDialog(spaceEditor.getSite().getShell(), tuples).open();
	}

}
