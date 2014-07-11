package com.tibco.as.spacebar.ui.handlers.space;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import com.tibco.as.spacebar.ui.model.Space;

public abstract class AbstractSpaceHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection currentSelection = HandlerUtil.getCurrentSelection(event);
		if (currentSelection instanceof IStructuredSelection) {
			Object selected = ((IStructuredSelection) currentSelection)
					.getFirstElement();
			if (selected instanceof Space) {
				handle(event, (Space) selected);
			}
		}
		return null;
	}

	protected abstract void handle(ExecutionEvent event, Space space)
			throws ExecutionException;

}
