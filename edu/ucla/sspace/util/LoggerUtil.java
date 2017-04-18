package edu.ucla.sspace.util;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;






















































public final class LoggerUtil
{
  private LoggerUtil() {}
  
  public static void setLevel(Level outputLevel)
  {
    Logger appRooLogger = Logger.getLogger("edu.ucla.sspace");
    Handler verboseHandler = new ConsoleHandler();
    verboseHandler.setLevel(outputLevel);
    appRooLogger.addHandler(verboseHandler);
    appRooLogger.setLevel(outputLevel);
    appRooLogger.setUseParentHandlers(false);
  }
  









  public static void setLevel(String loggerNamespace, Level outputLevel)
  {
    Logger appRooLogger = Logger.getLogger(loggerNamespace);
    Handler verboseHandler = new ConsoleHandler();
    verboseHandler.setLevel(outputLevel);
    appRooLogger.addHandler(verboseHandler);
    appRooLogger.setLevel(outputLevel);
    appRooLogger.setUseParentHandlers(false);
  }
  


  public static void veryVerbose(Logger log, String format, Object... args)
  {
    if (log.isLoggable(Level.FINER)) {
      StackTraceElement[] callStack = 
        Thread.currentThread().getStackTrace();
      



      StackTraceElement caller = callStack[2];
      String callingClass = caller.getClassName();
      String callingMethod = caller.getMethodName();
      log.logp(Level.FINER, callingClass, callingMethod, 
        String.format(format, args));
    }
  }
  


  public static void verbose(Logger log, String format, Object... args)
  {
    if (log.isLoggable(Level.FINE)) {
      StackTraceElement[] callStack = 
        Thread.currentThread().getStackTrace();
      



      StackTraceElement caller = callStack[2];
      String callingClass = caller.getClassName();
      String callingMethod = caller.getMethodName();
      log.logp(Level.FINE, callingClass, callingMethod, 
        String.format(format, args));
    }
  }
  


  public static void info(Logger log, String format, Object... args)
  {
    if (log.isLoggable(Level.INFO)) {
      StackTraceElement[] callStack = 
        Thread.currentThread().getStackTrace();
      



      StackTraceElement caller = callStack[2];
      String callingClass = caller.getClassName();
      String callingMethod = caller.getMethodName();
      log.logp(Level.INFO, callingClass, callingMethod, 
        String.format(format, args));
    }
  }
  


  public static void warning(Logger log, String format, Object... args)
  {
    if (log.isLoggable(Level.WARNING)) {
      log.warning(String.format(format, args));
    }
  }
  

  public static void severe(Logger log, String format, Object... args)
  {
    if (log.isLoggable(Level.SEVERE)) {
      log.severe(String.format(format, args));
    }
  }
}
