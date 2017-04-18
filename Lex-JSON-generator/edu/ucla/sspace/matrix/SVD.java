package edu.ucla.sspace.matrix;

import edu.ucla.sspace.matrix.factorization.SingularValueDecomposition;
import edu.ucla.sspace.matrix.factorization.SingularValueDecompositionLibC;
import edu.ucla.sspace.matrix.factorization.SingularValueDecompositionMatlab;
import edu.ucla.sspace.matrix.factorization.SingularValueDecompositionOctave;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Level;
import java.util.logging.Logger;




































































































public class SVD
{
  private static final Logger SVD_LOGGER = Logger.getLogger(SVD.class.getName());
  
  private SVD() {}
  

  public static enum Algorithm
  {
    SVDLIBC, 
    MATLAB, 
    OCTAVE, 
    JAMA, 
    COLT, 
    ANY;
  }
  










  public static SingularValueDecomposition getFastestAvailableFactorization()
  {
    if (isSVDLIBCavailable())
      return new SingularValueDecompositionLibC();
    if (isMatlabAvailable())
      return new SingularValueDecompositionMatlab();
    if (isOctaveAvailable())
      return new SingularValueDecompositionOctave();
    throw new UnsupportedOperationException(
      "Cannot find a valid SVD implementation");
  }
  



  public static SingularValueDecomposition getFactorization(Algorithm alg)
  {
    switch (alg) {
    case COLT:  return new SingularValueDecompositionMatlab();
    case JAMA:  return new SingularValueDecompositionOctave();
    case ANY:  return new SingularValueDecompositionLibC();
    case SVDLIBC:  return getFastestAvailableFactorization(); }
    throw new UnsupportedOperationException(
      "Cannot find a valid SVD implementation");
  }
  











  @Deprecated
  static Algorithm getFastestAvailableAlgorithm()
  {
    if (isSVDLIBCavailable())
      return Algorithm.SVDLIBC;
    if (isMatlabAvailable())
      return Algorithm.MATLAB;
    if (isOctaveAvailable())
      return Algorithm.OCTAVE;
    if (isJAMAavailable())
      return Algorithm.JAMA;
    if (isColtAvailable()) {
      return Algorithm.COLT;
    }
    throw new UnsupportedOperationException(
      "Cannot find a valid SVD implementation");
  }
  


















  @Deprecated
  public static Matrix[] svd(Matrix m, int dimensions)
  {
    return svd(m, getFastestAvailableAlgorithm(), dimensions);
  }
  



















  @Deprecated
  public static Matrix[] svd(Matrix m, Algorithm algorithm, int dimensions)
  {
    if (algorithm.equals(Algorithm.ANY))
      algorithm = getFastestAvailableAlgorithm();
    if (algorithm == null)
      throw new UnsupportedOperationException(
        "No SVD algorithm is available on this system");
    MatrixIO.Format fmt = null;
    switch (algorithm)
    {



    case OCTAVE: 
      return coltSVD(m.toDenseArray(), !(m instanceof SparseMatrix), 
        dimensions);
    case MATLAB: 
      return jamaSVD(m.toDenseArray(), dimensions);
    

    case ANY: 
      fmt = MatrixIO.Format.SVDLIBC_SPARSE_BINARY;
      break;
    case COLT: 
    case JAMA: 
    default: 
      fmt = MatrixIO.Format.MATLAB_SPARSE; }
    
    try {
      File tmpFile = File.createTempFile("matrix-svd", ".dat");
      tmpFile.deleteOnExit();
      MatrixIO.writeMatrix(m, tmpFile, fmt);
      return svd(tmpFile, algorithm, fmt, dimensions);
    }
    catch (IOException ioe) {
      SVD_LOGGER.log(Level.SEVERE, "convertFormat", ioe);
      
      throw new UnsupportedOperationException(
        "SVD algorithm failed in writing matrix to disk");
    }
  }
  















  @Deprecated
  public static Matrix[] svd(File matrix, int dimensions)
  {
    return svd(matrix, Algorithm.ANY, 
      MatrixIO.Format.MATLAB_SPARSE, dimensions);
  }
  


















