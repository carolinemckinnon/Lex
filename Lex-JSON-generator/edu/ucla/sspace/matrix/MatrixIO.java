package edu.ucla.sspace.matrix;

import edu.ucla.sspace.vector.CompactSparseVector;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.SparseDoubleVector;
import edu.ucla.sspace.vector.SparseVector;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;








































public class MatrixIO
{
  private static final Logger MATRIX_IO_LOGGER = Logger.getLogger(MatrixIO.class.getName());
  































  private MatrixIO() {}
  































  public static enum Format
  {
    DENSE_TEXT, 
    




    MATLAB_SPARSE, 
    





    SVDLIBC_SPARSE_TEXT, 
    





    SVDLIBC_DENSE_TEXT, 
    





    SVDLIBC_SPARSE_BINARY, 
    





    SVDLIBC_DENSE_BINARY, 
    





    CLUTO_DENSE, 
    







    CLUTO_SPARSE;
  }
  
























  public static File convertFormat(File matrix, Format current, Format desired)
    throws IOException
  {
    return convertFormat(matrix, current, desired, false);
  }
  















  public static File convertFormat(File matrix, Format current, Format desired, boolean transpose)
    throws IOException
  {
    if ((!transpose) && (current.equals(desired))) {
      return matrix;
    }
    

    switch (current) {
    case CLUTO_SPARSE: 
      if (desired.equals(Format.SVDLIBC_SPARSE_TEXT)) {
        File output = File.createTempFile(
          "matlab-to-SVDLIBC-sparse-text", ".dat");
        output.deleteOnExit();
        matlabToSvdlibcSparseText(matrix, output, transpose);
        return output;
      }
      if (desired.equals(Format.SVDLIBC_SPARSE_BINARY)) {
        File output = File.createTempFile(
          "matlab-to-SVDLIBC-sparse-binary", ".dat");
        output.deleteOnExit();
        matlabToSvdlibcSparseBinary(matrix, output, transpose);
        return output;
      }
      
      break;
    case SVDLIBC_DENSE_BINARY: 
      if (desired.equals(Format.MATLAB_SPARSE)) {
        File output = File.createTempFile(
          "SVDLIBC-sparse-binary-to-Matlab", ".dat");
        output.deleteOnExit();
        svdlibcSparseBinaryToMatlab(matrix, output, transpose);
        return output;
      }
      
      break;
    }
    
    
    File output = File.createTempFile("transposed", ".dat");
    output.deleteOnExit();
    

    Matrix transposed = readMatrix(matrix, current, 
      Matrix.Type.SPARSE_IN_MEMORY, true);
    writeMatrix(transposed, output, desired);
    transposed = null;
    return output;
  }
  










  public static Iterator<MatrixEntry> getMatrixFileIterator(File matrixFile, Format fileFormat)
    throws IOException
  {
    switch (fileFormat) {
    case CLUTO_DENSE: 
      return new DenseTextFileIterator(matrixFile);
    case SVDLIBC_DENSE_BINARY: 
      return new SvdlibcSparseBinaryFileIterator(matrixFile);
    case DENSE_TEXT: 
      return new SvdlibcSparseTextFileIterator(matrixFile);
    case SVDLIBC_DENSE_TEXT: 
      return new SvdlibcDenseBinaryFileIterator(matrixFile);
    case SVDLIBC_SPARSE_TEXT: 
      return new ClutoSparseFileIterator(matrixFile);
    
    case MATLAB_SPARSE: 
    case SVDLIBC_SPARSE_BINARY: 
      return new SvdlibcDenseTextFileIterator(matrixFile);
    case CLUTO_SPARSE: 
      return new MatlabSparseFileIterator(matrixFile);
    }
    throw new Error("Iterating over matrices of " + fileFormat + 
      " format is not " + 
      "currently supported. Email " + 
      "s-space-research-dev@googlegroups.com to request its" + 
      "inclusion and it will be quickly added");
  }
  







