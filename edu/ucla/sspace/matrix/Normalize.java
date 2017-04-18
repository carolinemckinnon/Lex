package edu.ucla.sspace.matrix;




















public class Normalize
{
  private Normalize() {}
  


















  public static void byRow(Matrix m)
  {
    for (int i = 0; i < m.rows(); i++) {
      double rowSum = 0.0D;
      for (int j = 0; j < m.columns(); j++) {
        rowSum += m.get(i, j);
      }
      
      if (rowSum != 0.0D)
      {
        for (int j = 0; j < m.columns(); j++) {
          m.set(i, j, m.get(i, j) / rowSum);
        }
      }
    }
  }
  



  public static void byColumn(Matrix m)
  {
    for (int i = 0; i < m.columns(); i++) {
      double colSum = 0.0D;
      for (int j = 0; j < m.rows(); j++) {
        colSum += m.get(j, i);
      }
      
      if (colSum != 0.0D)
      {
        for (int j = 0; j < m.rows(); j++) {
          m.set(j, i, m.get(j, i) / colSum);
        }
      }
    }
  }
  



  public static void byLength(Matrix m)
  {
    for (int i = 0; i < m.rows(); i++) {
      double rowSum = 0.0D;
      for (int j = 0; j < m.columns(); j++)
        rowSum += Math.pow(m.get(i, j), 2.0D);
      rowSum = Math.sqrt(rowSum);
      

      if (rowSum != 0.0D)
      {
        for (int j = 0; j < m.columns(); j++) {
          m.set(i, j, m.get(i, j) / rowSum);
        }
      }
    }
  }
  



  public static void byMagnitude(Matrix m)
  {
    double totalMag = 0.0D;
    for (int i = 0; i < m.rows(); i++) {
      for (int j = 0; j < m.columns(); j++) {
        totalMag += Math.pow(m.get(i, j), 2.0D);
      }
    }
    totalMag = Math.sqrt(totalMag);
    

    if (totalMag == 0.0D)
      return;
    for (int i = 0; i < m.rows(); i++) {
      for (int j = 0; j < m.columns(); j++) {
        m.set(i, j, m.get(i, j) / totalMag);
      }
    }
  }
  








  public static void byCorrelation(Matrix m, boolean saveNegatives)
  {
    double totalSum = 0.0D;
    

    double[] rowSums = new double[m.rows()];
    double[] colSums = new double[m.columns()];
    for (int i = 0; i < m.rows(); i++) {
      for (int j = 0; j < m.columns(); j++) {
        totalSum += m.get(i, j);
        colSums[j] += m.get(i, j);
        rowSums[i] += m.get(i, j);
      }
    }
    

    for (int i = 0; i < m.rows(); i++) {
      for (int j = 0; j < m.columns(); j++) {
        double newVal = 
          (totalSum * m.get(i, j) - rowSums[i] * colSums[j]) / 
          Math.sqrt(rowSums[i] * (totalSum - rowSums[i]) * 
          colSums[j] * (totalSum - colSums[j]));
        

        if (saveNegatives) {
          m.set(i, j, newVal);
        } else {
          m.set(i, j, newVal > 0.0D ? newVal : 0.0D);
        }
      }
    }
  }
}
