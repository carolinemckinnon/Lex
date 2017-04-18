package edu.ucla.sspace.graph;

public abstract interface Edge
{
  public abstract <T extends Edge> T clone(int paramInt1, int paramInt2);
  
  public abstract boolean equals(Object paramObject);
  
  public abstract <T extends Edge> T flip();
  
  public abstract int from();
  
  public abstract int to();
}
