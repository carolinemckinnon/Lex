package edu.ucla.sspace.util;

import java.io.Serializable;


























public class Duple<T, U>
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  public final T x;
  public final U y;
  
  public Duple(T x, U y)
  {
    this.x = x;
    this.y = y;
  }
  
  public boolean equals(Object o) {
    if ((o == null) || (!(o instanceof Duple)))
      return false;
    Duple d = (Duple)o;
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
