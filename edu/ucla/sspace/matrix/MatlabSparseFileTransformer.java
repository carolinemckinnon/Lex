package edu.ucla.sspace.matrix;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOError;
import java.io.IOException;
import java.io.PrintWriter;
































public class MatlabSparseFileTransformer
  implements FileTransformer
{
  public MatlabSparseFileTransformer() {}
  
  public File transform(File inputFile, File outputFile, GlobalTransform transform)
  {
    try
    {
      BufferedReader br = new BufferedReader(new FileReader(inputFile));
      PrintWriter writer = new PrintWriter(new BufferedWriter(
        new FileWriter(outputFile)));
      
      for (String line = null; (line = br.readLine()) != null;) {
        String[] rowColEntry = line.split("\\s+");
        int row = Integer.parseInt(rowColEntry[0]);
        int col = Integer.parseInt(rowColEntry[1]);
        double value = Double.parseDouble(rowColEntry[2]);
        writer.printf("%d %d %f\n", new Object[] { Integer.valueOf(row), Integer.valueOf(col), 
          Double.valueOf(transform.transform(row - 1, col - 1, value)) });
      }
      writer.close();
      
      return outputFile;
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
}
