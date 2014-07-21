package com.tibco.as.spacebar.ui.navigator;

import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.tibco.as.spacebar.ui.SpaceBarPlugin;
import com.tibco.as.spacebar.ui.model.Field;
import com.tibco.as.spacebar.ui.model.IElement;
import com.tibco.as.spacebar.ui.model.Index;
import com.tibco.as.spacebar.ui.model.Indexes;
import com.tibco.as.spacebar.ui.model.Member;
import com.tibco.as.spacebar.ui.model.Members;
import com.tibco.as.spacebar.ui.model.Metaspace;
import com.tibco.as.spacebar.ui.model.Metaspaces;
import com.tibco.as.spacebar.ui.model.Space;
import com.tibco.as.spacebar.ui.model.SpaceFields;
import com.tibco.as.spacebar.ui.model.SpaceMember;
import com.tibco.as.spacebar.ui.model.Spaces;

public class MetaspaceLabelProvider extends LabelProvider implements
		IFontProvider {

	private Font boldFont;

	@Override
	public String getText(Object element) {
		if (element instanceof IElement) {
			return ((IElement) element).getName();
		}
		return null;
	}

	@Override
	public Image getImage(Object object) {
		if (object instanceof Metaspaces) {
			return null;
		}
		if (object instanceof Metaspace) {
			Metaspace metaspace = (Metaspace) object;
			if (metaspace.isConnected()) {
				return getImage(com.tibco.as.spacebar.ui.Image.METASPACE_CONNECTED);
			}
			return getImage(com.tibco.as.spacebar.ui.Image.METASPACE_DISCONNECTED);
		}
		if (object instanceof Members) {
			return getImage(com.tibco.as.spacebar.ui.Image.MEMBERS);
		}
		if (object instanceof SpaceMember) {
			return getImage(((SpaceMember) object).isSeeder() ? com.tibco.as.spacebar.ui.Image.MEMBER_SEEDER
					: com.tibco.as.spacebar.ui.Image.MEMBER);
		}
		if (object instanceof Member) {
			return getImage(com.tibco.as.spacebar.ui.Image.MEMBER);
		}
		if (object instanceof Spaces) {
			return getImage(com.tibco.as.spacebar.ui.Image.SPACES);
		}
		if (object instanceof Space) {
			return getImage(com.tibco.as.spacebar.ui.Image.SPACE);
		}
		if (object instanceof SpaceFields) {
			return getImage(com.tibco.as.spacebar.ui.Image.FIELDS);
		}
		if (object instanceof Field) {
			Field field = (Field) object;
			if (field.isKey()) {
				if (field.isDistribution()) {
					return getImage(com.tibco.as.spacebar.ui.Image.DISTRIBUTION);
				}
				return getImage(com.tibco.as.spacebar.ui.Image.KEY);
			}
			return getImage(com.tibco.as.spacebar.ui.Image.FIELD);
		}
		if (object instanceof Indexes) {
			return getImage(com.tibco.as.spacebar.ui.Image.INDEXES);
		}
		if (object instanceof Index) {
			return getImage(com.tibco.as.spacebar.ui.Image.INDEX);
		}
		return null;
	}

	private Image getImage(com.tibco.as.spacebar.ui.Image image) {
		return SpaceBarPlugin.getDefault().getImage(image);
	}

	@Override
	public Font getFont(Object element) {
		// if (element instanceof Field) {
		// Field field = (Field) element;
		// if (isKey(field)) {
		// return getBoldFont();
		// }
		// }
		if (element instanceof Member) {
			Member member = (Member) element;
			if (member.isSelf()) {
				return getBoldFont();
			}
		}
		return null;
	}

	private Font getBoldFont() {
		if (boldFont == null) {
			Font originalFont = Display.getDefault().getSystemFont();
			FontData[] fontData = originalFont.getFontData();
			// Adding the bold attribute
			for (int i = 0; i < fontData.length; i++) {
				fontData[i].setStyle(fontData[i].getStyle() | SWT.BOLD);
			}
			boldFont = new Font(Display.getDefault(), fontData);
		}
		return boldFont;
	}

	@Override
	public void dispose() {
		if (boldFont != null) {
			boldFont.dispose();
		}
		super.dispose();
	}

}