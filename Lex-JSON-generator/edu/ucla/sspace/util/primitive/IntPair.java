package edu.ucla.sspace.util.primitive;













public class IntPair
{
  public final int x;
  










  public final int y;
  











  public IntPair(int x, int y)
  {
    this.x = x;
    this.y = y;
  }
  




  public boolean equals(Object o)
  {
    if (!(o instanceof IntPair))
      return false;
    IntPair p = (IntPair)o;
    return (x == x) && (y == y);
  }
  
  public int hashCode() {
    return x ^ y;
  }
  
  public String toString() {
    return "{" + x + ", " + y + "}";
  }
}
