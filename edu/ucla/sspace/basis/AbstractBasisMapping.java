package edu.ucla.sspace.basis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;




















































public abstract class AbstractBasisMapping<T, K>
  implements BasisMapping<T, K>, Serializable
{
  private static final long serialVersionUID = 1L;
  private Map<K, Integer> mapping;
  private transient List<K> indexToKeyCache;
  private boolean readOnly;
  
  public AbstractBasisMapping()
  {
    mapping = new HashMap();
    readOnly = false;
    indexToKeyCache = new ArrayList();
  }
  


  public K getDimensionDescription(int dimension)
  {
    if ((dimension < 0) || (dimension > mapping.size())) {
      throw new IllegalArgumentException(
        "invalid dimension: " + dimension);
    }
    

    if (indexToKeyCache == null) {
      indexToKeyCache = new ArrayList();
    }
    
    if (mapping.size() > indexToKeyCache.size())
    {
      synchronized (this) {
        indexToKeyCache = new ArrayList(mapping.size());
        for (int i = 0; i < mapping.size(); i++)
          indexToKeyCache.add(null);
        for (Map.Entry<K, Integer> e : mapping.entrySet())
          indexToKeyCache.set(((Integer)e.getValue()).intValue(), e.getKey());
      }
    }
    return indexToKeyCache.get(dimension);
  }
  


  public Set<K> keySet()
  {
    return mapping.keySet();
  }
  




  protected int getDimensionInternal(K key)
  {
    Integer index = (Integer)mapping.get(key);
    if (readOnly) {
      return index == null ? -1 : index.intValue();
    }
    if (index == null) {
      synchronized (this)
      {
        index = (Integer)mapping.get(key);
        


        if (index == null) {
          int i = mapping.size();
          mapping.put(key, Integer.valueOf(i));
          return i;
        }
      }
    }
    return index.intValue();
  }
  


  protected Map<K, Integer> getMapping()
  {
    return mapping;
  }
  


  public int numDimensions()
  {
    return mapping.size();
  }
  


  public void setReadOnly(boolean readOnly)
  {
    this.readOnly = readOnly;
  }
  


  public boolean isReadOnly()
  {
    return readOnly;
  }
}
