package com.tibco.as.spacebar.ui.wizards.space.index;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import com.tibco.as.spacebar.ui.model.Field;
import com.tibco.as.spacebar.ui.model.Index;
import com.tibco.as.spacebar.ui.model.Indexes;
import com.tibco.as.spacebar.ui.wizards.AbstractWizard;

import com.tibco.as.space.IndexDef;
import com.tibco.as.space.IndexDef.IndexType;
import com.tibco.as.space.Space;
import com.tibco.as.space.SpaceDef;

public class AddIndexWizard extends AbstractWizard implements INewWizard {

	private Indexes indexes;
	private Index index;

	public AddIndexWizard() {
		super("AddIndex", "Add Index Error", "Could not add index");
		setWindowTitle("Add Index");
	}

	public AddIndexWizard(Indexes indexes) {
		this();
		this.indexes = indexes;
	}

	@Override
	public void addPages() {
		index = new Index(indexes, null);
		index.setType(IndexType.HASH);
		addPage(new EditIndexWizardPage(null, index));
	}

	@Override
	protected void finish(IProgressMonitor monitor) throws Exception {
		monitor.beginTask("Adding index", 1);
		Space space = indexes.getParent().getSpace();
		SpaceDef spaceDef = space.getSpaceDef();
		IndexDef indexDef = IndexDef.create(index.getName());
		indexDef.setIndexType(index.getType());
		List<Field> fields = index.getChildren();
		String[] fieldNames = new String[fields.size()];
		for (int index = 0; index < fields.size(); index++) {
			fieldNames[index] = fields.get(index).getName();
		}
		indexDef.setFieldNames(fieldNames);
		spaceDef.addIndexDef(indexDef);
		space.getMetaspace().alterSpace(spaceDef);
		monitor.worked(1);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		Object element = selection.getFirstElement();
		if (element instanceof Indexes) {
			this.indexes = (Indexes) element;
		}
	}
}
