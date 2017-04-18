package matlabcontrol;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;



































class Configuration
{
  private Configuration() {}
  
  static boolean isOSX()
  {
    return getOperatingSystem().startsWith("Mac OS X");
  }
  





  static boolean isWindows()
  {
    return getOperatingSystem().startsWith("Windows");
  }
  





  static boolean isLinux()
  {
    return getOperatingSystem().startsWith("Linux");
  }
  





  static String getOperatingSystem()
  {
    return System.getProperty("os.name");
  }
  






  static String getMatlabLocation()
    throws MatlabConnectionException
  {
    String matlabLoc;
    





    if (isOSX())
    {
      matlabLoc = getOSXMatlabLocation();
    } else {
      String matlabLoc;
      if ((isWindows()) || (isLinux()))
      {
        matlabLoc = "matlab";

      }
      else
      {
        throw new MatlabConnectionException("MATLAB's location or alias can only be determined for OS X, Windows, & Linux. For this operating system the location or alias must be specified explicitly.");
      }
    }
    
    String matlabLoc;
    return matlabLoc;
  }
  








  private static String getOSXMatlabLocation()
    throws MatlabConnectionException
  {
    String matlabName = null;
    for (String fileName : new File("/Applications/").list())
    {
      if (fileName.startsWith("MATLAB"))
      {
        matlabName = fileName;
      }
    }
    

    if (matlabName == null)
    {
      throw new MatlabConnectionException("No installation of MATLAB on OS X can be found");
    }
    

    String matlabLocation = "/Applications/" + matlabName + "/bin/matlab";
    

    if (!new File(matlabLocation).exists())
    {
      throw new MatlabConnectionException("An installation of MATLAB on OS X was found but the main executable file was not found in the anticipated location: " + matlabLocation);
    }
    

    return matlabLocation;
  }
  






  static String getClassPathAsRMICodebase()
    throws MatlabConnectionException
  {
    try
    {
      StringBuilder codebaseBuilder = new StringBuilder();
      String[] paths = System.getProperty("java.class.path", "").split(File.pathSeparator);
      
      for (int i = 0; i < paths.length; i++)
      {
        codebaseBuilder.append("file://");
        codebaseBuilder.append(new File(paths[i]).getCanonicalFile().toURI().toURL().getPath());
        
        if (i != paths.length - 1)
        {
          codebaseBuilder.append(" ");
        }
      }
      
      return codebaseBuilder.toString();
    }
    catch (IOException e)
    {
      throw new MatlabConnectionException("Unable to resolve classpath entry", e);
    }
  }
  





  static String[] getClassPathAsCanonicalPaths()
    throws MatlabConnectionException
  {
    try
    {
      String[] paths = System.getProperty("java.class.path", "").split(File.pathSeparator);
      
      for (int i = 0; i < paths.length; i++)
      {
        paths[i] = new File(paths[i]).getCanonicalPath();
      }
      
      return paths;
    }
    catch (IOException e)
    {
      throw new MatlabConnectionException("Unable to resolve classpath entry", e);
    }
  }
  













  static String getSupportCodeLocation()
    throws MatlabConnectionException
  {
    ProtectionDomain domain = Configuration.class.getProtectionDomain();
    

    CodeSource codeSource = domain.getCodeSource();
    if (codeSource != null)
    {

      URL url = codeSource.getLocation();
      if (url != null)
      {

        try
        {

          URI uri = url.toURI();
          

          String path = uri.getPath();
          if (path != null)
          {
            try
            {
              File file = new File(path).getCanonicalFile();
              if (file.exists())
              {
                return file.getAbsolutePath();
              }
              

              ClassLoader loader = Configuration.class.getClassLoader();
              throw new MatlabConnectionException("Support code location was determined improperly. Location does not exist.\nLocation determined as: " + file.getAbsolutePath() + "\n" + "Path: " + path + "\n" + "URI Location: " + uri + "\n" + "URL Location: " + url + "\n" + "Code Source: " + codeSource + "\n" + "Protection Domain: " + domain + "\n" + "Class Loader: " + loader + (loader == null ? "" : new StringBuilder().append("\nClass Loader Class: ").append(loader.getClass()).toString()));






            }
            catch (IOException e)
            {





              ClassLoader loader = Configuration.class.getClassLoader();
              throw new MatlabConnectionException("Support code location could not be determined. Could not resolve canonical path.\nPath: " + path + "\n" + "URI Location: " + uri + "\n" + "URL Location: " + url + "\n" + "Code Source: " + codeSource + "\n" + "Protection Domain: " + domain + "\n" + "Class Loader: " + loader + (loader == null ? "" : new StringBuilder().append("\nClass Loader Class: ").append(loader.getClass()).toString()), e);
            }
          }
          










          ClassLoader loader = Configuration.class.getClassLoader();
          throw new MatlabConnectionException("Support code location could not be determined. Could not get path from URI location.\nURI Location: " + uri + "\n" + "URL Location: " + url + "\n" + "Code Source: " + codeSource + "\n" + "Protection Domain: " + domain + "\n" + "Class Loader: " + loader + (loader == null ? "" : new StringBuilder().append("\nClass Loader Class: ").append(loader.getClass()).toString()));





        }
        catch (URISyntaxException e)
        {




          ClassLoader loader = Configuration.class.getClassLoader();
          throw new MatlabConnectionException("Support code location could not be determined. Could not convert from URL to URI location.\nURL Location: " + url + "\n" + "Code Source: " + codeSource + "\n" + "Protection Domain: " + domain + "\n" + "Class Loader: " + loader + (loader == null ? "" : new StringBuilder().append("\nClass Loader Class: ").append(loader.getClass()).toString()), e);
        }
      }
      








      ClassLoader loader = Configuration.class.getClassLoader();
      throw new MatlabConnectionException("Support code location could not be determined. Could not get URL from CodeSource.\nCode Source: " + codeSource + "\n" + "Protection Domain: " + domain + "\n" + "Class Loader: " + loader + (loader == null ? "" : new StringBuilder().append("\nClass Loader Class: ").append(loader.getClass()).toString()));
    }
    








    ClassLoader loader = Configuration.class.getClassLoader();
    throw new MatlabConnectionException("Support code location could not be determined. Could not get CodeSource from ProtectionDomain.\nProtection Domain: " + domain + "\n" + "Class Loader: " + loader + (loader == null ? "" : new StringBuilder().append("\nClass Loader Class: ").append(loader.getClass()).toString()));
  }
  







  static boolean isRunningInsideMatlab()
  {
    boolean available;
    





    try
    {
      Class<?> matlabClass = Class.forName("com.mathworks.jmi.Matlab");
      Method isAvailableMethod = matlabClass.getMethod("isMatlabAvailable", new Class[0]);
      available = ((Boolean)isAvailableMethod.invoke(null, new Object[0])).booleanValue();
    }
    catch (Throwable t)
    {
      available = false;
    }
    
    return available;
  }
}
