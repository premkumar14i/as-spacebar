package com.tibco.as.spacebar.ui.wizards.space;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.ListChangeEvent;
import org.eclipse.core.databinding.observable.list.ListDiffEntry;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.jface.databinding.viewers.ViewerSupport;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;

public class DualList<T> extends Composite implements IListChangeListener {

	private TableViewer leftViewer;
	private TableViewer rightViewer;
	private IObservableList leftList;
	private IObservableList rightList;

	public DualList(Composite parent, int style, Class<T> type,
			String propertyName, List<T> items) {
		super(parent, style);
		setLayout(new GridLayout(4, false));
		leftViewer = createTableViewer();
		Composite leftButtonPane = new Composite(this, SWT.NONE);
		leftButtonPane.setLayout(new GridLayout());
		GridDataFactory.fillDefaults().grab(false, true)
				.align(GridData.CENTER, GridData.CENTER)
				.applyTo(leftButtonPane);
		final Button addButton = createButton(leftButtonPane, "Add ->",
				new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						selectItem();
					}
				});
		final Button addAllButton = createButton(leftButtonPane, "Add All ->",
				new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						selectAll();
					}
				});
		final Button removeButton = createButton(leftButtonPane, "<- Remove",
				new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						deselectItem();
					}
				});
		final Button removeAllButton = createButton(leftButtonPane,
				"<- Remove All", new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						deselectAll();
					}
				});
		leftViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				addButton.setEnabled(!event.getSelection().isEmpty());
			}
		});
		rightViewer = createTableViewer();
		rightViewer.getTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent event) {
				deselectItem();
			}
		});
		Composite rightButtonPane = new Composite(this, SWT.NONE);
		rightButtonPane.setLayout(new GridLayout());
		GridDataFactory.fillDefaults().grab(false, true)
				.align(GridData.CENTER, GridData.CENTER)
				.applyTo(rightButtonPane);
		final Button upButton = createButton(rightButtonPane, "Up",
				new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						moveItem(-1);
					}
				});
		final Button downButton = createButton(rightButtonPane, "Down",
				new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						moveItem(1);
					}
				});
		final Button topButton = createButton(rightButtonPane, "Top",
				new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						moveSelectionToFirstPosition();
					}
				});
		final Button bottomButton = createButton(rightButtonPane, "Bottom",
				new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						moveSelectionToLastPosition();
					}
				});
		rightViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {

					@Override
					public void selectionChanged(SelectionChangedEvent event) {
						boolean hasSelection = !event.getSelection().isEmpty();
						upButton.setEnabled(hasSelection);
						downButton.setEnabled(hasSelection);
						topButton.setEnabled(hasSelection);
						bottomButton.setEnabled(hasSelection);
						removeButton.setEnabled(hasSelection);
					}
				});
		// data binding
		leftList = WritableList.withElementType(type);
		leftList.addListChangeListener(new IListChangeListener() {

			@Override
			public void handleListChange(ListChangeEvent event) {
				addAllButton.setEnabled(!leftList.isEmpty());
			}
		});
		leftList.addAll(items);
		ViewerSupport.bind(leftViewer, leftList,
				BeanProperties.value(type, propertyName));

		rightList = WritableList.withElementType(type);
		rightList.addListChangeListener(new IListChangeListener() {

			@Override
			public void handleListChange(ListChangeEvent event) {
				removeAllButton.setEnabled(!rightList.isEmpty());
			}
		});
		ViewerSupport.bind(rightViewer, rightList,
				BeanProperties.value(type, propertyName));
	}

	private TableViewer createTableViewer() {
		TableViewer tableViewer = new TableViewer(this);
		GridDataFactory.fillDefaults().grab(true, true).span(1, 4)
				.applyTo(tableViewer.getTable());
		new TableColumn(tableViewer.getTable(), SWT.NONE);
		return tableViewer;
	}

	public void setSelection(List<T> selection) {
		for (T element : selection) {
			addSelection(element);
		}
	}

	public void addSelection(T element) {
		rightList.add(element);
		leftList.remove(element);
	}

	public void addSelection(Collection<T> elements) {
		rightList.addAll(elements);
		leftList.removeAll(elements);
	}

	/**
	 * Create a button
	 * 
	 * @param fileName
	 *            file name of the icon
	 * @param verticalExpand
	 *            if <code>true</code>, the button will take all the available
	 *            space vertically
	 * @param alignment
	 *            button alignment
	 * @return a new button
	 */
	private Button createButton(Composite parent, String text,
			SelectionListener listener) {
		Button button = new Button(parent, SWT.PUSH);
		button.setText(text);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(button);
		button.addSelectionListener(listener);
		button.setEnabled(false);
		return button;
	}

	/**
	 * Deselects all selected items in the receiver.
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void deselectAll() {
		leftList.addAll(rightList);
		rightList.clear();
	}

	/**
	 * Returns the item at the given, zero-relative index in the receiver.
	 * Throws an exception if the index is out of range.
	 * 
	 * @param index
	 *            the index of the item to return
	 * @return the item at the given index
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_RANGE - if the index is not between 0
	 *                and the number of elements in the list minus 1 (inclusive)
	 *                </li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */

	public Object getItem(int index) {
		return leftList.get(index);
	}

	/**
	 * Returns the number of items contained in the receiver.
	 * 
	 * @return the number of items
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getItemCount() {
		return leftList.size();
	}

	/**
	 * Removes the item from the receiver at the given zero-relative index.
	 * 
	 * @param index
	 *            the index for the item
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_RANGE - if the index is not between 0
	 *                and the number of elements in the list minus 1 (inclusive)
	 *                </li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void removeItem(int index) {
		leftList.remove(index);
	}

	/**
	 * Removes the items from the receiver at the given zero-relative indices.
	 * 
	 * @param indices
	 *            the array of indices of the items
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_RANGE - if the index is not between 0
	 *                and the number of elements in the list minus 1 (inclusive)
	 *                </li>
	 *                <li>ERROR_NULL_ARGUMENT - if the indices array is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void removeItems(int[] indices) {
		for (int index : indices) {
			leftList.remove(index);
		}
	}

	/**
	 * Removes the items from the receiver which are between the given
	 * zero-relative start and end indices (inclusive).
	 * 
	 * @param start
	 *            the start of the range
	 * @param end
	 *            the end of the range
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_RANGE - if either the start or end are
	 *                not between 0 and the number of elements in the list minus
	 *                1 (inclusive) or if start>end</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void removeItems(int start, int end) {
		for (int index = start; index < end; index++) {
			leftList.remove(index);
		}
	}

	/**
	 * Searches the receiver's list starting at the first item until an item is
	 * found that is equal to the argument, and removes that item from the list.
	 * 
	 * @param item
	 *            the item to remove
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the item is null</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the item is not found in
	 *                the list</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void removeItem(Object item) {
		leftList.remove(item);
	}

	/**
	 * Removes all of the items from the receiver.
	 * <p>
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li> <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                called from the thread that created the receiver</li>
	 *                </ul>
	 */
	public void removeAllItems() {
		leftList.clear();
	}

	/**
	 * Selects the item at the given zero-relative index in the receiver's list.
	 * If the item at the index was already selected, it remains selected.
	 * Indices that are out of range are ignored.
	 * 
	 * @param index
	 *            the index of the item to select
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void select(int index) {
		if (index < 0 || index >= leftList.size()) {
			return;
		}
		rightList.add(leftList.get(index));
	}

	/**
	 * Selects the items at the given zero-relative indices in the receiver. The
	 * current selection is not cleared before the new items are selected.
	 * <p>
	 * If the item at a given index is not selected, it is selected. If the item
	 * at a given index was already selected, it remains selected. Indices that
	 * are out of range and duplicate indices are ignored. If the receiver is
	 * single-select and multiple indices are specified, then all indices are
	 * ignored.
	 * 
	 * @param indices
	 *            the array of indices for the items to select
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the array of indices is null
	 *                </li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li> <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                called from the thread that created the receiver</li>
	 *                </ul>
	 */
	public void select(int[] indices) {
		for (int index : indices) {
			if (index < 0 || index >= leftList.size()) {
				continue;
			}
			rightList.add(leftList.get(index));
		}
	}

	/**
	 * Selects the items in the range specified by the given zero-relative
	 * indices in the receiver. The range of indices is inclusive. The current
	 * selection is not cleared before the new items are selected.
	 * <p>
	 * If an item in the given range is not selected, it is selected. If an item
	 * in the given range was already selected, it remains selected. Indices
	 * that are out of range are ignored and no items will be selected if start
	 * is greater than end. If the receiver is single-select and there is more
	 * than one item in the given range, then all indices are ignored.
	 * 
	 * @param start
	 *            the start of the range
	 * @param end
	 *            the end of the range
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li> <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                called from the thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see List#setSelection(int,int)
	 */
	public void select(int start, int end) {
		for (int index = start; index <= end; index++) {
			if (index < 0 || index >= leftList.size()) {
				continue;
			}
			rightList.add(leftList.get(index));
		}
	}

	/**
	 * Selects all of the items in the receiver.
	 * <p>
	 * If the receiver is single-select, do nothing.
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li> <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                called from the thread that created the receiver</li>
	 *                </ul>
	 */
	public void selectAll() {
		rightList.addAll(leftList);
		leftList.clear();
	}

	/**
	 * @see org.eclipse.swt.widgets.Control#setBounds(int, int, int, int)
	 */
	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		Point itemsTableDefaultSize = leftViewer.getTable().computeSize(
				SWT.DEFAULT, SWT.DEFAULT);
		Point selectionTableDefaultSize = rightViewer.getTable().computeSize(
				SWT.DEFAULT, SWT.DEFAULT);

		int itemsTableSize = leftViewer.getTable().getSize().x;
		if (itemsTableDefaultSize.y > leftViewer.getTable().getSize().y) {
			itemsTableSize -= leftViewer.getTable().getVerticalBar().getSize().x;
		}

		int selectionTableSize = rightViewer.getTable().getSize().x;
		if (selectionTableDefaultSize.y > rightViewer.getTable().getSize().y) {
			selectionTableSize -= rightViewer.getTable().getVerticalBar()
					.getSize().x;
		}

		leftViewer.getTable().getColumn(0).setWidth(itemsTableSize);
		rightViewer.getTable().getColumn(0).setWidth(selectionTableSize);
	}

	/**
	 * Sets the item in the receiver's list at the given zero-relative index to
	 * the item argument.
	 * 
	 * @param index
	 *            the index for the item
	 * @param item
	 *            the new item
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_RANGE - if the index is not between 0
	 *                and the number of elements in the list minus 1 (inclusive)
	 *                </li>
	 *                <li>ERROR_NULL_ARGUMENT - if the item is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setItem(int index, T item) {
		leftList.set(index, item);
	}

	/**
	 * Sets the receiver's items to be the given array of items.
	 * 
	 * @param items
	 *            the array of items
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the items array is null</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if an item in the items array
	 *                is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setItems(T[] items) {
		List<T> temp = new ArrayList<T>();
		for (T item : items) {
			temp.add(item);
		}
		leftList.clear();
		leftList.addAll(temp);
	}

	/**
	 * Move the selected item to the first position
	 */
	@SuppressWarnings("unchecked")
	protected void moveSelectionToFirstPosition() {
		IStructuredSelection selection = (IStructuredSelection) rightViewer
				.getSelection();
		Object[] elements = selection.toArray();
		for (int index = 0; index < elements.length; index++) {
			rightList.remove(elements[index]);
			rightList.add(index, elements[index]);
		}
		rightViewer.setSelection(new StructuredSelection(elements));
		rightViewer.getTable().forceFocus();
	}

	/**
	 * Select a given item
	 */
	protected void selectItem() {
		IStructuredSelection selection = (IStructuredSelection) leftViewer
				.getSelection();
		for (Object element : selection.toArray()) {
			rightList.add(element);
			leftList.remove(element);
		}
	}

	/**
	 * Move the selected item up
	 */
	private void moveItem(int shift) {
		IStructuredSelection selection = (IStructuredSelection) rightViewer
				.getSelection();
		List<Object> selected = new ArrayList<Object>();
		for (Iterator<?> iterator = selection.iterator(); iterator.hasNext();) {
			Object element = iterator.next();
			int index = rightList.indexOf(element);
			int second = index + shift;
			if (second >= rightList.size() || second < 0) {
				return;
			}
			selected.add(element);
		}
		if (shift > 0) {
			Collections.reverse(selected);
		}
		for (Object element1 : selected) {
			int index1 = rightList.indexOf(element1);
			int index2 = index1 + shift;
			Object element2 = rightList.get(index2);
			rightList.set(index1, element2);
			rightList.set(index2, element1);
		}
		StructuredSelection newSelection = new StructuredSelection(selected);
		rightViewer.setSelection(newSelection);
	}

	/**
	 * Deselect a given item
	 */
	protected void deselectItem() {
		IStructuredSelection selection = (IStructuredSelection) rightViewer
				.getSelection();
		for (Object element : selection.toArray()) {
			leftList.add(element);
			rightList.remove(element);
		}
	}

	/**
	 * Move the selected item to the last position
	 */
	protected void moveSelectionToLastPosition() {
		IStructuredSelection selection = (IStructuredSelection) rightViewer
				.getSelection();
		Object[] elements = selection.toArray();
		for (Object element : elements) {
			rightList.remove(element);
			rightList.add(element);
		}
		rightViewer.setSelection(new StructuredSelection(elements));
		rightViewer.getTable().forceFocus();
	}

	// public static void main(String[] args) {
	// Space space = new Space(new Spaces(new Metaspace(new Metaspaces())));
	// space.setSpaceDef(SpaceDef.create("space1"));
	// final List<Field> fields = new ArrayList<Field>();
	// final List<Field> selected = new ArrayList<Field>();
	// for (int index = 0; index < 100; index++) {
	// Field field = new Field(space.getFields());
	// field.setName("Field" + (index + 1));
	// field.setType(FieldType.values()[index % FieldType.values().length]);
	// field.setNullable(true);
	// fields.add(field);
	// if (index < 10) {
	// selected.add(field);
	// }
	// }
	// Display display = new Display();
	// Realm.runWithDefault(SWTObservables.getRealm(display), new Runnable() {
	// public void run() {
	//
	// // Build a UI
	// Display display = Display.getDefault();
	// Shell shell = new Shell(display);
	// shell.setLayout(new FillLayout());
	// DualList<Field> dualList = new DualList<Field>(shell, SWT.NONE,
	// Field.class, "name", fields);
	// dualList.setSelection(selected);
	// // dualList.setItems(new WritableList(fields, Field.class));
	// // Open and return the Shell
	// // shell.setSize(100, 300);
	// shell.open();
	// // The SWT event loop
	// while (!shell.isDisposed()) {
	// if (!display.readAndDispatch()) {
	// display.sleep();
	// }
	// }
	// }
	// });
	// }

	public IObservableList getSelection() {
		return rightList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleListChange(ListChangeEvent event) {
		for (ListDiffEntry entry : event.diff.getDifferences()) {
			if (entry.isAddition()) {
				if (entry.getPosition() >= leftList.size()) {
					leftList.add(entry.getElement());
				} else {
					leftList.add(entry.getPosition(), entry.getElement());
				}
			} else {
				leftList.remove(entry.getElement());
				rightList.remove(entry.getElement());
			}
		}
	}

}
