package com.tibco.as.spacebar.ui.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.tibco.as.space.ASException;
import com.tibco.as.space.FieldDef;
import com.tibco.as.space.FieldDef.FieldType;
import com.tibco.as.space.IndexDef;
import com.tibco.as.space.IndexDef.IndexType;
import com.tibco.as.space.KeyDef;
import com.tibco.as.space.Member;
import com.tibco.as.space.Member.DistributionRole;
import com.tibco.as.space.Metaspace;
import com.tibco.as.space.SpaceDef;
import com.tibco.as.space.SpaceDef.CachePolicy;
import com.tibco.as.space.SpaceDef.DistributionPolicy;
import com.tibco.as.space.SpaceDef.EvictionPolicy;
import com.tibco.as.space.SpaceDef.LockScope;
import com.tibco.as.space.SpaceDef.PersistencePolicy;
import com.tibco.as.space.SpaceDef.PersistenceType;
import com.tibco.as.space.SpaceDef.ReplicationPolicy;
import com.tibco.as.space.SpaceDef.SpaceState;
import com.tibco.as.space.SpaceDef.UpdateTransport;
import com.tibco.as.spacebar.ui.SpaceBarPlugin;
import com.tibco.as.util.Utils;

public class Space extends AbstractElement implements Cloneable {

	private Spaces spaces;
	private SpaceMembers members;
	private SpaceFields fields;
	private Keys keys;
	private Distribution distribution;
	private Indexes indexes;
	private CachePolicy cachePolicy;
	private long capacity;
	private DistributionPolicy distributionPolicy;
	private EvictionPolicy evictionPolicy;
	private long fileSyncInterval;
	private boolean forgetOldValue;
	private boolean hostAwareReplication;
	private IndexType keyIndexType;
	private LockScope lockScope;
	private long lockTtl;
	private long lockWait;
	private int minSeederCount;
	private String name;
	private DistributionPolicy persistenceDistributionPolicy;
	private PersistencePolicy persistencePolicy;
	private PersistenceType persistenceType;
	private int phaseCount;
	private int phaseRatio;
	private long queryLimit;
	private long queryTimeout;
	private long readTimeout;
	private int replicationCount;
	private ReplicationPolicy replicationPolicy;
	private boolean routed;
	private long spaceWait;
	private long ttl;
	private UpdateTransport updateTransport;
	private int virtualNodeCount;
	private long writeTimeout;
	private com.tibco.as.space.Space space;

	public Space() {
		members = new SpaceMembers();
		members.setSpace(this);
		fields = new SpaceFields();
		fields.setSpace(this);
		keys = new Keys();
		keys.setSpace(this);
		distribution = new Distribution();
		distribution.setSpace(this);
		indexes = new Indexes();
		indexes.setSpace(this);
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		fireNameChange(this.name, this.name = name);
	}

