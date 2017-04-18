package edu.ucla.sspace.clustering;

import edu.ucla.sspace.common.Similarity;
import edu.ucla.sspace.matrix.AbstractMatrix;
import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.matrix.SparseHashMatrix;
import edu.ucla.sspace.matrix.SparseMatrix;
import edu.ucla.sspace.matrix.SparseSymmetricMatrix;
import edu.ucla.sspace.util.HashMultiMap;
import edu.ucla.sspace.util.MultiMap;
import edu.ucla.sspace.util.WorkQueue;
import edu.ucla.sspace.vector.DenseVector;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.SparseDoubleVector;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.logging.Level;
import java.util.logging.Logger;































































































public class LinkClustering
  implements Clustering, Serializable
{
  private static final long serialVersionUID = 1L;
  public static final String PROPERTY_PREFIX = "edu.ucla.sspace.clustering.LinkClustering";
  public static final String KEEP_SIMILARITY_MATRIX_IN_MEMORY_PROPERTY = "edu.ucla.sspace.clustering.LinkClustering.keepSimilarityMatrixInMemory";
  private static final Logger LOGGER = Logger.getLogger(LinkClustering.class.getName());
  



  private final WorkQueue workQueue;
  



  private List<Merge> mergeOrder;
  



  private List<Edge> edgeList;
  



  private int numRows;
  




  public LinkClustering()
  {
    mergeOrder = null;
    edgeList = null;
    numRows = 0;
    workQueue = WorkQueue.getWorkQueue();
  }
  










  public Assignments cluster(Matrix matrix, int numClusters, Properties props)
  {
    LOGGER.warning("Link clustering does not take a specified number of clusters.  Clustering the matrix anyway.");
    
    return cluster(matrix, props);
  }
  





  public Assignments cluster(Matrix matrix, Properties props)
  {
    if (matrix.rows() != matrix.columns()) {
      throw new IllegalArgumentException("Input matrix is not square. Matrix is expected to be a square matrix whose values (i,j) denote an edge from row i to row j");
    }
    
    if (!(matrix instanceof SparseMatrix)) {
      throw new IllegalArgumentException("Input matrix must be a sparse matrix.");
    }
    
    SparseMatrix sm = (SparseMatrix)matrix;
    
    String inMemProp = 
      props.getProperty("edu.ucla.sspace.clustering.LinkClustering.keepSimilarityMatrixInMemory");
    boolean keepSimMatrixInMem = inMemProp != null ? 
      Boolean.parseBoolean(inMemProp) : true;
    







    final int rows = sm.rows();
    numRows = rows;
    LOGGER.fine("Generating link similarity matrix for " + rows + " nodes");
    



    final List<Edge> edgeList = new ArrayList();
    this.edgeList = edgeList;
    
    for (int r = 0; r < rows; r++) {
      SparseDoubleVector row = sm.getRowVector(r);
      int[] edges = row.getNonZeroIndices();
      for (int col : edges)
      {
        if (r > col) {
          edgeList.add(new Edge(r, col));


        }
        else if ((r < col) && (sm.get(col, r) == 0.0D)) {
          edgeList.add(new Edge(r, col));
        }
      }
    }
    final int numEdges = edgeList.size();
    LOGGER.fine("Number of edges to cluster: " + numEdges);
    
    Matrix edgeSimMatrix = 
      getEdgeSimMatrix(edgeList, sm, keepSimMatrixInMem);
    
    LOGGER.fine("Computing single linkage link clustering");
    
    final List<Merge> mergeOrder = 
      new HierarchicalAgglomerativeClustering()
      .buildDendrogram(edgeSimMatrix, HierarchicalAgglomerativeClustering.ClusterLinkage.SINGLE_LINKAGE);
    this.mergeOrder = mergeOrder;
    
    LOGGER.fine("Calculating partition densitities");
    



    final ConcurrentNavigableMap<Double, Integer> partitionDensities = 
      new ConcurrentSkipListMap();
    


    Object key = workQueue.registerTaskGroup(mergeOrder.size());
    for (int p = 0; p < mergeOrder.size(); p++) {
      final int part = p;
      workQueue.add(key, new Runnable()
      {
        public void run()
        {
          List<Merge> mergeSteps = mergeOrder.subList(0, part);
          

          MultiMap<Integer, Integer> clusterToElements = 
            LinkClustering.convertMergesToAssignments(mergeSteps, numEdges);
          


          double partitionDensitySum = 0.0D;
          for (Integer cluster : clusterToElements.keySet()) {
            Set<Integer> linkPartition = 
              clusterToElements.get(cluster);
            int numLinks = linkPartition.size();
            BitSet nodesInPartition = new BitSet(rows);
            for (Integer linkIndex : linkPartition) {
              LinkClustering.Edge link = (LinkClustering.Edge)edgeList.get(linkIndex.intValue());
              nodesInPartition.set(from);
              nodesInPartition.set(to);
            }
            int numNodes = nodesInPartition.cardinality();
            

            double partitionDensity = 
              (numLinks - (numNodes - 1.0D)) / (
              numNodes * (numNodes - 1.0D) / 2.0D - (
              numLinks - 1));
            partitionDensitySum += partitionDensity;
          }
          

          double partitionDensity = 
            2.0D / numEdges * partitionDensitySum;
          LinkClustering.LOGGER.log(Level.FINER, "Partition solution {0} had density {1}", 
          
            new Object[] { Integer.valueOf(part), Double.valueOf(partitionDensity) });
          


          partitionDensities.put(Double.valueOf(partitionDensity), Integer.valueOf(part));
        }
      });
    }
    


    workQueue.await(key);
    
    Object densest = partitionDensities.lastEntry();
    LOGGER.fine("Partition " + ((Map.Entry)densest).getValue() + 
      " had the highest density: " + ((Map.Entry)densest).getKey());
    int partitionWithMaxDensity = ((Integer)((Map.Entry)densest).getValue()).intValue();
    


    MultiMap<Integer, Integer> bestEdgeAssignment = 
      convertMergesToAssignments(
      mergeOrder.subList(0, partitionWithMaxDensity), numEdges);
    
    List<Set<Integer>> nodeClusters = new ArrayList(rows);
    for (int i = 0; i < rows; i++) {
      nodeClusters.add(new HashSet());
    }
    

    int clusterId = 0;
    


    for (Integer cluster : bestEdgeAssignment.keySet()) {
      Set<Integer> edgePartition = bestEdgeAssignment.get(cluster);
      for (Integer edgeId : edgePartition) {
        Edge e = (Edge)edgeList.get(edgeId.intValue());
        ((Set)nodeClusters.get(from)).add(Integer.valueOf(clusterId));
        ((Set)nodeClusters.get(to)).add(Integer.valueOf(clusterId));
      }
      
      clusterId++;
    }
    
    int numClusters = 0;
    Assignment[] nodeAssignments = new Assignment[rows];
    for (int i = 0; i < nodeAssignments.length; i++) {
      nodeAssignments[i] = 
        new SoftAssignment((Collection)nodeClusters.get(i));
    }
    return new Assignments(numClusters, nodeAssignments, matrix);
  }
  




  private Matrix getEdgeSimMatrix(List<Edge> edgeList, SparseMatrix sm, boolean keepSimilarityMatrixInMemory)
  {
    return keepSimilarityMatrixInMemory ? 
      calculateEdgeSimMatrix(edgeList, sm) : 
      new LazySimilarityMatrix(edgeList, sm);
  }
  










  private Matrix calculateEdgeSimMatrix(final List<Edge> edgeList, final SparseMatrix sm)
  {
    final int numEdges = edgeList.size();
    final Matrix edgeSimMatrix = 
      new SparseSymmetricMatrix(
      new SparseHashMatrix(numEdges, numEdges));
    
    Object key = workQueue.registerTaskGroup(numEdges);
    for (int i = 0; i < numEdges; i++) {
      final int row = i;
      workQueue.add(key, new Runnable() {
        public void run() {
          for (int j = row; j < numEdges; j++) {
            LinkClustering.Edge e1 = (LinkClustering.Edge)edgeList.get(row);
            LinkClustering.Edge e2 = (LinkClustering.Edge)edgeList.get(j);
            
            double sim = getEdgeSimilarity(sm, e1, e2);
            
            if (sim > 0.0D)
            {
              edgeSimMatrix.set(row, j, sim);
            }
          }
        }
      });
    }
    workQueue.await(key);
    return edgeSimMatrix;
  }
  












  private static MultiMap<Integer, Integer> convertMergesToAssignments(List<Merge> merges, int numOriginalClusters)
  {
    MultiMap<Integer, Integer> clusterToElements = 
      new HashMultiMap();
    for (int i = 0; i < numOriginalClusters; i++) {
      clusterToElements.put(Integer.valueOf(i), Integer.valueOf(i));
    }
    for (Merge m : merges) {
      clusterToElements.putMany(Integer.valueOf(m.remainingCluster()), 
        clusterToElements.remove(Integer.valueOf(m.mergedCluster())));
    }
    
    return clusterToElements;
  }
  





















  protected double getEdgeSimilarity(SparseMatrix sm, Edge e1, Edge e2)
  {
    int keystone = -1;
    int impost1 = -1;
    int impost2 = -1;
    if (from == from) {
      keystone = from;
      impost1 = to;
      impost2 = to;
    }
    else if (from == to) {
      keystone = from;
      impost1 = to;
      impost2 = from;
    }
    else if (to == from) {
      keystone = from;
      impost1 = to;
      impost2 = from;
    }
    else if (to == to) {
      keystone = to;
      impost1 = from;
      impost2 = from;
    }
    else {
      return 0.0D;
    }
    
    int[] impost1edges = getImpostNeighbors(sm, impost1);
    int[] impost2edges = getImpostNeighbors(sm, impost2);
    double similarity = Similarity.jaccardIndex(impost1edges, impost2edges);
    return similarity;
  }
  



  private static int[] getImpostNeighbors(SparseMatrix sm, int rowIndex)
  {
    int[] impost1edges = sm.getRowVector(rowIndex).getNonZeroIndices();
    int[] neighbors = Arrays.copyOf(impost1edges, impost1edges.length + 1);
    neighbors[(neighbors.length - 1)] = rowIndex;
    return neighbors;
  }
  


  public double getSolutionDensity(int solutionNum)
  {
    if ((solutionNum < 0) || (solutionNum >= mergeOrder.size())) {
      throw new IllegalArgumentException(
        "not a valid solution: " + solutionNum);
    }
    if ((mergeOrder == null) || (edgeList == null)) {
      throw new IllegalStateException(
        "initial clustering solution is not valid yet");
    }
    
    int numEdges = edgeList.size();
    

    List<Merge> mergeSteps = 
      mergeOrder.subList(0, solutionNum);
    

    MultiMap<Integer, Integer> clusterToElements = 
      convertMergesToAssignments(mergeSteps, numEdges);
    

    double partitionDensitySum = 0.0D;
    for (Integer cluster : clusterToElements.keySet()) {
      Set<Integer> linkPartition = clusterToElements.get(cluster);
      int numLinks = linkPartition.size();
      BitSet nodesInPartition = new BitSet(numRows);
      for (Integer linkIndex : linkPartition) {
        Edge link = (Edge)edgeList.get(linkIndex.intValue());
        nodesInPartition.set(from);
        nodesInPartition.set(to);
      }
      int numNodes = nodesInPartition.cardinality();
      

      double partitionDensity = (numLinks - (numNodes - 1.0D)) / (
        numNodes * (numNodes - 1.0D) / 2.0D - (numLinks - 1));
      partitionDensitySum += partitionDensity;
    }
    
    double partitionDensity = 2.0D / numEdges * partitionDensitySum;
    return partitionDensity;
  }
  











  public Assignments getSolution(int solutionNum)
  {
    if ((solutionNum < 0) || (solutionNum >= mergeOrder.size())) {
      throw new IllegalArgumentException(
        "not a valid solution: " + solutionNum);
    }
    if ((mergeOrder == null) || (edgeList == null)) {
      throw new IllegalStateException(
        "initial clustering solution is not valid yet");
    }
    
    int numEdges = edgeList.size();
    

    MultiMap<Integer, Integer> bestEdgeAssignment = 
      convertMergesToAssignments(
      mergeOrder.subList(0, solutionNum), numEdges);
    
    List<Set<Integer>> nodeClusters = new ArrayList(numRows);
    for (int i = 0; i < numRows; i++) {
      nodeClusters.add(new HashSet());
    }
    

    int clusterId = 0;
    


    for (Integer cluster : bestEdgeAssignment.keySet()) {
      Set<Integer> edgePartition = bestEdgeAssignment.get(cluster);
      for (Integer edgeId : edgePartition) {
        Edge e = (Edge)edgeList.get(edgeId.intValue());
        ((Set)nodeClusters.get(from)).add(Integer.valueOf(clusterId));
        ((Set)nodeClusters.get(to)).add(Integer.valueOf(clusterId));
      }
      
      clusterId++;
    }
    
    Assignment[] nodeAssignments = new Assignment[numRows];
    for (int i = 0; i < nodeAssignments.length; i++)
      nodeAssignments[i] = new SoftAssignment((Collection)nodeClusters.get(i));
    return new Assignments(clusterId, nodeAssignments);
  }
  






  public int numberOfSolutions()
  {
    return mergeOrder == null ? 0 : mergeOrder.size();
  }
  


  protected static class Edge
  {
    public final int from;
    
    public final int to;
    

    public Edge(int from, int to)
    {
      this.from = from;
      this.to = to;
    }
    
    public boolean equals(Object o) {
      if ((o instanceof Edge)) {
        Edge e = (Edge)o;
        return (from == from) && (to == to);
      }
      return false;
    }
    
    public int hashCode() {
      return from ^ to;
    }
    
    public String toString() {
      return "(" + from + "->" + to + ")";
    }
  }
  


  private class LazySimilarityMatrix
    extends AbstractMatrix
  {
    private final List<LinkClustering.Edge> edgeList;
    

    private final SparseMatrix sm;
    


    public LazySimilarityMatrix(SparseMatrix edgeList)
    {
      this.edgeList = edgeList;
      this.sm = sm;
    }
    
    public int columns() {
      return edgeList.size();
    }
    
    public double get(int row, int column) {
      LinkClustering.Edge e1 = (LinkClustering.Edge)edgeList.get(row);
      LinkClustering.Edge e2 = (LinkClustering.Edge)edgeList.get(column);
      
      double sim = getEdgeSimilarity(sm, e1, e2);
      return sim;
    }
    
    public DoubleVector getRowVector(int row) {
      int cols = columns();
      DoubleVector vec = new DenseVector(cols);
      for (int c = 0; c < cols; c++) {
        vec.set(c, get(row, c));
      }
      return vec;
    }
    
    public int rows() {
      return edgeList.size();
    }
    
    public void set(int row, int columns, double val) {
      throw new UnsupportedOperationException();
    }
  }
}
