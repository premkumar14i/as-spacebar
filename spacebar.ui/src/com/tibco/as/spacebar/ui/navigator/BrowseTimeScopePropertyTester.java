package com.tibco.as.spacebar.ui.navigator;

import org.eclipse.core.expressions.PropertyTester;

import com.tibco.as.space.browser.BrowserDef.TimeScope;

import com.tibco.as.spacebar.ui.model.Space;

public class BrowseTimeScopePropertyTester extends PropertyTester {

	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		if (receiver == null || !(receiver instanceof Space)) {
			return false;
		}
		if (property.equals("name")) {
			for (TimeScope timeScope : TimeScope.values()) {
				if (timeScope.name().equals(expectedValue)) {
					return true;
				}
			}
		}
		return false;
	}

}
