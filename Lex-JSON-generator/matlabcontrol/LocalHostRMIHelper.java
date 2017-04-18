package matlabcontrol;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;






























class LocalHostRMIHelper
{
  private static final LocalHostRMISocketFactory SOCKET_FACTORY = new LocalHostRMISocketFactory(null);
  
  LocalHostRMIHelper() {}
  
  public static Registry getRegistry(int port) throws RemoteException { return LocateRegistry.getRegistry("localhost", port, SOCKET_FACTORY); }
  
  public static Registry createRegistry(int port)
    throws RemoteException
  {
    return LocateRegistry.createRegistry(port, SOCKET_FACTORY, SOCKET_FACTORY);
  }
  
  public static Remote exportObject(Remote object) throws RemoteException
  {
    return UnicastRemoteObject.exportObject(object, 0, SOCKET_FACTORY, SOCKET_FACTORY);
  }
  
  private static class LocalHostRMISocketFactory implements RMIClientSocketFactory, RMIServerSocketFactory, Serializable
  {
    private LocalHostRMISocketFactory() {}
    
    public Socket createSocket(String host, int port) throws IOException {
      return SocketFactory.getDefault().createSocket(InetAddress.getByName("localhost"), port);
    }
    
    public ServerSocket createServerSocket(int port)
      throws IOException
    {
      return ServerSocketFactory.getDefault().createServerSocket(port, 1, InetAddress.getByName("localhost"));
    }
    

    public boolean equals(Object o)
    {
      return o instanceof LocalHostRMISocketFactory;
    }
    

    public int hashCode()
    {
      return 5;
    }
    






    public String toString()
    {
      return "MLC localhost Socket Factory";
    }
  }
  
  static class LocalHostRemoteObject extends UnicastRemoteObject
  {
    LocalHostRemoteObject() throws RemoteException
    {
      super(LocalHostRMIHelper.SOCKET_FACTORY, LocalHostRMIHelper.SOCKET_FACTORY);
    }
  }
}
