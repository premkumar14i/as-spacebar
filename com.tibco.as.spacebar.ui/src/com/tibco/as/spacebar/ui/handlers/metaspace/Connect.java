package com.tibco.as.spacebar.ui.handlers.metaspace;

import org.eclipse.core.commands.ExecutionEvent;

import com.tibco.as.spacebar.ui.ConnectJob;
import com.tibco.as.spacebar.ui.model.Metaspace;

public class Connect extends AbstractMetaspaceHandler {

	@Override
	protected void handle(ExecutionEvent event, Metaspace metaspace) {
		new ConnectJob(metaspace).schedule();
	}

}
