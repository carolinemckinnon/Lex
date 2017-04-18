package matlabcontrol;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;
import java.security.ProtectionDomain;


































































class MatlabClassLoaderHelper
{
  MatlabClassLoaderHelper() {}
  
  public static void configureClassLoading()
    throws MatlabConnectionException
  {
    if (!isOnSystemClassLoader())
    {
      addToSystemClassLoader();
    }
  }
  





  private static boolean isOnSystemClassLoader()
    throws MatlabConnectionException
  {
    URL matlabcontrolLocation = MatlabClassLoaderHelper.class.getProtectionDomain().getCodeSource().getLocation();
    
    try
    {
      URLClassLoader systemClassLoader = (URLClassLoader)ClassLoader.getSystemClassLoader();
      URL[] urls = systemClassLoader.getURLs();
      
      boolean onClasspath = false;
      for (URL url : urls)
      {
        if (url.equals(matlabcontrolLocation))
        {
          onClasspath = true;
          break;
        }
      }
      
      return onClasspath;
    }
    catch (ClassCastException e)
    {
      throw new MatlabConnectionException("Unable to determine if matlabcontrol is on the system class loader's classpath", e);
    }
  }
  





  private static void addToSystemClassLoader()
    throws MatlabConnectionException
  {
    URL matlabcontrolLocation = MatlabClassLoaderHelper.class.getProtectionDomain().getCodeSource().getLocation();
    
    try
    {
      URLClassLoader systemClassLoader = (URLClassLoader)ClassLoader.getSystemClassLoader();
      Class<URLClassLoader> classLoaderClass = URLClassLoader.class;
      
      Method method = classLoaderClass.getDeclaredMethod("addURL", new Class[] { URL.class });
      method.setAccessible(true);
      method.invoke(systemClassLoader, new Object[] { matlabcontrolLocation });
    }
    catch (Exception e)
    {
      throw new MatlabConnectionException("Unable to add matlabcontrol to system class loader", e);
    }
  }
}
