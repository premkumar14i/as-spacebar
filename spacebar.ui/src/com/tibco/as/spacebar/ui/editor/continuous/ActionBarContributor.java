package com.tibco.as.spacebar.ui.editor.continuous;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.IEditorPart;

import com.tibco.as.spacebar.ui.editor.AbstractActionBarContributor;

public class ActionBarContributor extends
		AbstractActionBarContributor {

	private ScrollLockAction scrollLockAction;

	public ActionBarContributor() {
		this.scrollLockAction = new ScrollLockAction();
	}

	public void setActiveEditor(IEditorPart targetEditor) {
		super.setActiveEditor(targetEditor);
		scrollLockAction.setEditor((ContinuousBrowser) targetEditor);
	}

	@Override
	public void dispose() {
		scrollLockAction.setEditor(null);
		scrollLockAction = null;
		super.dispose();
	}

	@Override
	public void contributeToToolBar(IToolBarManager toolBarManager) {
		super.contributeToToolBar(toolBarManager);
		toolBarManager.add(scrollLockAction);
	}

}
