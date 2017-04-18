package edu.ucla.sspace.vector;
















public class ScaledDoubleVector
  implements DoubleVector
{
  private DoubleVector vector;
  













  private double scale;
  














  public ScaledDoubleVector(DoubleVector vector, double scale)
  {
    if (scale == 0.0D) {
      throw new IllegalArgumentException("Cannot scale a vector by 0");
    }
    



    if ((vector instanceof ScaledDoubleVector)) {
      ScaledDoubleVector sdv = (ScaledDoubleVector)vector;
      this.vector = vector;
      this.scale = (scale * scale);
    } else {
      this.vector = vector;
      this.scale = scale;
    }
  }
  


  public double add(int index, double delta)
  {
    return vector.add(index, delta / scale) * scale;
  }
  


  public double get(int index)
  {
    return vector.get(index) * scale;
  }
  


  public DoubleVector getBackingVector()
  {
    return vector;
  }
  



  public double getScalar()
  {
    return scale;
  }
  


  public Double getValue(int index)
  {
    return Double.valueOf(get(index));
  }
  


  public void set(int index, double value)
  {
    vector.set(index, value / scale);
  }
  


  public void set(int index, Number value)
  {
    vector.set(index, value.doubleValue() / scale);
  }
  


  public double magnitude()
  {
    double magnitude = 0.0D;
    int len = length();
    for (int c = 0; c < len; c++) {
      double d = get(c);
      magnitude += d * d;
    }
    return Math.sqrt(magnitude);
  }
  


  public int length()
  {
    return vector.length();
  }
  


  public double[] toArray()
  {
    double[] values = vector.toArray();
    for (int i = 0; i < values.length; i++)
      values[i] *= scale;
    return values;
  }
}
