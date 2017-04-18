package matlabcontrol.extensions;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxy.MatlabThreadCallable;














































/**
 * @deprecated
 */
public class CallbackMatlabProxy
{
  private final ExecutorService _executor = Executors.newFixedThreadPool(1, new DaemonThreadFactory(null));
  


  private final MatlabProxy _proxy;
  



  public CallbackMatlabProxy(MatlabProxy proxy)
  {
    _proxy = proxy;
  }
  






  public String toString()
  {
    return "[" + getClass().getName() + " proxy=" + _proxy + "]";
  }
  



  public void isConnected(final MatlabDataCallback<Boolean> callback)
  {
    _executor.submit(new Runnable()
    {

      public void run()
      {
        boolean connected = _proxy.isConnected();
        callback.invocationSucceeded(Boolean.valueOf(connected));
      }
    });
  }
  





  public void disconnect(final MatlabDataCallback<Boolean> callback)
  {
    _executor.submit(new Runnable()
    {

      public void run()
      {
        boolean succeeded = _proxy.disconnect();
        callback.invocationSucceeded(Boolean.valueOf(succeeded));
      }
    });
  }
  





  public void exit(final MatlabCallback callback)
  {
    _executor.submit(new Runnable()
    {

      public void run()
      {
        try
        {
          _proxy.exit();
          callback.invocationSucceeded();
        }
        catch (MatlabInvocationException e)
        {
          callback.invocationFailed(e);
        }
      }
    });
  }
  






  public void eval(final MatlabCallback callback, final String command)
  {
    _executor.submit(new Runnable()
    {

      public void run()
      {
        try
        {
          _proxy.eval(command);
          callback.invocationSucceeded();
        }
        catch (MatlabInvocationException e)
        {
          callback.invocationFailed(e);
        }
      }
    });
  }
  







  public void returningEval(final MatlabDataCallback<Object[]> callback, final String command, final int nargout)
  {
    _executor.submit(new Runnable()
    {

      public void run()
      {
        try
        {
          Object[] data = _proxy.returningEval(command, nargout);
          callback.invocationSucceeded(data);
        }
        catch (MatlabInvocationException e)
        {
          callback.invocationFailed(e);
        }
      }
    });
  }
  







  public void feval(final MatlabCallback callback, final String functionName, final Object... args)
  {
    _executor.submit(new Runnable()
    {

      public void run()
      {
        try
        {
          _proxy.feval(functionName, args);
          callback.invocationSucceeded();
        }
        catch (MatlabInvocationException e)
        {
          callback.invocationFailed(e);
        }
      }
    });
  }
  









  public void returningFeval(final MatlabDataCallback<Object[]> callback, final String functionName, final int nargout, final Object... args)
  {
    _executor.submit(new Runnable()
    {

      public void run()
      {
        try
        {
          Object[] data = _proxy.returningFeval(functionName, nargout, args);
          callback.invocationSucceeded(data);
        }
        catch (MatlabInvocationException e)
        {
          callback.invocationFailed(e);
        }
      }
    });
  }
  







  public void setVariable(final MatlabCallback callback, final String variableName, final Object value)
  {
    _executor.submit(new Runnable()
    {

      public void run()
      {
        try
        {
          _proxy.setVariable(variableName, value);
          callback.invocationSucceeded();
        }
        catch (MatlabInvocationException e)
        {
          callback.invocationFailed(e);
        }
      }
    });
  }
  






  public void getVariable(final MatlabDataCallback<Object> callback, final String variableName)
  {
    _executor.submit(new Runnable()
    {

      public void run()
      {
        try
        {
          Object data = _proxy.getVariable(variableName);
          callback.invocationSucceeded(data);
        }
        catch (MatlabInvocationException e)
        {
          callback.invocationFailed(e);
        }
      }
    });
  }
  











  public <U> void invokeAndWait(final MatlabProxy.MatlabThreadCallable<U> callable, final MatlabDataCallback<U> callback)
  {
    _executor.submit(new Runnable()
    {

      public void run()
      {
        try
        {
          U data = _proxy.invokeAndWait(callable);
          callback.invocationSucceeded(data);
        }
        catch (MatlabInvocationException e)
        {
          callback.invocationFailed(e);
        }
      }
    });
  }
  










































  private static class DaemonThreadFactory
    implements ThreadFactory
  {
    private final ThreadFactory _delegateFactory = Executors.defaultThreadFactory();
    
    private DaemonThreadFactory() {}
    
    public Thread newThread(Runnable r) {
      Thread thread = _delegateFactory.newThread(r);
      thread.setName("MatlabCallbackInteractor Thread");
      thread.setDaemon(true);
      
      return thread;
    }
  }
  
  public static abstract interface MatlabCallback
  {
    public abstract void invocationSucceeded();
    
    public abstract void invocationFailed(MatlabInvocationException paramMatlabInvocationException);
  }
  
  public static abstract interface MatlabDataCallback<V>
  {
    public abstract void invocationSucceeded(V paramV);
    
    public abstract void invocationFailed(MatlabInvocationException paramMatlabInvocationException);
  }
}
