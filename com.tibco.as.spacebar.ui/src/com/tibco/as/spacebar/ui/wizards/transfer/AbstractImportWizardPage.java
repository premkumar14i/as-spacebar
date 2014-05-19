package com.tibco.as.spacebar.ui.wizards.transfer;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.FileSystemElement;
import org.eclipse.ui.model.AdaptableList;

import com.tibco.as.spacebar.ui.SWTFactory;
import com.tibco.as.spacebar.ui.SpaceBarPlugin;

public class AbstractImportWizardPage extends WizardResourceImportPage {

	/**
	 * Represents a view of the file system, in that we only care about folders
	 * and files with the proper extension
	 * 
	 */
	class ImportFileSystemElement extends FileSystemElement {

		private boolean populated = false;

		public ImportFileSystemElement(String name, FileSystemElement parent,
				boolean isDirectory) {
			super(name, parent, isDirectory);
		}

		public void setPopulated() {
			populated = true;
		}

		public boolean isPopulated() {
			return populated;
		}

		public AdaptableList getFiles() {
			if (!populated) {
				populateElementChildren();
			}
			return super.getFiles();
		}

		public AdaptableList getFolders() {
			if (!populated) {
				populateElementChildren();
			}
			return super.getFolders();
		}

		/**
		 * Populates the children of the specified parent
		 * <code>FileSystemElement</code>
		 * 
		 * @param element
		 * @param folderonly
		 */
		private void populateElementChildren() {
			FileSystemStructureProvider provider = FileSystemStructureProvider.INSTANCE;
			List<?> allchildren = provider.getChildren(this
					.getFileSystemObject());
			File child = null;
			ImportFileSystemElement newelement = null;
			Iterator<?> iter = allchildren.iterator();
			while (iter.hasNext()) {
				child = (File) iter.next();
				if (child.isFile()) {
					Path childpath = new Path(child.getAbsolutePath());
					String extension = childpath.getFileExtension();
					if (extensions.contains(extension)) {
						newelement = new ImportFileSystemElement(
								provider.getLabel(child), this,
								provider.isFolder(child));
						newelement.setFileSystemObject(child);
					}
				} else {
					newelement = new ImportFileSystemElement(
							provider.getLabel(child), this,
							provider.isFolder(child));
					newelement.setFileSystemObject(child);
				}
			}
			setPopulated();
		}
	}

	private static final String EMPTY_STRING = "";

	private static final String STORE_SOURCE_NAME = "STORE_SOURCE_NAME";

	private Collection<String> extensions;

	private Text fFromDirectory = null;

	/**
	 * Constructor
	 */
	protected AbstractImportWizardPage(String name, String message,
			String[] extensions) {
		super(name, new StructuredSelection());
		this.extensions = Arrays.asList(extensions);
		setTitle("Import Spaces");
		setMessage(message);
	}

	@Override
	public void createControl(Composite parent) {
		Composite comp = SWTFactory.createComposite(parent, 1, 1,
				GridData.FILL_BOTH);
		createRootDirectoryGroup(comp);
		createFileSelectionGroup(comp);
		restoreWidgetValues();
		setControl(comp);
		PlatformUI.getWorkbench().getHelpSystem()
				.setHelp(comp, SpaceBarPlugin.ID_PLUGIN + "." + getName());
		setPageComplete(false);
	}

	@Override
	protected void restoreWidgetValues() {
		IDialogSettings settings = getDialogSettings();
		if (settings == null) {
			return;
		}
		String key = getSettingKey(STORE_SOURCE_NAME);
		String sourceName = settings.get(key);
		if (sourceName == null) {
			sourceName = EMPTY_STRING;
		}
		fFromDirectory.setText(sourceName);
		resetSelection(new Path(sourceName));
	}

	@Override
	protected void saveWidgetValues() {
		IDialogSettings settings = getDialogSettings();
		if (settings == null) {
			return;
		}
		String key = getSettingKey(STORE_SOURCE_NAME);
		settings.put(key, fFromDirectory.getText().trim());
	}

	@Override
	protected void updateWidgetEnablements() {
		setPageComplete(determinePageCompletion());
	}

	@Override
	protected boolean determinePageCompletion() {
		if (fFromDirectory.getText().trim().equals(EMPTY_STRING)) {
			setErrorMessage("You must select a root folder");
			return false;
		}
		if (selectionGroup.getCheckedElementCount() < 1) {
			setErrorMessage("You must select at least one file to import");
			return false;
		}
		setErrorMessage(null);
		setMessage("Import spaces from CSV files");
		return true;
	}

	@Override
	protected void createSourceGroup(Composite parent) {
	}

	/**
	 * Create the group for creating the root directory
	 */
	protected void createRootDirectoryGroup(Composite parent) {
		Composite comp = SWTFactory.createComposite(parent, parent.getFont(),
				3, 1, GridData.FILL_HORIZONTAL, 0, 0);
		SWTFactory.createLabel(comp, "From &Directory:", 1);
		// source name entry field
		fFromDirectory = SWTFactory.createText(comp, SWT.BORDER | SWT.SINGLE
				| SWT.READ_ONLY, 1, GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL);
		// source browse button
		Button browse = SWTFactory.createPushButton(comp, "Brows&e...", null);
		browse.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dd = new DirectoryDialog(getContainer()
						.getShell());
				dd.setText("Import Spaces");
				String filename = dd.open();
				if (filename != null) {
					IPath path = new Path(filename);
					if (path != null) {
						fFromDirectory.setText(path.toString());
						resetSelection(path);
						setPageComplete(determinePageCompletion());
					}
				}
			}
		});
	}

	/**
	 * Resets the selection of the tree root element for the viewer
	 * 
	 * @param path
	 *            the path from the text widget
	 */
	protected void resetSelection(final IPath path) {
		BusyIndicator.showWhile(getShell().getDisplay(), new Runnable() {
			public void run() {
				File file = new File(path.toOSString());
				ImportFileSystemElement dummyparent = new ImportFileSystemElement(
						EMPTY_STRING, null, true);
				dummyparent.setPopulated();
				ImportFileSystemElement element = new ImportFileSystemElement(
						FileSystemStructureProvider.INSTANCE.getLabel(file),
						dummyparent, file.isDirectory());
				element.setFileSystemObject(file);
				element.getFiles();
				selectionGroup.setRoot(dummyparent);
			}
		});
	}

	@Override
	protected ITreeContentProvider getFileProvider() {
		return new BaseWorkbenchContentProvider() {
			public Object[] getChildren(Object o) {
				if (o instanceof ImportFileSystemElement) {
					ImportFileSystemElement element = (ImportFileSystemElement) o;
					return element.getFiles().getChildren(element);
				}
				return new Object[0];
			}
		};
	}

	@Override
	protected ITreeContentProvider getFolderProvider() {
		return new BaseWorkbenchContentProvider() {
			public Object[] getChildren(Object o) {
				if (o instanceof ImportFileSystemElement) {
					ImportFileSystemElement element = (ImportFileSystemElement) o;
					return element.getFolders().getChildren();
				}
				return new Object[0];
			}

			public boolean hasChildren(Object o) {
				if (o instanceof ImportFileSystemElement) {
					ImportFileSystemElement element = (ImportFileSystemElement) o;
					if (element.isPopulated()) {
						return getChildren(element).length > 0;
					}
					// If we have not populated then wait until asked
					return true;
				}
				return false;
			}
		};
	}

}
