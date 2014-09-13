package com.tibco.as.spacebar.ui.editor;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.tibco.as.spacebar.ui.Image;
import com.tibco.as.spacebar.ui.SpaceBarPlugin;
import com.tibco.as.spacebar.ui.model.Space;

public class SpaceEditorInput implements IEditorInput {

	private Space space;
	private SpaceEditorExport export;

	public SpaceEditorInput(Space space, SpaceEditorExport export) {
		this.space = space;
		this.export = export;
		export.setSpaceName(space.getName());
	}

	public boolean exists() {
		return export != null;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return SpaceBarPlugin.getDefault().getImageDescriptor(Image.SPACE);
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return getName();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		return Platform.getAdapterManager().getAdapter(this, adapter);
	}

	public SpaceEditorExport getExport() {
		return export;
	}

	public void setExport(SpaceEditorExport export) {
		this.export = export;
	}

	public Space getSpace() {
		return space;
	}

	@Override
	public String getName() {
		return NLS.bind("{0} {1}", space, export.getTimeScope());
	}

	public String getEditorId() {
		switch (export.getTimeScope()) {
		case ALL:
			return AbstractBrowser.EDITOR_ID_ALL;
		case CURRENT:
			return AbstractBrowser.EDITOR_ID_CURRENT;
		case NEW:
			return AbstractBrowser.EDITOR_ID_NEW;
		default:
			return AbstractBrowser.EDITOR_ID_SNAPSHOT;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((export == null) ? 0 : export.hashCode());
		result = prime * result + ((space == null) ? 0 : space.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SpaceEditorInput other = (SpaceEditorInput) obj;
		if (export == null) {
			if (other.export != null)
				return false;
		} else if (!export.equals(other.export))
			return false;
		if (space == null) {
			if (other.space != null)
				return false;
		} else if (!space.equals(other.space))
			return false;
		return true;
	}

}
