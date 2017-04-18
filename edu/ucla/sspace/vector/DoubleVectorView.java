package edu.ucla.sspace.vector;
















class DoubleVectorView
  extends VectorView<Double>
  implements DoubleVector
{
  private static final long serialVersionUID = 1L;
  














  protected final DoubleVector doubleVector;
  















  public DoubleVectorView(DoubleVector v)
  {
    this(v, 0, v.length(), false);
  }
  







  public DoubleVectorView(DoubleVector v, boolean isImmutable)
  {
    this(v, 0, v.length(), isImmutable);
  }
  










  public DoubleVectorView(DoubleVector v, int offset, int length)
  {
    this(v, offset, length, false);
  }
  












  public DoubleVectorView(DoubleVector v, int offset, int length, boolean isImmutable)
  {
    super(v, offset, length, isImmutable);
    doubleVector = v;
  }
  


  public double add(int index, double delta)
  {
    if (isImmutable)
      throw new UnsupportedOperationException(
        "Cannot modify an immutable vector");
    return doubleVector.add(getIndex(index), delta);
  }
  


  public void set(int index, double value)
  {
    if (isImmutable)
      throw new UnsupportedOperationException(
        "Cannot modify an immutable vector");
    doubleVector.set(getIndex(index), value);
  }
  


  public double get(int index)
  {
    return doubleVector.get(getIndex(index));
  }
  


  public Double getValue(int index)
  {
    return Double.valueOf(doubleVector.get(getIndex(index)));
  }
  



  public double magnitude()
  {
    if (magnitude < 0.0D) {
      double m = 0.0D;
      for (int i = vectorOffset; i < vectorOffset + vectorLength; i++) {
        double d = doubleVector.get(i);
        m += d * d;
      }
      magnitude = Math.sqrt(m);
    }
    return magnitude;
  }
  


  public double[] toArray()
  {
    if ((vectorOffset > 0) || (vectorLength != vector.length())) {
      double[] r = new double[vectorLength - vectorOffset];
      for (int i = vectorOffset; i < vectorLength; i++)
        r[i] = doubleVector.get(i);
      return r;
    }
    
    return doubleVector.toArray();
  }
  




  public DoubleVector getOriginalVector()
  {
    return doubleVector;
  }
}
