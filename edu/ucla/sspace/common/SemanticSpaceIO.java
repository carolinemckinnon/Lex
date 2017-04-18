package edu.ucla.sspace.common;

import edu.ucla.sspace.util.SerializableUtil;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.IntegerVector;
import edu.ucla.sspace.vector.SparseDoubleVector;
import edu.ucla.sspace.vector.SparseVector;
import edu.ucla.sspace.vector.Vector;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;























































public class SemanticSpaceIO
{
  private static final Logger LOGGER = Logger.getLogger(SemanticSpaceIO.class.getName());
  

  private SemanticSpaceIO() {}
  


  public static enum SSpaceFormat
  {
    TEXT,  BINARY,  SPARSE_TEXT,  SPARSE_BINARY,  SERIALIZE;
  }
  















  static SSpaceFormat getFormat(File sspaceFile)
    throws IOException
  {
    DataInputStream dis = new DataInputStream(
      new BufferedInputStream(new FileInputStream(sspaceFile)));
    
    char header = dis.readChar();
    if (header != 's') {
      dis.close();
      return SSpaceFormat.SERIALIZE;
    }
    char encodedFormatCode = dis.readChar();
    int formatCode = encodedFormatCode - '0';
    dis.close();
    return (formatCode < 0) || (formatCode > SSpaceFormat.values().length) ? 
      SSpaceFormat.SERIALIZE : 
      SSpaceFormat.values()[formatCode];
  }
  














  static boolean fitsInMemory(long sspaceFileSize, SSpaceFormat format)
  {
    MemoryMXBean m = ManagementFactory.getMemoryMXBean();
    MemoryUsage mu = m.getHeapMemoryUsage();
    long available = mu.getMax() - mu.getUsed();
    boolean inMemory = false;
    
    switch (format)
    {

    case SERIALIZE: 
    case SPARSE_TEXT: 
    case TEXT: 
      inMemory = sspaceFileSize < available;
      break;
    

    case BINARY: 
      inMemory = (0.6666666666666666D * sspaceFileSize) < available;
      break;
    

    case SPARSE_BINARY: 
      inMemory = (0.75D * sspaceFileSize) < available;
      break;
    default: 
      if (!$assertionsDisabled) throw new AssertionError(format);
      break; }
    return inMemory;
  }
  










  public static SemanticSpace load(String sspaceFileName)
    throws IOException
  {
    return load(new File(sspaceFileName));
  }
  










  @Deprecated
  public static SemanticSpace load(String sspaceFileName, SSpaceFormat format)
    throws IOException
  {
    return load(new File(sspaceFileName), format);
  }
  











  public static SemanticSpace load(File sspaceFile)
    throws IOException
  {
    SSpaceFormat format = getFormat(sspaceFile);
    if (format == null)
      throw new IllegalArgumentException(
        "The file " + sspaceFile.getName() + " does not contain any " + 
        "internal format specification.");
    return loadInternal(sspaceFile, format, false);
  }
  










  @Deprecated
  public static SemanticSpace load(File sspaceFile, SSpaceFormat format)
    throws IOException
  {
    return loadInternal(sspaceFile, format, true);
  }
  





















  private static SemanticSpace loadInternal(File sspaceFile, SSpaceFormat format, boolean manuallySpecifiedFormat)
    throws IOException
  {
    if (format.equals(SSpaceFormat.SERIALIZE)) {
      LOGGER.fine("Loading serialized SemanticSpace from " + sspaceFile);
      return (SemanticSpace)SerializableUtil.load(sspaceFile);
    }
    



    if (fitsInMemory(sspaceFile.length(), format)) {
      LOGGER.fine(format + "-formatted .sspace file will fit into " + 
        "memory; creating StaticSemanticSpace");
      if (manuallySpecifiedFormat)
      {
        SemanticSpace s = 
          new StaticSemanticSpace(sspaceFile, format);
        return s;
      }
      
      return new StaticSemanticSpace(sspaceFile);
    }
    
    LOGGER.fine(format + "-formatted .sspace file will not fit into" + 
      "memory; creating OnDiskSemanticSpace");
    if (manuallySpecifiedFormat)
    {
      SemanticSpace s = 
        new OnDiskSemanticSpace(sspaceFile, format);
      return s;
    }
    
    return new OnDiskSemanticSpace(sspaceFile);
  }
  









