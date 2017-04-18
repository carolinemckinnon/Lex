package edu.ucla.sspace.clustering;

import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.matrix.SparseMatrix;
import edu.ucla.sspace.vector.CompactSparseVector;
import edu.ucla.sspace.vector.DenseVector;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.ScaledDoubleVector;
import edu.ucla.sspace.vector.ScaledSparseDoubleVector;
import edu.ucla.sspace.vector.SparseDoubleVector;
import edu.ucla.sspace.vector.VectorMath;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;





































public class Assignments
  implements Iterable<Assignment>
{
  private static final Logger LOGGER = Logger.getLogger(Assignments.class.getName());
  



  private Assignment[] assignments;
  



  private int numClusters;
  



  private Matrix matrix;
  


  private int[] counts;
  



  public Assignments(int numClusters, int numAssignments)
  {
    this(numClusters, numAssignments, null);
  }
  



  public Assignments(int numClusters, int numAssignments, Matrix matrix)
  {
    this.numClusters = numClusters;
    this.matrix = matrix;
    assignments = new Assignment[numAssignments];
  }
  






  public Assignments(int numClusters, Assignment[] initialAssignments)
  {
    this(numClusters, initialAssignments, null);
  }
  





  public Assignments(int numClusters, Assignment[] initialAssignments, Matrix matrix)
  {
    this.numClusters = numClusters;
    this.matrix = matrix;
    assignments = initialAssignments;
  }
  


  public void set(int i, Assignment assignment)
  {
    assignments[i] = assignment;
  }
  


  public int size()
  {
    return assignments.length;
  }
  


  public Iterator<Assignment> iterator()
  {
    return Arrays.asList(assignments).iterator();
  }
  


  public Assignment get(int i)
  {
    return assignments[i];
  }
  


  public int numClusters()
  {
    return numClusters;
  }
  


  public Assignment[] assignments()
  {
    return assignments;
  }
  





  public List<Set<Integer>> clusters()
  {
    List<Set<Integer>> clusters = new ArrayList();
    for (int c = 0; c < numClusters; c++)
      clusters.add(new HashSet());
    for (int i = 0; i < assignments.length; i++) {
      for (int k : assignments[i].assignments())
      {
        if (k >= 0)
          ((Set)clusters.get(k)).add(Integer.valueOf(i)); }
    }
    return clusters;
  }
  





  public DoubleVector[] getCentroids()
  {
    if (matrix == null) {
      throw new IllegalArgumentException(
        "The data matrix was not passed to Assignments.");
    }
    
    DoubleVector[] centroids = new DoubleVector[numClusters];
    counts = new int[numClusters];
    for (int c = 0; c < numClusters; c++) {
      centroids[c] = new DenseVector(matrix.columns());
    }
    

    int row = 0;
    for (Assignment assignment : assignments) {
      if (assignment.length() != 0)
      {

        int clus = assignment.assignments()[0];
        

        if (clus >= 0)
        {
          counts[clus] += 1;
          DoubleVector centroid = centroids[assignment.assignments()[0]];
          VectorMath.add(centroid, matrix.getRowVector(row));
        }
      } else { row++;
      }
    }
    
    for (int c = 0; c < numClusters; c++) {
      if (counts[c] != 0)
        centroids[c] = new ScaledDoubleVector(
          centroids[c], 1.0D / counts[c]);
    }
    return centroids;
  }
  






  public SparseDoubleVector[] getSparseCentroids()
  {
    if (matrix == null)
      throw new IllegalArgumentException(
        "The data matrix was not passed to Assignments.");
    if (!(matrix instanceof SparseMatrix)) {
      LOGGER.fine(
        "The underlying matrix that was clustered is not sparse, so sparse centroids may not be sparse");
    }
    


    SparseDoubleVector[] centroids = new SparseDoubleVector[numClusters];
    

    if (numClusters == 0) {
      return centroids;
    }
    counts = new int[numClusters];
    for (int c = 0; c < numClusters; c++) {
      centroids[c] = new CompactSparseVector(matrix.columns());
    }
    

    int row = 0;
    for (Assignment assignment : assignments)
    {




      if ((assignment.length() != 0) && (assignment.assignments()[0] >= 0)) {
        counts[assignment.assignments()[0]] += 1;
        DoubleVector centroid = centroids[assignment.assignments()[0]];
        VectorMath.add(centroid, matrix.getRowVector(row));
      }
      row++;
    }
    

    for (int c = 0; c < numClusters; c++) {
      if (counts[c] != 0)
        centroids[c] = new ScaledSparseDoubleVector(
          centroids[c], 1.0D / counts[c]);
    }
    return centroids;
  }
  
  public int[] clusterSizes() {
    return counts;
  }
}
