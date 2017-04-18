package edu.ucla.sspace.dependency;











public class SimpleDependencyRelation
  implements DependencyRelation
{
  private final DependencyTreeNode dependent;
  









  private final DependencyTreeNode headNode;
  









  private final String relation;
  










  public SimpleDependencyRelation(DependencyTreeNode headNode, String relation, DependencyTreeNode dependent)
  {
    this.headNode = headNode;
    this.relation = relation;
    this.dependent = dependent;
  }
  


  public DependencyTreeNode dependentNode()
  {
    return dependent;
  }
  
  public boolean equals(Object o) {
    if ((o instanceof SimpleDependencyRelation)) {
      SimpleDependencyRelation r = (SimpleDependencyRelation)o;
      return (headNode.equals(headNode)) && 
        (relation.equals(relation)) && 
        (dependent.equals(dependent));
    }
    return false;
  }
  
  public int hashCode() {
    return relation.hashCode() ^ headNode.hashCode() ^ dependent.hashCode();
  }
  


  public DependencyTreeNode headNode()
  {
    return headNode;
  }
  


  public String relation()
  {
    return relation;
  }
  
  public String toString() {
    return headNode + "<-" + relation + "--" + dependent;
  }
}
