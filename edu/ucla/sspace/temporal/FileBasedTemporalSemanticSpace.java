package edu.ucla.sspace.temporal;

import edu.ucla.sspace.util.IntegerMap;
import edu.ucla.sspace.vector.DenseVector;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.Vector;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOError;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.logging.Logger;






































public class FileBasedTemporalSemanticSpace
  implements TemporalSemanticSpace
{
  private static final Logger LOGGER = Logger.getLogger(FileBasedTemporalSemanticSpace.class.getName());
  


  private final Map<String, SemanticVector> wordToMeaning;
  


  private final String spaceName;
  


  private int dimensions;
  


  private long startTime;
  


  private long endTime;
  



  public FileBasedTemporalSemanticSpace(String filename)
  {
    this(new File(filename), TemporalSemanticSpaceUtils.TSSpaceFormat.TEXT);
  }
  






  public FileBasedTemporalSemanticSpace(File file)
  {
    this(file, TemporalSemanticSpaceUtils.TSSpaceFormat.TEXT);
  }
  







  public FileBasedTemporalSemanticSpace(String filename, TemporalSemanticSpaceUtils.TSSpaceFormat format)
  {
    this(new File(filename), format);
  }
  







  public FileBasedTemporalSemanticSpace(File file, TemporalSemanticSpaceUtils.TSSpaceFormat format)
  {
    startTime = Long.MAX_VALUE;
    endTime = Long.MIN_VALUE;
    
    Map<String, SemanticVector> m = null;
    try {
      switch (format) {
      case BINARY: 
        m = loadText(file);
        break;
      case SPARSE_TEXT: 
        m = loadSparseText(file);
        break;
      case SPARSE_BINARY: 
        m = loadBinary(file);
        break;
      case TEXT: 
        m = loadSparseBinary(file);
        break;
      default: 
        throw new IllegalArgumentException(
          "unhandled format type " + format);
      }
    }
    catch (IOException ioe) {
      throw new IOError(ioe);
    }
    
    wordToMeaning = m;
    spaceName = file.getName();
  }
  





  private Map<String, SemanticVector> loadText(File sspaceFile)
    throws IOException
  {
    LOGGER.info("loading text TSS from " + sspaceFile);
    
    BufferedReader br = new BufferedReader(new FileReader(sspaceFile));
    String[] header = br.readLine().split("\\s+");
    int words = Integer.parseInt(header[0]);
    dimensions = Integer.parseInt(header[1]);
    
    Map<String, SemanticVector> wordToSemantics = 
      new HashMap(words, 2.0F);
    

    for (String line = null; (line = br.readLine()) != null;) {
      String[] wordAndSemantics = line.split("\\|");
      String word = wordAndSemantics[0];
      SemanticVector semantics = new SemanticVector(dimensions);
      
      LOGGER.info("loading " + wordAndSemantics.length + 
        " timesteps for word " + word);
      
      for (int i = 1; i < wordAndSemantics.length; i++) {
        String[] timeStepAndValues = wordAndSemantics[i].split(" ");
        long timeStep = Long.parseLong(timeStepAndValues[0]);
        updateTimeRange(timeStep);
        




        Map<Integer, Double> sparseArray = new IntegerMap();
        
        for (int j = 1; j < timeStepAndValues.length; j++) {
          sparseArray.put(Integer.valueOf(j - 1), 
            Double.valueOf(timeStepAndValues[j]));
        }
        semantics.setSemantics(timeStep, sparseArray);
      }
      wordToSemantics.put(word, semantics);
    }
    
    return wordToSemantics;
  }
  







  private Map<String, SemanticVector> loadSparseText(File sspaceFile)
    throws IOException
  {
    LOGGER.info("loading sparse text TSS from " + sspaceFile);
    
    BufferedReader br = new BufferedReader(new FileReader(sspaceFile));
    String[] header = br.readLine().split("\\s+");
    int words = Integer.parseInt(header[0]);
    dimensions = Integer.parseInt(header[1]);
    
    Map<String, SemanticVector> wordToSemantics = 
      new HashMap(words, 2.0F);
    
    for (int wordIndex = 0; wordIndex < words; wordIndex++) {
      String[] wordAndSemantics = br.readLine().split("\\|");
      String word = wordAndSemantics[0];
      SemanticVector semantics = new SemanticVector(dimensions);
      wordToSemantics.put(word, semantics);
      
      LOGGER.info("loading " + wordAndSemantics.length + 
        " timesteps for word " + word);
      

      for (int tsIndx = 1; tsIndx < wordAndSemantics.length; tsIndx++) {
        String[] tsAndVec = wordAndSemantics[tsIndx].split("%");
        String[] tsAndNonZero = tsAndVec[0].split(" ");
        long timeStep = Long.parseLong(tsAndNonZero[0]);
        updateTimeRange(timeStep);
        int nonZero = Integer.parseInt(tsAndNonZero[1]);
        String[] vecElements = tsAndVec[1].split(",");
        
        Map<Integer, Double> sparseArr = new IntegerMap();
        
        for (int i = 0; i < vecElements.length; i += 2) {
          Integer index = Integer.valueOf(vecElements[i]);
          Double localDouble = Double.valueOf(vecElements[(i + 1)]);
        }
        semantics.setSemantics(timeStep, sparseArr);
      }
    }
    
    return wordToSemantics;
  }
  






  private Map<String, SemanticVector> loadBinary(File sspaceFile)
    throws IOException
  {
    LOGGER.info("loading binary TSS from " + sspaceFile);
    
    DataInputStream dis = 
      new DataInputStream(new FileInputStream(sspaceFile));
    int words = dis.readInt();
    dimensions = dis.readInt();
    


    Map<String, SemanticVector> wordToSemantics = 
      new HashMap(words, 2.0F);
    
    for (int wordIndex = 0; wordIndex < words; wordIndex++) {
      String word = dis.readUTF();
      int timeSteps = dis.readInt();
      SemanticVector vector = new SemanticVector(dimensions);
      wordToSemantics.put(word, vector);
      
      LOGGER.info("loading " + timeSteps + 
        " timesteps for word " + word);
      




      Map<Integer, Double> semantics = new IntegerMap();
      

      for (int tsIndex = 0; tsIndex < timeSteps; tsIndex++) {
        long timeStep = dis.readLong();
        updateTimeRange(timeStep);
        
        for (int i = 0; i < dimensions; i++) {
          int index = dis.readInt();
          double val = dis.readDouble();
          semantics.put(Integer.valueOf(index), Double.valueOf(val));
        }
        
        vector.setSemantics(timeStep, semantics);
      }
    }
    return wordToSemantics;
  }
  






  private Map<String, SemanticVector> loadSparseBinary(File sspaceFile)
    throws IOException
  {
    LOGGER.info("loading text TSS from " + sspaceFile);
    
    DataInputStream dis = 
      new DataInputStream(new FileInputStream(sspaceFile));
    int words = dis.readInt();
    dimensions = dis.readInt();
    


    Map<String, SemanticVector> wordToSemantics = 
      new HashMap(words, 2.0F);
    
    for (int wordIndex = 0; wordIndex < words; wordIndex++) {
      String word = dis.readUTF();
      int timeSteps = dis.readInt();
      SemanticVector vector = new SemanticVector(dimensions);
      wordToSemantics.put(word, vector);
      
      LOGGER.info("loading " + timeSteps + 
        " timesteps for word " + word);
      

      for (int tsIndex = 0; tsIndex < timeSteps; tsIndex++) {
        long timeStep = dis.readLong();
        updateTimeRange(timeStep);
        int nonZero = dis.readInt();
        

        Map<Integer, Double> semantics = new IntegerMap();
        for (int i = 0; i < nonZero; i++) {
          int index = dis.readInt();
          double val = dis.readDouble();
          semantics.put(Integer.valueOf(index), Double.valueOf(val));
        }
        
        vector.setSemantics(timeStep, semantics);
      }
    }
    
    return wordToSemantics;
  }
  



  private void updateTimeRange(long timestamp)
  {
    if (timestamp < startTime) {
      startTime = timestamp;
    }
    if (timestamp > endTime) {
      endTime = timestamp;
    }
  }
  









  public Long startTime()
  {
    return Long.valueOf(startTime);
  }
  


  public Long endTime()
  {
    return Long.valueOf(endTime);
  }
  



  public Vector getVector(String word)
  {
    SemanticVector v = (SemanticVector)wordToMeaning.get(word);
    return v == null ? null : v.getVector();
  }
  


  public Vector getVectorAfter(String word, long startTime)
  {
    SemanticVector v = (SemanticVector)wordToMeaning.get(word);
    return v == null ? null : v.getVectorAfter(startTime);
  }
  


  public Vector getVectorBefore(String word, long endTime)
  {
    SemanticVector v = (SemanticVector)wordToMeaning.get(word);
    return v == null ? null : v.getVectorBefore(endTime);
  }
  


  public Vector getVectorBetween(String word, long start, long endTime)
  {
    SemanticVector v = (SemanticVector)wordToMeaning.get(word);
    return v == null ? null : v.getVectorBetween(start, endTime);
  }
  


  public SortedSet<Long> getTimeSteps(String word)
  {
    SemanticVector v = (SemanticVector)wordToMeaning.get(word);
    return v == null ? null : v.getTimeSteps();
  }
  


  public Set<String> getWords()
  {
    return Collections.unmodifiableSet(wordToMeaning.keySet());
  }
  


  public String getSpaceName()
  {
    return spaceName;
  }
  


  public int getVectorLength()
  {
    return dimensions;
  }
  



  public void processDocument(BufferedReader document) {}
  



  public void processDocument(BufferedReader document, long time) {}
  


  public void processSpace(Properties props) {}
  


  private static class SemanticVector
  {
    private final NavigableMap<Long, Map<Integer, Double>> timeStampToSemantics;
    

    private final int dimensions;
    


    public SemanticVector(int dimensions)
    {
      this.dimensions = dimensions;
      timeStampToSemantics = new TreeMap();
    }
    






    public void setSemantics(long timestamp, double[] semantics)
    {
      Long t = Long.valueOf(timestamp);
      Map<Integer, Double> sparseArr = new IntegerMap();
      for (int i = 0; i < semantics.length; i++) {
        double d = semantics[i];
        if (d != 0.0D) {
          sparseArr.put(Integer.valueOf(i), Double.valueOf(d));
        }
      }
      timeStampToSemantics.put(t, sparseArr);
    }
    







    public void setSemantics(long timestamp, Map<Integer, Double> semantics)
    {
      timeStampToSemantics.put(Long.valueOf(timestamp), semantics);
    }
    




    private DoubleVector computeSemantics(Map<Long, Map<Integer, Double>> timespan)
    {
      double[] semantics = new double[dimensions];
      Iterator localIterator2; for (Iterator localIterator1 = timespan.values().iterator(); localIterator1.hasNext(); 
          
          localIterator2.hasNext())
      {
        Map<Integer, Double> vectors = (Map)localIterator1.next();
        
        localIterator2 = vectors.entrySet().iterator(); continue;Map.Entry<Integer, Double> e = (Map.Entry)localIterator2.next();
        semantics[((Integer)e.getKey()).intValue()] += ((Double)e.getValue()).intValue();
      }
      
      return new DenseVector(semantics);
    }
    



    public long getEndTime()
    {
      return ((Long)timeStampToSemantics.lastKey()).longValue();
    }
    



    public long getStartTime()
    {
      return ((Long)timeStampToSemantics.firstKey()).longValue();
    }
    


    public SortedSet<Long> getTimeSteps()
    {
      return Collections.unmodifiableSortedSet(
        timeStampToSemantics.navigableKeySet());
    }
    



    public DoubleVector getVector()
    {
      return computeSemantics(timeStampToSemantics);
    }
    



    public Vector getVectorAfter(long start)
    {
      SortedMap<Long, Map<Integer, Double>> timespan = 
        timeStampToSemantics.tailMap(Long.valueOf(start));
      return computeSemantics(timespan);
    }
    



    public Vector getVectorBefore(long end)
    {
      SortedMap<Long, Map<Integer, Double>> timespan = 
        timeStampToSemantics.headMap(Long.valueOf(end));
      return computeSemantics(timespan);
    }
    




    public Vector getVectorBetween(long start, long end)
    {
      SortedMap<Long, Map<Integer, Double>> timespan = 
        timeStampToSemantics.subMap(Long.valueOf(start), Long.valueOf(end));
      return computeSemantics(timespan);
    }
  }
}
