package matlabcontrol;

import java.rmi.registry.Registry;
import java.util.UUID;




































class MatlabSessionImpl
  implements MatlabSession
{
  private static final String MATLAB_SESSION_PREFIX = "MATLAB_SESSION_";
  private final String SESSION_ID = "MATLAB_SESSION_" + UUID.randomUUID().toString();
  
  MatlabSessionImpl() {}
  
  public synchronized boolean connectFromRMI(String receiverID, int port) {
    boolean success = false;
    if (MatlabConnector.isAvailableForConnection())
    {
      MatlabConnector.connect(receiverID, port, true);
      success = true;
    }
    
    return success;
  }
  





  String getSessionID()
  {
    return SESSION_ID;
  }
  








  static boolean connectToRunningSession(String receiverID, int port)
  {
    boolean establishedConnection = false;
    
    try
    {
      Registry registry = LocalHostRMIHelper.getRegistry(port);
      
      String[] remoteNames = registry.list();
      for (String name : remoteNames)
      {
        if (name.startsWith("MATLAB_SESSION_"))
        {
          MatlabSession session = (MatlabSession)registry.lookup(name);
          if (session.connectFromRMI(receiverID, port))
          {
            establishedConnection = true;
            break;
          }
        }
      }
    }
    catch (Exception e) {}
    
    return establishedConnection;
  }
}
