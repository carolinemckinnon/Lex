package edu.ucla.sspace.index;

import edu.ucla.sspace.vector.TernaryVector;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Random;


























































public class RandomIndexVectorGenerator
  implements IntegerVectorGenerator<TernaryVector>, Serializable
{
  private static final long serialVersionUID = 1L;
  public static final Random RANDOM = new Random();
  




  private static final String PROPERTY_PREFIX = "edu.ucla.sspace.index.RandomIndexVectorGenerator";
  




  public static final String VALUES_TO_SET_PROPERTY = "edu.ucla.sspace.index.RandomIndexVectorGenerator.values";
  



  public static final String INDEX_VECTOR_VARIANCE_PROPERTY = "edu.ucla.sspace.index.RandomIndexVectorGenerator.variance";
  



  public static final int DEFAULT_INDEX_VECTOR_VALUES = 4;
  



  public static final int DEFAULT_INDEX_VECTOR_LENGTH = 20000;
  



  public static final int DEFAULT_INDEX_VECTOR_VARIANCE = 0;
  



  private int numVectorValues;
  



  private int variance;
  



  private int indexVectorLength;
  




  public RandomIndexVectorGenerator(int indexVectorLength)
  {
    this(indexVectorLength, System.getProperties());
  }
  



  public RandomIndexVectorGenerator(int indexVectorLength, Properties properties)
  {
    this.indexVectorLength = indexVectorLength;
    
    String numVectorValuesProp = 
      properties.getProperty("edu.ucla.sspace.index.RandomIndexVectorGenerator.values");
    numVectorValues = (numVectorValuesProp != null ? 
      Integer.parseInt(numVectorValuesProp) : 
      4);
    
    String varianceProp = 
      properties.getProperty("edu.ucla.sspace.index.RandomIndexVectorGenerator.variance");
    variance = (varianceProp != null ? 
      Integer.parseInt(varianceProp) : 
      0);
  }
  





  public RandomIndexVectorGenerator(int indexVectorLength, int numVectorValues, int variance, int randSeed)
  {
    this.indexVectorLength = indexVectorLength;
    this.numVectorValues = numVectorValues;
    this.variance = variance;
    RANDOM.setSeed(randSeed);
  }
  






  public TernaryVector generate()
  {
    HashSet<Integer> pos = new HashSet();
    HashSet<Integer> neg = new HashSet();
    


    int bitsToSet = numVectorValues + 
      (int)(RANDOM.nextDouble() * variance * (
      RANDOM.nextDouble() > 0.5D ? 1 : -1));
    
    for (int i = 0; i < bitsToSet; i++) {
      boolean picked = false;
      
      while (!picked)
      {
        int index = RANDOM.nextInt(indexVectorLength);
        

        if ((!pos.contains(Integer.valueOf(index))) && (!neg.contains(Integer.valueOf(index))))
        {


          (RANDOM.nextDouble() > 0.5D ? pos : neg).add(Integer.valueOf(index));
          picked = true;
        }
      }
    }
    int[] positive = new int[pos.size()];
    int[] negative = new int[neg.size()];
    
    Iterator<Integer> it = pos.iterator();
    for (int i = 0; i < positive.length; i++) {
      positive[i] = ((Integer)it.next()).intValue();
    }
    it = neg.iterator();
    for (int i = 0; i < negative.length; i++) {
      negative[i] = ((Integer)it.next()).intValue();
    }
    
    Arrays.sort(positive);
    Arrays.sort(negative);
    return new TernaryVector(indexVectorLength, positive, negative);
  }
}
