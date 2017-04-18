package jnt.FFT;





public class ComplexDoubleFFT_Mixed
  extends ComplexDoubleFFT
{
  static final double PI = 3.141592653589793D;
  


  private int[] factors;
  


  private double[][][] twiddle;
  



  public ComplexDoubleFFT_Mixed(int n)
  {
    super(n);
    setup_wavetable(n);
  }
  
  public void transform(double[] data, int i0, int stride) {
    checkData(data, i0, stride);
    transform_internal(data, i0, stride, -1);
  }
  
  public void backtransform(double[] data, int i0, int stride) { checkData(data, i0, stride);
    transform_internal(data, i0, stride, 1);
  }
  





  private int[] available_factors = { 7, 6, 5, 4, 3, 2 };
  
  void setup_wavetable(int n)
  {
    if (n <= 0)
      throw new Error("length must be positive integer : " + n);
    this.n = n;
    
    factors = Factorize.factor(n, available_factors);
    
    double d_theta = -6.283185307179586D / n;
    int product = 1;
    twiddle = new double[factors.length][][];
    for (int i = 0; i < factors.length; i++) {
      int factor = factors[i];
      int product_1 = product;
      product *= factor;
      int q = n / product;
      
      twiddle[i] = new double[q + 1][2 * (factor - 1)];
      double[][] twid = twiddle[i];
      for (int j = 1; j < factor; j++) {
        twid[0][(2 * (j - 1))] = 1.0D;
        twid[0][(2 * (j - 1) + 1)] = 0.0D; }
      for (int k = 1; k <= q; k++) {
        int m = 0;
        for (int j = 1; j < factor; j++)
        {
          m += k * product_1;
          m %= n;
          double theta = d_theta * m;
          twid[k][(2 * (j - 1))] = Math.cos(theta);
          twid[k][(2 * (j - 1) + 1)] = Math.sin(theta);
        }
      }
    }
  }
  
  void transform_internal(double[] data, int i0, int stride, int sign)
  {
    if (n == 1) { return;
    }
    double[] scratch = new double[2 * n];
    int product = 1;
    int state = 0;
    



    for (int i = 0; i < factors.length; i++) {
      int factor = factors[i];
      product *= factor;
      double[] in;
      int in0; int istride; double[] out; int out0; int ostride; if (state == 0) {
        double[] in = data;
        int in0 = i0;
        int istride = stride;
        double[] out = scratch;
        int out0 = 0;
        int ostride = 2;
        state = 1;
      } else {
        in = scratch;
        in0 = 0;
        istride = 2;
        out = data;
        out0 = i0;
        ostride = stride;
        state = 0;
      }
      switch (factor) {
      case 2:  pass_2(i, in, in0, istride, out, out0, ostride, sign, product); break;
      case 3:  pass_3(i, in, in0, istride, out, out0, ostride, sign, product); break;
      case 4:  pass_4(i, in, in0, istride, out, out0, ostride, sign, product); break;
      case 5:  pass_5(i, in, in0, istride, out, out0, ostride, sign, product); break;
      case 6:  pass_6(i, in, in0, istride, out, out0, ostride, sign, product); break;
      case 7:  pass_7(i, in, in0, istride, out, out0, ostride, sign, product); break;
      default:  pass_n(i, in, in0, istride, out, out0, ostride, sign, factor, product); }
    }
    if (state == 1) {
      for (int i = 0; i < n; i++) {
        data[(i0 + stride * i)] = scratch[(2 * i)];
        data[(i0 + stride * i + 1)] = scratch[(2 * i + 1)];
      }
    }
  }
  




  void pass_2(int fi, double[] in, int in0, int istride, double[] out, int out0, int ostride, int sign, int product)
  {
    int factor = 2;
    int m = n / factor;
    int q = n / product;
    int product_1 = product / factor;
    
    int di = istride * m;
    int dj = ostride * product_1;
    int i = in0;int j = out0;
    
    for (int k = 0; k < q; k++) {
      double[] twids = twiddle[fi][k];
      double w_real = twids[0];
      double w_imag = -sign * twids[1];
      
      for (int k1 = 0; k1 < product_1; k1++) {
        double z0_real = in[i];
        double z0_imag = in[(i + 1)];
        double z1_real = in[(i + di)];
        double z1_imag = in[(i + di + 1)];
        i += istride;
        





        out[j] = (z0_real + z1_real);
        out[(j + 1)] = (z0_imag + z1_imag);
        

        double x_real = z0_real - z1_real;
        double x_imag = z0_imag - z1_imag;
        out[(j + dj)] = (w_real * x_real - w_imag * x_imag);
        out[(j + dj + 1)] = (w_real * x_imag + w_imag * x_real);
        
        j += ostride;
      }
      j += (factor - 1) * dj;
    }
  }
  




  void pass_3(int fi, double[] in, int in0, int istride, double[] out, int out0, int ostride, int sign, int product)
  {
    int factor = 3;
    int m = n / factor;
    int q = n / product;
    int product_1 = product / factor;
    int jump = (factor - 1) * product_1;
    
    double tau = sign * Math.sqrt(3.0D) / 2.0D;
    int di = istride * m;
    int dj = ostride * product_1;
    int i = in0;int j = out0;
    
    for (int k = 0; k < q; k++) {
      double[] twids = twiddle[fi][k];
      double w1_real = twids[0];
      double w1_imag = -sign * twids[1];
      double w2_real = twids[2];
      double w2_imag = -sign * twids[3];
      
      for (int k1 = 0; k1 < product_1; k1++) {
        double z0_real = in[i];
        double z0_imag = in[(i + 1)];
        double z1_real = in[(i + di)];
        double z1_imag = in[(i + di + 1)];
        double z2_real = in[(i + 2 * di)];
        double z2_imag = in[(i + 2 * di + 1)];
        i += istride;
        



        double t1_real = z1_real + z2_real;
        double t1_imag = z1_imag + z2_imag;
        

        double t2_real = z0_real - t1_real / 2.0D;
        double t2_imag = z0_imag - t1_imag / 2.0D;
        

        double t3_real = tau * (z1_real - z2_real);
        double t3_imag = tau * (z1_imag - z2_imag);
        



        out[j] = (z0_real + t1_real);
        out[(j + 1)] = (z0_imag + t1_imag);
        

        double x_real = t2_real - t3_imag;
        double x_imag = t2_imag + t3_real;
        out[(j + dj)] = (w1_real * x_real - w1_imag * x_imag);
        out[(j + dj + 1)] = (w1_real * x_imag + w1_imag * x_real);
        

        x_real = t2_real + t3_imag;
        x_imag = t2_imag - t3_real;
        out[(j + 2 * dj)] = (w2_real * x_real - w2_imag * x_imag);
        out[(j + 2 * dj + 1)] = (w2_real * x_imag + w2_imag * x_real);
        
        j += ostride;
      }
      j += (factor - 1) * dj;
    }
  }
  




  void pass_4(int fi, double[] in, int in0, int istride, double[] out, int out0, int ostride, int sign, int product)
  {
    int factor = 4;
    int m = n / factor;
    int q = n / product;
    int p_1 = product / factor;
    int jump = (factor - 1) * p_1;
    int i = in0;int j = out0;
    int di = istride * m;
    int dj = ostride * p_1;
    
    for (int k = 0; k < q; k++) {
      double[] twids = twiddle[fi][k];
      double w1_real = twids[0];
      double w1_imag = -sign * twids[1];
      double w2_real = twids[2];
      double w2_imag = -sign * twids[3];
      double w3_real = twids[4];
      double w3_imag = -sign * twids[5];
      
      for (int k1 = 0; k1 < p_1; k1++) {
        double z0_real = in[i];
        double z0_imag = in[(i + 1)];
        double z1_real = in[(i + di)];
        double z1_imag = in[(i + di + 1)];
        double z2_real = in[(i + 2 * di)];
        double z2_imag = in[(i + 2 * di + 1)];
        double z3_real = in[(i + 3 * di)];
        double z3_imag = in[(i + 3 * di + 1)];
        i += istride;
        



        double t1_real = z0_real + z2_real;
        double t1_imag = z0_imag + z2_imag;
        

        double t2_real = z1_real + z3_real;
        double t2_imag = z1_imag + z3_imag;
        

        double t3_real = z0_real - z2_real;
        double t3_imag = z0_imag - z2_imag;
        

        double t4_real = sign * (z1_real - z3_real);
        double t4_imag = sign * (z1_imag - z3_imag);
        



        out[j] = (t1_real + t2_real);
        out[(j + 1)] = (t1_imag + t2_imag);
        

        double x_real = t3_real - t4_imag;
        double x_imag = t3_imag + t4_real;
        out[(j + dj)] = (w1_real * x_real - w1_imag * x_imag);
        out[(j + dj + 1)] = (w1_real * x_imag + w1_imag * x_real);
        

        x_real = t1_real - t2_real;
        x_imag = t1_imag - t2_imag;
        out[(j + 2 * dj)] = (w2_real * x_real - w2_imag * x_imag);
        out[(j + 2 * dj + 1)] = (w2_real * x_imag + w2_imag * x_real);
        

        x_real = t3_real + t4_imag;
        x_imag = t3_imag - t4_real;
        out[(j + 3 * dj)] = (w3_real * x_real - w3_imag * x_imag);
        out[(j + 3 * dj + 1)] = (w3_real * x_imag + w3_imag * x_real);
        
        j += ostride;
      }
      j += (factor - 1) * dj;
    }
  }
  




  void pass_5(int fi, double[] in, int in0, int istride, double[] out, int out0, int ostride, int sign, int product)
  {
    int factor = 5;
    int m = n / factor;
    int q = n / product;
    int p_1 = product / factor;
    int jump = (factor - 1) * p_1;
    double tau = Math.sqrt(5.0D) / 4.0D;
    double sin_2pi_by_5 = sign * Math.sin(1.2566370614359172D);
    double sin_2pi_by_10 = sign * Math.sin(0.6283185307179586D);
    int i = in0;int j = out0;
    int di = istride * m;
    int dj = ostride * p_1;
    
    for (int k = 0; k < q; k++) {
      double[] twids = twiddle[fi][k];
      double w1_real = twids[0];
      double w1_imag = -sign * twids[1];
      double w2_real = twids[2];
      double w2_imag = -sign * twids[3];
      double w3_real = twids[4];
      double w3_imag = -sign * twids[5];
      double w4_real = twids[6];
      double w4_imag = -sign * twids[7];
      
      for (int k1 = 0; k1 < p_1; k1++) {
        double z0_real = in[i];
        double z0_imag = in[(i + 1)];
        double z1_real = in[(i + di)];
        double z1_imag = in[(i + di + 1)];
        double z2_real = in[(i + 2 * di)];
        double z2_imag = in[(i + 2 * di + 1)];
        double z3_real = in[(i + 3 * di)];
        double z3_imag = in[(i + 3 * di + 1)];
        double z4_real = in[(i + 4 * di)];
        double z4_imag = in[(i + 4 * di + 1)];
        i += istride;
        



        double t1_real = z1_real + z4_real;
        double t1_imag = z1_imag + z4_imag;
        

        double t2_real = z2_real + z3_real;
        double t2_imag = z2_imag + z3_imag;
        

        double t3_real = z1_real - z4_real;
        double t3_imag = z1_imag - z4_imag;
        

        double t4_real = z2_real - z3_real;
        double t4_imag = z2_imag - z3_imag;
        

        double t5_real = t1_real + t2_real;
        double t5_imag = t1_imag + t2_imag;
        

        double t6_real = tau * (t1_real - t2_real);
        double t6_imag = tau * (t1_imag - t2_imag);
        

        double t7_real = z0_real - t5_real / 4.0D;
        double t7_imag = z0_imag - t5_imag / 4.0D;
        

        double t8_real = t7_real + t6_real;
        double t8_imag = t7_imag + t6_imag;
        

        double t9_real = t7_real - t6_real;
        double t9_imag = t7_imag - t6_imag;
        

        double t10_real = sin_2pi_by_5 * t3_real + sin_2pi_by_10 * t4_real;
        double t10_imag = sin_2pi_by_5 * t3_imag + sin_2pi_by_10 * t4_imag;
        

        double t11_real = sin_2pi_by_10 * t3_real - sin_2pi_by_5 * t4_real;
        double t11_imag = sin_2pi_by_10 * t3_imag - sin_2pi_by_5 * t4_imag;
        



        out[j] = (z0_real + t5_real);
        out[(j + 1)] = (z0_imag + t5_imag);
        

        double x_real = t8_real - t10_imag;
        double x_imag = t8_imag + t10_real;
        out[(j + dj)] = (w1_real * x_real - w1_imag * x_imag);
        out[(j + dj + 1)] = (w1_real * x_imag + w1_imag * x_real);
        

        x_real = t9_real - t11_imag;
        x_imag = t9_imag + t11_real;
        out[(j + 2 * dj)] = (w2_real * x_real - w2_imag * x_imag);
        out[(j + 2 * dj + 1)] = (w2_real * x_imag + w2_imag * x_real);
        

        x_real = t9_real + t11_imag;
        x_imag = t9_imag - t11_real;
        out[(j + 3 * dj)] = (w3_real * x_real - w3_imag * x_imag);
        out[(j + 3 * dj + 1)] = (w3_real * x_imag + w3_imag * x_real);
        

        x_real = t8_real + t10_imag;
        x_imag = t8_imag - t10_real;
        out[(j + 4 * dj)] = (w4_real * x_real - w4_imag * x_imag);
        out[(j + 4 * dj + 1)] = (w4_real * x_imag + w4_imag * x_real);
        
        j += ostride;
      }
      j += (factor - 1) * dj;
    }
  }
  





  void pass_6(int fi, double[] in, int in0, int istride, double[] out, int out0, int ostride, int sign, int product)
  {
    int factor = 6;
    int m = n / factor;
    int q = n / product;
    int p_1 = product / factor;
    int jump = (factor - 1) * p_1;
    double tau = sign * Math.sqrt(3.0D) / 2.0D;
    int i = in0;int j = out0;
    int di = istride * m;
    int dj = ostride * p_1;
    
    for (int k = 0; k < q; k++) {
      double[] twids = twiddle[fi][k];
      double w1_real = twids[0];
      double w1_imag = -sign * twids[1];
      double w2_real = twids[2];
      double w2_imag = -sign * twids[3];
      double w3_real = twids[4];
      double w3_imag = -sign * twids[5];
      double w4_real = twids[6];
      double w4_imag = -sign * twids[7];
      double w5_real = twids[8];
      double w5_imag = -sign * twids[9];
      
      for (int k1 = 0; k1 < p_1; k1++) {
        double z0_real = in[i];
        double z0_imag = in[(i + 1)];
        double z1_real = in[(i + di)];
        double z1_imag = in[(i + di + 1)];
        double z2_real = in[(i + 2 * di)];
        double z2_imag = in[(i + 2 * di + 1)];
        double z3_real = in[(i + 3 * di)];
        double z3_imag = in[(i + 3 * di + 1)];
        double z4_real = in[(i + 4 * di)];
        double z4_imag = in[(i + 4 * di + 1)];
        double z5_real = in[(i + 5 * di)];
        double z5_imag = in[(i + 5 * di + 1)];
        i += istride;
        






        double ta1_real = z2_real + z4_real;
        double ta1_imag = z2_imag + z4_imag;
        

        double ta2_real = z0_real - ta1_real / 2.0D;
        double ta2_imag = z0_imag - ta1_imag / 2.0D;
        

        double ta3_real = tau * (z2_real - z4_real);
        double ta3_imag = tau * (z2_imag - z4_imag);
        

        double a0_real = z0_real + ta1_real;
        double a0_imag = z0_imag + ta1_imag;
        

        double a1_real = ta2_real - ta3_imag;
        double a1_imag = ta2_imag + ta3_real;
        

        double a2_real = ta2_real + ta3_imag;
        double a2_imag = ta2_imag - ta3_real;
        

        double tb1_real = z5_real + z1_real;
        double tb1_imag = z5_imag + z1_imag;
        

        double tb2_real = z3_real - tb1_real / 2.0D;
        double tb2_imag = z3_imag - tb1_imag / 2.0D;
        

        double tb3_real = tau * (z5_real - z1_real);
        double tb3_imag = tau * (z5_imag - z1_imag);
        

        double b0_real = z3_real + tb1_real;
        double b0_imag = z3_imag + tb1_imag;
        

        double b1_real = tb2_real - tb3_imag;
        double b1_imag = tb2_imag + tb3_real;
        

        double b2_real = tb2_real + tb3_imag;
        double b2_imag = tb2_imag - tb3_real;
        



        out[j] = (a0_real + b0_real);
        out[(j + 1)] = (a0_imag + b0_imag);
        

        double x_real = a1_real - b1_real;
        double x_imag = a1_imag - b1_imag;
        out[(j + dj)] = (w1_real * x_real - w1_imag * x_imag);
        out[(j + dj + 1)] = (w1_real * x_imag + w1_imag * x_real);
        

        x_real = a2_real + b2_real;
        x_imag = a2_imag + b2_imag;
        out[(j + 2 * dj)] = (w2_real * x_real - w2_imag * x_imag);
        out[(j + 2 * dj + 1)] = (w2_real * x_imag + w2_imag * x_real);
        

        x_real = a0_real - b0_real;
        x_imag = a0_imag - b0_imag;
        out[(j + 3 * dj)] = (w3_real * x_real - w3_imag * x_imag);
        out[(j + 3 * dj + 1)] = (w3_real * x_imag + w3_imag * x_real);
        

        x_real = a1_real + b1_real;
        x_imag = a1_imag + b1_imag;
        out[(j + 4 * dj)] = (w4_real * x_real - w4_imag * x_imag);
        out[(j + 4 * dj + 1)] = (w4_real * x_imag + w4_imag * x_real);
        

        x_real = a2_real - b2_real;
        x_imag = a2_imag - b2_imag;
        out[(j + 5 * dj)] = (w5_real * x_real - w5_imag * x_imag);
        out[(j + 5 * dj + 1)] = (w5_real * x_imag + w5_imag * x_real);
        
        j += ostride;
      }
      j += (factor - 1) * dj;
    }
  }
  





  void pass_7(int fi, double[] in, int in0, int istride, double[] out, int out0, int ostride, int sign, int product)
  {
    int factor = 7;
    int m = n / factor;
    int q = n / product;
    int p_1 = product / factor;
    int jump = (factor - 1) * p_1;
    double c1 = Math.cos(0.8975979010256552D);
    double c2 = Math.cos(1.7951958020513104D);
    double c3 = Math.cos(2.6927937030769655D);
    
    double s1 = -sign * Math.sin(0.8975979010256552D);
    double s2 = -sign * Math.sin(1.7951958020513104D);
    double s3 = -sign * Math.sin(2.6927937030769655D);
    int i = in0;int j = out0;
    int di = istride * m;
    int dj = ostride * p_1;
    
    for (int k = 0; k < q; k++) {
      double[] twids = twiddle[fi][k];
      double w1_real = twids[0];
      double w1_imag = -sign * twids[1];
      double w2_real = twids[2];
      double w2_imag = -sign * twids[3];
      double w3_real = twids[4];
      double w3_imag = -sign * twids[5];
      double w4_real = twids[6];
      double w4_imag = -sign * twids[7];
      double w5_real = twids[8];
      double w5_imag = -sign * twids[9];
      double w6_real = twids[10];
      double w6_imag = -sign * twids[11];
      
      for (int k1 = 0; k1 < p_1; k1++) {
        double z0_real = in[i];
        double z0_imag = in[(i + 1)];
        double z1_real = in[(i + di)];
        double z1_imag = in[(i + di + 1)];
        double z2_real = in[(i + 2 * di)];
        double z2_imag = in[(i + 2 * di + 1)];
        double z3_real = in[(i + 3 * di)];
        double z3_imag = in[(i + 3 * di + 1)];
        double z4_real = in[(i + 4 * di)];
        double z4_imag = in[(i + 4 * di + 1)];
        double z5_real = in[(i + 5 * di)];
        double z5_imag = in[(i + 5 * di + 1)];
        double z6_real = in[(i + 6 * di)];
        double z6_imag = in[(i + 6 * di + 1)];
        i += istride;
        



        double t0_real = z1_real + z6_real;
        double t0_imag = z1_imag + z6_imag;
        

        double t1_real = z1_real - z6_real;
        double t1_imag = z1_imag - z6_imag;
        

        double t2_real = z2_real + z5_real;
        double t2_imag = z2_imag + z5_imag;
        

        double t3_real = z2_real - z5_real;
        double t3_imag = z2_imag - z5_imag;
        

        double t4_real = z4_real + z3_real;
        double t4_imag = z4_imag + z3_imag;
        

        double t5_real = z4_real - z3_real;
        double t5_imag = z4_imag - z3_imag;
        

        double t6_real = t2_real + t0_real;
        double t6_imag = t2_imag + t0_imag;
        

        double t7_real = t5_real + t3_real;
        double t7_imag = t5_imag + t3_imag;
        

        double b0_real = z0_real + t6_real + t4_real;
        double b0_imag = z0_imag + t6_imag + t4_imag;
        

        double b1_real = ((c1 + c2 + c3) / 3.0D - 1.0D) * (t6_real + t4_real);
        double b1_imag = ((c1 + c2 + c3) / 3.0D - 1.0D) * (t6_imag + t4_imag);
        

        double b2_real = (2.0D * c1 - c2 - c3) / 3.0D * (t0_real - t4_real);
        double b2_imag = (2.0D * c1 - c2 - c3) / 3.0D * (t0_imag - t4_imag);
        

        double b3_real = (c1 - 2.0D * c2 + c3) / 3.0D * (t4_real - t2_real);
        double b3_imag = (c1 - 2.0D * c2 + c3) / 3.0D * (t4_imag - t2_imag);
        

        double b4_real = (c1 + c2 - 2.0D * c3) / 3.0D * (t2_real - t0_real);
        double b4_imag = (c1 + c2 - 2.0D * c3) / 3.0D * (t2_imag - t0_imag);
        

        double b5_real = (s1 + s2 - s3) / 3.0D * (t7_real + t1_real);
        double b5_imag = (s1 + s2 - s3) / 3.0D * (t7_imag + t1_imag);
        

        double b6_real = (2.0D * s1 - s2 + s3) / 3.0D * (t1_real - t5_real);
        double b6_imag = (2.0D * s1 - s2 + s3) / 3.0D * (t1_imag - t5_imag);
        

        double b7_real = (s1 - 2.0D * s2 - s3) / 3.0D * (t5_real - t3_real);
        double b7_imag = (s1 - 2.0D * s2 - s3) / 3.0D * (t5_imag - t3_imag);
        

        double b8_real = (s1 + s2 + 2.0D * s3) / 3.0D * (t3_real - t1_real);
        double b8_imag = (s1 + s2 + 2.0D * s3) / 3.0D * (t3_imag - t1_imag);
        


        double T0_real = b0_real + b1_real;
        double T0_imag = b0_imag + b1_imag;
        

        double T1_real = b2_real + b3_real;
        double T1_imag = b2_imag + b3_imag;
        

        double T2_real = b4_real - b3_real;
        double T2_imag = b4_imag - b3_imag;
        

        double T3_real = -b2_real - b4_real;
        double T3_imag = -b2_imag - b4_imag;
        

        double T4_real = b6_real + b7_real;
        double T4_imag = b6_imag + b7_imag;
        

        double T5_real = b8_real - b7_real;
        double T5_imag = b8_imag - b7_imag;
        

        double T6_real = -b8_real - b6_real;
        double T6_imag = -b8_imag - b6_imag;
        

        double T7_real = T0_real + T1_real;
        double T7_imag = T0_imag + T1_imag;
        

        double T8_real = T0_real + T2_real;
        double T8_imag = T0_imag + T2_imag;
        

        double T9_real = T0_real + T3_real;
        double T9_imag = T0_imag + T3_imag;
        

        double T10_real = T4_real + b5_real;
        double T10_imag = T4_imag + b5_imag;
        

        double T11_real = T5_real + b5_real;
        double T11_imag = T5_imag + b5_imag;
        

        double T12_real = T6_real + b5_real;
        double T12_imag = T6_imag + b5_imag;
        



        out[j] = b0_real;
        out[(j + 1)] = b0_imag;
        

        double x_real = T7_real + T10_imag;
        double x_imag = T7_imag - T10_real;
        out[(j + dj)] = (w1_real * x_real - w1_imag * x_imag);
        out[(j + dj + 1)] = (w1_real * x_imag + w1_imag * x_real);
        

        x_real = T9_real + T12_imag;
        x_imag = T9_imag - T12_real;
        out[(j + 2 * dj)] = (w2_real * x_real - w2_imag * x_imag);
        out[(j + 2 * dj + 1)] = (w2_real * x_imag + w2_imag * x_real);
        

        x_real = T8_real - T11_imag;
        x_imag = T8_imag + T11_real;
        out[(j + 3 * dj)] = (w3_real * x_real - w3_imag * x_imag);
        out[(j + 3 * dj + 1)] = (w3_real * x_imag + w3_imag * x_real);
        

        x_real = T8_real + T11_imag;
        x_imag = T8_imag - T11_real;
        out[(j + 4 * dj)] = (w4_real * x_real - w4_imag * x_imag);
        out[(j + 4 * dj + 1)] = (w4_real * x_imag + w4_imag * x_real);
        

        x_real = T9_real - T12_imag;
        x_imag = T9_imag + T12_real;
        out[(j + 5 * dj)] = (w5_real * x_real - w5_imag * x_imag);
        out[(j + 5 * dj + 1)] = (w5_real * x_imag + w5_imag * x_real);
        

        x_real = T7_real - T10_imag;
        x_imag = T7_imag + T10_real;
        out[(j + 6 * dj)] = (w6_real * x_real - w6_imag * x_imag);
        out[(j + 6 * dj + 1)] = (w6_real * x_imag + w6_imag * x_real);
        
        j += ostride;
      }
      j += (factor - 1) * dj;
    }
  }
  


  void pass_n(int fi, double[] in, int in0, int istride, double[] out, int out0, int ostride, int sign, int factor, int product)
  {
    int i = 0;int j = 0;
    

    int m = n / factor;
    int q = n / product;
    int p_1 = product / factor;
    int jump = (factor - 1) * p_1;
    

    for (i = 0; i < m; i++) {
      out[(out0 + ostride * i)] = in[(in0 + istride * i)];
      out[(out0 + ostride * i + 1)] = in[(in0 + istride * i + 1)];
    }
    for (int e = 1; e < (factor - 1) / 2 + 1; e++) {
      for (i = 0; i < m; i++) {
        int idx = i + e * m;
        int idxc = i + (factor - e) * m;
        in[(in0 + istride * idx)] += in[(in0 + istride * idxc)];
        in[(in0 + istride * idx + 1)] += in[(in0 + istride * idxc + 1)];
        in[(in0 + istride * idx)] -= in[(in0 + istride * idxc)];
        in[(in0 + istride * idx + 1)] -= in[(in0 + istride * idxc + 1)];
      }
    }
    
    for (i = 0; i < m; i++) {
      in[(in0 + istride * i)] = out[(out0 + ostride * i)];
      in[(in0 + istride * i + 1)] = out[(out0 + ostride * i + 1)];
    }
    for (int e1 = 1; e1 < (factor - 1) / 2 + 1; e1++)
      for (i = 0; i < m; i++) {
        in[(in0 + istride * i)] += out[(out0 + ostride * (i + e1 * m))];
        in[(in0 + istride * i + 1)] += out[(out0 + ostride * (i + e1 * m) + 1)];
      }
    double[] twiddl = twiddle[fi][q];
    
    for (e = 1; e < (factor - 1) / 2 + 1; e++) {
      int idx = e;
      

      int em = e * m;
      int ecm = (factor - e) * m;
      
      for (i = 0; i < m; i++) {
        in[(in0 + istride * (i + em))] = out[(out0 + ostride * i)];
        in[(in0 + istride * (i + em) + 1)] = out[(out0 + ostride * i + 1)];
        in[(in0 + istride * (i + ecm))] = out[(out0 + ostride * i)];
        in[(in0 + istride * (i + ecm) + 1)] = out[(out0 + ostride * i + 1)];
      }
      
      for (e1 = 1; e1 < (factor - 1) / 2 + 1; e1++) { double w_imag;
        double w_real; double w_imag; if (idx == 0) {
          double w_real = 1.0D;
          w_imag = 0.0D;
        } else {
          w_real = twiddl[(2 * (idx - 1))];
          w_imag = -sign * twiddl[(2 * (idx - 1) + 1)]; }
        for (i = 0; i < m; i++) {
          double ap = w_real * out[(out0 + ostride * (i + e1 * m))];
          double am = w_imag * out[(out0 + ostride * (i + (factor - e1) * m) + 1)];
          
          double bp = w_real * out[(out0 + ostride * (i + e1 * m) + 1)];
          double bm = w_imag * out[(out0 + ostride * (i + (factor - e1) * m))];
          
          in[(in0 + istride * (i + em))] += ap - am;
          in[(in0 + istride * (i + em) + 1)] += bp + bm;
          in[(in0 + istride * (i + ecm))] += ap + am;
          in[(in0 + istride * (i + ecm) + 1)] += bp - bm;
        }
        idx += e;
        idx %= factor;
      }
    }
    
    i = 0;
    j = 0;
    

    for (int k1 = 0; k1 < p_1; k1++) {
      out[(out0 + ostride * k1)] = in[(in0 + istride * k1)];
      out[(out0 + ostride * k1 + 1)] = in[(in0 + istride * k1 + 1)];
    }
    for (e1 = 1; e1 < factor; e1++) {
      for (k1 = 0; k1 < p_1; k1++) {
        out[(out0 + ostride * (k1 + e1 * p_1))] = in[(in0 + istride * (k1 + e1 * m))];
        out[(out0 + ostride * (k1 + e1 * p_1) + 1)] = in[(in0 + istride * (k1 + e1 * m) + 1)];
      }
    }
    i = p_1;
    j = product;
    
    for (int k = 1; k < q; k++) {
      for (k1 = 0; k1 < p_1; k1++) {
        out[(out0 + ostride * j)] = in[(in0 + istride * i)];
        out[(out0 + ostride * j + 1)] = in[(in0 + istride * i + 1)];
        i++;
        j++;
      }
      j += jump;
    }
    
    i = p_1;
    j = product;
    
    for (k = 1; k < q; k++) {
      twiddl = twiddle[fi][k];
      for (k1 = 0; k1 < p_1; k1++) {
        for (e1 = 1; e1 < factor; e1++) {
          double x_real = in[(in0 + istride * (i + e1 * m))];
          double x_imag = in[(in0 + istride * (i + e1 * m) + 1)];
          
          double w_real = twiddl[(2 * (e1 - 1))];
          double w_imag = -sign * twiddl[(2 * (e1 - 1) + 1)];
          
          out[(out0 + ostride * (j + e1 * p_1))] = (w_real * x_real - w_imag * x_imag);
          out[(out0 + ostride * (j + e1 * p_1) + 1)] = (w_real * x_imag + w_imag * x_real);
        }
        i++;
        j++;
      }
      j += jump;
    }
  }
}
