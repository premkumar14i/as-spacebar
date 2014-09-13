package com.tibco.as.spacebar.ui.adapter;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.tibco.as.spacebar.ui.editor.SpaceEditorInput;

public class BrowserInputPropertySource implements IPropertySource {

	private static final String PROPERTY_SPACE = "space";
	private static final String PROPERTY_BROWSER_TYPE = "browserType";
	private static final String PROPERTY_TIME_SCOPE = "timeScope";
	private static final String PROPERTY_DISTRIBUTION_SCOPE = "distributionScope";
	private static final String PROPERTY_TIMEOUT = "timeout";
	private static final String PROPERTY_PREFETCH = "prefetch";
	private static final String PROPERTY_QUERY_LIMIT = "queryLimit";
	private static final String PROPERTY_FILTER = "filter";
	private static final String PROPERTY_LIMIT = "limit";

	private SpaceEditorInput input;

	public BrowserInputPropertySource(SpaceEditorInput input) {
		this.input = input;
	}

	@Override
	public Object getEditableValue() {
		return null;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return new IPropertyDescriptor[] {
				new PropertyDescriptor(PROPERTY_SPACE, "Space"),
				new PropertyDescriptor(PROPERTY_BROWSER_TYPE, "Browser type"),
				new PropertyDescriptor(PROPERTY_TIME_SCOPE, "Time scope"),
				new PropertyDescriptor(PROPERTY_DISTRIBUTION_SCOPE,
						"Distribution scope"),
				new PropertyDescriptor(PROPERTY_TIMEOUT, "Timeout"),
				new PropertyDescriptor(PROPERTY_PREFETCH, "Prefetch"),
				new PropertyDescriptor(PROPERTY_QUERY_LIMIT, "Query limit"),
				new PropertyDescriptor(PROPERTY_FILTER, "Filter"),
				new PropertyDescriptor(PROPERTY_LIMIT, "Limit") };
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (PROPERTY_SPACE.equals(id)) {
			return input.getSpace().getName();
		}
		if (PROPERTY_BROWSER_TYPE.equals(id)) {
			return input.getExport().getBrowserType();
		}
		if (PROPERTY_DISTRIBUTION_SCOPE.equals(id)) {
			return input.getExport().getDistributionScope();
		}
		if (PROPERTY_FILTER.equals(id)) {
			return input.getExport().getFilter();
		}
		if (PROPERTY_LIMIT.equals(id)) {
			return input.getExport().getLimit();
		}
		if (PROPERTY_PREFETCH.equals(id)) {
			return input.getExport().getPrefetch();
		}
		if (PROPERTY_QUERY_LIMIT.equals(id)) {
			return input.getExport().getQueryLimit();
		}
		if (PROPERTY_TIME_SCOPE.equals(id)) {
			return input.getExport().getTimeScope();
		}
		if (PROPERTY_TIMEOUT.equals(id)) {
			return input.getExport().getTimeout();
		}
		return null;
	}

	@Override
	public boolean isPropertySet(Object id) {
		return false;
	}

	@Override
	public void resetPropertyValue(Object id) {
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
	}

}
