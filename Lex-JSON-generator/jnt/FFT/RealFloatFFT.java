package jnt.FFT;














public abstract class RealFloatFFT
{
  int n;
  













  public RealFloatFFT(int n)
  {
    if (n <= 0)
      throw new IllegalArgumentException("The transform length must be >=0 : " + n);
    this.n = n;
  }
  
  protected void checkData(float[] data, int i0, int stride) { if (i0 < 0)
      throw new IllegalArgumentException("The offset must be >=0 : " + i0);
    if (stride < 1)
      throw new IllegalArgumentException("The stride must be >=1 : " + stride);
    if (i0 + stride * (n - 1) + 1 > data.length)
      throw new IllegalArgumentException("The data array is too small for " + n + ":" + 
        "i0=" + i0 + " stride=" + stride + 
        " data.length=" + data.length);
  }
  
  public void transform(float[] data) {
    transform(data, 0, 1);
  }
  

  public abstract void transform(float[] paramArrayOfFloat, int paramInt1, int paramInt2);
  
  public float[] toWraparoundOrder(float[] data)
  {
    return toWraparoundOrder(data, 0, 1);
  }
  


  public abstract float[] toWraparoundOrder(float[] paramArrayOfFloat, int paramInt1, int paramInt2);
  

  public void backtransform(float[] data)
  {
    backtransform(data, 0, 1);
  }
  

  public abstract void backtransform(float[] paramArrayOfFloat, int paramInt1, int paramInt2);
  
  public float normalization()
  {
    return 1.0F / n;
  }
  
  public void inverse(float[] data) {
    inverse(data, 0, 1);
  }
  
  public void inverse(float[] data, int i0, int stride) {
    backtransform(data, i0, stride);
    

    float norm = normalization();
    for (int i = 0; i < n; i++) {
      data[(i0 + stride * i)] *= norm;
    }
  }
}
