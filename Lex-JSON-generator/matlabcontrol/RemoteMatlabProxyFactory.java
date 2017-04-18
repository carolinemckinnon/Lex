package matlabcontrol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.rmi.AlreadyBoundException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;










































class RemoteMatlabProxyFactory
  implements ProxyFactory
{
  private final MatlabProxyFactoryOptions _options;
  private final CopyOnWriteArrayList<RemoteRequestReceiver> _receivers = new CopyOnWriteArrayList();
  



  static final long RECEIVER_CHECK_PERIOD = 1000L;
  



  private volatile Registry _registry = null;
  
  public RemoteMatlabProxyFactory(MatlabProxyFactoryOptions options)
  {
    _options = options;
  }
  

  public MatlabProxyFactory.Request requestProxy(MatlabProxyFactory.RequestCallback requestCallback)
    throws MatlabConnectionException
  {
    RemoteIdentifier proxyID = new RemoteIdentifier(null);
    



    initRegistry(false);
    

    RemoteRequestReceiver receiver = new RemoteRequestReceiver(requestCallback, proxyID, Configuration.getClassPathAsRMICodebase(), Configuration.getClassPathAsCanonicalPaths());
    
    _receivers.add(receiver);
    try
    {
      _registry.bind(receiver.getReceiverID(), LocalHostRMIHelper.exportObject(receiver));
    }
    catch (RemoteException ex)
    {
      _receivers.remove(receiver);
      throw new MatlabConnectionException("Could not bind proxy receiver to the RMI registry", ex);
    }
    catch (AlreadyBoundException ex)
    {
      _receivers.remove(receiver);
      throw new MatlabConnectionException("Could not bind proxy receiver to the RMI registry", ex);
    }
    

    RequestMaintainer maintainer = new RequestMaintainer(receiver);
    MatlabProxyFactory.Request request;
    try {
      MatlabProxyFactory.Request request;
      if ((_options.getUsePreviouslyControlledSession()) && (MatlabSessionImpl.connectToRunningSession(receiver.getReceiverID(), _options.getPort())))
      {

        request = new RemoteRequest(proxyID, null, receiver, maintainer, null);

      }
      else
      {
        Process process = createProcess(receiver);
        request = new RemoteRequest(proxyID, process, receiver, maintainer, null);
      }
    }
    catch (MatlabConnectionException e)
    {
      maintainer.shutdown();
      receiver.shutdown();
      throw e;
    }
    
    return request;
  }
  

  public MatlabProxy getProxy()
    throws MatlabConnectionException
  {
    GetProxyRequestCallback callback = new GetProxyRequestCallback();
    MatlabProxyFactory.Request request = requestProxy(callback);
    




    try
    {
      try
      {
        Thread.sleep(_options.getProxyTimeout());

      }
      catch (InterruptedException e)
      {
        if (callback.getProxy() == null)
        {
          throw new MatlabConnectionException("Thread was interrupted while waiting for MATLAB proxy", e);
        }
      }
      

      if (callback.getProxy() == null)
      {
        throw new MatlabConnectionException("MATLAB proxy could not be created in " + _options.getProxyTimeout() + " milliseconds");
      }
      

      return callback.getProxy();
    }
    catch (MatlabConnectionException e)
    {
      request.cancel();
      throw e;
    }
  }
  






  private synchronized void initRegistry(boolean force)
    throws MatlabConnectionException
  {
    if ((_registry == null) || (force))
    {
      try
      {

        _registry = LocalHostRMIHelper.createRegistry(_options.getPort());

      }
      catch (Exception e)
      {
        try
        {
          _registry = LocalHostRMIHelper.getRegistry(_options.getPort());
        }
        catch (Exception ex)
        {
          throw new MatlabConnectionException("Could not create or connect to the RMI registry", ex);
        }
      }
    }
  }
  







  private Process createProcess(RemoteRequestReceiver receiver)
    throws MatlabConnectionException
  {
    List<String> processArguments = new ArrayList();
    

    if (_options.getMatlabLocation() != null)
    {
      processArguments.add(_options.getMatlabLocation());
    }
    else
    {
      processArguments.add(Configuration.getMatlabLocation());
    }
    


    if (_options.getHidden())
    {
      if (Configuration.isWindows())
      {
        processArguments.add("-automation");
      }
      else
      {
        processArguments.add("-nosplash");
        processArguments.add("-nodesktop");

      }
      


    }
    else if (!Configuration.isWindows())
    {
      processArguments.add("-desktop");
    }
    

    if (_options.getLicenseFile() != null)
    {
      processArguments.add("-c");
      processArguments.add(_options.getLicenseFile());
    }
    
    if (_options.getLogFile() != null)
    {
      processArguments.add("-logfile");
      processArguments.add(_options.getLogFile());
    }
    
    if (_options.getJavaDebugger() != null)
    {
      processArguments.add("-jdb");
      processArguments.add(_options.getJavaDebugger().toString());
    }
    
    if (_options.getUseSingleComputationalThread())
    {
      processArguments.add("-singleCompThread");
    }
    

    processArguments.add("-r");
    





    String codeLocation = Configuration.getSupportCodeLocation();
    String runArg = "javaaddpath '" + codeLocation + "'; " + MatlabClassLoaderHelper.class.getName() + ".configureClassLoading(); " + "javarmpath '" + codeLocation + "'; " + MatlabConnector.class.getName() + ".connectFromMatlab('" + receiver.getReceiverID() + "', " + _options.getPort() + ");";
    



    processArguments.add(runArg);
    

    ProcessBuilder builder = new ProcessBuilder(processArguments);
    builder.directory(_options.getStartingDirectory());
    
    try
    {
      Process process = builder.start();
      

      if ((_options.getHidden()) && (!Configuration.isWindows()))
      {
        new ProcessStreamDrainer(process.getInputStream(), "Input", null).start();
        new ProcessStreamDrainer(process.getErrorStream(), "Error", null).start();
      }
      
      return process;

    }
    catch (IOException e)
    {
      String errorMsg = "Could not launch MATLAB. This is likely caused by MATLAB not being in a known location or on a known path. MATLAB's location can be explicitly provided by using " + MatlabProxyFactoryOptions.Builder.class.getCanonicalName() + "'s setMatlabLocation(...) method.\n" + "OS: " + Configuration.getOperatingSystem() + "\n" + "Command: " + builder.command() + "\n" + "Environment: " + builder.environment();
      




      throw new MatlabConnectionException(errorMsg, e);
    }
  }
  


  private static class ProcessStreamDrainer
    extends Thread
  {
    private final InputStream _stream;
    

    private ProcessStreamDrainer(InputStream stream, String type)
    {
      _stream = stream;
      
      setDaemon(true);
      setName("ProcessStreamDrainer - " + type);
    }
    

    public void run()
    {
      try
      {
        BufferedReader in = new BufferedReader(new InputStreamReader(_stream));
        while (in.readLine() != null) {}
      }
      catch (IOException e) {}
    }
  }
  

  private class RemoteRequestReceiver
    implements RequestReceiver
  {
    private final MatlabProxyFactory.RequestCallback _requestCallback;
    
    private final RemoteMatlabProxyFactory.RemoteIdentifier _proxyID;
    
    private final String _codebase;
    
    private final String[] _canonicalPaths;
    private final String _receiverID;
    private volatile boolean _receivedJMIWrapper = false;
    

    public RemoteRequestReceiver(MatlabProxyFactory.RequestCallback requestCallback, RemoteMatlabProxyFactory.RemoteIdentifier proxyID, String codebase, String[] canonicalPaths)
    {
      _requestCallback = requestCallback;
      _proxyID = proxyID;
      _codebase = codebase;
      _canonicalPaths = canonicalPaths;
      
      _receiverID = ("PROXY_RECEIVER_" + proxyID.getUUIDString());
    }
    


    public void receiveJMIWrapper(JMIWrapperRemote jmiWrapper, boolean existingSession)
    {
      _receivers.remove(this);
      

      RemoteMatlabProxy proxy = new RemoteMatlabProxy(jmiWrapper, this, _proxyID, existingSession);
      proxy.init();
      

      _receivedJMIWrapper = true;
      

      _requestCallback.proxyCreated(proxy);
    }
    

    public String getReceiverID()
    {
      return _receiverID;
    }
    
    public boolean shutdown()
    {
      _receivers.remove(this);
      
      boolean success;
      try
      {
        success = UnicastRemoteObject.unexportObject(this, true);
      }
      catch (NoSuchObjectException e)
      {
        success = true;
      }
      
      return success;
    }
    
    public boolean hasReceivedJMIWrapper()
    {
      return _receivedJMIWrapper;
    }
    
    public String getClassPathAsRMICodebase()
      throws RemoteException
    {
      return _codebase;
    }
    
    public String[] getClassPathAsCanonicalPaths()
      throws RemoteException
    {
      return _canonicalPaths;
    }
  }
  


  private class RequestMaintainer
  {
    private final Timer _timer;
    

    RequestMaintainer(final RemoteMatlabProxyFactory.RemoteRequestReceiver receiver)
    {
      _timer = new Timer("MLC Request Maintainer " + receiver.getReceiverID());
      
      _timer.schedule(new TimerTask()
      {

        public void run()
        {

          try
          {

            _registry.lookup(receiver.getReceiverID());

          }
          catch (NotBoundException e)
          {

            try
            {
              UnicastRemoteObject.unexportObject(receiver, true);
            }
            catch (NoSuchObjectException ex) {}
            

            try
            {
              _registry.bind(receiver.getReceiverID(), LocalHostRMIHelper.exportObject(receiver));

            }
            catch (RemoteException ex) {}catch (AlreadyBoundException ex) {}

          }
          catch (RemoteException e)
          {

            try
            {
              RemoteMatlabProxyFactory.this.initRegistry(true);
              

              try
              {
                UnicastRemoteObject.unexportObject(receiver, true);
              }
              catch (NoSuchObjectException ex) {}
              

              try
              {
                _registry.bind(receiver.getReceiverID(), LocalHostRMIHelper.exportObject(receiver));
              }
              catch (RemoteException ex) {}catch (AlreadyBoundException ex) {}
            }
            catch (MatlabConnectionException ex) {}
          }
          


          if (receiver.hasReceivedJMIWrapper())
          {
            _timer.cancel(); } } }, 1000L, 1000L);
    }
    



    void shutdown()
    {
      _timer.cancel();
    }
  }
  
  private static class GetProxyRequestCallback implements MatlabProxyFactory.RequestCallback
  {
    private final Thread _requestingThread;
    private volatile MatlabProxy _proxy;
    
    public GetProxyRequestCallback()
    {
      _requestingThread = Thread.currentThread();
    }
    

    public void proxyCreated(MatlabProxy proxy)
    {
      _proxy = proxy;
      
      _requestingThread.interrupt();
    }
    
    public MatlabProxy getProxy()
    {
      return _proxy;
    }
  }
  
  private static final class RemoteIdentifier implements MatlabProxy.Identifier
  {
    private final UUID _id;
    
    private RemoteIdentifier()
    {
      _id = UUID.randomUUID();
    }
    

    public boolean equals(Object other)
    {
      boolean equals;
      boolean equals;
      if ((other instanceof RemoteIdentifier))
      {
        equals = _id.equals(_id);
      }
      else
      {
        equals = false;
      }
      
      return equals;
    }
    

    public int hashCode()
    {
      return _id.hashCode();
    }
    

    public String toString()
    {
      return "PROXY_REMOTE_" + _id;
    }
    
    String getUUIDString()
    {
      return _id.toString();
    }
  }
  
  private static class RemoteRequest implements MatlabProxyFactory.Request
  {
    private final MatlabProxy.Identifier _proxyID;
    private final Process _process;
    private final RemoteMatlabProxyFactory.RemoteRequestReceiver _receiver;
    private final RemoteMatlabProxyFactory.RequestMaintainer _maintainer;
    private boolean _isCancelled = false;
    

    private RemoteRequest(MatlabProxy.Identifier proxyID, Process process, RemoteMatlabProxyFactory.RemoteRequestReceiver receiver, RemoteMatlabProxyFactory.RequestMaintainer maintainer)
    {
      _proxyID = proxyID;
      _process = process;
      _receiver = receiver;
      _maintainer = maintainer;
    }
    

    public MatlabProxy.Identifier getProxyIdentifer()
    {
      return _proxyID;
    }
    

    public synchronized boolean cancel()
    {
      if (!_isCancelled)
      {
        _maintainer.shutdown();
        boolean success;
        boolean success;
        if (!isCompleted())
        {
          if (_process != null)
          {
            _process.destroy();
          }
          success = _receiver.shutdown();
        }
        else
        {
          success = false;
        }
        _isCancelled = success;
      }
      
      return _isCancelled;
    }
    

    public synchronized boolean isCancelled()
    {
      return _isCancelled;
    }
    

    public boolean isCompleted()
    {
      return _receiver.hasReceivedJMIWrapper();
    }
  }
}
