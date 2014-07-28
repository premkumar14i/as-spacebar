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

	public static final String PROPERTY_NAME = "name";
	public static final String PROPERTY_METASPACE = "metaspace";
	public static final String PROPERTY_MEMBER = "member";
	public static final String PROPERTY_DISCOVERY = "discovery";
	public static final String PROPERTY_LISTEN = "listen";
	public static final String PROPERTY_REMOTE = "remote";
	public static final String PROPERTY_AUTOCONNECT = "autoconnect";
	public static final String PROPERTY_TIMEOUT = "timeout";

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
	private MetaspaceMembers members;

	@XmlTransient
	private Spaces spaces;

	@XmlTransient
	private Connection connection;

	public Metaspace() {
		members = new MetaspaceMembers();
		members.setMetaspace(this);
		spaces = new Spaces();
		spaces.setMetaspace(this);
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
		Metaspace metaspace = new Metaspace();
		copyTo(metaspace);
		return metaspace;
	}

	public void setMetaspaces(Metaspaces metaspaces) {
		this.metaspaces = metaspaces;
	}

	@Override
	public Metaspaces getParent() {
		return metaspaces;
	}

	@Override
	public void copyTo(IElement element) {
		Metaspace metaspace = (Metaspace) element;
		metaspace.setMetaspaces(metaspaces);
		metaspace.setName(name);
		metaspace.setAutoconnect(autoconnect);
		metaspace.setDiscovery(discovery);
		metaspace.setListen(listen);
		metaspace.setMemberName(memberName);
		MetaspaceMembers members = this.members.clone();
		members.setMetaspace(metaspace);
		metaspace.setMembers(members);
		metaspace.setMetaspaceName(metaspaceName);
		Spaces spaces = this.spaces.clone();
		spaces.setMetaspace(metaspace);
		metaspace.setSpaces(spaces);
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

	public Connection getConnection() {
		return connection;
	}

	public boolean isConnected() {
		return connection != null;
	}

	public String getMetaspaceName() {
		return metaspaceName;
	}

	@Override
	public List<IElement> getChildren() {
		if (isConnected()) {
			return Arrays.asList((IElement) members, spaces);
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

	public void setConnection(Connection connection) {
		List<IElement> oldValue = getChildren();
		this.connection = connection;
		if (connection == null) {
			members.getChildren().clear();
			spaces.getChildren().clear();
		}
		fireChildrenChange(oldValue, getChildren());
	}

}
