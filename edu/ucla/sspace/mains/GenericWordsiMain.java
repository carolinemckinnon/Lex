package edu.ucla.sspace.mains;

import edu.ucla.sspace.clustering.Clustering;
import edu.ucla.sspace.clustering.OnlineClustering;
import edu.ucla.sspace.common.ArgOptions;
import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.common.SemanticSpaceIO;
import edu.ucla.sspace.text.Document;
import edu.ucla.sspace.util.Generator;
import edu.ucla.sspace.util.ReflectionUtil;
import edu.ucla.sspace.vector.SparseDoubleVector;
import edu.ucla.sspace.wordsi.AssignmentReporter;
import edu.ucla.sspace.wordsi.ContextExtractor;
import edu.ucla.sspace.wordsi.ContextGenerator;
import edu.ucla.sspace.wordsi.EvaluationWordsi;
import edu.ucla.sspace.wordsi.GeneralContextExtractor;
import edu.ucla.sspace.wordsi.StreamingWordsi;
import edu.ucla.sspace.wordsi.WaitingWordsi;
import edu.ucla.sspace.wordsi.psd.PseudoWordContextExtractor;
import edu.ucla.sspace.wordsi.psd.PseudoWordReporter;
import edu.ucla.sspace.wordsi.semeval.SemEvalContextExtractor;
import edu.ucla.sspace.wordsi.semeval.SemEvalReporter;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOError;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;




































































































































