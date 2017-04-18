package edu.ucla.sspace.tools;

import edu.ucla.sspace.common.ArgOptions;
import edu.ucla.sspace.common.DimensionallyInterpretableSemanticSpace;
import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.common.SemanticSpaceIO;
import edu.ucla.sspace.common.Similarity;
import edu.ucla.sspace.common.Similarity.SimType;
import edu.ucla.sspace.util.NearestNeighborFinder;
import edu.ucla.sspace.util.PartitioningNearestNeighborFinder;
import edu.ucla.sspace.util.SortedMultiMap;
import edu.ucla.sspace.vector.SparseVector;
import edu.ucla.sspace.vector.Vector;
import edu.ucla.sspace.vector.VectorIO;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



































public class SemanticSpaceExplorer
{
  private static final Map<String, Command> abbreviatedCommands;
  private final Map<String, SemanticSpace> fileNameToSSpace;
  private final Map<String, String> aliasToFileName;
  private SemanticSpace current;
  private NearestNeighborFinder currentNnf;
  
  private static enum Command
  {
    LOAD, 
    UNLOAD, 
    GET_NEIGHBORS, 
    GET_SIMILARITY, 
    COMPARE_SSPACE_VECTORS, 
    HELP, 
    WRITE_COMMAND_RESULTS, 
    SET_CURRENT_SSPACE, 
    GET_CURRENT_SSPACE, 
    PRINT_VECTOR, 
    ALIAS, 
    GET_WORDS, 
    DESCRIBE_DIMENSION, 
    DESCRIBE_SEMANTIC_SPACE;
  }
  


  static
  {
    abbreviatedCommands = 
      new HashMap();
    



    for (Command c : Command.values()) {
      String[] commandWords = c.toString().split("_");
      StringBuilder abbv = new StringBuilder();
      for (String w : commandWords)
        abbv.append(w.charAt(0));
      abbreviatedCommands.put(abbv.toString().toLowerCase(), c);
    }
  }
  

























  private SemanticSpaceExplorer()
  {
    fileNameToSSpace = new LinkedHashMap();
    aliasToFileName = new HashMap();
    current = null;
  }
  








  private String getCurrentSSpaceFileName()
  {
    for (Map.Entry<String, SemanticSpace> e : fileNameToSSpace.entrySet()) {
      if (e.getValue() == current) {
        return (String)e.getKey();
      }
    }
    return null;
  }
  








  private SemanticSpace getSSpace(String name)
  {
    String aliased = (String)aliasToFileName.get(name);
    return aliased != null ? 
      (SemanticSpace)fileNameToSSpace.get(aliased) : 
      (SemanticSpace)fileNameToSSpace.get(name);
  }
  








  public boolean execute(Iterator<String> commandTokens)
  {
    return execute(commandTokens, System.out);
  }
  












