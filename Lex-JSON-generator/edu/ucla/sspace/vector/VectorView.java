package edu.ucla.sspace.vector;

import java.io.Serializable;






























































class VectorView<T extends Number>
  implements Vector<T>, Serializable
{
  private static final long serialVersionUID = 1L;
  protected final boolean isImmutable;
  protected final Vector vector;
  protected final int vectorLength;
  protected final int vectorOffset;
  protected double magnitude;
  
  public VectorView(Vector v)
  {
    this(v, 0, v.length(), false);
  }
  






  public VectorView(Vector v, boolean isImmutable)
  {
    this(v, 0, v.length(), isImmutable);
  }
  













  public VectorView(Vector v, int offset, int length)
  {
    this(v, 0, v.length(), false);
  }
  















  public VectorView(Vector v, int offset, int length, boolean isImmutable)
  {
    if (v == null) {
      throw new NullPointerException(
        "Cannot create a view of a null vector");
    }
    vector = v;
    vectorOffset = offset;
    vectorLength = length;
    this.isImmutable = isImmutable;
    


    if (length < 0)
      throw new IllegalArgumentException("Cannot have negative length");
    if (offset < 0)
      throw new IllegalArgumentException("Offset cannot be negative");
    if (offset + length > v.length())
      throw new IllegalArgumentException(
        "Cannot create view larger than vector");
    magnitude = -1.0D;
  }
  


  public void set(int index, Number value)
  {
    if (isImmutable)
      throw new UnsupportedOperationException(
        "Cannot modify an immutable vector");
    vector.set(getIndex(index), value);
    magnitude = -1.0D;
  }
  


  protected int getIndex(int index)
  {
    if ((index < 0) || (index > vectorLength)) {
      throw new IllegalArgumentException("Invalid index: " + index);
    }
    return index + vectorOffset;
  }
  


  public Number getValue(int index)
  {
    return vector.getValue(getIndex(index));
  }
  


  public int length()
  {
    return vectorLength;
  }
  




  public double magnitude()
  {
    if (magnitude < 0.0D) {
      double m = 0.0D;
      for (int i = vectorOffset; i < vectorOffset + vectorLength; i++) {
        Number j = vector.getValue(i);
        m += j.doubleValue() * j.doubleValue();
      }
      magnitude = Math.sqrt(m);
    }
    return magnitude;
  }
  
  public String toString() {
    return vector.toString();
  }
}
