package edu.ucla.sspace.mains;

import edu.ucla.sspace.common.ArgOptions;
import edu.ucla.sspace.common.SemanticSpaceIO.SSpaceFormat;
import edu.ucla.sspace.dependency.DefaultDependencyPermutationFunction;
import edu.ucla.sspace.dependency.DependencyPermutationFunction;
import edu.ucla.sspace.index.PermutationFunction;
import edu.ucla.sspace.index.RandomIndexVectorGenerator;
import edu.ucla.sspace.util.GeneratorMap;
import edu.ucla.sspace.util.ReflectionUtil;
import edu.ucla.sspace.vector.TernaryVector;
import edu.ucla.sspace.wordsi.DependencyContextGenerator;
import edu.ucla.sspace.wordsi.RandomIndexingDependencyContextGenerator;





























































public class DVRIWordsiMain
  extends DVWordsiMain
{
  private GeneratorMap<TernaryVector> indexMap;
  private DependencyPermutationFunction<TernaryVector> permFunc;
  
  public DVRIWordsiMain() {}
  
  protected void addExtraOptions(ArgOptions options)
  {
    super.addExtraOptions(options);
    
    options.addOption('P', "permutationFunction", 
      "Specifies the DependencyPermutationFunction for TernaryVectors that will permute index vectors before adding them to context vectors. (Default: None)", 
      


      true, "CLASSNAME", "Optional");
    options.addOption('l', "vectorLength", 
      "Specifies the length of each index vector. (Default: 5000)", 
      
      true, "CLASSNAME", "Optional");
  }
  


  protected void postProcessing()
  {
    if (argOptions.hasOption('S')) {
      saveObject(openSaveFile(), indexMap);
      saveObject(openSaveFile(), permFunc);
    }
  }
  


  protected DependencyContextGenerator getContextGenerator()
  {
    int pathLength = argOptions.getIntOption('W', 5);
    int vectorLength = argOptions.getIntOption('l', 5000);
    
    if (argOptions.hasOption('L')) {
      indexMap = ((GeneratorMap)loadObject(openLoadFile()));
      permFunc = ((DependencyPermutationFunction)loadObject(openLoadFile()));
    } else {
      indexMap = new GeneratorMap(
        new RandomIndexVectorGenerator(vectorLength));
      if (argOptions.hasOption('P')) {
        PermutationFunction<TernaryVector> basePermFunc = 
          (PermutationFunction)ReflectionUtil.getObjectInstance(
          argOptions.getStringOption('P'));
        permFunc = 
          new DefaultDependencyPermutationFunction(
          basePermFunc);
      }
    }
    

    if (argOptions.hasOption('e')) {
      throw new Error("fix me");
    }
    
    return new RandomIndexingDependencyContextGenerator(
      permFunc, getAcceptor(), indexMap, vectorLength, pathLength);
  }
  


  protected SemanticSpaceIO.SSpaceFormat getSpaceFormat()
  {
    return SemanticSpaceIO.SSpaceFormat.SPARSE_BINARY;
  }
}
