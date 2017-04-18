package edu.ucla.sspace.tools;

import edu.ucla.sspace.basis.BasisMapping;
import edu.ucla.sspace.basis.StringBasisMapping;
import edu.ucla.sspace.common.ArgOptions;
import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.dependency.DependencyExtractor;
import edu.ucla.sspace.dependency.DependencyExtractorManager;
import edu.ucla.sspace.dependency.DependencyPath;
import edu.ucla.sspace.dependency.DependencyPathAcceptor;
import edu.ucla.sspace.dependency.DependencyPathWeight;
import edu.ucla.sspace.dependency.DependencyTreeNode;
import edu.ucla.sspace.dependency.FilteredDependencyIterator;
import edu.ucla.sspace.dependency.FlatPathWeight;
import edu.ucla.sspace.dependency.UniversalPathAcceptor;
import edu.ucla.sspace.mains.DependencyGenericMain;
import edu.ucla.sspace.matrix.AtomicGrowingSparseHashMatrix;
import edu.ucla.sspace.matrix.NoTransform;
import edu.ucla.sspace.matrix.SparseMatrix;
import edu.ucla.sspace.matrix.Transform;
import edu.ucla.sspace.util.BoundedSortedMap;
import edu.ucla.sspace.util.Pair;
import edu.ucla.sspace.vector.DenseVector;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.SparseDoubleVector;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;








































public class DependencyBasisMaker
  extends DependencyGenericMain
{
  public DependencyBasisMaker() {}
  
  public void addExtraOptions(ArgOptions options)
  {
    options.addOption('b', "basisSize", 
      "Specifies the total desired size of the basis (Default: 10000)", 
      
      true, "INT", "Optional");
    options.addOption('a', "pathAcceptor", 
      "Specifies the dependency path acceptor to use. (Default:    UnivseralPathAcceptor)", 
      
      true, "CLASSNAME", "Optional");
    options.addOption('w', "pathWeighter", 
      "Specifies the dependency path weighter to use. (Default:    FlatPathWeight)", 
      
      true, "CLASSNAME", "Optional");
    options.addOption('l', "pathLength", 
      "Specifies the maximum dependency path length. (Default:    5)", 
      
      true, "INT", "Optional");
  }
  


  protected SemanticSpace getSpace()
  {
    setupDependencyExtractor();
    
    int bound = argOptions.getIntOption('b', 10000);
    Transform transform = (Transform)argOptions.getObjectOption(
      'T', new NoTransform());
    DependencyPathAcceptor acceptor = (DependencyPathAcceptor)argOptions.getObjectOption(
      'a', new UniversalPathAcceptor());
    DependencyPathWeight weighter = (DependencyPathWeight)argOptions.getObjectOption(
      'w', new FlatPathWeight());
    int pathLength = argOptions.getIntOption('l', 5);
    return new OccurrenceCounter(
      transform, bound, acceptor, weighter, pathLength);
  }
  



  protected void saveSSpace(SemanticSpace sspace, File outputFile)
    throws IOException
  {
    BasisMapping<String, String> savedTerms = new StringBasisMapping();
    for (String term : sspace.getWords()) {
      savedTerms.getDimension(term);
    }
    ObjectOutputStream ouStream = new ObjectOutputStream(
      new FileOutputStream(outputFile));
    ouStream.writeObject(savedTerms);
    ouStream.close();
  }
  





  public class OccurrenceCounter
    implements SemanticSpace
  {
    private final AtomicGrowingSparseHashMatrix cooccurrenceMatrix;
    




    private final BasisMapping<String, String> basis;
    




    private final Map<String, Double> wordScores;
    




    private final Transform transform;
    




    private final DependencyPathAcceptor acceptor;
    




    private final DependencyPathWeight weighter;
    




    private final int pathLength;
    




    private final DependencyExtractor extractor;
    




    public OccurrenceCounter(Transform transform, int bound, DependencyPathAcceptor acceptor, DependencyPathWeight weighter, int pathLength)
    {
      cooccurrenceMatrix = new AtomicGrowingSparseHashMatrix();
      basis = new StringBasisMapping();
      wordScores = new BoundedSortedMap(bound);
      extractor = DependencyExtractorManager.getDefaultExtractor();
      
      this.transform = transform;
      this.acceptor = acceptor;
      this.weighter = weighter;
      this.pathLength = pathLength;
    }
    







    public void processDocument(BufferedReader document)
      throws IOException
    {
      Map<Pair<Integer>, Double> matrixEntryToCount = 
        new HashMap();
      


      DependencyTreeNode[] nodes = null;
      while ((nodes = extractor.readNextTree(document)) != null)
      {

        if (nodes.length != 0)
        {


          for (wordIndex = 0; wordIndex < nodes.length; wordIndex++) {
            String focusWord = nodes[wordIndex].word();
            int focusIndex = basis.getDimension(focusWord);
            



            Iterator<DependencyPath> paths = 
              new FilteredDependencyIterator(
              nodes[wordIndex], acceptor, pathLength);
            



            while (paths.hasNext()) {
              DependencyPath path = (DependencyPath)paths.next();
              
              String occurrence = path.last().word();
              int featureIndex = basis.getDimension(occurrence);
              
              double score = weighter.scorePath(path);
              matrixEntryToCount.put(new Pair(
                Integer.valueOf(focusIndex), Integer.valueOf(featureIndex)), Double.valueOf(score));
            }
          }
        }
      }
      


      int wordIndex = matrixEntryToCount.entrySet().iterator();
      while (wordIndex.hasNext()) {
        Map.Entry<Pair<Integer>, Double> e = (Map.Entry)wordIndex.next();
        Pair<Integer> p = (Pair)e.getKey();
        cooccurrenceMatrix.addAndGet(((Integer)x).intValue(), ((Integer)y).intValue(), ((Double)e.getValue()).doubleValue());
      }
    }
    


    public Set<String> getWords()
    {
      return Collections.unmodifiableSet(wordScores.keySet());
    }
    


    public DoubleVector getVector(String word)
    {
      Double score = (Double)wordScores.get(word);
      return score == null ? 
        new DenseVector(new double[] { 0.0D }) : 
        new DenseVector(new double[] { score.doubleValue() });
    }
    


    public int getVectorLength()
    {
      return 1;
    }
    


    public void processSpace(Properties properties)
    {
      SparseMatrix cleanedMatrix = (SparseMatrix)transform.transform(
        cooccurrenceMatrix);
      for (String term : basis.keySet()) {
        int index = basis.getDimension(term);
        SparseDoubleVector sdv = cleanedMatrix.getRowVector(index);
        
        double score = 0.0D;
        for (int i : sdv.getNonZeroIndices()) {
          score += sdv.get(i);
        }
        wordScores.put(term, Double.valueOf(score));
      }
    }
    


    public String getSpaceName()
    {
      return "BasisMaker";
    }
  }
}
