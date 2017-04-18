package edu.ucla.sspace.dependency;

public abstract interface DependencyRelation
{
  public abstract DependencyTreeNode dependentNode();
  
  public abstract DependencyTreeNode headNode();
  
  public abstract String relation();
}
