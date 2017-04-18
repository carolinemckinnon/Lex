package matlabcontrol;

import java.util.concurrent.CopyOnWriteArrayList;
























































































































































































public abstract class MatlabProxy
  implements MatlabOperations
{
  private final Identifier _id;
  private final boolean _existingSession;
  private final CopyOnWriteArrayList<DisconnectionListener> _listeners;
  
  MatlabProxy(Identifier id, boolean existingSession)
  {
    _id = id;
    _existingSession = existingSession;
    
    _listeners = new CopyOnWriteArrayList();
  }
  





  public Identifier getIdentifier()
  {
    return _id;
  }
  






  public boolean isExistingSession()
  {
    return _existingSession;
  }
  







  public String toString()
  {
    return "[" + getClass().getName() + " identifier=" + getIdentifier() + "," + " connected=" + isConnected() + "," + " insideMatlab=" + isRunningInsideMatlab() + "," + " existingSession=" + isExistingSession() + "]";
  }
  










  public void addDisconnectionListener(DisconnectionListener listener)
  {
    _listeners.add(listener);
  }
  





  public void removeDisconnectionListener(DisconnectionListener listener)
  {
    _listeners.remove(listener);
  }
  



  void notifyDisconnectionListeners()
  {
    for (DisconnectionListener listener : _listeners)
    {
      listener.proxyDisconnected(this);
    }
  }
  
  public abstract boolean isRunningInsideMatlab();
  
  public abstract boolean isConnected();
  
  public abstract boolean disconnect();
  
  public abstract void exit()
    throws MatlabInvocationException;
  
  public abstract <T> T invokeAndWait(MatlabThreadCallable<T> paramMatlabThreadCallable)
    throws MatlabInvocationException;
  
  public static abstract interface Identifier
  {
    public abstract boolean equals(Object paramObject);
    
    public abstract int hashCode();
  }
  
  public static abstract interface DisconnectionListener
  {
    public abstract void proxyDisconnected(MatlabProxy paramMatlabProxy);
  }
  
  public static abstract interface MatlabThreadProxy
    extends MatlabOperations
  {}
  
  public static abstract interface MatlabThreadCallable<T>
  {
    public abstract T call(MatlabProxy.MatlabThreadProxy paramMatlabThreadProxy)
      throws MatlabInvocationException;
  }
}
