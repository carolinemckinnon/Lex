package jnt.FFT;






















public class RealDoubleFFT_Radix2
  extends RealDoubleFFT
{
  private int logn;
  





















  public RealDoubleFFT_Radix2(int n)
  {
    super(n);
    logn = Factorize.log2(n);
    if (logn < 0) {
      throw new IllegalArgumentException(n + " is not a power of 2");
    }
  }
  

  public void transform(double[] data, int i0, int stride)
  {
    checkData(data, i0, stride);
    

    if (n == 1) { return;
    }
    
    bitreverse(data, i0, stride);
    

    int p = 1;int q = n;
    for (int i = 1; i <= logn; i++)
    {

      int p_1 = p;
      p *= 2;
      q /= 2;
      


      for (int b = 0; b < q; b++) {
        double t0_real = data[(i0 + stride * b * p)] + data[(i0 + stride * (b * p + p_1))];
        double t1_real = data[(i0 + stride * b * p)] - data[(i0 + stride * (b * p + p_1))];
        
        data[(i0 + stride * b * p)] = t0_real;
        data[(i0 + stride * (b * p + p_1))] = t1_real;
      }
      



      double w_real = 1.0D;
      double w_imag = 0.0D;
      
      double theta = -6.283185307179586D / p;
      
      double s = Math.sin(theta);
      double t = Math.sin(theta / 2.0D);
      double s2 = 2.0D * t * t;
      
      for (int a = 1; a < p_1 / 2; a++)
      {


        double tmp_real = w_real - s * w_imag - s2 * w_real;
        double tmp_imag = w_imag + s * w_real - s2 * w_imag;
        w_real = tmp_real;
        w_imag = tmp_imag;
        

        for (b = 0; b < q; b++) {
          double z0_real = data[(i0 + stride * (b * p + a))];
          double z0_imag = data[(i0 + stride * (b * p + p_1 - a))];
          double z1_real = data[(i0 + stride * (b * p + p_1 + a))];
          double z1_imag = data[(i0 + stride * (b * p + p - a))];
          

          data[(i0 + stride * (b * p + a))] = (z0_real + w_real * z1_real - w_imag * z1_imag);
          data[(i0 + stride * (b * p + p - a))] = (z0_imag + w_real * z1_imag + w_imag * z1_real);
          
          data[(i0 + stride * (b * p + p_1 - a))] = (z0_real - w_real * z1_real + w_imag * z1_imag);
          data[(i0 + stride * (b * p + p_1 + a))] = (-(z0_imag - w_real * z1_imag - w_imag * z1_real));
        }
      }
      

      if (p_1 > 1) {
        for (b = 0; b < q; b++)
        {
          data[(i0 + stride * (b * p + p - p_1 / 2))] *= -1.0D;
        }
      }
    }
  }
  

  public void backtransform(double[] data, int i0, int stride)
  {
    checkData(data, i0, stride);
    

    if (n == 1) { return;
    }
    

    int p = n;int q = 1;int p_1 = n / 2;
    
    for (int i = 1; i <= logn; i++)
    {



      for (int b = 0; b < q; b++) {
        double z0 = data[(i0 + stride * b * p)];
        double z1 = data[(i0 + stride * (b * p + p_1))];
        data[(i0 + stride * b * p)] = (z0 + z1);
        data[(i0 + stride * (b * p + p_1))] = (z0 - z1);
      }
      


      double w_real = 1.0D;
      double w_imag = 0.0D;
      
      double theta = 6.283185307179586D / p;
      
      double s = Math.sin(theta);
      double t = Math.sin(theta / 2.0D);
      double s2 = 2.0D * t * t;
      
      for (int a = 1; a < p_1 / 2; a++)
      {
        double tmp_real = w_real - s * w_imag - s2 * w_real;
        double tmp_imag = w_imag + s * w_real - s2 * w_imag;
        w_real = tmp_real;
        w_imag = tmp_imag;
        
        for (b = 0; b < q; b++) {
          double z0_real = data[(i0 + stride * (b * p + a))];
          double z0_imag = data[(i0 + stride * (b * p + p - a))];
          double z1_real = data[(i0 + stride * (b * p + p_1 - a))];
          double z1_imag = -data[(i0 + stride * (b * p + p_1 + a))];
          

          data[(i0 + stride * (b * p + a))] = (z0_real + z1_real);
          data[(i0 + stride * (b * p + p_1 - a))] = (z0_imag + z1_imag);
          

          double t1_real = z0_real - z1_real;
          double t1_imag = z0_imag - z1_imag;
          data[(i0 + stride * (b * p + p_1 + a))] = (w_real * t1_real - w_imag * t1_imag);
          data[(i0 + stride * (b * p + p - a))] = (w_real * t1_imag + w_imag * t1_real);
        }
      }
      

      if (p_1 > 1) {
        for (b = 0; b < q; b++) {
          data[(i0 + stride * (b * p + p_1 / 2))] *= 2.0D;
          data[(i0 + stride * (b * p + p_1 + p_1 / 2))] *= -2.0D;
        }
      }
      
      p_1 /= 2;
      p /= 2;
      q *= 2;
    }
    


    bitreverse(data, i0, stride);
  }
  


  public double[] toWraparoundOrder(double[] data)
  {
    return toWraparoundOrder(data, 0, 1);
  }
  


  public double[] toWraparoundOrder(double[] data, int i0, int stride)
  {
    checkData(data, i0, stride);
    double[] newdata = new double[2 * n];
    int nh = n / 2;
    newdata[0] = data[i0];
    newdata[1] = 0.0D;
    newdata[n] = data[(i0 + stride * nh)];
    newdata[(n + 1)] = 0.0D;
    for (int i = 1; i < nh; i++) {
      newdata[(2 * i)] = data[(i0 + stride * i)];
      newdata[(2 * i + 1)] = data[(i0 + stride * (n - i))];
      newdata[(2 * (n - i))] = data[(i0 + stride * i)];
      newdata[(2 * (n - i) + 1)] = (-data[(i0 + stride * (n - i))]); }
    return newdata;
  }
  
  protected void bitreverse(double[] data, int i0, int stride)
  {
    int i = 0; for (int j = 0; i < n - 1; i++) {
      int k = n / 2;
      if (i < j) {
        double tmp = data[(i0 + stride * i)];
        data[(i0 + stride * i)] = data[(i0 + stride * j)];
        data[(i0 + stride * j)] = tmp;
      }
      while (k <= j) {
        j -= k;
        k /= 2; }
      j += k;
    }
  }
}
