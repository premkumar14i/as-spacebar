package com.tibco.as.spacebar.ui.properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.dialogs.PropertyPage;

import com.tibco.as.spacebar.ui.SpaceBarPlugin;
import com.tibco.as.spacebar.ui.model.Metaspace;
import com.tibco.as.spacebar.ui.wizards.metaspace.MetaspaceEditor;

public class MetaspacePropertyPage extends PropertyPage {

	private MetaspaceEditor editor;

	@Override
	protected Control createContents(Composite parent) {
		Metaspace metaspace = (Metaspace) getElement();
		editor = new MetaspaceEditor(parent, SWT.NONE, metaspace.clone());
		GridData data = new GridData(GridData.FILL);
		data.grabExcessHorizontalSpace = true;
		editor.setLayoutData(data);
		return editor;
	}

	@Override
	protected void performDefaults() {
		super.performDefaults();
		editor.setMetaspace(((Metaspace) getElement()).clone());
	}

	public boolean performOk() {
		Metaspace metaspace = (Metaspace) getElement();
		editor.getMetaspace().copyTo(metaspace);
		SpaceBarPlugin.getDefault().saveMetaspaces();
		return true;
	}
}