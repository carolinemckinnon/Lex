package org.tartarus.snowball.ext;

import org.tartarus.snowball.Among;
import org.tartarus.snowball.SnowballStemmer;







public class norwegianStemmer
  extends SnowballStemmer
{
  private static final long serialVersionUID = 1L;
  private static final norwegianStemmer methodObject = new norwegianStemmer();
  
  private static final Among[] a_0 = {
    new Among("a", -1, 1, "", methodObject), 
    new Among("e", -1, 1, "", methodObject), 
    new Among("ede", 1, 1, "", methodObject), 
    new Among("ande", 1, 1, "", methodObject), 
    new Among("ende", 1, 1, "", methodObject), 
    new Among("ane", 1, 1, "", methodObject), 
    new Among("ene", 1, 1, "", methodObject), 
    new Among("hetene", 6, 1, "", methodObject), 
    new Among("erte", 1, 3, "", methodObject), 
    new Among("en", -1, 1, "", methodObject), 
    new Among("heten", 9, 1, "", methodObject), 
    new Among("ar", -1, 1, "", methodObject), 
    new Among("er", -1, 1, "", methodObject), 
    new Among("heter", 12, 1, "", methodObject), 
    new Among("s", -1, 2, "", methodObject), 
    new Among("as", 14, 1, "", methodObject), 
    new Among("es", 14, 1, "", methodObject), 
    new Among("edes", 16, 1, "", methodObject), 
    new Among("endes", 16, 1, "", methodObject), 
    new Among("enes", 16, 1, "", methodObject), 
    new Among("hetenes", 19, 1, "", methodObject), 
    new Among("ens", 14, 1, "", methodObject), 
    new Among("hetens", 21, 1, "", methodObject), 
    new Among("ers", 14, 1, "", methodObject), 
    new Among("ets", 14, 1, "", methodObject), 
    new Among("et", -1, 1, "", methodObject), 
    new Among("het", 25, 1, "", methodObject), 
    new Among("ert", -1, 3, "", methodObject), 
    new Among("ast", -1, 1, "", methodObject) };
  

  private static final Among[] a_1 = {
    new Among("dt", -1, -1, "", methodObject), 
    new Among("vt", -1, -1, "", methodObject) };
  

  private static final Among[] a_2 = {
    new Among("leg", -1, 1, "", methodObject), 
    new Among("eleg", 0, 1, "", methodObject), 
    new Among("ig", -1, 1, "", methodObject), 
    new Among("eig", 2, 1, "", methodObject), 
    new Among("lig", 2, 1, "", methodObject), 
    new Among("elig", 4, 1, "", methodObject), 
    new Among("els", -1, 1, "", methodObject), 
    new Among("lov", -1, 1, "", methodObject), 
    new Among("elov", 7, 1, "", methodObject), 
    new Among("slov", 7, 1, "", methodObject), 
    new Among("hetslov", 9, 1, "", methodObject) };
  

  private static final char[] g_v = { '\021', 'A', '\020', '\001', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '0', '\000''' };
  
  private static final char[] g_s_ending = { 'w', '}', '', '\001' };
  private int I_x;
  private int I_p1;
  
  public norwegianStemmer() {}
  
  private void copy_from(norwegianStemmer other) { I_x = I_x;
    I_p1 = I_p1;
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
    
    int among_var = find_among_b(a_0, 29);
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
      int v_3 = limit - cursor;
      
      if (!in_grouping_b(g_s_ending, 98, 122))
      {




        cursor = (limit - v_3);
        

        if (!eq_s_b(1, "k"))
        {
          return false;
        }
        if (!out_grouping_b(g_v, 97, 248))
        {
          return false;
        }
      }
      
      slice_del();
      break;
    

    case 3: 
      slice_from("er");
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
    
    if (find_among_b(a_1, 2) == 0)
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
    
    if (cursor < I_p1)
    {
      return false;
    }
    cursor = I_p1;
    int v_2 = limit_backward;
    limit_backward = cursor;
    cursor = (limit - v_1);
    

    ket = cursor;
    
    int among_var = find_among_b(a_2, 11);
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
    }
    
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
    cursor = limit_backward;return true;
  }
  
  public boolean equals(Object o) {
    return o instanceof norwegianStemmer;
  }
  
  public int hashCode() {
    return norwegianStemmer.class.getName().hashCode();
  }
}
