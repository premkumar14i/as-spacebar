package com.tibco.as.spacebar.ui.model;

import java.util.ArrayList;

public class Metaspace extends Element implements Cloneable {

	public enum State {
		DISCONNECTED, CONNECTED
	}

	private State state = State.DISCONNECTED;

	private Connection connection;

	private String metaspaceName;

	private String memberName;

	private String discovery;

	private String listen;

	private long timeout = 30000;

	private boolean remote;

	private boolean autoconnect;

	private Members members;

	private Spaces spaces;

	public Metaspace(Metaspaces parent, String name) {
		super(parent, name);
	}

	public Members getMembers() {
		return members;
	}

	public void setMembers(Members members) {
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
		Metaspace metaspace = new Metaspace(getParent(), getName());
		copyTo(metaspace);
		return metaspace;
	}

	@Override
	public Metaspaces getParent() {
		return (Metaspaces) super.getParent();
	}

	public void copyTo(Metaspace metaspace) {
		super.copyTo(metaspace);
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
		if (connection == null) {
			setState(State.DISCONNECTED);
		} else {
			synchronized (state) {
				connection.disconnect();
				setState(State.DISCONNECTED);
				connection = null;
			}
		}
		setChildren(new ArrayList<Element>());
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

	public void setConnection(Connection connection) {
		this.connection = connection;
		setState(State.CONNECTED);
	}

}
