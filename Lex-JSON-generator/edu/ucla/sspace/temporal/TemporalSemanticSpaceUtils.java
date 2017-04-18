package edu.ucla.sspace.temporal;

import edu.ucla.sspace.vector.Vector;
import edu.ucla.sspace.vector.VectorIO;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.logging.Level;
import java.util.logging.Logger;














































public class TemporalSemanticSpaceUtils
{
  private static final Logger LOGGER = Logger.getLogger(TemporalSemanticSpaceUtils.class.getName());
  
  private TemporalSemanticSpaceUtils() {}
  

  public static enum TSSpaceFormat
  {
    TEXT,  BINARY,  SPARSE_TEXT,  SPARSE_BINARY;
  }
  











  public static TemporalSemanticSpace loadTemporalSemanticSpace(String sspaceFileName)
  {
    return loadTemporalSemanticSpace(new File(sspaceFileName), 
      TSSpaceFormat.TEXT);
  }
  







  public static TemporalSemanticSpace loadTemporalSemanticSpace(File sspaceFile)
  {
    return loadTemporalSemanticSpace(sspaceFile, TSSpaceFormat.TEXT);
  }
  








  public static TemporalSemanticSpace loadTemporalSemanticSpace(File sspaceFile, TSSpaceFormat format)
  {
    return new FileBasedTemporalSemanticSpace(sspaceFile, format);
  }
  





  public static void printTemporalSemanticSpace(TemporalSemanticSpace sspace, String outputFileName)
    throws IOException
  {
    printTemporalSemanticSpace(sspace, new File(outputFileName), TSSpaceFormat.TEXT);
  }
  





  public static void printTemporalSemanticSpace(TemporalSemanticSpace sspace, File output)
    throws IOException
  {
    printTemporalSemanticSpace(sspace, output, TSSpaceFormat.TEXT);
  }
  







  public static void printTemporalSemanticSpace(TemporalSemanticSpace sspace, File output, TSSpaceFormat format)
    throws IOException
  {
    switch (format) {
    case BINARY: 
      printText(sspace, output);
      break;
    case SPARSE_TEXT: 
      printSparseText(sspace, output);
      break;
    case SPARSE_BINARY: 
      printBinary(sspace, output);
      break;
    case TEXT: 
      printSparseBinary(sspace, output);
      break;
    default: 
      throw new IllegalArgumentException("Unknown format type: " + format);
    }
  }
  
  private static void printText(TemporalSemanticSpace sspace, File output)
    throws IOException
  {
    PrintWriter pw = new PrintWriter(output);
    
    Set<String> words = sspace.getWords();
    
    int dimensions = 0;
    if (words.size() > 0) {
      dimensions = sspace.getVectorLength();
    }
    
    int size = words.size();
    
    pw.println(size + " " + dimensions);
    
    int wordCount = 0;
    for (String word : words) {
      pw.print(word + "|");
      if (LOGGER.isLoggable(Level.INFO)) {
        LOGGER.info(String.format("serializing text %d/%d: %s", new Object[] {
          Integer.valueOf(wordCount++), Integer.valueOf(size), word }));
      }
      for (Iterator localIterator2 = sspace.getTimeSteps(word).iterator(); localIterator2.hasNext();) { long timestep = ((Long)localIterator2.next()).longValue();
        Vector timeSlice = 
          sspace.getVectorBetween(word, timestep, timestep + 1L);
        if (timeSlice != null) {
          pw.print(timestep + " " + 
            VectorIO.toString(timeSlice) + "|");
        }
      }
      pw.println("");
    }
    pw.close();
  }
  




