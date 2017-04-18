package matlabcontrol;

import java.io.File;
import java.util.concurrent.atomic.AtomicLong;
































public class MatlabProxyFactoryOptions
{
  private final String _matlabLocation;
  private final File _startingDirectory;
  private final boolean _hidden;
  private final boolean _usePreviouslyControlled;
  private final long _proxyTimeout;
  private final String _logFile;
  private final Integer _jdbPort;
  private final String _licenseFile;
  private final boolean _useSingleCompThread;
  private final int _port;
  
  private MatlabProxyFactoryOptions(Builder options)
  {
    _matlabLocation = _matlabLocation;
    _startingDirectory = _startingDirectory;
    _hidden = _hidden;
    _usePreviouslyControlled = _usePreviouslyControlled;
    _proxyTimeout = _proxyTimeout.get();
    _logFile = _logFile;
    _jdbPort = _jdbPort;
    _licenseFile = _licenseFile;
    _useSingleCompThread = _useSingleCompThread;
    _port = _port;
  }
  
  String getMatlabLocation()
  {
    return _matlabLocation;
  }
  
  File getStartingDirectory()
  {
    return _startingDirectory;
  }
  
  boolean getHidden()
  {
    return _hidden;
  }
  
  boolean getUsePreviouslyControlledSession()
  {
    return _usePreviouslyControlled;
  }
  
  long getProxyTimeout()
  {
    return _proxyTimeout;
  }
  
  String getLogFile()
  {
    return _logFile;
  }
  
  Integer getJavaDebugger()
  {
    return _jdbPort;
  }
  
  String getLicenseFile()
  {
    return _licenseFile;
  }
  
  boolean getUseSingleComputationalThread()
  {
    return _useSingleCompThread;
  }
  
  int getPort()
  {
    return _port;
  }
  



















  public static class Builder
  {
    private volatile String _matlabLocation = null;
    private volatile File _startingDirectory = null;
    private volatile boolean _hidden = false;
    private volatile boolean _usePreviouslyControlled = false;
    private volatile String _logFile = null;
    private volatile Integer _jdbPort = null;
    private volatile String _licenseFile = null;
    private volatile boolean _useSingleCompThread = false;
    private volatile int _port = 2100;
    

    private final AtomicLong _proxyTimeout = new AtomicLong(180000L);
    





















    public Builder() {}
    




















    public final Builder setMatlabLocation(String matlabLocation)
    {
      _matlabLocation = matlabLocation;
      
      return this;
    }
    







    public final Builder setMatlabStartingDirectory(File dir)
    {
      if (dir == null)
      {
        throw new NullPointerException("dir may not be null");
      }
      
      if (!dir.exists())
      {
        throw new IllegalArgumentException("dir specifies a directory that does not exist");
      }
      
      if (!dir.isDirectory())
      {
        throw new IllegalArgumentException("dir does not specify a directory");
      }
      
      _startingDirectory = dir;
      
      return this;
    }
    















    public final Builder setHidden(boolean hidden)
    {
      _hidden = hidden;
      
      return this;
    }
    







    public final Builder setLogFile(String logFile)
    {
      _logFile = logFile;
      
      return this;
    }
    








    public final Builder setJavaDebugger(int portnumber)
    {
      if ((portnumber < 0) || (portnumber > 65535))
      {
        throw new IllegalArgumentException("port number [" + portnumber + "] must be in the range 0-65535");
      }
      
      _jdbPort = Integer.valueOf(portnumber);
      
      return this;
    }
    









    public final Builder setLicenseFile(String licenseFile)
    {
      _licenseFile = licenseFile;
      
      return this;
    }
    




































    public final Builder setUsePreviouslyControlledSession(boolean usePreviouslyControlled)
    {
      _usePreviouslyControlled = usePreviouslyControlled;
      
      return this;
    }
    








    public final Builder setProxyTimeout(long timeout)
    {
      if (timeout < 0L)
      {
        throw new IllegalArgumentException("timeout [" + timeout + "] may not be negative");
      }
      
      _proxyTimeout.set(timeout);
      
      return this;
    }
    






    public final Builder setUseSingleComputationalThread(boolean useSingleCompThread)
    {
      _useSingleCompThread = useSingleCompThread;
      
      return this;
    }
    











    public final Builder setPort(int port)
    {
      if (port < 0)
      {
        throw new IllegalArgumentException("port [" + port + "] may not be negative");
      }
      
      _port = port;
      
      return this;
    }
    





    public final MatlabProxyFactoryOptions build()
    {
      return new MatlabProxyFactoryOptions(this, null);
    }
  }
}
