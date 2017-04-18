package edu.ucla.sspace.basis;

import java.util.Set;

public abstract interface BasisMapping<T, E>
{
  public abstract int getDimension(T paramT);
  
  public abstract E getDimensionDescription(int paramInt);
  
  public abstract Set<E> keySet();
  
  public abstract int numDimensions();
  
  public abstract void setReadOnly(boolean paramBoolean);
  
  public abstract boolean isReadOnly();
}
