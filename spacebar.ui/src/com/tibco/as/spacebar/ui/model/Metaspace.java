package com.tibco.as.spacebar.ui.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "metaspace")
public class Metaspace extends AbstractElement {

	public enum State {
		DISCONNECTED, CONNECTED
	}

	@XmlTransient
	private Metaspaces metaspaces;

	@XmlAttribute(name = "displayName")
	private String name;

	@XmlAttribute(name = "name")
	private String metaspaceName;

	@XmlAttribute(name = "member")
	private String memberName;

	@XmlAttribute(name = "discovery")
	private String discovery;

	@XmlAttribute(name = "listen")
	private String listen;

	@XmlAttribute(name = "connectTimeout")
	private long timeout = 30000;

	@XmlAttribute(name = "remote")
	private boolean remote;

	@XmlAttribute(name = "autoConnect")
	private boolean autoconnect;

	@XmlTransient
	private MetaspaceMembers members = new MetaspaceMembers(this);

	@XmlTransient
	private Spaces spaces = new Spaces(this);

	@XmlTransient
	private State state = State.DISCONNECTED;

	@XmlTransient
	private Connection connection;

	public Metaspace() {
	}

	public Metaspace(Metaspaces metaspaces) {
		this.metaspaces = metaspaces;
	}

	public MetaspaceMembers getMembers() {
		return members;
	}

	public void setMembers(MetaspaceMembers members) {
		this.members = members;
	}

	public Spaces getSpaces() {
		return spaces;
	}

	public void setSpaces(Spaces spaces) {
		this.spaces = spaces;
	}

	@Override
	public Metaspace clone() {
		Metaspace metaspace = new Metaspace(getParent());
		copyTo(metaspace);
		return metaspace;
	}

	public Metaspaces getMetaspaces() {
		return metaspaces;
	}

	public void setMetaspaces(Metaspaces metaspaces) {
		this.metaspaces = metaspaces;
	}

	@Override
	public Metaspaces getParent() {
		return metaspaces;
	}

	public void copyTo(Metaspace metaspace) {
		metaspace.setAutoconnect(autoconnect);
		metaspace.setDiscovery(discovery);
		metaspace.setListen(listen);
		metaspace.setMemberName(memberName);
		metaspace.setMetaspaceName(metaspaceName);
		metaspace.setRemote(remote);
		metaspace.setTimeout(timeout);
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		firePropertyChange("timeout", this.timeout, this.timeout = timeout);
	}

	public void setMetaspaceName(String metaspaceName) {
		firePropertyChange("metaspaceName", this.metaspaceName,
				this.metaspaceName = metaspaceName);
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		firePropertyChange("memberName", this.memberName,
				this.memberName = memberName);
	}

	public boolean isAutoconnect() {
		return autoconnect;
	}

	public void setAutoconnect(boolean autoconnect) {
		firePropertyChange("autoconnect", this.autoconnect,
				this.autoconnect = autoconnect);
	}

	public String getListen() {
		return listen;
	}

	public void setListen(String listen) {
		firePropertyChange("listen", this.listen, this.listen = listen);
	}

	public String getDiscovery() {
		return discovery;
	}

	public void setDiscovery(String discovery) {
		firePropertyChange("discovery", this.discovery,
				this.discovery = discovery);
	}

	public boolean isRemote() {
		return remote;
	}

	public void setRemote(boolean remote) {
		firePropertyChange("remote", this.remote, this.remote = remote);
	}

	public State getState() {
		return state;
	}

	public void disconnect() throws Exception {
		List<? extends IElement> oldValue = getChildren();
		if (connection == null) {
			setState(State.DISCONNECTED);
		} else {
			synchronized (state) {
				connection.disconnect();
				connection = null;
				setState(State.DISCONNECTED);
			}
		}
		spaces = new Spaces(this);
		members = new MetaspaceMembers(this);
		fireChildrenChange(oldValue, Collections.emptyList());
	}

	public void setState(State state) {
		firePropertyChange("state", this.state, this.state = state);
	}

	public Connection getConnection() {
		return connection;
	}

	public boolean isConnected() {
		return state == State.CONNECTED;
	}

	public boolean isDisconnected() {
		return state == State.DISCONNECTED;
	}

	public String getMetaspaceName() {
		return metaspaceName;
	}

	@Override
	public List<? extends IElement> getChildren() {
		if (isConnected()) {
			return Arrays.asList(members, spaces);
		}
		return Collections.emptyList();
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		fireNameChange(this.name, this.name = name);
	}

	public void connect() throws Exception {
		if (connection == null) {
			synchronized (state) {
				Connection connection = new Connection(this);
				connection.connect();
				this.connection = connection;
				setState(State.CONNECTED);
				fireChildrenChange(Collections.emptyList(), getChildren());
			}
		} else {
			setState(State.CONNECTED);
		}
	}

}
