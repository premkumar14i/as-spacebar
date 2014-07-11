package com.tibco.as.spacebar.ui.adapter;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;

import com.tibco.as.spacebar.ui.editor.SpaceEditorInput;
import com.tibco.as.spacebar.ui.model.Field;
import com.tibco.as.spacebar.ui.model.IElement;
import com.tibco.as.spacebar.ui.model.Index;
import com.tibco.as.spacebar.ui.model.Member;
import com.tibco.as.spacebar.ui.model.Metaspace;
import com.tibco.as.spacebar.ui.model.Space;
import com.tibco.as.spacebar.ui.model.SpaceMember;

@SuppressWarnings("rawtypes")
public class AdapterFactory implements IAdapterFactory {

	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adapterType == IPropertySource.class) {
			if (adaptableObject instanceof Metaspace) {
				return new MetaspacePropertySource((Metaspace) adaptableObject);
			}
			if (adaptableObject instanceof Space) {
				return new SpacePropertySource((Space) adaptableObject);
			}
			if (adaptableObject instanceof Field) {
				return new FieldPropertySource((Field) adaptableObject);
			}
			if (adaptableObject instanceof Index) {
				return new IndexPropertySource((Index) adaptableObject);
			}
			if (adaptableObject instanceof SpaceEditorInput) {
				return new BrowserInputPropertySource(
						(SpaceEditorInput) adaptableObject);
			}
			if (adaptableObject instanceof SpaceMember) {
				return new SpaceMemberPropertySource(
						(SpaceMember) adaptableObject);
			}
			if (adaptableObject instanceof Member) {
				return new MemberPropertySource((Member) adaptableObject);
			}
		}
		if (adapterType == IElement.class) {
			if (adaptableObject instanceof SpaceEditorInput) {
				return ((SpaceEditorInput) adaptableObject).getSpace();
			}
		}
		return null;
	}

	@Override
	public Class[] getAdapterList() {
		return new Class[] { IPropertySource.class };
	}

}