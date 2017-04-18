package edu.ucla.sspace.vector;
















public abstract class AbstractIntegerVector
  extends AbstractVector<Integer>
  implements IntegerVector
{
  public AbstractIntegerVector() {}
  















  public int add(int index, int delta)
  {
    throw new UnsupportedOperationException("set is not supported");
  }
  


  public boolean equals(Object o)
  {
    if ((o instanceof IntegerVector)) {
      IntegerVector v = (IntegerVector)o;
      int len = v.length();
      if (len != length())
        return false;
      for (int i = 0; i < len; i++) {
        if (v.get(i) != get(i))
          return false;
      }
      return true;
    }
    
    return super.equals(o);
  }
  


  public Integer getValue(int index)
  {
    return Integer.valueOf(get(index));
  }
  


  public int hashCode()
  {
    int len = length();
    int hash = 0;
    for (int i = 0; i < len; i++) {
      hash ^= i ^ get(i);
    }
    return hash;
  }
  


  public double magnitude()
  {
    double m = 0.0D;
    int length = length();
    for (int i = 0; i < length; i++) {
      double d = get(i);
      m += d * d;
    }
    return Math.sqrt(m);
  }
  



  public void set(int index, int value)
  {
    throw new UnsupportedOperationException("set is not supported");
  }
  


  public void set(int index, Number value)
  {
    set(index, value.intValue());
  }
  


  public int[] toArray()
  {
    int[] arr = new int[length()];
    for (int i = 0; i < arr.length; i++)
      arr[i] = get(i);
    return arr;
  }
  


  public String toString()
  {
    int length = length();
    StringBuilder sb = new StringBuilder(length * 3);
    sb.append('[');
    for (int i = 0; i < length; i++) {
      sb.append(get(i));
      if (i + 1 < length)
        sb.append(", ");
    }
    sb.append(']');
    return sb.toString();
  }
}
