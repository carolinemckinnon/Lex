package jnt.FFT;














public abstract class RealDoubleFFT
{
  int n;
  













  public RealDoubleFFT(int n)
  {
    if (n <= 0)
      throw new IllegalArgumentException("The transform length must be >=0 : " + n);
    this.n = n;
  }
  
  protected void checkData(double[] data, int i0, int stride) { if (i0 < 0)
      throw new IllegalArgumentException("The offset must be >=0 : " + i0);
    if (stride < 1)
      throw new IllegalArgumentException("The stride must be >=1 : " + stride);
    if (i0 + stride * (n - 1) + 1 > data.length)
      throw new IllegalArgumentException("The data array is too small for " + n + ":" + 
        "i0=" + i0 + " stride=" + stride + 
        " data.length=" + data.length);
  }
  
  public void transform(double[] data) {
    transform(data, 0, 1);
  }
  


  public abstract void transform(double[] paramArrayOfDouble, int paramInt1, int paramInt2);
  


  public abstract double[] toWraparoundOrder(double[] paramArrayOfDouble);
  

  public abstract double[] toWraparoundOrder(double[] paramArrayOfDouble, int paramInt1, int paramInt2);
  

  public void backtransform(double[] data)
  {
    backtransform(data, 0, 1);
  }
  

  public abstract void backtransform(double[] paramArrayOfDouble, int paramInt1, int paramInt2);
  
  public double normalization()
  {
    return 1.0D / n;
  }
  
  public void inverse(double[] data) {
    inverse(data, 0, 1);
  }
  
  public void inverse(double[] data, int i0, int stride) {
    backtransform(data, i0, stride);
    

    double norm = normalization();
    for (int i = 0; i < n; i++) {
      data[(i0 + stride * i)] *= norm;
    }
  }
}