  public static FileTransformer fileTransformer(Format format)
  {
    switch (format) {
    case CLUTO_SPARSE: 
      return new MatlabSparseFileTransformer();
    case DENSE_TEXT: 
      return new SvdlibcSparseTextFileTransformer();
    case MATLAB_SPARSE: 
      return new SvdlibcDenseTextFileTransformer();
    case SVDLIBC_DENSE_BINARY: 
      return new SvdlibcSparseBinaryFileTransformer();
    case SVDLIBC_DENSE_TEXT: 
      return new SvdlibcDenseBinaryFileTransformer();
    }
    throw new UnsupportedOperationException(
      "Transforming format " + format + " is currently " + 
      "not implemented.  Please email " + 
      "s-space-research-dev@googlegroups.com to have this " + 
      "implemented.");
  }
  





  private static void matlabToSvdlibcSparseText(File input, File output, boolean transpose)
    throws IOException
  {
    MATRIX_IO_LOGGER.info("Converting from Matlab double values to SVDLIBC float values; possible loss of precision");
    

    BufferedReader br = new BufferedReader(new FileReader(input));
    Map<Integer, Integer> colToNonZero = new HashMap();
    

    int rows = 0;int cols = 0;int nonZero = 0;
    for (String line = null; (line = br.readLine()) != null;) {
      String[] rowColVal = line.split("\\s+");
      int col;
      int row; int col; if (transpose) {
        int row = Integer.parseInt(rowColVal[1]);
        col = Integer.parseInt(rowColVal[0]);
      }
      else {
        row = Integer.parseInt(rowColVal[0]);
        col = Integer.parseInt(rowColVal[1]);
      }
      if (row > rows)
        rows = row;
      if (col > cols)
        cols = col;
      nonZero++;
      


      Integer colCount = (Integer)colToNonZero.get(Integer.valueOf(col - 1));
      colToNonZero.put(Integer.valueOf(col - 1), Integer.valueOf(colCount == null ? 1 : colCount.intValue() + 1));
    }
    br.close();
    

    PrintWriter pw = new PrintWriter(output);
    pw.println(rows + "\t" + cols + "\t" + nonZero);
    





    int chunkSize = 1000;
    



    int lastCol = -1;
    

    int lowerBound = 0; for (int upperBound = chunkSize; lowerBound < rows; 
        upperBound += chunkSize)
    {


      br = new BufferedReader(new FileReader(input));
      




      int[] colIndices = new int[cols];
      

      SortedMap<Integer, int[]> colToRowIndex = 
        new TreeMap();
      SortedMap<Integer, float[]> colToRowValues = 
        new TreeMap();
      
      for (String line = null; (line = br.readLine()) != null;) {
        rowColVal = line.split("\\s+");
        int col;
        int row; int col; if (transpose) {
          int row = Integer.parseInt(rowColVal[1]) - 1;
          col = Integer.parseInt(rowColVal[0]) - 1;
        }
        else {
          row = Integer.parseInt(rowColVal[0]) - 1;
          col = Integer.parseInt(rowColVal[1]) - 1;
        }
        

        float val = Double.valueOf(rowColVal[2]).floatValue();
        

        if ((col >= lowerBound) && (col < upperBound))
        {




          int[] rowIndices = (int[])colToRowIndex.get(Integer.valueOf(col));
          float[] rowValues = (float[])colToRowValues.get(Integer.valueOf(col));
          if (rowIndices == null) {
            rowIndices = new int[((Integer)colToNonZero.get(Integer.valueOf(col))).intValue()];
            rowValues = new float[((Integer)colToNonZero.get(Integer.valueOf(col))).intValue()];
            colToRowIndex.put(Integer.valueOf(col), rowIndices);
            colToRowValues.put(Integer.valueOf(col), rowValues);
          }
          


          int curColIndex = colIndices[col];
          rowIndices[curColIndex] = row;
          rowValues[curColIndex] = val;
          colIndices[col] += 1;
        } }
      br.close();
      
      int[] nonZeroRows;
      
      int i;
      for (String[] rowColVal = colToRowIndex.entrySet().iterator(); rowColVal.hasNext(); 
          















          i < nonZeroRows.length)
      {
        Map.Entry<Integer, int[]> e = (Map.Entry)rowColVal.next();
        int col = ((Integer)e.getKey()).intValue();
        nonZeroRows = (int[])e.getValue();
        float[] values = (float[])colToRowValues.get(Integer.valueOf(col));
        
        if (col != lastCol)
        {

          for (int i = lastCol + 1; i < col; i++) {
            pw.println(0);
          }
          
          int colCount = ((Integer)colToNonZero.get(Integer.valueOf(col))).intValue();
          lastCol = col;
          pw.println(colCount);
        }
        
        i = 0; continue;
        pw.println(nonZeroRows[i] + " " + values[i]);i++;
      }
      lowerBound = upperBound;
    }
    
















































































    pw.flush();
    pw.close();
  }
  





