package com.tibco.as.spacebar.ui.navigator;

import java.text.Collator;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import com.tibco.as.spacebar.ui.model.Element;

public class MetaspaceSorter extends ViewerSorter {

	public MetaspaceSorter() {
	}

	public MetaspaceSorter(Collator collator) {
		super(collator);
	}

	@Override
	public int compare(Viewer viewer, Object object1, Object object2) {
		if (object1 instanceof Element && object2 instanceof Element) {
			Element element1 = (Element) object1;
			Element element2 = (Element) object2;
			return element1.getIndex() - element2.getIndex();
		}
		return super.compare(viewer, object1, object2);
	}

}
