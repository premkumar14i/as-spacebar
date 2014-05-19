package com.tibco.as.spacebar.ui.wizards.space.browse;

import java.util.Map;

import org.eclipse.swt.widgets.Composite;

import com.tibco.as.spacebar.ui.wizards.transfer.ExportEditor;

import com.tibco.as.io.Export;
import com.tibco.as.space.browser.BrowserDef.TimeScope;

public class BrowseEditor extends ExportEditor {

	private Map<String, Export> defaults;

	public BrowseEditor(Composite parent, int style, Export browse,
			Map<String, Export> defaults) {
		super(parent, style, browse);
		this.defaults = defaults;
	}

	@Override
	protected void timeScopeUpdate() {
		Export browse = getExport();
		TimeScope timeScope = browse.getTimeScope();
		if (timeScope != null) {
			String timeScopeName = timeScope.name();
			if (defaults != null && defaults.containsKey(timeScopeName)) {
				Export defaultBrowse = defaults.get(timeScopeName);
				prefetchText.setText(getText(defaultBrowse.getPrefetch()));
				queryLimitText.setText(getText(defaultBrowse.getQueryLimit()));
				timeoutText.setText(getText(defaultBrowse.getTimeout()));
			}
		}
		super.timeScopeUpdate();
	}

	private String getText(Long value) {
		if (value == null) {
			return "";
		}
		return String.valueOf(value);
	}

}
