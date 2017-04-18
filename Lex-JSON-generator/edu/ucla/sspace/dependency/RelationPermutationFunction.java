package edu.ucla.sspace.dependency;

import edu.ucla.sspace.index.PermutationFunction;
import edu.ucla.sspace.vector.Vector;
import java.io.Serializable;














































public class RelationPermutationFunction<T extends Vector>
  implements DependencyPermutationFunction<T>, Serializable
{
  private static final long serialVersionUID = 1L;
  private final PermutationFunction<T> permFunc;
  
  public RelationPermutationFunction(PermutationFunction<T> permFunc)
  {
    this.permFunc = permFunc;
  }
  


  public T permute(T vector, DependencyPath path)
  {
    int bestRelationScore = 0;
    for (DependencyRelation link : path) {
      int relationScore = getRelationScore(link.relation());
      if (relationScore > bestRelationScore)
        bestRelationScore = relationScore;
    }
    return permFunc.permute(vector, bestRelationScore);
  }
  
  private static int getRelationScore(String relation) {
    if (relation.length() == 0) {
      return 0;
    }
    if (relation.equals("SBJ"))
      return 6;
    if (relation.equals("OBJ"))
      return 5;
    if (relation.equals("NMOD"))
      return 4;
    if (relation.equals("VMOD"))
      return 3;
    if (relation.equals("ADV"))
      return 2;
    return 1;
  }
}
