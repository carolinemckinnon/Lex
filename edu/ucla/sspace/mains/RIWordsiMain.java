package edu.ucla.sspace.mains;

import edu.ucla.sspace.common.ArgOptions;
import edu.ucla.sspace.common.SemanticSpaceIO.SSpaceFormat;
import edu.ucla.sspace.index.PermutationFunction;
import edu.ucla.sspace.index.RandomIndexVectorGenerator;
import edu.ucla.sspace.util.GeneratorMap;
import edu.ucla.sspace.util.ReflectionUtil;
import edu.ucla.sspace.vector.TernaryVector;
import edu.ucla.sspace.wordsi.ContextExtractor;
import edu.ucla.sspace.wordsi.ContextGenerator;
import edu.ucla.sspace.wordsi.RandomIndexingContextGenerator;
import java.util.Map;


















































































public class RIWordsiMain
  extends GenericWordsiMain
{
  private PermutationFunction<TernaryVector> permFunction;
  private Map<String, TernaryVector> indexMap;
  private int indexVectorLength;
  
  public RIWordsiMain() {}
  
  protected void addExtraOptions(ArgOptions options)
  {
    super.addExtraOptions(options);
    
    options.addOption('p', "permutationFunction", 
      "Specifies the permutation function to apply on index vectors. (Default: none)", 
      
      true, "CLASSNAME", "Optional");
    
    options.addOption('l', "indexVectorLength", 
      "Specifies the length of index vectors.", 
      true, "CLASSNAME", "Required");
  }
  


  protected void handleExtraOptions()
  {
    indexVectorLength = argOptions.getIntOption('l');
    

    if (argOptions.hasOption('p')) {
      permFunction = ((PermutationFunction)ReflectionUtil.getObjectInstance(
        argOptions.getStringOption('p')));
    }
    
    if (argOptions.hasOption('L')) {
      indexMap = ((Map)loadObject(openLoadFile()));
    } else {
      indexMap = new GeneratorMap(new RandomIndexVectorGenerator(
        indexVectorLength));
    }
  }
  



  protected void postProcessing()
  {
    if (argOptions.hasOption('S')) {
      saveObject(openSaveFile(), indexMap);
    }
  }
  


  protected ContextExtractor getExtractor()
  {
    ContextGenerator generator = new RandomIndexingContextGenerator(
      indexMap, permFunction, indexVectorLength);
    return contextExtractorFromGenerator(generator);
  }
  


  protected SemanticSpaceIO.SSpaceFormat getSpaceFormat()
  {
    return SemanticSpaceIO.SSpaceFormat.SPARSE_TEXT;
  }
  
  public static void main(String[] args) throws Exception {
    RIWordsiMain main = new RIWordsiMain();
    main.run(args);
  }
}
