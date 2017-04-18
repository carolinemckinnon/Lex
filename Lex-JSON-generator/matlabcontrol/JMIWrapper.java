package matlabcontrol;

import com.mathworks.jmi.Matlab;
import com.mathworks.jmi.NativeMatlab;
import java.awt.AWTEvent;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;


















































class JMIWrapper
{
  private static final MatlabThreadOperations THREAD_OPERATIONS = new MatlabThreadOperations(null);
  
  private static final EventQueue EVENT_QUEUE = Toolkit.getDefaultToolkit().getSystemEventQueue();
  private static final Method EVENT_QUEUE_DISPATCH_METHOD;
  
  static
  {
    try {
      EVENT_QUEUE_DISPATCH_METHOD = EventQueue.class.getDeclaredMethod("dispatchEvent", new Class[] { AWTEvent.class });
    }
    catch (NoSuchMethodException e)
    {
      throw new IllegalStateException("java.awt.EventQueue's protected void dispatchEvent(java.awt.AWTEvent) method could not be found", e);
    }
    

    EVENT_QUEUE_DISPATCH_METHOD.setAccessible(true);
  }
  







  static void exit()
  {
    Runnable runnable = new Runnable()
    {

      public void run()
      {
        try
        {
          Matlab.mtFevalConsoleOutput("exit", null, 0);
        }
        catch (Exception e) {}
      }
    };
    


    if (NativeMatlab.nativeIsMatlabThread())
    {
      runnable.run();
    }
    else
    {
      Matlab.whenMatlabIdle(runnable);
    }
  }
  



  static void setVariable(String variableName, final Object value)
    throws MatlabInvocationException
  {
    invokeAndWait(new MatlabProxy.MatlabThreadCallable()
    {
      public Void call(MatlabProxy.MatlabThreadProxy proxy)
        throws MatlabInvocationException
      {
        proxy.setVariable(val$variableName, value);
        
        return null;
      }
    });
  }
  
  static Object getVariable(String variableName) throws MatlabInvocationException
  {
    invokeAndWait(new MatlabProxy.MatlabThreadCallable()
    {
      public Object call(MatlabProxy.MatlabThreadProxy proxy)
        throws MatlabInvocationException
      {
        return proxy.getVariable(val$variableName);
      }
    });
  }
  
  static void eval(String command) throws MatlabInvocationException
  {
    invokeAndWait(new MatlabProxy.MatlabThreadCallable()
    {
      public Void call(MatlabProxy.MatlabThreadProxy proxy)
        throws MatlabInvocationException
      {
        proxy.eval(val$command);
        
        return null;
      }
    });
  }
  
  static Object[] returningEval(String command, final int nargout) throws MatlabInvocationException
  {
    (Object[])invokeAndWait(new MatlabProxy.MatlabThreadCallable()
    {
      public Object[] call(MatlabProxy.MatlabThreadProxy proxy)
        throws MatlabInvocationException
      {
        return proxy.returningEval(val$command, nargout);
      }
    });
  }
  
  static void feval(String functionName, final Object... args) throws MatlabInvocationException
  {
    invokeAndWait(new MatlabProxy.MatlabThreadCallable()
    {
      public Void call(MatlabProxy.MatlabThreadProxy proxy)
        throws MatlabInvocationException
      {
        proxy.feval(val$functionName, args);
        
        return null;
      }
    });
  }
  
  static Object[] returningFeval(String functionName, final int nargout, final Object... args)
    throws MatlabInvocationException
  {
    (Object[])invokeAndWait(new MatlabProxy.MatlabThreadCallable()
    {
      public Object[] call(MatlabProxy.MatlabThreadProxy proxy)
        throws MatlabInvocationException
      {
        return proxy.returningFeval(val$functionName, nargout, args);
      }
    });
  }
  



