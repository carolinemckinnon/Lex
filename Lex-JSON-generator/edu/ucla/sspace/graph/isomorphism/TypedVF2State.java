package edu.ucla.sspace.graph.isomorphism;

import edu.ucla.sspace.graph.DirectedGraph;
import edu.ucla.sspace.graph.Edge;
import edu.ucla.sspace.graph.Graph;
import edu.ucla.sspace.graph.Multigraph;
import edu.ucla.sspace.graph.TypedEdge;
import edu.ucla.sspace.util.Pair;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;









































































public class TypedVF2State
  implements State
{
  private Graph<? extends Edge> g1;
  private Graph<? extends Edge> g2;
  private int coreLen;
  private int origCoreLen;
  private int addedNode1;
  int t1bothLen;
  int t2bothLen;
  int t1inLen;
  int t1outLen;
  int t2inLen;
  int t2outLen;
  int[] core1;
  int[] core2;
  int[] in1;
  int[] in2;
  int[] out1;
  int[] out2;
  int[] order;
  private final int n1;
  private final int n2;
  private final boolean checkMultiplexEdges;
  
  public TypedVF2State(Graph<? extends Edge> g1, Graph<? extends Edge> g2)
  {
    this.g1 = g1;
    this.g2 = g2;
    checkMultiplexEdges = 
      (((g1 instanceof Multigraph)) || ((g2 instanceof Multigraph)));
    
    n1 = g1.order();
    n2 = g2.order();
    
    order = null;
    
    coreLen = 0;
    origCoreLen = 0;
    t1bothLen = 0;
    t1inLen = 0;
    t1outLen = 0;
    t2bothLen = 0;
    t2inLen = 0;
    t2outLen = 0;
    
    addedNode1 = -1;
    
    core1 = new int[n1];
    core2 = new int[n2];
    in1 = new int[n1];
    in2 = new int[n2];
    out1 = new int[n1];
    out2 = new int[n2];
    
    Arrays.fill(core1, -1);
    Arrays.fill(core2, -1);
  }
  




  protected TypedVF2State(TypedVF2State copy)
  {
    checkMultiplexEdges = checkMultiplexEdges;
    g1 = g1;
    g2 = g2;
    coreLen = coreLen;
    origCoreLen = origCoreLen;
    t1bothLen = t1bothLen;
    t2bothLen = t2bothLen;
    t1inLen = t1inLen;
    t2inLen = t2inLen;
    t1outLen = t1outLen;
    t2outLen = t2outLen;
    n1 = n1;
    n2 = n2;
    
    addedNode1 = -1;
    



    core1 = core1;
    core2 = core2;
    in1 = in1;
    in2 = in2;
    out1 = out1;
    out2 = out2;
    order = order;
  }
  



  public Graph<? extends Edge> getGraph1()
  {
    return g1;
  }
  


  public Graph<? extends Edge> getGraph2()
  {
    return g2;
  }
  


  public Pair<Integer> nextPair(int prevN1, int prevN2)
  {
    if (prevN1 == -1) {
      prevN1 = 0;
    }
    if (prevN2 == -1) {
      prevN2 = 0;
    } else {
      prevN2++;
    }
    if ((t1bothLen > coreLen) && (t2bothLen > coreLen))
    {
      do
      {
        prevN1++;
        prevN2 = 0;
        if (prevN1 >= n1) break; } while ((core1[prevN1] != -1) || 
        (out1[prevN1] == 0) || 
        (in1[prevN1] == 0));



    }
    else if ((t1outLen > coreLen) && (t2outLen > coreLen))
    {
      do {
        prevN1++;
        prevN2 = 0;
        if (prevN1 >= n1) break;
      } while ((core1[prevN1] != -1) || (out1[prevN1] == 0));



    }
    else if ((t1inLen > coreLen) && (t2inLen > coreLen))
    {
      do {
        prevN1++;
        prevN2 = 0;
        if (prevN1 >= n1) break;
      } while ((core1[prevN1] != -1) || (in1[prevN1] == 0));



    }
    else if ((prevN1 == 0) && (order != null)) {
      i = 0;
      while ((i < n1) && (core1[(prevN1 = order[i])] != -1))
        i++;
      if (i == n1) {
        prevN1 = n1;
      }
    } else {
      while ((prevN1 < n1) && (core1[prevN1] != -1)) { int i;
        prevN1++;
        prevN2 = 0;
      }
    }
    
    if ((t1bothLen > coreLen) && (t2bothLen > coreLen))
    {
      do
      {
        prevN2++;
        if (prevN2 >= n2) break; } while ((core2[prevN2] != -1) || 
        (out2[prevN2] == 0) || 
        (in2[prevN2] == 0));


    }
    else if ((t1outLen > coreLen) && (t2outLen > coreLen)) {
      do
      {
        prevN2++;
        if (prevN2 >= n2) break; } while ((core2[prevN2] != -1) || 
        (out2[prevN2] == 0));


    }
    else if ((t1inLen > coreLen) && (t2inLen > coreLen)) {
      do
      {
        prevN2++;
        if (prevN2 >= n2) break; } while ((core2[prevN2] != -1) || 
        (in2[prevN2] == 0));

    }
    else
    {
      while ((prevN2 < n2) && (core2[prevN2] != -1)) {
        prevN2++;
      }
    }
    

    if ((prevN1 < n1) && (prevN2 < n2)) {
      return new Pair(Integer.valueOf(prevN1), Integer.valueOf(prevN2));
    }
    return null;
  }
  


  protected boolean areCompatibleEdges(int v1, int v2, int v3, int v4)
  {
    if (checkMultiplexEdges) {
      Set<? extends Edge> e1 = g1.getEdges(v1, v2);
      Set<? extends Edge> e2 = g2.getEdges(v3, v4);
      if (e1.size() != e2.size()) {
        return false;
      }
      


      Set<? extends TypedEdge<?>> te1 = e1;
      
      Set<? extends TypedEdge<?>> te2 = e2;
      

      Set<Object> types1 = new HashSet();
      for (TypedEdge<?> e : te1) {
        types1.add(e.edgeType());
      }
      
      for (TypedEdge<?> e : te2) {
        if (!types1.contains(e.edgeType())) {
          return false;
        }
      }
      

      return true;
    }
    return true;
  }
  
  protected boolean areCompatableVertices(int v1, int v2) {
    return true;
  }
  


  public boolean isFeasiblePair(int node1, int node2)
  {
    assert (node1 < n1);
    assert (node2 < n2);
    assert (core1[node1] == -1);
    assert (core2[node2] == -1);
    



    int termout1 = 0;int termout2 = 0;int termin1 = 0;int termin2 = 0;int new1 = 0;int new2 = 0;
    

    for (Iterator localIterator = getSuccessors(g1, Integer.valueOf(node1)).iterator(); localIterator.hasNext();) { int other1 = ((Integer)localIterator.next()).intValue();
      if (core1[other1] != -1) {
        int other2 = core1[other1];
        

        if ((!g2.contains(node2, other2)) || 
          (!areCompatibleEdges(node1, other1, node2, other2))) {
          return false;
        }
      } else {
        if (in1[other1] != 0)
          termin1++;
        if (out1[other1] != 0)
          termout1++;
        if ((in1[other1] == 0) && (out1[other1] == 0)) {
          new1++;
        }
      }
    }
    
    for (localIterator = getPredecessors(g1, Integer.valueOf(node1)).iterator(); localIterator.hasNext();) { int other1 = ((Integer)localIterator.next()).intValue();
      if (core1[other1] != -1) {
        int other2 = core1[other1];
        

        if ((!g2.contains(other2, node2)) || 
          (!areCompatibleEdges(node1, other1, node2, other2))) {
          return false;
        }
      } else {
        if (in1[other1] != 0)
          termin1++;
        if (out1[other1] != 0)
          termout1++;
        if ((in1[other1] == 0) && (out1[other1] == 0)) {
          new1++;
        }
      }
    }
    

    for (localIterator = getSuccessors(g2, Integer.valueOf(node2)).iterator(); localIterator.hasNext();) { int other2 = ((Integer)localIterator.next()).intValue();
      if (core2[other2] != -1) {
        int other1 = core2[other2];
        if (!g1.contains(node1, other1)) {
          return false;
        }
      } else {
        if (in2[other2] != 0)
          termin2++;
        if (out2[other2] != 0)
          termout2++;
        if ((in2[other2] == 0) && (out2[other2] == 0)) {
          new2++;
        }
      }
    }
    
    for (localIterator = getPredecessors(g2, Integer.valueOf(node2)).iterator(); localIterator.hasNext();) { int other2 = ((Integer)localIterator.next()).intValue();
      if (core2[other2] != -1) {
        int other1 = core2[other2];
        if (!g1.contains(other1, node1)) {
          return false;
        }
      }
      else {
        if (in2[other2] != 0)
          termin2++;
        if (out2[other2] != 0)
          termout2++;
        if ((in2[other2] == 0) && (out2[other2] == 0)) {
          new2++;
        }
      }
    }
    return (termin1 == termin2) && (termout1 == termout2) && (new1 == new2);
  }
  


  public void addPair(int node1, int node2)
  {
    assert (node1 < n1);
    assert (node2 < n2);
    assert (coreLen < n1);
    assert (coreLen < n2);
    
    coreLen += 1;
    addedNode1 = node1;
    
    if (in1[node1] == 0) {
      in1[node1] = coreLen;
      t1inLen += 1;
      if (out1[node1] != 0)
        t1bothLen += 1;
    }
    if (out1[node1] == 0) {
      out1[node1] = coreLen;
      t1outLen += 1;
      if (in1[node1] != 0) {
        t1bothLen += 1;
      }
    }
    if (in2[node2] == 0) {
      in2[node2] = coreLen;
      t2inLen += 1;
      if (out2[node2] != 0)
        t2bothLen += 1;
    }
    if (out2[node2] == 0) {
      out2[node2] = coreLen;
      t2outLen += 1;
      if (in2[node2] != 0) {
        t2bothLen += 1;
      }
    }
    core1[node1] = node2;
    core2[node2] = node1;
    
    for (Iterator localIterator = getPredecessors(g1, Integer.valueOf(node1)).iterator(); localIterator.hasNext();) { int other = ((Integer)localIterator.next()).intValue();
      if (in1[other] == 0) {
        in1[other] = coreLen;
        t1inLen += 1;
        if (out1[other] != 0) {
          t1bothLen += 1;
        }
      }
    }
    for (localIterator = getSuccessors(g1, Integer.valueOf(node1)).iterator(); localIterator.hasNext();) { int other = ((Integer)localIterator.next()).intValue();
      if (out1[other] == 0) {
        out1[other] = coreLen;
        t1outLen += 1;
        if (in1[other] != 0) {
          t1bothLen += 1;
        }
      }
    }
    for (localIterator = getPredecessors(g2, Integer.valueOf(node2)).iterator(); localIterator.hasNext();) { int other = ((Integer)localIterator.next()).intValue();
      if (in2[other] == 0) {
        in2[other] = coreLen;
        t2inLen += 1;
        if (out2[other] != 0) {
          t2bothLen += 1;
        }
      }
    }
    for (localIterator = getSuccessors(g2, Integer.valueOf(node2)).iterator(); localIterator.hasNext();) { int other = ((Integer)localIterator.next()).intValue();
      if (out2[other] == 0) {
        out2[other] = coreLen;
        t2outLen += 1;
        if (in2[other] != 0) {
          t2bothLen += 1;
        }
      }
    }
  }
  

  public boolean isGoal()
  {
    return (coreLen == n1) && (coreLen == n2);
  }
  


  public boolean isDead()
  {
    return (n1 != n2) || 
      (t1bothLen != t2bothLen) || 
      (t1outLen != t2outLen) || 
      (t1inLen != t2inLen);
  }
  


  public Map<Integer, Integer> getVertexMapping()
  {
    Map<Integer, Integer> vertexMapping = new HashMap();
    for (int i = 0; i < core1.length; i++) {
      if (core1[i] != -1) {
        vertexMapping.put(Integer.valueOf(i), Integer.valueOf(core1[i]));
      }
    }
    return vertexMapping;
  }
  


  public TypedVF2State copy()
  {
    return new TypedVF2State(this);
  }
  




  public void backTrack()
  {
    assert (addedNode1 != -1);
    
    if (origCoreLen < coreLen)
    {

      if (in1[addedNode1] == coreLen) {
        in1[addedNode1] = 0;
      }
      for (Iterator localIterator = getPredecessors(g1, Integer.valueOf(addedNode1)).iterator(); localIterator.hasNext();) { int other = ((Integer)localIterator.next()).intValue();
        if (in1[other] == coreLen) {
          in1[other] = 0;
        }
      }
      if (out1[addedNode1] == coreLen) {
        out1[addedNode1] = 0;
      }
      for (localIterator = getSuccessors(g1, Integer.valueOf(addedNode1)).iterator(); localIterator.hasNext();) { int other = ((Integer)localIterator.next()).intValue();
        if (out1[other] == coreLen) {
          out1[other] = 0;
        }
      }
      int node2 = core1[addedNode1];
      
      if (in2[node2] == coreLen) {
        in2[node2] = 0;
      }
      for (localIterator = getPredecessors(g2, Integer.valueOf(node2)).iterator(); localIterator.hasNext();) { int other = ((Integer)localIterator.next()).intValue();
        if (in2[other] == coreLen) {
          in2[other] = 0;
        }
      }
      if (out2[node2] == coreLen) {
        out2[node2] = 0;
      }
      for (localIterator = getSuccessors(g2, Integer.valueOf(node2)).iterator(); localIterator.hasNext();) { int other = ((Integer)localIterator.next()).intValue();
        if (out2[other] == coreLen) {
          out2[other] = 0;
        }
      }
      core1[addedNode1] = -1;
      core2[node2] = -1;
      
      coreLen = origCoreLen;
      addedNode1 = -1;
    }
  }
  



  private Set<Integer> getSuccessors(Graph g, Integer vertex)
  {
    if ((g instanceof DirectedGraph)) {
      DirectedGraph<?> dg = (DirectedGraph)g;
      return dg.successors(vertex.intValue());
    }
    
    return Collections.emptySet();
  }
  






  private Set<Integer> getPredecessors(Graph g, Integer vertex)
  {
    if ((g instanceof DirectedGraph)) {
      DirectedGraph<?> dg = (DirectedGraph)g;
      return dg.predecessors(vertex.intValue());
    }
    
    return g.getNeighbors(vertex.intValue());
  }
}
