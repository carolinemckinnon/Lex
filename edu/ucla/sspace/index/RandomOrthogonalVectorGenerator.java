package edu.ucla.sspace.index;

import edu.ucla.sspace.vector.DenseVector;
import edu.ucla.sspace.vector.DoubleVector;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;






























































public class RandomOrthogonalVectorGenerator
  implements DoubleVectorGenerator<DoubleVector>, Serializable
{
  private static final long serialVersionUID = 1L;
  public static final Random RANDOM = new Random();
  




  private static final String PROPERTY_PREFIX = "edu.ucla.sspace.index.RandomOrthogonalVectorGenerator";
  




  public static final String VECTOR_MEAN_PROPERTY = "edu.ucla.sspace.index.RandomOrthogonalVectorGenerator.mean";
  




  public static final String VECTOR_STANDARD_DEVIATION_PROPERTY = "edu.ucla.sspace.index.RandomOrthogonalVectorGenerator.std";
  




  public static final String VECTOR_LENGTH_PROPERTY = "edu.ucla.sspace.index.RandomOrthogonalVectorGenerator.length";
  




  public static final int DEFAULT_VECTOR_MEAN = 0;
  



  public static final int DEFAULT_VECTOR_LENGTH = 1000;
  



  public static final int DEFAULT_VECTOR_STANDARD_DEVIATION = 1;
  



  private double mean;
  



  private double std;
  



  private int vectorLength;
  



  private final List<DoubleVector> generatedVectors;
  




  public RandomOrthogonalVectorGenerator(int vectorLength)
  {
    this(vectorLength, System.getProperties(), null);
  }
  




  public RandomOrthogonalVectorGenerator(int vectorLength, DoubleVector originalVector)
  {
    this(vectorLength, System.getProperties(), originalVector);
  }
  





  public RandomOrthogonalVectorGenerator(int vectorLength, Properties properties, DoubleVector originalVector)
  {
    String meanProp = 
      properties.getProperty("edu.ucla.sspace.index.RandomOrthogonalVectorGenerator.mean");
    mean = (meanProp != null ? 
      Double.parseDouble(meanProp) : 
      0.0D);
    
    String stdProp = 
      properties.getProperty("edu.ucla.sspace.index.RandomOrthogonalVectorGenerator.std");
    std = (stdProp != null ? 
      Double.parseDouble(stdProp) : 
      1.0D);
    
    this.vectorLength = vectorLength;
    
    generatedVectors = new ArrayList();
    if (originalVector == null)
      originalVector = 
        generateInitialVector(vectorLength, mean, std);
    generatedVectors.add(originalVector);
  }
  




  private static DoubleVector generateInitialVector(int length, double mean, double std)
  {
    DoubleVector vector = new DenseVector(length);
    for (int i = 0; i < length; i++) {
      double v = RANDOM.nextGaussian();
      v = std * v + mean;
      vector.set(i, v);
    }
    return vector;
  }
  



  private static double dotProduct(DoubleVector u, DoubleVector v)
  {
    double dot = 0.0D;
    for (int i = 0; i < u.length(); i++) {
      double a = u.get(i);
      double b = v.get(i);
      dot += u.get(i) * v.get(i);
    }
    return dot;
  }
  






  public DoubleVector generate()
  {
    if (generatedVectors.size() == vectorLength) {
      throw new IllegalArgumentException(
        "Too many vectors have been generated");
    }
    DoubleVector vector = 
      generateInitialVector(vectorLength, mean, std);
    int i; for (Iterator localIterator = generatedVectors.iterator(); localIterator.hasNext(); 
        

        i < vectorLength)
    {
      DoubleVector otherVector = (DoubleVector)localIterator.next();
      double uDotV = dotProduct(otherVector, vector);
      double uDotU = dotProduct(otherVector, otherVector);
      i = 0; continue;
      double projection = otherVector.get(i) * uDotV / uDotU;
      vector.set(i, vector.get(i) - projection);i++;
    }
    


    generatedVectors.add(vector);
    return vector;
  }
}
