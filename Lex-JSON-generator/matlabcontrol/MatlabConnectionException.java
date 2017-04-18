package matlabcontrol;















public class MatlabConnectionException
  extends Exception
{
  private static final long serialVersionUID = 41984L;
  














  MatlabConnectionException(String msg)
  {
    super(msg);
  }
  
  MatlabConnectionException(String msg, Throwable cause)
  {
    super(msg, cause);
  }
}