  private static void matlabToSvdlibcSparseBinary(File input, File output, boolean transpose)
    throws IOException
  {
    MATRIX_IO_LOGGER.info("Converting from Matlab double values to SVDLIBC float values; possible loss of precision");
    

    BufferedReader br = new BufferedReader(new FileReader(input));
    Map<Integer, Integer> colToNonZero = new HashMap();
    

    int rows = 0;int cols = 0;int nonZero = 0;
    for (String line = null; (line = br.readLine()) != null;) {
      String[] rowColVal = line.split("\\s+");
      int col;
      int row; int col; if (transpose) {
        int row = Integer.parseInt(rowColVal[1]);
        col = Integer.parseInt(rowColVal[0]);
      }
      else {
        row = Integer.parseInt(rowColVal[0]);
        col = Integer.parseInt(rowColVal[1]);
      }
      if (row > rows)
        rows = row;
      if (col > cols)
        cols = col;
      nonZero++;
      


      Integer colCount = (Integer)colToNonZero.get(Integer.valueOf(col - 1));
      colToNonZero.put(Integer.valueOf(col - 1), Integer.valueOf(colCount == null ? 1 : colCount.intValue() + 1));
    }
    br.close();
    

    DataOutputStream dos = new DataOutputStream(
      new BufferedOutputStream(new FileOutputStream(output)));
    dos.writeInt(rows);
    dos.writeInt(cols);
    dos.writeInt(nonZero);
    





    int chunkSize = 1000;
    



    int lastCol = -1;
    

    int lowerBound = 0; for (int upperBound = chunkSize; lowerBound < rows; 
        upperBound += chunkSize)
    {


      br = new BufferedReader(new FileReader(input));
      



      int[] colIndices = new int[cols];
      

      SortedMap<Integer, int[]> colToRowIndex = 
        new TreeMap();
      SortedMap<Integer, float[]> colToRowValues = 
        new TreeMap();
      
      for (String line = null; (line = br.readLine()) != null;) {
        rowColVal = line.split("\\s+");
        int col;
        int row; int col; if (transpose) {
          int row = Integer.parseInt(rowColVal[1]) - 1;
          col = Integer.parseInt(rowColVal[0]) - 1;
        }
        else {
          row = Integer.parseInt(rowColVal[0]) - 1;
          col = Integer.parseInt(rowColVal[1]) - 1;
        }
        

        float val = Double.valueOf(rowColVal[2]).floatValue();
        

        if ((col >= lowerBound) && (col < upperBound))
        {




          int[] rowIndices = (int[])colToRowIndex.get(Integer.valueOf(col));
          float[] rowValues = (float[])colToRowValues.get(Integer.valueOf(col));
          if (rowIndices == null) {
            rowIndices = new int[((Integer)colToNonZero.get(Integer.valueOf(col))).intValue()];
            rowValues = new float[((Integer)colToNonZero.get(Integer.valueOf(col))).intValue()];
            colToRowIndex.put(Integer.valueOf(col), rowIndices);
            colToRowValues.put(Integer.valueOf(col), rowValues);
          }
          


          int curColIndex = colIndices[col];
          rowIndices[curColIndex] = row;
          rowValues[curColIndex] = val;
          colIndices[col] += 1;
        } }
      br.close();
      
      int[] nonZeroRows;
      
      int i;
      for (String[] rowColVal = colToRowIndex.entrySet().iterator(); rowColVal.hasNext(); 
          















          i < nonZeroRows.length)
      {
        Map.Entry<Integer, int[]> e = (Map.Entry)rowColVal.next();
        int col = ((Integer)e.getKey()).intValue();
        nonZeroRows = (int[])e.getValue();
        float[] values = (float[])colToRowValues.get(Integer.valueOf(col));
        
        if (col != lastCol)
        {

          for (int i = lastCol + 1; i < col; i++) {
            dos.writeInt(0);
          }
          
          int colCount = ((Integer)colToNonZero.get(Integer.valueOf(col))).intValue();
          lastCol = col;
          dos.writeInt(colCount);
        }
        
        i = 0; continue;
        dos.writeInt(nonZeroRows[i]);
        dos.writeFloat(values[i]);i++;
      }
      lowerBound = upperBound;
    }
    
















































































    dos.close();
  }
  





