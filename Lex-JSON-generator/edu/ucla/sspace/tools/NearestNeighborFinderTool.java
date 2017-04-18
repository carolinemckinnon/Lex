package edu.ucla.sspace.tools;

import edu.ucla.sspace.common.ArgOptions;
import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.common.SemanticSpaceIO;
import edu.ucla.sspace.util.LoggerUtil;
import edu.ucla.sspace.util.MultiMap;
import edu.ucla.sspace.util.NearestNeighborFinder;
import edu.ucla.sspace.util.PartitioningNearestNeighborFinder;
import edu.ucla.sspace.util.SerializableUtil;
import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;






























public class NearestNeighborFinderTool
{
  public NearestNeighborFinderTool() {}
  
  public static void main(String[] args)
  {
    ArgOptions options = new ArgOptions();
    
    options.addOption('h', "help", "Generates a help message and exits", 
      false, null, "Program Options");
    options.addOption('v', "verbose", "Enables verbose reporting", 
      false, null, "Program Options");
    

    options.addOption('C', "createFinder", "Creates a nearest neighbor finder from the provided .sspace file", 
    
      true, "FILE", "Program Options");
    options.addOption('L', "loadFinder", "Loads the finder from file", 
      true, "FILE", "Program Options");
    options.addOption('S', "saveFinder", "Saves the loaded or created finder to file", 
      true, "FILE", "Program Options");
    
    options.addOption('p', "principleVectors", "Specifies the number of principle vectors to create", 
    
      true, "INT", "Creation Options");
    
    options.parseOptions(args);
    
    if ((options.hasOption("help")) || (
      (!options.hasOption('C')) && (!options.hasOption('L')))) {
      usage(options);
      return;
    }
    
    if (options.hasOption("verbose")) {
      LoggerUtil.setLevel(Level.FINE);
    }
    if ((options.hasOption('C')) && (options.hasOption('L'))) {
      System.out.println("Cannot load and create a finder concurrently");
      System.exit(1);
    }
    
    NearestNeighborFinder nnf = null;
    if (options.hasOption('C')) {
      try {
        SemanticSpace sspace = 
          SemanticSpaceIO.load(options.getStringOption('C'));
        int numWords = sspace.getWords().size();
        
        int numPrincipleVectors = -1;
        if (options.hasOption('p')) {
          numPrincipleVectors = options.getIntOption('p');
          if (numPrincipleVectors > numWords) {
            throw new IllegalArgumentException(
              "Cannot have more principle vectors than word vectors: " + 
              numPrincipleVectors);
          }
          if (numPrincipleVectors < 1) {
            throw new IllegalArgumentException(
              "Must have at least one principle vector");
          }
        }
        else
        {
          numPrincipleVectors = 
            Math.min((int)Math.ceil(Math.log(numWords)), 1000);
          System.err.printf("Choosing a heuristically selected %d principle vectors%n", new Object[] {
          
            Integer.valueOf(numPrincipleVectors) });
        }
        nnf = new PartitioningNearestNeighborFinder(
          sspace, numPrincipleVectors);
      } catch (IOException ioe) {
        throw new IOError(ioe);
      }
      
    } else if (options.hasOption('L')) {
      nnf = (NearestNeighborFinder)SerializableUtil.load(
        new File(options.getStringOption('L')));
    }
    else {
      throw new IllegalArgumentException(
        "Must either create or load a NearestNeighborFinder");
    }
    
    if (options.hasOption('S')) {
      SerializableUtil.save(nnf, new File(options.getStringOption('S')));
    }
    
    int numWords = options.numPositionalArgs();
    for (int i = 0; i < numWords; i++) {
      String term = options.getPositionalArg(i);
      long start = System.currentTimeMillis();
      MultiMap<Double, String> m = nnf.getMostSimilar(term, 10);
      if (m == null) {
        System.out.println(term + " is not in the semantic " + 
          "space; no neighbors found.");
      }
      else {
        long time = System.currentTimeMillis() - start;
        

        System.out.println(term);
        for (Map.Entry<Double, String> e : m.entrySet()) {
          System.out.println((String)e.getValue() + "\t" + e.getKey());
        }
      }
    }
  }
  



  private static void usage(ArgOptions options)
  {
    System.out.println(
      "NearestNeighborFinder Tool version 1.0\nusage: java -jar nnf.jar [options] [word1 word2...]\n\n" + 
      
      options.prettyPrint() + 
      "The primary purpose of this tool is the build " + 
      "instances of the\n" + 
      "NearestNeighborFinder class from an existing .sspace " + 
      "file.  An example command\n" + 
      "line would look like:\n" + 
      "\n" + 
      "java -jar nnf.jar --createFinder my.sspace " + 
      "--saveFinder my.nnf.ser --principleVectors 1000\n" + 
      "\n" + 
      "However, it may also be used with an existing " + 
      "serialized NearestNeighborFinder\n" + 
      "instance to search for the nearest neighbors words, " + 
      "which are reported to stdout:\n" + 
      "\n" + 
      "java -jar tools/nnf.jar --loadFinder my.nnf.ser " + 
      "word1 word2 word3");
  }
}
