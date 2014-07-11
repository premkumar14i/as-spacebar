package com.tibco.as.spacebar.ui.editor.action;

import org.eclipse.nebula.widgets.nattable.layer.event.ILayerEvent;
import org.eclipse.nebula.widgets.nattable.selection.event.ISelectionEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.tibco.as.spacebar.ui.editor.AbstractBrowser;

public class CopyAction extends LayerListenerEditorAction {

	public CopyAction() {
		super("&Copy", "Copy to Clipboard", PlatformUI.getWorkbench()
				.getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
		setDisabledImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_TOOL_COPY_DISABLED));
		setEnabled(false);
	}

	@Override
	protected void runWithEvent(Event event, AbstractBrowser<?> editor) {
		editor.copy(event);
	}

	@Override
	public void handleLayerEvent(ILayerEvent event) {
		if (event instanceof ISelectionEvent) {
			setEnabled(((ISelectionEvent) event).getSelectionLayer()
					.getSelectedCellPositions().length > 0);
		}
	}

}
