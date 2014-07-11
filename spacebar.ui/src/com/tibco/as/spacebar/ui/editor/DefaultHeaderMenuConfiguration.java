package com.tibco.as.spacebar.ui.editor;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.ui.menu.HeaderMenuConfiguration;
import org.eclipse.nebula.widgets.nattable.ui.menu.PopupMenuBuilder;

public class DefaultHeaderMenuConfiguration extends HeaderMenuConfiguration {

	public DefaultHeaderMenuConfiguration(NatTable natTable) {
		super(natTable);
	}

	@Override
	protected PopupMenuBuilder createColumnHeaderMenu(NatTable natTable) {
		return super.createColumnHeaderMenu(natTable)
				.withColumnChooserMenuItem();
	}

}
