package com.tibco.as.spacebar.ui.handlers.metaspace;

import org.eclipse.core.runtime.jobs.ISchedulingRule;

import com.tibco.as.spacebar.ui.model.Metaspace;

public class MetaspaceRule implements ISchedulingRule {

	private Metaspace metaspace;

	/**
	 * @param metaspace
	 *            the metaspace to synchronize against
	 */
	public MetaspaceRule(Metaspace metaspace) {
		if (metaspace == null) {
			throw new IllegalArgumentException("Metaspace is null"); //$NON-NLS-1$
		}
		this.metaspace = metaspace;
	}

	public boolean isConflicting(ISchedulingRule rule) {
		return rule instanceof MetaspaceRule
				&& ((MetaspaceRule) rule).metaspace.equals(metaspace);
	}

	public boolean contains(ISchedulingRule rule) {
		return isConflicting(rule);
	}

}
