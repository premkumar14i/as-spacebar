package com.tibco.as.spacebar.ui.model;

import com.tibco.as.space.ASCommon;
import com.tibco.as.space.ASException;
import com.tibco.as.space.Member.DistributionRole;
import com.tibco.as.space.Member.ManagementRole;
import com.tibco.as.space.MemberDef;
import com.tibco.as.space.SpaceDef;
import com.tibco.as.space.listener.MetaspaceMemberListener;
import com.tibco.as.space.listener.SpaceDefListener;
import com.tibco.as.space.listener.SpaceMemberListener;
import com.tibco.as.spacebar.ui.SpaceBarPlugin;
import com.tibco.as.util.Utils;

public class Connection {

	private com.tibco.as.space.Metaspace ms;

	private Metaspace metaspace;

	public Connection(Metaspace metaspace) {
		this.metaspace = metaspace;
	}

	public void connect() throws Exception {
		ms = Utils.getMetaspace(metaspace.getMetaspaceName());
		if (ms == null) {
			final MemberDef memberDef = MemberDef.create();
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
			ms = com.tibco.as.space.Metaspace.connect(
					metaspace.getMetaspaceName(), memberDef);
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
				SpaceMember child = (SpaceMember) members.getChild(member
						.getName());
				if (child == null) {
					onJoin(spaceName, member, role);
				} else {
					child.setMember(member);
					child.setDistributionRole(role);
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
				Members members = space.getMembers();
				members.removeChild(member.getName());
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
				SpaceMember child = new SpaceMember(members);
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
				Member child = (Member) metaspace.getMembers().getChild(
						member.getName());
				if (child == null) {
					onJoin(member, role);
				} else {
					child.setMember(member);
				}
			}

			@Override
			public void onLeave(com.tibco.as.space.Member member) {
				metaspace.getMembers().removeChild(member.getName());
			}

			@Override
			public void onJoin(com.tibco.as.space.Member member,
					ManagementRole role) {
				MetaspaceMembers members = metaspace.getMembers();
				MetaspaceMember child = new MetaspaceMember(members);
				child.setMember(member);
				child.setSelf(ms.getSelfMember().getName()
						.equals(member.getName()));
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
				Space space = new Space(metaspace.getSpaces());
				space.setSpaceDef(spaceDef);
				metaspace.getSpaces().addChild(space);
				SpaceMembers members = space.getMembers();
				String spaceName = spaceDef.getName();
				try {
					for (com.tibco.as.space.Member member : ms
							.getSpaceMembers(spaceName)) {
						SpaceMember child = new SpaceMember(members);
						child.setMember(member);
						child.setSelf(ms.getSelfMember().getName()
								.equals(member.getName()));
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

	public com.tibco.as.space.Metaspace getMetaspace() {
		return ASCommon.getMetaspace(getMetaspaceName());
	}

}
