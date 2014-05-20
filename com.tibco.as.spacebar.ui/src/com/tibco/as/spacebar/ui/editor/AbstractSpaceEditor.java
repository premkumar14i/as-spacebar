package com.tibco.as.spacebar.ui.editor;

import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.columnChooser.command.DisplayColumnChooserCommandHandler;
import org.eclipse.nebula.widgets.nattable.config.AbstractUiBindingConfiguration;
import org.eclipse.nebula.widgets.nattable.config.ConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.IConfiguration;
import org.eclipse.nebula.widgets.nattable.copy.command.CopyDataCommandHandler;
import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.GlazedListsDataProvider;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.GlazedListsEventLayer;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultCornerDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultRowHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.layer.CornerLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultRowHeaderDataLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.GridLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.RowHeaderLayer;
import org.eclipse.nebula.widgets.nattable.group.ColumnGroupModel;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.layer.IUniqueIndexLayer;
import org.eclipse.nebula.widgets.nattable.layer.cell.ColumnOverrideLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.resize.action.AutoResizeColumnAction;
import org.eclipse.nebula.widgets.nattable.resize.action.ColumnResizeCursorAction;
import org.eclipse.nebula.widgets.nattable.resize.event.ColumnResizeEventMatcher;
import org.eclipse.nebula.widgets.nattable.resize.mode.ColumnResizeDragMode;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.sort.config.SingleClickSortConfiguration;
import org.eclipse.nebula.widgets.nattable.tooltip.NatTableContentTooltip;
import org.eclipse.nebula.widgets.nattable.ui.action.ClearCursorAction;
import org.eclipse.nebula.widgets.nattable.ui.action.NoOpMouseAction;
import org.eclipse.nebula.widgets.nattable.ui.binding.UiBindingRegistry;
import org.eclipse.nebula.widgets.nattable.ui.matcher.KeyEventMatcher;
import org.eclipse.nebula.widgets.nattable.ui.matcher.MouseEventMatcher;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.tibco.as.spacebar.ui.SpaceBarPlugin;
import com.tibco.as.spacebar.ui.model.Space;
import com.tibco.as.spacebar.ui.transfer.MetaspaceTransferJob;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.util.concurrent.Lock;

import com.tibco.as.io.Exporter;
import com.tibco.as.io.IMetaspaceTransferListener;
import com.tibco.as.io.IOutputStream;
import com.tibco.as.io.ITransfer;
import com.tibco.as.space.FieldDef;
import com.tibco.as.space.Metaspace;
import com.tibco.as.space.SpaceDef;
import com.tibco.as.utils.ASUtils;

