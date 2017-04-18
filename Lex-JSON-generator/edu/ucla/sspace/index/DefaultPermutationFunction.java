package edu.ucla.sspace.index;

import edu.ucla.sspace.vector.SparseVector;
import edu.ucla.sspace.vector.TernaryVector;
import edu.ucla.sspace.vector.Vector;
import edu.ucla.sspace.vector.Vectors;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;



































public class DefaultPermutationFunction
  implements PermutationFunction<Vector>, Serializable
{
  private static final long serialVersionUID = 1L;
  private static final Random RANDOM = RandomIndexVectorGenerator.RANDOM;
  


  private final TIntObjectMap<Function> permutationToReordering;
  



  public DefaultPermutationFunction()
  {
    permutationToReordering = new TIntObjectHashMap();
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
    
    Function function = (Function)permutationToReordering.get(exponent);
    




    if (function == null) {
      synchronized (this) {
        function = (Function)permutationToReordering.get(exponent);
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
          System.out.printf("Forward: %s%nBackward: %s%n", new Object[] {
            Arrays.toString(forwardMapping), 
            Arrays.toString(backwardMapping) });
          
          function = new Function(forwardMapping, backwardMapping);
          
          permutationToReordering.put(exponent, function);
        }
      }
    }
    
    return function;
  }
  


  public Vector permute(Vector v, int numPermutations)
  {
    if ((v instanceof TernaryVector)) {
      return permute((TernaryVector)v, numPermutations, v.length());
    }
    Vector result = Vectors.instanceOf(v);
    int[] dimensions = null;
    int[] oldDims = null;
    if ((v instanceof SparseVector)) {
      oldDims = ((SparseVector)v).getNonZeroIndices();
      dimensions = Arrays.copyOf(oldDims, oldDims.length);
    } else {
      dimensions = new int[v.length()];
      for (int i = 0; i < v.length(); i++) {
        dimensions[i] = i;
      }
    }
    System.out.printf("input: %s%ninitial result: %s%n", new Object[] { v, result });
    
    boolean isInverse = numPermutations < 0;
    


    int totalPermutations = Math.abs(numPermutations);
    
    int i;
    
    for (int count = 1; count <= totalPermutations; count++)
    {
      function = getFunction(count, v.length());
      


      reordering = isInverse ? 
        backward : forward;
      
      oldDims = Arrays.copyOf(dimensions, dimensions.length);
      
      for (i = 0; i < oldDims.length; i++) {
        dimensions[i] = reordering[oldDims[i]];
      }
    }
    
    int[] reordering = (i = dimensions).length; for (Function function = 0; function < reordering; function++) { int d = i[function];
      result.set(dimensions[d], v.getValue(d));
    }
    System.out.printf("input: %s%nfinal permuted result: %s%n", new Object[] { v, result });
    return result;
  }
  




  private Vector permute(TernaryVector v, int numPermutations, int length)
  {
    int[] oldPos = v.positiveDimensions();
    int[] oldNeg = v.negativeDimensions();
    





    int[] positive = Arrays.copyOf(oldPos, oldPos.length);
    int[] negative = Arrays.copyOf(oldNeg, oldNeg.length);
    
    boolean isInverse = numPermutations < 0;
    


    int totalPermutations = Math.abs(numPermutations);
    
    for (int count = 1; count <= totalPermutations; count++)
    {

      Function function = getFunction(count, length);
      


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
    
    return new TernaryVector(length, positive, negative);
  }
  


  public String toString()
  {
    return "DefaultPermutationFunction";
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
