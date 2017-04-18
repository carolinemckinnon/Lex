package matlabcontrol;















class ThrowableWrapper
  extends Throwable
{
  private static final long serialVersionUID = 50432L;
  













  private final String _toString;
  













  ThrowableWrapper(Throwable innerThrowable)
  {
    _toString = innerThrowable.toString();
    

    setStackTrace(innerThrowable.getStackTrace());
    

    if (innerThrowable.getCause() != null)
    {
      initCause(new ThrowableWrapper(innerThrowable.getCause()));
    }
  }
  

  public String toString()
  {
    return _toString;
  }
}
