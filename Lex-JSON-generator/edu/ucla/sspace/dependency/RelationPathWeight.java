package edu.ucla.sspace.dependency;


















public class RelationPathWeight
  implements DependencyPathWeight
{
  public RelationPathWeight() {}
  

















  public double scorePath(DependencyPath path)
  {
    double score = 1.0D;
    for (DependencyRelation wordRelation : path) {
      if (wordRelation.relation().equals("SBJ"))
        return 5.0D;
      if ((wordRelation.relation().equals("OBJ")) && (score < 4.0D))
        score = 4.0D;
      if ((wordRelation.relation().equals("OBL")) && (score < 3.0D))
        score = 3.0D;
      if ((wordRelation.relation().equals("GEN")) && (score < 2.0D))
        score = 2.0D;
    }
    return score;
  }
}
