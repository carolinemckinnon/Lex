package edu.ucla.sspace.matrix;

import edu.ucla.sspace.util.IntegerMap;
import edu.ucla.sspace.vector.SparseDoubleVector;
import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;










































public class TransformStatistics
{
  public TransformStatistics() {}
  
  public static MatrixStatistics extractStatistics(Matrix matrix)
  {
    return extractStatistics(matrix, false, false);
  }
  

















  public static MatrixStatistics extractStatistics(Matrix matrix, boolean countRowOccurrances, boolean countColumnOccurrances)
  {
    double[] rowSums = new double[matrix.rows()];
    double[] columnSums = new double[matrix.columns()];
    double matrixSum = 0.0D;
    
    if ((matrix instanceof SparseMatrix))
    {

      SparseMatrix smatrix = (SparseMatrix)matrix;
      

      for (int row = 0; row < matrix.rows(); row++) {
        SparseDoubleVector rowVec = smatrix.getRowVector(row);
        int[] nonZeros = rowVec.getNonZeroIndices();
        for (int index : nonZeros) {
          double value = rowVec.get(index);
          rowSums[row] += (countRowOccurrances ? 1.0D : value);
          columnSums[index] += (countColumnOccurrances ? 1.0D : value);
          matrixSum += value;
        }
      }
    }
    else
    {
      for (int row = 0; row < matrix.rows(); row++) {
        for (int col = 0; col < matrix.columns(); col++) {
          double value = matrix.get(row, col);
          rowSums[row] += (countRowOccurrances ? 1.0D : value);
          columnSums[col] += (countColumnOccurrances ? 1.0D : value);
          matrixSum += value;
        }
      }
    }
    return new MatrixStatistics(rowSums, columnSums, matrixSum);
  }
  








  public static MatrixStatistics extractStatistics(File inputMatrixFile, MatrixIO.Format format)
  {
    return extractStatistics(inputMatrixFile, format, false, false);
  }
  



















  public static MatrixStatistics extractStatistics(File inputMatrixFile, MatrixIO.Format format, boolean countRowOccurrances, boolean countColumnOccurrances)
  {
    int numColumns = 0;
    int numRows = 0;
    double matrixSum = 0.0D;
    Map<Integer, Double> rowCountMap = new IntegerMap();
    Map<Integer, Double> colCountMap = new IntegerMap();
    

    try
    {
      iter = MatrixIO.getMatrixFileIterator(inputMatrixFile, format);
    } catch (IOException ioe) { Iterator<MatrixEntry> iter;
      throw new IOError(ioe);
    }
    Iterator<MatrixEntry> iter;
    while (iter.hasNext()) {
      MatrixEntry entry = (MatrixEntry)iter.next();
      

      if (entry.column() >= numColumns)
        numColumns = entry.column() + 1;
      if (entry.row() >= numRows) {
        numRows = entry.row() + 1;
      }
      
      if (entry.value() != 0.0D)
      {


        Double occurance = (Double)rowCountMap.get(Integer.valueOf(entry.row()));
        double rowDelta = countRowOccurrances ? 1.0D : entry.value();
        rowCountMap.put(Integer.valueOf(entry.row()), 
        
          Double.valueOf(occurance == null ? rowDelta : occurance.doubleValue() + rowDelta));
        

        occurance = (Double)colCountMap.get(Integer.valueOf(entry.column()));
        double columnDelta = countColumnOccurrances ? 1.0D : entry.value();
        colCountMap.put(Integer.valueOf(entry.column()), 
        
          Double.valueOf(occurance == null ? columnDelta : occurance.doubleValue() + columnDelta));
        
        matrixSum += entry.value();
      }
    }
    
    double[] rowSums = extractValues(rowCountMap, numRows);
    double[] columnSums = extractValues(colCountMap, numColumns);
    return new MatrixStatistics(rowSums, columnSums, matrixSum);
  }
  







  private static <T extends Number> double[] extractValues(Map<Integer, T> map, int size)
  {
    double[] values = new double[size];
    for (Map.Entry<Integer, T> entry : map.entrySet()) {
      if (((Integer)entry.getKey()).intValue() > values.length) {
        throw new IllegalArgumentException(
          "Array size is too small for values in the given map");
      }
      values[((Integer)entry.getKey()).intValue()] = ((Number)entry.getValue()).doubleValue();
    }
    return values;
  }
  


  public static class MatrixStatistics
  {
    public double[] rowSums;
    

    public double[] columnSums;
    

    public double matrixSum;
    

    public MatrixStatistics(double[] rowSums, double[] columnSums, double matrixSum)
    {
      this.rowSums = rowSums;
      this.columnSums = columnSums;
      this.matrixSum = matrixSum;
    }
  }
}
