package jnt.FFT;







public class Factorize
{
  public Factorize() {}
  





  public static int[] factor(int n, int[] fromfactors)
  {
    int[] factors = new int[64];
    int nf = 0;
    int ntest = n;
    

    if (n <= 0) {
      throw new Error("Number (" + n + ") must be positive integer");
    }
    
    for (int i = 0; (i < fromfactors.length) && (ntest != 1); i++) {
      int factor = fromfactors[i];
      while (ntest % factor == 0) {
        ntest /= factor;
        factors[(nf++)] = factor;
      }
    }
    int factor = 2;
    while ((ntest % factor == 0) && (ntest != 1)) {
      ntest /= factor;
      factors[(nf++)] = factor;
    }
    
    factor = 3;
    while (ntest != 1) {
      while (ntest % factor != 0)
        factor += 2;
      ntest /= factor;
      factors[(nf++)] = factor;
    }
    
    int product = 1;
    for (int i = 0; i < nf; i++)
      product *= factors[i];
    if (product != n) {
      throw new Error("factorization failed for " + n);
    }
    
    int[] f = new int[nf];
    System.arraycopy(factors, 0, f, 0, nf);
    return f;
  }
  
  public static int log2(int n) {
    int log = 0;
    
    for (int k = 1; k < n; log++) { k *= 2;
    }
    if (n != 1 << log)
      return -1;
    return log;
  }
}
