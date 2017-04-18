package org.tartarus.snowball.ext;

import org.tartarus.snowball.Among;
import org.tartarus.snowball.SnowballStemmer;







public class dutchStemmer
  extends SnowballStemmer
{
  private static final long serialVersionUID = 1L;
  private static final dutchStemmer methodObject = new dutchStemmer();
  
  private static final Among[] a_0 = {
    new Among("", -1, 6, "", methodObject), 
    new Among("á", 0, 1, "", methodObject), 
    new Among("ä", 0, 1, "", methodObject), 
    new Among("é", 0, 2, "", methodObject), 
    new Among("ë", 0, 2, "", methodObject), 
    new Among("í", 0, 3, "", methodObject), 
    new Among("ï", 0, 3, "", methodObject), 
    new Among("ó", 0, 4, "", methodObject), 
    new Among("ö", 0, 4, "", methodObject), 
    new Among("ú", 0, 5, "", methodObject), 
    new Among("ü", 0, 5, "", methodObject) };
  

  private static final Among[] a_1 = {
    new Among("", -1, 3, "", methodObject), 
    new Among("I", 0, 2, "", methodObject), 
    new Among("Y", 0, 1, "", methodObject) };
  

  private static final Among[] a_2 = {
    new Among("dd", -1, -1, "", methodObject), 
    new Among("kk", -1, -1, "", methodObject), 
    new Among("tt", -1, -1, "", methodObject) };
  

  private static final Among[] a_3 = {
    new Among("ene", -1, 2, "", methodObject), 
    new Among("se", -1, 3, "", methodObject), 
    new Among("en", -1, 2, "", methodObject), 
    new Among("heden", 2, 1, "", methodObject), 
    new Among("s", -1, 3, "", methodObject) };
  

  private static final Among[] a_4 = {
    new Among("end", -1, 1, "", methodObject), 
    new Among("ig", -1, 2, "", methodObject), 
    new Among("ing", -1, 1, "", methodObject), 
    new Among("lijk", -1, 3, "", methodObject), 
    new Among("baar", -1, 4, "", methodObject), 
    new Among("bar", -1, 5, "", methodObject) };
  

  private static final Among[] a_5 = {
    new Among("aa", -1, -1, "", methodObject), 
    new Among("ee", -1, -1, "", methodObject), 
    new Among("oo", -1, -1, "", methodObject), 
    new Among("uu", -1, -1, "", methodObject) };
  

  private static final char[] g_v = { '\021', 'A', '\020', '\001', '\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''' };
  
  private static final char[] g_v_I = { '\001', '\000', '\000', '\021', 'A', '\020', '\001', '\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''' };
  
  private static final char[] g_v_j = { '\021', 'C', '\020', '\001', '\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''' };
  private int I_p2;
  private int I_p1;
  private boolean B_e_found;
  
  public dutchStemmer() {}
  
  private void copy_from(dutchStemmer other) { I_p2 = I_p2;
    I_p1 = I_p1;
    B_e_found = B_e_found;
    super.copy_from(other);
  }
  








  private boolean r_prelude()
  {
    int v_1 = cursor;
    int v_2;
    for (;;)
    {
      v_2 = cursor;
      


      bra = cursor;
      
      int among_var = find_among(a_0, 11);
      if (among_var == 0) {
        break;
      }
      

      ket = cursor;
      switch (among_var)
      {
      case 0: 
        break;
      
      case 1: 
        slice_from("a");
        break;
      

      case 2: 
        slice_from("e");
        break;
      

      case 3: 
        slice_from("i");
        break;
      

      case 4: 
        slice_from("o");
        break;
      

      case 5: 
        slice_from("u");
        break;
      

      case 6: 
        if (cursor >= limit) {
          break label163;
        }
        
        cursor += 1;
      }
      
    }
    label163:
    cursor = v_2;
    

    cursor = v_1;
    
    int v_3 = cursor;
    


    bra = cursor;
    
    if (!eq_s(1, "y"))
    {
      cursor = v_3;
    }
    else
    {
      ket = cursor;
      
      slice_from("Y");
    }
    


    int v_4 = cursor;
    

    for (;;)
    {
      int v_5 = cursor;
      

      if (in_grouping(g_v, 97, 232))
      {



        bra = cursor;
        

        int v_6 = cursor;
        


        if (eq_s(1, "i"))
        {



          ket = cursor;
          if (in_grouping(g_v, 97, 232))
          {



            slice_from("I");
            break label345;
          } }
        cursor = v_6;
        

        if (eq_s(1, "y"))
        {



          ket = cursor;
          
          slice_from("Y");
          label345:
          cursor = v_5;
          break;
        } }
      cursor = v_5;
      if (cursor >= limit) {
        break label387;
      }
      
      cursor += 1;
    }
    label387:
    int v_5;
    cursor = v_4;
    

    return true;
  }
  
  private boolean r_mark_regions()
  {
    I_p1 = limit;
    I_p2 = limit;
    



    while (!in_grouping(g_v, 97, 232))
    {




      if (cursor >= limit)
      {
        return false;
      }
      cursor += 1;
    }
    



    while (!out_grouping(g_v, 97, 232))
    {




      if (cursor >= limit)
      {
        return false;
      }
      cursor += 1;
    }
    
    I_p1 = cursor;
    


    if (I_p1 < 3)
    {


      I_p1 = 3;
    }
    



    while (!in_grouping(g_v, 97, 232))
    {




      if (cursor >= limit)
      {
        return false;
      }
      cursor += 1;
    }
    



    while (!out_grouping(g_v, 97, 232))
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
      
      int among_var = find_among(a_1, 3);
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
        slice_from("i");
        break;
      

      case 3: 
        if (cursor >= limit) {
          break label116;
        }
        
        cursor += 1;
      }
      
    }
    label116:
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
  


  private boolean r_undouble()
  {
    int v_1 = limit - cursor;
    
    if (find_among_b(a_2, 3) == 0)
    {
      return false;
    }
    cursor = (limit - v_1);
    
    ket = cursor;
    
    if (cursor <= limit_backward)
    {
      return false;
    }
    cursor -= 1;
    
    bra = cursor;
    
    slice_del();
    return true;
  }
  


  private boolean r_e_ending()
  {
    B_e_found = false;
    
    ket = cursor;
    
    if (!eq_s_b(1, "e"))
    {
      return false;
    }
    
    bra = cursor;
    
    if (!r_R1())
    {
      return false;
    }
    
    int v_1 = limit - cursor;
    if (!out_grouping_b(g_v, 97, 232))
    {
      return false;
    }
    cursor = (limit - v_1);
    
    slice_del();
    
    B_e_found = true;
    
    if (!r_undouble())
    {
      return false;
    }
    return true;
  }
  



  private boolean r_en_ending()
  {
    if (!r_R1())
    {
      return false;
    }
    
    int v_1 = limit - cursor;
    if (!out_grouping_b(g_v, 97, 232))
    {
      return false;
    }
    cursor = (limit - v_1);
    

    int v_2 = limit - cursor;
    

    if (eq_s_b(3, "gem"))
    {


      return false;
    }
    cursor = (limit - v_2);
    

    slice_del();
    
    if (!r_undouble())
    {
      return false;
    }
    return true;
  }
  












  private boolean r_standard_suffix()
  {
    int v_1 = limit - cursor;
    


    ket = cursor;
    
    int among_var = find_among_b(a_3, 5);
    if (among_var != 0)
    {



      bra = cursor;
      switch (among_var)
      {
      case 0: 
        break;
      
      case 1: 
        if (r_R1())
        {



          slice_from("heid"); }
        break;
      

      case 2: 
        if (r_en_ending())
          break;
      case 3:  if ((goto 136) && 
        




          (r_R1()))
        {


          if (out_grouping_b(g_v_j, 97, 232))
          {



            slice_del(); } }
        break;
      }
    }
    cursor = (limit - v_1);
    
    int v_2 = limit - cursor;
    

    if (!r_e_ending()) {}
    



    cursor = (limit - v_2);
    
    int v_3 = limit - cursor;
    


    ket = cursor;
    
    if (eq_s_b(4, "heid"))
    {



      bra = cursor;
      
      if (r_R2())
      {




        int v_4 = limit - cursor;
        

        if (!eq_s_b(1, "c"))
        {




          cursor = (limit - v_4);
          

          slice_del();
          
          ket = cursor;
          
          if (eq_s_b(2, "en"))
          {



            bra = cursor;
            
            if (r_en_ending()) {}
          }
        }
      }
    }
    cursor = (limit - v_3);
    
    int v_5 = limit - cursor;
    


    ket = cursor;
    
    among_var = find_among_b(a_4, 6);
    if (among_var != 0)
    {



      bra = cursor;
      switch (among_var)
      {
      case 0: 
        break;
      
      case 1: 
        if (r_R2())
        {



          slice_del();
          

          int v_6 = limit - cursor;
          


          ket = cursor;
          
          if (eq_s_b(2, "ig"))
          {



            bra = cursor;
            
            if (r_R2())
            {




              int v_7 = limit - cursor;
              

              if (!eq_s_b(1, "e"))
              {




                cursor = (limit - v_7);
                

                slice_del();
                break;
              } } }
          cursor = (limit - v_6);
          
          if (r_undouble()) break;
        }
        break; case 2:  if ((goto 629) && 
        





          (r_R2()))
        {




          int v_8 = limit - cursor;
          

          if (!eq_s_b(1, "e"))
          {




            cursor = (limit - v_8);
            

            slice_del(); } }
        break;
      

      case 3: 
        if (r_R2())
        {



          slice_del();
          
          if (r_e_ending()) break;
        }
        break; case 4:  if ((goto 629) && 
        




          (r_R2()))
        {



          slice_del(); }
        break;
      

      case 5: 
        if (r_R2())
        {



          if (B_e_found)
          {



            slice_del(); } }
        break;
      }
    }
    cursor = (limit - v_5);
    
    int v_9 = limit - cursor;
    

    if (out_grouping_b(g_v_I, 73, 232))
    {



      int v_10 = limit - cursor;
      

      if (find_among_b(a_5, 4) != 0)
      {


        if (out_grouping_b(g_v, 97, 232))
        {


          cursor = (limit - v_10);
          
          ket = cursor;
          
          if (cursor > limit_backward)
          {


            cursor -= 1;
            
            bra = cursor;
            
            slice_del();
          } } } }
    cursor = (limit - v_9);
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
    return o instanceof dutchStemmer;
  }
  
  public int hashCode() {
    return dutchStemmer.class.getName().hashCode();
  }
}
