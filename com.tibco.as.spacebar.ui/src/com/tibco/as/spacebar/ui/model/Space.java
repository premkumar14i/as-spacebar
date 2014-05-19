package com.tibco.as.spacebar.ui.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.tibco.as.space.ASException;
import com.tibco.as.space.FieldDef;
import com.tibco.as.space.IndexDef;
import com.tibco.as.space.KeyDef;
import com.tibco.as.space.SpaceDef;
import com.tibco.as.space.FieldDef.FieldType;
import com.tibco.as.utils.ASUtils;

public class Space extends Element implements Cloneable {

	private SpaceDef spaceDef = SpaceDef.create();

	private Members members = new Members(this, "Members");

	private SpaceFields fields = new SpaceFields(this, "Fields");

	private Keys keys = new Keys(this, "Keys");

	private Distribution distribution = new Distribution(this, "Distribution");

	private Indexes indexes = new Indexes(this, "Indexes");

	public Space(Spaces spaces, SpaceDef spaceDef) {
		super(spaces, spaceDef.getName());
		addChild(members);
		addChild(fields);
		addChild(indexes);
		setSpaceDef(spaceDef);
	}

	@Override
	public Space clone() {
		Space space = new Space(getParent(), spaceDef);
		copyTo(space);
		return space;
	}

	@Override
	public Spaces getParent() {
		return (Spaces) super.getParent();
	}

	public void copyTo(Space space) {
		super.copyTo(space);
		space.spaceDef = spaceDef;
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
				.getSpace(spaceDef.getName());
	}

	public SpaceDef getSpaceDef() {
		return spaceDef;
	}

	public SpaceDef getActualSpaceDef() {
		spaceDef.getFieldDefs().clear();
		for (Field field : fields.getFields()) {
			FieldDef fieldDef = FieldDef.create(field.getName(),
					FieldType.valueOf(field.getType().name()));
			if (ASUtils.hasMethod(FieldDef.class, "setEncrypted")) {
				fieldDef.setEncrypted(field.isEncrypted());
			}
			fieldDef.setNullable(field.isNullable());
			spaceDef.getFieldDefs().add(fieldDef);
		}
		spaceDef.getIndexDefList().clear();
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

	public void setSpaceDef(SpaceDef spaceDef) {
		this.spaceDef = spaceDef;
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

}
