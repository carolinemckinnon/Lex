package edu.ucla.sspace.util;

import java.io.Serializable;





































public class Triple<T>
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  public final T x;
  public final T y;
  public final T z;
  
  public Triple(T x, T y, T z)
  {
    this.x = x;
    this.y = y;
    this.z = z;
  }
  




  public boolean equals(Object o)
  {
    if ((o == null) || (!(o instanceof Triple)))
      return false;
    Triple p = (Triple)o;
    return ((x == x) || ((x != null) && (x.equals(x)))) && 
      ((y == y) || ((y != null) && (y.equals(y)))) && (
      (z == z) || ((z != null) && (z.equals(z))));
  }
  
  public int hashCode() {
    return (x == null ? 0 : x.hashCode()) ^ 
      (y == null ? 0 : y.hashCode()) ^ 
      (z == null ? 0 : z.hashCode());
  }
  
  public String toString() {
    return "{" + x + ", " + y + ", " + z + "}";
  }
}
