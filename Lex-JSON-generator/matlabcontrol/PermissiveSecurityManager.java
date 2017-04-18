package matlabcontrol;

import java.security.Permission;

public class PermissiveSecurityManager
  extends SecurityManager
{
  public PermissiveSecurityManager() {}
  
  public void checkPermission(Permission perm) {}
  
  public void checkPermission(Permission perm, Object context) {}
}
