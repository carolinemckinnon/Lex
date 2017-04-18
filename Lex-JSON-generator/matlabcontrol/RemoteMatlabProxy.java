package matlabcontrol;

import java.rmi.MarshalException;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.UnmarshalException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Timer;
import java.util.TimerTask;
















































class RemoteMatlabProxy
  extends MatlabProxy
{
  private final JMIWrapperRemote _jmiWrapper;
  private final RequestReceiver _receiver;
  private final Timer _connectionTimer;
  private volatile boolean _isConnected = true;
  






  private static final int CONNECTION_CHECK_PERIOD = 1000;
  






  RemoteMatlabProxy(JMIWrapperRemote internalProxy, RequestReceiver receiver, MatlabProxy.Identifier id, boolean existingSession)
  {
    super(id, existingSession);
    
    _connectionTimer = new Timer("MLC Connection Listener " + id);
    _jmiWrapper = internalProxy;
    _receiver = receiver;
  }
  






  void init() { _connectionTimer.schedule(new CheckConnectionTask(null), 1000L, 1000L); }
  
  private static abstract interface RemoteInvocation<T> {
    public abstract T invoke() throws RemoteException, MatlabInvocationException;
  }
  
  private class CheckConnectionTask extends TimerTask { private CheckConnectionTask() {}
    
    public void run() { if (!isConnected())
      {

        disconnect();
        

        notifyDisconnectionListeners();
        

        _connectionTimer.cancel();
      }
    }
  }
  

  public boolean isRunningInsideMatlab()
  {
    return false;
  }
  


  public boolean isConnected()
  {
    if (_isConnected)
    {
      boolean connected;
      
      try
      {
        _jmiWrapper.checkConnection();
        connected = true;
      }
      catch (RemoteException e)
      {
        connected = false;
      }
      
      _isConnected = connected;
    }
    
    return _isConnected;
  }
  

  public boolean disconnect()
  {
    _connectionTimer.cancel();
    



    try
    {
      _isConnected = (!UnicastRemoteObject.unexportObject(_receiver, true));
    }
    catch (NoSuchObjectException e) {}
    

    return isConnected();
  }
  






  private <T> T invoke(RemoteInvocation<T> invocation)
    throws MatlabInvocationException
  {
    if (!_isConnected)
    {
      throw MatlabInvocationException.Reason.PROXY_NOT_CONNECTED.asException();
    }
    

    try
    {
      return invocation.invoke();
    }
    catch (UnmarshalException e)
    {
      throw MatlabInvocationException.Reason.UNMARSHAL.asException(e);
    }
    catch (MarshalException e)
    {
      throw MatlabInvocationException.Reason.MARSHAL.asException(e);
    }
    catch (RemoteException e)
    {
      if (isConnected())
      {
        throw MatlabInvocationException.Reason.UNKNOWN.asException(e);
      }
      

      throw MatlabInvocationException.Reason.PROXY_NOT_CONNECTED.asException(e);
    }
  }
  


  public void setVariable(final String variableName, final Object value)
    throws MatlabInvocationException
  {
    invoke(new RemoteInvocation()
    {
      public Void invoke()
        throws RemoteException, MatlabInvocationException
      {
        _jmiWrapper.setVariable(variableName, value);
        
        return null;
      }
    });
  }
  
  public Object getVariable(final String variableName)
    throws MatlabInvocationException
  {
    invoke(new RemoteInvocation()
    {
      public Object invoke()
        throws RemoteException, MatlabInvocationException
      {
        return _jmiWrapper.getVariable(variableName);
      }
    });
  }
  
  public void exit()
    throws MatlabInvocationException
  {
    invoke(new RemoteInvocation()
    {
      public Void invoke()
        throws RemoteException, MatlabInvocationException
      {
        _jmiWrapper.exit();
        
        return null;
      }
    });
  }
  
  public void eval(final String command)
    throws MatlabInvocationException
  {
    invoke(new RemoteInvocation()
    {
      public Void invoke()
        throws RemoteException, MatlabInvocationException
      {
        _jmiWrapper.eval(command);
        
        return null;
      }
    });
  }
  
  public Object[] returningEval(final String command, final int nargout)
    throws MatlabInvocationException
  {
    (Object[])invoke(new RemoteInvocation()
    {
      public Object[] invoke()
        throws RemoteException, MatlabInvocationException
      {
        return _jmiWrapper.returningEval(command, nargout);
      }
    });
  }
  
  public void feval(final String functionName, final Object... args)
    throws MatlabInvocationException
  {
    invoke(new RemoteInvocation()
    {
      public Void invoke()
        throws RemoteException, MatlabInvocationException
      {
        _jmiWrapper.feval(functionName, args);
        
        return null;
      }
    });
  }
  

  public Object[] returningFeval(final String functionName, final int nargout, final Object... args)
    throws MatlabInvocationException
  {
    (Object[])invoke(new RemoteInvocation()
    {
      public Object[] invoke()
        throws RemoteException, MatlabInvocationException
      {
        return _jmiWrapper.returningFeval(functionName, nargout, args);
      }
    });
  }
  
  public <T> T invokeAndWait(final MatlabProxy.MatlabThreadCallable<T> callable)
    throws MatlabInvocationException
  {
    invoke(new RemoteInvocation()
    {
      public T invoke()
        throws RemoteException, MatlabInvocationException
      {
        return _jmiWrapper.invokeAndWait(callable);
      }
    });
  }
}
