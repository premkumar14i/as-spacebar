package com.tibco.as.spacebar.ui.model;

import java.util.Date;

import com.tibco.as.space.Member.ManagementRole;

public abstract class Member extends AbstractElement {

	private String name;
	private String hostAddress;
	private String id;
	private Date joinTime;
	private ManagementRole managementRole;
	private int port;
	private boolean self;

	public boolean isSelf() {
		return self;
	}

	public void setSelf(boolean self) {
		firePropertyChange("self", this.self, this.self = self);
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		fireNameChange(this.name, this.name = name);
	}

	public String getHostAddress() {
		return hostAddress;
	}

	public void setHostAddress(String hostAddress) {
		firePropertyChange("hostAddress", this.hostAddress,
				this.hostAddress = hostAddress);
	}

	@Override
	public void copyTo(IElement element) {
		Member target = (Member) element;
		target.setName(name);
		target.setHostAddress(hostAddress);
		target.setId(id);
		target.setJoinTime(joinTime);
		target.setManagementRole(managementRole);
		target.setPort(port);
		target.setSelf(self);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		firePropertyChange("id", this.id, this.id = id);
	}

	public Date getJoinTime() {
		return joinTime;
	}

	public void setJoinTime(Date joinTime) {
		firePropertyChange("joinTime", this.joinTime, this.joinTime = joinTime);
	}

	public ManagementRole getManagementRole() {
		return managementRole;
	}

	public void setManagementRole(ManagementRole managementRole) {
		firePropertyChange("managementRole", this.managementRole,
				this.managementRole = managementRole);
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		firePropertyChange("port", this.port, this.port = port);
	}

	public void setMember(com.tibco.as.space.Member member) {
		setHostAddress(member.getHostAddress());
		setId(member.getId());
		setJoinTime(member.getJoinTime().getTime().getTime());
		setManagementRole(member.getManagementRole());
		setName(member.getName());
		setPort(member.getPort());
	}

	public boolean isManager() {
		return managementRole == ManagementRole.MANAGER;
	}

}
