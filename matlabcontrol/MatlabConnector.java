package matlabcontrol;

import java.io.PrintStream;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import matlabcontrol.internal.MatlabRMIClassLoaderSpi;







































class MatlabConnector
{
  private static final ExecutorService _connectionExecutor = Executors.newSingleThreadExecutor(new NamedThreadFactory("MLC Connection Establisher"));
  




  private static final AtomicReference<RequestReceiver> _receiverRef = new AtomicReference();
  



  private static final AtomicBoolean _connectionInProgress = new AtomicBoolean(false);
  

  private MatlabConnector() {}
  

  private static class NamedThreadFactory
    implements ThreadFactory
  {
    private static final AtomicInteger COUNTER = new AtomicInteger();
    
    private final String _name;
    
    NamedThreadFactory(String name)
    {
      _name = name;
    }
    

    public Thread newThread(Runnable r)
    {
      return new Thread(r, _name + "-" + COUNTER.getAndIncrement());
    }
  }
  


  static boolean isAvailableForConnection()
  {
    boolean available;
    

    boolean available;
    

    if (_connectionInProgress.get())
    {
      available = false;
    }
    else
    {
      RequestReceiver receiver = (RequestReceiver)_receiverRef.get();
      
      boolean connected = false;
      if (receiver != null)
      {
        try
        {
          receiver.getReceiverID();
          connected = true;
        }
        catch (RemoteException e) {}
      }
      
      available = !connected;
    }
    
    return available;
  }
  








  public static void connectFromMatlab(String receiverID, int port)
  {
    connect(receiverID, port, false);
  }
  








  static void connect(String receiverID, int port, boolean existingSession)
  {
    _connectionInProgress.set(true);
    


    _connectionExecutor.submit(new EstablishConnectionRunnable(receiverID, port, existingSession, null));
  }
  



  private static class EstablishConnectionRunnable
    implements Runnable
  {
    private final String _receiverID;
    


    private final int _port;
    


    private final boolean _existingSession;
    

    private static volatile String[] _previousRemoteClassPath = new String[0];
    
    private EstablishConnectionRunnable(String receiverID, int port, boolean existingSession)
    {
      _receiverID = receiverID;
      _port = port;
      _existingSession = existingSession;
    }
    


    public void run()
    {
      try
      {
        
      }
      catch (MatlabConnectionException ex)
      {
        System.err.println("matlabcontrol is not compatible with this version of MATLAB");
        ex.printStackTrace();
        return;
      }
      

      if (!_existingSession)
      {

        System.setProperty("java.rmi.server.RMIClassLoaderSpi", MatlabRMIClassLoaderSpi.class.getName());
        

        try
        {
          MatlabBroadcaster.broadcast(_port);
        }
        catch (MatlabConnectionException ex)
        {
          System.err.println("Reconnecting to this session of MATLAB will not be possible");
          ex.printStackTrace();
        }
      }
      


      try
      {
        Registry registry = LocalHostRMIHelper.getRegistry(_port);
        

        RequestReceiver receiver;
        

        try
        {
          receiver = (RequestReceiver)registry.lookup(_receiverID);
        }
        catch (NotBoundException nbe1)
        {
          try
          {
            Thread.sleep(1000L);
            
            try
            {
              receiver = (RequestReceiver)registry.lookup(_receiverID);
            }
            catch (NotBoundException nbe2)
            {
              System.err.println("First attempt to connect to Java application failed");
              nbe1.printStackTrace();
              System.err.println("Second attempt to connect to Java application failed");
              nbe2.printStackTrace();
              return;
            }
          }
          catch (InterruptedException ie)
          {
            System.err.println("First attempt to connect to Java application failed");
            nbe1.printStackTrace();
            System.err.println("Interrupted while waiting to retry connection to Java application");
            ie.printStackTrace();
            return;
          }
        }
        

        MatlabConnector._receiverRef.set(receiver);
        

        if (System.getSecurityManager() == null)
        {
          System.setSecurityManager(new PermissiveSecurityManager());
        }
        


        MatlabRMIClassLoaderSpi.setCodebase(receiver.getClassPathAsRMICodebase());
        



        String[] newClassPath = receiver.getClassPathAsCanonicalPaths();
        try
        {
          JMIWrapper.invokeAndWait(new MatlabConnector.ModifyCodebaseCallable(_previousRemoteClassPath, newClassPath));
          _previousRemoteClassPath = newClassPath;
        }
        catch (MatlabInvocationException e)
        {
          System.err.println("Unable to update MATLAB's class loader; issues may arise interacting with classes not defined in MATLAB's Java Virtual Machine");
          
          e.printStackTrace();
        }
        

        receiver.receiveJMIWrapper(new JMIWrapperRemoteImpl(), _existingSession);
      }
      catch (RemoteException ex)
      {
        System.err.println("Connection to Java application could not be established");
        ex.printStackTrace();
      }
      
      MatlabConnector._connectionInProgress.set(false);
    }
  }
  


  private static class ModifyCodebaseCallable
    implements MatlabProxy.MatlabThreadCallable<Void>
  {
    private final String[] _toRemove;
    
    private final String[] _toAdd;
    

    public ModifyCodebaseCallable(String[] oldRemoteClassPath, String[] newRemoteClassPath)
    {
      _toRemove = oldRemoteClassPath;
      _toAdd = newRemoteClassPath;
    }
    

    public Void call(MatlabProxy.MatlabThreadProxy proxy)
      throws MatlabInvocationException
    {
      String[] curr = (String[])proxy.returningFeval("javaclasspath", 1, new Object[] { "-dynamic" })[0];
      

      List<String> newDynamic = new ArrayList();
      newDynamic.addAll(Arrays.asList(curr));
      newDynamic.removeAll(Arrays.asList(_toRemove));
      newDynamic.addAll(Arrays.asList(_toAdd));
      

      if (!newDynamic.equals(Arrays.asList(curr)))
      {
        proxy.feval("javaclasspath", new Object[] { newDynamic.toArray(new String[newDynamic.size()]) });
      }
      
      return null;
    }
  }
}
