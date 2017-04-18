package edu.ucla.sspace.mains;

import edu.ucla.sspace.common.ArgOptions;
import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.common.SemanticSpaceIO.SSpaceFormat;
import edu.ucla.sspace.dependency.DefaultDependencyPermutationFunction;
import edu.ucla.sspace.dependency.DependencyPermutationFunction;
import edu.ucla.sspace.dri.DependencyRandomIndexing;
import edu.ucla.sspace.index.PermutationFunction;
import edu.ucla.sspace.index.TernaryPermutationFunction;
import edu.ucla.sspace.util.GeneratorMap;
import edu.ucla.sspace.util.SerializableUtil;
import edu.ucla.sspace.vector.TernaryVector;
import java.io.File;
import java.lang.reflect.Constructor;



















































































public class DependencyRandomIndexingMain
  extends DependencyGenericMain
{
  private DependencyRandomIndexing dri;
  
  public DependencyRandomIndexingMain() {}
  
  public void addExtraOptions(ArgOptions options)
  {
    super.addExtraOptions(options);
    




    options.addOption('l', "vectorLength", 
      "The size of the vectors", 
      true, "INT", "Process Properties");
    options.addOption('s', "windowSize", 
      "The maximum number of link in a dependency path to accept", 
      
      true, "INT", "Process Properties");
    options.addOption('P', "usePermutations", 
      "Set if permutations should be used", 
      false, null, "Process Properties");
    options.addOption('p', "permutationFunction", 
      "The DependencyPermutationFunction to use.", 
      true, "CLASSNAME", "Process Properties");
    options.addOption('a', "pathAcceptor", 
      "The DependencyPathAcceptor to use", 
      true, "CLASSNAME", "Optional");
    options.addOption('W', "pathWeighter", 
      "The DependencyPathWeight to use", 
      true, "CLASSNAME", "Optional");
    

    options.addOption('S', "saveIndexes", 
      "Save index vectors and permutation function to a binary file", 
      
      true, "FILE", "Post Processing");
    options.addOption('L', "loadIndexes", 
      "Load index vectors and permutation function from binary files", 
      
      true, "FILE", "Pre Processing");
  }
  



  private DependencyPermutationFunction getPermutationFunction()
  {
    try
    {
      if (!argOptions.hasOption('P')) {
        return null;
      }
      if (!argOptions.hasOption('p'))
        return new DefaultDependencyPermutationFunction(
          new TernaryPermutationFunction());
      Class clazz = Class.forName(argOptions.getStringOption('p'));
      Constructor<?> c = clazz.getConstructor(new Class[] { PermutationFunction.class });
      return 
        (DependencyPermutationFunction)c.newInstance(new Object[] { new TernaryPermutationFunction() });
    } catch (Exception e) {
      throw new Error(e);
    }
  }
  



  protected void handleExtraOptions()
  {
    if (argOptions.hasOption("vectorLength")) {
      System.setProperty("edu.ucla.sspace.dri.DependencyRandomIndexing.indexVectorLength", 
        argOptions.getStringOption("vectorLength"));
    }
    if (argOptions.hasOption("windowSize")) {
      System.setProperty(
        "edu.ucla.sspace.dri.DependencyRandomIndexing.dependencyPathLength", 
        argOptions.getStringOption("windowSize"));
    }
    if (argOptions.hasOption("pathAcceptor")) {
      System.setProperty(
        "edu.ucla.sspace.dri.DependencyRandomIndexing.dependencyAcceptor", 
        argOptions.getStringOption("pathAcceptor"));
    }
    DependencyPermutationFunction<TernaryVector> permFunction = null;
    
    if ((argOptions.hasOption("loadIndexes")) && 
      (argOptions.hasOption("usePermutations"))) {
      permFunction = 
        (DependencyPermutationFunction)SerializableUtil.load(
        new File(argOptions.getStringOption("loadIndexes") + 
        ".permutation"));
    } else {
      permFunction = getPermutationFunction();
    }
    

    setupDependencyExtractor();
    
    dri = new DependencyRandomIndexing(permFunction, System.getProperties());
    if (argOptions.hasOption("loadIndexes")) {
      String savedIndexName = argOptions.getStringOption("loadIndexes");
      dri.setWordToVectorMap(
        (GeneratorMap)SerializableUtil.load(new File(savedIndexName + ".index"), 
        GeneratorMap.class));
    }
  }
  


  protected void postProcessing()
  {
    if (argOptions.hasOption("saveIndexes")) {
      String filename = argOptions.getStringOption("saveIndexes");
      SerializableUtil.save(dri.getWordToVectorMap(), 
        new File(filename + ".index"));
      SerializableUtil.save(dri.getPermutations(), 
        new File(filename + ".permutation"));
    }
  }
  


  public SemanticSpace getSpace()
  {
    return dri;
  }
  


  public static void main(String[] args)
  {
    DependencyRandomIndexingMain drim = new DependencyRandomIndexingMain();
    try {
      drim.run(args);
    }
    catch (Throwable t) {
      t.printStackTrace();
    }
  }
  


  protected SemanticSpaceIO.SSpaceFormat getSpaceFormat()
  {
    return SemanticSpaceIO.SSpaceFormat.SPARSE_BINARY;
  }
}