public abstract class AbstractSpaceEditor<T extends Map<String, Object>>
		extends EditorPart implements IOutputStream<T> {

	private NatTable natTable;

	private EventList<T> eventList = GlazedLists.threadSafeList(GlazedLists
			.eventList(new LinkedList<T>()));

	private FilterList<T> filterList = new FilterList<T>(eventList);

	private SortedList<T> sortedList = new SortedList<T>(filterList, null);

	private BodyLayerStack bodyLayer;

	private CopyDataAction copyDataAction;

	private List<FieldDef> fieldDefs;

	private List<String> fieldNames;

	private List<String> nonKeyFieldNames;

	private MetaspaceTransferJob browseJob;

	private GlazedListsDataProvider<T> bodyDataProvider;

	private GlazedListsEventLayer<T> glazedListsEventLayer;

	private IUniqueIndexLayer layer;

	private DataLayer bodyDataLayer;

	private Map<String, String> fieldToLabelMap;

	private boolean disposed;

	private ColumnHeaderLayerStack<T> columnHeaderLayer;

	private SpaceDef spaceDef;

	@Override
	public void init(IEditorSite site, final IEditorInput input)
			throws PartInitException {
		if (!(input instanceof SpaceEditorInput))
			throw new PartInitException("Invalid Input: Must be BrowserInput");
		setSite(site);
		internalInit((SpaceEditorInput) input);
	}

	public SpaceEditorInput getBrowserInput() {
		return (SpaceEditorInput) getEditorInput();
	}

	public SelectionLayer getSelectionLayer() {
		return bodyLayer.getSelectionLayer();
	}

	public void activate() {
		getEditorSite().getPage().activate(this);
	}

	private void internalInit(final SpaceEditorInput input)
			throws PartInitException {

		IRunnableWithProgress runnable = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException {
				try {
					doSetInput(input);
				} catch (InterruptedException e) {
					throw new InvocationTargetException(e);
				}
			}
		};

		try {
			getSite().getWorkbenchWindow().run(false, true, runnable);
		} catch (InterruptedException x) {
			// do nothing
		} catch (InvocationTargetException x) {
			Throwable t = x.getTargetException();
			if (t instanceof CoreException) {
				CoreException e = (CoreException) t;
				IStatus status = e.getStatus();
				if (status.getException() != null)
					throw new PartInitException(status);
				throw new PartInitException(new Status(status.getSeverity(),
						status.getPlugin(), status.getCode(),
						status.getMessage(), t));
			}
			throw new PartInitException(new Status(IStatus.ERROR,
					SpaceBarPlugin.ID_PLUGIN, IStatus.OK,
					"Space browser could not be initialized.", t));
		}
	}

	private void stopBrowseJob() {
		if (browseJob != null) {
			browseJob.cancel();
		}
	}

	protected void doSetInput(SpaceEditorInput input)
			throws InterruptedException {
		if (input == null) {
			close(isSaveOnCloseNeeded());
		} else {
			setInput(input);
			Space space = input.getSpace();
			setPartName(space.getName());
			spaceDef = space.getSpaceDef();
			fieldDefs = ASUtils.getFieldDefs(spaceDef);
			fieldNames = ASUtils.getFieldNames(spaceDef);
			nonKeyFieldNames = ASUtils.getNonKeyFieldNames(spaceDef);
			fieldToLabelMap = new HashMap<String, String>();
			for (FieldDef fieldDef : fieldDefs) {
				fieldToLabelMap.put(fieldDef.getName(), fieldDef.getName());
			}
			bodyDataProvider = new GlazedListsDataProvider<T>(sortedList,
					getPropertyAccessor());
			bodyDataLayer = new DataLayer(bodyDataProvider);
			glazedListsEventLayer = new GlazedListsEventLayer<T>(bodyDataLayer,
					sortedList);
			refresh();
		}
	}

	public void refresh() throws InterruptedException {
		if (browseJob != null && browseJob.getState() == Job.RUNNING) {
			browseJob.cancel();
			browseJob.join();
		}
		eventList.clear();
		SpaceEditorInput input = getBrowserInput();
		Space space = input.getSpace();
		Metaspace metaspace = space.getParent().getMetaspace().getConnection()
				.getMetaspace();
		Exporter<T> exporter = getExporter(metaspace);
		exporter.addTransfer(input.getExport());
		exporter.setOutputStream(this);
		exporter.addListener(new IMetaspaceTransferListener() {

			@Override
			public void opening(Collection<ITransfer> transfers) {
			}

			@Override
			public void executing(ITransfer transfer) {
				transfer.addListener(new StatusLineUpdater(transfer,
						getEditorSite()));
			}

		});
		String jobName = NLS.bind("Browsing space ''{0}''", space.getName());
		String task = "Browsing";
		String error = "Could not browse";
		browseJob = new MetaspaceTransferJob(jobName, task, exporter, error);
		browseJob.schedule();
	}

	protected abstract Exporter<T> getExporter(Metaspace metaspace);

	protected List<String> getFieldNames() {
		return fieldNames;
	}

	public EventList<T> getEventList() {
		return eventList;
	}

	public SortedList<T> getSortedList() {
		return sortedList;
	}

	protected Collection<String> getNonKeyFieldNames() {
		return nonKeyFieldNames;
	}

	protected ViewportLayer getViewportLayer() {
		return bodyLayer.getViewportLayer();
	}

	protected abstract AbstractConfiguration getConfiguration();

	private void close(final boolean save) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				if (isDisposed()) {
					return;
				}
				getSite().getPage().closeEditor(AbstractSpaceEditor.this, save);
			}
		});
	}

	@Override
	public void createPartControl(Composite parent) {
		IConfigRegistry registry = new ConfigRegistry();
		layer = getLayer(glazedListsEventLayer, bodyDataProvider, registry);
		ColumnGroupModel columnGroupModel = new ColumnGroupModel();
		bodyLayer = new BodyLayerStack(layer, columnGroupModel);
		ColumnOverrideLabelAccumulator columnLabelAccumulator = new ColumnOverrideLabelAccumulator(
				bodyDataLayer);
		bodyDataLayer.setConfigLabelAccumulator(columnLabelAccumulator);
		AbstractConfiguration configuration = getConfiguration();
		for (int index = 0; index < fieldDefs.size(); index++) {
			columnLabelAccumulator.registerColumnOverrides(index,
					configuration.getLabels(fieldDefs.get(index)));
		}
		columnHeaderLayer = new ColumnHeaderLayerStack<T>(sortedList,
				filterList, fieldNames, fieldToLabelMap, bodyLayer,
				bodyLayer.getSelectionLayer(), columnGroupModel, registry,
				getPropertyAccessor());
		DisplayColumnChooserCommandHandler columnChooserCommandHandler = new DisplayColumnChooserCommandHandler(
				bodyLayer.getSelectionLayer(),
				bodyLayer.getColumnHideShowLayer(),
				columnHeaderLayer.getColumnHeaderLayer(),
				columnHeaderLayer.getColumnHeaderDataLayer(),
				columnHeaderLayer.getColumnGroupHeaderLayer(), columnGroupModel);
		bodyLayer.registerCommandHandler(columnChooserCommandHandler);
		CopyDataCommandHandler copyDataCommandHandler = new CopyDataCommandHandler(
				bodyLayer.getSelectionLayer(),
				columnHeaderLayer.getColumnHeaderDataLayer(), null);
		copyDataCommandHandler.setCopyFormattedText(true);
		bodyLayer.registerCommandHandler(copyDataCommandHandler);
		DefaultRowHeaderDataProvider rowHeaderDataProvider = new DefaultRowHeaderDataProvider(
				bodyDataProvider);
		DefaultRowHeaderDataLayer rowHeaderDataLayer = new DefaultRowHeaderDataLayer(
				rowHeaderDataProvider);
		RowHeaderLayer rowHeaderLayer = new RowHeaderLayer(rowHeaderDataLayer,
				bodyLayer, bodyLayer.getSelectionLayer());
		DefaultCornerDataProvider cornerDataProvider = new DefaultCornerDataProvider(
				columnHeaderLayer.getColumnHeaderDataProvider(),
				rowHeaderDataProvider);
		DataLayer cornerDataLayer = new DataLayer(cornerDataProvider);
		ILayer cornerLayer = new CornerLayer(cornerDataLayer, rowHeaderLayer,
				columnHeaderLayer);
		GridLayer layer = new GridLayer(bodyLayer, columnHeaderLayer,
				rowHeaderLayer, cornerLayer);
		natTable = createNatTable(parent, layer, registry);
		natTable.configure();
		copyDataAction = new CopyDataAction();
	}

	protected abstract IUniqueIndexLayer getLayer(
			GlazedListsEventLayer<T> glazedListsEventLayer,
			GlazedListsDataProvider<T> bodyDataProvider,
			IConfigRegistry registry);

	protected NatTable createNatTable(Composite parent, GridLayer layer,
			IConfigRegistry registry) {
		NatTable natTable = new NatTable(parent, layer, false);
		natTable.setConfigRegistry(registry);
		natTable.addConfiguration(new AbstractUiBindingConfiguration() {

			@Override
			public void configureUiBindings(UiBindingRegistry uiBindingRegistry) {
				// Mouse move - Show resize cursor
				uiBindingRegistry.registerFirstMouseMoveBinding(
						new ColumnResizeEventMatcher(SWT.NONE,
								GridRegion.ROW_HEADER, 0),
						new ColumnResizeCursorAction());
				uiBindingRegistry.registerMouseMoveBinding(
						new MouseEventMatcher(), new ClearCursorAction());

				// Column resize
				uiBindingRegistry.registerFirstMouseDragMode(
						new ColumnResizeEventMatcher(SWT.NONE,
								GridRegion.ROW_HEADER, 1),
						new ColumnResizeDragMode());

				uiBindingRegistry.registerDoubleClickBinding(
						new ColumnResizeEventMatcher(SWT.NONE,
								GridRegion.ROW_HEADER, 1),
						new AutoResizeColumnAction());
				uiBindingRegistry.registerSingleClickBinding(
						new ColumnResizeEventMatcher(SWT.NONE,
								GridRegion.ROW_HEADER, 1),
						new NoOpMouseAction());
			}
		});
		natTable.addConfiguration(getHeaderMenuConfiguration(natTable));
		natTable.addConfiguration(new SingleClickSortConfiguration());
		natTable.addConfiguration(new FilterRowConfiguration(fieldDefs));
		natTable.addConfiguration(getConfiguration());
		natTable.getUiBindingRegistry().registerKeyBinding(
				new KeyEventMatcher(SWT.MOD1, 'c'), copyDataAction);
		new NatTableContentTooltip(natTable, GridRegion.BODY);
		return natTable;
	}

	protected IConfiguration getHeaderMenuConfiguration(NatTable natTable) {
		return new DefaultHeaderMenuConfiguration(natTable);
	}

	protected abstract IColumnPropertyAccessor<T> getPropertyAccessor();

	@Override
	public void dispose() {
		this.disposed = true;
		stopBrowseJob();
		if (natTable != null) {
			natTable.dispose();
			natTable = null;
		}
		if (bodyLayer != null) {
			bodyLayer.dispose();
			bodyLayer = null;
		}
		if (layer != null) {
			layer.dispose();
			layer = null;
		}
		if (bodyDataLayer != null) {
			bodyDataLayer.dispose();
			bodyDataLayer = null;
		}
		if (glazedListsEventLayer != null) {
			glazedListsEventLayer.dispose();
			glazedListsEventLayer = null;
		}
		if (filterList != null) {
			filterList.dispose();
			filterList = null;
		}
		if (sortedList != null) {
			sortedList.dispose();
			sortedList = null;
		}
		if (eventList != null) {
			eventList.dispose();
			eventList = null;
		}
		if (columnHeaderLayer != null) {
			columnHeaderLayer.dispose();
			columnHeaderLayer = null;
		}
		super.dispose();
	}

	public boolean isDisposed() {
		return disposed;
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
		// not supported
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void setFocus() {
		// do nothing
	}

	public Space getSpace() {
		return getBrowserInput().getSpace();
	}

	public void copy(Event event) {
		copyDataAction.run(natTable, new KeyEvent(event));
	}

	protected NatTable getNatTable() {
		return natTable;
	}

	protected PropertyChangeListener getPropertyChangeListener() {
		return glazedListsEventLayer;
	}

	protected GlazedListsEventLayer<T> getGlazedListsEventLayer() {
		return glazedListsEventLayer;
	}

	@Override
	public void open() throws Exception {
	}

	@Override
	public void write(List<T> elements) {
		if (isDisposed()) {
			return;
		}
		Lock lock = eventList.getReadWriteLock().writeLock();
		lock.lock();
		try {
			eventList.addAll(elements);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void write(T element) {
		if (isDisposed()) {
			return;
		}
		Lock lock = eventList.getReadWriteLock().writeLock();
		lock.lock();
		try {
			eventList.add(element);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void close() {
	}

	@Override
	public boolean isClosed() {
		return isDisposed();
	}

}
