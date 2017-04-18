package edu.ucla.sspace.common.statistics;












public class GTest
  implements SignificanceTest
{
  public GTest() {}
  










  public double score(int both, int justA, int justB, int neither)
  {
    int all = both + justA + justB + neither;
    double probA = (both + justA) / all;
    double probB = (both + justB) / all;
    
    double expectedBoth = probA * probB * all;
    
    return 2.0D * (both * Math.log(both / expectedBoth));
  }
}