	public void setSpaceDef(SpaceDef spaceDef) {
		setCachePolicy(spaceDef.getCachePolicy());
		setCapacity(spaceDef.getCapacity());
		setDistributionPolicy(spaceDef.getDistributionPolicy());
		setEvictionPolicy(spaceDef.getEvictionPolicy());
		if (Utils.hasSpaceDefMethod("getFileSyncInterval")) {
			setFileSyncInterval(spaceDef.getFileSyncInterval());
		}
		setForgetOldValue(spaceDef.isForgetOldValue());
		setHostAwareReplication(spaceDef.isHostAwareReplication());
		setKeyIndexType(spaceDef.getKeyDef().getIndexType());
		setLockScope(spaceDef.getLockScope());
		setLockTTL(spaceDef.getLockTTL());
		setLockWait(spaceDef.getLockWait());
		setMinSeederCount(spaceDef.getMinSeederCount());
		setName(spaceDef.getName());
		setPersistenceDistributionPolicy(spaceDef
				.getPersistenceDistributionPolicy());
		setPersistencePolicy(spaceDef.getPersistencePolicy());
		setPersistenceType(spaceDef.getPersistenceType());
		setPhaseCount(spaceDef.getPhaseCount());
		if (Utils.hasSpaceDefMethod("getPhaseRatio")) {
			setPhaseRatio(spaceDef.getPhaseRatio());
		}
		if (Utils.hasSpaceDefMethod("getQueryLimit")) {
			setQueryLimit(spaceDef.getQueryLimit());
		}
		if (Utils.hasSpaceDefMethod("getQueryTimeout")) {
			setQueryTimeout(spaceDef.getQueryTimeout());
		}
		setReadTimeout(spaceDef.getReadTimeout());
		setReplicationCount(spaceDef.getReplicationCount());
		setReplicationPolicy(spaceDef.getReplicationPolicy());
		if (Utils.hasSpaceDefMethod("isRouted")) {
			setRouted(spaceDef.isRouted());
		}
		setSpaceWait(spaceDef.getSpaceWait());
		setTTL(spaceDef.getTTL());
		setUpdateTransport(spaceDef.getUpdateTransport());
		setVirtualNodeCount(spaceDef.getVirtualNodeCount());
		setWriteTimeout(spaceDef.getWriteTimeout());
		Collection<String> keyFields = spaceDef.getKeyDef().getFieldNames();
		Collection<String> distributionFields = new ArrayList<String>();
		if (Utils.hasSpaceDefMethod("getDistributionFields")) {
			if (spaceDef.getDistributionFields() != null) {
				distributionFields = spaceDef.getDistributionFields();
			}
		}
		for (FieldDef fieldDef : spaceDef.getFieldDefs()) {
			String fieldName = fieldDef.getName();
			Field field = new Field();
			field.setFields(fields);
			field.setName(fieldName);
			field.setType(fieldDef.getType());
			field.setNullable(fieldDef.isNullable());
			if (Utils.hasFieldDefMethod("isEncrypted")) {
				field.setEncrypted(fieldDef.isEncrypted());
			}
			field.setKey(keyFields.contains(fieldName));
			field.setDistribution(distributionFields.contains(fieldName));
			fields.addChild(field);
		}
		for (String keyField : keyFields) {
			Field field = fields.getField(keyField);
			keys.addChild(field);
		}
		for (String fieldName : distributionFields) {
			distribution.addChild(fields.getField(fieldName));
		}
		for (Index index : indexes.getIndexes()) {
			if (spaceDef.getIndexDef(index.getName()) == null) {
				indexes.removeChild(index);
			}
		}
		for (IndexDef indexDef : spaceDef.getIndexDefList()) {
			String indexName = indexDef.getName();
			Index index = indexes.getIndex(indexName);
			boolean newIndex = false;
			if (index == null) {
				index = new Index();
				index.setIndexes(indexes);
				index.setName(indexName);
				newIndex = true;
			}
			index.setType(indexDef.getIndexType());
			Collection<String> fieldNames = indexDef.getFieldNames();
			for (IElement field : index.getChildren()) {
				if (!fieldNames.contains(field.getName())) {
					index.removeChild(field);
				}
			}
			for (String fieldName : fieldNames) {
				if (index.getChild(fieldName) == null) {
					index.addChild(fields.getChild(fieldName));
				}
			}
			if (newIndex) {
				indexes.addChild(index);
			}
		}
	}

