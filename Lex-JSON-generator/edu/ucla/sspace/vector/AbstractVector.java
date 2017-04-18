package edu.ucla.sspace.vector;
















public abstract class AbstractVector<T extends Number>
  implements Vector<T>
{
  public AbstractVector() {}
  















  public double add(int index, double delta)
  {
    throw new UnsupportedOperationException("Modification is unsupported");
  }
  


  public boolean equals(Object o)
  {
    if ((o instanceof Vector)) {
      Vector v = (Vector)o;
      int len = v.length();
      if (len != length())
        return false;
      for (int i = 0; i < len; i++) {
        if (!v.getValue(i).equals(getValue(i)))
          return false;
      }
      return true;
    }
    return false;
  }
  


  public int hashCode()
  {
    int len = length();
    int hash = 0;
    
    for (int i = 0; i < len; i++) {
      hash ^= i ^ getValue(i).intValue();
    }
    return hash;
  }
  


  public double magnitude()
  {
    double m = 0.0D;
    int length = length();
    for (int i = 0; i < length; i++) {
      double d = getValue(i).doubleValue();
      m += d * d;
    }
    return Math.sqrt(m);
  }
  


  public void set(int index, Number value)
  {
    throw new UnsupportedOperationException("Modification is unsupported");
  }
  


  public String toString()
  {
    int length = length();
    StringBuilder sb = new StringBuilder(length * 3);
    sb.append('[');
    for (int i = 0; i < length; i++) {
      sb.append(getValue(i));
      if (i + 1 < length)
        sb.append(", ");
    }
    sb.append(']');
    return sb.toString();
  }
}