  private boolean execute(Iterator<String> commandTokens, PrintStream out)
  {
    if (!commandTokens.hasNext()) {
      return false;
    }
    
    String commandStr = (String)commandTokens.next();
    Command command = null;
    try {
      command = 
        Command.valueOf(commandStr.replaceAll("-", "_").toUpperCase());
    } catch (IllegalArgumentException iae) {
      command = (Command)abbreviatedCommands.get(commandStr);
      if (command == null) {
        out.println("Unknown command: " + commandStr);
        return false;
      }
    }
    
    Map.Entry<String, String> e;
    String name2;
    switch (command)
    {

    case ALIAS: 
      if (!commandTokens.hasNext()) {
        out.println("missing .sspace file argument");
        return false;
      }
      String sspaceFileName = (String)commandTokens.next();
      

      if (!fileNameToSSpace.containsKey(sspaceFileName))
      {

        SemanticSpace sspace = null;
        try {
          sspace = SemanticSpaceIO.load(sspaceFileName);
        }
        catch (Throwable t) {
          out.println("an error occurred while loading the semantic space from " + 
            sspaceFileName + ":\n" + t);
          t.printStackTrace();
        }
        fileNameToSSpace.put(sspaceFileName, sspace);
        current = sspace;
        currentNnf = null; }
      break;
    



    case COMPARE_SSPACE_VECTORS: 
      if (!commandTokens.hasNext()) {
        out.println("missing .sspace file argument");
        return false;
      }
      String sspaceName = (String)commandTokens.next();
      String aliased = (String)aliasToFileName.get(sspaceName);
      SemanticSpace removed = null;
      if (aliased != null) {
        aliasToFileName.remove(sspaceName);
        removed = (SemanticSpace)fileNameToSSpace.remove(aliased);
      }
      else {
        removed = (SemanticSpace)fileNameToSSpace.remove(sspaceName);
        
        Iterator<Map.Entry<String, String>> it = 
          aliasToFileName.entrySet().iterator();
        while (it.hasNext()) {
          e = (Map.Entry)it.next();
          if (((String)e.getValue()).equals(sspaceName)) {
            it.remove();
            break;
          }
        }
      }
      


      if (removed == current) {
        Iterator<SemanticSpace> it = 
          fileNameToSSpace.values().iterator();
        current = (it.hasNext() ? (SemanticSpace)it.next() : null);
      }
      break;
    



    case PRINT_VECTOR: 
      if (!commandTokens.hasNext()) {
        out.println("missing .sspace file argument");
        return false;
      }
      String fileName = (String)commandTokens.next();
      if (!fileNameToSSpace.containsKey(fileName)) {
        out.println(fileName + "is not currently loaded");
        return false;
      }
      if (!commandTokens.hasNext()) {
        out.println("missing alias name");
        return false;
      }
      String alias = (String)commandTokens.next();
      aliasToFileName.put(alias, fileName);
      break;
    


    case DESCRIBE_DIMENSION: 
      if (!commandTokens.hasNext()) {
        out.println("missing word argument");
        return false;
      }
      String focusWord = (String)commandTokens.next();
      
      int neighbors = 10;
      if (commandTokens.hasNext()) {
        String countStr = (String)commandTokens.next();
        try {
          neighbors = Integer.parseInt(countStr);
        } catch (NumberFormatException nfe) {
          out.println("invalid number of neighbors: " + countStr);
          return false;
        }
      }
      


      if (currentNnf == null) {
        currentNnf = new PartitioningNearestNeighborFinder(current);
      }
      

      SortedMultiMap<Double, String> mostSimilar = 
        currentNnf.getMostSimilar(focusWord, neighbors);
      
      if (mostSimilar == null) {
        out.println(focusWord + 
          " is not in the current semantic space");
      }
      else
      {
        for (Map.Entry<Double, String> e : mostSimilar.entrySet()) {
          out.println((String)e.getValue() + "\t" + e.getKey());
        }
      }
      break;
    


    case DESCRIBE_SEMANTIC_SPACE: 
      if (current == null) {
        out.println("no current semantic space");
        return false;
      }
      
      if (!commandTokens.hasNext()) {
        out.println("missing word argument");
        return false;
      }
      String word1 = (String)commandTokens.next();
      
      if (!commandTokens.hasNext()) {
        out.println("missing word argument");
        return false;
      }
      String word2 = (String)commandTokens.next();
      
      Similarity.SimType simType = Similarity.SimType.COSINE;
      if (commandTokens.hasNext())
      {
        String simTypeStr = ((String)commandTokens.next()).toUpperCase();
        try {
          simType = Similarity.SimType.valueOf(simTypeStr);
        }
        catch (IllegalArgumentException iae)
        {
          for (Similarity.SimType t : Similarity.SimType.values()) {
            if (t.name().startsWith(simTypeStr))
              simType = t;
          }
          if (simType == null) {
            out.println("invalid similarity measure: " + simTypeStr);
            return false;
          }
        }
      }
      
      Vector word1vec = current.getVector(word1);
      if (word1vec == null) {
        out.println(word1 + " is not in semantic space " + 
          getCurrentSSpaceFileName());
      }
      else {
        Vector word2vec = current.getVector(word2);
        if (word2vec == null) {
          out.println(word2 + " is not in semantic space " + 
            getCurrentSSpaceFileName());
        }
        else
        {
          double similarity = 
            Similarity.getSimilarity(simType, word1vec, word2vec);
          out.println(similarity); } }
      break;
    




    case GET_CURRENT_SSPACE: 
      if (!commandTokens.hasNext()) {
        out.println("missing word argument");
        return false;
      }
      String word = (String)commandTokens.next();
      
      if (!commandTokens.hasNext()) {
        out.println("missing sspace argument");
        return false;
      }
      String name1 = (String)commandTokens.next();
      SemanticSpace sspace1 = getSSpace(name1);
      if (sspace1 == null) {
        out.println("no such semantic space: " + name1);
        return false;
      }
      
      if (!commandTokens.hasNext()) {
        out.println("missing sspace argument");
        return false;
      }
      name2 = (String)commandTokens.next();
      SemanticSpace sspace2 = getSSpace(name2);
      if (sspace2 == null) {
        out.println("no such semantic space: " + name2);
        return false;
      }
      
      Similarity.SimType simType = Similarity.SimType.COSINE;
      if (commandTokens.hasNext()) {
        String simTypeStr = (String)commandTokens.next();
        try {
          simType = Similarity.SimType.valueOf(simTypeStr);
        } catch (IllegalArgumentException iae) {
          out.println("invalid similarity measure: " + simTypeStr);
          return false;
        }
      }
      

      Vector sspace1vec = sspace1.getVector(word);
      if (sspace1vec == null) {
        out.println(word + " is not in semantic space " + 
          name1);
      }
      else {
        Vector sspace2vec = sspace2.getVector(word);
        if (sspace2vec == null) {
          out.println(word + " is not in semantic space " + 
            name2);



        }
        else if (sspace1vec.length() != sspace2vec.length()) {
          out.println(name1 + " and " + name2 + " have different numbers " + 
            "of dimensions and are not comparable.");
        }
        else
        {
          double similarity = 
            Similarity.getSimilarity(simType, sspace1vec, sspace2vec);
          out.println(similarity); } }
      break;
    

    case GET_NEIGHBORS: 
      out.println("available commands:\n" + getCommands());
      break;
    


    case GET_SIMILARITY: 
      if (!commandTokens.hasNext()) {
        out.println("missing file destination argument");
        return false;
      }
      String fileName = (String)commandTokens.next();
      
      try
      {
        PrintStream ps = new PrintStream(fileName);
        

        execute(commandTokens, ps);
        ps.close();
      } catch (IOException ioe) {
        out.println("An error occurred while writing to " + fileName + 
          ":\n" + ioe);
      }
    



    case LOAD: 
      if (current == null) {
        out.println("no current semantic space");
        return false;
      }
      
      if (!commandTokens.hasNext()) {
        out.println("missing word argument");
        return false;
      }
      String word = (String)commandTokens.next();
      
      Vector vec = current.getVector(word);
      if (vec == null) {
        out.println(word + " is not in semantic space " + 
          getCurrentSSpaceFileName());
      }
      else
      {
        out.println(VectorIO.toString(vec)); }
      break;
    


    case GET_WORDS: 
      if (!commandTokens.hasNext()) {
        out.println("missing .sspace file argument");
        return false;
      }
      String spaceName = (String)commandTokens.next();
      
      String fileName = (String)aliasToFileName.get(spaceName);
      
      if (fileName == null) {
        fileName = spaceName;
      }
      SemanticSpace s = (SemanticSpace)fileNameToSSpace.get(fileName);
      if (s == null) {
        out.println("no such .sspace (file is not currently loaded)");
        return false;
      }
      current = s;
      break;
    


    case HELP: 
      String currentSpaceName = getCurrentSSpaceFileName();
      if (currentSpaceName != null) {
        out.println(currentSpaceName);
      } else
        out.println("none");
      break;
    


    case SET_CURRENT_SSPACE: 
      String prefix = null;
      if (commandTokens.hasNext())
        prefix = (String)commandTokens.next();
      Set<String> words = current.getWords();
      for (String word : words) {
        if (prefix == null) {
          out.println(word);
        } else if (word.startsWith(prefix))
          out.println(word);
      }
      break;
    


    case UNLOAD: 
      if ((current instanceof DimensionallyInterpretableSemanticSpace)) {
        if (!commandTokens.hasNext()) {
          out.println("Must supply a dimension number");
        }
        else {
          int dim = -1;
          String next = (String)commandTokens.next();
          try {
            dim = Integer.parseInt(next);
          } catch (NumberFormatException nfe) {
            out.println("Invalid dimension: " + next);
            break;
          }
          DimensionallyInterpretableSemanticSpace<?> diss = 
            (DimensionallyInterpretableSemanticSpace)current;
          try {
            out.println(diss.getDimensionDescription(dim).toString());
          } catch (Exception e) {
            out.println(e.getMessage());
          }
        }
      } else
        out.println("Current space has no dimension descriptions");
      break;
    


    case WRITE_COMMAND_RESULTS: 
      if (current == null) {
        out.println("no .sspace loaded");
      }
      else {
        String name = current.getSpaceName();
        boolean hasDimDescriptions = 
          current instanceof DimensionallyInterpretableSemanticSpace;
        int dims = current.getVectorLength();
        int words = current.getWords().size();
        if (!current.getWords().isEmpty()) {} boolean isSparse = 
          (current.getVector((String)current.getWords().iterator().next()) instanceof SparseVector);
        
        out.println(name + ": " + words + " words, " + 
          dims + " dimensions" + (
          hasDimDescriptions ? 
          " with descriptions" : "") + (
          isSparse ? ", sparse vectors" : 
          ", dense vectors")); }
      break;
    

    default: 
      if (!$assertionsDisabled) throw new AssertionError(command);
      break; }
    
    return true;
  }
  





