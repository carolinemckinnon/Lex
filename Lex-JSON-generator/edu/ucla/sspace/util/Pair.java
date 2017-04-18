package edu.ucla.sspace.util;

import java.io.Serializable;

































public class Pair<T>
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  public final T x;
  public final T y;
  
  public Pair(T x, T y)
  {
    this.x = x;
    this.y = y;
  }
  




  public boolean equals(Object o)
  {
    if ((o == null) || (!(o instanceof Pair)))
      return false;
    Pair p = (Pair)o;
    return ((x == x) || ((x != null) && (x.equals(x)))) && (
      (y == y) || ((y != null) && (y.equals(y))));
  }
  
  public int hashCode() {
    return (x == null ? 0 : x.hashCode()) ^ 
      (y == null ? 0 : y.hashCode());
  }
  
  public String toString() {
    return "{" + x + ", " + y + "}";
  }
}
