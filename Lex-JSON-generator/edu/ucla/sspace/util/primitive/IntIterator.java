package edu.ucla.sspace.util.primitive;

import java.util.Iterator;

public abstract interface IntIterator
  extends Iterator<Integer>
{
  public abstract int nextInt();
}
