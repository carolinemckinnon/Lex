package edu.ucla.sspace.matrix;

import edu.ucla.sspace.similarity.SimilarityFunction;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.SparseDoubleVector;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;














public class MinSimilarityAffinityMatrixCreator
  implements AffinityMatrixCreator
{
  private static final Logger LOG = Logger.getLogger(MinSimilarityAffinityMatrixCreator.class.getName());
  private SimilarityFunction edgeSim;
  private SimilarityFunction kernelSim;
  private double edgeSimThreshold;
  
  public MinSimilarityAffinityMatrixCreator() {}
  
  public void setParams(double... params)
  {
    edgeSimThreshold = params[0];
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
      File affMatrixFile = File.createTempFile("affinty-matrix", ".dat");
      PrintWriter affMatrixWriter = new PrintWriter(affMatrixFile);
      
      int rows = input.rows();
      






      for (int i = 0; i < rows; i++) {
        LOG.fine("computing affinity for row " + i);
        DoubleVector row1 = input.getRowVector(i);
        for (int j = i + 1; j < rows; j++) {
          DoubleVector row2 = input.getRowVector(j);
          
          double dataSimilarity = edgeSim.sim(row1, row2);
          


          if (dataSimilarity > edgeSimThreshold) {
            double edgeWeight = kernelSim.sim(row1, row2);
            affMatrixWriter.printf("%d %d %f\n", new Object[] { Integer.valueOf(i + 1), Integer.valueOf(j + 1), Double.valueOf(edgeWeight) });
            



            edgeWeight = kernelSim.isSymmetric() ? 
              edgeWeight : 
              kernelSim.sim(row2, row1);
            affMatrixWriter.printf("%d %d %f\n", new Object[] { Integer.valueOf(j + 1), Integer.valueOf(i + 1), Double.valueOf(edgeWeight) });
          }
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
      









      File converted = 
        MatrixIO.convertFormat(matrixFile, format, 
        MatrixIO.Format.SVDLIBC_SPARSE_BINARY, 
        !useColumns);
      LOG.fine("Calculating the affinity matrix");
      

      DataInputStream dis = new DataInputStream(
        new BufferedInputStream(new FileInputStream(converted)));
      


      int cols = dis.readInt();
      int rows = dis.readInt();
      dis.close();
      



      File affMatrixFile = File.createTempFile("affinity-matrix", ".dat");
      PrintWriter affMatrixWriter = new PrintWriter(affMatrixFile);
      



      SparseDoubleVector curRow = null;
      SparseDoubleVector nextRow = null;
      
      SvdlibcSparseBinaryFileRowIterator matrixIter = 
        new SvdlibcSparseBinaryFileRowIterator(converted);
      
      for (int row = 0; row < rows; row++) {
        LOG.fine("computing affinity for row " + row);
        


        for (int other = 0; other < rows; other++)
        {

          if ((row == 0) && (curRow == null)) {
            curRow = matrixIter.next();
          }
          else
          {
            SparseDoubleVector otherRow = matrixIter.next();
            



            if (other >= row)
            {



              if (other == row + 1) {
                nextRow = otherRow;
              }
              




              double dataSimilarity = edgeSim.sim(curRow, otherRow);
              
              if (dataSimilarity > edgeSimThreshold) {
                double edgeWeight = kernelSim.sim(curRow, otherRow);
                affMatrixWriter.printf("%d %d %f\n", new Object[] {
                  Integer.valueOf(row + 1), Integer.valueOf(other + 1), Double.valueOf(edgeWeight) });
                



                edgeWeight = kernelSim.isSymmetric() ? 
                  edgeWeight : 
                  kernelSim.sim(otherRow, curRow);
                
                affMatrixWriter.printf("%d %d %f\n", new Object[] {
                  Integer.valueOf(other + 1), Integer.valueOf(row + 1), Double.valueOf(edgeWeight) });
              }
            } } }
        curRow = nextRow;
        matrixIter.reset();
      }
      

      affMatrixWriter.close();
      return new MatrixFile(affMatrixFile, MatrixIO.Format.MATLAB_SPARSE);
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
}
