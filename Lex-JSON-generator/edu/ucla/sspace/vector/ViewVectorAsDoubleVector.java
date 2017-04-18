package edu.ucla.sspace.vector;



























class ViewVectorAsDoubleVector
  extends VectorView<Double>
  implements DoubleVector
{
  private static final long serialVersionUID = 1L;
  

























  public ViewVectorAsDoubleVector(Vector v)
  {
    super(v);
  }
  







  public ViewVectorAsDoubleVector(Vector v, boolean isImmutable)
  {
    super(v, isImmutable);
  }
  









  public ViewVectorAsDoubleVector(Vector v, int offset, int length)
  {
    super(v, offset, length);
  }
  












  public ViewVectorAsDoubleVector(Vector v, int offset, int length, boolean isImmutable)
  {
    super(v, offset, length, isImmutable);
  }
  


  public double add(int index, double delta)
  {
    if (isImmutable)
      throw new UnsupportedOperationException(
        "Cannot modify an immutable vector");
    int mapped = getIndex(index);
    double value = vector.getValue(mapped).doubleValue();
    vector.set(mapped, Double.valueOf(value + delta));
    return value + delta;
  }
  


  public void set(int index, double value)
  {
    if (isImmutable)
      throw new UnsupportedOperationException(
        "Cannot modify an immutable vector");
    vector.set(getIndex(index), Double.valueOf(value));
  }
  


  public double get(int index)
  {
    return vector.getValue(getIndex(index)).doubleValue();
  }
  


  public Double getValue(int index)
  {
    return Double.valueOf(vector.getValue(getIndex(index)).doubleValue());
  }
  


  public double[] toArray()
  {
    double[] r = new double[vectorLength - vectorOffset];
    for (int i = vectorOffset; i < vectorLength; i++)
      r[i] = vector.getValue(i).doubleValue();
    return r;
  }
}
