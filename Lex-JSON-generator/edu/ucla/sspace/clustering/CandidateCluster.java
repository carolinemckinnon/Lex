package edu.ucla.sspace.clustering;

import edu.ucla.sspace.util.primitive.IntSet;
import edu.ucla.sspace.util.primitive.TroveIntSet;
import edu.ucla.sspace.vector.DenseVector;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.SparseHashDoubleVector;
import edu.ucla.sspace.vector.SparseVector;
import edu.ucla.sspace.vector.VectorMath;







































class CandidateCluster
{
  private final IntSet indices;
  private DoubleVector sumVector;
  private DoubleVector centroid;
  
  public CandidateCluster()
  {
    indices = new TroveIntSet();
    centroid = null;
  }
  



  public DoubleVector centerOfMass()
  {
    if (centroid == null) {
      if (indices.size() == 1) {
        centroid = sumVector;



      }
      else
      {



        int length = sumVector.length();
        double d = 1.0D / indices.size();
        if ((sumVector instanceof SparseVector)) {
          centroid = new SparseHashDoubleVector(length);
          SparseVector sv = (SparseVector)sumVector;
          for (int nz : sv.getNonZeroIndices()) {
            centroid.set(nz, sumVector.get(nz) * d);
          }
        } else {
          centroid = new DenseVector(length);
          for (int i = 0; i < length; i++)
            centroid.set(i, sumVector.get(i) * d);
        }
      }
    }
    return centroid;
  }
  


  public void add(int index, DoubleVector v)
  {
    boolean added = indices.add(index);
    assert (added) : "Adding duplicate indices to candidate facility";
    if (sumVector == null) {
      sumVector = ((v instanceof SparseVector) ? 
        new SparseHashDoubleVector(v) : 
        new DenseVector(v));
    }
    else {
      VectorMath.add(sumVector, v);
      centroid = null;
    }
  }
  
  public int hashCode() {
    return indices.hashCode();
  }
  
  public boolean equals(Object o) {
    if ((o instanceof CandidateCluster)) {
      CandidateCluster f = (CandidateCluster)o;
      return indices.equals(indices);
    }
    return false;
  }
  


  public IntSet indices()
  {
    return indices;
  }
  


  public void merge(CandidateCluster other)
  {
    indices.addAll(indices);
    VectorMath.add(sumVector, sumVector);
    centroid = null;
  }
  


  public int size()
  {
    return indices.size();
  }
  



  public DoubleVector sum()
  {
    return sumVector;
  }
}
