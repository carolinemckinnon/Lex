package org.tartarus.snowball.ext;

import org.tartarus.snowball.Among;
import org.tartarus.snowball.SnowballStemmer;







public class spanishStemmer
  extends SnowballStemmer
{
  private static final long serialVersionUID = 1L;
  private static final spanishStemmer methodObject = new spanishStemmer();
  
  private static final Among[] a_0 = {
    new Among("", -1, 6, "", methodObject), 
    new Among("á", 0, 1, "", methodObject), 
    new Among("é", 0, 2, "", methodObject), 
    new Among("í", 0, 3, "", methodObject), 
    new Among("ó", 0, 4, "", methodObject), 
    new Among("ú", 0, 5, "", methodObject) };
  

  private static final Among[] a_1 = {
    new Among("la", -1, -1, "", methodObject), 
    new Among("sela", 0, -1, "", methodObject), 
    new Among("le", -1, -1, "", methodObject), 
    new Among("me", -1, -1, "", methodObject), 
    new Among("se", -1, -1, "", methodObject), 
    new Among("lo", -1, -1, "", methodObject), 
    new Among("selo", 5, -1, "", methodObject), 
    new Among("las", -1, -1, "", methodObject), 
    new Among("selas", 7, -1, "", methodObject), 
    new Among("les", -1, -1, "", methodObject), 
    new Among("los", -1, -1, "", methodObject), 
    new Among("selos", 10, -1, "", methodObject), 
    new Among("nos", -1, -1, "", methodObject) };
  

  private static final Among[] a_2 = {
    new Among("ando", -1, 6, "", methodObject), 
    new Among("iendo", -1, 6, "", methodObject), 
    new Among("yendo", -1, 7, "", methodObject), 
    new Among("ándo", -1, 2, "", methodObject), 
    new Among("iéndo", -1, 1, "", methodObject), 
    new Among("ar", -1, 6, "", methodObject), 
    new Among("er", -1, 6, "", methodObject), 
    new Among("ir", -1, 6, "", methodObject), 
    new Among("ár", -1, 3, "", methodObject), 
    new Among("ér", -1, 4, "", methodObject), 
    new Among("ír", -1, 5, "", methodObject) };
  

  private static final Among[] a_3 = {
    new Among("ic", -1, -1, "", methodObject), 
    new Among("ad", -1, -1, "", methodObject), 
    new Among("os", -1, -1, "", methodObject), 
    new Among("iv", -1, 1, "", methodObject) };
  

  private static final Among[] a_4 = {
    new Among("able", -1, 1, "", methodObject), 
    new Among("ible", -1, 1, "", methodObject), 
    new Among("ante", -1, 1, "", methodObject) };
  

  private static final Among[] a_5 = {
    new Among("ic", -1, 1, "", methodObject), 
    new Among("abil", -1, 1, "", methodObject), 
    new Among("iv", -1, 1, "", methodObject) };
  

  private static final Among[] a_6 = {
    new Among("ica", -1, 1, "", methodObject), 
    new Among("ancia", -1, 2, "", methodObject), 
    new Among("encia", -1, 5, "", methodObject), 
    new Among("adora", -1, 2, "", methodObject), 
    new Among("osa", -1, 1, "", methodObject), 
    new Among("ista", -1, 1, "", methodObject), 
    new Among("iva", -1, 9, "", methodObject), 
    new Among("anza", -1, 1, "", methodObject), 
    new Among("logía", -1, 3, "", methodObject), 
    new Among("idad", -1, 8, "", methodObject), 
    new Among("able", -1, 1, "", methodObject), 
    new Among("ible", -1, 1, "", methodObject), 
    new Among("ante", -1, 2, "", methodObject), 
    new Among("mente", -1, 7, "", methodObject), 
    new Among("amente", 13, 6, "", methodObject), 
    new Among("ación", -1, 2, "", methodObject), 
    new Among("ución", -1, 4, "", methodObject), 
    new Among("ico", -1, 1, "", methodObject), 
    new Among("ismo", -1, 1, "", methodObject), 
    new Among("oso", -1, 1, "", methodObject), 
    new Among("amiento", -1, 1, "", methodObject), 
    new Among("imiento", -1, 1, "", methodObject), 
    new Among("ivo", -1, 9, "", methodObject), 
    new Among("ador", -1, 2, "", methodObject), 
    new Among("icas", -1, 1, "", methodObject), 
    new Among("ancias", -1, 2, "", methodObject), 
    new Among("encias", -1, 5, "", methodObject), 
    new Among("adoras", -1, 2, "", methodObject), 
    new Among("osas", -1, 1, "", methodObject), 
    new Among("istas", -1, 1, "", methodObject), 
    new Among("ivas", -1, 9, "", methodObject), 
    new Among("anzas", -1, 1, "", methodObject), 
    new Among("logías", -1, 3, "", methodObject), 
    new Among("idades", -1, 8, "", methodObject), 
    new Among("ables", -1, 1, "", methodObject), 
    new Among("ibles", -1, 1, "", methodObject), 
    new Among("aciones", -1, 2, "", methodObject), 
    new Among("uciones", -1, 4, "", methodObject), 
    new Among("adores", -1, 2, "", methodObject), 
    new Among("antes", -1, 2, "", methodObject), 
    new Among("icos", -1, 1, "", methodObject), 
    new Among("ismos", -1, 1, "", methodObject), 
    new Among("osos", -1, 1, "", methodObject), 
    new Among("amientos", -1, 1, "", methodObject), 
    new Among("imientos", -1, 1, "", methodObject), 
    new Among("ivos", -1, 9, "", methodObject) };
  

  private static final Among[] a_7 = {
    new Among("ya", -1, 1, "", methodObject), 
    new Among("ye", -1, 1, "", methodObject), 
    new Among("yan", -1, 1, "", methodObject), 
    new Among("yen", -1, 1, "", methodObject), 
    new Among("yeron", -1, 1, "", methodObject), 
    new Among("yendo", -1, 1, "", methodObject), 
    new Among("yo", -1, 1, "", methodObject), 
    new Among("yas", -1, 1, "", methodObject), 
    new Among("yes", -1, 1, "", methodObject), 
    new Among("yais", -1, 1, "", methodObject), 
    new Among("yamos", -1, 1, "", methodObject), 
    new Among("yó", -1, 1, "", methodObject) };
  

  private static final Among[] a_8 = {
    new Among("aba", -1, 2, "", methodObject), 
    new Among("ada", -1, 2, "", methodObject), 
    new Among("ida", -1, 2, "", methodObject), 
    new Among("ara", -1, 2, "", methodObject), 
    new Among("iera", -1, 2, "", methodObject), 
    new Among("ía", -1, 2, "", methodObject), 
    new Among("aría", 5, 2, "", methodObject), 
    new Among("ería", 5, 2, "", methodObject), 
    new Among("iría", 5, 2, "", methodObject), 
    new Among("ad", -1, 2, "", methodObject), 
    new Among("ed", -1, 2, "", methodObject), 
    new Among("id", -1, 2, "", methodObject), 
    new Among("ase", -1, 2, "", methodObject), 
    new Among("iese", -1, 2, "", methodObject), 
    new Among("aste", -1, 2, "", methodObject), 
    new Among("iste", -1, 2, "", methodObject), 
    new Among("an", -1, 2, "", methodObject), 
    new Among("aban", 16, 2, "", methodObject), 
    new Among("aran", 16, 2, "", methodObject), 
    new Among("ieran", 16, 2, "", methodObject), 
    new Among("ían", 16, 2, "", methodObject), 
    new Among("arían", 20, 2, "", methodObject), 
    new Among("erían", 20, 2, "", methodObject), 
    new Among("irían", 20, 2, "", methodObject), 
    new Among("en", -1, 1, "", methodObject), 
    new Among("asen", 24, 2, "", methodObject), 
    new Among("iesen", 24, 2, "", methodObject), 
    new Among("aron", -1, 2, "", methodObject), 
    new Among("ieron", -1, 2, "", methodObject), 
    new Among("arán", -1, 2, "", methodObject), 
    new Among("erán", -1, 2, "", methodObject), 
    new Among("irán", -1, 2, "", methodObject), 
    new Among("ado", -1, 2, "", methodObject), 
    new Among("ido", -1, 2, "", methodObject), 
    new Among("ando", -1, 2, "", methodObject), 
    new Among("iendo", -1, 2, "", methodObject), 
    new Among("ar", -1, 2, "", methodObject), 
    new Among("er", -1, 2, "", methodObject), 
    new Among("ir", -1, 2, "", methodObject), 
    new Among("as", -1, 2, "", methodObject), 
    new Among("abas", 39, 2, "", methodObject), 
    new Among("adas", 39, 2, "", methodObject), 
    new Among("idas", 39, 2, "", methodObject), 
    new Among("aras", 39, 2, "", methodObject), 
    new Among("ieras", 39, 2, "", methodObject), 
    new Among("ías", 39, 2, "", methodObject), 
    new Among("arías", 45, 2, "", methodObject), 
    new Among("erías", 45, 2, "", methodObject), 
    new Among("irías", 45, 2, "", methodObject), 
    new Among("es", -1, 1, "", methodObject), 
    new Among("ases", 49, 2, "", methodObject), 
    new Among("ieses", 49, 2, "", methodObject), 
    new Among("abais", -1, 2, "", methodObject), 
    new Among("arais", -1, 2, "", methodObject), 
    new Among("ierais", -1, 2, "", methodObject), 
    new Among("íais", -1, 2, "", methodObject), 
    new Among("aríais", 55, 2, "", methodObject), 
    new Among("eríais", 55, 2, "", methodObject), 
    new Among("iríais", 55, 2, "", methodObject), 
    new Among("aseis", -1, 2, "", methodObject), 
    new Among("ieseis", -1, 2, "", methodObject), 
    new Among("asteis", -1, 2, "", methodObject), 
    new Among("isteis", -1, 2, "", methodObject), 
    new Among("áis", -1, 2, "", methodObject), 
    new Among("éis", -1, 1, "", methodObject), 
    new Among("aréis", 64, 2, "", methodObject), 
    new Among("eréis", 64, 2, "", methodObject), 
    new Among("iréis", 64, 2, "", methodObject), 
    new Among("ados", -1, 2, "", methodObject), 
    new Among("idos", -1, 2, "", methodObject), 
    new Among("amos", -1, 2, "", methodObject), 
    new Among("ábamos", 70, 2, "", methodObject), 
    new Among("áramos", 70, 2, "", methodObject), 
    new Among("iéramos", 70, 2, "", methodObject), 
    new Among("íamos", 70, 2, "", methodObject), 
    new Among("aríamos", 74, 2, "", methodObject), 
    new Among("eríamos", 74, 2, "", methodObject), 
    new Among("iríamos", 74, 2, "", methodObject), 
    new Among("emos", -1, 1, "", methodObject), 
    new Among("aremos", 78, 2, "", methodObject), 
    new Among("eremos", 78, 2, "", methodObject), 
    new Among("iremos", 78, 2, "", methodObject), 
    new Among("ásemos", 78, 2, "", methodObject), 
    new Among("iésemos", 78, 2, "", methodObject), 
    new Among("imos", -1, 2, "", methodObject), 
    new Among("arás", -1, 2, "", methodObject), 
    new Among("erás", -1, 2, "", methodObject), 
    new Among("irás", -1, 2, "", methodObject), 
    new Among("ís", -1, 2, "", methodObject), 
    new Among("ará", -1, 2, "", methodObject), 
    new Among("erá", -1, 2, "", methodObject), 
    new Among("irá", -1, 2, "", methodObject), 
    new Among("aré", -1, 2, "", methodObject), 
    new Among("eré", -1, 2, "", methodObject), 
    new Among("iré", -1, 2, "", methodObject), 
    new Among("ió", -1, 2, "", methodObject) };
  

  private static final Among[] a_9 = {
    new Among("a", -1, 1, "", methodObject), 
    new Among("e", -1, 2, "", methodObject), 
    new Among("o", -1, 1, "", methodObject), 
    new Among("os", -1, 1, "", methodObject), 
    new Among("á", -1, 1, "", methodObject), 
    new Among("é", -1, 2, "", methodObject), 
    new Among("í", -1, 1, "", methodObject), 
    new Among("ó", -1, 1, "", methodObject) };
  

  private static final char[] g_v = { '\021', 'A', '\020', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\001', '\021', '\004', '\n' };
  private int I_p2;
  private int I_p1;
  private int I_pV;
  
  public spanishStemmer() {}
  
  private void copy_from(spanishStemmer other) { I_p2 = I_p2;
    I_p1 = I_p1;
    I_pV = I_pV;
    super.copy_from(other);
  }
  





  private boolean r_mark_regions()
  {
    I_pV = limit;
    I_p1 = limit;
    I_p2 = limit;
    
    int v_1 = cursor;
    



    int v_2 = cursor;
    

    if (in_grouping(g_v, 97, 252))
    {




      int v_3 = cursor;
      

      if (out_grouping(g_v, 97, 252))
      {


        for (;;)
        {


          if (in_grouping(g_v, 97, 252)) {
            break label319;
          }
          


          if (cursor >= limit) {
            break;
          }
          
          cursor += 1;
        }
      }
      
      cursor = v_3;
      
      if (in_grouping(g_v, 97, 252))
      {


        for (;;)
        {


          if (out_grouping(g_v, 97, 252)) {
            break label319;
          }
          


          if (cursor >= limit) {
            break;
          }
          
          cursor += 1;
        }
      }
    }
    
    cursor = v_2;
    
    if (out_grouping(g_v, 97, 252))
    {




      int v_6 = cursor;
      

      if (out_grouping(g_v, 97, 252))
      {


        for (;;)
        {


          if (in_grouping(g_v, 97, 252)) {
            break label319;
          }
          


          if (cursor >= limit) {
            break;
          }
          
          cursor += 1;
        }
      }
      
      cursor = v_6;
      
      if (in_grouping(g_v, 97, 252))
      {



        if (cursor < limit)
        {


          cursor += 1;
          
          label319:
          
          I_pV = cursor;
        } } }
    cursor = v_1;
    
    int v_8 = cursor;
    





    while (!in_grouping(g_v, 97, 252))
    {




      if (cursor >= limit) {
        break label522;
      }
      
      cursor += 1;
    }
    



    while (!out_grouping(g_v, 97, 252))
    {




      if (cursor >= limit) {
        break label522;
      }
      
      cursor += 1;
    }
    
    I_p1 = cursor;
    



    while (!in_grouping(g_v, 97, 252))
    {




      if (cursor >= limit) {
        break label522;
      }
      
      cursor += 1;
    }
    



    while (!out_grouping(g_v, 97, 252))
    {




      if (cursor >= limit) {
        break label522;
      }
      
      cursor += 1;
    }
    
    I_p2 = cursor;
    label522:
    cursor = v_8;
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
          break label160;
        }
        
        cursor += 1;
      }
      
    }
    label160:
    cursor = v_1;
    

    return true;
  }
  
  private boolean r_RV() {
    if (I_pV > cursor)
    {
      return false;
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
  


  private boolean r_attached_pronoun()
  {
    ket = cursor;
    
    if (find_among_b(a_1, 13) == 0)
    {
      return false;
    }
    
    bra = cursor;
    
    int among_var = find_among_b(a_2, 11);
    if (among_var == 0)
    {
      return false;
    }
    
    if (!r_RV())
    {
      return false;
    }
    switch (among_var) {
    case 0: 
      return false;
    

    case 1: 
      bra = cursor;
      
      slice_from("iendo");
      break;
    

    case 2: 
      bra = cursor;
      
      slice_from("ando");
      break;
    

    case 3: 
      bra = cursor;
      
      slice_from("ar");
      break;
    

    case 4: 
      bra = cursor;
      
      slice_from("er");
      break;
    

    case 5: 
      bra = cursor;
      
      slice_from("ir");
      break;
    

    case 6: 
      slice_del();
      break;
    

    case 7: 
      if (!eq_s_b(1, "u"))
      {
        return false;
      }
      
      slice_del();
    }
    
    return true;
  }
  







  private boolean r_standard_suffix()
  {
    ket = cursor;
    
    int among_var = find_among_b(a_6, 46);
    if (among_var == 0)
    {
      return false;
    }
    
    bra = cursor;
    switch (among_var) {
    case 0: 
      return false;
    

    case 1: 
      if (!r_R2())
      {
        return false;
      }
      
      slice_del();
      break;
    

    case 2: 
      if (!r_R2())
      {
        return false;
      }
      
      slice_del();
      
      int v_1 = limit - cursor;
      


      ket = cursor;
      
      if (!eq_s_b(2, "ic"))
      {
        cursor = (limit - v_1);
      }
      else
      {
        bra = cursor;
        
        if (!r_R2())
        {
          cursor = (limit - v_1);
        }
        else
        {
          slice_del(); }
      }
      break;
    

    case 3: 
      if (!r_R2())
      {
        return false;
      }
      
      slice_from("log");
      break;
    

    case 4: 
      if (!r_R2())
      {
        return false;
      }
      
      slice_from("u");
      break;
    

    case 5: 
      if (!r_R2())
      {
        return false;
      }
      
      slice_from("ente");
      break;
    

    case 6: 
      if (!r_R1())
      {
        return false;
      }
      
      slice_del();
      
      int v_2 = limit - cursor;
      


      ket = cursor;
      
      among_var = find_among_b(a_3, 4);
      if (among_var == 0)
      {
        cursor = (limit - v_2);
      }
      else
      {
        bra = cursor;
        
        if (!r_R2())
        {
          cursor = (limit - v_2);
        }
        else
        {
          slice_del();
          switch (among_var) {
          case 0: 
            cursor = (limit - v_2);
            break;
          

          case 1: 
            ket = cursor;
            
            if (!eq_s_b(2, "at"))
            {
              cursor = (limit - v_2);
            }
            else
            {
              bra = cursor;
              
              if (!r_R2())
              {
                cursor = (limit - v_2);
              }
              else
              {
                slice_del(); }
            }
            break; }
        } }
      break;
    

    case 7: 
      if (!r_R2())
      {
        return false;
      }
      
      slice_del();
      
      int v_3 = limit - cursor;
      


      ket = cursor;
      
      among_var = find_among_b(a_4, 3);
      if (among_var == 0)
      {
        cursor = (limit - v_3);
      }
      else
      {
        bra = cursor;
        switch (among_var) {
        case 0: 
          cursor = (limit - v_3);
          break;
        

        case 1: 
          if (!r_R2())
          {
            cursor = (limit - v_3);
          }
          else
          {
            slice_del(); }
          break;
        }
      }
      break;
    

    case 8: 
      if (!r_R2())
      {
        return false;
      }
      
      slice_del();
      
      int v_4 = limit - cursor;
      


      ket = cursor;
      
      among_var = find_among_b(a_5, 3);
      if (among_var == 0)
      {
        cursor = (limit - v_4);
      }
      else
      {
        bra = cursor;
        switch (among_var) {
        case 0: 
          cursor = (limit - v_4);
          break;
        

        case 1: 
          if (!r_R2())
          {
            cursor = (limit - v_4);
          }
          else
          {
            slice_del(); }
          break;
        }
      }
      break;
    

    case 9: 
      if (!r_R2())
      {
        return false;
      }
      
      slice_del();
      
      int v_5 = limit - cursor;
      


      ket = cursor;
      
      if (!eq_s_b(2, "at"))
      {
        cursor = (limit - v_5);
      }
      else
      {
        bra = cursor;
        
        if (!r_R2())
        {
          cursor = (limit - v_5);
        }
        else
        {
          slice_del(); }
      }
      break;
    }
    return true;
  }
  




  private boolean r_y_verb_suffix()
  {
    int v_1 = limit - cursor;
    
    if (cursor < I_pV)
    {
      return false;
    }
    cursor = I_pV;
    int v_2 = limit_backward;
    limit_backward = cursor;
    cursor = (limit - v_1);
    

    ket = cursor;
    
    int among_var = find_among_b(a_7, 12);
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
      if (!eq_s_b(1, "u"))
      {
        return false;
      }
      
      slice_del();
    }
    
    return true;
  }
  






  private boolean r_verb_suffix()
  {
    int v_1 = limit - cursor;
    
    if (cursor < I_pV)
    {
      return false;
    }
    cursor = I_pV;
    int v_2 = limit_backward;
    limit_backward = cursor;
    cursor = (limit - v_1);
    

    ket = cursor;
    
    int among_var = find_among_b(a_8, 96);
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
      int v_3 = limit - cursor;
      


      if (!eq_s_b(1, "u"))
      {
        cursor = (limit - v_3);
      }
      else
      {
        int v_4 = limit - cursor;
        
        if (!eq_s_b(1, "g"))
        {
          cursor = (limit - v_3);
        }
        else {
          cursor = (limit - v_4);
        }
      }
      bra = cursor;
      
      slice_del();
      break;
    

    case 2: 
      slice_del();
    }
    
    return true;
  }
  




  private boolean r_residual_suffix()
  {
    ket = cursor;
    
    int among_var = find_among_b(a_9, 8);
    if (among_var == 0)
    {
      return false;
    }
    
    bra = cursor;
    switch (among_var) {
    case 0: 
      return false;
    

    case 1: 
      if (!r_RV())
      {
        return false;
      }
      
      slice_del();
      break;
    

    case 2: 
      if (!r_RV())
      {
        return false;
      }
      
      slice_del();
      
      int v_1 = limit - cursor;
      


      ket = cursor;
      
      if (!eq_s_b(1, "u"))
      {
        cursor = (limit - v_1);
      }
      else
      {
        bra = cursor;
        
        int v_2 = limit - cursor;
        
        if (!eq_s_b(1, "g"))
        {
          cursor = (limit - v_1);
        }
        else {
          cursor = (limit - v_2);
          
          if (!r_RV())
          {
            cursor = (limit - v_1);
          }
          else
          {
            slice_del(); }
        }
      }
      break; }
    return true;
  }
  







  public boolean stem()
  {
    int v_1 = cursor;
    

    if (!r_mark_regions()) {}
    



    cursor = v_1;
    
    limit_backward = cursor;cursor = limit;
    

    int v_2 = limit - cursor;
    

    if (!r_attached_pronoun()) {}
    



    cursor = (limit - v_2);
    
    int v_3 = limit - cursor;
    



    int v_4 = limit - cursor;
    

    if (!r_standard_suffix())
    {




      cursor = (limit - v_4);
      

      if (!r_y_verb_suffix())
      {




        cursor = (limit - v_4);
        
        if (r_verb_suffix()) {}
      }
    }
    


    cursor = (limit - v_3);
    
    int v_5 = limit - cursor;
    

    if (!r_residual_suffix()) {}
    



    cursor = (limit - v_5);
    cursor = limit_backward;
    int v_6 = cursor;
    

    if (!r_postlude()) {}
    



    cursor = v_6;
    return true;
  }
  
  public boolean equals(Object o) {
    return o instanceof spanishStemmer;
  }
  
  public int hashCode() {
    return spanishStemmer.class.getName().hashCode();
  }
}
