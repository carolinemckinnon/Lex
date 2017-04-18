package matlabcontrol.extensions;

import java.lang.reflect.Array;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxy.DisconnectionListener;
import matlabcontrol.MatlabProxy.Identifier;
import matlabcontrol.MatlabProxy.MatlabThreadCallable;








































/**
 * @deprecated
 */
public class MatlabProxyLogger
{
  private static final Logger LOGGER = Logger.getLogger(MatlabProxyLogger.class.getName());
  

  static { LOGGER.setLevel(Level.FINER); }
  
  private static final String CLASS_NAME = MatlabProxyLogger.class.getName();
  



  private final MatlabProxy _proxy;
  



  public MatlabProxyLogger(MatlabProxy proxy)
  {
    _proxy = proxy;
  }
  





  public static void showInConsoleHandler()
  {
    for (Handler handler : Logger.getLogger("").getHandlers())
    {
      if ((handler instanceof ConsoleHandler))
      {
        handler.setLevel(Level.FINER);
      }
    }
  }
  
  private static abstract class Invocation
  {
    final String name;
    final Object[] args;
    
    public Invocation(String name, Object... args)
    {
      this.name = name;
      this.args = args;
    }
  }
  
  private static abstract class VoidThrowingInvocation extends MatlabProxyLogger.Invocation
  {
    public VoidThrowingInvocation(String name, Object... args)
    {
      super(args);
    }
    
    public abstract void invoke() throws MatlabInvocationException;
  }
  
  private static abstract class VoidInvocation extends MatlabProxyLogger.Invocation
  {
    public VoidInvocation(String name, Object... args)
    {
      super(args);
    }
    
    public abstract void invoke();
  }
  
  private static abstract class ReturnThrowingInvocation<T> extends MatlabProxyLogger.Invocation
  {
    public ReturnThrowingInvocation(String name, Object... args)
    {
      super(args);
    }
    
    public abstract T invoke() throws MatlabInvocationException;
  }
  
  private static abstract class ReturnInvocation<T> extends MatlabProxyLogger.Invocation
  {
    public ReturnInvocation(String name, Object... args)
    {
      super(args);
    }
    
    public abstract T invoke();
  }
  
  private static abstract class ReturnBooleanInvocation extends MatlabProxyLogger.Invocation
  {
    public ReturnBooleanInvocation(String name, Object... args)
    {
      super(args);
    }
    
    public abstract boolean invoke();
  }
  
  private void invoke(VoidThrowingInvocation invocation) throws MatlabInvocationException
  {
    LOGGER.entering(CLASS_NAME, name, args);
    
    try
    {
      invocation.invoke();
      LOGGER.exiting(CLASS_NAME, name);
    }
    catch (MatlabInvocationException e)
    {
      LOGGER.throwing(CLASS_NAME, name, e);
      LOGGER.exiting(CLASS_NAME, name);
      
      throw e;
    }
  }
  
  private void invoke(VoidInvocation invocation)
  {
    LOGGER.entering(CLASS_NAME, name, args);
    invocation.invoke();
    LOGGER.exiting(CLASS_NAME, name);
  }
  

  private <T> T invoke(ReturnThrowingInvocation<T> invocation)
    throws MatlabInvocationException
  {
    LOGGER.entering(CLASS_NAME, name, args);
    T data;
    try {
      data = invocation.invoke();
      LOGGER.exiting(CLASS_NAME, name, formatResult(data));
    }
    catch (MatlabInvocationException e)
    {
      LOGGER.throwing(CLASS_NAME, name, e);
      LOGGER.exiting(CLASS_NAME, name);
      
      throw e;
    }
    return data;
  }
  


  private <T> T invoke(ReturnInvocation<T> invocation)
  {
    LOGGER.entering(CLASS_NAME, name, args);
    T data = invocation.invoke();
    LOGGER.exiting(CLASS_NAME, name, formatResult(data));
    
    return data;
  }
  


  private boolean invoke(ReturnBooleanInvocation invocation)
  {
    LOGGER.entering(CLASS_NAME, name, args);
    boolean data = invocation.invoke();
    LOGGER.exiting(CLASS_NAME, name, "boolean: " + data);
    
    return data;
  }
  





  public void eval(final String command)
    throws MatlabInvocationException
  {
    invoke(new VoidThrowingInvocation("eval(String)", new Object[] { command })
    {
      public void invoke()
        throws MatlabInvocationException
      {
        _proxy.eval(command);
      }
    });
  }
  







  public Object[] returningEval(final String command, final int nargout)
    throws MatlabInvocationException
  {
    (Object[])invoke(new ReturnThrowingInvocation("returningEval(String, int)", new Object[] { command, Integer.valueOf(nargout) })
    {
      public Object[] invoke()
        throws MatlabInvocationException
      {
        return _proxy.returningEval(command, nargout);
      }
    });
  }
  






  public void feval(final String functionName, Object... args)
    throws MatlabInvocationException
  {
    invoke(new VoidThrowingInvocation("feval(String, Object...)", new Object[] { functionName, args })
    {
      public void invoke()
        throws MatlabInvocationException
      {
        _proxy.feval(functionName, args);
      }
    });
  }
  









  public Object[] returningFeval(final String functionName, final int nargout, Object... args)
    throws MatlabInvocationException
  {
    (Object[])invoke(new ReturnThrowingInvocation("returningFeval(String, int, Object...)", new Object[] { functionName, Integer.valueOf(nargout), args })
    {

      public Object[] invoke()
        throws MatlabInvocationException
      {
        return _proxy.returningFeval(functionName, nargout, args);
      }
    });
  }
  






