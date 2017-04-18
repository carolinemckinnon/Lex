package edu.ucla.sspace.vector;












public class MaskedDoubleVectorView
  extends VectorView<Double>
  implements DoubleVector
{
  private static final long serialVersionUID = 1L;
  









  private final DoubleVector doubleVector;
  









  private final int[] columnMask;
  









  protected boolean updated;
  










  public MaskedDoubleVectorView(DoubleVector v, int[] columnMask)
  {
    super(v, 0, columnMask.length, false);
    doubleVector = v;
    this.columnMask = columnMask;
    updated = false;
  }
  



  protected int getIndex(int index)
  {
    if ((index < 0) || (index > columnMask.length)) {
      throw new IllegalArgumentException("The given index is not within the bounds of the masked vector");
    }
    return columnMask[index];
  }
  


  public double add(int index, double delta)
  {
    updated = true;
    int newIndex = getIndex(index);
    return newIndex == -1 ? 0.0D : doubleVector.add(newIndex, delta);
  }
  


  public void set(int index, double value)
  {
    updated = true;
    int newIndex = getIndex(index);
    if (newIndex == -1)
      return;
    doubleVector.set(newIndex, value);
  }
  


  public double get(int index)
  {
    int newIndex = getIndex(index);
    return newIndex == -1 ? 0.0D : doubleVector.get(newIndex);
  }
  


  public Double getValue(int index)
  {
    return Double.valueOf(get(index));
  }
  


  public double[] toArray()
  {
    double[] r = new double[length()];
    for (int i = 0; i < length(); i++)
      r[i] = get(i);
    return r;
  }
  


  public int length()
  {
    return columnMask.length;
  }
  




  public DoubleVector getOriginalVector()
  {
    return doubleVector;
  }
}
