package com.tibco.as.spacebar.ui.handlers.metaspace;

import org.eclipse.core.commands.ExecutionEvent;

import com.tibco.as.spacebar.ui.ReconnectJob;
import com.tibco.as.spacebar.ui.model.Metaspace;

public class Reconnect extends AbstractMetaspaceHandler {

	@Override
	protected void handle(ExecutionEvent event, Metaspace metaspace) {
		new ReconnectJob(metaspace).schedule();
	}

}