  @Deprecated
  public static Matrix[] svd(File matrix, Algorithm alg, int dimensions)
  {
    return svd(matrix, alg, MatrixIO.Format.MATLAB_SPARSE, dimensions);
  }
  

















  @Deprecated
  public static Matrix[] svd(File matrix, MatrixIO.Format format, int dimensions)
  {
    return svd(matrix, Algorithm.ANY, format, dimensions);
  }
  



















  @Deprecated
  public static Matrix[] svd(File matrix, Algorithm alg, MatrixIO.Format format, int dimensions)
  {
    try
    {
      File converted = null;
      switch (alg)
      {

      case ANY: 
        switch (format) {
        case DENSE_TEXT: 
        case MATLAB_SPARSE: 
        case SVDLIBC_DENSE_BINARY: 
        case SVDLIBC_DENSE_TEXT: 
          converted = matrix;
          break;
        default: 
          SVD_LOGGER.fine("converting input matrix format from" + 
            format + " to SVDLIBC " + 
            "ready format");
          converted = MatrixIO.convertFormat(
            matrix, format, MatrixIO.Format.SVDLIBC_SPARSE_BINARY);
          format = MatrixIO.Format.SVDLIBC_SPARSE_BINARY;
        }
        
        return svdlibc(converted, dimensions, format);
      

      case MATLAB: 
        double[][] inputMatrix = 
          MatrixIO.readMatrixArray(matrix, format);
        return jamaSVD(inputMatrix, dimensions);
      


      case COLT: 
        if (!format.equals(MatrixIO.Format.MATLAB_SPARSE)) {
          matrix = MatrixIO.convertFormat(
            matrix, format, MatrixIO.Format.MATLAB_SPARSE);
        }
        return matlabSVDS(matrix, dimensions);
      

      case JAMA: 
        if (!format.equals(MatrixIO.Format.MATLAB_SPARSE)) {
          matrix = MatrixIO.convertFormat(
            matrix, format, MatrixIO.Format.MATLAB_SPARSE);
        }
        return octaveSVDS(matrix, dimensions);
      
      case OCTAVE: 
        double[][] m = MatrixIO.readMatrixArray(matrix, format);
        return coltSVD(m, Matrices.isDense(format), dimensions);
      
      case SVDLIBC: 
        return svd(matrix, getFastestAvailableAlgorithm(), format, dimensions); }
      
    } catch (IOException ioe) {
      SVD_LOGGER.log(Level.SEVERE, "convertFormat", ioe);
    }
    

    throw new UnsupportedOperationException("Unknown algorithm: " + alg);
  }
  

  private static boolean isJAMAavailable()
  {
    try
    {
      Class localClass = loadJamaMatrixClass();
    } catch (ClassNotFoundException cnfe) {
      return false;
    }
    return true;
  }
  





  private static Class<?> loadJamaMatrixClass()
    throws ClassNotFoundException
  {
    try
    {
      return Class.forName("Jama.Matrix");


    }
    catch (ClassNotFoundException cnfe)
    {


      String jamaProp = System.getProperty("jama.path");
      

      if (jamaProp == null) {
        throw cnfe;
      }
      File jamaJarFile = new File(jamaProp);
      try
      {
        URLClassLoader classLoader = 
          new URLClassLoader(
          new URL[] { jamaJarFile.toURI().toURL() });
        
        return Class.forName("Jama.Matrix", true, 
          classLoader);

      }
      catch (Exception localException)
      {

        throw cnfe;
      }
    }
  }
  


  private static boolean isColtAvailable()
  {
    try
    {
      Class localClass = loadColtSparseMatrixClass();
    } catch (ClassNotFoundException cnfe) {
      return false;
    }
    return true;
  }
  



  private static Class<?> loadColtSparseMatrixClass()
    throws ClassNotFoundException
  {
    String coltMatrixClassName = 
      "cern.colt.matrix.impl.SparseDoubleMatrix2D";
    return loadColtClass(coltMatrixClassName);
  }
  



