package matlabcontrol;

import java.rmi.Remote;
import java.rmi.RemoteException;

abstract interface RequestReceiver
  extends Remote
{
  public abstract void receiveJMIWrapper(JMIWrapperRemote paramJMIWrapperRemote, boolean paramBoolean)
    throws RemoteException;
  
  public abstract String getReceiverID()
    throws RemoteException;
  
  public abstract String getClassPathAsRMICodebase()
    throws RemoteException;
  
  public abstract String[] getClassPathAsCanonicalPaths()
    throws RemoteException;
}
