package com.tibco.as.spacebar.ui.wizards.space;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * A content provider for the tuple def editor table viewer.
 * 
 */
public class ListContentProvider implements IStructuredContentProvider {

	private List<?> list;

	@Override
	public Object[] getElements(Object input) {
		return list.toArray();
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.list = (List<?>) newInput;
	}

	@Override
	public void dispose() {
		list = null;
	}

}
