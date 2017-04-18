package edu.ucla.sspace.hal;

















public class LinearWeighting
  implements WeightingFunction
{
  public LinearWeighting() {}
  
















  public double weight(int positionOffset, int windowSize)
  {
    return windowSize - (Math.abs(positionOffset) - 1);
  }
}
