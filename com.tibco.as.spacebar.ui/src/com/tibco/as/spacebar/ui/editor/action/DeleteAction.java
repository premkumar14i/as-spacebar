package com.tibco.as.spacebar.ui.editor.action;

import org.eclipse.nebula.widgets.nattable.layer.event.ILayerEvent;
import org.eclipse.nebula.widgets.nattable.selection.event.ISelectionEvent;
import org.eclipse.swt.widgets.Event;

import com.tibco.as.spacebar.ui.Image;
import com.tibco.as.spacebar.ui.editor.AbstractSpaceEditor;
import com.tibco.as.spacebar.ui.editor.snapshot.SpaceEditor;

public class DeleteAction extends LayerListenerEditorAction {

	public DeleteAction() {
		super("&Delete", "Delete rows", Image.DELETE);
		setEnabled(false);
	}

	@Override
	protected void runWithEvent(Event event, AbstractSpaceEditor<?> editor) {
		((SpaceEditor) editor).deleteSelected();
	}

	@Override
	public void handleLayerEvent(ILayerEvent event) {
		if (event instanceof ISelectionEvent) {
			setEnabled(((ISelectionEvent) event).getSelectionLayer()
					.getFullySelectedRowPositions().length > 0);
		}
	}

}
