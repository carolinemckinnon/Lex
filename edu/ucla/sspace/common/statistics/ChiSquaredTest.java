package edu.ucla.sspace.common.statistics;











public class ChiSquaredTest
  implements SignificanceTest
{
  public ChiSquaredTest() {}
  










  public double score(int both, int justA, int justB, int neither)
  {
    int col1sum = both + justA;
    int col2sum = justB + neither;
    int row1sum = both + justB;
    int row2sum = justA + neither;
    double sum = row1sum + row2sum;
    

    double aExp = row1sum / sum * col1sum;
    double bExp = row1sum / sum * col2sum;
    double cExp = row2sum / sum * col1sum;
    double dExp = row2sum / sum * col2sum;
    

    return 
      (both - aExp) * (both - aExp) / aExp + 
      (justB - bExp) * (justB - bExp) / bExp + 
      (justA - cExp) * (justA - cExp) / cExp + 
      (neither - dExp) * (neither - dExp) / dExp;
  }
}
