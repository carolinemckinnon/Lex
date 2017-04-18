package org.tartarus.snowball.ext;

import org.tartarus.snowball.Among;
import org.tartarus.snowball.SnowballStemmer;







public class germanStemmer
  extends SnowballStemmer
{
  private static final long serialVersionUID = 1L;
  private static final germanStemmer methodObject = new germanStemmer();
  
  private static final Among[] a_0 = {
    new Among("", -1, 6, "", methodObject), 
    new Among("U", 0, 2, "", methodObject), 
    new Among("Y", 0, 1, "", methodObject), 
    new Among("ä", 0, 3, "", methodObject), 
    new Among("ö", 0, 4, "", methodObject), 
    new Among("ü", 0, 5, "", methodObject) };
  

  private static final Among[] a_1 = {
    new Among("e", -1, 2, "", methodObject), 
    new Among("em", -1, 1, "", methodObject), 
    new Among("en", -1, 2, "", methodObject), 
    new Among("ern", -1, 1, "", methodObject), 
    new Among("er", -1, 1, "", methodObject), 
    new Among("s", -1, 3, "", methodObject), 
    new Among("es", 5, 2, "", methodObject) };
  

  private static final Among[] a_2 = {
    new Among("en", -1, 1, "", methodObject), 
    new Among("er", -1, 1, "", methodObject), 
    new Among("st", -1, 2, "", methodObject), 
    new Among("est", 2, 1, "", methodObject) };
  

  private static final Among[] a_3 = {
    new Among("ig", -1, 1, "", methodObject), 
    new Among("lich", -1, 1, "", methodObject) };
  

  private static final Among[] a_4 = {
    new Among("end", -1, 1, "", methodObject), 
    new Among("ig", -1, 2, "", methodObject), 
    new Among("ung", -1, 1, "", methodObject), 
    new Among("lich", -1, 3, "", methodObject), 
    new Among("isch", -1, 2, "", methodObject), 
    new Among("ik", -1, 2, "", methodObject), 
    new Among("heit", -1, 3, "", methodObject), 
    new Among("keit", -1, 4, "", methodObject) };
  

  private static final char[] g_v = { '\021', 'A', '\020', '\001', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\b', '\000', ' ', '\b' };
  
  private static final char[] g_s_ending = { 'u', '\036', '\005' };
  
  private static final char[] g_st_ending = { 'u', '\036', '\004' };
  private int I_x;
  private int I_p2;
  private int I_p1;
  
  public germanStemmer() {}
  
  private void copy_from(germanStemmer other) { I_x = I_x;
    I_p2 = I_p2;
    I_p1 = I_p1;
    super.copy_from(other);
  }
  







  private boolean r_prelude()
  {
    int v_1 = cursor;
    int v_2;
    for (;;)
    {
      v_2 = cursor;
      



      int v_3 = cursor;
      


      bra = cursor;
      
      if (eq_s(1, "ß"))
      {



        ket = cursor;
        
        slice_from("ss");
      }
      else {
        cursor = v_3;
        
        if (cursor >= limit) {
          break;
        }
        
        cursor += 1;
      }
    }
    
    cursor = v_2;
    

    cursor = v_1;
    


    int v_4 = cursor;
    

    for (;;)
    {
      int v_5 = cursor;
      

      if (in_grouping(g_v, 97, 252))
      {



        bra = cursor;
        

        int v_6 = cursor;
        


        if (eq_s(1, "u"))
        {



          ket = cursor;
          if (in_grouping(g_v, 97, 252))
          {



            slice_from("U");
            break label238;
          } }
        cursor = v_6;
        

        if (eq_s(1, "y"))
        {



          ket = cursor;
          if (in_grouping(g_v, 97, 252))
          {



            slice_from("Y");
            label238:
            cursor = v_5;
            break;
          } } }
      cursor = v_5;
      if (cursor >= limit) {
        break label280;
      }
      
      cursor += 1;
    }
    label280:
    int v_5;
    cursor = v_4;
    

    return true;
  }
  

  private boolean r_mark_regions()
  {
    I_p1 = limit;
    I_p2 = limit;
    
    int v_1 = cursor;
    


    int c = cursor + 3;
    if ((c < 0) || (c > limit))
    {
      return false;
    }
    cursor = c;
    

    I_x = cursor;
    cursor = v_1;
    



    while (!in_grouping(g_v, 97, 252))
    {




      if (cursor >= limit)
      {
        return false;
      }
      cursor += 1;
    }
    



    while (!out_grouping(g_v, 97, 252))
    {




      if (cursor >= limit)
      {
        return false;
      }
      cursor += 1;
    }
    
    I_p1 = cursor;
    


    if (I_p1 < I_x)
    {


      I_p1 = I_x;
    }
    



    while (!in_grouping(g_v, 97, 252))
    {




      if (cursor >= limit)
      {
        return false;
      }
      cursor += 1;
    }
    



    while (!out_grouping(g_v, 97, 252))
    {




      if (cursor >= limit)
      {
        return false;
      }
      cursor += 1;
    }
    
    I_p2 = cursor;
    return true;
  }
  

  private boolean r_postlude()
  {
    int v_1;
    for (;;)
    {
      v_1 = cursor;
      


      bra = cursor;
      
      int among_var = find_among(a_0, 6);
      if (among_var == 0) {
        break;
      }
      

      ket = cursor;
      switch (among_var)
      {
      case 0: 
        break;
      
      case 1: 
        slice_from("y");
        break;
      

      case 2: 
        slice_from("u");
        break;
      

      case 3: 
        slice_from("a");
        break;
      

      case 4: 
        slice_from("o");
        break;
      

      case 5: 
        slice_from("u");
        break;
      

      case 6: 
        if (cursor >= limit) {
          break label155;
        }
        
        cursor += 1;
      }
      
    }
    label155:
    cursor = v_1;
    

    return true;
  }
  
  private boolean r_R1() {
    if (I_p1 > cursor)
    {
      return false;
    }
    return true;
  }
  
  private boolean r_R2() {
    if (I_p2 > cursor)
    {
      return false;
    }
    return true;
  }
  












  private boolean r_standard_suffix()
  {
    int v_1 = limit - cursor;
    


    ket = cursor;
    
    int among_var = find_among_b(a_1, 7);
    if (among_var != 0)
    {



      bra = cursor;
      
      if (r_R1())
      {


        switch (among_var)
        {
        case 0: 
          break;
        
        case 1: 
          slice_del();
          break;
        

        case 2: 
          slice_del();
          
          int v_2 = limit - cursor;
          


          ket = cursor;
          
          if (!eq_s_b(1, "s"))
          {
            cursor = (limit - v_2);
          }
          else
          {
            bra = cursor;
            
            if (!eq_s_b(3, "nis"))
            {
              cursor = (limit - v_2);
            }
            else
            {
              slice_del(); }
          }
          break;
        
        case 3: 
          if (in_grouping_b(g_s_ending, 98, 116))
          {



            slice_del(); }
          break;
        } }
    }
    cursor = (limit - v_1);
    
    int v_3 = limit - cursor;
    


    ket = cursor;
    
    among_var = find_among_b(a_2, 4);
    if (among_var != 0)
    {



      bra = cursor;
      
      if (r_R1())
      {


        switch (among_var)
        {
        case 0: 
          break;
        
        case 1: 
          slice_del();
          break;
        
        case 2: 
          if (in_grouping_b(g_st_ending, 98, 116))
          {




            int c = cursor - 3;
            if ((limit_backward <= c) && (c <= limit))
            {


              cursor = c;
              

              slice_del();
            }
          }
          break; } } }
    cursor = (limit - v_3);
    
    int v_4 = limit - cursor;
    


    ket = cursor;
    
    among_var = find_among_b(a_4, 8);
    if (among_var != 0)
    {



      bra = cursor;
      
      if (r_R2())
      {


        switch (among_var)
        {
        case 0: 
          break;
        
        case 1: 
          slice_del();
          
          int v_5 = limit - cursor;
          


          ket = cursor;
          
          if (!eq_s_b(2, "ig"))
          {
            cursor = (limit - v_5);
          }
          else
          {
            bra = cursor;
            

            int v_6 = limit - cursor;
            

            if (eq_s_b(1, "e"))
            {


              cursor = (limit - v_5);
            }
            else {
              cursor = (limit - v_6);
              

              if (!r_R2())
              {
                cursor = (limit - v_5);
              }
              else
              {
                slice_del(); }
            } }
          break;
        


        case 2: 
          int v_7 = limit - cursor;
          

          if (!eq_s_b(1, "e"))
          {




            cursor = (limit - v_7);
            

            slice_del(); }
          break;
        

        case 3: 
          slice_del();
          
          int v_8 = limit - cursor;
          


          ket = cursor;
          

          int v_9 = limit - cursor;
          

          if (!eq_s_b(2, "er"))
          {




            cursor = (limit - v_9);
            
            if (!eq_s_b(2, "en"))
            {
              cursor = (limit - v_8);
              break;
            }
          }
          
          bra = cursor;
          
          if (!r_R1())
          {
            cursor = (limit - v_8);
          }
          else
          {
            slice_del();
          }
          break;
        

        case 4: 
          slice_del();
          
          int v_10 = limit - cursor;
          


          ket = cursor;
          
          among_var = find_among_b(a_3, 2);
          if (among_var == 0)
          {
            cursor = (limit - v_10);
          }
          else
          {
            bra = cursor;
            
            if (!r_R2())
            {
              cursor = (limit - v_10);
            }
            else
              switch (among_var) {
              case 0: 
                cursor = (limit - v_10);
                break;
              

              case 1: 
                slice_del();
              }
          }
          break;
        }
      }
    }
    cursor = (limit - v_4);
    return true;
  }
  





  public boolean stem()
  {
    int v_1 = cursor;
    

    if (!r_prelude()) {}
    



    cursor = v_1;
    
    int v_2 = cursor;
    

    if (!r_mark_regions()) {}
    



    cursor = v_2;
    
    limit_backward = cursor;cursor = limit;
    
    int v_3 = limit - cursor;
    

    if (!r_standard_suffix()) {}
    



    cursor = (limit - v_3);
    cursor = limit_backward;
    int v_4 = cursor;
    

    if (!r_postlude()) {}
    



    cursor = v_4;
    return true;
  }
  
  public boolean equals(Object o) {
    return o instanceof germanStemmer;
  }
  
  public int hashCode() {
    return germanStemmer.class.getName().hashCode();
  }
}
