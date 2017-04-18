package edu.ucla.sspace.dependency;

import edu.ucla.sspace.index.PermutationFunction;
import edu.ucla.sspace.vector.Vector;
import java.io.Serializable;














































public class DefaultDependencyPermutationFunction<T extends Vector>
  implements DependencyPermutationFunction<T>, Serializable
{
  private static final long serialVersionUID = 1L;
  private final PermutationFunction<T> permFunc;
  
  public DefaultDependencyPermutationFunction(PermutationFunction<T> permFunc)
  {
    this.permFunc = permFunc;
  }
  


  public T permute(T vector, DependencyPath path)
  {
    return permFunc.permute(vector, path.length());
  }
}