  private static Class<?> loadColtDenseMatrixClass()
    throws ClassNotFoundException
  {
    String coltMatrixClassName = 
      "cern.colt.matrix.impl.DenseDoubleMatrix2D";
    return loadColtClass(coltMatrixClassName);
  }
  



  private static Class<?> loadColtSVDClass()
    throws ClassNotFoundException
  {
    String coltSVDClassName = 
      "cern.colt.matrix.linalg.SingularValueDecomposition";
    return loadColtClass(coltSVDClassName);
  }
  




  private static Class<?> loadColtClass(String className)
    throws ClassNotFoundException
  {
    try
    {
      return 
        Class.forName(className);


    }
    catch (ClassNotFoundException cnfe)
    {


      String coltProp = System.getProperty("colt.path");
      

      if (coltProp == null) {
        throw cnfe;
      }
      File coltJarFile = new File(coltProp);
      





      try
      {
        if (coltClassLoader == null) {
          coltClassLoader = 
            new URLClassLoader(
            new URL[] { coltJarFile.toURI().toURL() });
        }
        
        return Class.forName(className, true, 
          coltClassLoader);

      }
      catch (Exception localException)
      {

        throw cnfe;
      }
    }
  }
  




  private static URLClassLoader coltClassLoader = null;
  

  public static boolean isSVDLIBCavailable()
  {
    try
    {
      Process svdlibc = Runtime.getRuntime().exec("svd");
      BufferedReader br = new BufferedReader(
        new InputStreamReader(svdlibc.getInputStream()));
      


      String line = null; while ((line = br.readLine()) != null) {}
      
      br.close();
      svdlibc.waitFor();
    } catch (Exception e) {
      return false;
    }
    return true;
  }
  

  public static boolean isOctaveAvailable()
  {
    try
    {
      Process octave = Runtime.getRuntime().exec("octave -v");
      BufferedReader br = new BufferedReader(
        new InputStreamReader(octave.getInputStream()));
      


      String line = null; while ((line = br.readLine()) != null) {}
      
      br.close();
      octave.waitFor();
    } catch (Exception e) {
      return false;
    }
    return true;
  }
  

  public static boolean isMatlabAvailable()
  {
    try
    {
      Process matlab = Runtime.getRuntime().exec("matlab -h");
      BufferedReader br = new BufferedReader(
        new InputStreamReader(matlab.getInputStream()));
      


      String line = null; while ((line = br.readLine()) != null) {}
      
      br.close();
      matlab.waitFor();
    } catch (Exception ioe) {
      return false;
    }
    return true;
  }
  














