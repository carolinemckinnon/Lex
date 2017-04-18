package edu.ucla.sspace.hal;
















public class EvenWeighting
  implements WeightingFunction
{
  public EvenWeighting() {}
  















  public double weight(int positionOffset, int windowSize)
  {
    return 1.0D;
  }
}
