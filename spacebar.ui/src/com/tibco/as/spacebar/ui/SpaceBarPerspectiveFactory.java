package com.tibco.as.spacebar.ui;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.progress.IProgressConstants;

public class SpaceBarPerspectiveFactory implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();

		layout.addShowViewShortcut(SpaceBarPlugin.ID_METASPACES);
		layout.addShowViewShortcut(IPageLayout.ID_PROP_SHEET);
		layout.addShowViewShortcut(IProgressConstants.PROGRESS_VIEW_ID);
		layout.addShowViewShortcut("org.eclipse.pde.runtime.LogView");

		IFolderLayout leftFolder = layout.createFolder("left",
				IPageLayout.LEFT, .25f, editorArea);
		leftFolder.addView(SpaceBarPlugin.ID_METASPACES);

		IFolderLayout bottom = layout.createFolder("bottom",
				IPageLayout.BOTTOM, .75f, editorArea);
		bottom.addView(IPageLayout.ID_PROP_SHEET);
		bottom.addPlaceholder(IProgressConstants.PROGRESS_VIEW_ID);
	}

}
