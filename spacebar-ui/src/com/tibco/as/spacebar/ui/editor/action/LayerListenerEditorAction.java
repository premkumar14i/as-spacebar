package com.tibco.as.spacebar.ui.editor.action;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.nebula.widgets.nattable.layer.ILayerListener;

import com.tibco.as.spacebar.ui.Image;
import com.tibco.as.spacebar.ui.editor.AbstractBrowser;

public abstract class LayerListenerEditorAction extends SpaceEditorAction implements
		ILayerListener {

	public LayerListenerEditorAction(String text, String toolTip, Image image) {
		super(text, toolTip, image);
	}

	public LayerListenerEditorAction(String text, String toolTip,
			ImageDescriptor imageDescriptor) {
		super(text, toolTip, imageDescriptor);
	}

	@Override
	public void setEditor(AbstractBrowser<?> editor) {
		editor.getSelectionLayer().addLayerListener(this);
		super.setEditor(editor);
	}

}
