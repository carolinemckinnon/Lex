package edu.ucla.sspace.dependency;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;




















































public class BreadthFirstPathIterator
  implements Iterator<DependencyPath>
{
  protected final Queue<SimpleDependencyPath> frontier;
  private final int maxPathLength;
  
  public BreadthFirstPathIterator(DependencyTreeNode startNode)
  {
    this(startNode, Integer.MAX_VALUE);
  }
  










  public BreadthFirstPathIterator(DependencyTreeNode startNode, int maxPathLength)
  {
    if (maxPathLength < 1)
      throw new IllegalArgumentException(
        "Must specify a path length greater than or equal to 1");
    this.maxPathLength = maxPathLength;
    frontier = new ArrayDeque();
    

    for (DependencyRelation rel : startNode.neighbors())
    {



      frontier.offer(new SimpleDependencyPath(
        rel, rel.headNode().equals(startNode)));
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
      if (!lastRelation.equals(rel))
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
