package edu.ucla.sspace.index;

import edu.ucla.sspace.vector.DenseVector;
import edu.ucla.sspace.vector.DoubleVector;
import java.io.Serializable;
import java.util.Properties;
import java.util.Random;




















































































public class GaussianVectorGenerator
  implements DoubleVectorGenerator<DoubleVector>, Serializable
{
  private static final long serialVersionUID = 1L;
  private static final String PROPERTY_PREFIX = "edu.ucla.sspace.index.GuassianVectorGenerator";
  public static final String STANDARD_DEVIATION_PROPERTY = "edu.ucla.sspace.index.GuassianVectorGenerator.stdev";
  public static final String MEAN_PROPERTY = "edu.ucla.sspace.index.GuassianVectorGenerator.mean";
  public static final double DEFAULT_STANDARD_DEVIATION = 1.0D;
  public static final String DEFAULT_MEAN = "0";
  private double stdev;
  private final int indexVectorLength;
  private double mean;
  private Random randomGenerator;
  
  public GaussianVectorGenerator(int indexVectorLength)
  {
    this(indexVectorLength, System.getProperties());
  }
  







  public GaussianVectorGenerator(int indexVectorLength, Properties prop)
  {
    randomGenerator = new Random();
    
    this.indexVectorLength = indexVectorLength;
    
    String stdevProp = prop.getProperty(
      "edu.ucla.sspace.index.GuassianVectorGenerator.stdev");
    stdev = (stdevProp != null ? 
      Double.parseDouble(stdevProp) : 
      1.0D);
    mean = Double.parseDouble(prop.getProperty(
      "edu.ucla.sspace.index.GuassianVectorGenerator.mean", "0"));
  }
  



  public synchronized DoubleVector generate()
  {
    DoubleVector termVector = new DenseVector(indexVectorLength);
    for (int i = 0; i < indexVectorLength; i++)
      termVector.set(i, mean + randomGenerator.nextGaussian() * stdev);
    return termVector;
  }
}
