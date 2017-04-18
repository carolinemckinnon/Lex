package edu.ucla.sspace.util;

import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.common.Similarity;
import edu.ucla.sspace.common.VectorMapSemanticSpace;
import edu.ucla.sspace.vector.DenseVector;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.Vector;
import edu.ucla.sspace.vector.VectorMath;
import edu.ucla.sspace.vector.Vectors;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;



































































public class PartitioningNearestNeighborFinder
  implements NearestNeighborFinder, Serializable
{
  private static final long serialVersionUID = 1L;
  private static final Logger LOGGER = Logger.getLogger(PartitioningNearestNeighborFinder.class.getName());
  




  private transient SemanticSpace sspace;
  




  private final MultiMap<DoubleVector, String> principleVectorToNearestTerms;
  




  private transient WorkQueue workQueue;
  




  public PartitioningNearestNeighborFinder(SemanticSpace sspace)
  {
    this(sspace, (int)Math.ceil(Math.log(sspace.getWords().size())));
  }
  









  public PartitioningNearestNeighborFinder(SemanticSpace sspace, int numPrincipleVectors)
  {
    if (sspace == null)
      throw new NullPointerException();
    if (numPrincipleVectors > sspace.getWords().size())
      throw new IllegalArgumentException(
        "Cannot have more principle vectors than word vectors: " + 
        numPrincipleVectors);
    if (numPrincipleVectors < 1)
      throw new IllegalArgumentException(
        "Must have at least one principle vector");
    this.sspace = sspace;
    principleVectorToNearestTerms = new HashMultiMap();
    workQueue = new WorkQueue();
    computePrincipleVectors(numPrincipleVectors);
  }
  








  private void computePrincipleVectors(int numPrincipleVectors)
  {
    final int numTerms = sspace.getWords().size();
    final List<DoubleVector> termVectors = 
      new ArrayList(numTerms);
    String[] termAssignments = new String[numTerms];
    int k = 0;
    for (String term : sspace.getWords()) {
      termVectors.add(Vectors.asDouble(sspace.getVector(term)));
      termAssignments[(k++)] = term;
    }
    
    Random random = new Random();
    
    final DoubleVector[] principles = new DoubleVector[numPrincipleVectors];
    for (int i = 0; i < principles.length; i++) {
      principles[i] = new DenseVector(sspace.getVectorLength());
    }
    
    final MultiMap<Integer, Integer> clusterAssignment = 
      new HashMultiMap();
    for (int i = 0; i < numTerms; i++) {
      clusterAssignment.put(Integer.valueOf(random.nextInt(numPrincipleVectors)), Integer.valueOf(i));
    }
    int numIters = 1;
    for (int iter = 0; iter < numIters; iter++) {
      LoggerUtil.verbose(LOGGER, "Computing principle vectors (round %d/%d)", new Object[] { Integer.valueOf(iter + 1), Integer.valueOf(numIters) });
      

      Iterator localIterator2 = clusterAssignment.asMap().entrySet().iterator();
      Iterator localIterator3;
      for (; localIterator2.hasNext(); 
          



          localIterator3.hasNext())
      {
        Map.Entry<Integer, Set<Integer>> e = (Map.Entry)localIterator2.next();
        int cluster = ((Integer)e.getKey()).intValue();
        DoubleVector principle = new DenseVector(sspace.getVectorLength());
        principles[cluster] = principle;
        localIterator3 = ((Set)e.getValue()).iterator(); continue;row = (Integer)localIterator3.next();
        VectorMath.add(principle, (DoubleVector)termVectors.get(row.intValue()));
      }
      

      clusterAssignment.clear();
      final int numThreads = workQueue.availableThreads();
      Object key = workQueue.registerTaskGroup(numThreads);
      
      for (int threadId_ = 0; threadId_ < numThreads; threadId_++) {
        final int threadId = threadId_;
        workQueue.add(key, new Runnable()
        {
          public void run() {
            MultiMap<Integer, Integer> clusterAssignment_ = 
              new HashMultiMap();
            


            for (int i = threadId; i < numTerms; i += numThreads) {
              DoubleVector v = (DoubleVector)termVectors.get(i);
              double highestSim = -1.7976931348623157E308D;
              int pVec = -1;
              for (int j = 0; j < principles.length; j++) {
                DoubleVector principle = principles[j];
                double sim = Similarity.cosineSimilarity(
                  v, principle);
                if ((!PartitioningNearestNeighborFinder.$assertionsDisabled) && ((sim < -1.0D) || (sim > 1.0D)))
                {
                  throw new AssertionError("similarity  to principle vector " + j + " is " + "outside the expected range: " + sim); }
                if (sim > highestSim) {
                  highestSim = sim;
                  pVec = j;
                }
              }
              if ((!PartitioningNearestNeighborFinder.$assertionsDisabled) && (pVec == -1))
              {
                throw new AssertionError("Could not find match to any of the " + principles.length + " principle vectors"); }
              clusterAssignment_.put(Integer.valueOf(pVec), Integer.valueOf(i));
            }
            



            synchronized (clusterAssignment) {
              clusterAssignment.putAll(clusterAssignment_);
            }
          }
        });
      }
      workQueue.await(key);
    }
    
    double mean = numTerms / numPrincipleVectors;
    double variance = 0.0D;
    
    Integer row = clusterAssignment.asMap().entrySet().iterator();
    while (row.hasNext()) {
      Map.Entry<Integer, Set<Integer>> e = (Map.Entry)row.next();
      Object rows = (Set)e.getValue();
      Set<String> terms = new HashSet();
      for (Integer i : (Set)rows)
        terms.add(termAssignments[i.intValue()]);
      LoggerUtil.verbose(LOGGER, "Principle vectod %d is closest to %d terms", new Object[] {
        e.getKey(), Integer.valueOf(terms.size()) });
      double diff = mean - terms.size();
      variance += diff * diff;
      principleVectorToNearestTerms.putMany(
        principles[((Integer)e.getKey()).intValue()], terms);
    }
    
    LoggerUtil.verbose(LOGGER, "Average number terms per principle vector: %f, (%f stddev)", new Object[] {
      Double.valueOf(mean), Double.valueOf(Math.sqrt(variance / numPrincipleVectors)) });
  }
  



  public SortedMultiMap<Double, String> getMostSimilar(String word, int numberOfSimilarWords)
  {
    Vector v = sspace.getVector(word);
    
    if (v == null) {
      return null;
    }
    

    SortedMultiMap<Double, String> mostSim = 
      getMostSimilar(v, numberOfSimilarWords + 1);
    Iterator<Map.Entry<Double, String>> iter = mostSim.entrySet().iterator();
    while (iter.hasNext()) {
      Map.Entry<Double, String> e = (Map.Entry)iter.next();
      if (word.equals(e.getValue())) {
        iter.remove();
        break;
      }
    }
    return mostSim;
  }
  



  public SortedMultiMap<Double, String> getMostSimilar(Set<String> terms, int numberOfSimilarWords)
  {
    if (terms.isEmpty()) {
      return null;
    }
    DoubleVector mean = new DenseVector(sspace.getVectorLength());
    int found = 0;
    for (String term : terms) {
      Vector v = sspace.getVector(term);
      if (v == null) {
        LoggerUtil.info(LOGGER, "No vector for term " + term, new Object[0]);
      } else {
        VectorMath.add(mean, v);
        found++;
      }
    }
    
    if (found == 0) {
      return null;
    }
    



    SortedMultiMap<Double, String> mostSim = 
      getMostSimilar(mean, numberOfSimilarWords + terms.size());
    Object iter = mostSim.entrySet().iterator();
    Set<Map.Entry<Double, String>> toRemove = 
      new HashSet();
    while (((Iterator)iter).hasNext()) {
      Map.Entry<Double, String> e = (Map.Entry)((Iterator)iter).next();
      if (terms.contains(e.getValue()))
        toRemove.add(e);
    }
    for (Map.Entry<Double, String> e : toRemove) {
      mostSim.remove(e.getKey(), e.getValue());
    }
    

    while (mostSim.size() > numberOfSimilarWords) {
      mostSim.remove((Double)mostSim.firstKey());
    }
    return mostSim;
  }
  




  public SortedMultiMap<Double, String> getMostSimilar(final Vector v, int numberOfSimilarWords)
  {
    if (v == null)
      return null;
    final int k = numberOfSimilarWords;
    



    SortedMultiMap<Double, Map.Entry<DoubleVector, Set<String>>> mostSimilarPrincipleVectors = new BoundedSortedMultiMap(
      k, false);
    
    Iterator localIterator1 = principleVectorToNearestTerms.asMap().entrySet().iterator();
    while (localIterator1.hasNext()) {
      Map.Entry<DoubleVector, Set<String>> e = (Map.Entry)localIterator1.next();
      DoubleVector pVec = (DoubleVector)e.getKey();
      double sim = Similarity.cosineSimilarity(v, pVec);
      mostSimilarPrincipleVectors.put(Double.valueOf(sim), e);
    }
    


    final SortedMultiMap<Double, String> kMostSimTerms = 
      new BoundedSortedMultiMap(k, false);
    


    Object taskId = workQueue.registerTaskGroup(
      mostSimilarPrincipleVectors.values().size());
    int termsCompared = 0;
    int i = 0;
    
    Iterator localIterator2 = mostSimilarPrincipleVectors.values().iterator();
    while (localIterator2.hasNext()) {
      Map.Entry<DoubleVector, Set<String>> e = (Map.Entry)localIterator2.next();
      final Set<String> terms = (Set)e.getValue();
      termsCompared += terms.size();
      

      int i_ = i++;
      workQueue.add(taskId, new Runnable()
      {
        public void run()
        {
          SortedMultiMap<Double, String> localMostSimTerms = 
            new BoundedSortedMultiMap(k, false);
          Vector tVec; for (String term : terms) {
            tVec = sspace.getVector(term);
            double sim = Similarity.cosineSimilarity(v, tVec);
            localMostSimTerms.put(Double.valueOf(sim), term);
          }
          
          synchronized (kMostSimTerms)
          {
            tVec = localMostSimTerms.entrySet().iterator();
            while (tVec.hasNext()) {
              Object e = (Map.Entry)tVec.next();
              kMostSimTerms.put((Double)((Map.Entry)e).getKey(), (String)((Map.Entry)e).getValue());
            }
          }
        }
      });
    }
    workQueue.await(taskId);
    LoggerUtil.verbose(LOGGER, "Compared %d of the total %d terms to find the %d-nearest neighbors", new Object[] {
      Integer.valueOf(termsCompared), 
      Integer.valueOf(sspace.getWords().size()), Integer.valueOf(k) });
    
    return kMostSimTerms;
  }
  



  private void readObject(ObjectInputStream ois)
    throws ClassNotFoundException, IOException
  {
    ois.defaultReadObject();
    
    workQueue = new WorkQueue();
    sspace = ((SemanticSpace)ois.readObject());
  }
  


  private void writeObject(ObjectOutputStream out)
    throws IOException
  {
    out.defaultWriteObject();
    

    if ((sspace instanceof Serializable)) {
      out.writeObject(sspace);

    }
    else
    {
      LoggerUtil.verbose(LOGGER, "%s is not serializable, so writing a copy of the data", new Object[] {
        sspace.getSpaceName() });
      Map<String, Vector> termToVector = 
        new HashMap(sspace.getWords().size());
      for (String term : sspace.getWords())
        termToVector.put(term, sspace.getVector(term));
      SemanticSpace copy = new VectorMapSemanticSpace(
        termToVector, "copy of " + sspace.getSpaceName(), 
        sspace.getVectorLength());
      out.writeObject(copy);
    }
  }
}
