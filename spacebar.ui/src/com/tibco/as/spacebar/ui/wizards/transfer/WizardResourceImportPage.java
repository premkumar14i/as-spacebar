package com.tibco.as.spacebar.ui.wizards.transfer;

import java.io.File;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.FileSystemElement;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.model.WorkbenchViewerComparator;

/**
 * The abstract superclass for a typical import wizard's main page.
 * <p>
 * Clients may subclass this page to inherit its common destination resource
 * selection facilities.
 * </p>
 * <p>
 * Subclasses must implement
 * <ul>
 * <li><code>createSourceGroup</code></li>
 * </ul>
 * </p>
 * <p>
 * Subclasses may override
 * <ul>
 * <li><code>allowNewContainerName</code></li>
 * </ul>
 * </p>
 * <p>
 * Subclasses may extend
 * <ul>
 * <li><code>handleEvent</code></li>
 * </ul>
 * </p>
 */
public abstract class WizardResourceImportPage extends
		AbstractTransferWizardPage {
	
	private File currentResourceSelection;

	// initial value stores
	private String initialContainerFieldValue;

	// widgets
	private Text containerNameField;

	private Button containerBrowseButton;

	/**
	 * The <code>selectionGroup</code> field should have been created with a
	 * private modifier. Subclasses should not access this field directly.
	 */
	protected ResourceTreeAndListGroup selectionGroup;

	// messages
	private static final String EMPTY_FOLDER_MESSAGE = "Please specify folder";

	/**
	 * Creates an import wizard page. If the initial resource selection contains
	 * exactly one container resource then it will be used as the default import
	 * destination.
	 * 
	 * @param name
	 *            the name of the page
	 * @param selection
	 *            the current resource selection
	 */
	protected WizardResourceImportPage(String name,
			IStructuredSelection selection) {
		super(name);

		// Initialize to null
		currentResourceSelection = null;
		if (selection.size() == 1) {
			Object firstElement = selection.getFirstElement();
			if (firstElement instanceof IAdaptable) {
				Object resource = ((IAdaptable) firstElement)
						.getAdapter(File.class);
				if (resource != null) {
					currentResourceSelection = (File) resource;
				}
			}
		}

		if (currentResourceSelection != null) {
			if (currentResourceSelection.isFile()) {
				currentResourceSelection = currentResourceSelection.getParentFile();
			}

			if (!currentResourceSelection.exists()) {
				currentResourceSelection = null;
			}
		}

	}

	@Override
	public void createControl(Composite parent) {

		initializeDialogUnits(parent);

		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL
				| GridData.HORIZONTAL_ALIGN_FILL));
		composite.setSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		composite.setFont(parent.getFont());

		createSourceGroup(composite);

		createDestinationGroup(composite);

		createOptionsGroup(composite);

		restoreWidgetValues();
		updateWidgetEnablements();
		setPageComplete(determinePageCompletion());
		setErrorMessage(null); // should not initially have error message

		setControl(composite);
	}

	/**
	 * Creates the import destination specification controls.
	 * 
	 * @param parent
	 *            the parent control
	 */
	protected final void createDestinationGroup(Composite parent) {
		// container specification group
		Composite containerGroup = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		containerGroup.setLayout(layout);
		containerGroup.setLayoutData(new GridData(
				GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
		containerGroup.setFont(parent.getFont());

		// container label
		Label resourcesLabel = new Label(containerGroup, SWT.NONE);
		resourcesLabel.setText("Into fo&lder:");
		resourcesLabel.setFont(parent.getFont());

		// container name entry field
		containerNameField = new Text(containerGroup, SWT.SINGLE | SWT.BORDER);
		containerNameField.addListener(SWT.Modify, this);
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.GRAB_HORIZONTAL);
		data.widthHint = SIZING_TEXT_FIELD_WIDTH;
		containerNameField.setLayoutData(data);
		containerNameField.setFont(parent.getFont());

		// container browse button
		containerBrowseButton = new Button(containerGroup, SWT.PUSH);
		containerBrowseButton
				.setText("Bro&wse...");
		containerBrowseButton.setLayoutData(new GridData(
				GridData.HORIZONTAL_ALIGN_FILL));
		containerBrowseButton.addListener(SWT.Selection, this);
		containerBrowseButton.setFont(parent.getFont());
		setButtonLayoutData(containerBrowseButton);

		initialPopulateContainerField();
	}

	/**
	 * Create the import source selection widget
	 */
	protected void createFileSelectionGroup(Composite parent) {

		// Just create with a dummy root.
		this.selectionGroup = new ResourceTreeAndListGroup(
				parent,
				new FileSystemElement("Dummy", null, true),//$NON-NLS-1$
				getFolderProvider(), new WorkbenchLabelProvider(),
				getFileProvider(), new WorkbenchLabelProvider(), SWT.NONE,
				inRegularFontMode(parent));

		ICheckStateListener listener = new ICheckStateListener() {
			public void checkStateChanged(CheckStateChangedEvent event) {
				updateWidgetEnablements();
			}
		};

		WorkbenchViewerComparator comparator = new WorkbenchViewerComparator();
		this.selectionGroup.setTreeComparator(comparator);
		this.selectionGroup.setListComparator(comparator);
		this.selectionGroup.addCheckStateListener(listener);

	}

	/**
	 * Creates the import source specification controls.
	 * <p>
	 * Subclasses must implement this method.
	 * </p>
	 * 
	 * @param parent
	 *            the parent control
	 */
	protected abstract void createSourceGroup(Composite parent);

	/**
	 * Returns the path of the container resource specified in the container
	 * name entry field, or <code>null</code> if no name has been typed in.
	 * <p>
	 * The container specified by the full path might not exist and would need
	 * to be created.
	 * </p>
	 * 
	 * @return the full path of the container resource specified in the
	 *         container name entry field, or <code>null</code>
	 */
	protected IPath getContainerFullPath() {

		// make the path absolute to allow for optional leading slash
		return getResourcePath();

	}

	/**
	 * Returns a content provider for <code>FileSystemElement</code>s that
	 * returns only files as children.
	 */
	protected abstract ITreeContentProvider getFileProvider();

	/**
	 * Returns a content provider for <code>FileSystemElement</code>s that
	 * returns only folders as children.
	 */
	protected abstract ITreeContentProvider getFolderProvider();

	/**
	 * Return the path for the resource field.
	 * 
	 * @return IPath
	 */
	protected IPath getResourcePath() {
		return getPathFromText(this.containerNameField);
	}

	/**
	 * Returns this page's list of currently-specified resources to be imported.
	 * This is the primary resource selection facility accessor for subclasses.
	 * 
	 * @return a list of resources currently selected for export (element type:
	 *         <code>IResource</code>)
	 */
	protected List<?> getSelectedResources() {
		return this.selectionGroup.getAllCheckedListItems();
	}

	/**
	 * Returns this page's list of currently-specified resources to be imported
	 * filtered by the IElementFilter.
	 * 
	 */
	protected void getSelectedResources(IElementFilter filter,
			IProgressMonitor monitor) throws InterruptedException {
		this.selectionGroup.getAllCheckedListItems(filter, monitor);
	}

	/**
	 * The <code>WizardResourceImportPage</code> implementation of this
	 * <code>Listener</code> method handles all events and enablements for
	 * controls on this page. Subclasses may extend.
	 * 
	 * @param event
	 *            Event
	 */
	public void handleEvent(Event event) {
		updateWidgetEnablements();
	}

	/**
	 * Sets the initial contents of the container name field.
	 */
	protected final void initialPopulateContainerField() {
		if (initialContainerFieldValue != null) {
			containerNameField.setText(initialContainerFieldValue);
		} else if (currentResourceSelection != null) {
			containerNameField.setText(currentResourceSelection.toString());
		}
	}

	/**
	 * Check if widgets are enabled or disabled by a change in the dialog.
	 */
	protected void updateWidgetEnablements() {

		boolean pageComplete = determinePageCompletion();
		setPageComplete(pageComplete);
		if (pageComplete) {
			setMessage(null);
		}
		super.updateWidgetEnablements();
	}

	@Override
	protected final boolean validateDestinationGroup() {

		IPath containerPath = getContainerFullPath();
		if (containerPath == null) {
			setMessage(EMPTY_FOLDER_MESSAGE);
			return false;
		}

		return true;

	}

}
