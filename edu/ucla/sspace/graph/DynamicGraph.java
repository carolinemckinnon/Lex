package edu.ucla.sspace.graph;

import edu.ucla.sspace.util.primitive.IntSet;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

public abstract interface DynamicGraph
  extends Graph
{
  public abstract boolean addVertex(int paramInt);
  
  public abstract boolean addVertex(int paramInt, Calendar paramCalendar1, Calendar paramCalendar2);
  
  public abstract boolean addVertex(int paramInt, Date paramDate1, Date paramDate2);
  
  public abstract boolean addVertex(int paramInt, long paramLong1, long paramLong2);
  
  public abstract boolean addEdge(int paramInt1, int paramInt2);
  
  public abstract boolean addEdge(int paramInt1, int paramInt2, Calendar paramCalendar1, Calendar paramCalendar2);
  
  public abstract boolean addEdge(int paramInt1, int paramInt2, Date paramDate1, Date paramDate2);
  
  public abstract boolean addEdge(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
  
  public abstract void clear();
  
  public abstract void clearEdges();
  
  public abstract boolean containsEdge(int paramInt1, int paramInt2, Calendar paramCalendar);
  
  public abstract boolean containsEdge(int paramInt1, int paramInt2, Date paramDate);
  
  public abstract boolean containsEdge(int paramInt1, int paramInt2, long paramLong);
  
  public abstract Set<? extends TemporalEdge> edges();
  
  public abstract Set<? extends TemporalEdge> getAdjacencyList(int paramInt);
  
  public abstract TemporalEdge getEdge(int paramInt1, int paramInt2);
  
  public abstract Graph subgraph(IntSet paramIntSet);
  
  public abstract Graph subgraph(IntSet paramIntSet, Calendar paramCalendar);
  
  public abstract Graph subgraph(IntSet paramIntSet, Date paramDate);
  
  public abstract Graph subgraph(IntSet paramIntSet, long paramLong);
  
  public abstract Graph subgraph(IntSet paramIntSet, Calendar paramCalendar1, Calendar paramCalendar2);
  
  public abstract Graph subgraph(IntSet paramIntSet, Date paramDate1, Date paramDate2);
  
  public abstract Graph subgraph(IntSet paramIntSet, long paramLong1, long paramLong2);
}
