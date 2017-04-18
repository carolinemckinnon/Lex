package jnt.FFT;




public class ComplexFloatFFT_Radix2
  extends ComplexFloatFFT
{
  static final int FORWARD = -1;
  


  static final int BACKWARD = 1;
  


  static final int DECINTIME = 0;
  

  static final int DECINFREQ = 1;
  

  private int logn;
  

  private int decimate = 0;
  
  public ComplexFloatFFT_Radix2(int n) {
    super(n);
    
    logn = Factorize.log2(n);
    if (logn < 0) {
      throw new Error(n + " is not a power of 2");
    }
  }
  

  public void setDecimateInTime() { decimate = 0; }
  
  public void setDecimateInFrequency() { decimate = 1; }
  
  public void transform(float[] data, int i0, int stride) {
    checkData(data, i0, stride);
    transform_internal(data, i0, stride, -1);
  }
  
  public void backtransform(float[] data, int i0, int stride) { checkData(data, i0, stride);
    transform_internal(data, i0, stride, 1);
  }
  
  void transform_internal(float[] data, int i0, int stride, int direction)
  {
    if (decimate == 1) {
      transform_DIF(data, i0, stride, direction);
    } else
      transform_DIT(data, i0, stride, direction);
  }
  
  void transform_DIT(float[] data, int i0, int stride, int direction) { if (n == 1) { return;
    }
    
    bitreverse(data, i0, stride);
    

    int bit = 0; for (int dual = 1; bit < logn; dual *= 2) {
      float w_real = 1.0F;
      float w_imag = 0.0F;
      
      double theta = 2.0D * direction * 3.141592653589793D / (2.0D * dual);
      float s = (float)Math.sin(theta);
      float t = (float)Math.sin(theta / 2.0D);
      float s2 = 2.0F * t * t;
      

      for (int b = 0; b < n; b += 2 * dual) {
        int i = i0 + b * stride;
        int j = i0 + (b + dual) * stride;
        
        float wd_real = data[j];
        float wd_imag = data[(j + 1)];
        
        data[i] -= wd_real;
        data[(i + 1)] -= wd_imag;
        data[i] += wd_real;
        data[(i + 1)] += wd_imag;
      }
      

      for (int a = 1; a < dual; a++)
      {

        float tmp_real = w_real - s * w_imag - s2 * w_real;
        float tmp_imag = w_imag + s * w_real - s2 * w_imag;
        w_real = tmp_real;
        w_imag = tmp_imag;
        
        for (int b = 0; b < n; b += 2 * dual) {
          int i = i0 + (b + a) * stride;
          int j = i0 + (b + a + dual) * stride;
          
          float z1_real = data[j];
          float z1_imag = data[(j + 1)];
          
          float wd_real = w_real * z1_real - w_imag * z1_imag;
          float wd_imag = w_real * z1_imag + w_imag * z1_real;
          
          data[i] -= wd_real;
          data[(i + 1)] -= wd_imag;
          data[i] += wd_real;
          data[(i + 1)] += wd_imag;
        }
      }
      bit++;
    }
  }
  














































  void transform_DIF(float[] data, int i0, int stride, int direction)
  {
    if (n == 1) { return;
    }
    
    int bit = 0; for (int dual = n / 2; bit < logn; dual /= 2) {
      float w_real = 1.0F;
      float w_imag = 0.0F;
      
      double theta = 2.0D * direction * 3.141592653589793D / (2 * dual);
      
      float s = (float)Math.sin(theta);
      float t = (float)Math.sin(theta / 2.0D);
      float s2 = 2.0F * t * t;
      
      for (int b = 0; b < dual; b++) {
        for (int a = 0; a < n; a += 2 * dual) {
          int i = i0 + (b + a) * stride;
          int j = i0 + (b + a + dual) * stride;
          
          float t1_real = data[i] + data[j];
          float t1_imag = data[(i + 1)] + data[(j + 1)];
          float t2_real = data[i] - data[j];
          float t2_imag = data[(i + 1)] - data[(j + 1)];
          
          data[i] = t1_real;
          data[(i + 1)] = t1_imag;
          data[j] = (w_real * t2_real - w_imag * t2_imag);
          data[(j + 1)] = (w_real * t2_imag + w_imag * t2_real);
        }
        

        float tmp_real = w_real - s * w_imag - s2 * w_real;
        float tmp_imag = w_imag + s * w_real - s2 * w_imag;
        w_real = tmp_real;
        w_imag = tmp_imag;
      }
      bit++;
    }
    
































    bitreverse(data, i0, stride);
  }
  

  protected void bitreverse(float[] data, int i0, int stride)
  {
    int i = 0; for (int j = 0; i < n - 1; i++) {
      int ii = i0 + i * stride;
      int jj = i0 + j * stride;
      int k = n / 2;
      if (i < j) {
        float tmp_real = data[ii];
        float tmp_imag = data[(ii + 1)];
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
