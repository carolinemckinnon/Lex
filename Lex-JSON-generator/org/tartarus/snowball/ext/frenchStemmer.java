package org.tartarus.snowball.ext;

import org.tartarus.snowball.Among;
import org.tartarus.snowball.SnowballStemmer;







public class frenchStemmer
  extends SnowballStemmer
{
  private static final long serialVersionUID = 1L;
  private static final frenchStemmer methodObject = new frenchStemmer();
  
  private static final Among[] a_0 = {
    new Among("col", -1, -1, "", methodObject), 
    new Among("par", -1, -1, "", methodObject), 
    new Among("tap", -1, -1, "", methodObject) };
  

  private static final Among[] a_1 = {
    new Among("", -1, 4, "", methodObject), 
    new Among("I", 0, 1, "", methodObject), 
    new Among("U", 0, 2, "", methodObject), 
    new Among("Y", 0, 3, "", methodObject) };
  

  private static final Among[] a_2 = {
    new Among("iqU", -1, 3, "", methodObject), 
    new Among("abl", -1, 3, "", methodObject), 
    new Among("Ièr", -1, 4, "", methodObject), 
    new Among("ièr", -1, 4, "", methodObject), 
    new Among("eus", -1, 2, "", methodObject), 
    new Among("iv", -1, 1, "", methodObject) };
  

  private static final Among[] a_3 = {
    new Among("ic", -1, 2, "", methodObject), 
    new Among("abil", -1, 1, "", methodObject), 
    new Among("iv", -1, 3, "", methodObject) };
  

  private static final Among[] a_4 = {
    new Among("iqUe", -1, 1, "", methodObject), 
    new Among("atrice", -1, 2, "", methodObject), 
    new Among("ance", -1, 1, "", methodObject), 
    new Among("ence", -1, 5, "", methodObject), 
    new Among("logie", -1, 3, "", methodObject), 
    new Among("able", -1, 1, "", methodObject), 
    new Among("isme", -1, 1, "", methodObject), 
    new Among("euse", -1, 11, "", methodObject), 
    new Among("iste", -1, 1, "", methodObject), 
    new Among("ive", -1, 8, "", methodObject), 
    new Among("if", -1, 8, "", methodObject), 
    new Among("usion", -1, 4, "", methodObject), 
    new Among("ation", -1, 2, "", methodObject), 
    new Among("ution", -1, 4, "", methodObject), 
    new Among("ateur", -1, 2, "", methodObject), 
    new Among("iqUes", -1, 1, "", methodObject), 
    new Among("atrices", -1, 2, "", methodObject), 
    new Among("ances", -1, 1, "", methodObject), 
    new Among("ences", -1, 5, "", methodObject), 
    new Among("logies", -1, 3, "", methodObject), 
    new Among("ables", -1, 1, "", methodObject), 
    new Among("ismes", -1, 1, "", methodObject), 
    new Among("euses", -1, 11, "", methodObject), 
    new Among("istes", -1, 1, "", methodObject), 
    new Among("ives", -1, 8, "", methodObject), 
    new Among("ifs", -1, 8, "", methodObject), 
    new Among("usions", -1, 4, "", methodObject), 
    new Among("ations", -1, 2, "", methodObject), 
    new Among("utions", -1, 4, "", methodObject), 
    new Among("ateurs", -1, 2, "", methodObject), 
    new Among("ments", -1, 15, "", methodObject), 
    new Among("ements", 30, 6, "", methodObject), 
    new Among("issements", 31, 12, "", methodObject), 
    new Among("ités", -1, 7, "", methodObject), 
    new Among("ment", -1, 15, "", methodObject), 
    new Among("ement", 34, 6, "", methodObject), 
    new Among("issement", 35, 12, "", methodObject), 
    new Among("amment", 34, 13, "", methodObject), 
    new Among("emment", 34, 14, "", methodObject), 
    new Among("aux", -1, 10, "", methodObject), 
    new Among("eaux", 39, 9, "", methodObject), 
    new Among("eux", -1, 1, "", methodObject), 
    new Among("ité", -1, 7, "", methodObject) };
  

  private static final Among[] a_5 = {
    new Among("ira", -1, 1, "", methodObject), 
    new Among("ie", -1, 1, "", methodObject), 
    new Among("isse", -1, 1, "", methodObject), 
    new Among("issante", -1, 1, "", methodObject), 
    new Among("i", -1, 1, "", methodObject), 
    new Among("irai", 4, 1, "", methodObject), 
    new Among("ir", -1, 1, "", methodObject), 
    new Among("iras", -1, 1, "", methodObject), 
    new Among("ies", -1, 1, "", methodObject), 
    new Among("îmes", -1, 1, "", methodObject), 
    new Among("isses", -1, 1, "", methodObject), 
    new Among("issantes", -1, 1, "", methodObject), 
    new Among("îtes", -1, 1, "", methodObject), 
    new Among("is", -1, 1, "", methodObject), 
    new Among("irais", 13, 1, "", methodObject), 
    new Among("issais", 13, 1, "", methodObject), 
    new Among("irions", -1, 1, "", methodObject), 
    new Among("issions", -1, 1, "", methodObject), 
    new Among("irons", -1, 1, "", methodObject), 
    new Among("issons", -1, 1, "", methodObject), 
    new Among("issants", -1, 1, "", methodObject), 
    new Among("it", -1, 1, "", methodObject), 
    new Among("irait", 21, 1, "", methodObject), 
    new Among("issait", 21, 1, "", methodObject), 
    new Among("issant", -1, 1, "", methodObject), 
    new Among("iraIent", -1, 1, "", methodObject), 
    new Among("issaIent", -1, 1, "", methodObject), 
    new Among("irent", -1, 1, "", methodObject), 
    new Among("issent", -1, 1, "", methodObject), 
    new Among("iront", -1, 1, "", methodObject), 
    new Among("ît", -1, 1, "", methodObject), 
    new Among("iriez", -1, 1, "", methodObject), 
    new Among("issiez", -1, 1, "", methodObject), 
    new Among("irez", -1, 1, "", methodObject), 
    new Among("issez", -1, 1, "", methodObject) };
  

  private static final Among[] a_6 = {
    new Among("a", -1, 3, "", methodObject), 
    new Among("era", 0, 2, "", methodObject), 
    new Among("asse", -1, 3, "", methodObject), 
    new Among("ante", -1, 3, "", methodObject), 
    new Among("ée", -1, 2, "", methodObject), 
    new Among("ai", -1, 3, "", methodObject), 
    new Among("erai", 5, 2, "", methodObject), 
    new Among("er", -1, 2, "", methodObject), 
    new Among("as", -1, 3, "", methodObject), 
    new Among("eras", 8, 2, "", methodObject), 
    new Among("âmes", -1, 3, "", methodObject), 
    new Among("asses", -1, 3, "", methodObject), 
    new Among("antes", -1, 3, "", methodObject), 
    new Among("âtes", -1, 3, "", methodObject), 
    new Among("ées", -1, 2, "", methodObject), 
    new Among("ais", -1, 3, "", methodObject), 
    new Among("erais", 15, 2, "", methodObject), 
    new Among("ions", -1, 1, "", methodObject), 
    new Among("erions", 17, 2, "", methodObject), 
    new Among("assions", 17, 3, "", methodObject), 
    new Among("erons", -1, 2, "", methodObject), 
    new Among("ants", -1, 3, "", methodObject), 
    new Among("és", -1, 2, "", methodObject), 
    new Among("ait", -1, 3, "", methodObject), 
    new Among("erait", 23, 2, "", methodObject), 
    new Among("ant", -1, 3, "", methodObject), 
    new Among("aIent", -1, 3, "", methodObject), 
    new Among("eraIent", 26, 2, "", methodObject), 
    new Among("èrent", -1, 2, "", methodObject), 
    new Among("assent", -1, 3, "", methodObject), 
    new Among("eront", -1, 2, "", methodObject), 
    new Among("ât", -1, 3, "", methodObject), 
    new Among("ez", -1, 2, "", methodObject), 
    new Among("iez", 32, 2, "", methodObject), 
    new Among("eriez", 33, 2, "", methodObject), 
    new Among("assiez", 33, 3, "", methodObject), 
    new Among("erez", 32, 2, "", methodObject), 
    new Among("é", -1, 2, "", methodObject) };
  

  private static final Among[] a_7 = {
    new Among("e", -1, 3, "", methodObject), 
    new Among("Ière", 0, 2, "", methodObject), 
    new Among("ière", 0, 2, "", methodObject), 
    new Among("ion", -1, 1, "", methodObject), 
    new Among("Ier", -1, 2, "", methodObject), 
    new Among("ier", -1, 2, "", methodObject), 
    new Among("ë", -1, 4, "", methodObject) };
  

  private static final Among[] a_8 = {
    new Among("ell", -1, -1, "", methodObject), 
    new Among("eill", -1, -1, "", methodObject), 
    new Among("enn", -1, -1, "", methodObject), 
    new Among("onn", -1, -1, "", methodObject), 
    new Among("ett", -1, -1, "", methodObject) };
  

  private static final char[] g_v = { '\021', 'A', '\020', '\001', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '', '', 'g', '\b', '\005' };
  
  private static final char[] g_keep_with_s = { '\001', 'A', '\024', '\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''' };
  private int I_p2;
  private int I_p1;
  private int I_pV;
  
  public frenchStemmer() {}
  
  private void copy_from(frenchStemmer other) { I_p2 = I_p2;
    I_p1 = I_p1;
    I_pV = I_pV;
    super.copy_from(other);
  }
  






  private boolean r_prelude()
  {
    int v_1 = cursor;
    

    for (;;)
    {
      int v_2 = cursor;
      



      int v_3 = cursor;
      

      if (in_grouping(g_v, 97, 251))
      {



        bra = cursor;
        

        int v_4 = cursor;
        


        if (eq_s(1, "u"))
        {



          ket = cursor;
          if (in_grouping(g_v, 97, 251))
          {



            slice_from("U");
            break label304;
          } }
        cursor = v_4;
        


        if (eq_s(1, "i"))
        {



          ket = cursor;
          if (in_grouping(g_v, 97, 251))
          {



            slice_from("I");
            break label304;
          } }
        cursor = v_4;
        

        if (eq_s(1, "y"))
        {



          ket = cursor;
          
          slice_from("Y");
          break label304;
        }
      }
      cursor = v_3;
      


      bra = cursor;
      
      if (eq_s(1, "y"))
      {



        ket = cursor;
        if (in_grouping(g_v, 97, 251))
        {



          slice_from("Y");
          break label304;
        } }
      cursor = v_3;
      

      if (eq_s(1, "q"))
      {



        bra = cursor;
        
        if (eq_s(1, "u"))
        {



          ket = cursor;
          
          slice_from("U");
          label304:
          cursor = v_2;
          break;
        } }
      cursor = v_2;
      if (cursor >= limit) {
        break label344;
      }
      
      cursor += 1; }
    label344:
    int v_3;
    int v_2;
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
    

    if (in_grouping(g_v, 97, 251))
    {


      if (in_grouping(g_v, 97, 251))
      {



        if (cursor < limit)
        {


          cursor += 1;
          break label184;
        } } }
    cursor = v_2;
    

    if (find_among(a_0, 3) == 0)
    {




      cursor = v_2;
      

      if (cursor < limit)
      {


        cursor += 1;
        



        while (!in_grouping(g_v, 97, 251))
        {




          if (cursor >= limit) {
            break label192;
          }
          
          cursor += 1;
        }
      }
    } else { label184:
      I_pV = cursor; }
    label192:
    cursor = v_1;
    
    int v_4 = cursor;
    





    while (!in_grouping(g_v, 97, 251))
    {




      if (cursor >= limit) {
        break label386;
      }
      
      cursor += 1;
    }
    



    while (!out_grouping(g_v, 97, 251))
    {




      if (cursor >= limit) {
        break label386;
      }
      
      cursor += 1;
    }
    
    I_p1 = cursor;
    



    while (!in_grouping(g_v, 97, 251))
    {




      if (cursor >= limit) {
        break label386;
      }
      
      cursor += 1;
    }
    



    while (!out_grouping(g_v, 97, 251))
    {




      if (cursor >= limit) {
        break label386;
      }
      
      cursor += 1;
    }
    
    I_p2 = cursor;
    label386:
    cursor = v_4;
    return true;
  }
  

  private boolean r_postlude()
  {
    int v_1;
    for (;;)
    {
      v_1 = cursor;
      


      bra = cursor;
      
      int among_var = find_among(a_1, 4);
      if (among_var == 0) {
        break;
      }
      

      ket = cursor;
      switch (among_var)
      {
      case 0: 
        break;
      
      case 1: 
        slice_from("i");
        break;
      

      case 2: 
        slice_from("u");
        break;
      

      case 3: 
        slice_from("y");
        break;
      

      case 4: 
        if (cursor >= limit) {
          break label131;
        }
        
        cursor += 1;
      }
      
    }
    label131:
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
    
    int among_var = find_among_b(a_4, 43);
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
        

        int v_2 = limit - cursor;
        


        if (r_R2())
        {



          slice_del();
        }
        else {
          cursor = (limit - v_2);
          
          slice_from("iqU");
        }
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
      
      slice_from("ent");
      break;
    

    case 6: 
      if (!r_RV())
      {
        return false;
      }
      
      slice_del();
      
      int v_3 = limit - cursor;
      


      ket = cursor;
      
      among_var = find_among_b(a_2, 6);
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
            slice_del();
            
            ket = cursor;
            
            if (!eq_s_b(2, "at"))
            {
              cursor = (limit - v_3);
            }
            else
            {
              bra = cursor;
              
              if (!r_R2())
              {
                cursor = (limit - v_3);
              }
              else
              {
                slice_del(); } } }
          break;
        


        case 2: 
          int v_4 = limit - cursor;
          


          if (r_R2())
          {



            slice_del();
          }
          else {
            cursor = (limit - v_4);
            

            if (!r_R1())
            {
              cursor = (limit - v_3);
            }
            else
            {
              slice_from("eux"); }
          }
          break;
        

        case 3: 
          if (!r_R2())
          {
            cursor = (limit - v_3);
          }
          else
          {
            slice_del(); }
          break;
        

        case 4: 
          if (!r_RV())
          {
            cursor = (limit - v_3);
          }
          else
          {
            slice_from("i"); }
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
      
      int v_5 = limit - cursor;
      


      ket = cursor;
      
      among_var = find_among_b(a_3, 3);
      if (among_var == 0)
      {
        cursor = (limit - v_5);
      }
      else
      {
        bra = cursor;
        switch (among_var) {
        case 0: 
          cursor = (limit - v_5);
          break;
        


        case 1: 
          int v_6 = limit - cursor;
          


          if (r_R2())
          {



            slice_del();
          }
          else {
            cursor = (limit - v_6);
            
            slice_from("abl");
          }
          break;
        


        case 2: 
          int v_7 = limit - cursor;
          


          if (r_R2())
          {



            slice_del();
          }
          else {
            cursor = (limit - v_7);
            
            slice_from("iqU");
          }
          break;
        

        case 3: 
          if (!r_R2())
          {
            cursor = (limit - v_5);
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
      
      int v_8 = limit - cursor;
      


      ket = cursor;
      
      if (!eq_s_b(2, "at"))
      {
        cursor = (limit - v_8);
      }
      else
      {
        bra = cursor;
        
        if (!r_R2())
        {
          cursor = (limit - v_8);
        }
        else
        {
          slice_del();
          
          ket = cursor;
          
          if (!eq_s_b(2, "ic"))
          {
            cursor = (limit - v_8);
          }
          else
          {
            bra = cursor;
            

            int v_9 = limit - cursor;
            


            if (r_R2())
            {



              slice_del();
            }
            else {
              cursor = (limit - v_9);
              
              slice_from("iqU");
            }
          } } }
      break;
    

    case 9: 
      slice_from("eau");
      break;
    

    case 10: 
      if (!r_R1())
      {
        return false;
      }
      
      slice_from("al");
      break;
    


    case 11: 
      int v_10 = limit - cursor;
      


      if (r_R2())
      {



        slice_del();
      }
      else {
        cursor = (limit - v_10);
        

        if (!r_R1())
        {
          return false;
        }
        
        slice_from("eux");
      }
      break;
    

    case 12: 
      if (!r_R1())
      {
        return false;
      }
      if (!out_grouping_b(g_v, 97, 251))
      {
        return false;
      }
      
      slice_del();
      break;
    

    case 13: 
      if (!r_RV())
      {
        return false;
      }
      


      slice_from("ant");
      return false;
    

    case 14: 
      if (!r_RV())
      {
        return false;
      }
      


      slice_from("ent");
      return false;
    

    case 15: 
      int v_11 = limit - cursor;
      
      if (!in_grouping_b(g_v, 97, 251))
      {
        return false;
      }
      
      if (!r_RV())
      {
        return false;
      }
      cursor = (limit - v_11);
      


      slice_del();
      return false;
    }
    return true;
  }
  



  private boolean r_i_verb_suffix()
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
    
    int among_var = find_among_b(a_5, 35);
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
      if (!out_grouping_b(g_v, 97, 251))
      {
        limit_backward = v_2;
        return false;
      }
      
      slice_del();
    }
    
    limit_backward = v_2;
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
    
    int among_var = find_among_b(a_6, 38);
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
      if (!r_R2())
      {
        limit_backward = v_2;
        return false;
      }
      
      slice_del();
      break;
    

    case 2: 
      slice_del();
      break;
    

    case 3: 
      slice_del();
      
      int v_3 = limit - cursor;
      


      ket = cursor;
      
      if (!eq_s_b(1, "e"))
      {
        cursor = (limit - v_3);
      }
      else
      {
        bra = cursor;
        
        slice_del();
      }
      break;
    }
    limit_backward = v_2;
    return true;
  }
  







  private boolean r_residual_suffix()
  {
    int v_1 = limit - cursor;
    


    ket = cursor;
    
    if (!eq_s_b(1, "s"))
    {
      cursor = (limit - v_1);
    }
    else
    {
      bra = cursor;
      
      int v_2 = limit - cursor;
      if (!out_grouping_b(g_keep_with_s, 97, 232))
      {
        cursor = (limit - v_1);
      }
      else {
        cursor = (limit - v_2);
        
        slice_del();
      }
    }
    int v_3 = limit - cursor;
    
    if (cursor < I_pV)
    {
      return false;
    }
    cursor = I_pV;
    int v_4 = limit_backward;
    limit_backward = cursor;
    cursor = (limit - v_3);
    

    ket = cursor;
    
    int among_var = find_among_b(a_7, 7);
    if (among_var == 0)
    {
      limit_backward = v_4;
      return false;
    }
    
    bra = cursor;
    switch (among_var) {
    case 0: 
      limit_backward = v_4;
      return false;
    

    case 1: 
      if (!r_R2())
      {
        limit_backward = v_4;
        return false;
      }
      

      int v_5 = limit - cursor;
      

      if (!eq_s_b(1, "s"))
      {




        cursor = (limit - v_5);
        
        if (!eq_s_b(1, "t"))
        {
          limit_backward = v_4;
          return false;
        }
      }
      
      slice_del();
      break;
    

    case 2: 
      slice_from("i");
      break;
    

    case 3: 
      slice_del();
      break;
    

    case 4: 
      if (!eq_s_b(2, "gu"))
      {
        limit_backward = v_4;
        return false;
      }
      
      slice_del();
    }
    
    limit_backward = v_4;
    return true;
  }
  


  private boolean r_un_double()
  {
    int v_1 = limit - cursor;
    
    if (find_among_b(a_8, 5) == 0)
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
  



  private boolean r_un_accent()
  {
    int v_1 = 1;
    



    while (out_grouping_b(g_v, 97, 251))
    {


      v_1--;
    }
    


    if (v_1 > 0)
    {
      return false;
    }
    

    ket = cursor;
    

    int v_3 = limit - cursor;
    

    if (!eq_s_b(1, "é"))
    {




      cursor = (limit - v_3);
      
      if (!eq_s_b(1, "è"))
      {
        return false;
      }
    }
    
    bra = cursor;
    
    slice_from("e");
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
      

      if (!r_i_verb_suffix())
      {




        cursor = (limit - v_6);
        
        if (!r_verb_suffix()) {
          break label264;
        }
      }
    }
    cursor = (limit - v_5);
    
    int v_7 = limit - cursor;
    


    ket = cursor;
    

    int v_8 = limit - cursor;
    


    if (eq_s_b(1, "Y"))
    {



      bra = cursor;
      
      slice_from("i");
    }
    else {
      cursor = (limit - v_8);
      

      if (!eq_s_b(1, "ç"))
      {
        cursor = (limit - v_7);
      }
      else
      {
        bra = cursor;
        
        slice_from("c");
        
        break label282;
        
        label264:
        cursor = (limit - v_4);
        
        if (r_residual_suffix()) {}
      }
    }
    
    label282:
    
    cursor = (limit - v_3);
    
    int v_9 = limit - cursor;
    

    if (!r_un_double()) {}
    



    cursor = (limit - v_9);
    
    int v_10 = limit - cursor;
    

    if (!r_un_accent()) {}
    



    cursor = (limit - v_10);
    cursor = limit_backward;
    int v_11 = cursor;
    

    if (!r_postlude()) {}
    



    cursor = v_11;
    return true;
  }
  
  public boolean equals(Object o) {
    return o instanceof frenchStemmer;
  }
  
  public int hashCode() {
    return frenchStemmer.class.getName().hashCode();
  }
}
