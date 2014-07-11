package com.tibco.as.spacebar.ui.editor.continuous;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;

import com.tibco.as.spacebar.ui.SpaceBarPlugin;
import com.tibco.as.spacebar.ui.Image;

public class ScrollLockAction extends Action {

	private ContinuousBrowser editor;

	public ScrollLockAction() {
		super("Scroll Lock", IAction.AS_CHECK_BOX);
		setToolTipText("Scroll Lock");
		setDisabledImageDescriptor(SpaceBarPlugin.getDefault().getImageDescriptor(
				Image.LOCK_DISABLED));
		setImageDescriptor(SpaceBarPlugin.getDefault().getImageDescriptor(
				Image.LOCK_ENABLED));
		setHoverImageDescriptor(SpaceBarPlugin.getDefault().getImageDescriptor(
				Image.LOCK_ENABLED));
	}

	public void setEditor(ContinuousBrowser editor) {
		this.editor = editor;
	}

	@Override
	public void setChecked(boolean checked) {
		super.setChecked(checked);
		editor.setScrollLocked(checked);
	}

}
