package edu.ucla.sspace.common;

import java.util.Set;

public abstract interface Filterable
{
  public abstract void setSemanticFilter(Set<String> paramSet);
}