  public static void save(SemanticSpace sspace, String outputFileName)
    throws IOException
  {
    save(sspace, new File(outputFileName), SSpaceFormat.TEXT);
  }
  







  public static void save(SemanticSpace sspace, File output)
    throws IOException
  {
    save(sspace, output, SSpaceFormat.TEXT);
  }
  







  public static void save(SemanticSpace sspace, File output, SSpaceFormat format)
    throws IOException
  {
    switch (format) {
    case BINARY: 
      writeText(sspace, output);
      break;
    case SERIALIZE: 
      writeBinary(sspace, output);
      break;
    case SPARSE_BINARY: 
      writeSparseText(sspace, output);
      break;
    case SPARSE_TEXT: 
      writeSparseBinary(sspace, output);
      break;
    case TEXT: 
      LOGGER.fine("Saving " + sspace + " to disk as serialized object");
      SerializableUtil.save(sspace, output);
      break;
    default: 
      if (!$assertionsDisabled) { throw new AssertionError(format);
      }
      




      break;
    }
    
  }
  




  static void writeHeader(OutputStream os, SSpaceFormat format)
    throws IOException
  {
    DataOutputStream dos = new DataOutputStream(os);
    dos.writeChar(115);
    dos.writeChar(48 + format.ordinal());
  }
  









  private static void writeText(SemanticSpace sspace, File output)
    throws IOException
  {
    OutputStream os = new FileOutputStream(output);
    PrintWriter pw = new PrintWriter(os);
    Set<String> words = sspace.getWords();
    
    int dimensions = 0;
    if (words.size() > 0) {
      dimensions = sspace.getVectorLength();
    }
    writeHeader(os, SSpaceFormat.TEXT);
    
    pw.println(words.size() + " " + dimensions);
    LOGGER.fine("saving text S-Space with " + words.size() + 
      " words with " + dimensions + "-dimensional vectors");
    



    for (String word : words) {
      StringBuilder sb = new StringBuilder(word);
      sb.append('|');
      Vector vector = sspace.getVector(word);
      int length = vector.length();
      
      if ((vector instanceof DoubleVector)) {
        DoubleVector dv = (DoubleVector)vector;
        for (int i = 0; i < length - 1; i++)
          sb.append(dv.get(i)).append(" ");
        sb.append(dv.get(length - 1));
      }
      else if ((vector instanceof IntegerVector)) {
        IntegerVector iv = (IntegerVector)vector;
        for (int i = 0; i < length - 1; i++)
          sb.append(iv.get(i)).append(" ");
        sb.append(iv.get(length - 1));
      }
      else {
        for (int i = 0; i < length - 1; i++)
          sb.append(vector.getValue(i).doubleValue()).append(" ");
        sb.append(vector.getValue(length - 1).doubleValue());
      }
      pw.println(sb);
    }
    pw.close();
  }
  









  private static void writeBinary(SemanticSpace sspace, File output)
    throws IOException
  {
    DataOutputStream dos = new DataOutputStream(
      new BufferedOutputStream(new FileOutputStream(output)));
    Set<String> words = sspace.getWords();
    
    int dimensions = 0;
    if (words.size() > 0) {
      dimensions = sspace.getVectorLength();
    }
    writeHeader(dos, SSpaceFormat.BINARY);
    
    dos.writeInt(words.size());
    dos.writeInt(dimensions);
    LOGGER.fine("saving binary S-Space with " + words.size() + 
      " words with " + dimensions + "-dimensional vectors");
    Vector v;
    int i; for (Iterator localIterator = words.iterator(); localIterator.hasNext(); 
        

        i < v.length())
    {
      String word = (String)localIterator.next();
      dos.writeUTF(word);
      v = sspace.getVector(word);
      i = 0; continue;
      dos.writeDouble(v.getValue(i).doubleValue());i++;
    }
    

    dos.close();
  }
  









