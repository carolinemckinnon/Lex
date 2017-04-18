package edu.ucla.sspace.dv;

import edu.ucla.sspace.common.DimensionallyInterpretableSemanticSpace;
import edu.ucla.sspace.dependency.DependencyExtractor;
import edu.ucla.sspace.dependency.DependencyExtractorManager;
import edu.ucla.sspace.dependency.DependencyPath;
import edu.ucla.sspace.dependency.DependencyPathAcceptor;
import edu.ucla.sspace.dependency.DependencyPathWeight;
import edu.ucla.sspace.dependency.DependencyTreeNode;
import edu.ucla.sspace.dependency.FilteredDependencyIterator;
import edu.ucla.sspace.dependency.FlatPathWeight;
import edu.ucla.sspace.util.ReflectionUtil;
import edu.ucla.sspace.vector.CompactSparseVector;
import edu.ucla.sspace.vector.SparseDoubleVector;
import edu.ucla.sspace.vector.Vector;
import edu.ucla.sspace.vector.Vectors;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;



































































































































































public class DependencyVectorSpace
  implements DimensionallyInterpretableSemanticSpace<String>
{
  public static final String PROPERTY_PREFIX = "edu.ucla.sspace.dri.DependencyVectorSpace";
  public static final String PATH_ACCEPTOR_PROPERTY = "edu.ucla.sspace.dri.DependencyVectorSpace.pathAcceptor";
  public static final String PATH_WEIGHTING_PROPERTY = "edu.ucla.sspace.dri.DependencyVectorSpace.pathWeighting";
  public static final String BASIS_MAPPING_PROPERTY = "edu.ucla.sspace.dri.DependencyVectorSpace.basisMapping";
  private static final Logger LOGGER = Logger.getLogger(DependencyVectorSpace.class.getName());
  




  private Map<String, SparseDoubleVector> termToVector;
  




  private final DependencyExtractor extractor;
  




  private final DependencyPathBasisMapping basisMapping;
  




  private final DependencyPathWeight weighter;
  




  private final DependencyPathAcceptor acceptor;
  



  private final int pathLength;
  




  public DependencyVectorSpace()
  {
    this(System.getProperties(), 0);
  }
  








  public DependencyVectorSpace(Properties properties)
  {
    this(properties, 0);
  }
  














  public DependencyVectorSpace(Properties properties, int pathLength)
  {
    if (pathLength < 0) {
      throw new IllegalArgumentException(
        "path length must be non-negative");
    }
    termToVector = new HashMap();
    
    String basisMappingProp = 
      properties.getProperty("edu.ucla.sspace.dri.DependencyVectorSpace.basisMapping");
    basisMapping = (basisMappingProp == null ? 
      new WordBasedBasisMapping() : 
      
      (DependencyPathBasisMapping)ReflectionUtil.getObjectInstance(basisMappingProp));
    
    String pathWeightProp = 
      properties.getProperty("edu.ucla.sspace.dri.DependencyVectorSpace.pathWeighting");
    weighter = (pathWeightProp == null ? 
      new FlatPathWeight() : 
      
      (DependencyPathWeight)ReflectionUtil.getObjectInstance(pathWeightProp));
    
    String acceptorProp = 
      properties.getProperty("edu.ucla.sspace.dri.DependencyVectorSpace.pathAcceptor");
    acceptor = (acceptorProp == null ? 
      new MinimumPennTemplateAcceptor() : 
      
      (DependencyPathAcceptor)ReflectionUtil.getObjectInstance(acceptorProp));
    
    this.pathLength = (pathLength == 0 ? 
      acceptor.maxPathLength() : 
      pathLength);
    
    extractor = DependencyExtractorManager.getDefaultExtractor();
  }
  






  public String getDimensionDescription(int dimension)
  {
    if ((dimension < 0) || (dimension >= basisMapping.numDimensions()))
      throw new IllegalArgumentException(
        "Invalid dimension: " + dimension);
    return (String)basisMapping.getDimensionDescription(dimension);
  }
  








  private SparseDoubleVector getSemanticVector(String word)
  {
    SparseDoubleVector v = (SparseDoubleVector)termToVector.get(word);
    if (v == null)
    {

      synchronized (this)
      {

        v = (SparseDoubleVector)termToVector.get(word);
        if (v == null) {
          v = new CompactSparseVector();
          termToVector.put(word, v);
        }
      }
    }
    return v;
  }
  


  public Set<String> getWords()
  {
    return Collections.unmodifiableSet(termToVector.keySet());
  }
  


  public Vector getVector(String term)
  {
    SparseDoubleVector v = (SparseDoubleVector)termToVector.get(term);
    return v == null ? null : Vectors.immutable(
      Vectors.subview(v, 0, basisMapping.numDimensions()));
  }
  



  public String getSpaceName()
  {
    return 
      "DependencyVectorSpace_" + basisMapping + "_" + weighter + "_" + acceptor;
  }
  


  public int getVectorLength()
  {
    return basisMapping.numDimensions();
  }
  







  public void processDocument(BufferedReader document)
    throws IOException
  {
    DependencyTreeNode[] nodes = null;
    while ((nodes = extractor.readNextTree(document)) != null)
    {

      if (nodes.length != 0)
      {


        for (int wordIndex = 0; wordIndex < nodes.length; wordIndex++)
        {
          String focusWord = nodes[wordIndex].word();
          

          SparseDoubleVector focusMeaning = getSemanticVector(focusWord);
          



          Iterator<DependencyPath> paths = new FilteredDependencyIterator(
            nodes[wordIndex], acceptor, pathLength);
          



          while (paths.hasNext()) {
            DependencyPath path = (DependencyPath)paths.next();
            




            int dimension = basisMapping.getDimension(path);
            



            double weight = weighter.scorePath(path);
            


            synchronized (focusMeaning) {
              focusMeaning.add(dimension, weight);
            }
          }
        } }
    }
    document.close();
  }
  
  public void processSpace(Properties properties) {}
}
