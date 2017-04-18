package edu.ucla.sspace.clustering;

import edu.ucla.sspace.matrix.Matrices;
import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.vector.DoubleVector;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;















































public class BisectingKMeans
  implements Clustering
{
  public BisectingKMeans() {}
  
  public Assignments cluster(Matrix dataPoints, Properties props)
  {
    throw new UnsupportedOperationException(
      "KMeansClustering requires that the number of clusters be specified");
  }
  






  public Assignments cluster(Matrix dataPoints, int numClusters, Properties props)
  {
    if (numClusters <= 1) {
      Assignment[] assignments = new Assignment[dataPoints.rows()];
      for (int i = 0; i < assignments.length; i++)
        assignments[i] = new HardAssignment(0);
      return new Assignments(numClusters, assignments, dataPoints);
    }
    

    int[] numAssignments = new int[numClusters];
    



    List<List<DoubleVector>> clusters = new ArrayList(
      numClusters);
    for (int c = 0; c < numClusters; c++) {
      clusters.add(new ArrayList());
    }
    Clustering clustering = new DirectClustering();
    
    Assignment[] assignments = 
      clustering.cluster(dataPoints, 2, props).assignments();
    


    for (int i = 0; i < assignments.length; i++) {
      int assignment = assignments[i].assignments()[0];
      numAssignments[assignment] += 1;
      ((List)clusters.get(assignment)).add(dataPoints.getRowVector(i));
    }
    




    for (int k = 2; k < numClusters; k++)
    {
      int largestSize = 0;
      int largestIndex = 0;
      for (int c = 0; c < numClusters; c++) {
        if (numAssignments[c] > largestSize) {
          largestSize = numAssignments[c];
          largestIndex = c;
        }
      }
      



      List<DoubleVector> originalCluster = (List)clusters.get(largestIndex);
      List<DoubleVector> newCluster = (List)clusters.get(k);
      

      Matrix clusterToSplit = Matrices.asMatrix(originalCluster);
      Assignment[] newAssignments = 
        clustering.cluster(clusterToSplit, 2, props).assignments();
      


      originalCluster.clear();
      newCluster.clear();
      numAssignments[largestIndex] = 0;
      numAssignments[k] = 0;
      




      int i = 0; for (int j = 0; i < dataPoints.rows(); i++) {
        if (assignments[i].assignments()[0] == largestIndex)
        {

          if (newAssignments[j].assignments()[0] == 0) {
            originalCluster.add(dataPoints.getRowVector(i));
            numAssignments[largestIndex] += 1;

          }
          else
          {
            newCluster.add(dataPoints.getRowVector(i));
            assignments[i] = new HardAssignment(k);
            numAssignments[k] += 1;
          }
          j++;
        }
      }
    }
    return new Assignments(numClusters, assignments, dataPoints);
  }
  
  public String toString() {
    return "BisectingKMeans";
  }
}
