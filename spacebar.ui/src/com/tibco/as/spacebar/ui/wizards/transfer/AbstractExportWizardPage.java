package com.tibco.as.spacebar.ui.wizards.transfer;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.model.WorkbenchLabelProvider;

import com.tibco.as.space.Tuple;
import com.tibco.as.spacebar.ui.SpaceBarPlugin;
import com.tibco.as.spacebar.ui.model.IElement;
import com.tibco.as.spacebar.ui.model.Metaspace;
import com.tibco.as.spacebar.ui.model.Space;
import com.tibco.as.spacebar.ui.model.TupleSelection;

public abstract class AbstractExportWizardPage extends
		AbstractTransferWizardPage {

	private static final String STORE_DESTINATION_NAMES = "STORE_DESTINATION_NAMES_ID"; //$NON-NLS-1$

	private static final String STORE_OVERWRITE_EXISTING_FILES = "STORE_OVERWRITE_EXISTING_FILES_ID"; //$NON-NLS-1$

	private static final String SELECT_DESTINATION_MESSAGE = "Select a directory to export to.";

	private Combo destinationNameField;

	private Button destinationBrowseButton;

	private IStructuredSelection initialResourceSelection;

	private Button overwriteExistingFilesCheckbox;

	private ResourceTreeAndListGroup resourceGroup;

	private final static String SELECT_ALL_TITLE = " &Select All";

	private final static String DESELECT_ALL_TITLE = "&Deselect All";

	/**
	 * Creates an export wizard page. If the current resource selection is not
	 * empty then it will be used as the initial collection of resources
	 * selected for export.
	 * 
	 * @param pageName
	 *            the name of the page
	 * @param selection
	 *            {@link IStructuredSelection} of {@link IResource}
	 */
	protected AbstractExportWizardPage(String pageName,
			IStructuredSelection selection) {
		super(pageName);
		this.initialResourceSelection = selection;
	}

	/**
	 * Create the buttons in the options group.
	 */
	protected void createOptionsGroup(Group optionsGroup) {
		Font font = optionsGroup.getFont();
		overwriteExistingFilesCheckbox = new Button(optionsGroup, SWT.CHECK
				| SWT.LEFT);
		overwriteExistingFilesCheckbox
				.setText("&Overwrite existing files without warning");
		overwriteExistingFilesCheckbox.setFont(font);
	}

	public boolean isOverwrite() {
		return overwriteExistingFilesCheckbox.getSelection();
	}

	/**
	 * Answer a boolean indicating whether the receivers destination
	 * specification widgets currently all contain valid values.
	 */
	protected boolean validateDestinationGroup() {
		String destinationValue = getDestinationValue();
		if (destinationValue.length() == 0) {
			setMessage(destinationEmptyMessage());
			return false;
		}
		return true;
	}

	@Override
	protected boolean validateSourceGroup() {
		// there must be some resources selected for Export
		boolean isValid;
		if (!isTupleSelection() && getWhiteCheckedResources().isEmpty()) {
			setErrorMessage("There are no items currently selected for export.");
			isValid = false;
		} else {
			setErrorMessage(null);
			isValid = true;
		}
		return super.validateSourceGroup() && isValid;
	}

	/**
	 * Get the message used to denote an empty destination.
	 */
	protected String destinationEmptyMessage() {
		return "Please enter a destination directory.";
	}

	/**
	 * Creates a new button with the given id.
	 * <p>
	 * The <code>Dialog</code> implementation of this framework method creates a
	 * standard push button, registers for selection events including button
	 * presses and registers default buttons with its shell. The button id is
	 * stored as the buttons client data. Note that the parent's layout is
	 * assumed to be a GridLayout and the number of columns in this layout is
	 * incremented. Subclasses may override.
	 * </p>
	 * 
	 * @param parent
	 *            the parent composite
	 * @param id
	 *            the id of the button (see <code>IDialogConstants.*_ID</code>
	 *            constants for standard dialog button ids)
	 * @param label
	 *            the label from the button
	 * @param defaultButton
	 *            <code>true</code> if the button is to be the default button,
	 *            and <code>false</code> otherwise
	 */
	protected Button createButton(Composite parent, int id, String label,
			boolean defaultButton) {
		// increment the number of columns in the button bar
		((GridLayout) parent.getLayout()).numColumns++;

		Button button = new Button(parent, SWT.PUSH);

		GridData buttonData = new GridData(GridData.FILL_HORIZONTAL);
		button.setLayoutData(buttonData);

		button.setData(new Integer(id));
		button.setText(label);
		button.setFont(parent.getFont());

		if (defaultButton) {
			Shell shell = parent.getShell();
			if (shell != null) {
				shell.setDefaultButton(button);
			}
			button.setFocus();
		}
		button.setFont(parent.getFont());
		setButtonLayoutData(button);
		return button;
	}

	/**
	 * Creates the buttons for selecting specific types or selecting all or none
	 * of the elements.
	 * 
	 * @param parent
	 *            the parent control
	 */
	protected final void createButtonsGroup(Composite parent) {

		Font font = parent.getFont();

		// top level group
		Composite buttonComposite = new Composite(parent, SWT.NONE);
		buttonComposite.setFont(parent.getFont());

		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		layout.makeColumnsEqualWidth = true;
		buttonComposite.setLayout(layout);
		buttonComposite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL
				| GridData.HORIZONTAL_ALIGN_FILL));

		Button selectButton = createButton(buttonComposite,
				IDialogConstants.SELECT_ALL_ID, SELECT_ALL_TITLE, false);

		SelectionListener listener = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				resourceGroup.setAllSelections(true);
				updateWidgetEnablements();
			}
		};
		selectButton.addSelectionListener(listener);
		selectButton.setFont(font);
		setButtonLayoutData(selectButton);

		Button deselectButton = createButton(buttonComposite,
				IDialogConstants.DESELECT_ALL_ID, DESELECT_ALL_TITLE, false);

		listener = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				resourceGroup.setAllSelections(false);
				updateWidgetEnablements();
			}
		};
		deselectButton.addSelectionListener(listener);
		deselectButton.setFont(font);
		setButtonLayoutData(deselectButton);

	}

	@Override
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL
				| GridData.HORIZONTAL_ALIGN_FILL));
		composite.setFont(parent.getFont());
		boolean tupleSelection = isTupleSelection();
		if (!tupleSelection) {
			createResourcesGroup(composite);
			createButtonsGroup(composite);
		}
		createDestinationGroup(composite);
		createOptionsGroup(composite);
		restoreWidgetValues();
		if (!tupleSelection && initialResourceSelection != null) {
			setupBasedOnInitialSelections();
		}
		updateWidgetEnablements();
		setPageComplete(determinePageCompletion());
		setErrorMessage(null); // should not initially have error message
		setControl(composite);
		giveFocusToDestination();
		PlatformUI
				.getWorkbench()
				.getHelpSystem()
				.setHelp(getControl(),
						SpaceBarPlugin.ID_PLUGIN + "." + getName());
	}

	protected boolean isTupleSelection() {
		if (initialResourceSelection == null) {
			return false;
		}
		return initialResourceSelection instanceof TupleSelection;
	}

	/**
	 * Create the options specification widgets.
	 * 
	 * @param parent
	 *            org.eclipse.swt.widgets.Composite
	 */
	protected void createOptionsGroup(Composite parent) {
		// options group
		Group optionsGroup = new Group(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		optionsGroup.setLayout(layout);
		optionsGroup.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.GRAB_HORIZONTAL));
		optionsGroup.setText("Options");
		optionsGroup.setFont(parent.getFont());
		createOptionsGroup(optionsGroup);
	}

	/**
	 * Creates the export destination specification visual components.
	 * <p>
	 * Subclasses must implement this method.
	 * </p>
	 * 
	 * @param parent
	 *            the parent control
	 */
	protected void createDestinationGroup(Composite parent) {
		Font font = parent.getFont();
		// destination specification group
		Composite destinationSelectionGroup = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		destinationSelectionGroup.setLayout(layout);
		destinationSelectionGroup.setLayoutData(new GridData(
				GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		destinationSelectionGroup.setFont(font);

		Label destinationLabel = new Label(destinationSelectionGroup, SWT.NONE);
		destinationLabel.setText(getDestinationLabel());
		destinationLabel.setFont(font);

		// destination name entry field
		destinationNameField = new Combo(destinationSelectionGroup, SWT.SINGLE
				| SWT.BORDER);
		destinationNameField.addListener(SWT.Modify, this);
		destinationNameField.addListener(SWT.Selection, this);
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.GRAB_HORIZONTAL);
		data.widthHint = SIZING_TEXT_FIELD_WIDTH;
		destinationNameField.setLayoutData(data);
		destinationNameField.setFont(font);

		// destination browse button
		destinationBrowseButton = new Button(destinationSelectionGroup,
				SWT.PUSH);
		destinationBrowseButton.setText("B&rowse...");
		destinationBrowseButton.addListener(SWT.Selection, this);
		destinationBrowseButton.setFont(font);
		setButtonLayoutData(destinationBrowseButton);
		new Label(parent, SWT.NONE); // vertical spacer
	}

	/**
	 * Creates the checkbox tree and list for selecting resources.
	 * 
	 * @param parent
	 *            the parent control
	 */
	protected final void createResourcesGroup(Composite parent) {
		this.resourceGroup = new ResourceTreeAndListGroup(parent,
				SpaceBarPlugin.getDefault().getMetaspaces()
						.getConnectedMetaspaces(),
				getResourceProvider(Metaspace.class),
				WorkbenchLabelProvider.getDecoratingWorkbenchLabelProvider(),
				getResourceProvider(Space.class),
				WorkbenchLabelProvider.getDecoratingWorkbenchLabelProvider(),
				SWT.NONE, inRegularFontMode(parent));
		ICheckStateListener listener = new ICheckStateListener() {
			public void checkStateChanged(CheckStateChangedEvent event) {
				updateWidgetEnablements();
			}
		};
		this.resourceGroup.addCheckStateListener(listener);
	}

	/**
	 * Returns a content provider for <code>IResource</code>s that returns only
	 * children of the given resource type.
	 */
	private ITreeContentProvider getResourceProvider(
			Class<? extends IElement> type) {
		return new ElementContentProvider(type, Metaspace.class);
	}

	@Override
	protected String getErrorDialogTitle() {
		return "Export Problems";
	}

	/**
	 * Returns this page's collection of currently-specified resources to be
	 * exported. This returns both folders and files - for just the files use
	 * getSelectedResources.
	 * 
	 * @return a collection of resources currently selected for export (element
	 *         type: <code>IResource</code>)
	 */
	protected List<?> getWhiteCheckedResources() {
		return resourceGroup.getAllWhiteCheckedItems();
	}

	/**
	 * Persists resource specification control setting that are to be restored
	 * in the next instance of this page.
	 */
	protected void saveWidgetValues() {
		IDialogSettings settings = getDialogSettings();
		if (settings == null) {
			return;
		}
		String key = getSettingKey(STORE_DESTINATION_NAMES);
		// update directory names history
		String[] directoryNames = settings.getArray(key);
		if (directoryNames == null) {
			directoryNames = new String[0];
		}
		directoryNames = addToHistory(directoryNames, getDestinationValue());
		settings.put(key, directoryNames);
		// options
		settings.put(getSettingKey(STORE_OVERWRITE_EXISTING_FILES),
				isOverwrite());
	}

	/**
	 * Set the initial selections in the resource group.
	 */
	protected void setupBasedOnInitialSelections() {
		Iterator<?> it = initialResourceSelection.iterator();
		while (it.hasNext()) {
			setupSelection((IElement) it.next());
		}
	}

	private void setupSelection(IElement element) {
		if (element instanceof Space) {
			resourceGroup.initialCheckListItem(element);
		} else if (element instanceof Metaspace) {
			resourceGroup.initialCheckTreeItem(element);
		} else
			for (IElement child : element.getChildren()) {
				setupSelection(child);
			}
	}

	/**
	 * Add the passed value to self's destination widget's history
	 * 
	 * @param value
	 *            java.lang.String
	 */
	protected void addDestinationItem(String value) {
		destinationNameField.add(value);
	}

	/**
	 * Answer the string to display in self as the destination type
	 * 
	 * @return java.lang.String
	 */
	protected String getDestinationLabel() {
		return "To director&y:";
	}

	/**
	 * Answer the contents of self's destination specification widget
	 * 
	 * @return java.lang.String
	 */
	public String getDestinationValue() {
		return destinationNameField.getText().trim();
	}

	/**
	 * Set the current input focus to self's destination entry field
	 */
	protected void giveFocusToDestination() {
		destinationNameField.setFocus();
	}

	/**
	 * Open an appropriate destination browser so that the user can specify a
	 * source to import from
	 */
	protected void handleDestinationBrowseButtonPressed() {
		DirectoryDialog dialog = new DirectoryDialog(getContainer().getShell(),
				SWT.SAVE | SWT.SHEET);
		dialog.setMessage(SELECT_DESTINATION_MESSAGE);
		dialog.setText(getDestinationTitle());
		dialog.setFilterPath(getDestinationValue());
		String selectedDirectoryName = dialog.open();

		if (selectedDirectoryName != null) {
			setErrorMessage(null);
			setDestinationValue(selectedDirectoryName);
		}
	}

	protected abstract String getDestinationTitle();

	/**
	 * Handle all events and enablements for widgets in this page
	 * 
	 * @param e
	 *            Event
	 */
	public void handleEvent(Event e) {
		Widget source = e.widget;

		if (source == destinationBrowseButton) {
			handleDestinationBrowseButtonPressed();
		}

		updatePageCompletion();
	}

	@Override
	protected void restoreWidgetValues() {
		IDialogSettings settings = getDialogSettings();
		if (settings == null) {
			return;
		}
		String key = getSettingKey(STORE_DESTINATION_NAMES);
		String[] directoryNames = settings.getArray(key);
		if (directoryNames == null) {
			return; // ie.- no settings stored
		}
		setDestinationValue(directoryNames[0]);
		for (String directoryName : directoryNames) {
			addDestinationItem(directoryName);
		}
		overwriteExistingFilesCheckbox.setSelection(settings
				.getBoolean(getSettingKey(STORE_OVERWRITE_EXISTING_FILES)));
	}

	/**
	 * Set the contents of the receivers destination specification widget to the
	 * passed value
	 * 
	 */
	protected void setDestinationValue(String value) {
		destinationNameField.setText(value);
	}

	@SuppressWarnings("unchecked")
	public List<Tuple> getTuples() {
		return (List<Tuple>) initialResourceSelection.getFirstElement();
	}

}
