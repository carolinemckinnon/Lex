package edu.ucla.sspace.common;

import edu.ucla.sspace.vector.CompactSparseVector;
import edu.ucla.sspace.vector.DenseVector;
import edu.ucla.sspace.vector.Vector;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOError;
import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;


























































public class OnDiskSemanticSpace
  implements SemanticSpace
{
  private static final Logger LOGGER = Logger.getLogger(OnDiskSemanticSpace.class.getName());
  





  private Map<String, Long> termToOffset;
  





  private final boolean containsHeader;
  





  private int dimensions;
  





  private String spaceName;
  





  private RandomAccessBufferedReader textSSpace;
  





  private RandomAccessFile binarySSpace;
  




  private SemanticSpaceIO.SSpaceFormat format;
  





  public OnDiskSemanticSpace(String filename)
    throws IOException
  {
    this(new File(filename));
  }
  








  public OnDiskSemanticSpace(File file)
    throws IOException
  {
    containsHeader = true;
    SemanticSpaceIO.SSpaceFormat format = SemanticSpaceIO.getFormat(file);
    if (format == null)
      throw new Error("Unrecognzied format in file: " + 
        file.getName());
    loadOffsetsFromFormat(file, format);
  }
  











  @Deprecated
  public OnDiskSemanticSpace(File file, SemanticSpaceIO.SSpaceFormat format)
    throws IOException
  {
    containsHeader = false;
    loadOffsetsFromFormat(file, format);
  }
  










  private void loadOffsetsFromFormat(File file, SemanticSpaceIO.SSpaceFormat format)
    throws IOException
  {
    this.format = format;
    spaceName = file.getName();
    




    termToOffset = new LinkedHashMap();
    long start = System.currentTimeMillis();
    int dims = -1;
    RandomAccessFile raf = null;
    RandomAccessBufferedReader lnr = null;
    switch (format) {
    case BINARY: 
      lnr = new RandomAccessBufferedReader(file);
      dims = loadTextOffsets(lnr);
      break;
    case SERIALIZE: 
      raf = new RandomAccessFile(file, "r");
      dims = loadBinaryOffsets(raf);
      break;
    case SPARSE_BINARY: 
      lnr = new RandomAccessBufferedReader(file);
      dims = loadSparseTextOffsets(lnr);
      break;
    case SPARSE_TEXT: 
      raf = new RandomAccessFile(file, "r");
      dims = loadSparseBinaryOffsets(raf);
      break;
    default: 
      if (!$assertionsDisabled) throw new AssertionError(format);
      break; }
    if (LOGGER.isLoggable(Level.FINE)) {
      LOGGER.fine("loaded " + format + " .sspace file in " + (
        System.currentTimeMillis() - start) + "ms");
    }
    
    dimensions = dims;
    binarySSpace = raf;
    textSSpace = lnr;
  }
  








  private int loadTextOffsets(RandomAccessBufferedReader textSSpace)
    throws IOException
  {
    String line = textSSpace.readLine();
    if (line == null) {
      throw new IOError(new Throwable(
        "An empty file has been passed in"));
    }
    
    if (containsHeader)
      line = line.substring(4);
    String[] dimensionStrs = line.split("\\s");
    int dimensions = Integer.parseInt(dimensionStrs[1]);
    
    int row = 1;
    while ((line = textSSpace.readLine()) != null) {
      String[] termVectorPair = line.split("\\|");
      termToOffset.put(termVectorPair[0], Long.valueOf(row));
      row++;
    }
    
    return dimensions;
  }
  






  private double[] loadTextVector(String word)
    throws IOException
  {
    Long lineNumber = (Long)termToOffset.get(word);
    if (lineNumber == null) {
      return null;
    }
    
    textSSpace.moveToLine(lineNumber.intValue());
    String line = textSSpace.readLine();
    
    double[] row = new double[dimensions];
    String[] termVectorPair = line.split("\\|");
    String[] values = termVectorPair[1].split("\\s");
    
    if (values.length != dimensions) {
      throw new IOError(new Throwable(
        "improperly formated semantic space file"));
    }
    for (int c = 0; c < dimensions; c++) {
      double d = Double.parseDouble(values[c]);
      row[c] = d;
    }
    return row;
  }
  







  private int loadSparseTextOffsets(RandomAccessBufferedReader textSSpace)
    throws IOException
  {
    String line = textSSpace.readLine();
    if (line == null) {
      throw new IOError(new Throwable(
        "An empty file has been passed in"));
    }
    
    if (containsHeader) {
      line = line.substring(4);
      System.out.println(line);
    }
    
    String[] dimensions = line.split("\\s");
    int columns = Integer.parseInt(dimensions[1]);
    int rows = Integer.parseInt(dimensions[0]);
    int row = 1;
    
    while ((line = textSSpace.readLine()) != null) {
      String[] termVectorPair = line.split("\\|");
      termToOffset.put(termVectorPair[0], Long.valueOf(row));
      row++;
    }
    if (row - 1 != rows)
      throw new IOException(String.format(
        "Different number of rows than specified (%d): %d", new Object[] { Integer.valueOf(rows), Integer.valueOf(row) }));
    return columns;
  }
  






  private double[] loadSparseTextVector(String word)
    throws IOException
  {
    Long lineNumber = (Long)termToOffset.get(word);
    if (lineNumber == null) {
      return null;
    }
    
    textSSpace.moveToLine(lineNumber.intValue());
    String line = textSSpace.readLine();
    if (line == null)
      System.out.printf("%s -> null row %d%n", new Object[] { word, lineNumber });
    double[] row = new double[dimensions];
    
    String[] termVectorPair = line.split("\\|");
    String[] values = termVectorPair[1].split(",");
    

    for (int i = 0; i < values.length; i += 2) {
      int col = Integer.parseInt(values[i]);
      double val = Double.parseDouble(values[(i + 1)]);
      row[col] = val;
    }
    return row;
  }
  








  private int loadBinaryOffsets(RandomAccessFile binarySSpace)
    throws IOException
  {
    if (containsHeader) {
      binarySSpace.readInt();
    }
    int rows = binarySSpace.readInt();
    int cols = binarySSpace.readInt();
    
    for (int row = 0; row < rows; row++) {
      String word = binarySSpace.readUTF();
      termToOffset.put(word, Long.valueOf(binarySSpace.getFilePointer()));
      
      for (int col = 0; col < cols; col++) {
        binarySSpace.readDouble();
      }
    }
    return cols;
  }
  






  private double[] loadBinaryVector(String word)
    throws IOException
  {
    Long byteOffset = (Long)termToOffset.get(word);
    if (byteOffset == null) {
      return null;
    }
    binarySSpace.seek(byteOffset.longValue());
    
    double[] vector = new double[dimensions];
    
    for (int col = 0; col < dimensions; col++) {
      vector[col] = binarySSpace.readDouble();
    }
    
    return vector;
  }
  







  private int loadSparseBinaryOffsets(RandomAccessFile binarySSpace)
    throws IOException
  {
    if (containsHeader) {
      int i = binarySSpace.readInt();
    }
    int rows = binarySSpace.readInt();
    int cols = binarySSpace.readInt();
    
    for (long row = 0L; row < rows; row += 1L) {
      String word = binarySSpace.readUTF();
      termToOffset.put(word, Long.valueOf(binarySSpace.getFilePointer()));
      

      int nonZero = binarySSpace.readInt();
      for (int i = 0; i < nonZero; i++) {
        binarySSpace.readInt();
        binarySSpace.readDouble();
      }
    }
    return cols;
  }
  






  private double[] loadSparseBinaryVector(String word)
    throws IOException
  {
    Long byteOffset = (Long)termToOffset.get(word);
    if (byteOffset == null) {
      return null;
    }
    binarySSpace.seek(byteOffset.longValue());
    
    int nonZero = binarySSpace.readInt();
    double[] vector = new double[dimensions];
    for (int i = 0; i < nonZero; i++) {
      int col = binarySSpace.readInt();
      double val = binarySSpace.readDouble();
      vector[col] = val;
    }
    
    return vector;
  }
  


  public Set<String> getWords()
  {
    return Collections.unmodifiableSet(termToOffset.keySet());
  }
  




  public synchronized Vector getVector(String word)
  {
    try
    {
      switch (format) {
      case BINARY: 
        return new DenseVector(loadTextVector(word));
      case SERIALIZE: 
        return new DenseVector(loadBinaryVector(word));
      case SPARSE_BINARY: 
        return new CompactSparseVector(loadSparseTextVector(word));
      case SPARSE_TEXT: 
        return new CompactSparseVector(loadSparseBinaryVector(word));
      }
    }
    catch (IOException ioe)
    {
      throw new IOError(ioe);
    }
    return null;
  }
  


  public String getSpaceName()
  {
    return spaceName;
  }
  


  public int getVectorLength()
  {
    return dimensions;
  }
  


  public void processDocument(BufferedReader document)
  {
    throw new UnsupportedOperationException(
      "OnDiskSemanticSpace instances cannot be updated");
  }
  


  public void processSpace(Properties props)
  {
    throw new UnsupportedOperationException(
      "OnDiskSemanticSpace instances cannot be updated");
  }
  






  private static class RandomAccessBufferedReader
  {
    private final File backingFile;
    





    private BufferedReader current;
    





    private int currentLineNumber;
    





    public RandomAccessBufferedReader(File f)
      throws IOException
    {
      backingFile = f;
      reset();
    }
    





    public int getLineNumber()
    {
      return currentLineNumber;
    }
    






    public void moveToLine(int lineNum)
      throws IOException
    {
      if (lineNum < currentLineNumber) {
        reset();
      }
      for (int i = currentLineNumber; i < lineNum; i++) {
        current.readLine();
      }
      

      currentLineNumber = lineNum;
    }
    




    public String readLine()
      throws IOException
    {
      currentLineNumber += 1;
      return current.readLine();
    }
    


    private void reset()
      throws IOException
    {
      current = new BufferedReader(new FileReader(backingFile));
      currentLineNumber = 0;
    }
  }
}
