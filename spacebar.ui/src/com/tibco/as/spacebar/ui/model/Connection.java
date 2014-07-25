package com.tibco.as.spacebar.ui.model;

import com.tibco.as.space.ASCommon;
import com.tibco.as.space.ASException;
import com.tibco.as.space.Member.DistributionRole;
import com.tibco.as.space.Member.ManagementRole;
import com.tibco.as.space.MemberDef;
import com.tibco.as.space.Metaspace;
import com.tibco.as.space.SpaceDef;
import com.tibco.as.space.listener.MetaspaceMemberListener;
import com.tibco.as.space.listener.SpaceDefListener;
import com.tibco.as.space.listener.SpaceMemberListener;
import com.tibco.as.spacebar.ui.SpaceBarPlugin;
import com.tibco.as.util.Utils;

public class Connection {

	private Metaspace ms;

	private com.tibco.as.spacebar.ui.model.Metaspace metaspace;

	public Connection(com.tibco.as.spacebar.ui.model.Metaspace metaspace) {
		this.metaspace = metaspace;
	}

	public void connect() throws Exception {
		ms = Utils.getMetaspace(metaspace.getMetaspaceName());
		if (ms == null) {
			MemberDef memberDef = MemberDef.create();
			if (metaspace.getMemberName() != null) {
				memberDef.setMemberName(metaspace.getMemberName());
			}
			if (metaspace.getDiscovery() != null) {
				if (metaspace.isRemote()) {
					memberDef.setRemoteDiscovery(metaspace.getDiscovery());
				} else {
					memberDef.setDiscovery(metaspace.getDiscovery());
				}
			}
			if (metaspace.getListen() != null) {
				memberDef.setListen(metaspace.getListen());
			}
			if (Utils.hasMethod(MemberDef.class, "setConnectTimeout")) {
				memberDef.setConnectTimeout(metaspace.getTimeout());
			}
			ms = Metaspace.connect(metaspace.getMetaspaceName(), memberDef);
		}
		ms.listenSpaceMembers(new SpaceMemberListener() {

			@Override
			public void onUpdate(String spaceName,
					com.tibco.as.space.Member member, DistributionRole role) {
				Spaces spaces = metaspace.getSpaces();
				Space space = (Space) spaces.getChild(spaceName);
				if (space == null) {
					return;
				}
				SpaceMembers members = space.getMembers();
				SpaceMember spaceMember = (SpaceMember) members
						.getMemberById(member.getId());
				if (spaceMember == null) {
					onJoin(spaceName, member, role);
				} else {
					spaceMember.setMember(member);
					spaceMember.setDistributionRole(role);
				}
			}

			@Override
			public void onLeave(String spaceName,
					com.tibco.as.space.Member member) {
				Spaces spaces = metaspace.getSpaces();
				Space space = (Space) spaces.getChild(spaceName);
				if (space == null) {
					return;
				}
				SpaceMembers members = space.getMembers();
				Member spaceMember = members.getMemberById(member.getId());
				if (spaceMember == null) {
					return;
				}
				members.removeChild(spaceMember);
			}

			@Override
			public void onJoin(String spaceName,
					com.tibco.as.space.Member member, DistributionRole role) {
				Spaces spaces = metaspace.getSpaces();
				Space space = (Space) spaces.getChild(spaceName);
				if (space == null) {
					return;
				}
				SpaceMembers members = space.getMembers();
				SpaceMember child = new SpaceMember();
				child.setMembers(members);
				child.setMember(member);
				child.setSelf(ms.getSelfMember().getName()
						.equals(member.getName()));
				child.setDistributionRole(role);
				members.addChild(child);
			}
		});
		ms.listenMetaspaceMembers(new MetaspaceMemberListener() {

			@Override
			public void onUpdate(com.tibco.as.space.Member member,
					ManagementRole role) {
				MetaspaceMembers members = metaspace.getMembers();
				Member metaspaceMember = members.getMemberById(member.getId());
				if (metaspaceMember == null) {
					onJoin(member, role);
				} else {
					metaspaceMember.setMember(member);
				}
			}

			@Override
			public void onLeave(com.tibco.as.space.Member member) {
				MetaspaceMembers members = metaspace.getMembers();
				Member metaspaceMember = members.getMemberById(member.getId());
				if (metaspaceMember == null) {
					return;
				}
				members.removeChild(metaspaceMember);
			}

			@Override
			public void onJoin(com.tibco.as.space.Member member,
					ManagementRole role) {
				MetaspaceMembers members = metaspace.getMembers();
				MetaspaceMember child = new MetaspaceMember();
				child.setMembers(members);
				child.setMember(member);
				child.setSelf(ms.getSelfMember().getId().equals(member.getId()));
				members.addChild(child);
			}
		});
		ms.listenSpaceDefs(new SpaceDefListener() {

			@Override
			public void onDrop(SpaceDef spaceDef) {
				metaspace.getSpaces().removeChild(spaceDef.getName());
			}

			@Override
			public void onDefine(SpaceDef spaceDef) {
				Space space = new Space();
				space.setSpaces(metaspace.getSpaces());
				space.setSpaceDef(spaceDef);
				metaspace.getSpaces().addChild(space);
				SpaceMembers members = space.getMembers();
				String spaceName = spaceDef.getName();
				try {
					for (com.tibco.as.space.Member member : ms
							.getSpaceMembers(spaceName)) {
						SpaceMember child = new SpaceMember();
						child.setMembers(members);
						child.setMember(member);
						child.setSelf(ms.getSelfMember().getId()
								.equals(member.getId()));
						child.setDistributionRole(member
								.getDistributionRole(spaceName));
						members.addChild(child);
					}
				} catch (ASException e) {
					SpaceBarPlugin.logException("Could not get space members",
							e);
				}
			}

			@Override
			public void onAlter(SpaceDef oldSpaceDef, SpaceDef newSpaceDef) {
				String oldSpaceName = oldSpaceDef.getName();
				Space space = metaspace.getSpaces().getChild(oldSpaceName);
				if (oldSpaceName.equals(newSpaceDef.getName())) {
					if (space == null) {
						onDefine(newSpaceDef);
					} else {
						space.setSpaceDef(newSpaceDef);
					}
				} else {
					metaspace.removeChild(oldSpaceName);
					onDefine(newSpaceDef);
				}
			}
		});
	}

	public void disconnect() throws Exception {
		if (ms != null) {
			ms.closeAll();
			ms = null;
		}
	}

	public String getMetaspaceName() {
		return ms == null ? null : ms.getName();
	}

	public Metaspace getMetaspace() {
		return ASCommon.getMetaspace(getMetaspaceName());
	}

	public boolean isConnected() {
		return ms != null;
	}

}
