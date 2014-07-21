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
import com.tibco.as.space.Member.DistributionRole;
import com.tibco.as.space.SpaceDef;
import com.tibco.as.space.SpaceDef.CachePolicy;
import com.tibco.as.space.SpaceDef.DistributionPolicy;
import com.tibco.as.space.SpaceDef.EvictionPolicy;
import com.tibco.as.space.SpaceDef.LockScope;
import com.tibco.as.space.SpaceDef.PersistencePolicy;
import com.tibco.as.space.SpaceDef.PersistenceType;
import com.tibco.as.space.SpaceDef.ReplicationPolicy;
import com.tibco.as.space.SpaceDef.UpdateTransport;
import com.tibco.as.util.Utils;

public class Space extends AbstractElement implements Cloneable {

	private Spaces spaces;
	private SpaceMembers members = new SpaceMembers(this);
	private SpaceFields fields = new SpaceFields(this);
	private Keys keys = new Keys(this);
	private Distribution distribution = new Distribution(this);
	private Indexes indexes = new Indexes(this);
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

	public Space(Spaces spaces) {
		this.spaces = spaces;
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
			Field field = new Field(fields);
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
				index = new Index(indexes, indexName);
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
		Space space = new Space(getParent());
		copyTo(space);
		return space;
	}

	@Override
	public Spaces getParent() {
		return spaces;
	}

	public void copyTo(Space space) {
		space.name = name;
		space.cachePolicy = cachePolicy;
		space.capacity = capacity;
		space.distributionPolicy = distributionPolicy;
		space.evictionPolicy = evictionPolicy;
		space.forgetOldValue = forgetOldValue;
		space.hostAwareReplication = hostAwareReplication;
		space.routed = routed;
		space.lockScope = lockScope;
		space.lockTtl = lockTtl;
		space.lockWait = lockWait;
		space.minSeederCount = minSeederCount;
		space.persistenceDistributionPolicy = persistenceDistributionPolicy;
		space.persistencePolicy = persistencePolicy;
		space.persistenceType = persistenceType;
		space.phaseCount = phaseCount;
		space.phaseRatio = phaseRatio;
		space.queryLimit = queryLimit;
		space.queryTimeout = queryTimeout;
		space.readTimeout = readTimeout;
		space.replicationCount = replicationCount;
		space.replicationPolicy = replicationPolicy;
		space.spaceWait = spaceWait;
		space.ttl = ttl;
		space.updateTransport = updateTransport;
		space.virtualNodeCount = virtualNodeCount;
		space.writeTimeout = writeTimeout;
		space.fields = fields.clone();
		space.keys = keys.clone();
		space.distribution = distribution.clone();
		space.indexes = indexes.clone();
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
		this.space = spaces.getParent().getConnection().getMetaspace()
				.getSpace(name, DistributionRole.LEECH);
	}

	public void joinSeeder() throws ASException {
		this.space = spaces.getParent().getConnection().getMetaspace()
				.getSpace(name, DistributionRole.SEEDER);
	}

	public synchronized void leave() throws ASException {
		if (space == null) {
			return;
		}
		space.close();
		space = null;
	}

	public boolean isJoined() {
		return space != null;
	}

}
