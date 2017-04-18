package edu.ucla.sspace.dependency;

public abstract interface DependencyTreeTransform
{
  public abstract DependencyTreeNode[] transform(DependencyTreeNode[] paramArrayOfDependencyTreeNode);
}
