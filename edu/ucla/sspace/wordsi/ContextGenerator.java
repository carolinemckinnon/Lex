package edu.ucla.sspace.wordsi;

import edu.ucla.sspace.vector.SparseDoubleVector;
import java.util.Queue;

public abstract interface ContextGenerator
{
  public abstract SparseDoubleVector generateContext(Queue<String> paramQueue1, Queue<String> paramQueue2);
  
  public abstract int getVectorLength();
  
  public abstract void setReadOnly(boolean paramBoolean);
}
