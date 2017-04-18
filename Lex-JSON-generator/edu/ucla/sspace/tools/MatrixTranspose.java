package edu.ucla.sspace.tools;

import edu.ucla.sspace.matrix.MatrixIO.Format;
import java.io.File;

public class MatrixTranspose
{
  public MatrixTranspose() {}
  
  public static void main(String[] args) throws Exception
  {
    edu.ucla.sspace.matrix.Matrix m = edu.ucla.sspace.matrix.MatrixIO.readMatrix(new File(args[0]), MatrixIO.Format.DENSE_TEXT);
    m = edu.ucla.sspace.matrix.Matrices.transpose(m);
    File out = new File(args[1]);
    edu.ucla.sspace.matrix.MatrixIO.writeMatrix(m, out, MatrixIO.Format.DENSE_TEXT);
  }
}
