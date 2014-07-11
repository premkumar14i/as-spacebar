package com.tibco.as.spacebar.ui.adapter;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.model.WorkbenchAdapter;

import com.tibco.as.spacebar.ui.Image;
import com.tibco.as.spacebar.ui.SpaceBarPlugin;
import com.tibco.as.spacebar.ui.model.Field;
import com.tibco.as.spacebar.ui.model.IElement;
import com.tibco.as.spacebar.ui.model.Index;
import com.tibco.as.spacebar.ui.model.Indexes;
import com.tibco.as.spacebar.ui.model.Metaspace;
import com.tibco.as.spacebar.ui.model.Space;
import com.tibco.as.spacebar.ui.model.SpaceFields;

public class ElementAdapter extends WorkbenchAdapter {

	@Override
	public Object[] getChildren(Object o) {
		return ((IElement) o).getChildren().toArray();
	}

	@Override
	public ImageDescriptor getImageDescriptor(Object object) {
		SpaceBarPlugin activator = SpaceBarPlugin.getDefault();
		if (object instanceof Field) {
			return activator.getImageDescriptor(Image.FIELD);
		}
		if (object instanceof Metaspace) {
			Metaspace metaspace = (Metaspace) object;
			if (metaspace.isConnected()) {
				return activator.getImageDescriptor(Image.METASPACE_CONNECTED);
			}
			return activator.getImageDescriptor(Image.METASPACE_DISCONNECTED);
		}
		if (object instanceof Space) {
			return activator.getImageDescriptor(Image.SPACE);
		}
		if (object instanceof SpaceFields) {
			return activator.getImageDescriptor(Image.FIELDS);
		}
		if (object instanceof Indexes) {
			return activator.getImageDescriptor(Image.INDEXES);
		}
		if (object instanceof Index) {
			return activator.getImageDescriptor(Image.INDEX);
		}
		return null;
	}

	public String getLabel(Object o) {
		return ((IElement) o).getName();
	}
}
