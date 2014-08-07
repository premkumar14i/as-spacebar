package com.tibco.as.spacebar.ui.wizards.space;

import org.eclipse.jface.viewers.CellLabelProvider;

public class ColumnConfig {

	private String name;

	private int weight;

	private Integer minimumWidth;

	private boolean resizable;

	private CellLabelProvider labelProvider;

	private int style;

	public ColumnConfig(String name, int style, int weight,
			Integer minimumWidth, boolean resizable,
			CellLabelProvider labelProvider) {
		this.name = name;
		this.style = style;
		this.weight = weight;
		this.minimumWidth = minimumWidth;
		this.resizable = resizable;
		this.labelProvider = labelProvider;
	}

	public String getName() {
		return name;
	}

	public int getStyle() {
		return style;
	}

	public int getWeight() {
		return weight;
	}

	public Integer getMinimumWidth() {
		return minimumWidth;
	}

	public boolean isResizable() {
		return resizable;
	}

	public CellLabelProvider getLabelProvider() {
		return labelProvider;
	}

}
