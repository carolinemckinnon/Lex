package edu.ucla.sspace.clustering.criterion;





















public class H1Function
  extends HybridBaseFunction
{
  public H1Function() {}
  



















  protected BaseFunction getInternalFunction()
  {
    return new I1Function(matrix, centroids, i1Costs, 
      assignments, clusterSizes);
  }
  


  protected BaseFunction getExternalFunction()
  {
    return new E1Function(matrix, centroids, e1Costs, 
      assignments, clusterSizes, 
      completeCentroid, simToComplete);
  }
  


  public boolean isMaximize()
  {
    return true;
  }
}
