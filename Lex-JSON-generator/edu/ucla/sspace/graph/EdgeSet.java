package edu.ucla.sspace.graph;

import edu.ucla.sspace.util.primitive.IntSet;
import java.util.Set;

public abstract interface EdgeSet<T extends Edge>
  extends Set<T>
{
  public abstract boolean add(T paramT);
  
  public abstract IntSet connected();
  
  public abstract boolean connects(int paramInt);
  
  public abstract EdgeSet copy(IntSet paramIntSet);
  
  public abstract int disconnect(int paramInt);
  
  public abstract Set<T> getEdges(int paramInt);
  
  public abstract int getRoot();
}
