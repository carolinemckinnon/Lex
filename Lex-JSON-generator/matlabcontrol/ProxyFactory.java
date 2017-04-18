package matlabcontrol;

abstract interface ProxyFactory
{
  public abstract MatlabProxy getProxy()
    throws MatlabConnectionException;
  
  public abstract MatlabProxyFactory.Request requestProxy(MatlabProxyFactory.RequestCallback paramRequestCallback)
    throws MatlabConnectionException;
}
