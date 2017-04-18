package org.tartarus.snowball.ext;

import org.tartarus.snowball.Among;
import org.tartarus.snowball.SnowballStemmer;







public class englishStemmer
  extends SnowballStemmer
{
  private static final long serialVersionUID = 1L;
  private static final englishStemmer methodObject = new englishStemmer();
  
  private static final Among[] a_0 = {
    new Among("arsen", -1, -1, "", methodObject), 
    new Among("commun", -1, -1, "", methodObject), 
    new Among("gener", -1, -1, "", methodObject) };
  

  private static final Among[] a_1 = {
    new Among("'", -1, 1, "", methodObject), 
    new Among("'s'", 0, 1, "", methodObject), 
    new Among("'s", -1, 1, "", methodObject) };
  

  private static final Among[] a_2 = {
    new Among("ied", -1, 2, "", methodObject), 
    new Among("s", -1, 3, "", methodObject), 
    new Among("ies", 1, 2, "", methodObject), 
    new Among("sses", 1, 1, "", methodObject), 
    new Among("ss", 1, -1, "", methodObject), 
    new Among("us", 1, -1, "", methodObject) };
  

  private static final Among[] a_3 = {
    new Among("", -1, 3, "", methodObject), 
    new Among("bb", 0, 2, "", methodObject), 
    new Among("dd", 0, 2, "", methodObject), 
    new Among("ff", 0, 2, "", methodObject), 
    new Among("gg", 0, 2, "", methodObject), 
    new Among("bl", 0, 1, "", methodObject), 
    new Among("mm", 0, 2, "", methodObject), 
    new Among("nn", 0, 2, "", methodObject), 
    new Among("pp", 0, 2, "", methodObject), 
    new Among("rr", 0, 2, "", methodObject), 
    new Among("at", 0, 1, "", methodObject), 
    new Among("tt", 0, 2, "", methodObject), 
    new Among("iz", 0, 1, "", methodObject) };
  

  private static final Among[] a_4 = {
    new Among("ed", -1, 2, "", methodObject), 
    new Among("eed", 0, 1, "", methodObject), 
    new Among("ing", -1, 2, "", methodObject), 
    new Among("edly", -1, 2, "", methodObject), 
    new Among("eedly", 3, 1, "", methodObject), 
    new Among("ingly", -1, 2, "", methodObject) };
  

  private static final Among[] a_5 = {
    new Among("anci", -1, 3, "", methodObject), 
    new Among("enci", -1, 2, "", methodObject), 
    new Among("ogi", -1, 13, "", methodObject), 
    new Among("li", -1, 16, "", methodObject), 
    new Among("bli", 3, 12, "", methodObject), 
    new Among("abli", 4, 4, "", methodObject), 
    new Among("alli", 3, 8, "", methodObject), 
    new Among("fulli", 3, 14, "", methodObject), 
    new Among("lessli", 3, 15, "", methodObject), 
    new Among("ousli", 3, 10, "", methodObject), 
    new Among("entli", 3, 5, "", methodObject), 
    new Among("aliti", -1, 8, "", methodObject), 
    new Among("biliti", -1, 12, "", methodObject), 
    new Among("iviti", -1, 11, "", methodObject), 
    new Among("tional", -1, 1, "", methodObject), 
    new Among("ational", 14, 7, "", methodObject), 
    new Among("alism", -1, 8, "", methodObject), 
    new Among("ation", -1, 7, "", methodObject), 
    new Among("ization", 17, 6, "", methodObject), 
    new Among("izer", -1, 6, "", methodObject), 
    new Among("ator", -1, 7, "", methodObject), 
    new Among("iveness", -1, 11, "", methodObject), 
    new Among("fulness", -1, 9, "", methodObject), 
    new Among("ousness", -1, 10, "", methodObject) };
  

  private static final Among[] a_6 = {
    new Among("icate", -1, 4, "", methodObject), 
    new Among("ative", -1, 6, "", methodObject), 
    new Among("alize", -1, 3, "", methodObject), 
    new Among("iciti", -1, 4, "", methodObject), 
    new Among("ical", -1, 4, "", methodObject), 
    new Among("tional", -1, 1, "", methodObject), 
    new Among("ational", 5, 2, "", methodObject), 
    new Among("ful", -1, 5, "", methodObject), 
    new Among("ness", -1, 5, "", methodObject) };
  

  private static final Among[] a_7 = {
    new Among("ic", -1, 1, "", methodObject), 
    new Among("ance", -1, 1, "", methodObject), 
    new Among("ence", -1, 1, "", methodObject), 
    new Among("able", -1, 1, "", methodObject), 
    new Among("ible", -1, 1, "", methodObject), 
    new Among("ate", -1, 1, "", methodObject), 
    new Among("ive", -1, 1, "", methodObject), 
    new Among("ize", -1, 1, "", methodObject), 
    new Among("iti", -1, 1, "", methodObject), 
    new Among("al", -1, 1, "", methodObject), 
    new Among("ism", -1, 1, "", methodObject), 
    new Among("ion", -1, 2, "", methodObject), 
    new Among("er", -1, 1, "", methodObject), 
    new Among("ous", -1, 1, "", methodObject), 
    new Among("ant", -1, 1, "", methodObject), 
    new Among("ent", -1, 1, "", methodObject), 
    new Among("ment", 15, 1, "", methodObject), 
    new Among("ement", 16, 1, "", methodObject) };
  

  private static final Among[] a_8 = {
    new Among("e", -1, 1, "", methodObject), 
    new Among("l", -1, 2, "", methodObject) };
  

  private static final Among[] a_9 = {
    new Among("succeed", -1, -1, "", methodObject), 
    new Among("proceed", -1, -1, "", methodObject), 
    new Among("exceed", -1, -1, "", methodObject), 
    new Among("canning", -1, -1, "", methodObject), 
    new Among("inning", -1, -1, "", methodObject), 
    new Among("earring", -1, -1, "", methodObject), 
    new Among("herring", -1, -1, "", methodObject), 
    new Among("outing", -1, -1, "", methodObject) };
  

  private static final Among[] a_10 = {
    new Among("andes", -1, -1, "", methodObject), 
    new Among("atlas", -1, -1, "", methodObject), 
    new Among("bias", -1, -1, "", methodObject), 
    new Among("cosmos", -1, -1, "", methodObject), 
    new Among("dying", -1, 3, "", methodObject), 
    new Among("early", -1, 9, "", methodObject), 
    new Among("gently", -1, 7, "", methodObject), 
    new Among("howe", -1, -1, "", methodObject), 
    new Among("idly", -1, 6, "", methodObject), 
    new Among("lying", -1, 4, "", methodObject), 
    new Among("news", -1, -1, "", methodObject), 
    new Among("only", -1, 10, "", methodObject), 
    new Among("singly", -1, 11, "", methodObject), 
    new Among("skies", -1, 2, "", methodObject), 
    new Among("skis", -1, 1, "", methodObject), 
    new Among("sky", -1, -1, "", methodObject), 
    new Among("tying", -1, 5, "", methodObject), 
    new Among("ugly", -1, 8, "", methodObject) };
  

  private static final char[] g_v = { '\021', 'A', '\020', '\001' };
  
  private static final char[] g_v_WXY = { '\001', '\021', 'A', 'Ð', '\001' };
  
  private static final char[] g_valid_LI = { '7', '', '\002' };
  private boolean B_Y_found;
  private int I_p2;
  private int I_p1;
  
  public englishStemmer() {}
  
  private void copy_from(englishStemmer other) { B_Y_found = B_Y_found;
    I_p2 = I_p2;
    I_p1 = I_p1;
    super.copy_from(other);
  }
  






  private boolean r_prelude()
  {
    B_Y_found = false;
    
    int v_1 = cursor;
    


    bra = cursor;
    
    if (eq_s(1, "'"))
    {



      ket = cursor;
      
      slice_del();
    }
    cursor = v_1;
    
    int v_2 = cursor;
    


    bra = cursor;
    
    if (eq_s(1, "y"))
    {



      ket = cursor;
      
      slice_from("Y");
      
      B_Y_found = true;
    }
    cursor = v_2;
    
    int v_3 = cursor;
    
    int v_4;
    for (;;)
    {
      v_4 = cursor;
      


      for (;;)
      {
        int v_5 = cursor;
        

        if (in_grouping(g_v, 97, 121))
        {



          bra = cursor;
          
          if (eq_s(1, "y"))
          {



            ket = cursor;
            cursor = v_5;
            break;
          } }
        cursor = v_5;
        if (cursor >= limit) {
          break label221;
        }
        
        cursor += 1;
      }
      int v_5;
      slice_from("Y");
      
      B_Y_found = true;
    }
    label221:
    cursor = v_4;
    


    cursor = v_3;
    return true;
  }
  


  private boolean r_mark_regions()
  {
    I_p1 = limit;
    I_p2 = limit;
    
    int v_1 = cursor;
    



    int v_2 = cursor;
    

    if (find_among(a_0, 3) == 0)
    {




      cursor = v_2;
      




      while (!in_grouping(g_v, 97, 121))
      {




        if (cursor >= limit) {
          break label222;
        }
        
        cursor += 1;
      }
      



      while (!out_grouping(g_v, 97, 121))
      {




        if (cursor >= limit) {
          break label222;
        }
        
        cursor += 1;
      }
    }
    
    I_p1 = cursor;
    



    while (!in_grouping(g_v, 97, 121))
    {




      if (cursor >= limit) {
        break label222;
      }
      
      cursor += 1;
    }
    



    while (!out_grouping(g_v, 97, 121))
    {




      if (cursor >= limit) {
        break label222;
      }
      
      cursor += 1;
    }
    
    I_p2 = cursor;
    label222:
    cursor = v_1;
    return true;
  }
  



  private boolean r_shortv()
  {
    int v_1 = limit - cursor;
    

    if ((!out_grouping_b(g_v_WXY, 89, 121)) || 
    


      (!in_grouping_b(g_v, 97, 121)) || 
      


      (!out_grouping_b(g_v, 97, 121)))
    {




      cursor = (limit - v_1);
      
      if (!out_grouping_b(g_v, 97, 121))
      {
        return false;
      }
      if (!in_grouping_b(g_v, 97, 121))
      {
        return false;
      }
      
      if (cursor > limit_backward)
      {
        return false;
      }
    }
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
  




  private boolean r_Step_1a()
  {
    int v_1 = limit - cursor;
    


    ket = cursor;
    
    int among_var = find_among_b(a_1, 3);
    if (among_var == 0)
    {
      cursor = (limit - v_1);
    }
    else
    {
      bra = cursor;
      switch (among_var) {
      case 0: 
        cursor = (limit - v_1);
        break;
      

      case 1: 
        slice_del();
      }
      
    }
    
    ket = cursor;
    
    among_var = find_among_b(a_2, 6);
    if (among_var == 0)
    {
      return false;
    }
    
    bra = cursor;
    switch (among_var) {
    case 0: 
      return false;
    

    case 1: 
      slice_from("ss");
      break;
    


    case 2: 
      int v_2 = limit - cursor;
      



      int c = cursor - 2;
      if ((limit_backward <= c) && (c <= limit))
      {


        cursor = c;
        

        slice_from("i");
      }
      else {
        cursor = (limit - v_2);
        
        slice_from("ie");
      }
      break;
    

    case 3: 
      if (cursor <= limit_backward)
      {
        return false;
      }
      cursor -= 1;
      



      while (!in_grouping_b(g_v, 97, 121))
      {




        if (cursor <= limit_backward)
        {
          return false;
        }
        cursor -= 1;
      }
      
      slice_del();
    }
    
    return true;
  }
  





  private boolean r_Step_1b()
  {
    ket = cursor;
    
    int among_var = find_among_b(a_4, 6);
    if (among_var == 0)
    {
      return false;
    }
    
    bra = cursor;
    switch (among_var) {
    case 0: 
      return false;
    

    case 1: 
      if (!r_R1())
      {
        return false;
      }
      
      slice_from("ee");
      break;
    

    case 2: 
      int v_1 = limit - cursor;
      



      while (!in_grouping_b(g_v, 97, 121))
      {




        if (cursor <= limit_backward)
        {
          return false;
        }
        cursor -= 1;
      }
      cursor = (limit - v_1);
      
      slice_del();
      
      int v_3 = limit - cursor;
      
      among_var = find_among_b(a_3, 13);
      if (among_var == 0)
      {
        return false;
      }
      cursor = (limit - v_3);
      switch (among_var) {
      case 0: 
        return false;
      


      case 1: 
        int c = cursor;
        insert(cursor, cursor, "e");
        cursor = c;
        
        break;
      

      case 2: 
        ket = cursor;
        
        if (cursor <= limit_backward)
        {
          return false;
        }
        cursor -= 1;
        
        bra = cursor;
        
        slice_del();
        break;
      

      case 3: 
        if (cursor != I_p1)
        {
          return false;
        }
        
        int v_4 = limit - cursor;
        
        if (!r_shortv())
        {
          return false;
        }
        cursor = (limit - v_4);
        

        int c = cursor;
        insert(cursor, cursor, "e");
        cursor = c;
      }
      
      break;
    }
    
    return true;
  }
  



  private boolean r_Step_1c()
  {
    ket = cursor;
    

    int v_1 = limit - cursor;
    

    if (!eq_s_b(1, "y"))
    {




      cursor = (limit - v_1);
      
      if (!eq_s_b(1, "Y"))
      {
        return false;
      }
    }
    
    bra = cursor;
    if (!out_grouping_b(g_v, 97, 121))
    {
      return false;
    }
    

    int v_2 = limit - cursor;
    

    if (cursor <= limit_backward)
    {


      return false;
    }
    cursor = (limit - v_2);
    

    slice_from("i");
    return true;
  }
  


  private boolean r_Step_2()
  {
    ket = cursor;
    
    int among_var = find_among_b(a_5, 24);
    if (among_var == 0)
    {
      return false;
    }
    
    bra = cursor;
    
    if (!r_R1())
    {
      return false;
    }
    switch (among_var) {
    case 0: 
      return false;
    

    case 1: 
      slice_from("tion");
      break;
    

    case 2: 
      slice_from("ence");
      break;
    

    case 3: 
      slice_from("ance");
      break;
    

    case 4: 
      slice_from("able");
      break;
    

    case 5: 
      slice_from("ent");
      break;
    

    case 6: 
      slice_from("ize");
      break;
    

    case 7: 
      slice_from("ate");
      break;
    

    case 8: 
      slice_from("al");
      break;
    

    case 9: 
      slice_from("ful");
      break;
    

    case 10: 
      slice_from("ous");
      break;
    

    case 11: 
      slice_from("ive");
      break;
    

    case 12: 
      slice_from("ble");
      break;
    

    case 13: 
      if (!eq_s_b(1, "l"))
      {
        return false;
      }
      
      slice_from("og");
      break;
    

    case 14: 
      slice_from("ful");
      break;
    

    case 15: 
      slice_from("less");
      break;
    
    case 16: 
      if (!in_grouping_b(g_valid_LI, 99, 116))
      {
        return false;
      }
      
      slice_del();
    }
    
    return true;
  }
  


  private boolean r_Step_3()
  {
    ket = cursor;
    
    int among_var = find_among_b(a_6, 9);
    if (among_var == 0)
    {
      return false;
    }
    
    bra = cursor;
    
    if (!r_R1())
    {
      return false;
    }
    switch (among_var) {
    case 0: 
      return false;
    

    case 1: 
      slice_from("tion");
      break;
    

    case 2: 
      slice_from("ate");
      break;
    

    case 3: 
      slice_from("al");
      break;
    

    case 4: 
      slice_from("ic");
      break;
    

    case 5: 
      slice_del();
      break;
    

    case 6: 
      if (!r_R2())
      {
        return false;
      }
      
      slice_del();
    }
    
    return true;
  }
  



  private boolean r_Step_4()
  {
    ket = cursor;
    
    int among_var = find_among_b(a_7, 18);
    if (among_var == 0)
    {
      return false;
    }
    
    bra = cursor;
    
    if (!r_R2())
    {
      return false;
    }
    switch (among_var) {
    case 0: 
      return false;
    

    case 1: 
      slice_del();
      break;
    


    case 2: 
      int v_1 = limit - cursor;
      

      if (!eq_s_b(1, "s"))
      {




        cursor = (limit - v_1);
        
        if (!eq_s_b(1, "t"))
        {
          return false;
        }
      }
      
      slice_del();
    }
    
    return true;
  }
  




  private boolean r_Step_5()
  {
    ket = cursor;
    
    int among_var = find_among_b(a_8, 2);
    if (among_var == 0)
    {
      return false;
    }
    
    bra = cursor;
    switch (among_var) {
    case 0: 
      return false;
    


    case 1: 
      int v_1 = limit - cursor;
      

      if (!r_R2())
      {




        cursor = (limit - v_1);
        

        if (!r_R1())
        {
          return false;
        }
        

        int v_2 = limit - cursor;
        

        if (r_shortv())
        {


          return false;
        }
        cursor = (limit - v_2);
      }
      

      slice_del();
      break;
    

    case 2: 
      if (!r_R2())
      {
        return false;
      }
      
      if (!eq_s_b(1, "l"))
      {
        return false;
      }
      
      slice_del();
    }
    
    return true;
  }
  

  private boolean r_exception2()
  {
    ket = cursor;
    
    if (find_among_b(a_9, 8) == 0)
    {
      return false;
    }
    
    bra = cursor;
    
    if (cursor > limit_backward)
    {
      return false;
    }
    return true;
  }
  


  private boolean r_exception1()
  {
    bra = cursor;
    
    int among_var = find_among(a_10, 18);
    if (among_var == 0)
    {
      return false;
    }
    
    ket = cursor;
    
    if (cursor < limit)
    {
      return false;
    }
    switch (among_var) {
    case 0: 
      return false;
    

    case 1: 
      slice_from("ski");
      break;
    

    case 2: 
      slice_from("sky");
      break;
    

    case 3: 
      slice_from("die");
      break;
    

    case 4: 
      slice_from("lie");
      break;
    

    case 5: 
      slice_from("tie");
      break;
    

    case 6: 
      slice_from("idl");
      break;
    

    case 7: 
      slice_from("gentl");
      break;
    

    case 8: 
      slice_from("ugli");
      break;
    

    case 9: 
      slice_from("earli");
      break;
    

    case 10: 
      slice_from("onli");
      break;
    

    case 11: 
      slice_from("singl");
    }
    
    return true;
  }
  



  private boolean r_postlude()
  {
    if (!B_Y_found)
    {
      return false;
    }
    int v_1;
    for (;;)
    {
      v_1 = cursor;
      


      for (;;)
      {
        int v_2 = cursor;
        


        bra = cursor;
        
        if (eq_s(1, "Y"))
        {



          ket = cursor;
          cursor = v_2;
          break;
        }
        cursor = v_2;
        if (cursor >= limit) {
          break label99;
        }
        
        cursor += 1;
      }
      int v_2;
      slice_from("y");
    }
    label99:
    cursor = v_1;
    

    return true;
  }
  















  public boolean stem()
  {
    int v_1 = cursor;
    

    if (!r_exception1())
    {




      cursor = v_1;
      


      int v_2 = cursor;
      


      int c = cursor + 3;
      if ((c >= 0) && (c <= limit))
      {


        cursor = c;
      }
      else
      {
        cursor = v_2;
        
        break label380;
      }
      cursor = v_1;
      

      int v_3 = cursor;
      

      if (!r_prelude()) {}
      



      cursor = v_3;
      
      int v_4 = cursor;
      

      if (!r_mark_regions()) {}
      



      cursor = v_4;
      
      limit_backward = cursor;cursor = limit;
      

      int v_5 = limit - cursor;
      

      if (!r_Step_1a()) {}
      



      cursor = (limit - v_5);
      

      int v_6 = limit - cursor;
      

      if (!r_exception2())
      {




        cursor = (limit - v_6);
        

        int v_7 = limit - cursor;
        

        if (!r_Step_1b()) {}
        



        cursor = (limit - v_7);
        
        int v_8 = limit - cursor;
        

        if (!r_Step_1c()) {}
        



        cursor = (limit - v_8);
        
        int v_9 = limit - cursor;
        

        if (!r_Step_2()) {}
        



        cursor = (limit - v_9);
        
        int v_10 = limit - cursor;
        

        if (!r_Step_3()) {}
        



        cursor = (limit - v_10);
        
        int v_11 = limit - cursor;
        

        if (!r_Step_4()) {}
        



        cursor = (limit - v_11);
        
        int v_12 = limit - cursor;
        

        if (!r_Step_5()) {}
        



        cursor = (limit - v_12);
      }
      cursor = limit_backward;
      int v_13 = cursor;
      

      if (!r_postlude()) {}
      



      cursor = v_13; }
    label380:
    return true;
  }
  
  public boolean equals(Object o) {
    return o instanceof englishStemmer;
  }
  
  public int hashCode() {
    return englishStemmer.class.getName().hashCode();
  }
}
