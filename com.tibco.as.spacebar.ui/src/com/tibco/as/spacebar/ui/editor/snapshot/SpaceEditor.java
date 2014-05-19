package com.tibco.as.spacebar.ui.editor.snapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.edit.command.EditCellCommand;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.GlazedListsDataProvider;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.GlazedListsEventLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.GridLayer;
import org.eclipse.nebula.widgets.nattable.layer.IUniqueIndexLayer;
import org.eclipse.nebula.widgets.nattable.layer.cell.ILayerCell;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;

import com.tibco.as.spacebar.ui.SpaceBarPlugin;
import com.tibco.as.spacebar.ui.editor.AbstractConfiguration;
import com.tibco.as.spacebar.ui.editor.AbstractSpaceEditor;
import com.tibco.as.spacebar.ui.editor.snapshot.Change.Type;
import com.tibco.as.spacebar.ui.model.Space;
import ca.odell.glazedlists.SortedList;

import com.tibco.as.convert.ConvertException;
import com.tibco.as.convert.IConverter;
import com.tibco.as.convert.UnsupportedConversionException;
import com.tibco.as.io.Exporter;
import com.tibco.as.io.IOutputStream;
import com.tibco.as.space.Metaspace;
import com.tibco.as.space.SpaceDef;
import com.tibco.as.space.Tuple;

public class SpaceEditor extends AbstractSpaceEditor<Tuple> {

	public static final String EDITOR_ID = "com.tibco.as.spacebar.ui.editor.snapshot";

	private boolean dirty;

	private Stack<Change> changeList;

	private DropAdapter dropAdapter;

	@Override
	public void dispose() {
		changeList = null;
		dropAdapter = null;
		super.dispose();
	}

	@Override
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		changeList = new Stack<Change>();
	}

	@Override
	protected IUniqueIndexLayer getLayer(
			GlazedListsEventLayer<Tuple> glazedListsEventLayer,
			GlazedListsDataProvider<Tuple> bodyDataProvider,
			IConfigRegistry registry) {
		return glazedListsEventLayer;
	}

	@Override
	protected NatTable createNatTable(Composite parent, GridLayer layer,
			IConfigRegistry registry) {
		NatTable natTable = super.createNatTable(parent, layer, registry);
		FileTransfer fileTransfer = FileTransfer.getInstance();
		Transfer[] transferTypes = new Transfer[] { fileTransfer };
		dropAdapter = new DropAdapter(fileTransfer, this);
		natTable.addDropSupport(DND.DROP_COPY | DND.DROP_LINK | DND.DROP_MOVE,
				transferTypes, dropAdapter);
		return natTable;
	}

	public IOutputStream<Tuple> getChangeOutputStream() {
		return new IOutputStream<Tuple>() {

			private List<Tuple> eventList = getEventList();
			private boolean closed;

			@Override
			public void open() throws Exception {
				this.closed = false;
			}

			@Override
			public void close() {
				this.closed = true;
			}

			@Override
			public void write(List<Tuple> elements) {
				eventList.addAll(elements);
				for (Tuple element : elements) {
					add(new Change(Type.PUT, element));
				}
			}

			@Override
			public void write(Tuple element) {
				eventList.add(element);
				add(new Change(Type.PUT, element));
			}

			@Override
			public boolean isClosed() {
				return closed;
			}

		};
	}

	private void add(Change change) {
		changeList.push(change);
		setDirty(true);
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		Space space = getSpace();
		String jobName = NLS.bind("Save space ''{0}''", space.getName());
		SaveJob saveJob = new SaveJob(jobName, this);
		saveJob.schedule();
	}

	public Stack<Change> getChangeList() {
		return changeList;
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean value) {
		if (dirty != value) {
			dirty = value;
			firePropertyChange(PROP_DIRTY);
		}
	}

	public void addChanged(Tuple rowObject) {
		for (Change change : changeList) {
			if (change.getType() == Type.PUT) {
				if (change.getTuple() == rowObject) {
					return;
				}
			}
		}
		add(new Change(Type.PUT, rowObject));
	}

	@Override
	protected AbstractConfiguration getConfiguration() {
		return new EditConfiguration();
	}

	public void insert() {
		Tuple tuple = Tuple.create();
		SortedList<Tuple> list = getSortedList();
		list.add(tuple);
		NatTable natTable = getNatTable();
		getViewportLayer().moveRowPositionIntoViewport(list.size() - 1);
		ILayerCell cell = natTable.getCellByPosition(1,
				natTable.getRowCount() - 1);
		IConfigRegistry registry = natTable.getConfigRegistry();
		natTable.doCommand(new EditCellCommand(natTable, registry, cell));
		add(new Change(Type.PUT, tuple));
	}

	@Override
	protected IColumnPropertyAccessor<Tuple> getPropertyAccessor() {
		return new EditablePropertyAccessor(getFieldNames(), this);
	}

	public void deleteSelected() {
		List<Tuple> selection = getSelection();
		boolean removed = getEventList().removeAll(selection);
		if (removed) {
			for (Tuple tuple : selection) {
				add(new Change(Type.TAKE, tuple));
			}
		} else {
			SpaceBarPlugin.errorDialog("Delete Error",
					"Could not delete selected tuples");
		}
	}

	public List<Tuple> getSelection() {
		int[] rows = getSelectionLayer().getFullySelectedRowPositions();
		SortedList<Tuple> list = getSortedList();
		List<Tuple> selection = new ArrayList<Tuple>();
		for (int row : rows) {
			Tuple tuple = list.get(row);
			selection.add(tuple);
		}
		return selection;
	}

	@Override
	protected Exporter<Tuple> getExporter(Metaspace metaspace) {
		return new Exporter<Tuple>(metaspace) {

			@Override
			protected IConverter<Tuple, Tuple> getConverter(
					com.tibco.as.io.Transfer transfer, SpaceDef spaceDef)
					throws UnsupportedConversionException {
				return new IConverter<Tuple, Tuple>() {

					@Override
					public Tuple convert(Tuple source) throws ConvertException {
						return source;
					}

				};
			}

		};
	}
}
