package com.tibco.as.spacebar.ui.wizards.space.field;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.OwnerDrawLabelProvider;
import org.eclipse.nebula.widgets.nattable.util.GUIHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;

public abstract class CheckBoxLabelProvider extends OwnerDrawLabelProvider {

	private static final String CHECKED = "CHECKED";

	private static final String UNCHECKED = "UNCHECKED";

	static {
		if (JFaceResources.getImageRegistry().get(CHECKED) == null) {
			JFaceResources.getImageRegistry().put(CHECKED,
					GUIHelper.getImage("checked"));
		}
		if (JFaceResources.getImageRegistry().get(UNCHECKED) == null) {
			JFaceResources.getImageRegistry().put(UNCHECKED,
					GUIHelper.getImage("unchecked"));
		}
	}

	public Image getImage(Object element) {
		if (isChecked(element)) {
			return JFaceResources.getImageRegistry().get(CHECKED);
		} else {
			return JFaceResources.getImageRegistry().get(UNCHECKED);
		}
	}

	protected void measure(Event event, Object element) {
		event.height = getImage(element).getBounds().height;
	}

	protected void paint(Event event, Object element) {

		Image img = getImage(element);

		if (img != null) {
			Rectangle bounds;

			if (event.item instanceof TableItem) {
				bounds = ((TableItem) event.item).getBounds(event.index);
			} else {
				bounds = ((TreeItem) event.item).getBounds(event.index);
			}

			Rectangle imgBounds = img.getBounds();
			bounds.width /= 2;
			bounds.width -= imgBounds.width / 2;
			bounds.height /= 2;
			bounds.height -= imgBounds.height / 2;

			int x = bounds.width > 0 ? bounds.x + bounds.width : bounds.x;
			int y = bounds.height > 0 ? bounds.y + bounds.height : bounds.y;

			if (SWT.getPlatform().equals("carbon")) {
				event.gc.drawImage(img, x + 2, y - 1);
			} else {
				event.gc.drawImage(img, x, y - 1);
			}

		}
	}

	protected abstract boolean isChecked(Object element);
}