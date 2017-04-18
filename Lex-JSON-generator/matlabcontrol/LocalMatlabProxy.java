package matlabcontrol;



































class LocalMatlabProxy
  extends MatlabProxy
{
  private volatile boolean _isConnected = true;
  
  LocalMatlabProxy(MatlabProxy.Identifier id)
  {
    super(id, true);
  }
  

  public boolean isRunningInsideMatlab()
  {
    return true;
  }
  

  public boolean isConnected()
  {
    return _isConnected;
  }
  

  public boolean disconnect()
  {
    _isConnected = false;
    

    notifyDisconnectionListeners();
    
    return true;
  }
  


  public void exit()
    throws MatlabInvocationException
  {
    if (isConnected())
    {
      JMIWrapper.exit();
    }
    else
    {
      throw MatlabInvocationException.Reason.PROXY_NOT_CONNECTED.asException();
    }
  }
  
  public void eval(String command)
    throws MatlabInvocationException
  {
    if (isConnected())
    {
      JMIWrapper.eval(command);
    }
    else
    {
      throw MatlabInvocationException.Reason.PROXY_NOT_CONNECTED.asException();
    }
  }
  
  public Object[] returningEval(String command, int nargout)
    throws MatlabInvocationException
  {
    if (isConnected())
    {
      return JMIWrapper.returningEval(command, nargout);
    }
    

    throw MatlabInvocationException.Reason.PROXY_NOT_CONNECTED.asException();
  }
  

  public void feval(String functionName, Object... args)
    throws MatlabInvocationException
  {
    if (isConnected())
    {
      JMIWrapper.feval(functionName, args);
    }
    else
    {
      throw MatlabInvocationException.Reason.PROXY_NOT_CONNECTED.asException();
    }
  }
  
  public Object[] returningFeval(String functionName, int nargout, Object... args)
    throws MatlabInvocationException
  {
    if (isConnected())
    {
      return JMIWrapper.returningFeval(functionName, nargout, args);
    }
    

    throw MatlabInvocationException.Reason.PROXY_NOT_CONNECTED.asException();
  }
  

  public void setVariable(String variableName, Object value)
    throws MatlabInvocationException
  {
    if (isConnected())
    {
      JMIWrapper.setVariable(variableName, value);
    }
    else
    {
      throw MatlabInvocationException.Reason.PROXY_NOT_CONNECTED.asException();
    }
  }
  
  public Object getVariable(String variableName)
    throws MatlabInvocationException
  {
    if (isConnected())
    {
      return JMIWrapper.getVariable(variableName);
    }
    

    throw MatlabInvocationException.Reason.PROXY_NOT_CONNECTED.asException();
  }
  

  public <T> T invokeAndWait(MatlabProxy.MatlabThreadCallable<T> callable)
    throws MatlabInvocationException
  {
    if (isConnected())
    {
      return JMIWrapper.invokeAndWait(callable);
    }
    

    throw MatlabInvocationException.Reason.PROXY_NOT_CONNECTED.asException();
  }
}
