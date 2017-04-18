package edu.ucla.sspace.common;

import edu.ucla.sspace.matrix.ArrayMatrix;
import edu.ucla.sspace.matrix.Matrices;
import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.vector.CompactSparseVector;
import edu.ucla.sspace.vector.Vector;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;



















































public class StaticSemanticSpace
  implements SemanticSpace
{
  private static final Logger LOGGER = Logger.getLogger(StaticSemanticSpace.class.getName());
  




  private Matrix wordSpace;
  




  private Map<String, Integer> termToIndex;
  




  private String spaceName;
  





  public StaticSemanticSpace(String filename)
    throws IOException
  {
    this(new File(filename));
  }
  







  public StaticSemanticSpace(File file)
    throws IOException
  {
    spaceName = file.getName();
    SemanticSpaceIO.SSpaceFormat format = SemanticSpaceIO.getFormat(file);
    if (format == null)
      throw new Error("Unrecognzied format in file: " + 
        file.getName());
    DataInputStream dis = new DataInputStream(
      new BufferedInputStream(new FileInputStream(file)));
    


    dis.readInt();
    loadFromFormat(dis, format);
  }
  











  @Deprecated
  public StaticSemanticSpace(File file, SemanticSpaceIO.SSpaceFormat format)
    throws IOException
  {
    loadFromFormat(new BufferedInputStream(
      new FileInputStream(file)), format);
    spaceName = file.getName();
  }
  










  private void loadFromFormat(InputStream is, SemanticSpaceIO.SSpaceFormat format)
    throws IOException
  {
    termToIndex = new LinkedHashMap();
    Matrix m = null;
    long start = System.currentTimeMillis();
    
    switch (format) {
    case BINARY: 
      m = Matrices.synchronizedMatrix(loadText(is));
      break;
    case SERIALIZE: 
      m = Matrices.synchronizedMatrix(loadBinary(is));
      break;
    




    case SPARSE_BINARY: 
      m = loadSparseText(is);
      break;
    case SPARSE_TEXT: 
      m = loadSparseBinary(is);
    }
    
    
    if (LOGGER.isLoggable(Level.FINE)) {
      LOGGER.fine("loaded " + format + " .sspace file in " + (
        System.currentTimeMillis() - start) + "ms");
    }
    wordSpace = m;
  }
  





  private Matrix loadText(InputStream fileStream)
    throws IOException
  {
    Matrix matrix = null;
    
    BufferedReader br = 
      new BufferedReader(new InputStreamReader(fileStream));
    String line = br.readLine();
    if (line == null) {
      throw new IOException("Empty .sspace file");
    }
    String[] dimensions = line.split("\\s");
    int rows = Integer.parseInt(dimensions[0]);
    int columns = Integer.parseInt(dimensions[1]);
    int index = 0;
    

    double[] row = new double[columns];
    
    matrix = new ArrayMatrix(rows, columns);
    
    while ((line = br.readLine()) != null) {
      if (index >= rows)
        throw new IOException("More rows than specified");
      String[] termVectorPair = line.split("\\|");
      String[] values = termVectorPair[1].split("\\s");
      termToIndex.put(termVectorPair[0], Integer.valueOf(index));
      if (values.length != columns) {
        throw new IOException(
          "improperly formated semantic space file");
      }
      for (int c = 0; c < columns; c++) {
        double d = Double.parseDouble(values[c]);
        row[c] = d;
      }
      
      matrix.setRow(index, row);
      index++;
    }
    if (index != rows)
      throw new IOException(String.format(
        "Expected %d rows; saw %d", new Object[] { Integer.valueOf(rows), Integer.valueOf(index) }));
    return matrix;
  }
  





  private Matrix loadSparseText(InputStream fileStream)
    throws IOException
  {
    Matrix matrix = null;
    
    BufferedReader br = 
      new BufferedReader(new InputStreamReader(fileStream));
    String line = br.readLine();
    if (line == null)
      throw new IOError(new Throwable(
        "An empty file has been passed in"));
    String[] dimensions = line.split("\\s");
    int rows = Integer.parseInt(dimensions[0]);
    int columns = Integer.parseInt(dimensions[1]);
    
    int row = 0;
    

    matrix = Matrices.create(rows, columns, false);
    while ((line = br.readLine()) != null) {
      String[] termVectorPair = line.split("\\|");
      String[] values = termVectorPair[1].split(",");
      termToIndex.put(termVectorPair[0], Integer.valueOf(row));
      

      for (int i = 0; i < values.length; i += 2) {
        int col = Integer.parseInt(values[i]);
        double val = Double.parseDouble(values[(i + 1)]);
        matrix.set(row, col, val);
      }
      row++;
    }
    return matrix;
  }
  





  private Matrix loadBinary(InputStream fileStream)
    throws IOException
  {
    DataInputStream dis = new DataInputStream(fileStream);
    int rows = dis.readInt();
    int cols = dis.readInt();
    

    Matrix m = new ArrayMatrix(rows, cols);
    double[] d = new double[cols];
    for (int row = 0; row < rows; row++) {
      String word = dis.readUTF();
      termToIndex.put(word, Integer.valueOf(row));
      
      for (int col = 0; col < cols; col++) {
        d[col] = dis.readDouble();
      }
      m.setRow(row, d);
    }
    return m;
  }
  





  private Matrix loadSparseBinary(InputStream fileStream)
    throws IOException
  {
    DataInputStream dis = new DataInputStream(fileStream);
    int rows = dis.readInt();
    int cols = dis.readInt();
    


    CompactSparseVector[] rowVectors = new CompactSparseVector[rows];
    
    for (int row = 0; row < rows; row++) {
      String word = dis.readUTF();
      termToIndex.put(word, Integer.valueOf(row));
      
      int nonZero = dis.readInt();
      int[] indices = new int[nonZero];
      double[] values = new double[nonZero];
      for (int i = 0; i < nonZero; i++) {
        int nz = dis.readInt();
        double val = dis.readDouble();
        indices[i] = nz;
        values[i] = val;
      }
      rowVectors[row] = new CompactSparseVector(indices, values, cols);
    }
    return Matrices.asSparseMatrix(Arrays.asList(rowVectors));
  }
  


  public Set<String> getWords()
  {
    return Collections.unmodifiableSet(termToIndex.keySet());
  }
  


  public Vector getVector(String term)
  {
    Integer index = (Integer)termToIndex.get(term);
    return index == null ? 
      null : 
      wordSpace.getRowVector(index.intValue());
  }
  


  public String getSpaceName()
  {
    return spaceName;
  }
  


  public int getVectorLength()
  {
    return wordSpace.columns();
  }
  




  public void processDocument(BufferedReader document)
  {
    throw new UnsupportedOperationException(
      "StaticSemanticSpace instances cannot be updated");
  }
  




  public void processSpace(Properties props)
  {
    throw new UnsupportedOperationException(
      "StaticSemanticSpace instances cannot be updated");
  }
}
