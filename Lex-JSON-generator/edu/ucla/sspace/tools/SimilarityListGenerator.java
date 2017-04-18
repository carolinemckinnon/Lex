package edu.ucla.sspace.tools;

import edu.ucla.sspace.common.ArgOptions;
import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.common.SemanticSpaceIO;
import edu.ucla.sspace.util.NearestNeighborFinder;
import edu.ucla.sspace.util.PartitioningNearestNeighborFinder;
import edu.ucla.sspace.util.SortedMultiMap;
import java.io.File;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;











































public class SimilarityListGenerator
{
  public static final int DEFAULT_SIMILAR_ITEMS = 10;
  private static final Logger LOGGER = Logger.getLogger(SimilarityListGenerator.class.getName());
  private final ArgOptions argOptions;
  
  public SimilarityListGenerator()
  {
    argOptions = new ArgOptions();
    addOptions();
  }
  



  private void addOptions()
  {
    argOptions.addOption('p', "printSimilarity", 
      "whether to print the similarity score (default: false)", 
      
      false, null, "Program Options");
    



    argOptions.addOption('n', "numSimilar", "the number of similar words to print (default: 10)", 
      true, "String", 
      "Program Options");
    argOptions.addOption('t', "threads", "the number of threads to use (default: #procesors)", 
      true, "int", 
      "Program Options");
    argOptions.addOption('w', "overwrite", "specifies whether to overwrite the existing output (default: true)", 
    
      true, "boolean", "Program Options");
    argOptions.addOption('v', "verbose", "prints verbose output (default: false)", 
      false, null, 
      "Program Options");
  }
  
  private void usage()
  {
    System.out.println("usage: java SimilarityListGenerator [options] <sspace-file> <output-dir>\n" + 
    
      argOptions.prettyPrint());
  }
  
  public static void main(String[] args) {
    try {
      SimilarityListGenerator generator = new SimilarityListGenerator();
      if (args.length == 0) {
        generator.usage();
        return;
      }
      
      generator.run(args);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public void run(String[] args)
    throws Exception
  {
    argOptions.parseOptions(args);
    
    if (argOptions.numPositionalArgs() < 2) {
      throw new IllegalArgumentException("must specify input and output");
    }
    
    File sspaceFile = new File(argOptions.getPositionalArg(0));
    File outputDir = new File(argOptions.getPositionalArg(1));
    
    if (!outputDir.isDirectory()) {
      throw new IllegalArgumentException(
        "output directory is not a directory: " + outputDir);
    }
    

    int numThreads = Runtime.getRuntime().availableProcessors();
    if (argOptions.hasOption("threads")) {
      numThreads = argOptions.getIntOption("threads");
    }
    
    boolean overwrite = true;
    if (argOptions.hasOption("overwrite")) {
      overwrite = argOptions.getBooleanOption("overwrite");
    }
    
    if (argOptions.hasOption('v'))
    {

      Logger.getLogger("edu.ucla.sspace").setLevel(Level.FINE);
    }
    

    boolean printSimilarity = argOptions.hasOption('p');
    








    int numSimilar = argOptions.hasOption('n') ? 
      argOptions.getIntOption('n') : 10;
    
    LOGGER.fine("loading .sspace file: " + sspaceFile.getName());
    
    SemanticSpace sspace = SemanticSpaceIO.load(sspaceFile);
    
    File output = null;
    if (overwrite) {
      output = new File(outputDir, 
        sspaceFile.getName() + ".similarityList");
    }
    else {
      output = File.createTempFile(sspaceFile.getName(), "similarityList", 
        outputDir);
      output.deleteOnExit();
    }
    
    PrintWriter outputWriter = new PrintWriter(output);
    
    Set<String> words = sspace.getWords();
    NearestNeighborFinder nnf = 
      new PartitioningNearestNeighborFinder(sspace);
    

    for (String word : words)
    {
      SortedMultiMap<Double, String> mostSimilar = 
        nnf.getMostSimilar(word, numSimilar);
      


      StringBuilder sb = new StringBuilder(256);
      sb.append(word).append("|");
      
      Iterator localIterator2 = mostSimilar.entrySet().iterator();
      while (localIterator2.hasNext()) {
        Map.Entry<Double, String> e = (Map.Entry)localIterator2.next();
        String s = (String)e.getValue();
        Double d = (Double)e.getKey();
        
        sb.append(s);
        if (printSimilarity) {
          sb.append(" ").append(d);
        }
        sb.append("|");
      }
      
      outputWriter.println(sb.toString());
      outputWriter.flush();
    }
  }
}
