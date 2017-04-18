package edu.ucla.sspace.dri;

import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.dependency.DependencyExtractor;
import edu.ucla.sspace.dependency.DependencyExtractorManager;
import edu.ucla.sspace.dependency.DependencyIterator;
import edu.ucla.sspace.dependency.DependencyPath;
import edu.ucla.sspace.dependency.DependencyPermutationFunction;
import edu.ucla.sspace.dependency.DependencyRelationAcceptor;
import edu.ucla.sspace.dependency.DependencyTreeNode;
import edu.ucla.sspace.dependency.UniversalRelationAcceptor;
import edu.ucla.sspace.index.RandomIndexVectorGenerator;
import edu.ucla.sspace.util.GeneratorMap;
import edu.ucla.sspace.util.ReflectionUtil;
import edu.ucla.sspace.vector.CompactSparseIntegerVector;
import edu.ucla.sspace.vector.IntegerVector;
import edu.ucla.sspace.vector.TernaryVector;
import edu.ucla.sspace.vector.Vector;
import edu.ucla.sspace.vector.Vectors;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;







































































































































































































public class DependencyRandomIndexing
  implements SemanticSpace
{
  public static final String PROPERTY_PREFIX = "edu.ucla.sspace.dri.DependencyRandomIndexing";
  public static final String VECTOR_LENGTH_PROPERTY = "edu.ucla.sspace.dri.DependencyRandomIndexing.indexVectorLength";
  public static final String DEPENDENCY_ACCEPTOR_PROPERTY = "edu.ucla.sspace.dri.DependencyRandomIndexing.dependencyAcceptor";
  public static final String DEPENDENCY_PATH_LENGTH_PROPERTY = "edu.ucla.sspace.dri.DependencyRandomIndexing.dependencyPathLength";
  public static final int DEFAULT_VECTOR_LENGTH = 50000;
  public static final int DEFAULT_DEPENDENCY_PATH_LENGTH = Integer.MAX_VALUE;
  public static final String SSPACE_NAME = "dependency-random-indexing";
  private static final Logger LOGGER = Logger.getLogger(DependencyRandomIndexing.class.getName());
  




  private Map<String, TernaryVector> indexMap;
  




  private final DependencyPermutationFunction<TernaryVector> permFunc;
  




  private ConcurrentMap<String, IntegerVector> wordSpace;
  



  private final int vectorLength;
  



  private final DependencyExtractor parser;
  



  private final DependencyRelationAcceptor acceptor;
  



  private final int pathLength;
  



  private Set<String> semanticFilter;
  




  public DependencyRandomIndexing(DependencyPermutationFunction<TernaryVector> permFunc)
  {
    this(permFunc, System.getProperties());
  }
  





  public DependencyRandomIndexing(DependencyPermutationFunction<TernaryVector> permFunc, Properties properties)
  {
    this.permFunc = permFunc;
    parser = DependencyExtractorManager.getDefaultExtractor();
    

    String vectorLengthProp = 
      properties.getProperty("edu.ucla.sspace.dri.DependencyRandomIndexing.indexVectorLength");
    vectorLength = (vectorLengthProp != null ? 
      Integer.parseInt(vectorLengthProp) : 
      50000);
    

    String pathLengthProp = 
      properties.getProperty("edu.ucla.sspace.dri.DependencyRandomIndexing.dependencyPathLength");
    pathLength = (pathLengthProp != null ? 
      Integer.parseInt(pathLengthProp) : 
      Integer.MAX_VALUE);
    

    String acceptorProp = 
      properties.getProperty("edu.ucla.sspace.dri.DependencyRandomIndexing.dependencyAcceptor");
    acceptor = (acceptorProp != null ? 
    
      (DependencyRelationAcceptor)ReflectionUtil.getObjectInstance(acceptorProp) : 
      new UniversalRelationAcceptor());
    

    RandomIndexVectorGenerator indexVectorGenerator = 
      new RandomIndexVectorGenerator(vectorLength, properties);
    indexMap = new GeneratorMap(indexVectorGenerator);
    wordSpace = new ConcurrentHashMap();
    semanticFilter = new HashSet();
  }
  


  public Set<String> getWords()
  {
    return Collections.unmodifiableSet(wordSpace.keySet());
  }
  


  public Vector getVector(String term)
  {
    return Vectors.immutable((IntegerVector)wordSpace.get(term));
  }
  
  public DependencyPermutationFunction<TernaryVector> getPermutations() {
    return permFunc;
  }
  
  public Map<String, TernaryVector> getWordToVectorMap() {
    return indexMap;
  }
  
  public void setWordToVectorMap(Map<String, TernaryVector> vectorMap) {
    indexMap = vectorMap;
  }
  


  public String getSpaceName()
  {
    return "dependency-random-indexing-" + vectorLength;
  }
  


  public int getVectorLength()
  {
    return vectorLength;
  }
  



  public void processDocument(BufferedReader document)
    throws IOException
  {
    DependencyTreeNode[] nodes = null;
    while ((nodes = parser.readNextTree(document)) != null)
    {

      if (nodes.length != 0)
      {


        for (int i = 0; i < nodes.length; i++) {
          String focusWord = nodes[i].word();
          

          if (acceptWord(focusWord))
          {


            IntegerVector focusMeaning = getSemanticVector(focusWord);
            


            Iterator<DependencyPath> pathIter = 
              new DependencyIterator(nodes[i], acceptor, pathLength);
            




            while (pathIter.hasNext()) {
              DependencyPath path = (DependencyPath)pathIter.next();
              TernaryVector termVector = (TernaryVector)indexMap.get(path.last().word());
              if (permFunc != null)
                termVector = (TernaryVector)permFunc.permute(termVector, path);
              add(focusMeaning, termVector);
            }
          }
        } } }
    document.close();
  }
  






  public void processSpace(Properties properties) {}
  






  public void setWordToIndexVector(Map<String, TernaryVector> m)
  {
    indexMap = m;
  }
  








  public void setSemanticFilter(Set<String> semanticsToRetain)
  {
    semanticFilter.clear();
    semanticFilter.addAll(semanticsToRetain);
  }
  



  private boolean acceptWord(String word)
  {
    return (semanticFilter.isEmpty()) || (semanticFilter.contains(word));
  }
  







  private static void add(IntegerVector semantics, TernaryVector index)
  {
    synchronized (semantics) {
      for (int p : index.positiveDimensions())
        semantics.add(p, 1);
      for (int n : index.negativeDimensions()) {
        semantics.add(n, -1);
      }
    }
  }
  







  private IntegerVector getSemanticVector(String word)
  {
    IntegerVector v = (IntegerVector)wordSpace.get(word);
    if (v == null)
    {

      synchronized (this)
      {

        v = (IntegerVector)wordSpace.get(word);
        if (v == null) {
          v = new CompactSparseIntegerVector(vectorLength);
          wordSpace.put(word, v);
        }
      }
    }
    return v;
  }
}
