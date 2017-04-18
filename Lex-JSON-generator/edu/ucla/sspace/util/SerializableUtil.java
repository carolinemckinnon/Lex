package edu.ucla.sspace.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;








































public class SerializableUtil
{
  private SerializableUtil() {}
  
  public static void save(Object o, String file)
  {
    save(o, new File(file));
  }
  




  public static void save(Object o, File file)
  {
    try
    {
      FileOutputStream fos = new FileOutputStream(file);
      ObjectOutputStream outStream = 
        new ObjectOutputStream(new BufferedOutputStream(fos));
      outStream.writeObject(o);
      outStream.close();
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
  






  public static void save(Object o, OutputStream stream)
  {
    try
    {
      ObjectOutputStream outStream = 
        (stream instanceof ObjectOutputStream) ? 
        (ObjectOutputStream)stream : 
        new ObjectOutputStream(stream);
      outStream.writeObject(o);
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
  




  public static byte[] save(Object o)
  {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    save(o, baos);
    return baos.toByteArray();
  }
  






  public static <T> T load(File file, Class<T> type)
  {
    try
    {
      FileInputStream fis = new FileInputStream(file);
      ObjectInputStream inStream = 
        new ObjectInputStream(new BufferedInputStream(fis));
      T object = type.cast(inStream.readObject());
      inStream.close();
      return object;
    } catch (IOException ioe) {
      throw new IOError(ioe);
    } catch (ClassNotFoundException cnfe) {
      throw new IOError(cnfe);
    }
  }
  







  public static <T> T load(String fileName)
  {
    return load(new File(fileName));
  }
  






  public static <T> T load(File file)
  {
    try
    {
      FileInputStream fis = new FileInputStream(file);
      ObjectInputStream inStream = 
        new ObjectInputStream(new BufferedInputStream(fis));
      T object = inStream.readObject();
      inStream.close();
      return object;
    } catch (IOException ioe) {
      throw new IOError(ioe);
    } catch (ClassNotFoundException cnfe) {
      throw new IOError(cnfe);
    }
  }
  








  public static <T> T load(InputStream stream)
  {
    try
    {
      ObjectInputStream inStream = (stream instanceof ObjectInputStream) ? 
        (ObjectInputStream)stream : 
        new ObjectInputStream(stream);
      return inStream.readObject();
    }
    catch (IOException ioe) {
      throw new IOError(ioe);
    } catch (ClassNotFoundException cnfe) {
      throw new IOError(cnfe);
    }
  }
}
