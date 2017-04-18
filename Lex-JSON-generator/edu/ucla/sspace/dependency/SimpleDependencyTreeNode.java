package edu.ucla.sspace.dependency;

import java.util.LinkedList;
import java.util.List;
















































public class SimpleDependencyTreeNode
  implements DependencyTreeNode
{
  private String word;
  private String pos;
  private String lemma;
  private int index;
  private List<DependencyRelation> neighbors;
  
  public SimpleDependencyTreeNode(String word, String pos, int index)
  {
    this(word, pos, word, index);
  }
  
  public SimpleDependencyTreeNode(String word, String pos, String lemma, int index)
  {
    this(word, pos, lemma, index, new LinkedList());
  }
  






  public SimpleDependencyTreeNode(String word, String pos, int index, List<DependencyRelation> neighbors)
  {
    this(word, pos, word, index, neighbors);
  }
  







  public SimpleDependencyTreeNode(String word, String pos, String lemma, int index, List<DependencyRelation> neighbors)
  {
    this.word = word;
    this.pos = pos;
    this.lemma = lemma;
    this.index = index;
    this.neighbors = neighbors;
  }
  








  public void addNeighbor(DependencyRelation relation)
  {
    neighbors.add(relation);
  }
  
  public boolean equals(Object o) {
    if ((o instanceof SimpleDependencyTreeNode)) {
      SimpleDependencyTreeNode n = (SimpleDependencyTreeNode)o;
      return (pos.equals(pos)) && 
        (word.equals(word)) && 
        (lemma.equals(lemma));
    }
    



    return false;
  }
  



  public int hashCode()
  {
    return pos.hashCode() ^ word.hashCode();
  }
  


  public List<DependencyRelation> neighbors()
  {
    return neighbors;
  }
  


  public String pos()
  {
    return pos;
  }
  


  public String toString()
  {
    return word + ":" + pos;
  }
  



  void setWord(String word)
  {
    this.word = word;
  }
  


  public String word()
  {
    return word;
  }
  


  public String lemma()
  {
    return lemma;
  }
  


  public int index()
  {
    return index;
  }
}
