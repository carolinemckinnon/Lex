package edu.ucla.sspace.util;

public abstract interface ObjectEntry<T>
{
  public abstract int index();
  
  public abstract T value();
}
