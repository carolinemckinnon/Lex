package edu.ucla.sspace.util;

import java.util.Map;

public abstract interface BiMap<K, V>
  extends Map<K, V>
{
  public abstract BiMap<V, K> inverse();
}
