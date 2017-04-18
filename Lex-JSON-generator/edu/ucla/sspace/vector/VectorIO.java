package edu.ucla.sspace.vector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;






































public class VectorIO
{
  private VectorIO() {}
  
  public static double[] readDoubleArray(File f)
    throws IOException
  {
    BufferedReader br = new BufferedReader(new FileReader(f));
    String[] valueStrs = br.readLine().trim().split("\\s+");
    
    double[] values = new double[valueStrs.length];
    for (int i = 0; i < valueStrs.length; i++) {
      values[i] = Double.parseDouble(valueStrs[i]);
    }
    
    br.close();
    return values;
  }
  







  public static String toString(Vector vector)
  {
    StringBuilder sb = new StringBuilder(vector.length() * 5);
    if ((vector instanceof SparseDoubleVector)) {
      SparseDoubleVector sdv = (SparseDoubleVector)vector;
      int[] nz = sdv.getNonZeroIndices();
      sb.append(nz[0]).append(",").append(sdv.get(nz[0]));
      for (int i = 1; i < nz.length; i++)
        sb.append(";").append(nz[i]).append(",").append(sdv.get(nz[i]));
    } else {
      for (int i = 0; i < vector.length() - 1; i++)
        sb.append(vector.getValue(i).doubleValue()).append(" ");
      sb.append(vector.getValue(vector.length() - 1).doubleValue());
    }
    
    return sb.toString();
  }
  







  public static String toString(double[] vector)
  {
    StringBuilder sb = new StringBuilder(vector.length * 5);
    
    for (int i = 0; i < vector.length - 1; i++)
      sb.append(vector[i]).append(" ");
    sb.append(vector[(vector.length - 1)]);
    
    return sb.toString();
  }
  







  public static String toString(int[] vector)
  {
    StringBuilder sb = new StringBuilder(vector.length * 3);
    
    for (int i = 0; i < vector.length - 1; i++)
      sb.append(vector[i]).append(" ");
    sb.append(vector[(vector.length - 1)]);
    
    return sb.toString();
  }
  






  public static void writeVector(int[] vector, PrintWriter pw)
    throws IOException
  {
    pw.println(toString(vector));
    pw.close();
  }
  




  public static void writeVector(int[] vector, File f)
    throws IOException
  {
    writeVector(vector, new PrintWriter(f));
  }
  








  public static void writeVector(int[] vector, String word, File outputDir)
    throws IOException
  {
    if (!outputDir.isDirectory()) {
      throw new IllegalArgumentException("provided output directory file is not a directory: " + 
      
        outputDir);
    }
    
    word = word.replaceAll("/", "-SLASH-");
    File output = new File(outputDir, word + ".vector");
    writeVector(vector, output);
  }
}
