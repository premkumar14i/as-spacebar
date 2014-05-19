package com.tibco.as.spacebar.ui.editor;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.tibco.as.spacebar.ui.Image;
import com.tibco.as.spacebar.ui.SpaceBarPlugin;
import com.tibco.as.spacebar.ui.editor.continuous.ContinuousBrowser;
import com.tibco.as.spacebar.ui.editor.snapshot.SpaceEditor;
import com.tibco.as.spacebar.ui.model.Space;

import com.tibco.as.io.Export;

public class SpaceEditorInput implements IEditorInput {

	private Space space;
	private Export export;

	public SpaceEditorInput(Space space, Export export) {
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

	public Export getExport() {
		return export;
	}

	public void setExport(Export export) {
		this.export = export;
	}

	public Space getSpace() {
		return space;
	}

	@Override
	public String getName() {
		return NLS.bind("{0} {1}", space.getName(),
				export.getTimeScope());
	}

	public String getEditorId() {
		if (export.isAllOrNew()) {
			return ContinuousBrowser.EDITOR_ID;
		}
		return SpaceEditor.EDITOR_ID;
	}

}