	public SpaceDef getSpaceDef() {
		SpaceDef spaceDef = SpaceDef.create();
		spaceDef.setCachePolicy(cachePolicy);
		spaceDef.setCapacity(capacity);
		spaceDef.setDistributionPolicy(distributionPolicy);
		spaceDef.setEvictionPolicy(evictionPolicy);
		if (Utils.hasSpaceDefMethod("setFileSyncInterval")) {
			spaceDef.setFileSyncInterval(fileSyncInterval);
		}
		spaceDef.setForgetOldValue(forgetOldValue);
		spaceDef.setHostAwareReplication(hostAwareReplication);
		spaceDef.setLockScope(lockScope);
		spaceDef.setLockTTL(lockTtl);
		spaceDef.setLockWait(lockWait);
		spaceDef.setMinSeederCount(minSeederCount);
		spaceDef.setName(name);
		spaceDef.setPersistenceDistributionPolicy(persistenceDistributionPolicy);
		spaceDef.setPersistencePolicy(persistencePolicy);
		spaceDef.setPersistenceType(persistenceType);
		spaceDef.setPhaseCount(phaseCount);
		if (Utils.hasSpaceDefMethod("setPhaseRatio")) {
			spaceDef.setPhaseRatio(phaseRatio);
		}
		if (Utils.hasSpaceDefMethod("setQueryLimit")) {
			spaceDef.setQueryLimit(queryLimit);
		}
		if (Utils.hasSpaceDefMethod("setQueryTimeout")) {
			spaceDef.setQueryTimeout(queryTimeout);
		}
		spaceDef.setReadTimeout(readTimeout);
		spaceDef.setReplicationCount(replicationCount);
		spaceDef.setReplicationPolicy(replicationPolicy);
		if (Utils.hasSpaceDefMethod("setRouted")) {
			spaceDef.setRouted(routed);
		}
		spaceDef.setSpaceWait(spaceWait);
		spaceDef.setTTL(ttl);
		spaceDef.setUpdateTransport(updateTransport);
		spaceDef.setVirtualNodeCount(virtualNodeCount);
		spaceDef.setWriteTimeout(writeTimeout);
		for (IElement element : fields.getChildren()) {
			Field field = (Field) element;
			FieldDef fieldDef = FieldDef.create(field.getName(),
					FieldType.valueOf(field.getType().name()));
			if (Utils.hasFieldDefMethod("setEncrypted")) {
				fieldDef.setEncrypted(field.isEncrypted());
			}
			fieldDef.setNullable(field.isNullable());
			spaceDef.putFieldDef(fieldDef);
		}
		for (Index index : indexes.getIndexes()) {
			IndexDef indexDef = IndexDef.create(index.getName());
			indexDef.setIndexType(index.getType());
			List<String> fields = new ArrayList<String>();
			for (Field indexField : index.getChildren()) {
				fields.add(indexField.getName());
			}
			indexDef.setFieldNames(fields.toArray(new String[fields.size()]));
			spaceDef.addIndexDef(indexDef);
		}
		List<Field> distributionFields = distribution.getChildren();
		String[] distribution = new String[distributionFields.size()];
		for (int index = 0; index < distribution.length; index++) {
			distribution[index] = distributionFields.get(index).getName();
		}
		if (Utils.hasSpaceDefMethod("setDistributionFields")) {
			spaceDef.setDistributionFields(distribution);
		}
		KeyDef keyDef = spaceDef.getKeyDef();
		keyDef.setIndexType(keyIndexType);
		if (!keys.getChildren().isEmpty()) {
			List<String> keyFields = new ArrayList<String>();
			for (Field keyField : keys.getChildren()) {
				keyFields.add(keyField.getName());
			}
			keyDef.setFieldNames(keyFields.toArray(new String[keyFields.size()]));
		}
		spaceDef.setKeyDef(keyDef);
		return spaceDef;
	}

	@Override
	public Space clone() {
		Space space = new Space();
		copyTo(space);
		return space;
	}

	@Override
	public Spaces getParent() {
		return spaces;
	}

	@Override
	public void copyTo(IElement element) {
		Space space = (Space) element;
		space.setSpaces(spaces);
		space.setName(name);
		space.setFields(fields.clone());
		space.setKeys(keys.clone());
		space.setDistribution(distribution.clone());
		space.setIndexes(indexes.clone());
		space.setCachePolicy(cachePolicy);
		space.setCapacity(capacity);
		space.setDistributionPolicy(distributionPolicy);
		space.setEvictionPolicy(evictionPolicy);
		space.setForgetOldValue(forgetOldValue);
		space.setHostAwareReplication(hostAwareReplication);
		space.setRouted(routed);
		space.setLockScope(lockScope);
		space.setLockTTL(lockTtl);
		space.setLockWait(lockWait);
		space.setMinSeederCount(minSeederCount);
		space.setPersistenceDistributionPolicy(persistenceDistributionPolicy);
		space.setPersistencePolicy(persistencePolicy);
		space.setPersistenceType(persistenceType);
		space.setPhaseCount(phaseCount);
		space.setPhaseRatio(phaseRatio);
		space.setQueryLimit(queryLimit);
		space.setQueryTimeout(queryTimeout);
		space.setReadTimeout(readTimeout);
		space.setReplicationCount(replicationCount);
		space.setReplicationPolicy(replicationPolicy);
		space.setSpaceWait(spaceWait);
		space.setTTL(ttl);
		space.setUpdateTransport(updateTransport);
		space.setVirtualNodeCount(virtualNodeCount);
		space.setWriteTimeout(writeTimeout);
	}

