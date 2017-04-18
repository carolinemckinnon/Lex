package edu.ucla.sspace.clustering;

import edu.ucla.sspace.common.Similarity;
import edu.ucla.sspace.common.Similarity.SimType;
import edu.ucla.sspace.matrix.Matrices;
import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.matrix.SparseMatrix;
import edu.ucla.sspace.util.BoundedSortedMultiMap;
import edu.ucla.sspace.util.Duple;
import edu.ucla.sspace.util.MultiMap;
import edu.ucla.sspace.vector.CompactSparseVector;
import edu.ucla.sspace.vector.SparseDoubleVector;
import edu.ucla.sspace.vector.VectorMath;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;









































































































































































public class ClusteringByCommittee
  implements Clustering
{
  private static final String PROPERTY_PREFIX = "edu.ucla.sspace.clustering.ClusteringByCommittee";
  public static final String AVERGAGE_LINK_MERGE_THRESHOLD_PROPERTY = "edu.ucla.sspace.clustering.ClusteringByCommittee.averageLinkMergeThreshold";
  public static final String DEFAULT_AVERGAGE_LINK_MERGE_THRESHOLD = ".25";
  public static final String COMMITTEE_SIMILARITY_THRESHOLD_PROPERTY = "edu.ucla.sspace.clustering.ClusteringByCommittee.maxCommitteeSimilarity";
  public static final String DEFAULT_COMMITTEE_SIMILARITY_THRESHOLD = ".35";
  public static final String RESIDUE_SIMILARITY_THRESHOLD_PROPERTY = "edu.ucla.sspace.clustering.ClusteringByCommittee.residueSimilarityThreshold";
  public static final String DEFAULT_RESIDUE_SIMILARITY_THRESHOLD = ".25";
  public static final String SOFT_CLUSTERING_SIMILARITY_THRESHOLD_PROPERTY = "edu.ucla.sspace.clustering.ClusteringByCommittee.softClusteringThreshold";
  public static final String DEFAULT_SOFT_CLUSTERING_SIMILARITY_THRESHOLD = ".25";
  public static final String HARD_CLUSTERING_PROPERTY = "edu.ucla.sspace.clustering.ClusteringByCommittee.useHardClustering";
  private static final Logger LOGGER = Logger.getLogger(ClusteringByCommittee.class.getName());
  





  private static final int K_MOST_SIMILAR_NEIGHBORS = 10;
  






  public ClusteringByCommittee() {}
  





  public Assignments cluster(Matrix m, int numClusters, Properties props)
  {
    LOGGER.warning("CBC does not take in a specified number of clusters.  Ignoring specification and clustering anyway.");
    
    return cluster(m, props);
  }
  







  public Assignments cluster(Matrix m, Properties props)
  {
    double avgLinkMergeThresh = Double.parseDouble(props.getProperty(
      "edu.ucla.sspace.clustering.ClusteringByCommittee.averageLinkMergeThreshold", 
      ".25"));
    double maxCommitteeSimThresh = Double.parseDouble(props.getProperty(
      "edu.ucla.sspace.clustering.ClusteringByCommittee.maxCommitteeSimilarity", 
      ".35"));
    double residueSimThresh = Double.parseDouble(props.getProperty(
      "edu.ucla.sspace.clustering.ClusteringByCommittee.residueSimilarityThreshold", 
      ".25"));
    double softClusteringThresh = Double.parseDouble(props.getProperty(
      "edu.ucla.sspace.clustering.ClusteringByCommittee.softClusteringThreshold", 
      ".25"));
    boolean useHardClustering = Boolean.parseBoolean(
      props.getProperty("edu.ucla.sspace.clustering.ClusteringByCommittee.useHardClustering", "true"));
    
    LOGGER.info("Starting Clustering By Committee");
    

    if (!(m instanceof SparseMatrix)) {
      throw new IllegalArgumentException("CBC only accepts sparse matrices");
    }
    SparseMatrix sm = (SparseMatrix)m;
    



    BitSet allRows = new BitSet(sm.rows());
    allRows.set(0, sm.rows());
    LOGGER.info("CBC begining Phase 2");
    List<Committee> committees = phase2(
      sm, allRows, avgLinkMergeThresh, 
      maxCommitteeSimThresh, residueSimThresh);
    
    LOGGER.info("CBC begining Phase 3");
    
    Assignment[] result = new Assignment[m.rows()];
    for (int r = 0; r < m.rows(); r++) {
      LOGGER.fine("Computing Phase 3 for row " + r);
      SparseDoubleVector row = sm.getRowVector(r);
      
      List<Integer> committeeIds = phase3(
        row, committees, useHardClustering, softClusteringThresh);
      int[] assignments = new int[committeeIds.size()];
      for (int i = 0; i < committeeIds.size(); i++) {
        assignments[i] = ((Integer)committeeIds.get(i)).intValue();
      }
      result[r] = new SoftAssignment(assignments);
    }
    return new Assignments(committees.size(), result, m);
  }
  





























  private static List<Committee> phase2(SparseMatrix sm, BitSet rowsToConsider, double avgLinkMergeThresh, double maxCommitteeSimThresh, double residueSimThresh)
  {
    if (LOGGER.isLoggable(Level.FINE)) {
      LOGGER.fine("CBC computing Phase 2 for " + 
        rowsToConsider.cardinality() + " rows");
    }
    
    List<CandidateCommittee> candidateCommittees = 
      new ArrayList();
    
    List<CandidateCommittee> commsForRow;
    
    for (int r = rowsToConsider.nextSetBit(0); r >= 0; 
        r = rowsToConsider.nextSetBit(r + 1))
    {


      MultiMap<Double, Integer> mostSimilarElements = 
        new BoundedSortedMultiMap(10);
      
      for (int r2 = rowsToConsider.nextSetBit(0); r2 >= 0; 
          r2 = rowsToConsider.nextSetBit(r2 + 1))
      {
        if (r != r2)
        {

          double sim = Similarity.cosineSimilarity(sm.getRowVector(r), 
            sm.getRowVector(r2));
          mostSimilarElements.put(Double.valueOf(sim), Integer.valueOf(r2));
        }
      }
      
      if (mostSimilarElements.size() != 0)
      {





        commsForRow = buildCommitteesForRow(
          mostSimilarElements.values(), sm, avgLinkMergeThresh);
        Collections.sort(commsForRow);
        

        candidateCommittees.add((CandidateCommittee)commsForRow.get(0));
      }
    }
    

    Collections.sort(candidateCommittees);
    


    List<Committee> committees = new ArrayList();
    

    for (CandidateCommittee cc : candidateCommittees)
    {







      boolean isDissimilar = true;
      for (Committee c : committees)
      {
        if (Similarity.cosineSimilarity(cc.centroid(), c.centroid()) >= maxCommitteeSimThresh) {
          isDissimilar = false;
        }
      }
      
      if (isDissimilar) {
        committees.add(new Committee(cc));
      }
    }
    
    LOGGER.log(Level.FINE, 
      "Found {0} committees.", new Object[] { Integer.valueOf(committees.size()) });
    


    if (committees.isEmpty()) {
      return committees;
    }
    Set<Integer> residues = new HashSet();
    
    SparseDoubleVector row;
    
    for (int r = rowsToConsider.nextSetBit(0); r >= 0; 
        r = rowsToConsider.nextSetBit(r + 1))
    {


      boolean isResidue = true;
      row = sm.getRowVector(r);
      for (Committee c : committees)
      {
        if (Similarity.cosineSimilarity(c.centroid(), row) >= residueSimThresh) {
          isResidue = false;
        }
      }
      if (isResidue) {
        residues.add(Integer.valueOf(r));
      }
    }
    if ((LOGGER.isLoggable(Level.FINER)) && (!residues.isEmpty())) {
      LOGGER.finer("Found residual elements: " + residues);
    }
    


    if (residues.isEmpty()) {
      return committees;
    }
    


    if (residues.size() == 1) {
      return committees;
    }
    BitSet b = new BitSet(sm.rows());
    for (Integer i : residues) {
      b.set(i.intValue());
    }
    

    committees.addAll(phase2(sm, b, avgLinkMergeThresh, 
      maxCommitteeSimThresh, residueSimThresh));
    

    return committees;
  }
  


















  private static List<Integer> phase3(SparseDoubleVector row, List<Committee> committees, boolean useHardClustering, double softClusteringThresh)
  {
    if (useHardClustering) {
      int mostSimilarCommittee = -1;
      double highestSim = -1.0D;
      for (int i = 0; i < committees.size(); i++) {
        Committee c = (Committee)committees.get(i);
        double sim = Similarity.cosineSimilarity(row, c.centroid());
        if (sim > highestSim) {
          highestSim = sim;
          mostSimilarCommittee = i;
        }
      }
      return Collections.singletonList(Integer.valueOf(mostSimilarCommittee));
    }
    


    SparseDoubleVector copy = new CompactSparseVector(row);
    

    List<Integer> assignedClusters = new ArrayList();
    

    MultiMap<Double, Duple<Committee, Integer>> mostSimilarCommittees = 
      new BoundedSortedMultiMap(200);
    Committee c;
    for (int i = 0; i < committees.size(); i++) {
      c = (Committee)committees.get(i);
      mostSimilarCommittees.put(
        Double.valueOf(Similarity.cosineSimilarity(row, c.centroid())), 
        new Duple(c, Integer.valueOf(i)));
    }
    





    for (Duple<Committee, Integer> p : mostSimilarCommittees.values()) {
      Committee c = (Committee)x;
      Integer comId = (Integer)y;
      
      SparseDoubleVector centroid = c.centroid();
      

      if (Similarity.cosineSimilarity(copy, centroid) >= 0.0D)
      {




        boolean isSimilar = false;
        Committee c2; for (Integer committeeId : assignedClusters) {
          c2 = (Committee)committees.get(committeeId.intValue());
          
          if (Similarity.cosineSimilarity(c2.centroid(), centroid) >= softClusteringThresh) {
            isSimilar = true;
            break;
          }
        }
        if (!isSimilar)
        {
          assignedClusters.add(comId);
          
          int[] arrayOfInt;
          
          c2 = (arrayOfInt = centroid.getNonZeroIndices()).length; for (Committee localCommittee1 = 0; localCommittee1 < c2; localCommittee1++) { int i = arrayOfInt[localCommittee1];
            copy.set(i, 0.0D);
          }
        }
      } }
    return assignedClusters;
  }
  










  public static List<CandidateCommittee> buildCommitteesForRow(Collection<Integer> rows, SparseMatrix sm, double avgLinkMergeThresh)
  {
    if (rows.size() == 0) {
      return new ArrayList();
    }
    double AVG_LINK_MERGE_THRESHOLD = 0.25D;
    


    List<SparseDoubleVector> v = new ArrayList();
    for (Integer neighbor : rows) {
      v.add(sm.getRowVector(neighbor.intValue()));
    }
    int[] assignments = HierarchicalAgglomerativeClustering.clusterRows(
      Matrices.asSparseMatrix(v), AVG_LINK_MERGE_THRESHOLD, 
      HierarchicalAgglomerativeClustering.ClusterLinkage.MEAN_LINKAGE, Similarity.SimType.COSINE);
    

    Object clusters = 
      new HashMap();
    
    int i = 0;
    int clusterId; for (Integer row : rows) {
      clusterId = assignments[i];
      Set<Integer> cluster = (Set)((Map)clusters).get(Integer.valueOf(clusterId));
      if (cluster == null) {
        cluster = new HashSet();
        ((Map)clusters).put(Integer.valueOf(clusterId), cluster);
      }
      cluster.add(row);
      i++;
    }
    

    List<CandidateCommittee> candidates = 
      new ArrayList();
    
    for (Object cluster : ((Map)clusters).values()) {
      candidates.add(new CandidateCommittee((Set)cluster, sm));
    }
    return candidates;
  }
  


  private static class Committee
  {
    private final ClusteringByCommittee.CandidateCommittee cc;
    


    public Committee(ClusteringByCommittee.CandidateCommittee cc)
    {
      this.cc = cc;
    }
    


    public SparseDoubleVector centroid()
    {
      return cc.centroid();
    }
    
    public boolean equals(Object o) {
      return ((o instanceof Committee)) && 
        (cc.equals(cc));
    }
    
    public int hashCode() {
      return cc.hashCode();
    }
    
    public String toString() {
      return cc.toString();
    }
  }
  






  private static class CandidateCommittee
    implements Comparable<CandidateCommittee>
  {
    private final Set<Integer> rows;
    




    private final SparseDoubleVector centroid;
    




    private final double score;
    





    public CandidateCommittee(Set<Integer> rows, SparseMatrix sm)
    {
      this.rows = rows;
      
      centroid = new CompactSparseVector(sm.columns());
      double simSum = 0.0D;
      Iterator localIterator2; int r2; for (Iterator localIterator1 = rows.iterator(); localIterator1.hasNext(); 
          


          localIterator2.hasNext())
      {
        int r = ((Integer)localIterator1.next()).intValue();
        SparseDoubleVector row = sm.getRowVector(r);
        VectorMath.add(centroid, row);
        
        localIterator2 = rows.iterator(); continue;r2 = ((Integer)localIterator2.next()).intValue();
        if (r != r2)
        {

          simSum = simSum + Similarity.cosineSimilarity(row, sm.getRowVector(r2));
        }
      }
      double denom = 1.0D / rows.size();
      for (int nz : centroid.getNonZeroIndices()) {
        centroid.set(nz, centroid.get(nz) / denom);
      }
      






      double avgSim = rows.size() == 1 ? 0.0D : 
        simSum / (rows.size() * rows.size() - rows.size());
      score = (rows.size() * avgSim);
    }
    


    public SparseDoubleVector centroid()
    {
      return centroid;
    }
    



    public int compareTo(CandidateCommittee c)
    {
      return -Double.compare(score, score);
    }
    
    public boolean equals(Object o) {
      if ((o instanceof CandidateCommittee)) {
        CandidateCommittee cc = (CandidateCommittee)o;
        return rows.equals(rows);
      }
      return false;
    }
    
    public int hashCode() {
      return rows.hashCode();
    }
    



    public double score()
    {
      return score;
    }
    
    public String toString() {
      return 
        "Committee {rows=" + rows + ", score=" + score + ", centroid=" + centroid + "}";
    }
  }
}
