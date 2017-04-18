package edu.ucla.sspace.tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintStream;























public class ConvertCorpusToOneSentencePerLine
{
  public ConvertCorpusToOneSentencePerLine() {}
  
  public static void main(String[] args)
  {
    if (args.length == 0) {
      System.out.println("usage: java <class name> <input file>");
      return;
    }
    try
    {
      BufferedReader br = new BufferedReader(new FileReader(args[0]));
      StringBuilder sb = new StringBuilder();
      boolean inQuotation = false;
      int j; int i; for (String line = null; (line = br.readLine()) != null; 
          
          i < j)
      {
        String[] tokens = line.split("\\s+");
        String[] arrayOfString1; j = (arrayOfString1 = tokens).length;i = 0; continue;String token = arrayOfString1[i];
        
        if (token.startsWith("\"")) {
          inQuotation = true;
        }
        
        if (token.endsWith(".\"")) {
          inQuotation = false;
        }
        

        if (((!inQuotation) && 
          (token.endsWith("!"))) || 
          (token.endsWith("?")) || 
          (token.endsWith(".\"")) || (
          (token.endsWith(".")) && 
          (!isInitial(token)) && 
          (!isAbbreviation(token))))
        {

          sb.append(token);
          System.out.println(sb.toString());
          sb = new StringBuilder();
        }
        else {
          sb.append(token).append(" ");
        }
        i++;








      }
      









    }
    catch (Throwable t)
    {








      t.printStackTrace();
    }
  }
  
  public static boolean isInitial(String token) {
    if (token.length() == 2)
    {
      if (token.toUpperCase().equals(token))
        return true; } return false;
  }
  

  public static boolean isAbbreviation(String token)
  {
    String s = token.toLowerCase();
    

    if ((!s.equals("mr.")) && 
      (!s.equals("ms.")) && 
      (!s.equals("mrs.")) && 
      (!s.equals("dr.")) && 
      (!s.equals("drs.")) && 
      (!s.equals("fr.")))
    {

      if ((!s.equals("jr.")) && 
        (!s.equals("sr.")))
      {

        if ((!s.equals("st.")) && 
          (!s.equals("ave.")) && 
          (!s.equals("abbr.")))
        {

          if ((!s.equals("i.e.")) && 
            (!s.equals("e.g.")))
          {

            if ((!s.equals("jan.")) && 
              (!s.equals("feb.")) && 
              (!s.equals("apr.")) && 
              (!s.equals("jun.")) && 
              (!s.equals("jul.")) && 
              (!s.equals("aug.")) && 
              (!s.equals("sep.")) && 
              (!s.equals("sept.")) && 
              (!s.equals("oct.")) && 
              (!s.equals("nov.")) && 
              (!s.equals("desc.")))
            {

              if ((!s.equals("b.a.")) && 
                (!s.equals("b.s.")) && 
                (!s.equals("m.s.")) && 
                (!s.equals("ph.d.")))
              {

                if ((!s.equals("in.")) && 
                  (!s.equals("ft.")) && 
                  (!s.equals("lbs.")) && 
                  (!s.equals("gal.")) && 
                  (!s.equals("min.")))
                {

                  if ((!s.equals("assn.")) && 
                    (!s.equals("c.")) && 
                    (!s.equals("corp.")) && 
                    (!s.equals("col.")) && 
                    (!s.equals("cpl.")) && 
                    (!s.equals("d.")) && 
                    (!s.equals("dist.")) && 
                    (!s.equals("inst.")) && 
                    (!s.equals("lt.")) && 
                    (!s.equals("msgr.")) && 
                    (!s.equals("pl.")) && 
                    (!s.equals("vol.")) && 
                    (!s.equals("wt.")))
                  {

                    if ((!s.equals("d.c.")) && 
                      (!s.equals("n.y.c.")))
                      return false; } } } } } } } } return true;
  }
}