  static <T> T invokeAndWait(MatlabProxy.MatlabThreadCallable<T> callable)
    throws MatlabInvocationException
  {
    T result;
    

    T result;
    

    if (NativeMatlab.nativeIsMatlabThread())
    {
      try
      {
        result = callable.call(THREAD_OPERATIONS);
      }
      catch (RuntimeException e)
      {
        ThrowableWrapper cause = new ThrowableWrapper(e);
        throw MatlabInvocationException.Reason.RUNTIME_EXCEPTION.asException(cause);
      }
    }
    else if (EventQueue.isDispatchThread())
    {
      final AtomicReference<MatlabReturn<T>> returnRef = new AtomicReference();
      
      Matlab.whenMatlabIdle(new Runnable()
      {
        public void run()
        {
          JMIWrapper.MatlabReturn<T> matlabReturn;
          

          try
          {
            matlabReturn = new JMIWrapper.MatlabReturn(val$callable.call(JMIWrapper.THREAD_OPERATIONS));
          }
          catch (MatlabInvocationException e)
          {
            matlabReturn = new JMIWrapper.MatlabReturn(e);
          }
          catch (RuntimeException e)
          {
            ThrowableWrapper cause = new ThrowableWrapper(e);
            MatlabInvocationException userCausedException = MatlabInvocationException.Reason.RUNTIME_EXCEPTION.asException(cause);
            
            matlabReturn = new JMIWrapper.MatlabReturn(userCausedException);
          }
          
          returnRef.set(matlabReturn);
        }
      });
      

      try
      {
        while (returnRef.get() == null)
        {
          if (EVENT_QUEUE.peekEvent() != null)
          {
            EVENT_QUEUE_DISPATCH_METHOD.invoke(EVENT_QUEUE, new Object[] { EVENT_QUEUE.getNextEvent() });
          }
        }
      }
      catch (InterruptedException e)
      {
        throw MatlabInvocationException.Reason.EVENT_DISPATCH_THREAD.asException(e);
      }
      catch (IllegalAccessException e)
      {
        throw MatlabInvocationException.Reason.EVENT_DISPATCH_THREAD.asException(e);
      }
      catch (InvocationTargetException e)
      {
        throw MatlabInvocationException.Reason.EVENT_DISPATCH_THREAD.asException(e);
      }
      

      MatlabReturn<T> matlabReturn = (MatlabReturn)returnRef.get();
      

      if (exception != null)
      {
        throw exception;
      }
      


      result = data;

    }
    else
    {

      final ArrayBlockingQueue<MatlabReturn<T>> returnQueue = new ArrayBlockingQueue(1);
      
      Matlab.whenMatlabIdle(new Runnable()
      {
        public void run()
        {
          JMIWrapper.MatlabReturn<T> matlabReturn;
          

          try
          {
            matlabReturn = new JMIWrapper.MatlabReturn(val$callable.call(JMIWrapper.THREAD_OPERATIONS));
          }
          catch (MatlabInvocationException e)
          {
            matlabReturn = new JMIWrapper.MatlabReturn(e);
          }
          catch (RuntimeException e)
          {
            ThrowableWrapper cause = new ThrowableWrapper(e);
            MatlabInvocationException userCausedException = MatlabInvocationException.Reason.RUNTIME_EXCEPTION.asException(cause);
            
            matlabReturn = new JMIWrapper.MatlabReturn(userCausedException);
          }
          
          returnQueue.add(matlabReturn);
        }
      });
      

      try
      {
        MatlabReturn<T> matlabReturn = (MatlabReturn)returnQueue.take();
        

        if (exception != null)
        {
          throw exception;
        }
        


        result = data;

      }
      catch (InterruptedException e)
      {
        throw MatlabInvocationException.Reason.INTERRRUPTED.asException(e);
      }
    }
    
    return result;
  }
  

  private JMIWrapper() {}
  

  private static class MatlabReturn<T>
  {
    final T data;
    
    final MatlabInvocationException exception;
    

    MatlabReturn(T value)
    {
      data = value;
      exception = null;
    }
    
    MatlabReturn(MatlabInvocationException exception)
    {
      data = null;
      this.exception = exception;
    }
  }
  
  private static class MatlabThreadOperations
    implements MatlabProxy.MatlabThreadProxy
  {
    private MatlabThreadOperations() {}
    
    public void setVariable(String variableName, Object value)
      throws MatlabInvocationException
    {
      returningFeval("assignin", 0, new Object[] { "base", variableName, value });
    }
    
    public Object getVariable(String variableName)
      throws MatlabInvocationException
    {
      return returningFeval("evalin", 1, new Object[] { "base", variableName })[0];
    }
    
    public void eval(String command)
      throws MatlabInvocationException
    {
      returningFeval("evalin", 0, new Object[] { "base", command });
    }
    
    public Object[] returningEval(String command, int nargout)
      throws MatlabInvocationException
    {
      return returningFeval("evalin", nargout, new Object[] { "base", command });
    }
    
    public void feval(String functionName, Object... args)
      throws MatlabInvocationException
    {
      returningFeval(functionName, 0, args);
    }
    

    public Object[] returningFeval(String functionName, int nargout, Object... args)
      throws MatlabInvocationException
    {
      if ((args != null) && (args.length == 0))
      {
        args = null;
      }
      
      try
      {
        Object matlabResult = Matlab.mtFevalConsoleOutput(functionName, args, nargout);
        Object[] resultArray;
        Object[] resultArray;
        if (nargout == 0)
        {
          resultArray = new Object[0];
        } else { Object[] resultArray;
          if (nargout == 1)
          {
            resultArray = new Object[] { matlabResult };

          }
          else
          {
            if (matlabResult == null)
            {
              String errorMsg = "Expected " + nargout + " return arguments, instead null was returned";
              throw MatlabInvocationException.Reason.NARGOUT_MISMATCH.asException(errorMsg);
            }
            if (!matlabResult.getClass().equals([Ljava.lang.Object.class))
            {
              String errorMsg = "Expected " + nargout + " return arguments, instead 1 argument was returned";
              throw MatlabInvocationException.Reason.NARGOUT_MISMATCH.asException(errorMsg);
            }
            
            resultArray = (Object[])matlabResult;
            
            if (nargout != resultArray.length)
            {
              String errorMsg = "Expected " + nargout + " return arguments, instead " + resultArray.length + (resultArray.length == 1 ? " argument was" : " arguments were") + " returned";
              
              throw MatlabInvocationException.Reason.NARGOUT_MISMATCH.asException(errorMsg);
            }
          }
        }
        return resultArray;
      }
      catch (Exception e)
      {
        throw MatlabInvocationException.Reason.INTERNAL_EXCEPTION.asException(new ThrowableWrapper(e));
      }
    }
  }
}
