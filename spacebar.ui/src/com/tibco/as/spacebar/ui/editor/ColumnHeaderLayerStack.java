package com.tibco.as.spacebar.ui.editor;

import java.util.Map;

import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.GlazedListsSortModel;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.filterrow.DefaultGlazedListsFilterStrategy;
import org.eclipse.nebula.widgets.nattable.filterrow.FilterRowHeaderComposite;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultColumnHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.layer.ColumnHeaderLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultColumnHeaderDataLayer;
import org.eclipse.nebula.widgets.nattable.group.ColumnGroupHeaderLayer;
import org.eclipse.nebula.widgets.nattable.group.ColumnGroupModel;
import org.eclipse.nebula.widgets.nattable.layer.AbstractLayerTransform;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.sort.SortHeaderLayer;
import org.eclipse.nebula.widgets.nattable.util.IClientAreaProvider;

import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.matchers.CompositeMatcherEditor;

public class ColumnHeaderLayerStack<T> extends AbstractLayerTransform {

	private ColumnHeaderLayer columnHeaderLayer;
	private ColumnGroupHeaderLayer columnGroupHeaderLayer;
	private SortHeaderLayer<T> sortableColumnHeaderLayer;
	private IDataProvider columnHeaderDataProvider;
	private DefaultColumnHeaderDataLayer columnHeaderDataLayer;
	private DefaultGlazedListsFilterStrategy<T> filterStrategy;
	private FilterRowHeaderComposite<T> composite;

	public ColumnHeaderLayerStack(SortedList<T> sortedList,
			FilterList<T> filterList, String[] propertyNames,
			Map<String, String> propertyToLabelMap, ILayer bodyLayer,
			SelectionLayer selectionLayer, ColumnGroupModel columnGroupModel,
			IConfigRegistry configRegistry,
			IColumnPropertyAccessor<T> propertyAccessor) {
		columnHeaderDataProvider = new DefaultColumnHeaderDataProvider(
				propertyNames, propertyToLabelMap);
		columnHeaderDataLayer = new DefaultColumnHeaderDataLayer(
				columnHeaderDataProvider);
		columnHeaderLayer = new ColumnHeaderLayer(columnHeaderDataLayer,
				bodyLayer, selectionLayer);
		sortableColumnHeaderLayer = new SortHeaderLayer<T>(columnHeaderLayer,
				new GlazedListsSortModel<T>(sortedList, propertyAccessor,
						configRegistry, columnHeaderDataLayer));
		columnGroupHeaderLayer = new ColumnGroupHeaderLayer(
				sortableColumnHeaderLayer, selectionLayer, columnGroupModel);
		CompositeMatcherEditor<T> matcherEditor = new CompositeMatcherEditor<T>();
		filterList.setMatcherEditor(matcherEditor);
		filterStrategy = new DefaultGlazedListsFilterStrategy<T>(filterList,
				matcherEditor, propertyAccessor, configRegistry);
		composite = new FilterRowHeaderComposite<T>(filterStrategy,
				sortableColumnHeaderLayer, columnHeaderDataProvider,
				configRegistry);
		setUnderlyingLayer(composite);
	}

	@Override
	public void dispose() {
		if (columnHeaderDataLayer != null) {
			columnHeaderDataLayer.dispose();
			columnHeaderDataLayer = null;
		}
		if (columnHeaderLayer != null) {
			columnHeaderLayer.dispose();
			columnHeaderLayer = null;
		}
		if (sortableColumnHeaderLayer != null) {
			sortableColumnHeaderLayer.dispose();
			sortableColumnHeaderLayer = null;
		}
		if (columnGroupHeaderLayer != null) {
			columnGroupHeaderLayer.dispose();
			columnGroupHeaderLayer = null;
		}
		filterStrategy = null;
		composite = null;
		super.dispose();
	}

	@Override
	public void setClientAreaProvider(IClientAreaProvider clientAreaProvider) {
		super.setClientAreaProvider(clientAreaProvider);
	}

	public ColumnGroupHeaderLayer getColumnGroupHeaderLayer() {
		return columnGroupHeaderLayer;
	}

	public ColumnHeaderLayer getColumnHeaderLayer() {
		return columnHeaderLayer;
	}

	public IDataProvider getColumnHeaderDataProvider() {
		return columnHeaderDataProvider;
	}

	public DefaultColumnHeaderDataLayer getColumnHeaderDataLayer() {
		return columnHeaderDataLayer;
	}
}
