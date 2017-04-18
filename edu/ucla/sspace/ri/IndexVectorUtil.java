package edu.ucla.sspace.ri;

import edu.ucla.sspace.vector.TernaryVector;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;





































public class IndexVectorUtil
{
  private IndexVectorUtil() {}
  
  public static void save(Map<String, TernaryVector> wordToIndexVector, File output)
  {
    try
    {
      FileOutputStream fos = new FileOutputStream(output);
      ObjectOutputStream outStream = new ObjectOutputStream(fos);
      outStream.writeObject(wordToIndexVector);
      outStream.close();
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
  


  public static Map<String, TernaryVector> load(File indexVectorFile)
  {
    try
    {
      FileInputStream fis = new FileInputStream(indexVectorFile);
      ObjectInputStream inStream = new ObjectInputStream(fis);
      Map<String, TernaryVector> vectorMap = 
        (Map)inStream.readObject();
      inStream.close();
      return vectorMap;
    } catch (IOException ioe) {
      throw new IOError(ioe);
    } catch (ClassNotFoundException cnfe) {
      throw new Error(cnfe);
    }
  }
}
