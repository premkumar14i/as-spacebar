package com.tibco.as.spacebar.ui.editor.snapshot;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
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

	public ActionBarContributor() {
		this.insertAction = new InsertAction();
		this.deleteAction = new DeleteAction();
		this.csvExportAction = new CSVExportAction();
		this.excelExportAction = new ExcelExportAction();
		this.sizeAction = new TupleSizeAction();
	}

	@Override
	public void setActiveEditor(IEditorPart editor) {
		super.setActiveEditor(editor);
		SpaceEditor browser = (SpaceEditor) editor;
		insertAction.setEditor(browser);
		deleteAction.setEditor(browser);
		csvExportAction.setEditor(browser);
		excelExportAction.setEditor(browser);
		sizeAction.setEditor(browser);
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
