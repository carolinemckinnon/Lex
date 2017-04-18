package edu.ucla.sspace.matrix;





















public class Statistics
{
  public Statistics() {}
  



















  public static enum Dimension
  {
    ALL, 
    ROW, 
    COLUMN;
  }
  















  public static Matrix std(Matrix m, Matrix average, Dimension dim)
  {
    if (average == null) {
      average = average(m, dim);
    }
    
    if (((dim == Dimension.ALL) && (
      (average.rows() != 1) || (average.columns() != 1))) || 
      ((dim == Dimension.ROW) && (
      (average.rows() != m.rows()) || (average.columns() != 1))) || (
      (dim == Dimension.COLUMN) && (
      (average.rows() != 1) || (average.columns() != m.columns())))) {
      throw new IllegalArgumentException(
        "The matrix is not properly formatted.");
    }
    
    Matrix std = null;
    if (dim == Dimension.ALL)
    {
      double variance = 0.0D;
      std = new ArrayMatrix(1, 1);
      for (int i = 0; i < m.rows(); i++) {
        for (int j = 0; j < m.columns(); j++) {
          variance += Math.pow(m.get(i, j) - average.get(0, 0), 2.0D);
        }
      }
      variance /= m.rows() * m.columns();
      std.set(0, 0, Math.sqrt(variance));
    } else if (dim == Dimension.ROW)
    {
      std = new ArrayMatrix(m.rows(), 1);
      for (int i = 0; i < m.rows(); i++) {
        double variance = 0.0D;
        for (int j = 0; j < m.columns(); j++) {
          variance += Math.pow(m.get(i, j) - average.get(i, 0), 2.0D);
        }
        variance /= m.columns();
        std.set(i, 0, Math.sqrt(variance));
      }
    }
    else if (dim == Dimension.COLUMN)
    {
      std = new ArrayMatrix(1, m.columns());
      
      for (int i = 0; i < m.rows(); i++) {
        for (int j = 0; j < m.columns(); j++) {
          double variance = std.get(0, j);
          variance += Math.pow(m.get(i, j) - average.get(0, j), 2.0D);
          std.set(0, j, variance);
        }
      }
      
      for (int i = 0; i < m.columns(); i++) {
        double variance = std.get(0, i);
        variance /= m.rows();
        std.set(0, i, Math.sqrt(variance));
      }
    }
    return std;
  }
  








  public static Matrix average(Matrix m, Dimension dim)
  {
    Matrix averageMatrix = null;
    
    if (dim == Dimension.ALL)
    {
      double average = 0.0D;
      for (int i = 0; i < m.rows(); i++) {
        for (int j = 0; j < m.columns(); j++) {
          average += m.get(i, j);
        }
      }
      averageMatrix = new ArrayMatrix(1, 1);
      average /= m.rows() * m.columns();
      averageMatrix.set(1, 1, average);
    } else if (dim == Dimension.ROW)
    {
      averageMatrix = new ArrayMatrix(m.rows(), 1);
      for (int i = 0; i < m.rows(); i++) {
        double average = 0.0D;
        for (int j = 0; j < m.columns(); j++) {
          average += m.get(i, j);
        }
        average /= m.columns();
        averageMatrix.set(i, 0, average);
      }
    } else if (dim == Dimension.COLUMN)
    {
      averageMatrix = new ArrayMatrix(1, m.columns());
      for (int i = 0; i < m.rows(); i++) {
        for (int j = 0; j < m.columns(); j++) {
          double newValue = m.get(i, j) + averageMatrix.get(0, j);
          averageMatrix.set(0, j, newValue);
        }
      }
      
      for (int i = 0; i < m.columns(); i++) {
        double average = averageMatrix.get(0, i);
        average /= m.rows();
        averageMatrix.set(0, i, average);
      }
    }
    return averageMatrix;
  }
  













