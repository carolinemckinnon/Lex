package jnt.FFT;



public class ComplexDoubleFFT_Radix2
  extends ComplexDoubleFFT
{
  static final double PI = 3.141592653589793D;
  

  static final int FORWARD = -1;
  

  static final int BACKWARD = 1;
  

  static final int DECINTIME = 0;
  

  static final int DECINFREQ = 1;
  

  private int logn;
  

  private int decimate = 0;
  private double[] trigs;
  
  public ComplexDoubleFFT_Radix2(int n) {
    super(n);
    
    int log = Factorize.log2(n);
    if (log < 0)
      throw new Error(n + " is not a power of 2");
    logn = log;
    trigs = new double[logn + 1];
    double theta = 3.141592653589793D;
    for (int i = 0; i <= logn; i++) {
      trigs[i] = Math.sin(theta);
      theta /= 2.0D;
    }
  }
  

  public void setDecimateInTime() { decimate = 0; }
  
  public void setDecimateInFrequency() { decimate = 1; }
  
  public void transform(double[] data, int i0, int stride) {
    checkData(data, i0, stride);
    transform_internal(data, i0, stride, -1);
  }
  
  public void backtransform(double[] data, int i0, int stride) { checkData(data, i0, stride);
    transform_internal(data, i0, stride, 1);
  }
  
  void transform_internal(double[] data, int i0, int stride, int direction)
  {
    if (decimate == 1) {
      transform_DIF(data, i0, stride, direction);
    } else
      transform_DIT(data, i0, stride, direction);
  }
  
  void transform_DIT(double[] data, int i0, int stride, int direction) { if (n == 1) { return;
    }
    
    bitreverse(data, i0, stride);
    

    int bit = 0; for (int dual = 1; bit < logn; dual *= 2) {
      double w_real = 1.0D;
      double w_imag = 0.0D;
      



      double s = direction * trigs[bit];
      double t = direction * trigs[(bit + 1)];
      double s2 = 2.0D * t * t;
      

      for (int b = 0; b < n; b += 2 * dual) {
        int i = i0 + b * stride;
        int j = i0 + (b + dual) * stride;
        
        double wd_real = data[j];
        double wd_imag = data[(j + 1)];
        
        data[i] -= wd_real;
        data[(i + 1)] -= wd_imag;
        data[i] += wd_real;
        data[(i + 1)] += wd_imag;
      }
      

      for (int a = 1; a < dual; a++)
      {

        double tmp_real = w_real - s * w_imag - s2 * w_real;
        double tmp_imag = w_imag + s * w_real - s2 * w_imag;
        w_real = tmp_real;
        w_imag = tmp_imag;
        
        for (int b = 0; b < n; b += 2 * dual) {
          int i = i0 + (b + a) * stride;
          int j = i0 + (b + a + dual) * stride;
          
          double z1_real = data[j];
          double z1_imag = data[(j + 1)];
          
          double wd_real = w_real * z1_real - w_imag * z1_imag;
          double wd_imag = w_real * z1_imag + w_imag * z1_real;
          
          data[i] -= wd_real;
          data[(i + 1)] -= wd_imag;
          data[i] += wd_real;
          data[(i + 1)] += wd_imag;
        }
      }
      bit++;
    }
  }
  
















































  void transform_DIF(double[] data, int i0, int stride, int direction)
  {
    if (n == 1) { return;
    }
    
    int bit = 0; for (int dual = n / 2; bit < logn; dual /= 2) {
      double w_real = 1.0D;
      double w_imag = 0.0D;
      



      double s = direction * trigs[(logn - 1 - bit)];
      double t = direction * trigs[(logn - bit)];
      
      double s2 = 2.0D * t * t;
      
      for (int b = 0; b < dual; b++) {
        for (int a = 0; a < n; a += 2 * dual) {
          int i = i0 + (b + a) * stride;
          int j = i0 + (b + a + dual) * stride;
          
          double t1_real = data[i] + data[j];
          double t1_imag = data[(i + 1)] + data[(j + 1)];
          double t2_real = data[i] - data[j];
          double t2_imag = data[(i + 1)] - data[(j + 1)];
          
          data[i] = t1_real;
          data[(i + 1)] = t1_imag;
          data[j] = (w_real * t2_real - w_imag * t2_imag);
          data[(j + 1)] = (w_real * t2_imag + w_imag * t2_real);
        }
        

        double tmp_real = w_real - s * w_imag - s2 * w_real;
        double tmp_imag = w_imag + s * w_real - s2 * w_imag;
        w_real = tmp_real;
        w_imag = tmp_imag;
      }
      bit++;
    }
    


































    bitreverse(data, i0, stride);
  }
  

  protected void bitreverse(double[] data, int i0, int stride)
  {
    int i = 0; for (int j = 0; i < n - 1; i++) {
      int ii = i0 + i * stride;
      int jj = i0 + j * stride;
      int k = n / 2;
      if (i < j) {
        double tmp_real = data[ii];
        double tmp_imag = data[(ii + 1)];
        data[ii] = data[jj];
        data[(ii + 1)] = data[(jj + 1)];
        data[jj] = tmp_real;
        data[(jj + 1)] = tmp_imag;
      }
      while (k <= j) {
        j -= k;
        k /= 2; }
      j += k;
    }
  }
}
