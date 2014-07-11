package com.tibco.as.spacebar.ui.adapter;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.eclipse.ui.model.IWorkbenchAdapter2;
import org.eclipse.ui.model.IWorkbenchAdapter3;

/**
 * Dispenses adapters for various core objects. Returns IWorkbenchAdapter
 * adapters, used for displaying, navigating, and populating menus for core
 * objects.
 */
public class ElementAdapterFactory implements IAdapterFactory {

	private Object elementAdapter = new ElementAdapter();

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Object o, Class adapterType) {
		if (adapterType.isInstance(o)) {
			return o;
		}
		if (adapterType == IWorkbenchAdapter.class
				|| adapterType == IWorkbenchAdapter2.class
				|| adapterType == IWorkbenchAdapter3.class) {
			return elementAdapter;
		}
		return null;
	}

	@Override
	public Class<?>[] getAdapterList() {
		return new Class[] { IWorkbenchAdapter.class, IWorkbenchAdapter2.class,
				IWorkbenchAdapter3.class };
	}

}
