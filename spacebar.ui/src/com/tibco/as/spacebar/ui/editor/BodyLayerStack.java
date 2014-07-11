package com.tibco.as.spacebar.ui.editor;

import org.eclipse.nebula.widgets.nattable.freeze.CompositeFreezeLayer;
import org.eclipse.nebula.widgets.nattable.freeze.FreezeLayer;
import org.eclipse.nebula.widgets.nattable.group.ColumnGroupExpandCollapseLayer;
import org.eclipse.nebula.widgets.nattable.group.ColumnGroupModel;
import org.eclipse.nebula.widgets.nattable.group.ColumnGroupReorderLayer;
import org.eclipse.nebula.widgets.nattable.hideshow.ColumnHideShowLayer;
import org.eclipse.nebula.widgets.nattable.layer.AbstractLayerTransform;
import org.eclipse.nebula.widgets.nattable.layer.IUniqueIndexLayer;
import org.eclipse.nebula.widgets.nattable.layer.config.ColumnStyleChooserConfiguration;
import org.eclipse.nebula.widgets.nattable.reorder.ColumnReorderLayer;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;

public class BodyLayerStack extends AbstractLayerTransform {

	private ColumnReorderLayer columnReorderLayer;
	private ColumnGroupReorderLayer columnGroupReorderLayer;
	private ColumnHideShowLayer columnHideShowLayer;
	private ColumnGroupExpandCollapseLayer columnGroupExpandCollapseLayer;
	private SelectionLayer selectionLayer;
	private ViewportLayer viewportLayer;
	private FreezeLayer freezeLayer;
	private CompositeFreezeLayer compositeFreezeLayer;

	public BodyLayerStack(IUniqueIndexLayer layer,
			ColumnGroupModel columnGroupModel) {
		columnReorderLayer = new ColumnReorderLayer(layer);
		columnGroupReorderLayer = new ColumnGroupReorderLayer(
				columnReorderLayer, columnGroupModel);
		columnHideShowLayer = new ColumnHideShowLayer(columnGroupReorderLayer);
		columnGroupExpandCollapseLayer = new ColumnGroupExpandCollapseLayer(
				columnHideShowLayer, columnGroupModel);
		selectionLayer = new SelectionLayer(columnGroupExpandCollapseLayer);
		viewportLayer = new ViewportLayer(selectionLayer);
		freezeLayer = new FreezeLayer(selectionLayer);
		compositeFreezeLayer = new CompositeFreezeLayer(freezeLayer,
				viewportLayer, selectionLayer);
		setUnderlyingLayer(compositeFreezeLayer);
		addConfiguration(new ColumnStyleChooserConfiguration(this,
				selectionLayer));
	}

	@Override
	public void dispose() {
		super.dispose();
		compositeFreezeLayer.dispose();
		freezeLayer.dispose();
		viewportLayer.dispose();
		selectionLayer.dispose();
		columnGroupExpandCollapseLayer.dispose();
		columnHideShowLayer.dispose();
		columnGroupReorderLayer.dispose();
		columnReorderLayer.dispose();
	}

	public ColumnHideShowLayer getColumnHideShowLayer() {
		return columnHideShowLayer;
	}

	public SelectionLayer getSelectionLayer() {
		return selectionLayer;
	}

	public ViewportLayer getViewportLayer() {
		return viewportLayer;
	}

}
