package com.tibco.as.spacebar.ui.editor.continuous;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.tibco.as.space.Tuple;

public class ObservableTuple implements Map<String, Object>,
		Comparable<ObservableTuple> {

	private final Tuple tuple;
	private PropertyChangeSupport support = new PropertyChangeSupport(this);
	private PropertyChangeListener changeListener;
	private int id;

	public ObservableTuple(Tuple map, int id,
			PropertyChangeListener changeListener) {
		this.tuple = map;
		this.id = id;
		this.changeListener = changeListener;
		addPropertyChangeListener(this.changeListener);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		support.removePropertyChangeListener(listener);
		support.removePropertyChangeListener(changeListener);
	}

	@Override
	public int compareTo(ObservableTuple o) {
		return 0;
	}

	@Override
	public Object put(String key, Object value) {
		Object previous = tuple.put(key, value);
		if (!equals(previous, value)) {
			support.firePropertyChange(key, previous, value);
		}
		return previous;
	}

	private boolean equals(Object oldValue, Object newValue) {
		if (newValue == null) {
			return oldValue == null;
		}
		return newValue.equals(oldValue);
	}

	@Override
	public Object get(Object key) {
		return tuple.get(key);
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ObservableTuple other = (ObservableTuple) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public int getId() {
		return id;
	}

	@Override
	public int size() {
		return tuple.size();
	}

	@Override
	public boolean isEmpty() {
		return tuple.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return tuple.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return tuple.containsValue(value);
	}

	@Override
	public Object remove(Object key) {
		return tuple.remove(key);
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		tuple.putAll(m);
	}

	@Override
	public void clear() {
		tuple.clear();
	}

	@Override
	public Set<String> keySet() {
		return tuple.keySet();
	}

	@Override
	public Collection<Object> values() {
		return tuple.values();
	}

	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		return tuple.entrySet();
	}

	public Tuple getTuple() {
		return tuple;
	}
}