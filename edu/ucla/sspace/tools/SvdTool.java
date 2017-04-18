package edu.ucla.sspace.tools;

import edu.ucla.sspace.common.ArgOptions;
import edu.ucla.sspace.matrix.MatrixFactorization;
import edu.ucla.sspace.matrix.MatrixFile;
import edu.ucla.sspace.matrix.MatrixIO;
import edu.ucla.sspace.matrix.MatrixIO.Format;
import edu.ucla.sspace.matrix.SVD;
import java.io.File;
import java.io.PrintStream;





















public class SvdTool
{
  public SvdTool() {}
  
  public static void main(String[] args)
    throws Exception
  {
    ArgOptions options = new ArgOptions();
    
    options.addOption('h', "help", "Generates a help message and exits", 
      false, null, "Program Options");
    
    options.addOption('d', "dimensions", "Desired SVD Triples", 
      true, "INT", "Program Options");
    
    options.addOption('o', "fileRoot", "Root of files in which to store resulting U,S,V", 
      true, "DIR", "Program Options");
    options.addOption('r', "inputFormat", "Input matrix file format", 
      true, "STRING", "Program Options");
    options.addOption('w', "outputFormat", "Output matrix file format", 
      true, "STRING", "Program Options");
    
    options.parseOptions(args);
    
    if ((options.numPositionalArgs() == 0) || (options.hasOption("help"))) {
      usage(options);
      return;
    }
    

    int dimensions = options.getIntOption('d');
    String matrixFileName = options.getPositionalArg(0);
    File matrixFile = new File(matrixFileName);
    if (!matrixFile.exists()) {
      throw new IllegalArgumentException(
        "non-existent input matrix file: " + matrixFileName);
    }
    String outputDirName = options.getStringOption('o');
    File outputDir = new File(outputDirName);
    if ((!outputDir.exists()) || (!outputDir.isDirectory())) {
      throw new IllegalArgumentException(
        "invalid output directory: " + outputDirName);
    }
    MatrixIO.Format inputFormat = options.hasOption('r') ? 
      getFormat(options.getStringOption('r')) : 
      MatrixIO.Format.SVDLIBC_SPARSE_TEXT;
    MatrixIO.Format outputFormat = options.hasOption('w') ? 
      getFormat(options.getStringOption('w')) : 
      MatrixIO.Format.SVDLIBC_DENSE_TEXT;
    
    MatrixFactorization factorizer = SVD.getFastestAvailableFactorization();
    factorizer.factorize(new MatrixFile(matrixFile, inputFormat), dimensions);
    File uFile = new File(outputDir, "U.mat");
    MatrixIO.writeMatrix(factorizer.dataClasses(), uFile, outputFormat);
    File vFile = new File(outputDir, "V.mat");
    MatrixIO.writeMatrix(factorizer.classFeatures(), vFile, outputFormat);
  }
  
  private static MatrixIO.Format getFormat(String code) {
    if (code.equals("cd"))
      return MatrixIO.Format.CLUTO_DENSE;
    if (code.equals("cs"))
      return MatrixIO.Format.CLUTO_SPARSE;
    if (code.equals("ms"))
      return MatrixIO.Format.MATLAB_SPARSE;
    if (code.equals("db"))
      return MatrixIO.Format.SVDLIBC_DENSE_BINARY;
    if (code.equals("dt"))
      return MatrixIO.Format.SVDLIBC_DENSE_TEXT;
    if (code.equals("sb"))
      return MatrixIO.Format.SVDLIBC_SPARSE_BINARY;
    if (code.equals("st")) {
      return MatrixIO.Format.SVDLIBC_SPARSE_TEXT;
    }
    throw new IllegalArgumentException("unrecognized format: " + code);
  }
  




  private static void usage(ArgOptions options)
  {
    System.out.println(
      "SVD version 1.0\n  Based on SVDLIBJ, written by Adrian Kuhn and David Erni,\n  which was adapted from SVDLIBC, written by Doug Rohde,\n  which was based on code adapted from SVDPACKC\n\nusage: java -jar svd.jar [options] matrix_file\n\n" + 
      



      options.prettyPrint() + 
      "\nValid matrix formats are:\n" + 
      "  cd        The sparse text format supported by CLUTO.\n" + 
      "  cs        The sparse text format supported by CLUTO.\n" + 
      "  ms        The sparse format supported by Matlab.\n" + 
      "  db        The dense binary format supported by SVDLIBC.\n" + 
      "  dt        The dense text format supported by SVDLIBC. (default output)\n" + 
      "  sb        The sparse binary format supported by SVDLIBC.\n" + 
      "  st        The sparse text format supported by SVDLIBC. (default input)\n" + 
      "\nSee http://tedlab.mit.edu/~dr/SVDLIBC/ for more format details");
  }
}
