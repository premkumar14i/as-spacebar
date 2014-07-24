package com.tibco.as.spacebar.ui.wizards.space.index;

import java.util.List;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.tibco.as.space.IndexDef.IndexType;
import com.tibco.as.spacebar.ui.model.Field;
import com.tibco.as.spacebar.ui.model.IElement;
import com.tibco.as.spacebar.ui.model.Index;
import com.tibco.as.spacebar.ui.model.Indexes;
import com.tibco.as.spacebar.ui.wizards.space.ColumnConfig;
import com.tibco.as.spacebar.ui.wizards.space.ElementListEditor;

public class IndexListEditor extends ElementListEditor {

	private static final String COLUMN_NAME = "Name";
	private static final String COLUMN_TYPE = "Type";
	private static final String COLUMN_FIELDS = "Fields";

	public IndexListEditor(Composite parent, int style, Indexes indexes) {
		super(parent, style, indexes, new ColumnConfig(COLUMN_NAME, SWT.LEFT,
				40, 100, true, new ColumnLabelProvider() {
					@Override
					public String getText(Object element) {
						return ((Index) element).getName();
					}
				}), new ColumnConfig(COLUMN_TYPE, SWT.LEFT, 20, 50, true,
				new ColumnLabelProvider() {
					@Override
					public String getText(Object element) {
						IndexType type = ((Index) element).getType();
						if (type == null) {
							return null;
						}
						return type.name();
					}
				}), new ColumnConfig(COLUMN_FIELDS, SWT.LEFT, 40, 150, true,
				new ColumnLabelProvider() {
					@Override
					public String getText(Object element) {
						String label = "";
						List<Field> fields = ((Index) element).getFields();
						for (int i = 0; i < fields.size(); i++) {
							if (i > 0) {
								label += ", ";
							}
							label += fields.get(i).getName();
						}
						return label;
					}
				}));
	}

	@Override
	protected IElement newElement(IElement parentElement) {
		Index index = new Index();
		index.setIndexes((Indexes) parentElement);
		index.setName("");
		index.setType(IndexType.HASH);
		return index;
	}

	@Override
	protected boolean editElement(IElement original, IElement field) {
		EditIndexWizard wizard = new EditIndexWizard((Index) original,
				(Index) field);
		WizardDialog dialog = new WizardDialog(getShell(), wizard);
		dialog.setMinimumPageSize(300, 400);
		return dialog.open() == Window.OK;
	}

}