  static Matrix[] jamaSVD(double[][] inputMatrix, int dimensions)
  {
    try
    {
      SVD_LOGGER.fine("attempting JAMA");
      isJAMAavailable();
      
      int rows = inputMatrix.length;
      int cols = inputMatrix[0].length;
      
      Class<?> clazz = loadJamaMatrixClass();
      Constructor<?> c = clazz.getConstructor(new Class[] { [[D.class });
      Object jamaMatrix = 
        c.newInstance(new Object[] { inputMatrix });
      Method svdMethod = clazz.getMethod("svd", new Class[0]);
      Object svdObject = svdMethod.invoke(jamaMatrix, new Object[0]);
      

      String[] matrixMethods = { "getU", "getS", "getV" };
      String[] matrixNames = { "JAMA-U", "JAMA-S", "JAMA-V" };
      
      Matrix[] usv = new Matrix[3];
      

      for (int i = 0; i < 3; i++) {
        Method matrixAccessMethod = svdObject.getClass()
          .getMethod(matrixMethods[i], new Class[0]);
        Object matrixObject = matrixAccessMethod.invoke(
          svdObject, new Object[0]);
        Method toArrayMethod = matrixObject.getClass()
          .getMethod("getArray", new Class[0]);
        double[][] matrixArray = (double[][])toArrayMethod
          .invoke(matrixObject, new Object[0]);
        



        switch (i) {
        case 0: 
          Matrix u = Matrices.create(rows, dimensions, 
            Matrix.Type.DENSE_IN_MEMORY);
          
          for (int row = 0; row < rows; row++) {
            for (int col = 0; col < dimensions; col++) {
              u.set(row, col, matrixArray[row][col]);
            }
          }
          usv[i] = u;
          break;
        


        case 1: 
          Matrix s = new DiagonalMatrix(dimensions);
          for (int diag = 0; diag < dimensions; diag++) {
            s.set(diag, diag, matrixArray[diag][diag]);
          }
          usv[i] = s;
          break;
        




        case 2: 
          Matrix v = Matrices.create(dimensions, cols, 
            Matrix.Type.DENSE_ON_DISK);
          



          for (int row = 0; row < dimensions; row++) {
            for (int col = 0; col < cols; col++) {
              v.set(row, col, matrixArray[col][row]);
            }
          }
          usv[i] = v;
        }
        
      }
      
      return usv;
    } catch (ClassNotFoundException cnfe) {
      SVD_LOGGER.log(Level.SEVERE, "JAMA", cnfe);
    } catch (NoSuchMethodException nsme) {
      SVD_LOGGER.log(Level.SEVERE, "JAMA", nsme);
    } catch (InstantiationException ie) {
      SVD_LOGGER.log(Level.SEVERE, "JAMA", ie);
    } catch (IllegalAccessException iae) {
      SVD_LOGGER.log(Level.SEVERE, "JAMA", iae);
    } catch (InvocationTargetException ite) {
      SVD_LOGGER.log(Level.SEVERE, "JAMA", ite);
    }
    
    throw new UnsupportedOperationException(
      "JAMA-based SVD is not available on this system");
  }
  


















  static Matrix[] coltSVD(double[][] inputMatrix, boolean isDense, int dimensions)
  {
    try
    {
      SVD_LOGGER.fine("attempting COLT");
      
      int rows = inputMatrix.length;
      int cols = inputMatrix[0].length;
      



      Class<?> clazz = isDense ? 
        loadColtDenseMatrixClass() : 
        loadColtSparseMatrixClass();
      Constructor<?> c = clazz.getConstructor(new Class[] { [[D.class });
      












      Object coltMatrix = 
        c.newInstance(new Object[] { inputMatrix });
      

      inputMatrix = null;
      
      Class<?> svdClass = loadColtSVDClass();
      



      Class<?> matrixBaseClass = loadColtClass(
        "cern.colt.matrix.DoubleMatrix2D");
      

      Constructor<?> svdConstructor = 
        svdClass.getConstructor(new Class[] { matrixBaseClass });
      

      Object svdObject = 
        svdConstructor.newInstance(new Object[] { coltMatrix });
      
      Matrix[] usv = new Matrix[3];
      

      String[] matrixMethods = { "getU", "getS", "getV" };
      String[] matrixNames = { "COLT-U", "COLT-S", "COLT-V" };
      

      for (int i = 0; i < 3; i++) {
        Method matrixAccessMethod = svdObject.getClass()
          .getMethod(matrixMethods[i], new Class[0]);
        Object matrixObject = matrixAccessMethod.invoke(
          svdObject, new Object[0]);
        Method toArrayMethod = matrixObject.getClass()
          .getMethod("toArray", new Class[0]);
        



        switch (i)
        {

        case 0: 
          double[][] matrixArray = (double[][])toArrayMethod
            .invoke(matrixObject, new Object[0]);
          
          Matrix u = Matrices.create(rows, dimensions, 
            Matrix.Type.DENSE_IN_MEMORY);
          
          for (int row = 0; row < rows; row++) {
            for (int col = 0; col < dimensions; col++) {
              u.set(row, col, matrixArray[row][col]);
            }
          }
          usv[i] = u;
          break;
        






        case 1: 
          Matrix s = new DiagonalMatrix(dimensions);
          
          Method get = matrixObject.getClass()
            .getMethod("get", new Class[] { Integer.TYPE, 
            Integer.TYPE });
          
          for (int diag = 0; diag < dimensions; diag++) {
            double value = ((Double)get.invoke(matrixObject, 
              new Object[] { Integer.valueOf(diag), 
              Integer.valueOf(diag) })).doubleValue();
            s.set(diag, diag, value);
          }
          usv[i] = s;
          break;
        



        case 2: 
          double[][] matrixArray = (double[][])toArrayMethod
            .invoke(matrixObject, new Object[0]);
          


          Matrix v = Matrices.create(dimensions, cols, 
            Matrix.Type.DENSE_ON_DISK);
          

          for (int row = 0; row < dimensions; row++) {
            for (int col = 0; col < cols; col++) {
              v.set(row, col, matrixArray[row][col]);
            }
          }
          usv[i] = v;
        }
        
      }
      
      return usv;
    } catch (ClassNotFoundException cnfe) {
      SVD_LOGGER.log(Level.SEVERE, "COLT", cnfe);
    } catch (NoSuchMethodException nsme) {
      SVD_LOGGER.log(Level.SEVERE, "COLT", nsme);
    } catch (InstantiationException ie) {
      SVD_LOGGER.log(Level.SEVERE, "COLT", ie);
    } catch (IllegalAccessException iae) {
      SVD_LOGGER.log(Level.SEVERE, "COLT", iae);
    } catch (InvocationTargetException ite) {
      SVD_LOGGER.log(Level.SEVERE, "COLT", ite);
    }
    
    throw new UnsupportedOperationException(
      "COLT-based SVD is not available on this system");
  }
  










  static Matrix[] svdlibc(File matrix, int dimensions, MatrixIO.Format format)
  {
    try
    {
      String formatString = "";
      

      switch (format) {
      case SVDLIBC_DENSE_TEXT: 
        formatString = " -r db ";
        break;
      case MATLAB_SPARSE: 
        formatString = " -r dt ";
        break;
      case SVDLIBC_DENSE_BINARY: 
        formatString = " -r sb ";
        break;
      case DENSE_TEXT: 
        break;
      
      default: 
        throw new UnsupportedOperationException(
          "Format type is not accepted");
      }
      
      File outputMatrixFile = File.createTempFile("svdlibc", ".dat");
      outputMatrixFile.deleteOnExit();
      String outputMatrixPrefix = outputMatrixFile.getAbsolutePath();
      
      SVD_LOGGER.fine("creating SVDLIBC factor matrices at: " + 
        outputMatrixPrefix);
      String commandLine = "svd -o " + outputMatrixPrefix + formatString + 
        " -w dt " + 
        " -d " + dimensions + " " + matrix.getAbsolutePath();
      SVD_LOGGER.fine(commandLine);
      Process svdlibc = Runtime.getRuntime().exec(commandLine);
      
      BufferedReader stdout = new BufferedReader(
        new InputStreamReader(svdlibc.getInputStream()));
      BufferedReader stderr = new BufferedReader(
        new InputStreamReader(svdlibc.getErrorStream()));
      
      StringBuilder output = new StringBuilder("SVDLIBC output:\n");
      for (String line = null; (line = stderr.readLine()) != null;) {
        output.append(line).append("\n");
      }
      SVD_LOGGER.fine(output.toString());
      
      int exitStatus = svdlibc.waitFor();
      SVD_LOGGER.fine("svdlibc exit status: " + exitStatus);
      

      if (exitStatus == 0)
      {
        File Ut = new File(outputMatrixPrefix + "-Ut");
        File S = new File(outputMatrixPrefix + "-S");
        File Vt = new File(outputMatrixPrefix + "-Vt");
        


        return new Matrix[] {
        



          MatrixIO.readMatrix(Ut, MatrixIO.Format.SVDLIBC_DENSE_TEXT, 
          Matrix.Type.DENSE_IN_MEMORY, true), 
          



          readSVDLIBCsingularVector(S), 
          
          MatrixIO.readMatrix(Vt, MatrixIO.Format.SVDLIBC_DENSE_TEXT, 
          Matrix.Type.DENSE_ON_DISK) };
      }
      

      StringBuilder sb = new StringBuilder();
      for (String line = null; (line = stderr.readLine()) != null;) {
        sb.append(line).append("\n");
      }
      
      SVD_LOGGER.warning("svdlibc exited with error status.  stderr:\n" + 
        sb.toString());
    }
    catch (IOException ioe) {
      SVD_LOGGER.log(Level.SEVERE, "SVDLIBC", ioe);
    } catch (InterruptedException ie) {
      SVD_LOGGER.log(Level.SEVERE, "SVDLIBC", ie);
    }
    
    throw new UnsupportedOperationException(
      "SVDLIBC is not correctly installed on this system");
  }
  



  private static Matrix readSVDLIBCsingularVector(File sigmaMatrixFile)
    throws IOException
  {
    BufferedReader br = 
      new BufferedReader(new FileReader(sigmaMatrixFile));
    
    int dimension = -1;
    int valsSeen = 0;
    Matrix m = null;
    String[] vals; int i; for (String line = null; (line = br.readLine()) != null; 
        
        i < vals.length)
    {
      vals = line.split("\\s+");
      i = 0; continue;
      
      if (dimension == -1) {
        dimension = Integer.parseInt(vals[i]);
        m = new DiagonalMatrix(dimension);
      }
      else {
        m.set(valsSeen, valsSeen, Double.parseDouble(vals[i]));
        valsSeen++;
      }
      i++;
    }
    









    return m;
  }
  











  static Matrix[] matlabSVDS(File matrix, int dimensions)
  {
    try
    {
      File uOutput = File.createTempFile("matlab-svds-U", ".dat");
      File sOutput = File.createTempFile("matlab-svds-S", ".dat");
      File vOutput = File.createTempFile("matlab-svds-V", ".dat");
      if (SVD_LOGGER.isLoggable(Level.FINE)) {
        SVD_LOGGER.fine("writing Matlab output to files:\n  " + 
          uOutput + "\n" + 
          "  " + sOutput + "\n" + 
          "  " + vOutput + "\n");
      }
      
      uOutput.deleteOnExit();
      sOutput.deleteOnExit();
      vOutput.deleteOnExit();
      
      String commandLine = "matlab -nodisplay -nosplash -nojvm";
      SVD_LOGGER.fine(commandLine);
      Process matlab = Runtime.getRuntime().exec(commandLine);
      

      BufferedReader br = new BufferedReader(
        new InputStreamReader(matlab.getInputStream()));
      

      PrintWriter pw = new PrintWriter(matlab.getOutputStream());
      pw.println(
        "Z = load('" + matrix.getAbsolutePath() + "','-ascii');\n" + 
        "A = spconvert(Z);\n" + 
        "% Remove the raw data file to save space\n" + 
        "clear Z;\n" + 
        "[U, S, V] = svds(A, " + dimensions + " );\n" + 
        "save " + uOutput.getAbsolutePath() + " U -ASCII\n" + 
        "save " + sOutput.getAbsolutePath() + " S -ASCII\n" + 
        "save " + vOutput.getAbsolutePath() + " V -ASCII\n" + 
        "fprintf('Matlab Finished\\n');");
      pw.close();
      

      StringBuilder output = new StringBuilder("Matlab svds output:\n");
      for (String line = null; (line = br.readLine()) != null;) {
        output.append(line).append("\n");
        if (line.equals("Matlab Finished")) {
          matlab.destroy();
        }
      }
      SVD_LOGGER.fine(output.toString());
      
      int exitStatus = matlab.waitFor();
      SVD_LOGGER.fine("Matlab svds exit status: " + exitStatus);
      

      if (exitStatus == 0)
      {


        return new Matrix[] {
        

          MatrixIO.readMatrix(uOutput, MatrixIO.Format.DENSE_TEXT, 
          Matrix.Type.DENSE_IN_MEMORY), 
          
          MatrixIO.readMatrix(sOutput, MatrixIO.Format.DENSE_TEXT, 
          Matrix.Type.SPARSE_ON_DISK), 
          

          MatrixIO.readMatrix(vOutput, MatrixIO.Format.DENSE_TEXT, 
          Matrix.Type.DENSE_ON_DISK, true) };
      }
    }
    catch (IOException ioe)
    {
      SVD_LOGGER.log(Level.SEVERE, "Matlab svds", ioe);
    } catch (InterruptedException ie) {
      SVD_LOGGER.log(Level.SEVERE, "Matlab svds", ie);
    }
    
    throw new UnsupportedOperationException(
      "Matlab svds is not correctly installed on this system");
  }
  











  static Matrix[] octaveSVDS(File matrix, int dimensions)
  {
    try
    {
      File octaveFile = File.createTempFile("octave-svds", ".m");
      File uOutput = File.createTempFile("octave-svds-U", ".dat");
      File sOutput = File.createTempFile("octave-svds-S", ".dat");
      File vOutput = File.createTempFile("octave-svds-V", ".dat");
      octaveFile.deleteOnExit();
      uOutput.deleteOnExit();
      sOutput.deleteOnExit();
      vOutput.deleteOnExit();
      

      PrintWriter pw = new PrintWriter(octaveFile);
      pw.println(
        "Z = load('" + matrix.getAbsolutePath() + "','-ascii');\n" + 
        "A = spconvert(Z);\n" + 
        "% Remove the raw data file to save space\n" + 
        "clear Z;\n" + 
        "[U, S, V] = svds(A, " + dimensions + " );\n" + 
        "save(\"-ascii\", \"" + uOutput.getAbsolutePath() + "\", \"U\");\n" + 
        "save(\"-ascii\", \"" + sOutput.getAbsolutePath() + "\", \"S\");\n" + 
        "save(\"-ascii\", \"" + vOutput.getAbsolutePath() + "\", \"V\");\n" + 
        "fprintf('Octave Finished\\n');\n");
      pw.close();
      


      String commandLine = "octave " + octaveFile.getAbsolutePath();
      SVD_LOGGER.fine(commandLine);
      Process octave = Runtime.getRuntime().exec(commandLine);
      
      BufferedReader br = new BufferedReader(
        new InputStreamReader(octave.getInputStream()));
      BufferedReader stderr = new BufferedReader(
        new InputStreamReader(octave.getErrorStream()));
      

      StringBuilder output = new StringBuilder("Octave svds output:\n");
      for (String line = null; (line = br.readLine()) != null;) {
        output.append(line).append("\n");
      }
      SVD_LOGGER.fine(output.toString());
      
      int exitStatus = octave.waitFor();
      SVD_LOGGER.fine("Octave svds exit status: " + exitStatus);
      

      if (exitStatus == 0)
      {


        return new Matrix[] {
        

          MatrixIO.readMatrix(uOutput, MatrixIO.Format.DENSE_TEXT, 
          Matrix.Type.DENSE_IN_MEMORY), 
          
          MatrixIO.readMatrix(sOutput, MatrixIO.Format.DENSE_TEXT, 
          Matrix.Type.SPARSE_ON_DISK), 
          

          MatrixIO.readMatrix(vOutput, MatrixIO.Format.DENSE_TEXT, 
          Matrix.Type.DENSE_ON_DISK, true) };
      }
      

      StringBuilder sb = new StringBuilder();
      for (String line = null; (line = stderr.readLine()) != null;) {
        sb.append(line).append("\n");
      }
      
      SVD_LOGGER.warning("Octave exited with error status.  stderr:\n" + 
        sb.toString());
    }
    catch (IOException ioe) {
      SVD_LOGGER.log(Level.SEVERE, "Octave svds", ioe);
    } catch (InterruptedException ie) {
      SVD_LOGGER.log(Level.SEVERE, "Octave svds", ie);
    }
    
    throw new UnsupportedOperationException(
      "Octave svds is not correctly installed on this system");
  }
}
