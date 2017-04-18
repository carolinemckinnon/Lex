package edu.ucla.sspace.matrix;

import edu.ucla.sspace.similarity.SimilarityFunction;
import edu.ucla.sspace.util.BoundedSortedMultiMap;
import edu.ucla.sspace.util.Duple;
import edu.ucla.sspace.util.MultiMap;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.SparseDoubleVector;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;













public class NearestNeighborAffinityMatrixCreator
  implements AffinityMatrixCreator
{
  private static final Logger LOG = Logger.getLogger(MinSimilarityAffinityMatrixCreator.class.getName());
  private SimilarityFunction edgeSim;
  private SimilarityFunction kernelSim;
  private int numNearestNeighbors;
  
  public NearestNeighborAffinityMatrixCreator() {}
  
  public void setParams(double... params)
  {
    numNearestNeighbors = ((int)params[0]);
  }
  



  public void setFunctions(SimilarityFunction edgeSim, SimilarityFunction kernelSim)
  {
    this.edgeSim = edgeSim;
    this.kernelSim = kernelSim;
  }
  

  public MatrixFile calculate(Matrix input)
  {
    try
    {
      File affMatrixFile = 
        File.createTempFile("affinty-matrix", ".dat");
      affMatrixFile.deleteOnExit();
      PrintWriter affMatrixWriter = new PrintWriter(affMatrixFile);
      
      int rows = input.rows();
      
      LOG.fine("Calculating the affinity matrix");
      RowComparator rc = new RowComparator();
      for (int i = 0; i < rows; i++) {
        LOG.fine("computing affinity for row " + i);
        MultiMap<Double, Integer> neighborMap = rc.getMostSimilar(
          input, i, numNearestNeighbors, edgeSim);
        
        DoubleVector row = input.getRowVector(i);
        for (Iterator localIterator = neighborMap.values().iterator(); localIterator.hasNext();) { int n = ((Integer)localIterator.next()).intValue();
          double edgeWeight = kernelSim.sim(
            row, input.getRowVector(n));
          affMatrixWriter.printf("%d %d %f\n", new Object[] { Integer.valueOf(i + 1), Integer.valueOf(n + 1), Double.valueOf(edgeWeight) });
        }
      }
      

      affMatrixWriter.close();
      return new MatrixFile(affMatrixFile, MatrixIO.Format.MATLAB_SPARSE);
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
  


  public MatrixFile calculate(MatrixFile input)
  {
    return calculate(input, false);
  }
  


  public MatrixFile calculate(MatrixFile input, boolean useColumns)
  {
    File matrixFile = input.getFile();
    MatrixIO.Format format = input.getFormat();
    




    try
    {
      LOG.fine("Converting input matrix to new format for faster calculation of the affinity matrix");
      










      File converted = MatrixIO.convertFormat(
        matrixFile, format, 
        MatrixIO.Format.SVDLIBC_SPARSE_BINARY, !useColumns);
      
      LOG.fine("Calculating the affinity matrix");
      
      DataInputStream dis = new DataInputStream(
        new BufferedInputStream(new FileInputStream(converted)));
      


      int cols = dis.readInt();
      int rows = dis.readInt();
      dis.close();
      



      File affMatrixFile = File.createTempFile("affinity-matrix", ".dat");
      affMatrixFile.deleteOnExit();
      PrintWriter affMatrixWriter = new PrintWriter(affMatrixFile);
      



      SparseDoubleVector curRow = null;
      SparseDoubleVector nextRow = null;
      
      SvdlibcSparseBinaryFileRowIterator matrixIter = 
        new SvdlibcSparseBinaryFileRowIterator(converted);
      
      for (int row = 0; row < rows; row++) {
        LOG.fine("computing affinity for row " + row);
        




        MultiMap<Double, Duple<Integer, Double>> neighbors = 
          new BoundedSortedMultiMap(
          numNearestNeighbors, false);
        
        SparseDoubleVector otherRow;
        
        for (int other = 0; other < rows; other++)
        {

          if ((row == 0) && (curRow == null)) {
            curRow = matrixIter.next();
          }
          else
          {
            otherRow = matrixIter.next();
            


            if (other == row + 1) {
              nextRow = otherRow;
            }
            




            double dataSimilarity = edgeSim.sim(curRow, otherRow);
            double edgeWeight = kernelSim.sim(curRow, otherRow);
            
            neighbors.put(Double.valueOf(dataSimilarity), 
              new Duple(Integer.valueOf(other), Double.valueOf(edgeWeight)));
          } }
        curRow = nextRow;
        matrixIter.reset();
        




        for (Duple<Integer, Double> t : neighbors.values()) {
          affMatrixWriter.printf("%d %d %f\n", new Object[] { Integer.valueOf(row + 1), Integer.valueOf(((Integer)x).intValue() + 1), y });
        }
      }
      
      affMatrixWriter.close();
      return new MatrixFile(affMatrixFile, MatrixIO.Format.MATLAB_SPARSE);
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
}