	public void setIndexes(Indexes indexes) {
		this.indexes = indexes;
	}

	public void setDistribution(Distribution distribution) {
		this.distribution = distribution;
	}

	public void setKeys(Keys keys) {
		this.keys = keys;
	}

	public void setFields(SpaceFields fields) {
		this.fields = fields;
	}

	public void setSpaces(Spaces spaces) {
		this.spaces = spaces;
	}

	public SpaceMembers getMembers() {
		return members;
	}

	public SpaceFields getFields() {
		return fields;
	}

	public Indexes getIndexes() {
		return indexes;
	}

	public Keys getKeys() {
		return keys;
	}

	public Distribution getDistribution() {
		return distribution;
	}

	public com.tibco.as.space.Space getSpace() throws ASException {
		return spaces.getParent().getConnection().getMetaspace().getSpace(name);
	}

	public int getReplicationCount() {
		return replicationCount;
	}

	public void setReplicationCount(int replicationCount) {
		this.replicationCount = replicationCount;
	}

	public ReplicationPolicy getReplicationPolicy() {
		return replicationPolicy;
	}

	public void setReplicationPolicy(ReplicationPolicy replicationPolicy) {
		this.replicationPolicy = replicationPolicy;
	}

	public EvictionPolicy getEvictionPolicy() {
		return evictionPolicy;
	}

	public void setEvictionPolicy(EvictionPolicy evictionPolicy) {
		this.evictionPolicy = evictionPolicy;
	}

	public CachePolicy getCachePolicy() {
		return cachePolicy;
	}

	public void setCachePolicy(CachePolicy cachePolicy) {
		this.cachePolicy = cachePolicy;
	}

	public PersistencePolicy getPersistencePolicy() {
		return persistencePolicy;
	}

	public void setPersistencePolicy(PersistencePolicy persistencePolicy) {
		this.persistencePolicy = persistencePolicy;
	}

	public PersistenceType getPersistenceType() {
		return persistenceType;
	}

	public void setPersistenceType(PersistenceType persistenceType) {
		this.persistenceType = persistenceType;
	}

	public DistributionPolicy getPersistenceDistributionPolicy() {
		return persistenceDistributionPolicy;
	}

	public void setPersistenceDistributionPolicy(
			DistributionPolicy persistenceDistributionPolicy) {
		this.persistenceDistributionPolicy = persistenceDistributionPolicy;
	}

	public int getMinSeederCount() {
		return minSeederCount;
	}

	public void setMinSeederCount(int minSeederCount) {
		this.minSeederCount = minSeederCount;
	}

	public DistributionPolicy getDistributionPolicy() {
		return distributionPolicy;
	}

	public void setDistributionPolicy(DistributionPolicy distributionPolicy) {
		this.distributionPolicy = distributionPolicy;
	}

	public LockScope getLockScope() {
		return lockScope;
	}

	public void setLockScope(LockScope lockScope) {
		this.lockScope = lockScope;
	}

	public long getCapacity() {
		return capacity;
	}

	public void setCapacity(long capacity) {
		this.capacity = capacity;
	}

	public long getTTL() {
		return ttl;
	}

	public void setTTL(long ttl) {
		this.ttl = ttl;
	}

	public long getLockTTL() {
		return lockTtl;
	}

	public void setLockTTL(long lockTtl) {
		this.lockTtl = lockTtl;
	}

	public long getLockWait() {
		return lockWait;
	}

	public void setLockWait(long lockWait) {
		this.lockWait = lockWait;
	}

	public int getPhaseCount() {
		return phaseCount;
	}

