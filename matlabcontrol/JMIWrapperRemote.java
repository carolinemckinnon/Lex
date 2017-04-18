package matlabcontrol;

import java.rmi.Remote;
import java.rmi.RemoteException;

abstract interface JMIWrapperRemote
  extends Remote
{
  public abstract void exit()
    throws RemoteException;
  
  public abstract void setVariable(String paramString, Object paramObject)
    throws RemoteException, MatlabInvocationException;
  
  public abstract Object getVariable(String paramString)
    throws RemoteException, MatlabInvocationException;
  
  public abstract void eval(String paramString)
    throws RemoteException, MatlabInvocationException;
  
  public abstract Object[] returningEval(String paramString, int paramInt)
    throws RemoteException, MatlabInvocationException;
  
  public abstract void feval(String paramString, Object[] paramArrayOfObject)
    throws RemoteException, MatlabInvocationException;
  
  public abstract Object[] returningFeval(String paramString, int paramInt, Object... paramVarArgs)
    throws RemoteException, MatlabInvocationException;
  
  public abstract <U> U invokeAndWait(MatlabProxy.MatlabThreadCallable<U> paramMatlabThreadCallable)
    throws RemoteException, MatlabInvocationException;
  
  public abstract void checkConnection()
    throws RemoteException;
}
