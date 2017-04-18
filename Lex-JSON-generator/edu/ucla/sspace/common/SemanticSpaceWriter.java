package edu.ucla.sspace.common;

import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.SparseDoubleVector;
import edu.ucla.sspace.vector.SparseVector;
import edu.ucla.sspace.vector.Vector;
import edu.ucla.sspace.vector.VectorIO;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.Arrays;









































































public class SemanticSpaceWriter
{
  private final File outputFile;
  private final SemanticSpaceIO.SSpaceFormat format;
  private final DataOutputStream writer;
  private int vectorsSeen;
  private int vectorLength;
  
  public SemanticSpaceWriter(File sspaceFile, SemanticSpaceIO.SSpaceFormat format)
  {
    outputFile = sspaceFile;
    this.format = format;
    try {
      writer = new DataOutputStream(new FileOutputStream(sspaceFile));
      writeEmptyHeader();
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
  



  public void close()
    throws IOException
  {
    writer.close();
    
    RandomAccessFile raf = new RandomAccessFile(outputFile, "rw");
    raf.seek(4L);
    
    switch (format)
    {
    case BINARY: 
    case SPARSE_BINARY: 
      raf.writeChars(vectorsSeen + " " + vectorLength);
      break;
    case SERIALIZE: 
    case SPARSE_TEXT: 
      raf.writeInt(vectorsSeen);
      raf.writeInt(vectorLength);
      break;
    default: 
      if (!$assertionsDisabled) throw new AssertionError("unhandled s-space format");
      break; }
    raf.close();
  }
  


  public File getFile()
  {
    return outputFile;
  }
  

  public void write(String word, Vector vector)
    throws IOException
  {
    vectorLength = vector.length();
    vectorsSeen += 1;
    int i; double d; switch (format)
    {
    case SPARSE_BINARY: 
      PrintWriter pw = new PrintWriter(writer);
      pw.print(word + "|");
      
      StringBuilder sb = null;
      if ((vector instanceof SparseVector)) {
        if ((vector instanceof DoubleVector)) {
          SparseDoubleVector sdv = (SparseDoubleVector)vector;
          int[] nz = sdv.getNonZeroIndices();
          sb = new StringBuilder(nz.length * 4);
          
          sb.append(0).append(",").append(sdv.get(0));
          for (int i = 1; i < nz.length; i++) {
            sb.append(",").append(i).append(",").append(sdv.get(i));
          }
        } else {
          SparseVector sv = (SparseVector)vector;
          int[] nz = sv.getNonZeroIndices();
          sb = new StringBuilder(nz.length * 4);
          
          sb.append(0).append(",")
            .append(sv.getValue(0).doubleValue());
          for (int i = 1; i < nz.length; i++)
          {
            sb.append(",").append(i).append(",").append(sv.getValue(i).doubleValue());
          }
        }
      }
      else {
        boolean first = true;
        sb = new StringBuilder(vectorLength / 2);
        for (i = 0; i < vector.length(); i++) {
          d = vector.getValue(i).doubleValue();
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
      pw.flush();
      break;
    



    case BINARY: 
      PrintWriter pw = new PrintWriter(writer);
      pw.println(word + "|" + VectorIO.toString(vector));
      pw.flush();
      break;
    

    case SERIALIZE: 
      writer.writeUTF(word);
      for (int i = 0; i < vector.length(); i++) {
        writer.writeDouble(vector.getValue(i).doubleValue());
      }
      break;
    

    case SPARSE_TEXT: 
      writer.writeUTF(word);
      if ((vector instanceof SparseVector)) {
        if ((vector instanceof DoubleVector)) {
          SparseDoubleVector sdv = (SparseDoubleVector)vector;
          int[] nz = sdv.getNonZeroIndices();
          writer.writeInt(nz.length);
          for (int i : nz) {
            writer.writeInt(i);
            writer.writeDouble(sdv.get(i));
          }
        }
        else {
          SparseVector sv = (SparseVector)vector;
          int[] nz = sv.getNonZeroIndices();
          writer.writeInt(nz.length);
          for (int i : nz) {
            writer.writeInt(i);
            writer.writeDouble(sv.getValue(i).doubleValue());
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
        writer.writeInt(nonZero);
        for (int i = 0; i < vector.length(); i++) {
          double d = vector.getValue(i).doubleValue();
          if (d != 0.0D) {
            writer.writeInt(i);
            writer.writeDouble(d);
          }
        }
      }
      
      break;
    }
    
  }
  
  private void writeEmptyHeader()
    throws IOException
  {
    SemanticSpaceIO.writeHeader(writer, format);
    
    switch (format) {
    case BINARY: 
    case SPARSE_BINARY: 
      char[] blanks = new char[''];
      Arrays.fill(blanks, ' ');
      String blankStr = new String(blanks);
      PrintWriter pw = new PrintWriter(writer);
      pw.println(blankStr);
      pw.flush();
      break;
    case SERIALIZE: 
    case SPARSE_TEXT: 
      writer.writeInt(0);
      writer.writeInt(0);
      break;
    default: 
      if (!$assertionsDisabled) throw new AssertionError("unhandled s-space format");
      break;
    }
  }
}