  private static void svdlibcSparseBinaryToMatlab(File input, File output, boolean transpose)
    throws IOException
  {
    DataInputStream dis = new DataInputStream(
      new BufferedInputStream(new FileInputStream(input)));
    
    PrintWriter pw = new PrintWriter(output);
    
    int rows = dis.readInt();
    int cols = dis.readInt();
    int entries = dis.readInt();
    

    int entriesSeen = 0;
    for (int col = 0; 
        entriesSeen < entries; col++) {
      int nonZero = dis.readInt();
      
      for (int i = 0; i < nonZero; entriesSeen++) {
        int row = dis.readInt();
        float val = dis.readFloat();
        if (transpose) {
          pw.println(1 + col + " " + (1 + row) + " " + val);
        } else {
          pw.println(1 + row + " " + (1 + col) + " " + val);
        }
        i++;
      }
    }
    






    dis.close();
    pw.close();
  }
  










  @Deprecated
  public static double[][] readMatrixArray(File input, Format format)
    throws IOException
  {
    return readMatrix(input, format).toDenseArray();
  }
  














  public static Matrix readMatrix(File matrix, Format format)
    throws IOException
  {
    switch (format)
    {
    case CLUTO_SPARSE: 
    case DENSE_TEXT: 
    case SVDLIBC_DENSE_BINARY: 
    case SVDLIBC_SPARSE_TEXT: 
      return readMatrix(matrix, format, 
        Matrix.Type.SPARSE_IN_MEMORY, false);
    
    case CLUTO_DENSE: 
    case MATLAB_SPARSE: 
    case SVDLIBC_DENSE_TEXT: 
    case SVDLIBC_SPARSE_BINARY: 
      return readMatrix(matrix, format, 
        Matrix.Type.DENSE_IN_MEMORY, false);
    }
    throw new Error(
      "Reading matrices of " + format + " format is not " + 
      "currently supported. Email " + 
      "s-space-research-dev@googlegroups.com to request " + 
      "its inclusion and it will be quickly added");
  }
  



















  public static Matrix readMatrix(File matrix, Format format, Matrix.Type matrixType)
    throws IOException
  {
    return readMatrix(matrix, format, matrixType, false);
  }
  























  public static Matrix readMatrix(File matrix, Format format, Matrix.Type matrixType, boolean transposeOnRead)
    throws IOException
  {
    try
    {
      switch (format) {
      case CLUTO_DENSE: 
        return readDenseTextMatrix(matrix, matrixType, transposeOnRead);
      
      case CLUTO_SPARSE: 
        return readMatlabSparse(matrix, matrixType, transposeOnRead);
      
      case SVDLIBC_SPARSE_TEXT: 
        return readClutoSparse(matrix, matrixType, transposeOnRead);
      
      case DENSE_TEXT: 
        return readSparseSVDLIBCtext(matrix, matrixType, transposeOnRead);
      

      case MATLAB_SPARSE: 
      case SVDLIBC_SPARSE_BINARY: 
        return readDenseSVDLIBCtext(matrix, matrixType, transposeOnRead);
      
      case SVDLIBC_DENSE_BINARY: 
        return readSparseSVDLIBCbinary(matrix, matrixType, transposeOnRead);
      
      case SVDLIBC_DENSE_TEXT: 
        return readDenseSVDLIBCbinary(matrix, matrixType, transposeOnRead);
      }
    }
    catch (EOFException eofe) {
      throw new MatrixIOException("Matrix file " + matrix + " appeared " + 
        "truncated, or was missing expected values at the end of its " + 
        "contents.");
    }
    throw new Error("Reading matrices of " + format + " format is not " + 
      "currently supported. Email " + 
      "s-space-research-dev@googlegroups.com to request its " + 
      "inclusion and it will be quickly added");
  }
  











