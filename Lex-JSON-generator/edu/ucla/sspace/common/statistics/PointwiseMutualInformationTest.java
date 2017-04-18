package edu.ucla.sspace.common.statistics;

import edu.ucla.sspace.common.Statistics;





















public class PointwiseMutualInformationTest
  implements SignificanceTest
{
  public PointwiseMutualInformationTest() {}
  
  public double score(int both, int justA, int justB, int neither)
  {
    int all = both + justA + justB + neither;
    double probA = (both + justA) / all;
    double probB = (both + justB) / all;
    double probAandB = both / all;
    return Statistics.log2(probAandB / (probA * probB));
  }
}
