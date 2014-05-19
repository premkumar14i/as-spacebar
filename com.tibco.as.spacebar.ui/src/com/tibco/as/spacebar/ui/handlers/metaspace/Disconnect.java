package com.tibco.as.spacebar.ui.handlers.metaspace;

import org.eclipse.core.commands.ExecutionEvent;

import com.tibco.as.spacebar.ui.DisconnectJob;
import com.tibco.as.spacebar.ui.model.Metaspace;

public class Disconnect extends AbstractMetaspaceHandler {

	@Override
	protected void handle(ExecutionEvent event, Metaspace metaspace) {
		new DisconnectJob(metaspace).schedule();
	}

}
