package edu.ucla.sspace.wordsi;

import edu.ucla.sspace.dependency.DependencyTreeNode;
import edu.ucla.sspace.vector.SparseDoubleVector;

public abstract interface DependencyContextGenerator
{
  public abstract SparseDoubleVector generateContext(DependencyTreeNode[] paramArrayOfDependencyTreeNode, int paramInt);
  
  public abstract int getVectorLength();
  
  public abstract void setReadOnly(boolean paramBoolean);
}
