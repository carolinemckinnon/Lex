package comp6803.plainly;

import java.io.InputStream;

public class ReadStream implements Runnable
{
  String name;
  InputStream is;
  Thread thread;
  
  public ReadStream(String name, InputStream is)
  {
    this.name = name;
    this.is = is;
  }
  
  public void start() {
    thread = new Thread(this);
    thread.start();
  }
  
  public void run() {
    try {
      java.io.InputStreamReader isr = new java.io.InputStreamReader(is);
      java.io.BufferedReader br = new java.io.BufferedReader(isr);
      for (;;) {
        String s = br.readLine();
        if (s == null) break;
        System.out.println("[" + name + "] " + s);
      }
      is.close();
    } catch (Exception ex) {
      System.out.println("Problem reading stream " + name + "... :" + ex);
      ex.printStackTrace();
    }
  }
}