  public void setVariable(final String variableName, final Object value)
    throws MatlabInvocationException
  {
    invoke(new VoidThrowingInvocation("setVariable(String, int)", new Object[] { variableName, value })
    {
      public void invoke()
        throws MatlabInvocationException
      {
        _proxy.setVariable(variableName, value);
      }
    });
  }
  






  public Object getVariable(final String variableName)
    throws MatlabInvocationException
  {
    invoke(new ReturnThrowingInvocation("getVariable(String)", new Object[] { variableName })
    {
      public Object invoke()
        throws MatlabInvocationException
      {
        return _proxy.getVariable(variableName);
      }
    });
  }
  







  public <U> U invokeAndWait(final MatlabProxy.MatlabThreadCallable<U> callable)
    throws MatlabInvocationException
  {
    invoke(new ReturnThrowingInvocation("invokeAndWait(MatlabThreadCallable)", new Object[] { callable })
    {
      public U invoke()
        throws MatlabInvocationException
      {
        return _proxy.invokeAndWait(callable);
      }
    });
  }
  





  public void addDisconnectionListener(final MatlabProxy.DisconnectionListener listener)
  {
    invoke(new VoidInvocation("addDisconnectionListener(DisconnectionListener)", new Object[] { listener })
    {

      public void invoke()
      {
        _proxy.addDisconnectionListener(listener);
      }
    });
  }
  






  public void removeDisconnectionListener(final MatlabProxy.DisconnectionListener listener)
  {
    invoke(new VoidInvocation("removeDisconnectionListener(DisconnectionListener)", new Object[] { listener })
    {

      public void invoke()
      {
        _proxy.removeDisconnectionListener(listener);
      }
    });
  }
  





  public boolean disconnect()
  {
    invoke(new ReturnBooleanInvocation("disconnect()", new Object[0])
    {

      public boolean invoke()
      {
        return _proxy.disconnect();
      }
    });
  }
  





  public boolean isExistingSession()
  {
    invoke(new ReturnBooleanInvocation("isExistingSession()", new Object[0])
    {

      public boolean invoke()
      {
        return _proxy.isExistingSession();
      }
    });
  }
  





  public boolean isRunningInsideMatlab()
  {
    invoke(new ReturnBooleanInvocation("isRunningInsideMatlab", new Object[0])
    {

      public boolean invoke()
      {
        return _proxy.isRunningInsideMatlab();
      }
    });
  }
  





  public boolean isConnected()
  {
    invoke(new ReturnBooleanInvocation("isConnected()", new Object[0])
    {

      public boolean invoke()
      {
        return _proxy.isConnected();
      }
    });
  }
  





  public MatlabProxy.Identifier getIdentifier()
  {
    (MatlabProxy.Identifier)invoke(new ReturnInvocation("getIdentifier()", new Object[0])
    {

      public MatlabProxy.Identifier invoke()
      {
        return _proxy.getIdentifier();
      }
    });
  }
  




  public void exit()
    throws MatlabInvocationException
  {
    invoke(new VoidThrowingInvocation("exit", new Object[0])
    {
      public void invoke()
        throws MatlabInvocationException
      {
        _proxy.exit();
      }
    });
  }
  







  public String toString()
  {
    return "[" + getClass().getName() + " proxy=" + _proxy + "]";
  }
  
  private String formatResult(Object result)
  {
    String formattedResult;
    String formattedResult;
    if (result == null)
    {
      formattedResult = "null";
    } else { String formattedResult;
      if (result.getClass().isArray())
      {
        formattedResult = result.getClass().getName() + "\n" + formatResult(result, 0).trim();
      }
      else
      {
        formattedResult = result.getClass().getName() + ": " + result.toString();
      }
    }
    return formattedResult;
  }
  








  private static String formatResult(Object result, int level)
  {
    StringBuilder builder = new StringBuilder();
    

    String tab = "";
    for (int i = 0; i < level + 1; i++)
    {
      tab = tab + "  ";
    }
    

    if (result == null)
    {
      builder.append("null\n");

    }
    else if (result.getClass().isArray())
    {
      Class<?> componentClass = result.getClass().getComponentType();
      

      if (componentClass.isPrimitive())
      {
        String componentName = componentClass.toString();
        int length = Array.getLength(result);
        
        builder.append(componentName);
        builder.append(" array, length = ");
        builder.append(length);
        builder.append("\n");
        
        for (int i = 0; i < length; i++)
        {
          builder.append(tab);
          builder.append("index ");
          builder.append(i);
          builder.append(", ");
          builder.append(componentName);
          builder.append(": ");
          builder.append(Array.get(result, i));
          builder.append("\n");
        }
        
      }
      else
      {
        Object[] array = (Object[])result;
        
        builder.append(array.getClass().getComponentType().getName());
        builder.append(" array, length = ");
        builder.append(array.length);
        builder.append("\n");
        
        for (int i = 0; i < array.length; i++)
        {
          builder.append(tab);
          builder.append("index ");
          builder.append(i);
          builder.append(", ");
          builder.append(formatResult(array[i], level + 1));
        }
        
      }
    }
    else
    {
      builder.append(result.getClass().getCanonicalName());
      builder.append(": ");
      builder.append(result);
      builder.append("\n");
    }
    
    return builder.toString();
  }
}
