package matlabcontrol.internal;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.rmi.server.RMIClassLoader;
import java.rmi.server.RMIClassLoaderSpi;
import java.security.CodeSource;
import java.security.ProtectionDomain;










































public class MatlabRMIClassLoaderSpi
  extends RMIClassLoaderSpi
{
  private final RMIClassLoaderSpi _delegateLoaderSpi = RMIClassLoader.getDefaultProviderInstance();
  





  private static volatile String _remoteCodebase = null;
  


  public MatlabRMIClassLoaderSpi() {}
  


  public static void setCodebase(String remoteCodebase)
  {
    _remoteCodebase = remoteCodebase;
  }
  
  public Class<?> loadClass(String codebase, String name, ClassLoader defaultLoader)
    throws MalformedURLException, ClassNotFoundException
  {
    return _delegateLoaderSpi.loadClass(_remoteCodebase, name, defaultLoader);
  }
  
  public Class<?> loadProxyClass(String codebase, String[] interfaces, ClassLoader defaultLoader)
    throws MalformedURLException, ClassNotFoundException
  {
    return _delegateLoaderSpi.loadProxyClass(_remoteCodebase, interfaces, defaultLoader);
  }
  
  public ClassLoader getClassLoader(String codebase)
    throws MalformedURLException
  {
    return _delegateLoaderSpi.getClassLoader(_remoteCodebase);
  }
  












  public String getClassAnnotation(Class<?> clazz)
  {
    if (clazz == null)
    {
      throw new NullPointerException("class may not be null");
    }
    
    String annotation = null;
    

    if (clazz.getProtectionDomain().getCodeSource() != null)
    {


      try
      {


        File file = new File(clazz.getProtectionDomain().getCodeSource().getLocation().getPath());
        annotation = file.toURI().toURL().toString();
      }
      catch (MalformedURLException e) {}
    }
    
    return annotation;
  }
}
