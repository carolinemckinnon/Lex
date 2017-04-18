package jnt.FFT;











public abstract class ComplexDoubleFFT
{
  int n;
  










  public ComplexDoubleFFT(int n)
  {
    if (n <= 0)
      throw new IllegalArgumentException("The transform length must be >=0 : " + n);
    this.n = n;
  }
  


  public ComplexDoubleFFT getInstance(int n) { return new ComplexDoubleFFT_Mixed(n); }
  
  protected void checkData(double[] data, int i0, int stride) {
    if (i0 < 0)
      throw new IllegalArgumentException("The offset must be >=0 : " + i0);
    if (stride < 2)
      throw new IllegalArgumentException("The stride must be >=2 : " + stride);
    if (i0 + stride * (n - 1) + 2 > data.length) {
      throw new IllegalArgumentException("The data array is too small for " + n + ":" + 
        "i0=" + i0 + " stride=" + stride + 
        " data.length=" + data.length);
    }
  }
  
  public void transform(double[] data)
  {
    transform(data, 0, 2);
  }
  




  public abstract void transform(double[] paramArrayOfDouble, int paramInt1, int paramInt2);
  



  public double[] toWraparoundOrder(double[] data)
  {
    return data;
  }
  


  public double[] toWraparoundOrder(double[] data, int i0, int stride)
  {
    if ((i0 == 0) && (stride == 2)) return data;
    double[] newdata = new double[2 * n];
    for (int i = 0; i < n; i++) {
      newdata[(2 * i)] = data[(i0 + stride * i)];
      newdata[(2 * i + 1)] = data[(i0 + stride * i + 1)]; }
    return newdata;
  }
  
  public void backtransform(double[] data) {
    backtransform(data, 0, 2);
  }
  




  public abstract void backtransform(double[] paramArrayOfDouble, int paramInt1, int paramInt2);
  




  public double normalization()
  {
    return 1.0D / n;
  }
  
  public void inverse(double[] data) {
    inverse(data, 0, 2);
  }
  






  public void inverse(double[] data, int i0, int stride)
  {
    backtransform(data, i0, stride);
    

    double norm = normalization();
    for (int i = 0; i < n; i++) {
      data[(i0 + stride * i)] *= norm;
      data[(i0 + stride * i + 1)] *= norm;
    }
  }
}
