package edu.ucla.sspace.vector;

import java.io.Serializable;




































class IntAsDoubleVector
  extends VectorView<Double>
  implements DoubleVector, Serializable
{
  private static final long serialVersionUID = 1L;
  private final IntegerVector intVector;
  
  public IntAsDoubleVector(IntegerVector v)
  {
    this(v, 0, v.length(), false);
  }
  







  public IntAsDoubleVector(IntegerVector v, boolean isImmutable)
  {
    this(v, 0, v.length(), isImmutable);
  }
  









  public IntAsDoubleVector(IntegerVector v, int offset, int length)
  {
    this(v, offset, length, false);
  }
  












  public IntAsDoubleVector(IntegerVector v, int offset, int length, boolean isImmutable)
  {
    super(v, offset, length, isImmutable);
    intVector = v;
  }
  


  public double add(int index, double delta)
  {
    if (isImmutable)
      throw new UnsupportedOperationException(
        "Cannot modify an immutable vector");
    return intVector.add(getIndex(index), (int)delta);
  }
  


  public void set(int index, double value)
  {
    if (isImmutable)
      throw new UnsupportedOperationException(
        "Cannot modify an immutable vector");
    intVector.set(getIndex(index), (int)value);
  }
  


  public double get(int index)
  {
    return intVector.get(getIndex(index));
  }
  


  public Double getValue(int index)
  {
    return Double.valueOf(get(index));
  }
  


  public double[] toArray()
  {
    double[] r = new double[vectorLength - vectorOffset];
    for (int i = vectorOffset; i < vectorLength; i++)
      r[i] = intVector.get(i);
    return r;
  }
}
