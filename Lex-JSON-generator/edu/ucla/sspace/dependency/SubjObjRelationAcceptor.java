package edu.ucla.sspace.dependency;















public class SubjObjRelationAcceptor
  implements DependencyRelationAcceptor
{
  public SubjObjRelationAcceptor() {}
  













  public boolean accept(DependencyRelation relation)
  {
    return (relation.relation().equals("SBJ")) || 
      (relation.relation().equals("OBJ"));
  }
}
