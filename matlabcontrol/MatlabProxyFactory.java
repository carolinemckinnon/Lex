package matlabcontrol;

























public class MatlabProxyFactory
  implements ProxyFactory
{
  private final ProxyFactory _delegateFactory;
  























  public MatlabProxyFactory()
  {
    this(new MatlabProxyFactoryOptions.Builder().build());
  }
  






  public MatlabProxyFactory(MatlabProxyFactoryOptions options)
  {
    if (Configuration.isRunningInsideMatlab())
    {
      _delegateFactory = new LocalMatlabProxyFactory(options);
    }
    else
    {
      _delegateFactory = new RemoteMatlabProxyFactory(options);
    }
  }
  
  public MatlabProxy getProxy()
    throws MatlabConnectionException
  {
    return _delegateFactory.getProxy();
  }
  
  public Request requestProxy(RequestCallback callback)
    throws MatlabConnectionException
  {
    if (callback == null)
    {
      throw new NullPointerException("The request callback may not be null");
    }
    
    return _delegateFactory.requestProxy(callback);
  }
  
  public static abstract interface Request
  {
    public abstract MatlabProxy.Identifier getProxyIdentifer();
    
    public abstract boolean cancel();
    
    public abstract boolean isCancelled();
    
    public abstract boolean isCompleted();
  }
  
  public static abstract interface RequestCallback
  {
    public abstract void proxyCreated(MatlabProxy paramMatlabProxy);
  }
}
