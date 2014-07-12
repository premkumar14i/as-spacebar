package com.tibco.as.spacebar.ui.wizards.space.field;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import com.tibco.as.space.FieldDef;
import com.tibco.as.space.FieldDef.FieldType;
import com.tibco.as.space.Space;
import com.tibco.as.space.SpaceDef;
import com.tibco.as.spacebar.ui.model.Field;
import com.tibco.as.spacebar.ui.model.SpaceFields;
import com.tibco.as.spacebar.ui.wizards.AbstractWizard;
import com.tibco.as.util.Utils;

public class AddFieldWizard extends AbstractWizard implements INewWizard {

	private SpaceFields fields;
	private Field field;

	public AddFieldWizard() {
		super("AddField", "Add Field Error", "Could not add field");
		setWindowTitle("Add Field");
	}

	public AddFieldWizard(SpaceFields fields) {
		this();
		this.fields = fields;
	}

	@Override
	public void addPages() {
		field = new Field(fields);
		field.setNullable(true);
		field.setType(FieldType.STRING);
		addPage(new EditFieldWizardPage(null, field));
	}

	@Override
	protected void finish(IProgressMonitor monitor) throws Exception {
		monitor.beginTask("Adding field", 1);
		Space space = ((com.tibco.as.spacebar.ui.model.Space) fields
				.getParent()).getSpace();
		SpaceDef spaceDef = space.getSpaceDef();
		FieldDef fieldDef = FieldDef.create(field.getName(), field.getType());
		if (Utils.hasFieldDefMethod("setEncrypted")) {
			fieldDef.setEncrypted(field.isNullable());
		}
		fieldDef.setNullable(field.isNullable());
		spaceDef.getFieldDefs().add(fieldDef);
		space.getMetaspace().alterSpace(spaceDef);
		monitor.worked(1);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		Object element = selection.getFirstElement();
		if (element instanceof SpaceFields) {
			this.fields = (SpaceFields) element;
		}
	}
}
