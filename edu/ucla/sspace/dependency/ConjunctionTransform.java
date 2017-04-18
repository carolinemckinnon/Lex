package edu.ucla.sspace.dependency;

import java.util.List;



































public class ConjunctionTransform
  implements DependencyTreeTransform
{
  public ConjunctionTransform() {}
  
  public DependencyTreeNode[] transform(DependencyTreeNode[] dependencyTree)
  {
    for (DependencyTreeNode treeNode : dependencyTree)
    {



      boolean hasConj = false;
      DependencyRelation parentLink = null;
      for (DependencyRelation link : treeNode.neighbors()) {
        if ((link.relation().equals("conj")) && 
          (link.headNode().equals(treeNode)))
          hasConj = true;
        if (!link.headNode().equals(treeNode)) {
          parentLink = link;
        }
      }
      

      if ((hasConj) && (parentLink != null))
      {

        for (DependencyRelation link : treeNode.neighbors())
        {


          if ((link.relation().equals("conj")) && 
            (link.headNode().equals(treeNode))) {
            DependencyRelation newLink = new SimpleDependencyRelation(
              parentLink.headNode(), parentLink.relation(), 
              link.dependentNode());
            
            parentLink.headNode().neighbors().add(newLink);
          }
        }
      }
    }
    return dependencyTree;
  }
}
