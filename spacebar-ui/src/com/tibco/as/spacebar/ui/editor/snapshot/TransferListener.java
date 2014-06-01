package com.tibco.as.spacebar.ui.editor.snapshot;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorSite;

import com.tibco.as.io.IInputStream;
import com.tibco.as.io.ITransfer;
import com.tibco.as.io.ITransferListener;

public class TransferListener implements ITransferListener {

	private ITransfer transfer;
	private SpaceEditor editor;

	public TransferListener(ITransfer transfer, SpaceEditor editor) {
		this.transfer = transfer;
		this.editor = editor;
	}

	@Override
	public void opened() {
	}

	@Override
	public void transferred(int count) {
	}

	@Override
	public void closed() {
		IEditorSite site = editor.getEditorSite();
		if (site == null) {
			return;
		}
		Shell shell = site.getShell();
		if (shell == null || shell.isDisposed()) {
			return;
		}
		Display display = shell.getDisplay();
		if (display == null || display.isDisposed()) {
			return;
		}
		display.asyncExec(new Runnable() {

			@Override
			public void run() {
				IEditorSite site = editor.getEditorSite();
				if (site.getShell() == null || site.getShell().isDisposed()) {
					return;
				}
				IInputStream<?> in = transfer.getInputStream();
				long size = in.getPosition();
				double browseTime = (double) in.getOpenTime() / 1000000d;
				editor.setSize(size);
				editor.setBrowseTime(browseTime);
			}
		});
	}

}
