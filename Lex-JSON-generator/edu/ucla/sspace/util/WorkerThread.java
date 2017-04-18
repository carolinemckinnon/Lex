package edu.ucla.sspace.util;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;



















































public class WorkerThread
  extends Thread
{
  private static int threadInstanceCount;
  private final BlockingQueue<Runnable> workQueue;
  private final Queue<Runnable> internalQueue;
  private final int threadLocalItems;
  
  public WorkerThread(BlockingQueue<Runnable> workQueue)
  {
    this(workQueue, 1);
  }
  










  public WorkerThread(BlockingQueue<Runnable> workQueue, int threadLocalItems)
  {
    this.workQueue = workQueue;
    this.threadLocalItems = threadLocalItems;
    internalQueue = new ArrayDeque();
    setDaemon(true);
    synchronized (WorkerThread.class) {
      setName("WorkerThread-" + threadInstanceCount++);
    }
  }
  



  public void run()
  {
    Runnable r = null;
    for (;; 
        










        (r = (Runnable)internalQueue.poll()) != null) {
      if (workQueue.drainTo(internalQueue, threadLocalItems) == 0)
      {
        try {
          internalQueue.offer((Runnable)workQueue.take());
        } catch (InterruptedException ie) {
          throw new Error(ie);
        }
        


        r.run();
      }
    }
  }
}
