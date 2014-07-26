package com.tibco.as.spacebar.ui.wizards.space.field;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import com.tibco.as.space.FieldDef;
import com.tibco.as.space.FieldDef.FieldType;
import com.tibco.as.space.Metaspace;
import com.tibco.as.space.SpaceDef;
import com.tibco.as.spacebar.ui.model.Field;
import com.tibco.as.spacebar.ui.model.Space;
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
		field = new Field();
		field.setFields(fields);
		field.setNullable(true);
		field.setType(FieldType.STRING);
		addPage(new AddFieldWizardPage(field));
	}

	@Override
	protected void finish(IProgressMonitor monitor) throws Exception {
		monitor.beginTask("Adding field", 1);
		Space space = fields.getParent();
		Metaspace metaspace = space.getParent().getParent().getConnection()
				.getMetaspace();
		SpaceDef spaceDef = metaspace.getSpaceDef(space.getName());
		FieldDef fieldDef = FieldDef.create(field.getName(), field.getType());
		if (Utils.hasFieldDefMethod("setEncrypted")) {
			fieldDef.setEncrypted(field.isEncrypted());
		}
		fieldDef.setNullable(field.isNullable());
		spaceDef.putFieldDef(fieldDef);
		metaspace.alterSpace(spaceDef);
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
