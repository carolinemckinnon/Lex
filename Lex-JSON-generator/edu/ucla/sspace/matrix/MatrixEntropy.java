package edu.ucla.sspace.matrix;

import edu.ucla.sspace.vector.SparseDoubleVector;














public class MatrixEntropy
{
  public MatrixEntropy() {}
  
  public static EntropyStats entropy(Matrix m)
  {
    if ((m instanceof SparseMatrix)) {
      return entropy((SparseMatrix)m);
    }
    double sum = 0.0D;
    double[] colSums = new double[m.columns()];
    double[] rowSums = new double[m.rows()];
    for (int r = 0; r < m.rows(); r++) {
      for (int c = 0; c < m.columns(); c++) {
        double v = m.get(r, c);
        sum += v;
        colSums[c] += v;
        rowSums[r] += v;
      }
    }
    
    double entropy = 0.0D;
    double[] colEntropy = new double[m.columns()];
    double[] rowEntropy = new double[m.rows()];
    for (int r = 0; r < m.rows(); r++) {
      for (int c = 0; c < m.columns(); c++) {
        double v = m.get(r, c);
        if (v != 0.0D) {
          entropy -= entropy(v, sum);
          colEntropy[c] -= entropy(v, colSums[c]);
          rowEntropy[r] -= entropy(v, rowSums[r]);
        }
      }
    }
    return new EntropyStats(entropy, colEntropy, rowEntropy);
  }
  



  public static EntropyStats entropy(SparseMatrix m)
  {
    double sum = 0.0D;
    double[] colSums = new double[m.columns()];
    double[] rowSums = new double[m.rows()];
    for (int r = 0; r < m.rows(); r++) {
      SparseDoubleVector sv = m.getRowVector(r);
      for (int c : sv.getNonZeroIndices()) {
        double v = m.get(r, c);
        sum += v;
        colSums[c] += v;
        rowSums[r] += v;
      }
    }
    
    double entropy = 0.0D;
    double[] colEntropy = new double[m.columns()];
    double[] rowEntropy = new double[m.rows()];
    for (int r = 0; r < m.rows(); r++) {
      SparseDoubleVector sv = m.getRowVector(r);
      for (int c : sv.getNonZeroIndices()) {
        double v = m.get(r, c);
        entropy -= entropy(v, sum);
        colEntropy[c] -= entropy(v, colSums[c]);
        rowEntropy[r] -= entropy(v, rowSums[r]);
      }
    }
    return new EntropyStats(entropy, colEntropy, rowEntropy);
  }
  



  private static double entropy(double count, double sum)
  {
    double p = count / sum;
    return Math.log(p) * p;
  }
  

  public static class EntropyStats
  {
    public double entropy;
    
    public double[] colEntropy;
    
    public double[] rowEntropy;
    

    public EntropyStats(double entropy, double[] colEntropy, double[] rowEntropy)
    {
      this.entropy = entropy;
      this.colEntropy = colEntropy;
      this.rowEntropy = rowEntropy;
    }
  }
}
