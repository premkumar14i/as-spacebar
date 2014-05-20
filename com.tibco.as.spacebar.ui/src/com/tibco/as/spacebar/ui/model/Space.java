package com.tibco.as.spacebar.ui.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.tibco.as.space.ASException;
import com.tibco.as.space.FieldDef;
import com.tibco.as.space.FieldDef.FieldType;
import com.tibco.as.space.IndexDef;
import com.tibco.as.space.KeyDef;
import com.tibco.as.space.MemberDef;
import com.tibco.as.space.SpaceDef;
import com.tibco.as.space.SpaceDef.CachePolicy;
import com.tibco.as.space.SpaceDef.DistributionPolicy;
import com.tibco.as.space.SpaceDef.EvictionPolicy;
import com.tibco.as.space.SpaceDef.LockScope;
import com.tibco.as.space.SpaceDef.PersistencePolicy;
import com.tibco.as.space.SpaceDef.PersistenceType;
import com.tibco.as.space.SpaceDef.ReplicationPolicy;
import com.tibco.as.space.SpaceDef.UpdateTransport;
import com.tibco.as.utils.ASUtils;

public class Space extends Element implements Cloneable {

	private Members members = new Members(this, "Members");

	private SpaceFields fields = new SpaceFields(this, "Fields");

	private Keys keys = new Keys(this, "Keys");

	private Distribution distribution = new Distribution(this, "Distribution");

	private Indexes indexes = new Indexes(this, "Indexes");

	private int replicationCount;
	private ReplicationPolicy replicationPolicy;
	private EvictionPolicy evictionPolicy;
	private CachePolicy cachePolicy;
	private PersistencePolicy persistencePolicy;
	private PersistenceType persistenceType;
	private DistributionPolicy persistenceDistributionPolicy;
	private int minSeederCount;
	private DistributionPolicy distributionPolicy;
	private LockScope lockScope;
	private long capacity;
	private long ttl;
	private long lockTtl;
	private long lockWait;
	private long phaseInterval;
	private int phaseCount;
	private long spaceWait;
	private long writeTimeout;
	private long readTimeout;
	private long queryTimeout;
	private UpdateTransport updateTransport;
	private int virtualNodeCount;
	private boolean forgetOldValue;
	private boolean hostAwareReplication;
	private boolean routed;
	private int phaseRatio;
	private long queryLimit;

	public Space(Spaces spaces) {
		this(spaces, SpaceDef.create());
	}

	public Space(Spaces spaces, SpaceDef spaceDef) {
		super(spaces, spaceDef.getName());
		addChild(members);
		addChild(fields);
		addChild(indexes);
		setSpaceDef(spaceDef);
	}

