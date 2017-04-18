package edu.ucla.sspace.vector;
















class IntegerVectorView
  extends VectorView<Integer>
  implements IntegerVector
{
  private static final long serialVersionUID = 1L;
  














  protected final IntegerVector intVector;
  















  public IntegerVectorView(IntegerVector v)
  {
    this(v, 0, v.length(), false);
  }
  







  public IntegerVectorView(IntegerVector v, boolean isImmutable)
  {
    this(v, 0, v.length(), isImmutable);
  }
  










  public IntegerVectorView(IntegerVector v, int offset, int length)
  {
    this(v, offset, length, false);
  }
  












  public IntegerVectorView(IntegerVector v, int offset, int length, boolean isImmutable)
  {
    super(v, offset, length, isImmutable);
    intVector = v;
  }
  


  public int add(int index, int delta)
  {
    if (isImmutable)
      throw new UnsupportedOperationException(
        "Cannot modify an immutable vector");
    return intVector.add(getIndex(index), delta);
  }
  


  public void set(int index, int value)
  {
    if (isImmutable)
      throw new UnsupportedOperationException(
        "Cannot modify an immutable vector");
    intVector.set(getIndex(index), value);
  }
  


  public int get(int index)
  {
    return intVector.get(getIndex(index));
  }
  


  public Integer getValue(int index)
  {
    return Integer.valueOf(intVector.get(getIndex(index)));
  }
  



  public double magnitude()
  {
    if (magnitude < 0.0D) {
      double m = 0.0D;
      for (int i = vectorOffset; i < vectorOffset + vectorLength; i++) {
        int j = intVector.get(i);
        m += j * j;
      }
      magnitude = Math.sqrt(m);
    }
    return magnitude;
  }
  


  public int[] toArray()
  {
    if ((vectorOffset > 0) || (vectorLength != vector.length())) {
      int[] r = new int[vectorLength - vectorOffset];
      for (int i = vectorOffset; i < vectorLength; i++)
        r[i] = intVector.get(i);
      return r;
    }
    
    return intVector.toArray();
  }
}
