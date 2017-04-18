package edu.ucla.sspace.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;






































public class HashBiMap<K, V>
  implements BiMap<K, V>, Serializable
{
  private static final long serialVersionUID = 1L;
  private Map<K, V> originalMap;
  private Map<V, K> reverseMap;
  
  public HashBiMap()
  {
    this(new HashMap(), new HashMap());
  }
  




  public HashBiMap(Map<K, V> map)
  {
    originalMap = map;
    
    reverseMap = new HashMap();
    for (Map.Entry<K, V> entry : map.entrySet())
      reverseMap.put(entry.getValue(), entry.getKey());
    if (reverseMap.size() != originalMap.size()) {
      throw new IllegalArgumentException(
        "map is not bijective; some mappings have been lost");
    }
  }
  



  private HashBiMap(Map<K, V> map, Map<V, K> reverse)
  {
    originalMap = map;
    reverseMap = reverse;
  }
  


  public void clear()
  {
    originalMap.clear();
    reverseMap.clear();
  }
  


  public boolean containsKey(Object key)
  {
    return originalMap.containsKey(key);
  }
  


  public boolean containsValue(Object key)
  {
    return reverseMap.containsKey(key);
  }
  


  public Set<Map.Entry<K, V>> entrySet()
  {
    return originalMap.entrySet();
  }
  


  public boolean equals(Object o)
  {
    return originalMap.equals(o);
  }
  


  public V get(Object o)
  {
    return originalMap.get(o);
  }
  


  public int hashCode()
  {
    return originalMap.hashCode();
  }
  


  public BiMap<V, K> inverse()
  {
    return new HashBiMap(reverseMap, originalMap);
  }
  


  public boolean isEmpty()
  {
    return originalMap.isEmpty();
  }
  


  public Set<K> keySet()
  {
    return originalMap.keySet();
  }
  


  public V put(K key, V value)
  {
    reverseMap.put(value, key);
    return originalMap.put(key, value);
  }
  


  public void putAll(Map<? extends K, ? extends V> m)
  {
    originalMap.putAll(m);
    for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
      reverseMap.put(e.getValue(), e.getKey());
    }
  }
  

  public V remove(Object key)
  {
    V removed = originalMap.remove(key);
    if (removed != null)
      reverseMap.remove(removed);
    return removed;
  }
  


  public int size()
  {
    return originalMap.size();
  }
  


  public String toString()
  {
    return originalMap.toString();
  }
  


  public Collection<V> values()
  {
    return originalMap.values();
  }
  
  private void writeObject(ObjectOutputStream out) throws IOException {
    out.writeInt(originalMap.size());
    for (Map.Entry<K, V> e : originalMap.entrySet()) {
      out.writeObject(e.getKey());
      out.writeObject(e.getValue());
    }
  }
  
  private void readObject(ObjectInputStream in)
    throws IOException, ClassNotFoundException
  {
    int size = in.readInt();
    originalMap = new HashMap(size);
    reverseMap = new HashMap(size);
    for (int i = 0; i < size; i++) {
      K k = in.readObject();
      V v = in.readObject();
      originalMap.put(k, v);
      reverseMap.put(v, k);
    }
  }
}
