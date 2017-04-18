package edu.ucla.sspace.index;

import edu.ucla.sspace.vector.TernaryVector;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;




































public class TernaryPermutationFunction
  implements PermutationFunction<TernaryVector>, Serializable
{
  private static final long serialVersionUID = 1L;
  private static final Random RANDOM = RandomIndexVectorGenerator.RANDOM;
  


  private final Map<Integer, Function> permutationToReordering;
  



  public TernaryPermutationFunction()
  {
    permutationToReordering = new HashMap();
  }
  











  private Function getFunction(int exponent, int dimensions)
  {
    if (exponent == 0) {
      int[] func = new int[dimensions];
      for (int i = 0; i < dimensions; i++) {
        func[i] = i;
      }
      return new Function(func, func);
    }
    
    exponent = Math.abs(exponent);
    
    Function function = (Function)permutationToReordering.get(Integer.valueOf(exponent));
    




    if (function == null) {
      synchronized (this) {
        function = (Function)permutationToReordering.get(Integer.valueOf(exponent));
        if (function == null)
        {
          int priorExponent = exponent - 1;
          Function priorFunc = 
            getFunction(priorExponent, dimensions);
          


          Integer[] objFunc = new Integer[dimensions];
          for (int i = 0; i < dimensions; i++) {
            objFunc[i] = Integer.valueOf(forward[i]);
          }
          

          List<Integer> list = Arrays.asList(objFunc);
          Collections.shuffle(list, RANDOM);
          

          int[] forwardMapping = new int[dimensions];
          int[] backwardMapping = new int[dimensions];
          for (int i = 0; i < dimensions; i++) {
            forwardMapping[i] = objFunc[i].intValue();
            backwardMapping[objFunc[i].intValue()] = i;
          }
          function = new Function(forwardMapping, backwardMapping);
          
          permutationToReordering.put(Integer.valueOf(exponent), function);
        }
      }
    }
    
    return function;
  }
  





  public TernaryVector permute(TernaryVector v, int numPermutations)
  {
    int[] oldPos = v.positiveDimensions();
    int[] oldNeg = v.negativeDimensions();
    





    int[] positive = Arrays.copyOf(oldPos, oldPos.length);
    int[] negative = Arrays.copyOf(oldNeg, oldNeg.length);
    
    boolean isInverse = numPermutations < 0;
    


    int totalPermutations = Math.abs(numPermutations);
    
    for (int count = 1; count <= totalPermutations; count++)
    {

      Function function = getFunction(count, v.length());
      


      int[] reordering = isInverse ? 
        backward : forward;
      



      oldPos = Arrays.copyOf(positive, positive.length);
      oldNeg = Arrays.copyOf(negative, negative.length);
      





      for (int i = 0; i < oldPos.length; i++) {
        positive[i] = reordering[oldPos[i]];
      }
      
      for (int i = 0; i < oldNeg.length; i++) {
        negative[i] = reordering[oldNeg[i]];
      }
    }
    
    return new TernaryVector(v.length(), positive, negative);
  }
  


  public String toString()
  {
    return "TernaryPermutationFunction";
  }
  

  private static class Function
    implements Serializable
  {
    private static final long serialVersionUID = 1L;
    
    private final int[] forward;
    private final int[] backward;
    
    public Function(int[] forward, int[] backward)
    {
      this.forward = forward;
      this.backward = backward;
    }
  }
}
