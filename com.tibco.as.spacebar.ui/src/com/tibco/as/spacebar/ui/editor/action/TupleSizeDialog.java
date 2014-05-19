package com.tibco.as.spacebar.ui.editor.action;

import java.text.MessageFormat;
import java.util.List;

import org.eclipse.jface.dialogs.ProgressIndicator;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.tibco.as.spacebar.ui.SWTFactory;

import com.tibco.as.space.SpaceDef.PersistencePolicy;
import com.tibco.as.space.Tuple;

public class TupleSizeDialog extends TitleAreaDialog implements
		ITupleSizeListener {

	private final static String MESSAGE = "Tuple size: {0} bytes";

	private List<Tuple> tuples;
	private ProgressIndicator progressIndicator;
	private TupleSizeJob job;

	public TupleSizeDialog(Shell parentShell, List<Tuple> tuples) {
		super(parentShell);
		this.tuples = tuples;
	}

	@Override
	public void create() {
		super.create();
		setTitle("Tuple Size");
		setMessage("Tuple size:");
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = SWTFactory.createComposite(area, 1, 1,
				GridData.FILL_BOTH);
		updateLabel(null);
		progressIndicator = new ProgressIndicator(container);
		GridDataFactory.defaultsFor(progressIndicator).grab(true, false)
				.applyTo(progressIndicator);
		progressIndicator.beginAnimatedTask();
		job = new TupleSizeJob(tuples);
		job.addListener(this);
		job.schedule();
		return area;
	}

	@Override
	public void tupleSize(Long size) {
		updateLabel(size);
	}

	private void updateLabel(final Long size) {
		if (size == null) {
			return;
		}
		if (!getShell().isDisposed()) {
			getShell().getDisplay().asyncExec(new Runnable() {

				@Override
				public void run() {
					if (!getShell().isDisposed()) {
						setMessage(MessageFormat.format(MESSAGE, size));
					}
				}
			});
		}
	}

	@Override
	public boolean close() {
		if (job != null) {
			job.cancel();
		}
		return super.close();
	}

	@Override
	public void done() {
		if (!getShell().isDisposed()) {
			getShell().getDisplay().asyncExec(new Runnable() {

				@Override
				public void run() {
					if (!getShell().isDisposed()) {
						progressIndicator.done();
					}
				}
			});
		}
	}

	public static void main(String[] args) {
		System.out.println(PersistencePolicy.class.getName());
	}
}
