package com.tibco.as.spacebar.ui.editor.continuous;

import java.util.Arrays;

import org.eclipse.nebula.widgets.nattable.blink.BlinkingCellResolver;

public class BlinkResolver extends BlinkingCellResolver {

	private String[] configLabels;

	public BlinkResolver(String... labels) {
		configLabels = Arrays.copyOf(labels, labels.length + 1);
	}

	public void setLast(String label) {
		configLabels[configLabels.length - 1] = label;
	}

	public String[] resolve(Object oldValue, Object newValue) {
		return configLabels;
	}

	protected String[] getConfigLabels() {
		return configLabels;
	}
}
