package edu.ucla.sspace.util;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;








































public class GeneratorMap<T>
  extends AbstractMap<String, T>
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private final Generator<T> generator;
  private final Map<String, T> termToItem;
  
  public GeneratorMap(Generator<T> generator)
  {
    this(generator, new ConcurrentHashMap());
  }
  



  public GeneratorMap(Generator<T> generator, Map<String, T> map)
  {
    termToItem = map;
    this.generator = generator;
  }
  


  public void clear()
  {
    termToItem.clear();
  }
  


  public boolean containsKey(Object key)
  {
    return termToItem.containsKey(key);
  }
  


  public boolean containsValue(Object value)
  {
    return termToItem.containsValue(value);
  }
  


  public Set<Map.Entry<String, T>> entrySet()
  {
    return termToItem.entrySet();
  }
  


  public boolean equals(Object o)
  {
    return termToItem.equals(o);
  }
  








  public T get(Object term)
  {
    T v = termToItem.get(term);
    if (v == null) {
      synchronized (this)
      {

        v = termToItem.get(term);
        if (v == null)
        {
          v = generator.generate();
          termToItem.put((String)term, v);
        }
      }
    }
    return v;
  }
  


  public int hashCode()
  {
    return termToItem.hashCode();
  }
  


  public boolean isEmpty()
  {
    return termToItem.isEmpty();
  }
  


  public Set<String> keySet()
  {
    return termToItem.keySet();
  }
  


  public T put(String key, T vector)
  {
    return termToItem.put(key, vector);
  }
  












  public int size()
  {
    return termToItem.size();
  }
  


  public Collection<T> values()
  {
    return termToItem.values();
  }
  


  public T remove(Object key)
  {
    return termToItem.remove(key);
  }
}
