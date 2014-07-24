package com.tibco.as.spacebar.ui.navigator;

import org.eclipse.core.expressions.PropertyTester;

import com.tibco.as.spacebar.ui.model.Metaspace;

public class MetaspacePropertyTester extends PropertyTester {

	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		if (receiver == null || !(receiver instanceof Metaspace)) {
			return false;
		}
		if (property.equals("connected")) {
			return ((Metaspace) receiver).isConnected();
		}
		return false;
	}
}
