package com.tibco.as.spacebar.ui.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "metaspaces" })
@XmlRootElement(name = "metaspaces")
public class Metaspaces extends AbstractElement {

	@XmlElement(required = true)
	private List<Metaspace> metaspaces = new ArrayList<Metaspace>();

	@Override
	public String getName() {
		return "Metaspaces";
	}

	public List<Metaspace> getMetaspaces() {
		return metaspaces;
	}

	@Override
	public IElement getParent() {
		return null;
	}

	@Override
	protected boolean remove(IElement child) {
		return metaspaces.remove(child);
	}

	@Override
	public List<Metaspace> getChildren() {
		return metaspaces;
	}

	@Override
	protected void copyChild(IElement source, IElement target) {
		((Metaspace) source).copyTo((Metaspace) target);
	}

	@Override
	protected boolean add(IElement child) {
		return metaspaces.add((Metaspace) child);
	}

	public List<Metaspace> getConnectedMetaspaces() {
		List<Metaspace> connectedMetaspaces = new ArrayList<Metaspace>();
		for (Metaspace metaspace : metaspaces) {
			if (metaspace.isConnected()) {
				connectedMetaspaces.add(metaspace);
			}
		}
		return connectedMetaspaces;
	}

}
