package com.tibco.as.spacebar.ui.navigator;

import java.text.Collator;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import com.tibco.as.spacebar.ui.model.IElement;

public class MetaspaceSorter extends ViewerSorter {

	public MetaspaceSorter() {
	}

	public MetaspaceSorter(Collator collator) {
		super(collator);
	}

	@Override
	public int compare(Viewer viewer, Object object1, Object object2) {
		if (object1 instanceof IElement && object2 instanceof IElement) {
			IElement element1 = (IElement) object1;
			IElement element2 = (IElement) object2;
			
			return element1.getName().compareTo(element2.getName());
		}
		return super.compare(viewer, object1, object2);
	}

	private int getIndex(IElement element) {
		IElement parent = element.getParent();
		if (parent == null) {
			return -1;
		}
		return parent.getChildren().indexOf(element);
	}

}
