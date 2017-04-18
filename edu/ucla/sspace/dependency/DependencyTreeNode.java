package edu.ucla.sspace.dependency;

import java.util.List;

public abstract interface DependencyTreeNode
{
  public abstract List<DependencyRelation> neighbors();
  
  public abstract String word();
  
  public abstract String lemma();
  
  public abstract String pos();
  
  public abstract int index();
}
