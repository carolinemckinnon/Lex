package edu.ucla.sspace.dependency;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;































































public class DependencyIterator
  implements Iterator<DependencyPath>
{
  private final int maxPathLength;
  protected final Queue<SimpleDependencyPath> frontier;
  private DependencyRelationAcceptor acceptor;
  
  public DependencyIterator(DependencyTreeNode startNode, DependencyRelationAcceptor acceptor, int maxPathLength)
  {
    if (maxPathLength < 1)
      throw new IllegalArgumentException(
        "Must specify a path length greater than or equal to 1");
    this.maxPathLength = maxPathLength;
    this.acceptor = acceptor;
    frontier = new ArrayDeque();
    

    for (DependencyRelation rel : startNode.neighbors())
    {



      if (acceptor.accept(rel)) {
        frontier.offer(new SimpleDependencyPath(
          rel, rel.headNode().equals(startNode)));
      }
    }
  }
  




  void advance(SimpleDependencyPath path)
  {
    if (path.length() >= maxPathLength) {
      return;
    }
    
    DependencyRelation lastRelation = path.lastRelation();
    DependencyTreeNode last = path.last();
    



    for (DependencyRelation rel : last.neighbors())
    {
      if ((!lastRelation.equals(rel)) && (acceptor.accept(rel)))
      {
        SimpleDependencyPath extended = path.extend(rel);
        frontier.offer(extended);
      }
    }
  }
  

  public boolean hasNext()
  {
    return !frontier.isEmpty();
  }
  



  public DependencyPath next()
  {
    SimpleDependencyPath p = (SimpleDependencyPath)frontier.remove();
    
    advance(p);
    return p;
  }
  


  public void remove()
  {
    throw new UnsupportedOperationException("Removal is not possible");
  }
}
