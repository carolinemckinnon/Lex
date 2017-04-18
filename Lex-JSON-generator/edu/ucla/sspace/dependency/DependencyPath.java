package edu.ucla.sspace.dependency;

import java.util.Iterator;

public abstract interface DependencyPath
  extends Iterable<DependencyRelation>
{
  public abstract DependencyTreeNode first();
  
  public abstract DependencyRelation firstRelation();
  
  public abstract DependencyTreeNode getNode(int paramInt);
  
  public abstract String getRelation(int paramInt);
  
  public abstract Iterator<DependencyRelation> iterator();
  
  public abstract DependencyTreeNode last();
  
  public abstract DependencyRelation lastRelation();
  
  public abstract int length();
}
