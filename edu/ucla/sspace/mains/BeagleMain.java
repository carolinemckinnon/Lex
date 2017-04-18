package edu.ucla.sspace.mains;

import edu.ucla.sspace.beagle.Beagle;
import edu.ucla.sspace.beagle.Beagle.SemanticType;
import edu.ucla.sspace.common.ArgOptions;
import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.common.SemanticSpaceIO.SSpaceFormat;
import edu.ucla.sspace.index.GaussianVectorGenerator;
import edu.ucla.sspace.util.Generator;
import edu.ucla.sspace.util.GeneratorMap;
import edu.ucla.sspace.util.SerializableUtil;
import edu.ucla.sspace.vector.DoubleVector;
import java.io.File;






































































public class BeagleMain
  extends GenericMain
{
  private static final int DEFAULT_DIMENSION = 2048;
  private int dimension;
  private GeneratorMap<DoubleVector> generatorMap;
  
  private BeagleMain() {}
  
  public static void main(String[] args)
  {
    BeagleMain hMain = new BeagleMain();
    try {
      hMain.run(args);
    }
    catch (Throwable t) {
      t.printStackTrace();
    }
  }
  


  public void addExtraOptions(ArgOptions options)
  {
    options.addOption('n', "dimension", 
      "the length of each beagle vector", 
      true, "INT", "Options");
    options.addOption('s', "semanticType", 
      "The type of semantic vectors to generate", 
      true, "SemanticType", "Options");
    options.addOption('S', "saveVectors", 
      "save word-to-IndexVector mapping after processing", 
      true, "FILE", "Options");
    options.addOption('L', "loadVectors", 
      "load word-to-IndexVector mapping before processing", 
      true, "FILE", "Options");
  }
  



  public void handleExtraOptions()
  {
    dimension = (argOptions.hasOption("dimension") ? 
      argOptions.getIntOption("dimension") : 
      2048);
    if (argOptions.hasOption("loadVectors")) {
      generatorMap = ((GeneratorMap)SerializableUtil.load(
        new File(argOptions.getStringOption("loadVectors")), 
        GeneratorMap.class));
    } else {
      double stdev = 1.0D / Math.sqrt(dimension);
      System.setProperty(
        "edu.ucla.sspace.index.GuassianVectorGenerator.stdev", 
        Double.toString(stdev));
      Generator<DoubleVector> generator = 
        new GaussianVectorGenerator(dimension);
      generatorMap = new GeneratorMap(generator);
    }
  }
  


  protected void postProcessing()
  {
    if (argOptions.hasOption("saveVectors")) {
      SerializableUtil.save(
        generatorMap, 
        new File(argOptions.getStringOption("saveVectors")));
    }
  }
  


  public SemanticSpace getSpace()
  {
    Beagle.SemanticType type = argOptions.hasOption('s') ? 
      Beagle.SemanticType.valueOf(
      argOptions.getStringOption('s').toUpperCase()) : 
      Beagle.SemanticType.COMPOSITE;
    
    return new Beagle(dimension, type, generatorMap);
  }
  


  protected SemanticSpaceIO.SSpaceFormat getSpaceFormat()
  {
    return SemanticSpaceIO.SSpaceFormat.BINARY;
  }
}
