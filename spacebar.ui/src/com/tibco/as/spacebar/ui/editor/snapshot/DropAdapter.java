package com.tibco.as.spacebar.ui.editor.snapshot;

import java.io.File;

import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;

import com.tibco.as.spacebar.ui.SpaceBarPlugin;
import com.tibco.as.spacebar.ui.preferences.Preferences;
import com.tibco.as.spacebar.ui.transfer.MetaspaceTransferJob;

import com.tibco.as.io.IMetaspaceTransfer;
import com.tibco.as.io.IOUtils;
import com.tibco.as.excel.ExcelImport;
import com.tibco.as.excel.ExcelImporter;
import com.tibco.as.file.text.delimited.DelimitedImport;
import com.tibco.as.file.text.delimited.DelimitedImporter;
import com.tibco.as.space.Metaspace;

public class DropAdapter extends DropTargetAdapter {

	public final static String EXTENSION_CSV = "csv";

	public final static String EXTENSION_XLS = "xls";

	public final static String EXTENSION_XLSX = "xlsx";

	private FileTransfer fileTransfer;

	private SnapshotBrowser editor;

	public DropAdapter(FileTransfer fileTransfer, SnapshotBrowser browser) {
		this.fileTransfer = fileTransfer;
		this.editor = browser;
	}

	@Override
	public void drop(DropTargetEvent event) {
		if (fileTransfer.isSupportedType(event.currentDataType)) {
			for (String filename : (String[]) event.data) {
				File file = new File(filename);
				String extension = IOUtils.getExtension(filename);
				Metaspace metaspace = editor.getSpace().getParent().getParent()
						.getConnection().getMetaspace();
				if (extension.equals(EXTENSION_CSV)) {
					DelimitedImporter importer = new DelimitedImporter(
							metaspace, file.getParentFile());
					importer.setOutputStream(editor.getChangeOutputStream());
					DelimitedImport config = (DelimitedImport) importer
							.addImport(file.getName());
					Preferences.configureDelimitedImport(config);
					execute(importer, file);
				} else if (extension.equals(EXTENSION_XLS)
						|| extension.equals(EXTENSION_XLSX)) {
					ExcelImporter importer = new ExcelImporter(metaspace, file);
					importer.setOutputStream(editor.getChangeOutputStream());
					ExcelImport config = new ExcelImport();
					Preferences.configureExcelImport(config);
					importer.setDefaultTransfer(config);
					execute(importer, file);
				} else {
					SpaceBarPlugin
							.errorDialog(
									"Unsupported File Extension",
									NLS.bind(
											"File ''{0}'' has unknown extension ''{1}''",
											filename, extension));
				}
				editor.activate();
			}
		}
	}

	private <T> void execute(IMetaspaceTransfer transfer, File file) {
		new MetaspaceTransferJob("Import",
				NLS.bind("Importing file {0}", file), transfer, NLS.bind(
						"Could not import file ''{0}''", file)).schedule();
	}
}
