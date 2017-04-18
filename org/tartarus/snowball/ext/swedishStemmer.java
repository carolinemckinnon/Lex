package org.tartarus.snowball.ext;

import org.tartarus.snowball.Among;
import org.tartarus.snowball.SnowballStemmer;







public class swedishStemmer
  extends SnowballStemmer
{
  private static final long serialVersionUID = 1L;
  private static final swedishStemmer methodObject = new swedishStemmer();
  
  private static final Among[] a_0 = {
    new Among("a", -1, 1, "", methodObject), 
    new Among("arna", 0, 1, "", methodObject), 
    new Among("erna", 0, 1, "", methodObject), 
    new Among("heterna", 2, 1, "", methodObject), 
    new Among("orna", 0, 1, "", methodObject), 
    new Among("ad", -1, 1, "", methodObject), 
    new Among("e", -1, 1, "", methodObject), 
    new Among("ade", 6, 1, "", methodObject), 
    new Among("ande", 6, 1, "", methodObject), 
    new Among("arne", 6, 1, "", methodObject), 
    new Among("are", 6, 1, "", methodObject), 
    new Among("aste", 6, 1, "", methodObject), 
    new Among("en", -1, 1, "", methodObject), 
    new Among("anden", 12, 1, "", methodObject), 
    new Among("aren", 12, 1, "", methodObject), 
    new Among("heten", 12, 1, "", methodObject), 
    new Among("ern", -1, 1, "", methodObject), 
    new Among("ar", -1, 1, "", methodObject), 
    new Among("er", -1, 1, "", methodObject), 
    new Among("heter", 18, 1, "", methodObject), 
    new Among("or", -1, 1, "", methodObject), 
    new Among("s", -1, 2, "", methodObject), 
    new Among("as", 21, 1, "", methodObject), 
    new Among("arnas", 22, 1, "", methodObject), 
    new Among("ernas", 22, 1, "", methodObject), 
    new Among("ornas", 22, 1, "", methodObject), 
    new Among("es", 21, 1, "", methodObject), 
    new Among("ades", 26, 1, "", methodObject), 
    new Among("andes", 26, 1, "", methodObject), 
    new Among("ens", 21, 1, "", methodObject), 
    new Among("arens", 29, 1, "", methodObject), 
    new Among("hetens", 29, 1, "", methodObject), 
    new Among("erns", 21, 1, "", methodObject), 
    new Among("at", -1, 1, "", methodObject), 
    new Among("andet", -1, 1, "", methodObject), 
    new Among("het", -1, 1, "", methodObject), 
    new Among("ast", -1, 1, "", methodObject) };
  

  private static final Among[] a_1 = {
    new Among("dd", -1, -1, "", methodObject), 
    new Among("gd", -1, -1, "", methodObject), 
    new Among("nn", -1, -1, "", methodObject), 
    new Among("dt", -1, -1, "", methodObject), 
    new Among("gt", -1, -1, "", methodObject), 
    new Among("kt", -1, -1, "", methodObject), 
    new Among("tt", -1, -1, "", methodObject) };
  

  private static final Among[] a_2 = {
    new Among("ig", -1, 1, "", methodObject), 
    new Among("lig", 0, 1, "", methodObject), 
    new Among("els", -1, 1, "", methodObject), 
    new Among("fullt", -1, 3, "", methodObject), 
    new Among("löst", -1, 2, "", methodObject) };
  

  private static final char[] g_v = { '\021', 'A', '\020', '\001', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\030', '\000'' ' };
  
  private static final char[] g_s_ending = { 'w', '', '' };
  private int I_x;
  private int I_p1;
  
  public swedishStemmer() {}
  
  private void copy_from(swedishStemmer other) { I_x = I_x;
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
      
      if (in_grouping(g_v, 97, 246))
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
    
    while (!out_grouping(g_v, 97, 246))
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
    
    int among_var = find_among_b(a_0, 37);
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
      if (!in_grouping_b(g_s_ending, 98, 121))
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
    
    if (cursor < I_p1)
    {
      return false;
    }
    cursor = I_p1;
    int v_2 = limit_backward;
    limit_backward = cursor;
    cursor = (limit - v_1);
    

    int v_3 = limit - cursor;
    
    if (find_among_b(a_1, 7) == 0)
    {
      limit_backward = v_2;
      return false;
    }
    cursor = (limit - v_3);
    

    ket = cursor;
    
    if (cursor <= limit_backward)
    {
      limit_backward = v_2;
      return false;
    }
    cursor -= 1;
    
    bra = cursor;
    
    slice_del();
    limit_backward = v_2;
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
    
    int among_var = find_among_b(a_2, 5);
    if (among_var == 0)
    {
      limit_backward = v_2;
      return false;
    }
    
    bra = cursor;
    switch (among_var) {
    case 0: 
      limit_backward = v_2;
      return false;
    

    case 1: 
      slice_del();
      break;
    

    case 2: 
      slice_from("lös");
      break;
    

    case 3: 
      slice_from("full");
    }
    
    limit_backward = v_2;
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
    return o instanceof swedishStemmer;
  }
  
  public int hashCode() {
    return swedishStemmer.class.getName().hashCode();
  }
}
