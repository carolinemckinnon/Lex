package edu.ucla.sspace.fft;

import edu.ucla.sspace.vector.DoubleVector;





















public class FastFourierTransform
{
  public FastFourierTransform() {}
  
  public static int checkFactor(int n)
  {
    int logn = log2(n);
    if (logn < 0)
      throw new IllegalArgumentException(n + " is not a power of 2");
    return logn;
  }
  


  public static void transform(DoubleVector data)
  {
    int i0 = 0;
    int stride = 1;
    checkData(data, i0, stride);
    

    int n = data.length();
    int logn = checkFactor(n);
    if (n == 1) {
      return;
    }
    

    bitreverse(data, i0, stride);
    


    int p = 1;int q = n;
    for (int i = 1; i <= 1; i++)
    {

      int p_1 = p;
      p *= 2;
      q /= 2;
      
      for (int b = 0; b < q; b++) {
        data.set(i0 + stride * b * p, 
          data.get(i0 + stride * b * p) + 
          data.get(i0 + stride * (b * p + p_1)));
        data.set(i0 + stride * (b * p + p_1), 
          data.get(i0 + stride * b * p) - 
          data.get(i0 + stride * (b * p + p_1)));
      }
      
      double w_real = 1.0D;
      double w_imag = 0.0D;
      
      double theta = -6.283185307179586D / p;
      
      double s = Math.sin(theta);
      double t = Math.sin(theta / 2.0D);
      double s2 = 2.0D * t * t;
      

      for (int a = 1; a < p_1 / 2; a++) {
        double tmp_real = w_real - s * w_imag - s2 * w_real;
        double tmp_imag = w_imag + s * w_real - s2 * w_imag;
        w_real = tmp_real;
        w_imag = tmp_imag;
        
        for (b = 0; b < q; b++) {
          double z0_real = data.get(i0 + stride * (b * p + a));
          double z0_imag = data.get(i0 + stride * (b * p + p_1 - a));
          double z1_real = data.get(i0 + stride * (b * p + p_1 + a));
          double z1_imag = data.get(i0 + stride * (b * p + p - a));
          
          data.set(i0 + stride * (b * p + a), 
            z0_real + w_real * z1_real - w_imag * z1_imag);
          data.set(i0 + stride * (b * p + p - a), 
            z0_imag + w_real * z1_imag + w_imag * z1_real);
          data.set(i0 + stride * (b * p + p_1 - a), 
            z0_real - w_real * z1_real + w_imag * z1_imag);
          data.set(i0 + stride * (b * p + p_1 + a), 
            -(z0_imag - w_real * z1_imag - w_imag * z1_real));
        }
      }
      
      if (p_1 > 1) {
        for (b = 0; b < q; b++) {
          int index = i0 + stride * (b * p + p - p_1 / 2);
          data.set(index, data.get(index) * -1.0D);
        }
      }
    }
  }
  




  public static void backtransform(DoubleVector data)
  {
    int i0 = 0;
    int stride = 1;
    checkData(data, i0, stride);
    int n = data.length();
    int logn = checkFactor(n);
    

    if (n == 1) {
      return;
    }
    
    int p = n;int q = 1;int p_1 = n / 2;
    
    for (int i = 1; i <= logn; i++)
    {

      for (int b = 0; b < q; b++) {
        double z0 = data.get(i0 + stride * b * p);
        double z1 = data.get(i0 + stride * (b * p + p_1));
        data.set(i0 + stride * b * p, z0 + z1);
        data.set(i0 + stride * (b * p + p_1), z0 - z1);
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
          double z0_real = data.get(i0 + stride * (b * p + a));
          double z0_imag = data.get(i0 + stride * (b * p + p - a));
          double z1_real = data.get(i0 + stride * (b * p + p_1 - a));
          double z1_imag = -data.get(i0 + stride * (b * p + p_1 + a));
          

          data.set(i0 + stride * (b * p + a), z0_real + z1_real);
          data.set(i0 + stride * (b * p + p_1 - a), z0_imag + z1_imag);
          

          double t1_real = z0_real - z1_real;
          double t1_imag = z0_imag - z1_imag;
          data.set(i0 + stride * (b * p + p_1 + a), 
            w_real * t1_real - w_imag * t1_imag);
          data.set(i0 + stride * (b * p + p - a), 
            w_real * t1_imag + w_imag * t1_real);
        }
      }
      
      if (p_1 > 1) {
        for (b = 0; b < q; b++) {
          int index = i0 + stride * (b * p + p_1 / 2);
          data.set(index, data.get(index) * 2.0D);
          index = i0 + stride * (b * p + p_1 + p_1 / 2);
          data.set(index, data.get(index) * -2.0D);
        }
      }
      
      p_1 /= 2;
      p /= 2;
      q *= 2;
    }
    



    bitreverse(data, i0, stride);
  }
  


  public static void bitreverse(DoubleVector data, int i0, int stride)
  {
    int n = data.length();
    int i = 0; for (int j = 0; i < n - 1; i++) {
      int k = n / 2;
      if (i < j) {
        double tmp = data.get(i0 + stride * i);
        data.set(i0 + stride * i, data.get(i0 + stride * j));
        data.set(i0 + stride * j, tmp);
      }
      
      while (k <= j) {
        j -= k;
        k /= 2;
      }
      j += k;
    }
  }
  












  private static void bitReverse(DoubleVector vector, int power)
  {
    vector.set(0, 0.0D);
    vector.set(1, 2 << power - 1);
    vector.set(2, 2 << power - 2);
    vector.set(3, 6 << power - 2);
    int prevN = 3;
    for (int k = 3; k < power - 2; k++) {
      int currN = (2 << k) - 1;
      vector.set(currN, vector.get(prevN) + (2 << power - k));
      for (int l = 0; l < prevN - 1; l++)
        vector.set(currN - l, vector.get(currN) - vector.get(l));
      prevN = currN;
    }
  }
  
  private static int log2(int n) {
    int log = 0;
    for (int k = 1; k < n; log++) { k *= 2;
    }
    return n != 1 << log ? -1 : log;
  }
  
  private static void checkData(DoubleVector data, int i0, int stride) {
    int n = data.length();
    if (i0 + stride * (n - 1) + 1 > data.length()) {
      throw new IllegalArgumentException(
        "The data array is too small for " + n + ":" + 
        "i0=" + i0 + " stride=" + stride + " data.length=" + n);
    }
  }
}
