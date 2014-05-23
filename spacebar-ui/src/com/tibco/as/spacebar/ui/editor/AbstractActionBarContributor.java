package com.tibco.as.spacebar.ui.editor;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.EditorActionBarContributor;

import com.tibco.as.spacebar.ui.editor.action.BrowseAction;
import com.tibco.as.spacebar.ui.editor.action.CopyAction;

public abstract class AbstractActionBarContributor extends
		EditorActionBarContributor {

	private CopyAction copyAction;
	private BrowseAction browseAction;

	public AbstractActionBarContributor() {
		this.copyAction = new CopyAction();
		this.browseAction = new BrowseAction();
	}

	@Override
	public void setActiveEditor(IEditorPart editor) {
		AbstractSpaceEditor<?> browser = (AbstractSpaceEditor<?>) editor;
		getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(),
				copyAction);
		copyAction.setEditor(browser);
		browseAction.setEditor(browser);
	}

	@Override
	public void dispose() {
		copyAction.dispose();
		copyAction = null;
		browseAction.dispose();
		browseAction = null;
		super.dispose();
	}

	@Override
	public void contributeToToolBar(IToolBarManager toolBarManager) {
		super.contributeToToolBar(toolBarManager);
		toolBarManager.add(copyAction);
		toolBarManager.add(browseAction);
	}

}
