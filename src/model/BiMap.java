package model;

import java.util.*;

public class BiMap<K,V> {
	/**
	 * One-to-one mapping
	 * 
	 * Invariants: 
	 *   forwards = inverse(backwards)
	 */
	
	protected Map<K,V> forwards;
	protected Map<V,K> backwards;
	
	protected BiMap<V,K> inverse;
	
	public BiMap(){
		this.forwards = new HashMap<K,V>();
		this.backwards = new HashMap<V,K>();
	}
	
	/**
	 * Supply an inverse perspective on this BiMap.  Changes made to the inverse affect the natural BiMap and vice versa.
	 * @return the inverse BiMap
	 */
	public BiMap<V,K> inverse(){
		if (this.inverse==null){
			BiMap<V,K> invMap = new BiMap<V,K>();
			invMap.forwards = this.backwards;
			invMap.backwards = this.forwards;
			invMap.inverse = this;
			return invMap;
		} else {
			return this.inverse;
		}
	}
	
	/**
	 * Adds a relationship between key and value.  This method will throw an exception if there was a danger of overwriting.
	 * @param key
	 * @param value
	 * 
	 */
	public void put (K key, V value){		
		/***
		 * must maintain 1-1 invariant of BiMap
		 */
		// remove any existing occurrences of key or value
		if (backwards.containsKey(value))	throw new RuntimeException("Bimap already contains value: "+value);
		if (forwards.containsKey(key))		throw new RuntimeException("Bimap already contains key: "+key);
		
		// overwrite with new versions
		forwards.put(key, value);
		backwards.put(value, key);
	}
	
	public int size(){
		return forwards.size();
	}
	
	/**
	 * Removes the mapping for a key from this map if it is present.
	 * @return Returns the value to which this map previously associated the key, or null if the map contained no mapping for the key.
	 */
	public V remove(K key){
		
		if (forwards.containsKey(key)){
			V value = forwards.get(key);
			forwards.remove(key);
			backwards.remove(value);
			return value;
		} else {
			return null;
		}
	}
	
	/**
	 * @return Returns the value to which the specified key is mapped, or null if this map contains no mapping for the key.
	 */
	public V get(K key){
		return forwards.get(key);
	}
	
	public boolean containsKey(K key){
		return forwards.containsKey(key);
	}
	
	public boolean containsValue(V value){
		return backwards.containsKey(value);
	}
	
	public Set<K> keySet(){
		return forwards.keySet();
	}
	
	/** 
	 * Removes all of the mappings from this map. The map will be empty after this call returns.
	 */
	public void clear(){
		forwards.clear();
		backwards.clear();
	}
}
