package com.tibco.as.spacebar.ui.editor;

import java.text.MessageFormat;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorSite;

import com.tibco.as.io.IInputStream;
import com.tibco.as.io.ITransfer;
import com.tibco.as.io.ITransferListener;

public class StatusLineUpdater implements ITransferListener {

	private ITransfer transfer;
	private IEditorSite site;

	public StatusLineUpdater(ITransfer transfer, IEditorSite site) {
		this.transfer = transfer;
		this.site = site;
	}

	@Override
	public void closed() {
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
				if (site.getShell() == null || site.getShell().isDisposed()) {
					return;
				}
				IInputStream<?> in = transfer.getInputStream();
				double duration = (double) in.getOpenTime() / 1000000d;
				String message = MessageFormat.format(
						"{0} tuples - {1,number,###,###.000} ms",
						in.getPosition(), duration);
				site.getActionBars().getStatusLineManager().setMessage(message);
			}
		});
	}

	@Override
	public void opened() {
	}

	@Override
	public void transferred(int count) {
	}

}
