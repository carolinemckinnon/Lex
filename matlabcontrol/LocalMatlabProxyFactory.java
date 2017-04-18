package matlabcontrol;

import java.util.concurrent.atomic.AtomicInteger;

































class LocalMatlabProxyFactory
  implements ProxyFactory
{
  public LocalMatlabProxyFactory(MatlabProxyFactoryOptions options) {}
  
  public LocalMatlabProxy getProxy()
    throws MatlabConnectionException
  {
    JMIValidator.validateJMIMethods();
    
    return new LocalMatlabProxy(new LocalIdentifier(null));
  }
  
  public MatlabProxyFactory.Request requestProxy(MatlabProxyFactory.RequestCallback requestCallback)
    throws MatlabConnectionException
  {
    LocalMatlabProxy proxy = getProxy();
    requestCallback.proxyCreated(proxy);
    
    return new LocalRequest(proxy.getIdentifier(), null);
  }
  
  private static final class LocalIdentifier implements MatlabProxy.Identifier
  {
    private static final AtomicInteger PROXY_CREATION_COUNTER = new AtomicInteger();
    
    private final int _id = PROXY_CREATION_COUNTER.getAndIncrement();
    
    private LocalIdentifier() {}
    
    public boolean equals(Object other) {
      boolean equals;
      boolean equals;
      if ((other instanceof LocalIdentifier))
      {
        equals = _id == _id;
      }
      else
      {
        equals = false;
      }
      
      return equals;
    }
    

    public int hashCode()
    {
      return _id;
    }
    

    public String toString()
    {
      return "PROXY_LOCAL_" + _id;
    }
  }
  
  private static final class LocalRequest implements MatlabProxyFactory.Request
  {
    private final MatlabProxy.Identifier _proxyID;
    
    private LocalRequest(MatlabProxy.Identifier proxyID)
    {
      _proxyID = proxyID;
    }
    

    public MatlabProxy.Identifier getProxyIdentifer()
    {
      return _proxyID;
    }
    

    public boolean cancel()
    {
      return false;
    }
    

    public boolean isCancelled()
    {
      return false;
    }
    

    public boolean isCompleted()
    {
      return true;
    }
  }
}
