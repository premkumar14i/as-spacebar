package com.tibco.as.spacebar.ui.model;

import java.util.ArrayList;
import java.util.List;

public class Metaspaces extends Element {

	public Metaspaces() {
		super(null, null);
	}

	public List<Metaspace> getConnectedMetaspaces() {
		List<Metaspace> metaspaces = new ArrayList<Metaspace>();
		for (Element element : getChildren()) {
			Metaspace metaspace = (Metaspace) element;
			if (metaspace.isConnected()) {
				metaspaces.add(metaspace);
			}
		}
		return metaspaces;
	}

}
