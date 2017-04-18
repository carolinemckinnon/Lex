package edu.ucla.sspace.util.primitive;













public class DoublePair
{
  public final double x;
  










  public final double y;
  











  public DoublePair(double x, double y)
  {
    this.x = x;
    this.y = y;
  }
  




  public boolean equals(Object o)
  {
    if (!(o instanceof DoublePair))
      return false;
    DoublePair p = (DoublePair)o;
    return (x == x) && (y == y);
  }
  
  public int hashCode() {
    long v1 = Double.doubleToLongBits(x);
    long v2 = Double.doubleToLongBits(y);
    return (int)(v1 ^ v2 >>> 32) ^ (int)(v2 ^ v2 >>> 32);
  }
  
  public String toString() {
    return "{" + x + ", " + y + "}";
  }
}
