package org.tartarus.snowball.ext;

import org.tartarus.snowball.Among;
import org.tartarus.snowball.SnowballStemmer;







public class portugueseStemmer
  extends SnowballStemmer
{
  private static final long serialVersionUID = 1L;
  private static final portugueseStemmer methodObject = new portugueseStemmer();
  
  private static final Among[] a_0 = {
    new Among("", -1, 3, "", methodObject), 
    new Among("ã", 0, 1, "", methodObject), 
    new Among("õ", 0, 2, "", methodObject) };
  

  private static final Among[] a_1 = {
    new Among("", -1, 3, "", methodObject), 
    new Among("a~", 0, 1, "", methodObject), 
    new Among("o~", 0, 2, "", methodObject) };
  

  private static final Among[] a_2 = {
    new Among("ic", -1, -1, "", methodObject), 
    new Among("ad", -1, -1, "", methodObject), 
    new Among("os", -1, -1, "", methodObject), 
    new Among("iv", -1, 1, "", methodObject) };
  

  private static final Among[] a_3 = {
    new Among("ante", -1, 1, "", methodObject), 
    new Among("avel", -1, 1, "", methodObject), 
    new Among("ível", -1, 1, "", methodObject) };
  

  private static final Among[] a_4 = {
    new Among("ic", -1, 1, "", methodObject), 
    new Among("abil", -1, 1, "", methodObject), 
    new Among("iv", -1, 1, "", methodObject) };
  

  private static final Among[] a_5 = {
    new Among("ica", -1, 1, "", methodObject), 
    new Among("ância", -1, 1, "", methodObject), 
    new Among("ência", -1, 4, "", methodObject), 
    new Among("ira", -1, 9, "", methodObject), 
    new Among("adora", -1, 1, "", methodObject), 
    new Among("osa", -1, 1, "", methodObject), 
    new Among("ista", -1, 1, "", methodObject), 
    new Among("iva", -1, 8, "", methodObject), 
    new Among("eza", -1, 1, "", methodObject), 
    new Among("logía", -1, 2, "", methodObject), 
    new Among("idade", -1, 7, "", methodObject), 
    new Among("ante", -1, 1, "", methodObject), 
    new Among("mente", -1, 6, "", methodObject), 
    new Among("amente", 12, 5, "", methodObject), 
    new Among("ável", -1, 1, "", methodObject), 
    new Among("ível", -1, 1, "", methodObject), 
    new Among("ución", -1, 3, "", methodObject), 
    new Among("ico", -1, 1, "", methodObject), 
    new Among("ismo", -1, 1, "", methodObject), 
    new Among("oso", -1, 1, "", methodObject), 
    new Among("amento", -1, 1, "", methodObject), 
    new Among("imento", -1, 1, "", methodObject), 
    new Among("ivo", -1, 8, "", methodObject), 
    new Among("aça~o", -1, 1, "", methodObject), 
    new Among("ador", -1, 1, "", methodObject), 
    new Among("icas", -1, 1, "", methodObject), 
    new Among("ências", -1, 4, "", methodObject), 
    new Among("iras", -1, 9, "", methodObject), 
    new Among("adoras", -1, 1, "", methodObject), 
    new Among("osas", -1, 1, "", methodObject), 
    new Among("istas", -1, 1, "", methodObject), 
    new Among("ivas", -1, 8, "", methodObject), 
    new Among("ezas", -1, 1, "", methodObject), 
    new Among("logías", -1, 2, "", methodObject), 
    new Among("idades", -1, 7, "", methodObject), 
    new Among("uciones", -1, 3, "", methodObject), 
    new Among("adores", -1, 1, "", methodObject), 
    new Among("antes", -1, 1, "", methodObject), 
    new Among("aço~es", -1, 1, "", methodObject), 
    new Among("icos", -1, 1, "", methodObject), 
    new Among("ismos", -1, 1, "", methodObject), 
    new Among("osos", -1, 1, "", methodObject), 
    new Among("amentos", -1, 1, "", methodObject), 
    new Among("imentos", -1, 1, "", methodObject), 
    new Among("ivos", -1, 8, "", methodObject) };
  

  private static final Among[] a_6 = {
    new Among("ada", -1, 1, "", methodObject), 
    new Among("ida", -1, 1, "", methodObject), 
    new Among("ia", -1, 1, "", methodObject), 
    new Among("aria", 2, 1, "", methodObject), 
    new Among("eria", 2, 1, "", methodObject), 
    new Among("iria", 2, 1, "", methodObject), 
    new Among("ara", -1, 1, "", methodObject), 
    new Among("era", -1, 1, "", methodObject), 
    new Among("ira", -1, 1, "", methodObject), 
    new Among("ava", -1, 1, "", methodObject), 
    new Among("asse", -1, 1, "", methodObject), 
    new Among("esse", -1, 1, "", methodObject), 
    new Among("isse", -1, 1, "", methodObject), 
    new Among("aste", -1, 1, "", methodObject), 
    new Among("este", -1, 1, "", methodObject), 
    new Among("iste", -1, 1, "", methodObject), 
    new Among("ei", -1, 1, "", methodObject), 
    new Among("arei", 16, 1, "", methodObject), 
    new Among("erei", 16, 1, "", methodObject), 
    new Among("irei", 16, 1, "", methodObject), 
    new Among("am", -1, 1, "", methodObject), 
    new Among("iam", 20, 1, "", methodObject), 
    new Among("ariam", 21, 1, "", methodObject), 
    new Among("eriam", 21, 1, "", methodObject), 
    new Among("iriam", 21, 1, "", methodObject), 
    new Among("aram", 20, 1, "", methodObject), 
    new Among("eram", 20, 1, "", methodObject), 
    new Among("iram", 20, 1, "", methodObject), 
    new Among("avam", 20, 1, "", methodObject), 
    new Among("em", -1, 1, "", methodObject), 
    new Among("arem", 29, 1, "", methodObject), 
    new Among("erem", 29, 1, "", methodObject), 
    new Among("irem", 29, 1, "", methodObject), 
    new Among("assem", 29, 1, "", methodObject), 
    new Among("essem", 29, 1, "", methodObject), 
    new Among("issem", 29, 1, "", methodObject), 
    new Among("ado", -1, 1, "", methodObject), 
    new Among("ido", -1, 1, "", methodObject), 
    new Among("ando", -1, 1, "", methodObject), 
    new Among("endo", -1, 1, "", methodObject), 
    new Among("indo", -1, 1, "", methodObject), 
    new Among("ara~o", -1, 1, "", methodObject), 
    new Among("era~o", -1, 1, "", methodObject), 
    new Among("ira~o", -1, 1, "", methodObject), 
    new Among("ar", -1, 1, "", methodObject), 
    new Among("er", -1, 1, "", methodObject), 
    new Among("ir", -1, 1, "", methodObject), 
    new Among("as", -1, 1, "", methodObject), 
    new Among("adas", 47, 1, "", methodObject), 
    new Among("idas", 47, 1, "", methodObject), 
    new Among("ias", 47, 1, "", methodObject), 
    new Among("arias", 50, 1, "", methodObject), 
    new Among("erias", 50, 1, "", methodObject), 
    new Among("irias", 50, 1, "", methodObject), 
    new Among("aras", 47, 1, "", methodObject), 
    new Among("eras", 47, 1, "", methodObject), 
    new Among("iras", 47, 1, "", methodObject), 
    new Among("avas", 47, 1, "", methodObject), 
    new Among("es", -1, 1, "", methodObject), 
    new Among("ardes", 58, 1, "", methodObject), 
    new Among("erdes", 58, 1, "", methodObject), 
    new Among("irdes", 58, 1, "", methodObject), 
    new Among("ares", 58, 1, "", methodObject), 
    new Among("eres", 58, 1, "", methodObject), 
    new Among("ires", 58, 1, "", methodObject), 
    new Among("asses", 58, 1, "", methodObject), 
    new Among("esses", 58, 1, "", methodObject), 
    new Among("isses", 58, 1, "", methodObject), 
    new Among("astes", 58, 1, "", methodObject), 
    new Among("estes", 58, 1, "", methodObject), 
    new Among("istes", 58, 1, "", methodObject), 
    new Among("is", -1, 1, "", methodObject), 
    new Among("ais", 71, 1, "", methodObject), 
    new Among("eis", 71, 1, "", methodObject), 
    new Among("areis", 73, 1, "", methodObject), 
    new Among("ereis", 73, 1, "", methodObject), 
    new Among("ireis", 73, 1, "", methodObject), 
    new Among("áreis", 73, 1, "", methodObject), 
    new Among("éreis", 73, 1, "", methodObject), 
    new Among("íreis", 73, 1, "", methodObject), 
    new Among("ásseis", 73, 1, "", methodObject), 
    new Among("ésseis", 73, 1, "", methodObject), 
    new Among("ísseis", 73, 1, "", methodObject), 
    new Among("áveis", 73, 1, "", methodObject), 
    new Among("íeis", 73, 1, "", methodObject), 
    new Among("aríeis", 84, 1, "", methodObject), 
    new Among("eríeis", 84, 1, "", methodObject), 
    new Among("iríeis", 84, 1, "", methodObject), 
    new Among("ados", -1, 1, "", methodObject), 
    new Among("idos", -1, 1, "", methodObject), 
    new Among("amos", -1, 1, "", methodObject), 
    new Among("áramos", 90, 1, "", methodObject), 
    new Among("éramos", 90, 1, "", methodObject), 
    new Among("íramos", 90, 1, "", methodObject), 
    new Among("ávamos", 90, 1, "", methodObject), 
    new Among("íamos", 90, 1, "", methodObject), 
    new Among("aríamos", 95, 1, "", methodObject), 
    new Among("eríamos", 95, 1, "", methodObject), 
    new Among("iríamos", 95, 1, "", methodObject), 
    new Among("emos", -1, 1, "", methodObject), 
    new Among("aremos", 99, 1, "", methodObject), 
    new Among("eremos", 99, 1, "", methodObject), 
    new Among("iremos", 99, 1, "", methodObject), 
    new Among("ássemos", 99, 1, "", methodObject), 
    new Among("êssemos", 99, 1, "", methodObject), 
    new Among("íssemos", 99, 1, "", methodObject), 
    new Among("imos", -1, 1, "", methodObject), 
    new Among("armos", -1, 1, "", methodObject), 
    new Among("ermos", -1, 1, "", methodObject), 
    new Among("irmos", -1, 1, "", methodObject), 
    new Among("ámos", -1, 1, "", methodObject), 
    new Among("arás", -1, 1, "", methodObject), 
    new Among("erás", -1, 1, "", methodObject), 
    new Among("irás", -1, 1, "", methodObject), 
    new Among("eu", -1, 1, "", methodObject), 
    new Among("iu", -1, 1, "", methodObject), 
    new Among("ou", -1, 1, "", methodObject), 
    new Among("ará", -1, 1, "", methodObject), 
    new Among("erá", -1, 1, "", methodObject), 
    new Among("irá", -1, 1, "", methodObject) };
  

  private static final Among[] a_7 = {
    new Among("a", -1, 1, "", methodObject), 
    new Among("i", -1, 1, "", methodObject), 
    new Among("o", -1, 1, "", methodObject), 
    new Among("os", -1, 1, "", methodObject), 
    new Among("á", -1, 1, "", methodObject), 
    new Among("í", -1, 1, "", methodObject), 
    new Among("ó", -1, 1, "", methodObject) };
  

  private static final Among[] a_8 = {
    new Among("e", -1, 1, "", methodObject), 
    new Among("ç", -1, 2, "", methodObject), 
    new Among("é", -1, 1, "", methodObject), 
    new Among("ê", -1, 1, "", methodObject) };
  

  private static final char[] g_v = { '\021', 'A', '\020', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\003', '\023', '\f', '\002' };
  private int I_p2;
  private int I_p1;
  private int I_pV;
  
  public portugueseStemmer() {}
  
  private void copy_from(portugueseStemmer other) { I_p2 = I_p2;
    I_p1 = I_p1;
    I_pV = I_pV;
    super.copy_from(other);
  }
  

  private boolean r_prelude()
  {
    int v_1;
    for (;;)
    {
      v_1 = cursor;
      


      bra = cursor;
      
      int among_var = find_among(a_0, 3);
      if (among_var == 0) {
        break;
      }
      

      ket = cursor;
      switch (among_var)
      {
      case 0: 
        break;
      
      case 1: 
        slice_from("a~");
        break;
      

      case 2: 
        slice_from("o~");
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
  





  private boolean r_mark_regions()
  {
    I_pV = limit;
    I_p1 = limit;
    I_p2 = limit;
    
    int v_1 = cursor;
    



    int v_2 = cursor;
    

    if (in_grouping(g_v, 97, 250))
    {




      int v_3 = cursor;
      

      if (out_grouping(g_v, 97, 250))
      {


        for (;;)
        {


          if (in_grouping(g_v, 97, 250)) {
            break label319;
          }
          


          if (cursor >= limit) {
            break;
          }
          
          cursor += 1;
        }
      }
      
      cursor = v_3;
      
      if (in_grouping(g_v, 97, 250))
      {


        for (;;)
        {


          if (out_grouping(g_v, 97, 250)) {
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
    
    if (out_grouping(g_v, 97, 250))
    {




      int v_6 = cursor;
      

      if (out_grouping(g_v, 97, 250))
      {


        for (;;)
        {


          if (in_grouping(g_v, 97, 250)) {
            break label319;
          }
          


          if (cursor >= limit) {
            break;
          }
          
          cursor += 1;
        }
      }
      
      cursor = v_6;
      
      if (in_grouping(g_v, 97, 250))
      {



        if (cursor < limit)
        {


          cursor += 1;
          
          label319:
          
          I_pV = cursor;
        } } }
    cursor = v_1;
    
    int v_8 = cursor;
    





    while (!in_grouping(g_v, 97, 250))
    {




      if (cursor >= limit) {
        break label522;
      }
      
      cursor += 1;
    }
    



    while (!out_grouping(g_v, 97, 250))
    {




      if (cursor >= limit) {
        break label522;
      }
      
      cursor += 1;
    }
    
    I_p1 = cursor;
    



    while (!in_grouping(g_v, 97, 250))
    {




      if (cursor >= limit) {
        break label522;
      }
      
      cursor += 1;
    }
    



    while (!out_grouping(g_v, 97, 250))
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
        slice_from("ã");
        break;
      

      case 2: 
        slice_from("õ");
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
  






  private boolean r_standard_suffix()
  {
    ket = cursor;
    
    int among_var = find_among_b(a_5, 45);
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
      
      slice_from("log");
      break;
    

    case 3: 
      if (!r_R2())
      {
        return false;
      }
      
      slice_from("u");
      break;
    

    case 4: 
      if (!r_R2())
      {
        return false;
      }
      
      slice_from("ente");
      break;
    

    case 5: 
      if (!r_R1())
      {
        return false;
      }
      
      slice_del();
      
      int v_1 = limit - cursor;
      


      ket = cursor;
      
      among_var = find_among_b(a_2, 4);
      if (among_var == 0)
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
          slice_del();
          switch (among_var) {
          case 0: 
            cursor = (limit - v_1);
            break;
          

          case 1: 
            ket = cursor;
            
            if (!eq_s_b(2, "at"))
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
            break; }
        } }
      break;
    

    case 6: 
      if (!r_R2())
      {
        return false;
      }
      
      slice_del();
      
      int v_2 = limit - cursor;
      


      ket = cursor;
      
      among_var = find_among_b(a_3, 3);
      if (among_var == 0)
      {
        cursor = (limit - v_2);
      }
      else
      {
        bra = cursor;
        switch (among_var) {
        case 0: 
          cursor = (limit - v_2);
          break;
        

        case 1: 
          if (!r_R2())
          {
            cursor = (limit - v_2);
          }
          else
          {
            slice_del(); }
          break;
        }
      }
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
      
      if (!eq_s_b(2, "at"))
      {
        cursor = (limit - v_4);
      }
      else
      {
        bra = cursor;
        
        if (!r_R2())
        {
          cursor = (limit - v_4);
        }
        else
        {
          slice_del(); }
      }
      break;
    

    case 9: 
      if (!r_RV())
      {
        return false;
      }
      
      if (!eq_s_b(1, "e"))
      {
        return false;
      }
      
      slice_from("ir");
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
    
    int among_var = find_among_b(a_6, 120);
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
    }
    
    limit_backward = v_2;
    return true;
  }
  


  private boolean r_residual_suffix()
  {
    ket = cursor;
    
    int among_var = find_among_b(a_7, 7);
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
    }
    
    return true;
  }
  





  private boolean r_residual_form()
  {
    ket = cursor;
    
    int among_var = find_among_b(a_8, 4);
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
      
      ket = cursor;
      

      int v_1 = limit - cursor;
      


      if (eq_s_b(1, "u"))
      {



        bra = cursor;
        
        int v_2 = limit - cursor;
        
        if (eq_s_b(1, "g"))
        {


          cursor = (limit - v_2);
          break label218;
        } }
      cursor = (limit - v_1);
      

      if (!eq_s_b(1, "i"))
      {
        return false;
      }
      
      bra = cursor;
      
      int v_3 = limit - cursor;
      
      if (!eq_s_b(1, "c"))
      {
        return false;
      }
      cursor = (limit - v_3);
      

      if (!r_RV())
      {
        return false;
      }
      
      slice_del();
      break;
    case 2: 
      label218:
      
      slice_from("c");
    }
    
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
    



    int v_4 = limit - cursor;
    


    int v_5 = limit - cursor;
    


    int v_6 = limit - cursor;
    

    if (!r_standard_suffix())
    {




      cursor = (limit - v_6);
      
      if (!r_verb_suffix()) {}

    }
    else
    {
      cursor = (limit - v_5);
      
      int v_7 = limit - cursor;
      


      ket = cursor;
      
      if (eq_s_b(1, "i"))
      {



        bra = cursor;
        
        int v_8 = limit - cursor;
        
        if (eq_s_b(1, "c"))
        {


          cursor = (limit - v_8);
          
          if (r_RV())
          {



            slice_del(); }
        } }
      cursor = (limit - v_7);
      break label255;
    }
    cursor = (limit - v_4);
    
    if (!r_residual_suffix()) {}
    

    label255:
    

    cursor = (limit - v_3);
    
    int v_9 = limit - cursor;
    

    if (!r_residual_form()) {}
    



    cursor = (limit - v_9);
    cursor = limit_backward;
    int v_10 = cursor;
    

    if (!r_postlude()) {}
    



    cursor = v_10;
    return true;
  }
  
  public boolean equals(Object o) {
    return o instanceof portugueseStemmer;
  }
  
  public int hashCode() {
    return portugueseStemmer.class.getName().hashCode();
  }
}