  private static Matrix readClutoSparse(File matrix, Matrix.Type matrixType, boolean transposeOnRead)
    throws IOException
  {
    BufferedReader br = new BufferedReader(new FileReader(matrix));
    
    String[] rowCol = br.readLine().split("\\s+");
    

    int rows = Integer.parseInt(rowCol[0]);
    int cols = Integer.parseInt(rowCol[1]);
    

    Matrix m = transposeOnRead ? 
      Matrices.create(cols, rows, matrixType) : 
      Matrices.create(rows, cols, matrixType);
    


    int row = 0;
    for (String line = null; (line = br.readLine()) != null; row++) {
      String[] colValuePairs = line.split("\\s+");
      for (int i = 0; i < colValuePairs.length; i += 2) {
        int col = Integer.parseInt(colValuePairs[i]) - 1;
        double value = Double.parseDouble(colValuePairs[(i + 1)]);
        
        if (transposeOnRead) {
          m.set(col, row, value);
        } else {
          m.set(row, col, value);
        }
      }
    }
    return m;
  }
  









  private static Matrix readDenseTextMatrix(File matrix, Matrix.Type matrixType, boolean transposeOnRead)
    throws IOException
  {
    BufferedReader br = new BufferedReader(new FileReader(matrix));
    

    int rows = 0;
    int cols = -1;
    for (String line = null; (line = br.readLine()) != null; rows++)
    {

      Scanner s = new Scanner(line);
      s.useLocale(Locale.US);
      int vals = 0;
      for (; 
          s.hasNextDouble(); s.nextDouble()) { vals++;
      }
      

      if (cols == -1) {
        cols = vals;


      }
      else if (cols != vals) {
        throw new MatrixIOException("line " + (rows + 1) + 
          " contains an inconsistent number of columns. " + 
          cols + " columns expected versus " + vals + " found.");
      }
    }
    
    br.close();
    
    if (MATRIX_IO_LOGGER.isLoggable(Level.FINE)) {
      MATRIX_IO_LOGGER.fine("reading in text matrix with " + rows + 
        " rows and " + cols + " cols");
    }
    



    Scanner scanner = new Scanner(matrix);
    scanner.useLocale(Locale.US);
    Matrix m = transposeOnRead ? 
      Matrices.create(cols, rows, matrixType) : 
      Matrices.create(rows, cols, matrixType);
    
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        double d = scanner.nextDouble();
        if (transposeOnRead) {
          m.set(col, row, d);
        } else
          m.set(row, col, d);
      }
    }
    scanner.close();
    
    return m;
  }
  









  private static Matrix readDenseSVDLIBCtext(File matrix, Matrix.Type matrixType, boolean transposeOnRead)
    throws IOException
  {
    BufferedReader br = new BufferedReader(new FileReader(matrix));
    




    int rows = -1;
    int cols = -1;
    int valuesSeen = 0;
    
    Matrix m = null;
    String[] vals;
    int i; for (String line = null; (line = br.readLine()) != null; 
        
        i < vals.length)
    {
      vals = line.split("\\s+");
      i = 0; continue;
      
      if (rows == -1) {
        rows = Integer.parseInt(vals[i]);

      }
      else if (cols == -1) {
        cols = Integer.parseInt(vals[i]);
        


        m = transposeOnRead ? 
          Matrices.create(cols, rows, matrixType) : 
          Matrices.create(rows, cols, matrixType);
        MATRIX_IO_LOGGER.log(Level.FINE, 
          "created matrix of size {0} x {1}", 
          new Object[] { Integer.valueOf(rows), 
          Integer.valueOf(cols) });
      }
      else {
        int row = valuesSeen / cols;
        int col = valuesSeen % cols;
        





        double val = vals[i].equals("nan") ? 
          NaN.0D : 
          Double.parseDouble(vals[i]);
        
        if (transposeOnRead) {
          m.set(col, row, val);
        } else {
          m.set(row, col, val);
        }
        

        valuesSeen++;
      }
      i++;
    }
    









































    br.close();
    return m;
  }
  









  private static Matrix readDenseSVDLIBCbinary(File matrix, Matrix.Type matrixType, boolean transposeOnRead)
    throws IOException
  {
    DataInputStream dis = new DataInputStream(
      new BufferedInputStream(new FileInputStream(matrix)));
    
    int rows = dis.readInt();
    int cols = dis.readInt();
    Matrix m = transposeOnRead ? 
      Matrices.create(cols, rows, matrixType) : 
      Matrices.create(rows, cols, matrixType);
    
    if (transposeOnRead) {
      for (int row = 0; row < rows; row++) {
        for (int col = 0; col < cols; col++) {
          m.set(col, row, dis.readFloat());
        }
        
      }
    } else {
      for (int row = 0; row < rows; row++) {
        for (int col = 0; col < cols; col++) {
          m.set(row, col, dis.readFloat());
        }
      }
    }
    dis.close();
    return m;
  }
  









  private static Matrix readSparseSVDLIBCbinary(File matrix, Matrix.Type matrixType, boolean transposeOnRead)
    throws IOException
  {
    DataInputStream dis = new DataInputStream(
      new BufferedInputStream(new FileInputStream(matrix)));
    
    int rows = dis.readInt();
    int cols = dis.readInt();
    int nz = dis.readInt();
    MATRIX_IO_LOGGER.fine(
      String.format("Creating %s matrix %d rows, %d cols, %d nz%n", new Object[] {
      transposeOnRead ? "transposed" : "", 
      Integer.valueOf(rows), Integer.valueOf(cols), Integer.valueOf(nz) }));
    Matrix m = null;
    



    if (transposeOnRead) {
      SparseDoubleVector[] rowArr = new SparseDoubleVector[cols];
      int entriesSeen = 0;
      int col = 0;
      int curRow = 0;
      for (; entriesSeen < nz; col++) {
        int nzInCol = dis.readInt();
        int[] indices = new int[nzInCol];
        double[] vals = new double[nzInCol];
        for (int i = 0; i < nzInCol; entriesSeen++) {
          indices[i] = dis.readInt();
          vals[i] = dis.readFloat();i++;
        }
        
        SparseDoubleVector rowVec = 
          new CompactSparseVector(indices, vals, rows);
        rowArr[curRow] = rowVec;
        curRow++;
      }
      m = Matrices.asSparseMatrix(Arrays.asList(rowArr));
    }
    else {
      m = new SparseHashMatrix(rows, cols);
      int entriesSeen = 0;
      for (int col = 0; 
          entriesSeen < nz; col++) {
        int nzInCol = dis.readInt();
        for (int i = 0; i < nzInCol; entriesSeen++) {
          m.set(dis.readInt(), col, dis.readFloat());i++;
        }
      }
    }
    dis.close();
    
    MATRIX_IO_LOGGER.fine("Completed loading matrix");
    return m;
  }
  












  private static Matrix readMatlabSparse(File matrixFile, Matrix.Type matrixType, boolean transposeOnRead)
    throws IOException
  {
    Matrix matrix = new GrowingSparseMatrix();
    BufferedReader br = new BufferedReader(new FileReader(matrixFile));
    for (String line = null; (line = br.readLine()) != null;) {
      String[] rowColVal = line.split("\\s+");
      int row = Integer.parseInt(rowColVal[0]) - 1;
      int col = Integer.parseInt(rowColVal[1]) - 1;
      double value = Double.parseDouble(rowColVal[2]);
      if (transposeOnRead) {
        matrix.set(col, row, value);
      } else
        matrix.set(row, col, value);
    }
    br.close();
    return matrix;
  }
  












  private static Matrix readSparseSVDLIBCtext(File matrix, Matrix.Type matrixType, boolean transposeOnRead)
    throws IOException
  {
    BufferedReader br = new BufferedReader(new FileReader(matrix));
    String line = br.readLine();
    if (line == null) {
      throw new IOException("Empty input Matrix");
    }
    String[] numRowsColsNonZeros = line.split("\\s");
    int rows = Integer.parseInt(numRowsColsNonZeros[0]);
    int cols = Integer.parseInt(numRowsColsNonZeros[1]);
    
    Matrix m = transposeOnRead ? 
      Matrices.create(cols, rows, matrixType) : 
      Matrices.create(rows, cols, matrixType);
    
    for (int j = 0; (j < cols) && ((line = br.readLine()) != null); j++) {
      int numNonZeros = Integer.parseInt(line);
      for (int i = 0; (i < numNonZeros) && 
            ((line = br.readLine()) != null); i++) {
        String[] rowValue = line.split("\\s");
        int row = Integer.parseInt(rowValue[0]);
        double value = Double.parseDouble(rowValue[1]);
        if (value != 0.0D) {
          if (transposeOnRead) {
            m.set(j, row, value);
          } else
            m.set(row, j, value);
        }
      }
    }
    br.close();
    return m;
  }
  










  public static void writeMatrix(Matrix matrix, File output, Format format)
    throws IOException
  {
    if ((matrix.rows() == 0) || (matrix.columns() == 0))
      throw new IllegalArgumentException(
        "cannot write 0-dimensional matrix");
    switch (format)
    {
    case CLUTO_DENSE: 
      PrintWriter pw = new PrintWriter(output);
      for (int i = 0; i < matrix.rows(); i++) {
        StringBuffer sb = new StringBuffer(matrix.columns() * 5);
        for (int j = 0; j < matrix.columns(); j++) {
          sb.append(matrix.get(i, j)).append(" ");
        }
        pw.println(sb.toString());
      }
      pw.close();
      break;
    


    case MATLAB_SPARSE: 
    case SVDLIBC_SPARSE_BINARY: 
      PrintWriter pw = new PrintWriter(output);
      pw.println(matrix.rows() + " " + matrix.columns());
      for (int i = 0; i < matrix.rows(); i++) {
        StringBuffer sb = new StringBuffer(32);
        for (int j = 0; j < matrix.columns(); j++) {
          sb.append((float)matrix.get(i, j)).append(" ");
        }
        pw.println(sb.toString());
      }
      pw.close();
      break;
    

    case SVDLIBC_DENSE_TEXT: 
      DataOutputStream outStream = new DataOutputStream(
        new BufferedOutputStream(new FileOutputStream(output)));
      outStream.writeInt(matrix.rows());
      outStream.writeInt(matrix.columns());
      for (int i = 0; i < matrix.rows(); i++) {
        for (int j = 0; j < matrix.columns(); j++) {
          outStream.writeFloat((float)matrix.get(i, j));
        }
      }
      outStream.close();
      break;
    

    case SVDLIBC_SPARSE_TEXT: 
      PrintWriter pw = new PrintWriter(output);
      
      int nonZero = 0;
      int rows = matrix.rows();
      for (int i = 0; i < rows; i++) {
        DoubleVector v = matrix.getRowVector(i);
        if ((v instanceof SparseVector)) {
          nonZero += ((SparseVector)v).getNonZeroIndices().length;
        } else {
          for (int col = 0; col < v.length(); col++) {
            if (v.get(col) != 0.0D) {
              nonZero++;
            }
          }
        }
      }
      pw.println(matrix.rows() + " " + matrix.columns() + " " + nonZero);
      for (int row = 0; row < rows; row++) {
        StringBuilder sb = new StringBuilder(nonZero / rows);
        

        DoubleVector v = matrix.getRowVector(row);
        if ((v instanceof SparseVector)) {
          int[] nzIndices = ((SparseVector)v).getNonZeroIndices();
          for (int nz : nzIndices)
          {
            sb.append(nz + 1).append(" ").append(v.get(nz)).append(" ");
          }
        }
        else {
          for (int col = 0; col < v.length(); col++) {
            double d = v.get(col);
            if (d != 0.0D)
              sb.append(col + 1).append(" ").append(d).append(" ");
          }
        }
        pw.println(sb.toString());
      }
      pw.close();
      break;
    

    case DENSE_TEXT: 
      PrintWriter pw = new PrintWriter(output);
      

      int nonZero = 0;
      int[] nonZeroPerCol = new int[matrix.columns()];
      for (int i = 0; i < matrix.rows(); i++) {
        for (int j = 0; j < matrix.columns(); j++) {
          if (matrix.get(i, j) != 0.0D) {
            nonZero++;
            nonZeroPerCol[j] += 1;
          }
        }
      }
      



      pw.println(matrix.rows() + " " + matrix.columns() + " " + nonZero);
      for (int col = 0; col < matrix.columns(); col++) {
        pw.println(nonZeroPerCol[col]);
        if (nonZeroPerCol[col] > 0) {
          for (int row = 0; row < matrix.rows(); row++) {
            double val = matrix.get(row, col);
            if (val != 0.0D)
            {

              pw.println(row + " " + 
                Double.valueOf(val).floatValue());
            }
          }
        }
      }
      pw.close();
      break;
    

    case SVDLIBC_DENSE_BINARY: 
      DataOutputStream outStream = new DataOutputStream(
        new BufferedOutputStream(new FileOutputStream(output)));
      


      int nonZero = 0;
      int[] nonZeroPerCol = new int[matrix.columns()];
      for (int i = 0; i < matrix.rows(); i++) {
        for (int j = 0; j < matrix.columns(); j++) {
          if (matrix.get(i, j) != 0.0D) {
            nonZero++;
            nonZeroPerCol[j] += 1;
          }
        }
      }
      

      outStream.writeInt(matrix.rows());
      outStream.writeInt(matrix.columns());
      outStream.writeInt(nonZero);
      



      for (int col = 0; col < matrix.columns(); col++) {
        outStream.writeInt(nonZeroPerCol[col]);
        if (nonZeroPerCol[col] > 0) {
          for (int row = 0; row < matrix.rows(); row++) {
            double val = matrix.get(row, col);
            if (val != 0.0D)
            {

              outStream.writeInt(row);
              outStream.writeFloat((float)val);
            }
          }
        }
      }
      outStream.close();
      break;
    

    case CLUTO_SPARSE: 
      PrintWriter pw = new PrintWriter(output);
      





      int maxRowSeen = 0;
      int maxColSeen = 0;
      for (int i = 0; i < matrix.rows(); i++) {
        for (int j = 0; j < matrix.columns(); j++) {
          if (matrix.get(i, j) != 0.0D)
          {
            if (j > maxColSeen)
              maxColSeen = j;
            if (i > maxRowSeen)
              maxRowSeen = i;
            StringBuffer sb = new StringBuffer(32);
            

            sb.append(i + 1).append(" ").append(j + 1);
            sb.append(" ").append(matrix.get(i, j));
            pw.println(sb.toString());
          }
        }
      }
      if ((maxRowSeen + 1 != matrix.rows()) || 
        (maxColSeen + 1 != matrix.columns())) {
        pw.println(matrix.rows() + " " + matrix.columns() + " 0");
      }
      pw.close();
      break;
    

    default: 
      throw new UnsupportedOperationException(
        "writing to " + format + " is currently unsupported");
    }
  }
  
  @Deprecated
  public static void writeMatrixArray(double[][] matrix, File output) throws IOException
  {
    if ((matrix.length == 0) || (matrix[0].length == 0)) {
      throw new IllegalArgumentException("invalid matrix dimensions");
    }
    PrintWriter pw = new PrintWriter(output);
    int rows = matrix.length;
    int cols = matrix[0].length;
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        pw.print(matrix[row][col] + (col + 1 == cols ? "\n" : " "));
      }
    }
    pw.close();
  }
}
