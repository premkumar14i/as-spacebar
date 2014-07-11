package com.tibco.as.spacebar.ui.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.tibco.as.space.ASException;
import com.tibco.as.space.FieldDef;
import com.tibco.as.space.FieldDef.FieldType;
import com.tibco.as.space.IndexDef;
import com.tibco.as.space.KeyDef;
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
	private String name;
	private SpaceMembers members = new SpaceMembers(this);
	private SpaceFields fields = new SpaceFields(this);
	private Keys keys = new Keys(this);
	private Distribution distribution = new Distribution(this);
	private Indexes indexes = new Indexes(this);
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
		setName(spaceDef.getName());
		setCachePolicy(spaceDef.getCachePolicy());
		setCapacity(spaceDef.getCapacity());
		setDistributionPolicy(spaceDef.getDistributionPolicy());
		setEvictionPolicy(spaceDef.getEvictionPolicy());
		setForgetOldValue(spaceDef.isForgetOldValue());
		setHostAwareReplication(spaceDef.isHostAwareReplication());
		if (Utils.hasMethod(SpaceDef.class, "isRouted")) {
			setRouted(spaceDef.isRouted());
		}
		setLockScope(spaceDef.getLockScope());
		setLockTTL(spaceDef.getLockTTL());
		setLockWait(spaceDef.getLockWait());
		setMinSeederCount(spaceDef.getMinSeederCount());
		setPersistenceDistributionPolicy(spaceDef
				.getPersistenceDistributionPolicy());
		setPersistencePolicy(spaceDef.getPersistencePolicy());
		setPersistenceType(spaceDef.getPersistenceType());
		setPhaseCount(spaceDef.getPhaseCount());
		if (Utils.hasMethod(SpaceDef.class, "getPhaseRatio")) {
			setPhaseRatio(spaceDef.getPhaseRatio());
		}
		if (Utils.hasMethod(SpaceDef.class, "getQueryLimit")) {
			setQueryLimit(spaceDef.getQueryLimit());
		}
		if (Utils.hasMethod(SpaceDef.class, "getQueryTimeout")) {
			setQueryTimeout(spaceDef.getQueryTimeout());
		}
		setReadTimeout(spaceDef.getReadTimeout());
		setReplicationCount(spaceDef.getReplicationCount());
		setReplicationPolicy(spaceDef.getReplicationPolicy());
		setSpaceWait(spaceDef.getSpaceWait());
		setTTL(spaceDef.getTTL());
		setUpdateTransport(spaceDef.getUpdateTransport());
		setVirtualNodeCount(spaceDef.getVirtualNodeCount());
		setWriteTimeout(spaceDef.getWriteTimeout());
		Collection<String> keyFields = spaceDef.getKeyDef().getFieldNames();
		Collection<String> distributionFields = spaceDef
				.getDistributionFields();
		if (distributionFields == null) {
			distributionFields = Collections.emptyList();
		}
		for (FieldDef fieldDef : spaceDef.getFieldDefs()) {
			String fieldName = fieldDef.getName();
			Field field = new Field(fields);
			field.setName(fieldName);
			field.setType(fieldDef.getType());
			field.setNullable(fieldDef.isNullable());
			if (Utils.hasMethod(FieldDef.class, "isEncrypted")) {
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
		if (Utils.hasMethod(SpaceDef.class, "getDistributionFields")) {
			if (distributionFields != null) {
				for (String distributionField : distributionFields) {
					Field field = fields.getField(distributionField);
					distribution.addChild(field);
				}
			}
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
		SpaceDef spaceDef = SpaceDef.create(getName());
		spaceDef.setCachePolicy(cachePolicy);
		spaceDef.setCapacity(capacity);
		spaceDef.setDistributionPolicy(distributionPolicy);
		spaceDef.setEvictionPolicy(evictionPolicy);
		spaceDef.setForgetOldValue(forgetOldValue);
		spaceDef.setHostAwareReplication(hostAwareReplication);
		if (Utils.hasMethod(SpaceDef.class, "setRouted")) {
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
		if (Utils.hasMethod(SpaceDef.class, "setPhaseRatio")) {
			spaceDef.setPhaseRatio(phaseRatio);
		}
		if (Utils.hasMethod(SpaceDef.class, "setQueryLimit")) {
			spaceDef.setQueryLimit(queryLimit);
		}
		if (Utils.hasMethod(SpaceDef.class, "setQueryTimeout")) {
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
		for (IElement element : fields.getChildren()) {
			Field field = (Field) element;
			FieldDef fieldDef = FieldDef.create(field.getName(),
					FieldType.valueOf(field.getType().name()));
			if (Utils.hasMethod(FieldDef.class, "setEncrypted")) {
				fieldDef.setEncrypted(field.isEncrypted());
			}
			fieldDef.setNullable(field.isNullable());
			spaceDef.getFieldDefs().add(fieldDef);
		}
		for (Index index : indexes.getIndexes()) {
			IndexDef indexDef = IndexDef.create(index.getName());
			indexDef.setIndexType(index.getType());
			List<String> indexFieldNames = new ArrayList<String>();
			for (Field field : index.getChildren()) {
				indexFieldNames.add(field.getName());
			}
			indexDef.setFieldNames(indexFieldNames
					.toArray(new String[indexFieldNames.size()]));
			spaceDef.addIndexDef(indexDef);
		}
		List<Field> distributionFields = distribution.getChildren();
		String[] distribution = new String[distributionFields.size()];
		for (int index = 0; index < distribution.length; index++) {
			distribution[index] = distributionFields.get(index).getName();
		}
		if (Utils.hasMethod(SpaceDef.class, "setDistributionFields")) {
			spaceDef.setDistributionFields(distribution);
		}
		KeyDef keyDef = spaceDef.getKeyDef();
		String[] keyFields = new String[keys.getChildren().size()];
		for (int index = 0; index < keyFields.length; index++) {
			keyFields[index] = keys.getChildren().get(index).getName();
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
		return spaces.getParent().getConnection().getMetaspace()
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

	@Override
	public List<? extends IElement> getChildren() {
		return Arrays.asList(members, fields, indexes);
	}

}
