package com.tibco.as.spacebar.ui.wizards.space.browse;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import com.tibco.as.io.AbstractExport;
import com.tibco.as.spacebar.ui.editor.Export;
import com.tibco.as.spacebar.ui.model.Space;
import com.tibco.as.spacebar.ui.preferences.Preferences;
import com.tibco.as.spacebar.ui.wizards.AbstractWizardPage;

public class BrowseSpaceWizardPage extends AbstractWizardPage {

	private Space space;
	private Export export;

	/**
	 * Create the wizard.
	 * 
	 * @param space
	 */
	public BrowseSpaceWizardPage(Space space, Export export) {
		super("browseSpaceWizardPage", "Browse", NLS.bind(
				"Browse space ''{0}''", space));
		this.space = space;
		this.export = export;
	}

	@Override
	protected Control createControl(ScrolledComposite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout());
		GridDataFactory gridDataFactory = GridDataFactory.fillDefaults().grab(
				true, false);
		Map<String, AbstractExport> defaults = new HashMap<String, AbstractExport>();
		defaults.put(Preferences.TIMESCOPE_ALL,
				Preferences.getSpaceEditorExport(Preferences.TIMESCOPE_ALL));
		defaults.put(Preferences.TIMESCOPE_NEW,
				Preferences.getSpaceEditorExport(Preferences.TIMESCOPE_NEW));
		defaults.put(Preferences.TIMESCOPE_CURRENT,
				Preferences.getSpaceEditorExport(Preferences.TIMESCOPE_CURRENT));
		defaults.put(Preferences.TIMESCOPE_SNAPSHOT, Preferences
				.getSpaceEditorExport(Preferences.TIMESCOPE_SNAPSHOT));
		BrowseEditor browseEditor = new BrowseEditor(composite, SWT.NONE,
				export, defaults);
		gridDataFactory.applyTo(browseEditor);
		new Label(browseEditor, SWT.NONE).setText("Size:");
		SizeControl size = new SizeControl(browseEditor, SWT.NONE, space,
				export);
		gridDataFactory.applyTo(size);
		return composite;
	}

	public Export getExport() {
		return export;
	}

}