  public static Matrix std(Matrix m, Matrix average, Dimension dim, int errorCode)
  {
    if (average == null) {
      average = average(m, dim, errorCode);
    }
    
    if (((dim == Dimension.ALL) && (
      (average.rows() != 1) || (average.columns() != 1))) || 
      ((dim == Dimension.ROW) && (
      (average.rows() != m.rows()) || (average.columns() != 1))) || (
      (dim == Dimension.COLUMN) && (
      (average.rows() != 1) || (average.columns() != m.columns())))) {
      throw new IllegalArgumentException(
        "The matrix is not properly formatted.");
    }
    
    Matrix std = null;
    if (dim == Dimension.ALL)
    {
      double variance = 0.0D;
      std = new ArrayMatrix(1, 1);
      int validSize = 0;
      for (int i = 0; i < m.rows(); i++) {
        for (int j = 0; j < m.columns(); j++)
        {
          if (m.get(i, j) != errorCode)
          {

            validSize++;
            variance += Math.pow(m.get(i, j) - average.get(0, 0), 2.0D);
          }
        }
      }
      variance /= validSize;
      std.set(0, 0, Math.sqrt(variance));
    } else if (dim == Dimension.ROW)
    {
      std = new ArrayMatrix(m.rows(), 1);
      for (int i = 0; i < m.rows(); i++) {
        double variance = 0.0D;
        int validSize = 0;
        for (int j = 0; j < m.columns(); j++)
        {
          if (m.get(i, j) != errorCode)
          {

            validSize++;
            variance += Math.pow(m.get(i, j) - average.get(i, 0), 2.0D);
          }
        }
        variance /= validSize;
        std.set(i, 0, Math.sqrt(variance));
      }
    } else if (dim == Dimension.COLUMN)
    {
      std = new ArrayMatrix(1, m.columns());
      Matrix validSize = new ArrayMatrix(1, m.columns());
      for (int i = 0; i < m.rows(); i++) {
        for (int j = 0; j < m.columns(); j++)
        {
          if (m.get(i, j) != errorCode)
          {

            double variance = std.get(0, j);
            variance += Math.pow(m.get(i, j) - average.get(0, j), 2.0D);
            std.set(0, j, variance);
            validSize.set(0, j, validSize.get(0, j) + 1.0D);
          }
        }
      }
      for (int i = 0; i < m.columns(); i++) {
        double variance = std.get(0, i);
        variance /= validSize.get(0, i);
        std.set(0, i, Math.sqrt(variance));
      }
    }
    return std;
  }
  









  public static Matrix average(Matrix m, Dimension dim, int errorCode)
  {
    Matrix averageMatrix = null;
    
    if (dim == Dimension.ALL)
    {
      int validSize = 0;
      double average = 0.0D;
      for (int i = 0; i < m.rows(); i++) {
        for (int j = 0; j < m.columns(); j++)
        {
          if (m.get(i, j) != errorCode)
          {

            validSize++;
            average += m.get(i, j);
          }
        }
      }
      averageMatrix = new ArrayMatrix(1, 1);
      average /= validSize;
      averageMatrix.set(1, 1, average);
    } else if (dim == Dimension.ROW)
    {
      averageMatrix = new ArrayMatrix(m.rows(), 1);
      for (int i = 0; i < m.rows(); i++) {
        double average = 0.0D;
        int validSize = 0;
        for (int j = 0; j < m.columns(); j++)
        {
          if (m.get(i, j) != errorCode)
          {
            validSize++;
            average += m.get(i, j);
          }
        }
        average /= validSize;
        averageMatrix.set(i, 0, average);
      }
    } else if (dim == Dimension.COLUMN)
    {
      averageMatrix = new ArrayMatrix(1, m.columns());
      Matrix validSize = new ArrayMatrix(1, m.columns());
      for (int i = 0; i < m.rows(); i++) {
        for (int j = 0; j < m.columns(); j++)
        {
          if (m.get(i, j) != errorCode)
          {
            validSize.set(0, j, validSize.get(0, j) + 1.0D);
            double newValue = m.get(i, j) + averageMatrix.get(0, j);
            averageMatrix.set(0, j, newValue);
          }
        }
      }
      for (int i = 0; i < m.columns(); i++) {
        double average = averageMatrix.get(0, i);
        average /= validSize.get(0, i);
        averageMatrix.set(0, i, average);
      }
    }
    return averageMatrix;
  }
}
