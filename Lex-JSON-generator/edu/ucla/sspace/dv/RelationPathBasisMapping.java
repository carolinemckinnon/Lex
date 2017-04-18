package edu.ucla.sspace.dv;

import edu.ucla.sspace.dependency.DependencyPath;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;











































public class RelationPathBasisMapping
  implements DependencyPathBasisMapping
{
  private Map<PathSignature, Integer> pathToIndex;
  private String[] indexToPathCache;
  private boolean readOnly;
  
  public RelationPathBasisMapping()
  {
    pathToIndex = new HashMap();
    indexToPathCache = new String[0];
    readOnly = false;
  }
  







  public int getDimension(DependencyPath path)
  {
    return getDimension(new PathSignature(path));
  }
  





  private int getDimension(PathSignature path)
  {
    Integer index = (Integer)pathToIndex.get(path);
    if ((index == null) && (!readOnly)) {
      synchronized (this)
      {
        index = (Integer)pathToIndex.get(path);
        

        if (index == null) {
          int i = pathToIndex.size();
          pathToIndex.put(path, Integer.valueOf(i));
          return i;
        }
      }
    }
    return index.intValue();
  }
  



  public String getDimensionDescription(int dimension)
  {
    if ((dimension < 0) || (dimension > pathToIndex.size())) {
      throw new IllegalArgumentException(
        "invalid dimension: " + dimension);
    }
    if (pathToIndex.size() > indexToPathCache.length)
    {
      synchronized (this) {
        indexToPathCache = 
          new String[pathToIndex.size()];
        
        Iterator localIterator = pathToIndex.entrySet().iterator();
        while (localIterator.hasNext()) {
          Map.Entry<PathSignature, Integer> e = (Map.Entry)localIterator.next();
          indexToPathCache[((Integer)e.getValue()).intValue()] = ((PathSignature)e.getKey()).toString();
        }
      } }
    return indexToPathCache[dimension];
  }
  


  public int numDimensions()
  {
    return pathToIndex.size();
  }
  


  public void setReadOnly(boolean readOnly)
  {
    this.readOnly = readOnly;
  }
  


  public boolean isReadOnly()
  {
    return readOnly;
  }
  


  public Set<String> keySet()
  {
    return null;
  }
  





  private static class PathSignature
  {
    private final String[] relations;
    




    private int hashCode = 0;
    
    public PathSignature(DependencyPath path) {
      relations = new String[path.length() - 1];
      for (int i = 0; i < path.length(); i++) {
        if (i + 1 < path.length())
          relations[i] = path.getRelation(i);
      }
    }
    
    public boolean equals(Object o) {
      if ((o instanceof PathSignature)) {
        PathSignature p = (PathSignature)o;
        return (hashCode() == p.hashCode()) && 
          (Arrays.equals(relations, relations));
      }
      return false;
    }
    
    public int hashCode() {
      if (hashCode == 0)
        hashCode = Arrays.hashCode(relations);
      return hashCode;
    }
    
    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append(relations[0]);
      for (int i = 0; i < relations.length; i++)
        sb.append("-").append(relations[i]);
      return sb.toString();
    }
  }
}
