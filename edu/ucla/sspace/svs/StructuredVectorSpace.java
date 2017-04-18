package edu.ucla.sspace.svs;

import edu.ucla.sspace.basis.StringBasisMapping;
import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.dependency.DependencyExtractor;
import edu.ucla.sspace.dependency.DependencyPath;
import edu.ucla.sspace.dependency.DependencyPathAcceptor;
import edu.ucla.sspace.dependency.DependencyRelation;
import edu.ucla.sspace.dependency.DependencyTreeNode;
import edu.ucla.sspace.dependency.FilteredDependencyIterator;
import edu.ucla.sspace.util.Pair;
import edu.ucla.sspace.vector.CompactSparseVector;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.SparseDoubleVector;
import edu.ucla.sspace.vector.Vector;
import edu.ucla.sspace.vector.VectorMath;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;































































































public class StructuredVectorSpace
  implements SemanticSpace, Serializable
{
  private static final long serialVersionUID = 1L;
  public static final String SSPACE_NAME = "structured-vector-space";
  public static final String EMPTY_STRING = "";
  private static final Logger LOG = Logger.getLogger(StructuredVectorSpace.class.getName());
  





  private final StringBasisMapping termBasis;
  





  private final Map<String, SelectionalPreference> preferenceVectors;
  





  private final VectorCombinor combinor;
  





  private transient Map<RelationTuple, SparseDoubleVector> relationVectors;
  





  private final transient DependencyExtractor parser;
  





  private final transient DependencyPathAcceptor acceptor;
  




  private final transient Set<String> semanticFilter;
  





  public StructuredVectorSpace(DependencyExtractor extractor, DependencyPathAcceptor acceptor, VectorCombinor combinor)
  {
    this(extractor, acceptor, combinor, new StringBasisMapping(), new HashSet());
  }
  






  public StructuredVectorSpace(DependencyExtractor extractor, DependencyPathAcceptor acceptor, VectorCombinor combinor, StringBasisMapping termBasis, Set<String> semanticFilter)
  {
    parser = extractor;
    this.acceptor = acceptor;
    this.combinor = combinor;
    this.termBasis = termBasis;
    this.semanticFilter = semanticFilter;
    
    preferenceVectors = 
      new ConcurrentHashMap();
    relationVectors = 
      new ConcurrentHashMap();
  }
  


  public Set<String> getWords()
  {
    return Collections.unmodifiableSet(preferenceVectors.keySet());
  }
  


  public Vector getVector(String term)
  {
    SelectionalPreference preference = (SelectionalPreference)preferenceVectors.get(term);
    return preference == null ? null : lemmaVector;
  }
  


  public String getSpaceName()
  {
    return "structured-vector-space";
  }
  


  public int getVectorLength()
  {
    return termBasis.numDimensions();
  }
  


  public void processDocument(BufferedReader document)
    throws IOException
  {
    Map<Pair<String>, Double> localLemmaCounts = 
      new HashMap();
    Map<RelationTuple, SparseDoubleVector> localTuples = 
      new HashMap();
    


    DependencyTreeNode[] nodes = null;
    while ((nodes = parser.readNextTree(document)) != null)
    {

      if (nodes.length != 0)
      {


        for (i = 0; i < nodes.length; i++)
        {
          if ((nodes[i].pos().startsWith("N")) || 
            (nodes[i].pos().startsWith("J")) || 
            (nodes[i].pos().startsWith("V")))
          {

            String focusWord = nodes[i].word();
            

            if (acceptWord(focusWord))
            {
              int focusIndex = termBasis.getDimension(focusWord);
              


              Iterator<DependencyPath> pathIter = 
                new FilteredDependencyIterator(nodes[i], acceptor, 1);
              
              while (pathIter.hasNext()) {
                DependencyPath path = (DependencyPath)pathIter.next();
                DependencyTreeNode last = path.last();
                

                if ((last.pos().startsWith("N")) || 
                  (last.pos().startsWith("J")) || 
                  (last.pos().startsWith("V")))
                {


                  String otherTerm = last.word();
                  

                  if (!otherTerm.equals(""))
                  {

                    int featureIndex = termBasis.getDimension(otherTerm);
                    
                    Pair<String> p = new Pair(focusWord, otherTerm);
                    Double curCount = (Double)localLemmaCounts.get(p);
                    localLemmaCounts.put(p, 
                      Double.valueOf(curCount == null ? 1.0D : 1.0D + curCount.doubleValue()));
                    




                    DependencyRelation relation = (DependencyRelation)path.iterator().next();
                    



                    if (relation.headNode().word().equals(focusWord))
                    {

                      RelationTuple relationKey = new RelationTuple(
                        focusIndex, relation.relation().intern());
                      SparseDoubleVector relationVector = (SparseDoubleVector)localTuples.get(
                        relationKey);
                      if (relationVector == null) {
                        relationVector = new CompactSparseVector();
                        localTuples.put(relationKey, relationVector);
                      }
                      relationVector.add(featureIndex, 1.0D);
                    }
                  }
                }
              } } } } } }
    document.close();
    


    for (Map.Entry<Pair<String>, Double> e : localLemmaCounts.entrySet())
    {
      Pair<String> p = (Pair)e.getKey();
      


      SelectionalPreference preference = (SelectionalPreference)preferenceVectors.get(x);
      if (preference == null) {
        synchronized (this) {
          preference = (SelectionalPreference)preferenceVectors.get(x);
          if (preference == null) {
            preference = new SelectionalPreference(combinor);
            preferenceVectors.put((String)x, preference);
          }
        }
      }
      
      synchronized (preference) {
        lemmaVector.add(
          termBasis.getDimension((String)y), ((Double)e.getValue()).doubleValue());
      }
    }
    


    int i = localTuples.entrySet().iterator();
    while (i.hasNext()) {
      Map.Entry<RelationTuple, SparseDoubleVector> r = (Map.Entry)i.next();
      

      SparseDoubleVector relationCounts = (SparseDoubleVector)relationVectors.get(r.getKey());
      if (relationCounts == null) {
        synchronized (this) {
          relationCounts = (SparseDoubleVector)relationVectors.get(r.getKey());
          if (relationCounts == null) {
            relationCounts = new CompactSparseVector();
            relationVectors.put((RelationTuple)r.getKey(), relationCounts);
          }
        }
      }
      

      synchronized (relationCounts) {
        VectorMath.add(relationCounts, (DoubleVector)r.getValue());
      }
    }
  }
  


  public void processSpace(Properties properties)
  {
    SparseDoubleVector empty = new CompactSparseVector();
    
    Iterator localIterator = relationVectors.entrySet().iterator();
    while (localIterator.hasNext()) {
      Map.Entry<RelationTuple, SparseDoubleVector> e = (Map.Entry)localIterator.next();
      RelationTuple relation = (RelationTuple)e.getKey();
      SparseDoubleVector relationCounts = (SparseDoubleVector)e.getValue();
      String headWord = (String)termBasis.getDimensionDescription(head);
      String rel = relation;
      
      SelectionalPreference headPref = (SelectionalPreference)preferenceVectors.get(headWord);
      
      if (headPref == null) {
        LOG.fine("what the fuck");
      }
      for (int index : relationCounts.getNonZeroIndices()) {
        double frequency = relationCounts.get(index);
        String depWord = (String)termBasis.getDimensionDescription(index);
        SelectionalPreference depPref = (SelectionalPreference)preferenceVectors.get(depWord);
        


        if (depPref != null)
        {

          headPref.addPreference(
            rel, lemmaVector, frequency);
          depPref.addInversePreference(
            rel, lemmaVector, frequency);
        } }
      e.setValue(empty);
    }
    


    relationVectors = null;
  }
  


  public SparseDoubleVector contextualize(String focusWord, String relation, String secondWord, boolean isFocusHeadWord)
  {
    SelectionalPreference focusPref = (SelectionalPreference)preferenceVectors.get(focusWord);
    SelectionalPreference secondPref = (SelectionalPreference)preferenceVectors.get(secondWord);
    
    if (focusPref == null)
      return null;
    if (secondPref == null) {
      return lemmaVector;
    }
    if (isFocusHeadWord)
      return combinor.combineUnmodified(
        lemmaVector, 
        secondPref.inversePreference(relation));
    return combinor.combineUnmodified(lemmaVector, 
      secondPref.preference(relation));
  }
  


  public void setSemanticFilter(Set<String> semanticsToRetain)
  {
    semanticFilter.clear();
    semanticFilter.addAll(semanticsToRetain);
  }
  



  private boolean acceptWord(String word)
  {
    return (!word.equals("")) && (
      (semanticFilter.isEmpty()) || (semanticFilter.contains(word)));
  }
}
