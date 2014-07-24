package com.tibco.as.spacebar.ui.navigator;

import org.eclipse.core.expressions.PropertyTester;

import com.tibco.as.spacebar.ui.model.Space;

public class SpacePropertyTester extends PropertyTester {

	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		if (receiver == null || !(receiver instanceof Space)) {
			return false;
		}
		if (property.equals("joined")) {
			return ((Space) receiver).isJoined();
		}
		if (property.equals("suspended")) {
			return ((Space) receiver).isSuspended();
		}
		if (property.equals("ready")) {
			return ((Space) receiver).isReady();
		}
		return false;
	}
}
