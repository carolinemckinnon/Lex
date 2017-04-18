package edu.ucla.sspace.dependency;


















public class RelationSumPathWeight
  implements DependencyPathWeight
{
  public RelationSumPathWeight() {}
  

















  public double scorePath(DependencyPath path)
  {
    double score = 0.0D;
    for (DependencyRelation wordRelation : path) {
      if (wordRelation.relation().equals("SBJ")) {
        score += 5.0D;
      } else if (wordRelation.relation().equals("OBJ")) {
        score += 4.0D;
      } else if (wordRelation.relation().equals("OBL")) {
        score += 3.0D;
      } else if (wordRelation.relation().equals("GEN")) {
        score += 2.0D;
      } else
        score += 1.0D;
    }
    return score;
  }
}
