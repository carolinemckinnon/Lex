package edu.ucla.sspace.common.statistics;












public class LogLikelihoodTest
  implements SignificanceTest
{
  public LogLikelihoodTest() {}
  










  public double score(int both, int justA, int justB, int neither)
  {
    double rowEntropy = entropy(both, justA) + entropy(justB, neither);
    double columnEntropy = entropy(both, justB) + entropy(justA, neither);
    double matrixEntropy = entropy(both, justA, justB, neither);
    return 2.0D * (matrixEntropy - rowEntropy - columnEntropy);
  }
  
  private static double entropy(int x, int y) {
    assert ((x >= 0) && (y >= 0)) : "negative counts";
    double sum = x + y;
    
    double result = 0.0D;
    result += (x == 0 ? 0.0D : x * (Math.log(x) / sum));
    result += (y == 0 ? 0.0D : y * (Math.log(y) / sum));
    return -result;
  }
  
  private static double entropy(int a, int b, int c, int d) {
    assert ((a >= 0) && (b >= 0) && (c >= 0) && (d >= 0)) : "negative counts";
    double sum = a + b + c + d;
    
    double result = 0.0D;
    result += (a == 0 ? 0.0D : a * (Math.log(a) / sum));
    result += (b == 0 ? 0.0D : b * (Math.log(b) / sum));
    result += (c == 0 ? 0.0D : c * (Math.log(c) / sum));
    result += (d == 0 ? 0.0D : d * (Math.log(d) / sum));
    return -result;
  }
}
