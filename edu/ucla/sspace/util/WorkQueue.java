package edu.ucla.sspace.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

















































































public class WorkQueue
{
  private final List<Thread> threads;
  private final BlockingQueue<Runnable> workQueue;
  private final ConcurrentMap<Object, CountDownLatch> taskKeyToLatch;
  private static WorkQueue singleton;
  
  WorkQueue()
  {
    this(Runtime.getRuntime().availableProcessors());
  }
  



  WorkQueue(int numThreads)
  {
    workQueue = new LinkedBlockingQueue();
    threads = new ArrayList();
    taskKeyToLatch = new ConcurrentHashMap();
    for (int i = 0; i < numThreads; i++) {
      Thread t = new WorkerThread(workQueue);
      threads.add(t);
      t.start();
    }
  }
  










  public void add(Object taskGroupId, Runnable task)
  {
    CountDownLatch latch = (CountDownLatch)taskKeyToLatch.get(taskGroupId);
    if (latch == null)
      throw new IllegalArgumentException(
        "Unknown task id: " + taskGroupId);
    if (task == null)
      throw new NullPointerException("Cannot add null tasks");
    workQueue.offer(new CountingRunnable(task, latch));
  }
  


  private void addThread()
  {
    Thread t = new WorkerThread(workQueue);
    threads.add(t);
    t.start();
  }
  








  public void await(Object taskGroupId)
  {
    CountDownLatch latch = (CountDownLatch)taskKeyToLatch.get(taskGroupId);
    if (latch == null)
      throw new IllegalArgumentException(
        "Unknown task group: " + taskGroupId);
    try {
      while (!latch.await(5L, TimeUnit.SECONDS)) {}
      


      taskKeyToLatch.remove(taskGroupId);
    }
    catch (InterruptedException ie) {
      throw new IllegalStateException("Not all tasks finished", ie);
    }
  }
  








  public boolean await(Object taskGroupId, long timeout, TimeUnit unit)
  {
    CountDownLatch latch = (CountDownLatch)taskKeyToLatch.get(taskGroupId);
    if (latch == null)
      throw new IllegalArgumentException(
        "Unknown task group: " + taskGroupId);
    try {
      if (latch.await(timeout, unit))
      {

        taskKeyToLatch.remove(taskGroupId);
        return true;
      }
      return false;
    }
    catch (InterruptedException ie) {
      throw new IllegalStateException("Not all tasks finished", ie);
    }
  }
  










  public long getRemainingTasks(Object taskGroupId)
  {
    CountDownLatch latch = (CountDownLatch)taskKeyToLatch.get(taskGroupId);
    return latch == null ? 
      0L : 
      latch.getCount();
  }
  



  public static WorkQueue getWorkQueue()
  {
    if (singleton == null) {
      synchronized (WorkQueue.class) {
        if (singleton == null)
        {
          singleton = new WorkQueue(); }
      }
    }
    return singleton;
  }
  




  public static WorkQueue getWorkQueue(int numThreads)
  {
    if (singleton == null) {
      synchronized (WorkQueue.class) {
        if (singleton == null) {
          singleton = new WorkQueue(numThreads);
        }
      }
    }
    while (singleton.availableThreads() < numThreads) {
      singleton.addThread();
    }
    return singleton;
  }
  








  public Object registerTaskGroup(int numTasks)
  {
    Object key = new Object();
    taskKeyToLatch.putIfAbsent(key, new CountDownLatch(numTasks));
    return key;
  }
  













  public boolean registerTaskGroup(Object taskGroupId, int numTasks)
  {
    return taskKeyToLatch
      .putIfAbsent(taskGroupId, new CountDownLatch(numTasks)) == null;
  }
  






  public void run(Runnable... tasks)
  {
    run(Arrays.asList(tasks));
  }
  







  public void run(Collection<Runnable> tasks)
  {
    int numTasks = tasks.size();
    CountDownLatch latch = new CountDownLatch(numTasks);
    for (Runnable r : tasks) {
      if (r == null)
        throw new NullPointerException("Cannot run null tasks");
      workQueue.offer(new CountingRunnable(r, latch));
    }
    try
    {
      latch.await();
    }
    catch (InterruptedException ie) {
      throw new IllegalStateException("Not all tasks finished", ie);
    }
  }
  



  public int availableThreads()
  {
    return threads.size();
  }
  



  private static class CountingRunnable
    implements Runnable
  {
    private final Runnable task;
    


    private final CountDownLatch latch;
    



    public CountingRunnable(Runnable task, CountDownLatch latch)
    {
      this.task = task;
      this.latch = latch;
    }
    

    public void run()
    {
      try
      {
        task.run();
      }
      finally {
        latch.countDown();
      }
    }
  }
}