	public void setPhaseCount(int phaseCount) {
		this.phaseCount = phaseCount;
	}

	public long getSpaceWait() {
		return spaceWait;
	}

	public void setSpaceWait(long spaceWait) {
		this.spaceWait = spaceWait;
	}

	public long getWriteTimeout() {
		return writeTimeout;
	}

	public void setWriteTimeout(long writeTimeout) {
		this.writeTimeout = writeTimeout;
	}

	public long getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(long readTimeout) {
		this.readTimeout = readTimeout;
	}

	public long getQueryTimeout() {
		return queryTimeout;
	}

	public void setQueryTimeout(long queryTimeout) {
		this.queryTimeout = queryTimeout;
	}

	public UpdateTransport getUpdateTransport() {
		return updateTransport;
	}

	public void setUpdateTransport(UpdateTransport updateTransport) {
		this.updateTransport = updateTransport;
	}

	public int getVirtualNodeCount() {
		return virtualNodeCount;
	}

	public void setVirtualNodeCount(int virtualNodeCount) {
		this.virtualNodeCount = virtualNodeCount;
	}

	public boolean isForgetOldValue() {
		return forgetOldValue;
	}

	public void setForgetOldValue(boolean forgetOldValue) {
		this.forgetOldValue = forgetOldValue;
	}

	public boolean isHostAwareReplication() {
		return hostAwareReplication;
	}

	public void setHostAwareReplication(boolean hostAwareReplication) {
		this.hostAwareReplication = hostAwareReplication;
	}

	public IndexType getKeyIndexType() {
		return keyIndexType;
	}

	public void setKeyIndexType(IndexType keyIndexType) {
		this.keyIndexType = keyIndexType;
	}

	public boolean isRouted() {
		return routed;
	}

	public void setRouted(boolean routed) {
		this.routed = routed;
	}

	public int getPhaseRatio() {
		return phaseRatio;
	}

	public void setPhaseRatio(int phaseRatio) {
		this.phaseRatio = phaseRatio;
	}

	public long getQueryLimit() {
		return queryLimit;
	}

	public void setQueryLimit(long queryLimit) {
		this.queryLimit = queryLimit;
	}

	public long getFileSyncInterval() {
		return fileSyncInterval;
	}

	public void setFileSyncInterval(long fileSyncInterval) {
		this.fileSyncInterval = fileSyncInterval;
	}

	@Override
	public List<? extends IElement> getChildren() {
		return Arrays.asList(members, fields, indexes);
	}

	public void joinLeech() throws ASException {
		join(DistributionRole.LEECH);
	}

	private void join(DistributionRole role) throws ASException {
		com.tibco.as.space.Space oldSpace = space;
		space = spaces.getParent().getConnection().getMetaspace()
				.getSpace(name, role);
		if (oldSpace == null) {
			return;
		}
		oldSpace.close();
	}

	public void joinSeeder() throws ASException {
		join(DistributionRole.SEEDER);
	}

	public synchronized void leave() throws ASException {
		if (space == null) {
			return;
		}
		space.close();
		space = null;
	}

	public boolean isReady() {
		try {
			return getSpaceState() == SpaceState.READY;
		} catch (ASException e) {
			SpaceBarPlugin.logException(e);
		}
		return false;
	}

	public boolean isSuspended() {
		try {
			return getSpaceState() == SpaceState.SUSPENDED;
		} catch (ASException e) {
			SpaceBarPlugin.logException(e);
		}
		return false;
	}

	private SpaceState getSpaceState() throws ASException {
		Metaspace metaspace = spaces.getParent().getConnection().getMetaspace();
		return metaspace.getSpaceState(name);
	}

	public boolean isJoined() {
		Metaspace metaspace = spaces.getParent().getConnection().getMetaspace();
		Member self = metaspace.getSelfMember();
		try {
			for (Member member : metaspace.getSpaceMembers(name)) {
				if (member.getId().equals(self.getId())) {
					return true;
				}
			}
		} catch (ASException e) {
			SpaceBarPlugin.logException("Could not get space members", e);
		}
		return false;
	}
}
