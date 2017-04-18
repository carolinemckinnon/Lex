package matlabcontrol;

import java.rmi.RemoteException;



































class JMIWrapperRemoteImpl
  extends LocalHostRMIHelper.LocalHostRemoteObject
  implements JMIWrapperRemote
{
  public JMIWrapperRemoteImpl()
    throws RemoteException
  {}
  
  public void exit() {}
  
  public void eval(String command)
    throws MatlabInvocationException
  {
    JMIWrapper.eval(command);
  }
  
  public Object[] returningEval(String command, int nargout)
    throws MatlabInvocationException
  {
    return JMIWrapper.returningEval(command, nargout);
  }
  
  public void feval(String command, Object... args)
    throws MatlabInvocationException
  {
    JMIWrapper.feval(command, args);
  }
  
  public Object[] returningFeval(String command, int nargout, Object... args)
    throws MatlabInvocationException
  {
    return JMIWrapper.returningFeval(command, nargout, args);
  }
  
  public void setVariable(String variableName, Object value)
    throws MatlabInvocationException
  {
    JMIWrapper.setVariable(variableName, value);
  }
  
  public Object getVariable(String variableName)
    throws MatlabInvocationException
  {
    return JMIWrapper.getVariable(variableName);
  }
  
  public <T> T invokeAndWait(MatlabProxy.MatlabThreadCallable<T> callable)
    throws MatlabInvocationException
  {
    return JMIWrapper.invokeAndWait(callable);
  }
  
  public void checkConnection() {}
}
