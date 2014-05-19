package com.tibco.as.spacebar.ui.wizards.space.browse;

import java.text.NumberFormat;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.ProgressIndicator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.tibco.as.spacebar.ui.SpaceBarPlugin;
import com.tibco.as.spacebar.ui.model.Space;

import com.tibco.as.io.Export;
import com.tibco.as.space.ASException;

public class SizeControl extends Composite {

	private NumberFormat format = NumberFormat.getIntegerInstance();

	private Text text;
	private Button button;
	private ProgressIndicator progressIndicator;

	private Space space;

	private Export export;

	public SizeControl(Composite parent, int style, Space space, Export export) {
		super(parent, style);
		this.space = space;
		this.export = export;
		createControl(parent);
	}

	private void createControl(Composite parent) {
		setLayout(new GridLayout(2, false));
		text = new Text(this, SWT.BORDER | SWT.SINGLE | SWT.READ_ONLY);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		button = new Button(composite, SWT.NONE);
		button.setText("Retrieve");
		progressIndicator = new ProgressIndicator(composite);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				retrieveSize();
			}
		});
	}

	protected void retrieveSize() {
		button.setEnabled(false);
		progressIndicator.beginAnimatedTask();
		Job job = new Job("Space size") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				com.tibco.as.space.Space sp;
				try {
					sp = space.getSpace();
				} catch (ASException e) {
					return SpaceBarPlugin.createStatus(e,
							"Could not get space");
				}
				try {
					final long size = sp.size(export.getFilter());
					if (!isDisposed()) {
						getDisplay().asyncExec(new Runnable() {

							@Override
							public void run() {
								if (!isDisposed()) {
									text.setText(format.format(size));
								}
							}
						});
					}
					if (monitor.isCanceled()) {
						return Status.CANCEL_STATUS;
					}
					return Status.OK_STATUS;
				} catch (ASException e) {
					return SpaceBarPlugin.createStatus(e,
							"Could not retrieve space size");
				} finally {
					try {
						sp.close();
					} catch (ASException e) {
						return SpaceBarPlugin.createStatus(e,
								"Could not close space");
					} finally {
						if (!isDisposed()) {
							getDisplay().asyncExec(new Runnable() {

								@Override
								public void run() {
									if (!isDisposed()) {
										progressIndicator.done();
										button.setEnabled(true);
									}
								}
							});
						}
					}
				}
			}
		};
		job.setUser(true);
		job.setSystem(false);
		job.schedule();
	}

}