  private static String getCommands()
  {
    return 
      "  load file1.sspace [file2.sspace...]\n  unload file1.sspace [file2.sspace...]\n  get-neighbors word [number (default 10)] [similarity measure]\n  get-similarity word1 word2 [similarity measure (default cosine)]\n  compare-sspace-vectors word sspace1 sspace2 [similarity measure (default: cosine)]\n  help\n  set-current-sspace filename.sspace\n  get-current-sspace\n  alias filename.sspace name\n  write-command-results output-file command...\n  print-vector word\n  get-words [string-prefix]\n  describe-dimension number\n  describe-semantic-space\n";
  }
  



















  private static void usage(ArgOptions options)
  {
    System.out.println("usage: java SemanticSpaceExplorer [options]\n\nCommand line options:\n" + 
      options.prettyPrint() + 
      "\n\nExplorer commands:\n" + getCommands());
  }
  
  public static void main(String[] args) {
    ArgOptions options = new ArgOptions();
    options.addOption('h', "help", "Generates a help message and exits", 
      false, null, "Program Options");
    options.addOption('f', "executeFile", "Executes the commands in the specified file and exits", 
      true, "FILE", 
      "Program Options");
    options.addOption('s', "saveRecord", "Saves a record of all the executed commands to the specfied file", 
      true, 
      "FILE", "Program Options");
    
    options.parseOptions(args);
    
    if (options.hasOption("help")) {
      usage(options);
      return;
    }
    
    PrintWriter recordFile = null;
    if (options.hasOption("saveRecord")) {
      try {
        recordFile = new PrintWriter(
          options.getStringOption("saveRecord"));
      } catch (IOException ioe) {
        System.out.println("Unable to open file for saving commands:\n" + 
          ioe);
      }
    }
    
    BufferedReader commandsToExecute = null;
    if (options.hasOption("executeFile")) {
      try {
        commandsToExecute = new BufferedReader(new FileReader(
          options.getStringOption("executeFile")));
      } catch (IOException ioe) {
        System.out.println("unable to open commands file " + 
          options.getStringOption("executeFile") + 
          ":\n" + ioe);
        return;
      }
      
    } else {
      commandsToExecute = 
        new BufferedReader(new InputStreamReader(System.in));
    }
    
    boolean suppressPrompt = options.hasOption("executeFile");
    
    SemanticSpaceExplorer explorer = new SemanticSpaceExplorer();
    Pattern regex = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
    try {
      if (!suppressPrompt)
        System.out.print("> ");
      String command = null;
      while ((command = commandsToExecute.readLine()) != null)
      {


        List<String> tokens = new ArrayList();
        Matcher regexMatcher = regex.matcher(command);
        while (regexMatcher.find()) {
          if (regexMatcher.group(1) != null)
          {
            tokens.add(regexMatcher.group(1));
          } else if (regexMatcher.group(2) != null)
          {
            tokens.add(regexMatcher.group(2));
          }
          else {
            tokens.add(regexMatcher.group());
          }
        }
        

        if ((explorer.execute(tokens.iterator())) && (recordFile != null)) {
          recordFile.println(command);
        }
        if (!suppressPrompt)
          System.out.print("> ");
      }
    } catch (IOException ioe) {
      System.out.println("An error occurred while reading in a command:\n" + 
        ioe);
    }
    if (recordFile != null) {
      recordFile.close();
    }
  }
}
