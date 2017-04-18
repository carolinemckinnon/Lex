package edu.ucla.sspace.tools;

import edu.ucla.sspace.common.ArgOptions;
import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.matrix.MatrixIO;
import edu.ucla.sspace.matrix.MatrixIO.Format;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;


























public class MatrixConverter
{
  public MatrixConverter() {}
  
  public static void main(String[] args)
    throws IOException
  {
    ArgOptions options = new ArgOptions();
    options.addOption('i', "inputFormat", 
      "the matrix format of the input matrix", 
      true, "STRING", "Required");
    options.addOption('o', "ouputFormat", 
      "the matrix format of the output matrix", 
      true, "STRING", "Required");
    options.parseOptions(args);
    
    if ((options.numPositionalArgs() != 2) || 
      (!options.hasOption('i')) || (!options.hasOption('o'))) {
      System.out.println(
        "usage: java MatrixConverter [options] <int.mat> <out.mat>\n" + 
        options.prettyPrint());
      System.exit(1);
    }
    
    File inMatFile = new File(options.getPositionalArg(0));
    File outMatFile = new File(options.getPositionalArg(1));
    MatrixIO.Format inMatFormat = MatrixIO.Format.valueOf(
      options.getStringOption('i').toUpperCase());
    MatrixIO.Format outMatFormat = MatrixIO.Format.valueOf(
      options.getStringOption('o').toUpperCase());
    Matrix matrix = MatrixIO.readMatrix(inMatFile, inMatFormat);
    MatrixIO.writeMatrix(matrix, outMatFile, outMatFormat);
  }
}