public abstract class GenericWordsiMain
  extends GenericMain
{
  private ObjectOutputStream saveStream = null;
  
  private ObjectInputStream loadStream = null;
  

  public GenericWordsiMain() {}
  
  protected void addExtraOptions(ArgOptions options)
  {
    options.removeOption('Z');
    options.removeOption('X');
    options.removeOption('o');
    options.removeOption('w');
    

    options.addOption('s', "streamingClustering", 
      "Specifies the streaming clustering algorithm to use for forming word senses", 
      
      true, "CLASSNAME", "Required (one of)");
    options.addOption('b', "batchClustering", 
      "Specifies the batch clustering algorithm to use for forming word senses", 
      
      true, "CLASSNAME", "Required (one of)");
    options.addOption('e', "evaluationClustering", 
      "Specifies a trained Wordsi semantic space to be used for evaluation.  When set, one of the Evaluation Type arguments must be set", 
      

      true, "<sspace>", "Required (one of)");
    

    options.addOption('P', "pseudoWordEvaluation", 
      "Specifies a mapping from raw tokens to their pseudo word token.  Only the raw tokens in this mapping will be represented in the Wordsi space.  A PseudoWordReport will be generated for these pseudo words.  This overrides the -a option", 
      



      true, "FILENAME", "Evaluation Type");
    options.addOption('E', "semEvalEvaluation", 
      "Signifies that the data files are in the SemEval format and that only test instance words should be represented in the Wordsi space.  Each line must correspond to an instance context and the focus word must be precceded by the token given as the argument to this option.", 
      




      true, "STRING", "Evaluation Type");
    options.addOption('N', "wordlistEvaluation", 
      "Learned word senses are assumed to be related to the senses in for other words in the acceptedWords list.  This evaluation will track the headers for documents which should mark whether or not the focus words are being used with their common sense.", 
      




      false, null, "Evaluation Type");
    

    options.addOption('a', "acceptedWords", 
      "Specifies the set of words which should be represented by Wordsi. (Default: all words)", 
      
      true, "FILENAME", "Optional");
    options.addOption('c', "clusters", 
      "Specifies the desired number of clusters, or word senses.  (Default: 0)", 
      
      true, "INT", "Optional");
    options.addOption('W', "windowSize", 
      "Specifies the number of words, in one direction, that form a valid context.  For example, a window size of 5 means that up to 5 words before and after a focus word are used to form the context. (Default: 5)", 
      



      true, "INT", "Optional");
    options.addOption('h', "useHeaderToken", 
      "Set to true if the first token in a context should be treated as a document header. Note that this is only used when -E and -P are not used.", 
      


      false, null, "Optional");
    

    options.addOption('S', "save", 
      "Specfies a file to which all files needed to generate context vectors will be serialized", 
      
      true, "FILENAME", "Serialization");
    options.addOption('L', "load", 
      "Specfies a file from which all files needed to generate context vectors will be deserialized", 
      
      true, "FILENAME", "Serialization");
  }
  




  protected abstract ContextExtractor getExtractor();
  




  protected Set<String> getAcceptedWords()
  {
    if (!argOptions.hasOption('a')) {
      return null;
    }
    try {
      Set<String> acceptedWords = new HashSet();
      BufferedReader br = new BufferedReader(new FileReader(
        argOptions.getStringOption('a')));
      for (String line = null; (line = br.readLine()) != null;)
        acceptedWords.add(line.trim().toLowerCase());
      return acceptedWords;
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
  



  protected Map<String, String> getPseudoWordMap()
  {
    if (!argOptions.hasOption('P')) {
      return null;
    }
    try {
      Map<String, String> pseudoWordMap = new HashMap();
      BufferedReader br = new BufferedReader(new FileReader(
        argOptions.getStringOption('P')));
      for (String line = null; (line = br.readLine()) != null;) {
        String[] tokens = line.split("\\s+");
        pseudoWordMap.put(tokens[0].trim(), tokens[1].trim());
      }
      return pseudoWordMap;
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
  







  protected ContextExtractor contextExtractorFromGenerator(ContextGenerator generator)
  {
    if (argOptions.hasOption('e')) {
      generator.setReadOnly(true);
    }
    

    if (argOptions.hasOption('E')) {
      return new SemEvalContextExtractor(
        generator, windowSize(), argOptions.getStringOption('E'));
    }
    

    if (argOptions.hasOption('P')) {
      return new PseudoWordContextExtractor(
        generator, windowSize(), getPseudoWordMap());
    }
    
    return new GeneralContextExtractor(generator, windowSize(), 
      argOptions.hasOption('h'));
  }
  


  protected int windowSize()
  {
    return argOptions.getIntOption('W', 5);
  }
  
  protected Iterator<Document> getDocumentIterator() throws IOException {
    Iterator<Document> docIter = super.getDocumentIterator();
    




    if (!argOptions.hasOption('P')) {
      return docIter;
    }
    



    List<Document> docList = new LinkedList();
    while (docIter.hasNext())
      docList.add((Document)docIter.next());
    Collections.shuffle(docList);
    return docList.iterator();
  }
  


  protected SemanticSpace getSpace()
  {
    ArgOptions options = argOptions;
    

    AssignmentReporter reporter = null;
    if (options.hasOption('P')) {
      reporter = new PseudoWordReporter(System.out);
    }
    int numClusters = options.getIntOption('c', 0);
    


    if (options.hasOption('e'))
    {
      if ((!options.hasOption('E')) && (!options.hasOption('P'))) {
        usage();
        System.out.println(
          "An Evaluation Type must be set when evaluating  a trained Wordsi model.");
        
        System.exit(1);
      }
      

      try
      {
        SemanticSpace sspace = SemanticSpaceIO.load(
          options.getStringOption('e'));
        if (options.hasOption('E'))
          reporter = new SemEvalReporter(System.out);
        return new EvaluationWordsi(
          getAcceptedWords(), getExtractor(), sspace, reporter);
      } catch (IOException ioe) {
        throw new IOError(ioe);
      } }
    if (options.hasOption('s'))
    {

      System.getProperties().setProperty(
        "edu.ucla.sspace.clustering.OnlineClustering.numClusters", 
        options.getStringOption('c'));
      Generator<OnlineClustering<SparseDoubleVector>> clusterGenerator = 
        (Generator)ReflectionUtil.getObjectInstance(options.getStringOption('s'));
      return new StreamingWordsi(getAcceptedWords(), getExtractor(), 
        clusterGenerator, reporter, numClusters); }
    if (options.hasOption('b'))
    {

      Clustering clustering = 
        (Clustering)ReflectionUtil.getObjectInstance(options.getStringOption('b'));
      return new WaitingWordsi(getAcceptedWords(), getExtractor(), 
        clustering, reporter, numClusters);
    }
    

    usage();
    System.out.println("No clustering method was specified.");
    System.exit(1);
    return null;
  }
  



  protected ObjectOutputStream openSaveFile()
  {
    try
    {
      if ((saveStream == null) && (argOptions.hasOption('S')))
        saveStream = new ObjectOutputStream(new FileOutputStream(
          argOptions.getStringOption('S')));
      return saveStream;
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
  



  protected ObjectInputStream openLoadFile()
  {
    try
    {
      if ((loadStream == null) && (argOptions.hasOption('L')))
        loadStream = new ObjectInputStream(new FileInputStream(
          argOptions.getStringOption('L')));
      return loadStream;
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
  

  protected void saveObject(ObjectOutputStream outStream, Object obj)
  {
    try
    {
      outStream.writeObject(obj);
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
  





  protected <T> T loadObject(ObjectInputStream inStream)
  {
    try
    {
      return inStream.readObject();
    } catch (IOException ioe) {
      throw new IOError(ioe);
    } catch (ClassNotFoundException cnfe) {
      throw new IOError(cnfe);
    }
  }
}
