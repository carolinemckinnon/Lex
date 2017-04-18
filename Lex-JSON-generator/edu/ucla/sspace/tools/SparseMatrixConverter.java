package edu.ucla.sspace.tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;















public class SparseMatrixConverter
{
  public SparseMatrixConverter() {}
  
  public static void main(String[] args)
  {
    try
    {
      BufferedReader br = new BufferedReader(new FileReader(args[0]));
      
      Map<Integer, Integer> colToNonZero = 
        new HashMap();
      

      int rows = 0;int cols = 0;int nonZero = 0;
      for (String line = null; (line = br.readLine()) != null;) {
        String[] rowColVal = line.split("\\s+");
        int row = Integer.parseInt(rowColVal[0]);
        int col = Integer.parseInt(rowColVal[1]);
        if (row > rows)
          rows = row;
        if (col > cols)
          cols = col;
        nonZero++;
        Integer colCount = (Integer)colToNonZero.get(Integer.valueOf(col));
        colToNonZero.put(Integer.valueOf(col), Integer.valueOf(colCount == null ? 1 : colCount.intValue() + 1));
      }
      br.close();
      


      rows--;
      cols--;
      
      br = new BufferedReader(new FileReader(args[0]));
      


      System.out.println(rows + "\t" + cols + "\t" + nonZero);
      int lastCol = 0;
      for (String line = null; (line = br.readLine()) != null;) {
        String[] rowColVal = line.split("\\s+");
        int col = Integer.parseInt(rowColVal[1]);
        if (col != lastCol)
        {

          for (int i = lastCol + 1; i < col; i++) {
            System.out.println(0);
          }
          
          int colCount = ((Integer)colToNonZero.get(Integer.valueOf(col))).intValue();
          lastCol = col;
          System.out.println(colCount);
        }
        System.out.println(rowColVal[0] + "\t" + rowColVal[2]);
      }
      br.close();
      System.out.flush();
    }
    catch (Throwable t) {
      t.printStackTrace();
    }
  }
}
