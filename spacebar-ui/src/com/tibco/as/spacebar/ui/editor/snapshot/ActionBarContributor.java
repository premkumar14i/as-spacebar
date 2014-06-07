package com.tibco.as.spacebar.ui.editor.snapshot;

import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.StatusLineContributionItem;
import org.eclipse.ui.IEditorPart;

import com.tibco.as.spacebar.ui.editor.AbstractActionBarContributor;
import com.tibco.as.spacebar.ui.editor.action.CSVExportAction;
import com.tibco.as.spacebar.ui.editor.action.DeleteAction;
import com.tibco.as.spacebar.ui.editor.action.ExcelExportAction;
import com.tibco.as.spacebar.ui.editor.action.InsertAction;
import com.tibco.as.spacebar.ui.editor.action.TupleSizeAction;

public class ActionBarContributor extends AbstractActionBarContributor {

	private InsertAction insertAction;
	private DeleteAction deleteAction;
	private CSVExportAction csvExportAction;
	private ExcelExportAction excelExportAction;
	private TupleSizeAction sizeAction;

	private StatusLineContributionItem sizeItem;
	private StatusLineContributionItem browseTimeItem;

	public ActionBarContributor() {
		this.insertAction = new InsertAction();
		this.deleteAction = new DeleteAction();
		this.csvExportAction = new CSVExportAction();
		this.excelExportAction = new ExcelExportAction();
		this.sizeAction = new TupleSizeAction();
		sizeItem = new StatusLineContributionItem("size", 25);
		browseTimeItem = new StatusLineContributionItem("browseTime", 25);
	}

	@Override
	public void contributeToStatusLine(IStatusLineManager statusLineManager) {
		super.contributeToStatusLine(statusLineManager);
		statusLineManager.add(sizeItem);
		statusLineManager.add(new Separator());
		statusLineManager.add(browseTimeItem);
	}

	@Override
	public void setActiveEditor(IEditorPart editor) {
		super.setActiveEditor(editor);
		SnapshotBrowser spaceEditor = (SnapshotBrowser) editor;
		insertAction.setEditor(spaceEditor);
		deleteAction.setEditor(spaceEditor);
		csvExportAction.setEditor(spaceEditor);
		excelExportAction.setEditor(spaceEditor);
		sizeAction.setEditor(spaceEditor);
		spaceEditor.setSizeItem(sizeItem);
		spaceEditor.setBrowseTimeItem(browseTimeItem);
	}

	@Override
	public void dispose() {
		insertAction.dispose();
		insertAction = null;
		deleteAction.dispose();
		deleteAction = null;
		csvExportAction.dispose();
		csvExportAction = null;
		excelExportAction.dispose();
		excelExportAction = null;
		sizeAction.dispose();
		sizeAction = null;
		sizeItem.dispose();
		sizeItem = null;
		browseTimeItem.dispose();
		browseTimeItem = null;
		super.dispose();
	}

	@Override
	public void contributeToToolBar(IToolBarManager toolBarManager) {
		toolBarManager.add(insertAction);
		toolBarManager.add(deleteAction);
		toolBarManager.add(new Separator());
		super.contributeToToolBar(toolBarManager);
		toolBarManager.add(csvExportAction);
		toolBarManager.add(excelExportAction);
		toolBarManager.add(sizeAction);
	}
}
