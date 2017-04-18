package edu.ucla.sspace.matrix;

import comp6803.plainly.CorpusCreate;
import edu.ucla.sspace.common.Statistics;
import edu.ucla.sspace.util.IntegerMap;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.SparseDoubleVector;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

























































public class LogEntropyTransform
  extends BaseTransform
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private static final Logger LOGGER = Logger.getLogger(LogEntropyTransform.class.getName());
  

  public LogEntropyTransform() {}
  
  protected GlobalTransform getTransform(File inputMatrixFile, MatrixIO.Format format)
  {
    return new LogEntropyGlobalTransform(inputMatrixFile, format);
  }
  


  protected GlobalTransform getTransform(Matrix matrix)
  {
    return new LogEntropyGlobalTransform(matrix);
  }
  


  public String toString()
  {
    return "log-entropy";
  }
  




  public class LogEntropyGlobalTransform
    implements GlobalTransform, Serializable
  {
    private static final long serialVersionUID = 1L;
    



    private double[] rowEntropy;
    



    public LogEntropyGlobalTransform(Matrix matrix)
    {
      rowEntropy = new double[matrix.rows()];
      
      int numColumns = matrix.columns();
      if ((matrix instanceof SparseMatrix))
      {
        SparseMatrix smatrix = (SparseMatrix)matrix;
        

        for (int row = 0; row < matrix.rows(); row++)
        {
          double rowCount = 0.0D;
          SparseDoubleVector rowVec = smatrix.getRowVector(row);
          int[] nonZeros = rowVec.getNonZeroIndices();
          for (int index : nonZeros) {
            double value = rowVec.get(index);
            rowCount += value;
          }
          


          for (int index : nonZeros) {
            double value = rowVec.get(index);
            double rowProbabilityForFeature = value / rowCount;
            rowEntropy[row] += rowProbabilityForFeature * 
              Statistics.log2(rowProbabilityForFeature);
          }
          

          rowEntropy[row] = (1.0D + rowEntropy[row] / Statistics.log2(numColumns));
        }
        
      }
      else
      {
        for (int row = 0; row < matrix.rows(); row++)
        {
          double rowCount = 0.0D;
          for (int column = 0; column < matrix.columns(); column++) {
            rowCount += matrix.get(row, column);
          }
          

          for (int column = 0; column < matrix.columns(); column++) {
            double value = matrix.get(row, column);
            double rowProbabilityForFeature = value / rowCount;
            rowEntropy[row] += rowProbabilityForFeature * 
              Statistics.log2(rowProbabilityForFeature);
          }
          

          rowEntropy[row] = (1.0D + rowEntropy[row] / Statistics.log2(numColumns));
        }
      }
    }
    





    public LogEntropyGlobalTransform(File inputMatrixFile, MatrixIO.Format format)
    {
      Map<Integer, Double> rowSums = new IntegerMap();
      try
      {
        iter = MatrixIO.getMatrixFileIterator(inputMatrixFile, format);
      } catch (IOException ioe) { Iterator<MatrixEntry> iter;
        throw new IOError(ioe); }
      Iterator<MatrixEntry> iter;
      int numColumns = 0;
      int numRows = 0;
      
      LogEntropyTransform.LOGGER.info("Computing the total row counts");
      
      while (iter.hasNext()) {
        MatrixEntry entry = (MatrixEntry)iter.next();
        Double rowSum = (Double)rowSums.get(Integer.valueOf(entry.row()));
        rowSums.put(Integer.valueOf(entry.row()), 
        
          Double.valueOf(rowSum == null ? entry.value() : rowSum.doubleValue() + entry.value()));
        

        if (entry.row() >= numRows)
          numRows = entry.row() + 1;
        if (entry.column() >= numColumns) {
          numColumns = entry.column() + 1;
        }
      }
      LogEntropyTransform.LOGGER.info("Computing the entropy of each row");
      

      rowEntropy = new double[numRows];
      try {
        iter = MatrixIO.getMatrixFileIterator(inputMatrixFile, format);
      } catch (IOException ioe) {
        throw new IOError(ioe);
      }
      while (iter.hasNext()) {
        MatrixEntry entry = (MatrixEntry)iter.next();
        Double rowSumDouble = (Double)rowSums.get(Integer.valueOf(entry.row()));
        double rowSum = rowSumDouble == null ? 0.0D : rowSumDouble.doubleValue();
        double probability = entry.value() / rowSum;
        rowEntropy[entry.row()] += probability * Statistics.log2(probability);
      }
      
      LogEntropyTransform.LOGGER.info("Scaling the entropy of the rows");
      
      for (int row = 0; row < numRows; row++)
        rowEntropy[row] = (1.0D + rowEntropy[row] / Statistics.log2(numColumns));
      writeEntropyToFile();
    }
    















    public double transform(int row, int column, double value)
    {
      return Statistics.log2_1p(value) * rowEntropy[row];
    }
    















    public double transform(int row, DoubleVector column)
    {
      double value = column.get(row);
      return Statistics.log2_1p(value) * rowEntropy[row];
    }
    
    public double[] getRowEntropy() {
      return rowEntropy;
    }
    
    public void writeEntropyToFile()
    {
      try
      {
        new ObjectOutputStream(new FileOutputStream("corpus/" + CorpusCreate.getInstance().getName().replaceAll(" ", "-").toLowerCase() + "-entropy.dat")).writeObject(rowEntropy);
      }
      catch (FileNotFoundException e) {
        e.printStackTrace();
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
