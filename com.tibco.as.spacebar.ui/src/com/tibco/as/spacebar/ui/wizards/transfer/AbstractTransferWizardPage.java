package com.tibco.as.spacebar.ui.wizards.transfer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.IOverwriteQuery;

/**
 * The common superclass for wizard import and export pages.
 * <p>
 * This class is not intended to be subclassed outside of the workbench.
 * </p>
 * 
 * @noextend This class is not intended to be subclassed by clients.
 */
public abstract class AbstractTransferWizardPage extends WizardPage implements
		Listener, IOverwriteQuery {

	// constants
	protected static final int SIZING_TEXT_FIELD_WIDTH = 250;

	protected static final int COMBO_HISTORY_LENGTH = 5;

	/**
	 * Creates a new wizard page.
	 * 
	 * @param pageName
	 *            the name of the page
	 */
	protected AbstractTransferWizardPage(String pageName) {
		super(pageName);
	}

	protected String getSettingKey(String id) {
		return getName() + "." + id;
	}

	/**
	 * Adds an entry to a history, while taking care of duplicate history items
	 * and excessively long histories. The assumption is made that all histories
	 * should be of length
	 * <code>WizardDataTransferPage.COMBO_HISTORY_LENGTH</code>.
	 * 
	 * @param history
	 *            the current history
	 * @param newEntry
	 *            the entry to add to the history
	 */
	protected String[] addToHistory(String[] history, String newEntry) {
		List<String> list = new ArrayList<String>(Arrays.asList(history));
		addToHistory(list, newEntry);
		return list.toArray(new String[list.size()]);
	}

	/**
	 * Adds an entry to a history, while taking care of duplicate history items
	 * and excessively long histories. The assumption is made that all histories
	 * should be of length
	 * <code>WizardDataTransferPage.COMBO_HISTORY_LENGTH</code>.
	 * 
	 * @param history
	 *            the current history
	 * @param newEntry
	 *            the entry to add to the history
	 */
	protected void addToHistory(List<String> history, String newEntry) {
		history.remove(newEntry);
		history.add(0, newEntry);

		// since only one new item was added, we can be over the limit
		// by at most one item
		if (history.size() > COMBO_HISTORY_LENGTH) {
			history.remove(COMBO_HISTORY_LENGTH);
		}
	}

	/**
	 * Creates a new label with a bold font.
	 * 
	 * @param parent
	 *            the parent control
	 * @param text
	 *            the label text
	 * @return the new label control
	 */
	protected Label createBoldLabel(Composite parent, String text) {
		Label label = new Label(parent, SWT.NONE);
		label.setFont(JFaceResources.getBannerFont());
		label.setText(text);
		GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData(data);
		return label;
	}

	/**
	 * Creates the import/export options group controls.
	 * <p>
	 * The <code>WizardDataTransferPage</code> implementation of this method
	 * does nothing. Subclasses wishing to define such components should
	 * reimplement this hook method.
	 * </p>
	 * 
	 * @param optionsGroup
	 *            the parent control
	 */
	protected void createOptionsGroup(Composite parent) {
	}

	/**
	 * Creates a new label with a bold font.
	 * 
	 * @param parent
	 *            the parent control
	 * @param text
	 *            the label text
	 * @return the new label control
	 */
	protected Label createPlainLabel(Composite parent, String text) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(text);
		label.setFont(parent.getFont());
		GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData(data);
		return label;
	}

	/**
	 * Creates a horizontal spacer line that fills the width of its container.
	 * 
	 * @param parent
	 *            the parent control
	 */
	protected void createSpacer(Composite parent) {
		Label spacer = new Label(parent, SWT.NONE);
		GridData data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.BEGINNING;
		spacer.setLayoutData(data);
	}

	/**
	 * Returns whether this page is complete. This determination is made based
	 * upon the current contents of this page's controls. Subclasses wishing to
	 * include their controls in this determination should override the hook
	 * methods <code>validateSourceGroup</code> and/or
	 * <code>validateOptionsGroup</code>.
	 * 
	 * @return <code>true</code> if this page is complete, and
	 *         <code>false</code> if incomplete
	 * @see #validateSourceGroup
	 * @see #validateOptionsGroup
	 */
	protected boolean determinePageCompletion() {
		boolean complete = validateSourceGroup() && validateDestinationGroup()
				&& validateOptionsGroup();

		// Avoid draw flicker by not clearing the error
		// message unless all is valid.
		if (complete) {
			setErrorMessage(null);
		}

		return complete;
	}

	/**
	 * Get a path from the supplied text widget.
	 * 
	 * @return org.eclipse.core.runtime.IPath
	 */
	protected IPath getPathFromText(Text textField) {
		String text = textField.getText();
		// Do not make an empty path absolute so as not to confuse with the root
		if (text.length() == 0) {
			return new Path(text);
		}

		return (new Path(text)).makeAbsolute();
	}

	/**
	 * The <code>WizardDataTransfer</code> implementation of this
	 * <code>IOverwriteQuery</code> method asks the user whether the existing
	 * resource at the given path should be overwritten.
	 * 
	 * @param pathString
	 * @return the user's reply: one of <code>"YES"</code>, <code>"NO"</code>,
	 *         <code>"ALL"</code>, or <code>"CANCEL"</code>
	 */
	public String queryOverwrite(String pathString) {

		Path path = new Path(pathString);

		String messageString;
		// Break the message up if there is a file name and a directory
		// and there are at least 2 segments.
		if (path.getFileExtension() == null || path.segmentCount() < 2) {
			messageString = NLS.bind(
					"''{0}'' already exists.  Would you like to overwrite it?",
					pathString);
		} else {
			messageString = NLS
					.bind("Overwrite ''{0}'' in folder ''{1}''?", path
							.lastSegment(), path.removeLastSegments(1)
							.toOSString());
		}

		final MessageDialog dialog = new MessageDialog(getContainer()
				.getShell(), "Question", null, messageString,
				MessageDialog.QUESTION, new String[] {
						IDialogConstants.YES_LABEL,
						IDialogConstants.YES_TO_ALL_LABEL,
						IDialogConstants.NO_LABEL,
						IDialogConstants.NO_TO_ALL_LABEL,
						IDialogConstants.CANCEL_LABEL }, 0) {
			protected int getShellStyle() {
				return super.getShellStyle() | SWT.SHEET;
			}
		};
		String[] response = new String[] { YES, ALL, NO, NO_ALL, CANCEL };
		// run in syncExec because callback is from an operation,
		// which is probably not running in the UI thread.
		getControl().getDisplay().syncExec(new Runnable() {
			public void run() {
				dialog.open();
			}
		});
		return dialog.getReturnCode() < 0 ? CANCEL : response[dialog
				.getReturnCode()];
	}

	/**
	 * Restores control settings that were saved in the previous instance of
	 * this page.
	 * <p>
	 * The <code>WizardDataTransferPage</code> implementation of this method
	 * does nothing. Subclasses may override this hook method.
	 * </p>
	 */
	protected abstract void restoreWidgetValues();

	/**
	 * Saves control settings that are to be restored in the next instance of
	 * this page.
	 * <p>
	 * The <code>WizardDataTransferPage</code> implementation of this method
	 * does nothing. Subclasses may override this hook method.
	 * </p>
	 */
	protected abstract void saveWidgetValues();

	/**
	 * Determine if the page is complete and update the page appropriately.
	 */
	protected void updatePageCompletion() {
		boolean pageComplete = determinePageCompletion();
		setPageComplete(pageComplete);
		if (pageComplete) {
			setErrorMessage(null);
		}
	}

	/**
	 * Updates the enable state of this page's controls.
	 * <p>
	 * The <code>WizardDataTransferPage</code> implementation of this method
	 * does nothing. Subclasses may extend this hook method.
	 * </p>
	 */
	protected void updateWidgetEnablements() {
		boolean pageComplete = determinePageCompletion();
		setPageComplete(pageComplete);
		if (pageComplete) {
			setMessage(null);
		}
	}

	/**
	 * Returns whether this page's destination specification controls currently
	 * all contain valid values.
	 * <p>
	 * The <code>WizardDataTransferPage</code> implementation of this method
	 * returns <code>true</code>. Subclasses may reimplement this hook method.
	 * </p>
	 * 
	 * @return <code>true</code> indicating validity of all controls in the
	 *         destination specification group
	 */
	protected boolean validateDestinationGroup() {
		return true;
	}

	/**
	 * Returns whether this page's options group's controls currently all
	 * contain valid values.
	 * <p>
	 * The <code>WizardDataTransferPage</code> implementation of this method
	 * returns <code>true</code>. Subclasses may reimplement this hook method.
	 * </p>
	 * 
	 * @return <code>true</code> indicating validity of all controls in the
	 *         options group
	 */
	protected boolean validateOptionsGroup() {
		return true;
	}

	/**
	 * Returns whether this page's source specification controls currently all
	 * contain valid values.
	 * <p>
	 * The <code>WizardDataTransferPage</code> implementation of this method
	 * returns <code>true</code>. Subclasses may reimplement this hook method.
	 * </p>
	 * 
	 * @return <code>true</code> indicating validity of all controls in the
	 *         source specification group
	 */
	protected boolean validateSourceGroup() {
		return true;
	}

	/**
	 * Get the title for an error dialog. Subclasses should override.
	 */
	protected String getErrorDialogTitle() {
		return "Internal error";
	}

	protected boolean inRegularFontMode(Composite parent) {
		return availableRows(parent) > 50;
	}

	private int availableRows(Composite parent) {
		int fontHeight = (parent.getFont().getFontData())[0].getHeight();
		int displayHeight = parent.getDisplay().getClientArea().height;
		return displayHeight / fontHeight;
	}

	public void finish() {
		saveWidgetValues();
	}

}
