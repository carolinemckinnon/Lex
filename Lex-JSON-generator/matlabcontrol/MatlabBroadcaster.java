package matlabcontrol;

import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Timer;
import java.util.TimerTask;

































class MatlabBroadcaster
{
  private static Registry _registry = null;
  



  private static final MatlabSessionImpl _session = new MatlabSessionImpl();
  



  private static final int BROADCAST_CHECK_PERIOD = 1000;
  



  private static final Timer _broadcastTimer = new Timer("MLC Broadcast Maintainer");
  




  private MatlabBroadcaster() {}
  




  static MatlabSessionImpl getSession()
  {
    return _session;
  }
  






  static synchronized void broadcast(int broadcastPort)
    throws MatlabConnectionException
  {
    if (_registry == null)
    {

      setupRegistry(broadcastPort);
      

      bindSession();
      

      maintainRegistryConnection(broadcastPort);
    }
  }
  




  private static void setupRegistry(int broadcastPort)
    throws MatlabConnectionException
  {
    try
    {
      _registry = LocalHostRMIHelper.createRegistry(broadcastPort);

    }
    catch (Exception e)
    {
      try
      {
        _registry = LocalHostRMIHelper.getRegistry(broadcastPort);
      }
      catch (Exception ex)
      {
        throw new MatlabConnectionException("Could not create or connect to the RMI registry", ex);
      }
    }
  }
  





  private static void bindSession()
    throws MatlabConnectionException
  {
    try
    {
      UnicastRemoteObject.unexportObject(_session, true);
    }
    catch (NoSuchObjectException e) {}
    
    try
    {
      _registry.bind(_session.getSessionID(), LocalHostRMIHelper.exportObject(_session));
    }
    catch (Exception e)
    {
      throw new MatlabConnectionException("Could not register this session of MATLAB", e);
    }
  }
  





  private static void maintainRegistryConnection(int broadcastPort)
  {
    _broadcastTimer.schedule(new TimerTask()
    {

      public void run()
      {

        try
        {

          MatlabBroadcaster._registry.lookup(MatlabBroadcaster._session.getSessionID());

        }
        catch (NotBoundException e)
        {
          try
          {
            MatlabBroadcaster.access$200();

          }
          catch (MatlabConnectionException ex) {}

        }
        catch (RemoteException e)
        {
          try
          {
            MatlabBroadcaster.setupRegistry(val$broadcastPort);
            MatlabBroadcaster.access$200(); } catch (MatlabConnectionException ex) {} } } }, 1000L, 1000L);
  }
}
