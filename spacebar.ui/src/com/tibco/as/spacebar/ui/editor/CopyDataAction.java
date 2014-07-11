package com.tibco.as.spacebar.ui.editor;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.copy.command.CopyDataToClipboardCommand;
import org.eclipse.nebula.widgets.nattable.ui.action.IKeyAction;
import org.eclipse.swt.events.KeyEvent;

import com.tibco.as.spacebar.ui.SpaceBarPlugin;
import com.tibco.as.spacebar.ui.StringUtils;
import com.tibco.as.spacebar.ui.preferences.Preferences;

public class CopyDataAction implements IKeyAction {

	@Override
	public void run(NatTable natTable, KeyEvent event) {
		IPreferenceStore store = SpaceBarPlugin.getDefault().getPreferenceStore();
		String delimiter = StringUtils.unescape(store
				.getString(Preferences.EXPORT_CLIPBOARD_SEPARATOR));
		natTable.doCommand(new CopyDataToClipboardCommand(delimiter, System
				.getProperty("line.separator"), natTable.getConfigRegistry()));
	}

}
