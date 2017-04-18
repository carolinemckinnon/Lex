package edu.ucla.sspace.index;

import edu.ucla.sspace.vector.Vector;
import java.util.Properties;



































































public class WindowedPermutationFunction
  implements PermutationFunction<Vector>
{
  public static final String PROPERTY_PREFIX = "edu.ucla.sspace.index.WindowedPermutationFunction";
  public static final String WINDOW_LIMIT_PROPERTY = "edu.ucla.sspace.index.WindowedPermutationFunction.window";
  public static final String DEFAULT_WINDOW_LIMIT = "1";
  private final PermutationFunction<Vector> function;
  private final int windowSize;
  
  public WindowedPermutationFunction()
  {
    this(System.getProperties());
  }
  



  public WindowedPermutationFunction(Properties props)
  {
    function = new DefaultPermutationFunction();
    windowSize = Integer.parseInt(props.getProperty(
      "edu.ucla.sspace.index.WindowedPermutationFunction.window", "1"));
  }
  


  public Vector permute(Vector v, int numPermutations)
  {
    return function.permute(v, numPermutations / windowSize);
  }
}
