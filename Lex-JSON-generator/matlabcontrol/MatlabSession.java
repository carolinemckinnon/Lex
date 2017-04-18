package matlabcontrol;

import java.rmi.Remote;
import java.rmi.RemoteException;

abstract interface MatlabSession
  extends Remote
{
  public abstract boolean connectFromRMI(String paramString, int paramInt)
    throws RemoteException;
}