	public void setSpaceDef(SpaceDef spaceDef) {
		setName(spaceDef.getName());
		cachePolicy = spaceDef.getCachePolicy();
		capacity = spaceDef.getCapacity();
		distributionPolicy = spaceDef.getDistributionPolicy();
		evictionPolicy = spaceDef.getEvictionPolicy();
		forgetOldValue = spaceDef.isForgetOldValue();
		hostAwareReplication = spaceDef.isHostAwareReplication();
		if (ASUtils.hasMethod(SpaceDef.class, "isRouted")) {
			routed = spaceDef.isRouted();
		}
		lockScope = spaceDef.getLockScope();
		lockTtl = spaceDef.getLockTTL();
		lockWait = spaceDef.getLockWait();
		minSeederCount = spaceDef.getMinSeederCount();
		persistenceDistributionPolicy = spaceDef
				.getPersistenceDistributionPolicy();
		persistencePolicy = spaceDef.getPersistencePolicy();
		persistenceType = spaceDef.getPersistenceType();
		phaseCount = spaceDef.getPhaseCount();
		phaseInterval = spaceDef.getPhaseInterval();
		if (ASUtils.hasMethod(SpaceDef.class, "getPhaseRatio")) {
			phaseRatio = spaceDef.getPhaseRatio();
		}
		if (ASUtils.hasMethod(SpaceDef.class, "getQueryLimit")) {
			queryLimit = spaceDef.getQueryLimit();
		}
		if (ASUtils.hasMethod(SpaceDef.class, "getQueryTimeout")) {
			queryTimeout = spaceDef.getQueryTimeout();
		}
		readTimeout = spaceDef.getReadTimeout();
		replicationCount = spaceDef.getReplicationCount();
		replicationPolicy = spaceDef.getReplicationPolicy();
		spaceWait = spaceDef.getSpaceWait();
		ttl = spaceDef.getTTL();
		updateTransport = spaceDef.getUpdateTransport();
		virtualNodeCount = spaceDef.getVirtualNodeCount();
		writeTimeout = spaceDef.getWriteTimeout();
		for (FieldDef fieldDef : spaceDef.getFieldDefs()) {
			String fieldName = fieldDef.getName();
			Field field = new Field(fields, fieldName);
			field.setType(fieldDef.getType());
			field.setNullable(fieldDef.isNullable());
			if (ASUtils.hasMethod(FieldDef.class, "isEncrypted")) {
				field.setEncrypted(fieldDef.isEncrypted());
			}
			fields.addChild(field);
		}
		KeyDef keyDef = spaceDef.getKeyDef();
		for (String keyField : keyDef.getFieldNames()) {
			Field field = fields.getChild(keyField);
			keys.addChild(field);
		}
		if (ASUtils.hasMethod(SpaceDef.class, "getDistributionFields")) {
			Collection<String> distributionFields = spaceDef
					.getDistributionFields();
			if (distributionFields != null) {
				for (String distributionField : distributionFields) {
					Field field = fields.getChild(distributionField);
					distribution.addChild(field);
				}
			}
		}
		for (Index index : indexes.getIndexes()) {
			String indexName = index.getName();
			if (spaceDef.getIndexDef(indexName) == null) {
				indexes.removeChild(indexName);
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
			for (Field field : index.getFields()) {
				String fieldName = field.getName();
				if (!fieldNames.contains(fieldName)) {
					index.removeChild(fieldName);
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
		SpaceDef spaceDef = SpaceDef.create(getName());
		spaceDef.setCachePolicy(cachePolicy);
		spaceDef.setCapacity(capacity);
		spaceDef.setDistributionPolicy(distributionPolicy);
		spaceDef.setEvictionPolicy(evictionPolicy);
		spaceDef.setForgetOldValue(forgetOldValue);
		spaceDef.setHostAwareReplication(hostAwareReplication);
		if (ASUtils.hasMethod(SpaceDef.class, "setRouted")) {
			spaceDef.setRouted(routed);
		}
		spaceDef.setLockScope(lockScope);
		spaceDef.setLockTTL(lockTtl);
		spaceDef.setLockWait(lockWait);
		spaceDef.setMinSeederCount(minSeederCount);
		spaceDef.setPersistenceDistributionPolicy(persistenceDistributionPolicy);
		spaceDef.setPersistencePolicy(persistencePolicy);
		spaceDef.setPersistenceType(persistenceType);
		spaceDef.setPhaseCount(phaseCount);
		spaceDef.setPhaseInterval(phaseInterval);
		if (ASUtils.hasMethod(SpaceDef.class, "setPhaseRatio")) {
			spaceDef.setPhaseRatio(phaseRatio);
		}
		if (ASUtils.hasMethod(SpaceDef.class, "setQueryLimit")) {
			spaceDef.setQueryLimit(queryLimit);
		}
		if (ASUtils.hasMethod(SpaceDef.class, "setQueryTimeout")) {
			spaceDef.setQueryTimeout(queryTimeout);
		}
		spaceDef.setReadTimeout(readTimeout);
		spaceDef.setReplicationCount(replicationCount);
		spaceDef.setReplicationPolicy(replicationPolicy);
		spaceDef.setSpaceWait(spaceWait);
		spaceDef.setTTL(ttl);
		spaceDef.setUpdateTransport(updateTransport);
		spaceDef.setVirtualNodeCount(virtualNodeCount);
		spaceDef.setWriteTimeout(writeTimeout);
		for (Field field : fields.getFields()) {
			FieldDef fieldDef = FieldDef.create(field.getName(),
					FieldType.valueOf(field.getType().name()));
			if (ASUtils.hasMethod(FieldDef.class, "setEncrypted")) {
				fieldDef.setEncrypted(field.isEncrypted());
			}
			fieldDef.setNullable(field.isNullable());
			spaceDef.getFieldDefs().add(fieldDef);
		}
		for (Index index : indexes.getIndexes()) {
			IndexDef indexDef = IndexDef.create(index.getName());
			indexDef.setIndexType(index.getType());
			List<String> indexFieldNames = new ArrayList<String>();
			for (Field field : index.getFields()) {
				indexFieldNames.add(field.getName());
			}
			indexDef.setFieldNames(indexFieldNames
					.toArray(new String[indexFieldNames.size()]));
			spaceDef.addIndexDef(indexDef);
		}
		List<Element> distribution = this.distribution.getChildren();
		String[] distributionFields = new String[distribution.size()];
		for (int index = 0; index < distributionFields.length; index++) {
			distributionFields[index] = distribution.get(index).getName();
		}
		if (ASUtils.hasMethod(SpaceDef.class, "setDistributionFields")) {
			spaceDef.setDistributionFields(distributionFields);
		}
		KeyDef keyDef = spaceDef.getKeyDef();
		String[] keyFields = new String[keys.getFields().size()];
		for (int index = 0; index < keyFields.length; index++) {
			keyFields[index] = keys.getFields().get(index).getName();
		}
		keyDef.setFieldNames(keyFields);
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
		return (Spaces) super.getParent();
	}

	public void copyTo(Space space) {
		super.copyTo(space);
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
		space.phaseInterval = phaseInterval;
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

	public Members getMembers() {
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
		return getParent().getMetaspace().getConnection().getMetaspace()
				.getSpace(getName());
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

	public long getPhaseInterval() {
		return phaseInterval;
	}

	public void setPhaseInterval(long phaseInterval) {
		this.phaseInterval = phaseInterval;
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

}
