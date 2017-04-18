package edu.ucla.sspace.matrix;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOError;
import java.io.IOException;
import java.io.PrintWriter;





























class SvdlibcSparseTextFileTransformer
  implements FileTransformer
{
  SvdlibcSparseTextFileTransformer() {}
  
  public File transform(File inputFile, File outFile, GlobalTransform transform)
  {
    try
    {
      BufferedReader br = new BufferedReader(new FileReader(inputFile));
      PrintWriter writer = new PrintWriter(new BufferedWriter(
        new FileWriter(outFile)));
      

      String line = br.readLine();
      String[] rowColNonZeroCount = line.split("\\s+");
      int numRows = Integer.parseInt(rowColNonZeroCount[0]);
      int numCols = Integer.parseInt(rowColNonZeroCount[1]);
      int numTotalNonZeros = Integer.parseInt(rowColNonZeroCount[2]);
      

      writer.printf("%d %d %d\n", new Object[] { Integer.valueOf(numRows), Integer.valueOf(numCols), Integer.valueOf(numTotalNonZeros) });
      
      line = br.readLine();
      
      for (int col = 0; (line != null) && (col < numCols); col++) {
        int numNonZeros = Integer.parseInt(line);
        writer.printf("%d\n", new Object[] { Integer.valueOf(numNonZeros) });
        


        for (int index = 0; ((line = br.readLine()) != null) && (
              index < numNonZeros); index++) {
          String[] rowValue = line.split("\\s+");
          int row = Integer.parseInt(rowValue[0]);
          double value = Double.parseDouble(rowValue[1]);
          writer.printf("%d %f\n", new Object[] { Integer.valueOf(row), 
            Double.valueOf(transform.transform(row, col, value)) });
        }
      }
      
      writer.close();
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
    
    return outFile;
  }
}
