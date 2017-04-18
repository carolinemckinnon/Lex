package edu.ucla.sspace.matrix;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOError;
import java.io.IOException;
import java.io.PrintWriter;





























class SvdlibcDenseTextFileTransformer
  implements FileTransformer
{
  SvdlibcDenseTextFileTransformer() {}
  
  public File transform(File inputFile, File outFile, GlobalTransform transform)
  {
    try
    {
      BufferedReader br = new BufferedReader(new FileReader(inputFile));
      PrintWriter writer = new PrintWriter(new BufferedWriter(
        new FileWriter(outFile)));
      

      String line = br.readLine();
      String[] rowCol = line.split("\\s+");
      int numRows = Integer.parseInt(rowCol[0]);
      int numCols = Integer.parseInt(rowCol[1]);
      

      writer.printf("%d %d\n", new Object[] { Integer.valueOf(numRows), Integer.valueOf(numCols) });
      

      for (int row = 0; ((line = br.readLine()) != null) && (
            row < numRows); row++)
      {

        String[] values = line.split("\\s+");
        StringBuilder sb = new StringBuilder(values.length * 4);
        for (int col = 0; col < numCols; col++) {
          double value = Double.parseDouble(values[col]);
          sb.append(transform.transform(row, col, value)).append(" ");
        }
        writer.println(sb.toString());
      }
      
      writer.close();
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
    
    return outFile;
  }
}
