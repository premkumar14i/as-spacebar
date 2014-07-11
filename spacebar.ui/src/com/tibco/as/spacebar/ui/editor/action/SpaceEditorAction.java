package com.tibco.as.spacebar.ui.editor.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Event;

import com.tibco.as.spacebar.ui.SpaceBarPlugin;
import com.tibco.as.spacebar.ui.Image;
import com.tibco.as.spacebar.ui.editor.AbstractBrowser;

public abstract class SpaceEditorAction extends Action {

	private AbstractBrowser<?> editor;

	public SpaceEditorAction(String text, String toolTip, Image image) {
		this(text, toolTip, SpaceBarPlugin.getDefault().getImageDescriptor(image));
	}

	public SpaceEditorAction(String text, String toolTip,
			ImageDescriptor imageDescriptor) {
		super(text);
		setToolTipText(toolTip);
		setImageDescriptor(imageDescriptor);
		setHoverImageDescriptor(imageDescriptor);
	}

	public void setEditor(AbstractBrowser<?> editor) {
		this.editor = editor;
	}

	public AbstractBrowser<?> getEditor() {
		return editor;
	}

	@Override
	public void runWithEvent(Event event) {
		runWithEvent(event, editor);
	}

	protected abstract void runWithEvent(Event event, AbstractBrowser<?> editor);

	public void dispose() {
		editor = null;
	}

}