  private static void printSparseText(TemporalSemanticSpace sspace, File output)
    throws IOException
  {
    PrintWriter pw = new PrintWriter(output);
    
    Set<String> words = sspace.getWords();
    
    int dimensions = 0;
    if (words.size() > 0) {
      dimensions = sspace.getVectorLength();
    }
    
    int size = words.size();
    
    pw.println(size + " " + dimensions);
    
    int wordCount = 0;
    for (String word : words) {
      pw.print(word + "|");
      if (LOGGER.isLoggable(Level.INFO)) {
        LOGGER.info(String.format("serializing sparse text %d/%d: %s", new Object[] {
          Integer.valueOf(wordCount++), Integer.valueOf(size), word }));
      }
      
      SortedSet<Long> timeSteps = sspace.getTimeSteps(word);
      
      for (Iterator localIterator2 = timeSteps.iterator(); localIterator2.hasNext();) { long timestep = ((Long)localIterator2.next()).longValue();
        Vector timeSlice = 
          sspace.getVectorBetween(word, timestep, timestep + 1L);
        if (timeSlice != null)
        {
          int nonZero = 0;
          for (int i = 0; i < timeSlice.length(); i++) {
            if (timeSlice.getValue(i).doubleValue() != 0.0D) {
              nonZero++;
            }
          }
          
          pw.print(timestep + " " + nonZero + "%");
          StringBuilder sb = new StringBuilder(nonZero * 4);
          for (int i = 0; i < timeSlice.length(); i++) {
            double d = timeSlice.getValue(i).doubleValue();
            if (d != 0.0D) {
              sb.append(i).append(",").append(d);
            }
            if (i + 1 < timeSlice.length()) {
              sb.append(",");
            }
          }
          pw.print(sb.toString() + "|");
        }
      }
      pw.println("");
    }
    pw.close();
  }
  



  private static void printBinary(TemporalSemanticSpace sspace, File output)
    throws IOException
  {
    DataOutputStream dos = 
      new DataOutputStream(new FileOutputStream(output));
    
    Set<String> words = sspace.getWords();
    
    int dimensions = 0;
    if (words.size() > 0) {
      dimensions = sspace.getVectorLength();
    }
    
    int size = words.size();
    
    dos.writeInt(size);
    dos.writeInt(dimensions);
    
    int wordCount = 0;
    Iterator localIterator2; for (Iterator localIterator1 = words.iterator(); localIterator1.hasNext(); 
        





        localIterator2.hasNext())
    {
      String word = (String)localIterator1.next();
      dos.writeUTF(word);
      if (LOGGER.isLoggable(Level.INFO)) {
        LOGGER.info(String.format("serializing binary %d/%d: %s", new Object[] {
          Integer.valueOf(wordCount++), Integer.valueOf(size), word }));
      }
      
      localIterator2 = sspace.getTimeSteps(word).iterator(); continue;long timestep = ((Long)localIterator2.next()).longValue();
      Vector timeSlice = 
        sspace.getVectorBetween(word, timestep, timestep + 1L);
      if (timeSlice != null) {
        dos.writeLong(timestep);
        for (int i = 0; i < timeSlice.length(); i++) {
          dos.writeDouble(timeSlice.getValue(i).doubleValue());
        }
      }
    }
    
    dos.close();
  }
  

  private static void printSparseBinary(TemporalSemanticSpace sspace, File output)
    throws IOException
  {
    DataOutputStream dos = 
      new DataOutputStream(new FileOutputStream(output));
    
    Set<String> words = sspace.getWords();
    
    int dimensions = 0;
    if (words.size() > 0) {
      dimensions = sspace.getVectorLength();
    }
    
    int size = words.size();
    
    dos.writeInt(size);
    dos.writeInt(dimensions);
    
    int wordCount = 0;
    Iterator localIterator2; for (Iterator localIterator1 = words.iterator(); localIterator1.hasNext(); 
        










        localIterator2.hasNext())
    {
      String word = (String)localIterator1.next();
      dos.writeUTF(word);
      if (LOGGER.isLoggable(Level.INFO)) {
        LOGGER.info(String.format("serializing sparse binary %d/%d: %s", new Object[] {
          Integer.valueOf(wordCount++), Integer.valueOf(size), word }));
      }
      
      SortedSet<Long> timeSteps = sspace.getTimeSteps(word);
      

      dos.writeInt(timeSteps.size());
      
      localIterator2 = timeSteps.iterator(); continue;long timestep = ((Long)localIterator2.next()).longValue();
      Vector timeSlice = 
        sspace.getVectorBetween(word, timestep, timestep + 1L);
      if (timeSlice != null)
      {
        int nonZero = 0;
        for (int i = 0; i < timeSlice.length(); i++) {
          if (timeSlice.getValue(i).doubleValue() != 0.0D) {
            nonZero++;
          }
        }
        
        dos.writeLong(timestep);
        dos.writeInt(nonZero);
        for (int i = 0; i < timeSlice.length(); i++) {
          double d = timeSlice.getValue(i).doubleValue();
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
