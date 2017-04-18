package matlabcontrol;

public abstract interface MatlabOperations
{
  public abstract void eval(String paramString)
    throws MatlabInvocationException;
  
  public abstract Object[] returningEval(String paramString, int paramInt)
    throws MatlabInvocationException;
  
  public abstract void feval(String paramString, Object... paramVarArgs)
    throws MatlabInvocationException;
  
  public abstract Object[] returningFeval(String paramString, int paramInt, Object... paramVarArgs)
    throws MatlabInvocationException;
  
  public abstract void setVariable(String paramString, Object paramObject)
    throws MatlabInvocationException;
  
  public abstract Object getVariable(String paramString)
    throws MatlabInvocationException;
}