  private static void writeSparseText(SemanticSpace sspace, File output)
    throws IOException
  {
    OutputStream os = new FileOutputStream(output);
    PrintWriter pw = new PrintWriter(os);
    Set<String> words = sspace.getWords();
    
    int dimensions = 0;
    if (words.size() > 0) {
      dimensions = sspace.getVectorLength();
    }
    
    writeHeader(os, SSpaceFormat.SPARSE_TEXT);
    
    pw.println(words.size() + " " + dimensions);
    
    LOGGER.fine("saving sparse-text S-Space with " + words.size() + 
      " words with " + dimensions + "-dimensional vectors");
    
    for (String word : words) {
      pw.print(word + "|");
      

      Vector vector = sspace.getVector(word);
      StringBuilder sb = null;
      if ((vector instanceof SparseVector)) {
        if ((vector instanceof DoubleVector)) {
          SparseDoubleVector sdv = (SparseDoubleVector)vector;
          int[] nz = sdv.getNonZeroIndices();
          sb = new StringBuilder(nz.length * 4);
          
          sb.append(nz[0]).append(",").append(sdv.get(nz[0]));
          for (int i = 1; i < nz.length; i++)
          {
            sb.append(",").append(nz[i]).append(",").append(sdv.getValue(nz[i]).doubleValue());
          }
        } else {
          SparseVector sv = (SparseVector)vector;
          int[] nz = sv.getNonZeroIndices();
          sb = new StringBuilder(nz.length * 4);
          
          sb.append(nz[0]).append(",")
            .append(sv.getValue(nz[0]).doubleValue());
          for (int i = 1; i < nz.length; i++)
          {
            sb.append(",").append(nz[i]).append(",").append(sv.getValue(nz[i]).doubleValue());
          }
        }
      }
      else {
        boolean first = true;
        sb = new StringBuilder(dimensions / 2);
        for (int i = 0; i < vector.length(); i++) {
          double d = vector.getValue(i).doubleValue();
          if (d != 0.0D) {
            if (first) {
              sb.append(i).append(",").append(d);
              first = false;
            }
            else {
              sb.append(",").append(i).append(",").append(d);
            }
          }
        }
      }
      pw.println(sb.toString());
    }
    pw.flush();
    pw.close();
  }
  










  private static void writeSparseBinary(SemanticSpace sspace, File output)
    throws IOException
  {
    DataOutputStream dos = new DataOutputStream(
      new BufferedOutputStream(new FileOutputStream(output)));
    Set<String> words = sspace.getWords();
    
    int dimensions = 0;
    if (words.size() > 0) {
      dimensions = sspace.getVectorLength();
    }
    
    writeHeader(dos, SSpaceFormat.SPARSE_BINARY);
    
    dos.writeInt(words.size());
    dos.writeInt(dimensions);
    
    LOGGER.fine("saving sparse-binary S-Space with " + words.size() + 
      " words with " + dimensions + "-dimensional vectors");
    
    for (String word : words) {
      dos.writeUTF(word);
      Vector vector = sspace.getVector(word);
      if ((vector instanceof SparseVector)) {
        if ((vector instanceof DoubleVector)) {
          SparseDoubleVector sdv = (SparseDoubleVector)vector;
          int[] nz = sdv.getNonZeroIndices();
          dos.writeInt(nz.length);
          for (int i : nz) {
            dos.writeInt(i);
            dos.writeDouble(sdv.get(i));
          }
        }
        else {
          SparseVector sv = (SparseVector)vector;
          int[] nz = sv.getNonZeroIndices();
          dos.writeInt(nz.length);
          for (int i : nz) {
            dos.writeInt(i);
            dos.writeDouble(sv.getValue(i).doubleValue());
          }
        }
      }
      else
      {
        int nonZero = 0;
        for (int i = 0; i < vector.length(); i++) {
          if (vector.getValue(i).doubleValue() != 0.0D)
            nonZero++;
        }
        dos.writeInt(nonZero);
        for (int i = 0; i < vector.length(); i++) {
          double d = vector.getValue(i).doubleValue();
          if (d != 0.0D) {
            dos.writeInt(i);
            dos.writeDouble(d);
          }
        }
      }
    }
    dos.close();
  }
}
