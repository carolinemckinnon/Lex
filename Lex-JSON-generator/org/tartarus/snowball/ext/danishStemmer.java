package org.tartarus.snowball.ext;

import org.tartarus.snowball.Among;
import org.tartarus.snowball.SnowballStemmer;




public class danishStemmer
  extends SnowballStemmer
{
  private static final long serialVersionUID = 1L;
  
  public danishStemmer() {}
  
  private static final danishStemmer methodObject = new danishStemmer();
  
  private static final Among[] a_0 = {
    new Among("hed", -1, 1, "", methodObject), 
    new Among("ethed", 0, 1, "", methodObject), 
    new Among("ered", -1, 1, "", methodObject), 
    new Among("e", -1, 1, "", methodObject), 
    new Among("erede", 3, 1, "", methodObject), 
    new Among("ende", 3, 1, "", methodObject), 
    new Among("erende", 5, 1, "", methodObject), 
    new Among("ene", 3, 1, "", methodObject), 
    new Among("erne", 3, 1, "", methodObject), 
    new Among("ere", 3, 1, "", methodObject), 
    new Among("en", -1, 1, "", methodObject), 
    new Among("heden", 10, 1, "", methodObject), 
    new Among("eren", 10, 1, "", methodObject), 
    new Among("er", -1, 1, "", methodObject), 
    new Among("heder", 13, 1, "", methodObject), 
    new Among("erer", 13, 1, "", methodObject), 
    new Among("s", -1, 2, "", methodObject), 
    new Among("heds", 16, 1, "", methodObject), 
    new Among("es", 16, 1, "", methodObject), 
    new Among("endes", 18, 1, "", methodObject), 
    new Among("erendes", 19, 1, "", methodObject), 
    new Among("enes", 18, 1, "", methodObject), 
    new Among("ernes", 18, 1, "", methodObject), 
    new Among("eres", 18, 1, "", methodObject), 
    new Among("ens", 16, 1, "", methodObject), 
    new Among("hedens", 24, 1, "", methodObject), 
    new Among("erens", 24, 1, "", methodObject), 
    new Among("ers", 16, 1, "", methodObject), 
    new Among("ets", 16, 1, "", methodObject), 
    new Among("erets", 28, 1, "", methodObject), 
    new Among("et", -1, 1, "", methodObject), 
    new Among("eret", 30, 1, "", methodObject) };
  

  private static final Among[] a_1 = {
    new Among("gd", -1, -1, "", methodObject), 
    new Among("dt", -1, -1, "", methodObject), 
    new Among("gt", -1, -1, "", methodObject), 
    new Among("kt", -1, -1, "", methodObject) };
  

  private static final Among[] a_2 = {
    new Among("ig", -1, 1, "", methodObject), 
    new Among("lig", 0, 1, "", methodObject), 
    new Among("elig", 1, 1, "", methodObject), 
    new Among("els", -1, 1, "", methodObject), 
    new Among("løst", -1, 2, "", methodObject) };
  

  private static final char[] g_v = { '\021', 'A', '\020', '\001', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '0', '\000''' };
  
  private static final char[] g_s_ending = { 'ï', 'þ', '*', '\003', '\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\020' };
  
  private int I_x;
  private int I_p1;
  private StringBuilder S_ch = new StringBuilder();
  
  private void copy_from(danishStemmer other) {
    I_x = I_x;
    I_p1 = I_p1;
    S_ch = S_ch;
    super.copy_from(other);
  }
  


  private boolean r_mark_regions()
  {
    I_p1 = limit;
    
    int v_1 = cursor;
    


    int c = cursor + 3;
    if ((c < 0) || (c > limit))
    {
      return false;
    }
    cursor = c;
    

    I_x = cursor;
    cursor = v_1;
    
    for (;;)
    {
      int v_2 = cursor;
      
      if (in_grouping(g_v, 97, 248))
      {


        cursor = v_2;
        break;
      }
      cursor = v_2;
      if (cursor >= limit)
      {
        return false;
      }
      cursor += 1;
    }
    

    int v_2;
    
    while (!out_grouping(g_v, 97, 248))
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
    return true;
  }
  




  private boolean r_main_suffix()
  {
    int v_1 = limit - cursor;
    
    if (cursor < I_p1)
    {
      return false;
    }
    cursor = I_p1;
    int v_2 = limit_backward;
    limit_backward = cursor;
    cursor = (limit - v_1);
    

    ket = cursor;
    
    int among_var = find_among_b(a_0, 32);
    if (among_var == 0)
    {
      limit_backward = v_2;
      return false;
    }
    
    bra = cursor;
    limit_backward = v_2;
    switch (among_var) {
    case 0: 
      return false;
    

    case 1: 
      slice_del();
      break;
    
    case 2: 
      if (!in_grouping_b(g_s_ending, 97, 229))
      {
        return false;
      }
      
      slice_del();
    }
    
    return true;
  }
  




  private boolean r_consonant_pair()
  {
    int v_1 = limit - cursor;
    

    int v_2 = limit - cursor;
    
    if (cursor < I_p1)
    {
      return false;
    }
    cursor = I_p1;
    int v_3 = limit_backward;
    limit_backward = cursor;
    cursor = (limit - v_2);
    

    ket = cursor;
    
    if (find_among_b(a_1, 4) == 0)
    {
      limit_backward = v_3;
      return false;
    }
    
    bra = cursor;
    limit_backward = v_3;
    cursor = (limit - v_1);
    
    if (cursor <= limit_backward)
    {
      return false;
    }
    cursor -= 1;
    
    bra = cursor;
    
    slice_del();
    return true;
  }
  






  private boolean r_other_suffix()
  {
    int v_1 = limit - cursor;
    


    ket = cursor;
    
    if (eq_s_b(2, "st"))
    {



      bra = cursor;
      
      if (eq_s_b(2, "ig"))
      {



        slice_del(); }
    }
    cursor = (limit - v_1);
    
    int v_2 = limit - cursor;
    
    if (cursor < I_p1)
    {
      return false;
    }
    cursor = I_p1;
    int v_3 = limit_backward;
    limit_backward = cursor;
    cursor = (limit - v_2);
    

    ket = cursor;
    
    int among_var = find_among_b(a_2, 5);
    if (among_var == 0)
    {
      limit_backward = v_3;
      return false;
    }
    
    bra = cursor;
    limit_backward = v_3;
    switch (among_var) {
    case 0: 
      return false;
    

    case 1: 
      slice_del();
      
      int v_4 = limit - cursor;
      

      if (!r_consonant_pair()) {}
      



      cursor = (limit - v_4);
      break;
    

    case 2: 
      slice_from("løs");
    }
    
    return true;
  }
  



  private boolean r_undouble()
  {
    int v_1 = limit - cursor;
    
    if (cursor < I_p1)
    {
      return false;
    }
    cursor = I_p1;
    int v_2 = limit_backward;
    limit_backward = cursor;
    cursor = (limit - v_1);
    

    ket = cursor;
    if (!out_grouping_b(g_v, 97, 248))
    {
      limit_backward = v_2;
      return false;
    }
    
    bra = cursor;
    
    S_ch = slice_to(S_ch);
    limit_backward = v_2;
    
    if (!eq_v_b(S_ch))
    {
      return false;
    }
    
    slice_del();
    return true;
  }
  






  public boolean stem()
  {
    int v_1 = cursor;
    

    if (!r_mark_regions()) {}
    



    cursor = v_1;
    
    limit_backward = cursor;cursor = limit;
    

    int v_2 = limit - cursor;
    

    if (!r_main_suffix()) {}
    



    cursor = (limit - v_2);
    
    int v_3 = limit - cursor;
    

    if (!r_consonant_pair()) {}
    



    cursor = (limit - v_3);
    
    int v_4 = limit - cursor;
    

    if (!r_other_suffix()) {}
    



    cursor = (limit - v_4);
    
    int v_5 = limit - cursor;
    

    if (!r_undouble()) {}
    



    cursor = (limit - v_5);
    cursor = limit_backward;return true;
  }
  
  public boolean equals(Object o) {
    return o instanceof danishStemmer;
  }
  
  public int hashCode() {
    return danishStemmer.class.getName().hashCode();
  }
}
