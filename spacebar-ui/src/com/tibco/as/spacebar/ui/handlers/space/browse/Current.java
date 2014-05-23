package com.tibco.as.spacebar.ui.handlers.space.browse;

import com.tibco.as.spacebar.ui.preferences.Preferences;

public class Current extends AbstractBrowseHandler {

	@Override
	protected String getTimeScope() {
		return Preferences.TIMESCOPE_CURRENT;
	}

}
