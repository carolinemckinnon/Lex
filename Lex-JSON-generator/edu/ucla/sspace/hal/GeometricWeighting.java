package edu.ucla.sspace.hal;

















public class GeometricWeighting
  implements WeightingFunction
{
  public GeometricWeighting() {}
  
















  public double weight(int positionOffset, int windowSize)
  {
    return (1 << windowSize - (Math.abs(positionOffset) - 1)) / (
      1 << windowSize) * windowSize;
  }
}
