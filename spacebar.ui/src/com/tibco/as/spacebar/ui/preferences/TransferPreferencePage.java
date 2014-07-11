package com.tibco.as.spacebar.ui.preferences;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TabFolder;

import com.tibco.as.spacebar.ui.Messages;

public class TransferPreferencePage extends TabbedPreferencePage {

	@Override
	protected Composite createTabItemComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		composite.setLayout(layout);
		return composite;
	}

	@Override
	protected void addTabFields(TabFolder folder) {
		createImport(createTab(folder, Messages.Transfer_Import));
		createExport(createTab(folder, Messages.Transfer_Export));
	}

	private Composite createImport(Composite parent) {
		Composite composite = createTabItemComposite(parent);
		addIntegerField(Preferences.IMPORT_BATCH_SIZE,
				Messages.Transfer_Import_Batch_Size, composite,
				Messages.Transfer_Import_Batch_Size_ToolTip, 1,
				Integer.MAX_VALUE);
		addIntegerField(Preferences.IMPORT_WORKER_COUNT,
				Messages.Transfer_Import_Worker_Count, composite,
				Messages.Transfer_Import_Worker_Count_ToolTip, 1,
				Integer.MAX_VALUE);
		addComboField(Preferences.IMPORT_DISTRIBUTION_ROLE,
				Messages.Transfer_Import_Distribution_Role, new String[][] {
						{ Messages.Transfer_Seeder_Label, "SEEDER" },
						{ Messages.Transfer_Leech_Label, "LEECH" },
						{ Messages.Transfer_No_Role_Label, "NO_ROLE" } },
				composite, Messages.Transfer_Import_Distribution_Role_ToolTip);
		addComboField(Preferences.IMPORT_OPERATION,
				Messages.Transfer_Import_Space_Operation, new String[][] {
						{ Messages.Transfer_Get_Label, "GET" },
						{ Messages.Transfer_Load_Label, "LOAD" },
						{ Messages.Transfer_None_Label, "NONE" },
						{ Messages.Transfer_Partial_Label, "PARTIAL" },
						{ Messages.Transfer_Put_Label, "PUT" },
						{ Messages.Transfer_Take_Label, "TAKE" } }, composite,
				Messages.Transfer_Import_Space_Operation_ToolTip);
		addIntegerField(Preferences.IMPORT_WAIT_FOR_READY_TIMEOUT,
				Messages.Transfer_Import_Wait_Timeout, composite,
				Messages.Transfer_Import_Wait_Timeout_ToolTip);
		return composite;
	}

	private Composite createExport(Composite parent) {
		Composite composite = createTabItemComposite(parent);
		addIntegerField(Preferences.EXPORT_BATCH_SIZE,
				Messages.Transfer_Export_Batch_Size, composite,
				Messages.Transfer_Export_Batch_Size_ToolTip, 1,
				Integer.MAX_VALUE);
		addIntegerField(Preferences.EXPORT_WORKER_COUNT,
				Messages.Transfer_Export_Worker_Count, composite,
				Messages.Transfer_Export_Worker_Count_ToolTip, 1,
				Integer.MAX_VALUE);
		Group browseGroup = new Group(composite, SWT.NONE);
		browseGroup.setFont(parent.getFont());
		browseGroup.setText(Messages.Transfer_Export_Browse);
		GridDataFactory.defaultsFor(browseGroup).grab(true, false).span(2, 1)
				.applyTo(browseGroup);
		addComboField(Preferences.EXPORT_TIME_SCOPE,
				Messages.Transfer_Export_Timescope,
				SpaceEditorPreferencePage.TIME_SCOPES, browseGroup,
				Messages.Transfer_Export_Timescope_ToolTip);
		addIntegerField(Preferences.EXPORT_TIMEOUT,
				Messages.Transfer_Export_Timeout, browseGroup,
				Messages.Transfer_Export_Timeout_ToolTip, -1, Integer.MAX_VALUE);
		addIntegerField(Preferences.EXPORT_PREFETCH,
				Messages.Transfer_Export_Prefetch, browseGroup,
				Messages.Transfer_Export_Prefetch_ToolTip, -1,
				Integer.MAX_VALUE);
		addIntegerField(Preferences.EXPORT_QUERY_LIMIT,
				Messages.Transfer_Export_Query_Limit, browseGroup,
				Messages.Transfer_Export_Query_Limit_ToolTip, -1,
				Integer.MAX_VALUE);
		return composite;
	}

}
