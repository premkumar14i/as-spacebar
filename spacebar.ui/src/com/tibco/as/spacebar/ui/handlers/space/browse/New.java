package com.tibco.as.spacebar.ui.handlers.space.browse;

import com.tibco.as.spacebar.ui.preferences.Preferences;

public class New extends AbstractBrowseHandler {
	@Override
	protected String getTimeScope() {
		return Preferences.TIMESCOPE_NEW;
	}

}
