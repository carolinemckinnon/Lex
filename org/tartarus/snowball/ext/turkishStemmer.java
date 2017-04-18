package org.tartarus.snowball.ext;

import org.tartarus.snowball.Among;
import org.tartarus.snowball.SnowballStemmer;







public class turkishStemmer
  extends SnowballStemmer
{
  private static final long serialVersionUID = 1L;
  private static final turkishStemmer methodObject = new turkishStemmer();
  
  private static final Among[] a_0 = {
    new Among("m", -1, -1, "", methodObject), 
    new Among("n", -1, -1, "", methodObject), 
    new Among("miz", -1, -1, "", methodObject), 
    new Among("niz", -1, -1, "", methodObject), 
    new Among("muz", -1, -1, "", methodObject), 
    new Among("nuz", -1, -1, "", methodObject), 
    new Among("müz", -1, -1, "", methodObject), 
    new Among("nüz", -1, -1, "", methodObject), 
    new Among("mız", -1, -1, "", methodObject), 
    new Among("nız", -1, -1, "", methodObject) };
  

  private static final Among[] a_1 = {
    new Among("leri", -1, -1, "", methodObject), 
    new Among("ları", -1, -1, "", methodObject) };
  

  private static final Among[] a_2 = {
    new Among("ni", -1, -1, "", methodObject), 
    new Among("nu", -1, -1, "", methodObject), 
    new Among("nü", -1, -1, "", methodObject), 
    new Among("nı", -1, -1, "", methodObject) };
  

  private static final Among[] a_3 = {
    new Among("in", -1, -1, "", methodObject), 
    new Among("un", -1, -1, "", methodObject), 
    new Among("ün", -1, -1, "", methodObject), 
    new Among("ın", -1, -1, "", methodObject) };
  

  private static final Among[] a_4 = {
    new Among("a", -1, -1, "", methodObject), 
    new Among("e", -1, -1, "", methodObject) };
  

  private static final Among[] a_5 = {
    new Among("na", -1, -1, "", methodObject), 
    new Among("ne", -1, -1, "", methodObject) };
  

  private static final Among[] a_6 = {
    new Among("da", -1, -1, "", methodObject), 
    new Among("ta", -1, -1, "", methodObject), 
    new Among("de", -1, -1, "", methodObject), 
    new Among("te", -1, -1, "", methodObject) };
  

  private static final Among[] a_7 = {
    new Among("nda", -1, -1, "", methodObject), 
    new Among("nde", -1, -1, "", methodObject) };
  

  private static final Among[] a_8 = {
    new Among("dan", -1, -1, "", methodObject), 
    new Among("tan", -1, -1, "", methodObject), 
    new Among("den", -1, -1, "", methodObject), 
    new Among("ten", -1, -1, "", methodObject) };
  

  private static final Among[] a_9 = {
    new Among("ndan", -1, -1, "", methodObject), 
    new Among("nden", -1, -1, "", methodObject) };
  

  private static final Among[] a_10 = {
    new Among("la", -1, -1, "", methodObject), 
    new Among("le", -1, -1, "", methodObject) };
  

  private static final Among[] a_11 = {
    new Among("ca", -1, -1, "", methodObject), 
    new Among("ce", -1, -1, "", methodObject) };
  

  private static final Among[] a_12 = {
    new Among("im", -1, -1, "", methodObject), 
    new Among("um", -1, -1, "", methodObject), 
    new Among("üm", -1, -1, "", methodObject), 
    new Among("ım", -1, -1, "", methodObject) };
  

  private static final Among[] a_13 = {
    new Among("sin", -1, -1, "", methodObject), 
    new Among("sun", -1, -1, "", methodObject), 
    new Among("sün", -1, -1, "", methodObject), 
    new Among("sın", -1, -1, "", methodObject) };
  

  private static final Among[] a_14 = {
    new Among("iz", -1, -1, "", methodObject), 
    new Among("uz", -1, -1, "", methodObject), 
    new Among("üz", -1, -1, "", methodObject), 
    new Among("ız", -1, -1, "", methodObject) };
  

  private static final Among[] a_15 = {
    new Among("siniz", -1, -1, "", methodObject), 
    new Among("sunuz", -1, -1, "", methodObject), 
    new Among("sünüz", -1, -1, "", methodObject), 
    new Among("sınız", -1, -1, "", methodObject) };
  

  private static final Among[] a_16 = {
    new Among("lar", -1, -1, "", methodObject), 
    new Among("ler", -1, -1, "", methodObject) };
  

  private static final Among[] a_17 = {
    new Among("niz", -1, -1, "", methodObject), 
    new Among("nuz", -1, -1, "", methodObject), 
    new Among("nüz", -1, -1, "", methodObject), 
    new Among("nız", -1, -1, "", methodObject) };
  

  private static final Among[] a_18 = {
    new Among("dir", -1, -1, "", methodObject), 
    new Among("tir", -1, -1, "", methodObject), 
    new Among("dur", -1, -1, "", methodObject), 
    new Among("tur", -1, -1, "", methodObject), 
    new Among("dür", -1, -1, "", methodObject), 
    new Among("tür", -1, -1, "", methodObject), 
    new Among("dır", -1, -1, "", methodObject), 
    new Among("tır", -1, -1, "", methodObject) };
  

  private static final Among[] a_19 = {
    new Among("casına", -1, -1, "", methodObject), 
    new Among("cesine", -1, -1, "", methodObject) };
  

  private static final Among[] a_20 = {
    new Among("di", -1, -1, "", methodObject), 
    new Among("ti", -1, -1, "", methodObject), 
    new Among("dik", -1, -1, "", methodObject), 
    new Among("tik", -1, -1, "", methodObject), 
    new Among("duk", -1, -1, "", methodObject), 
    new Among("tuk", -1, -1, "", methodObject), 
    new Among("dük", -1, -1, "", methodObject), 
    new Among("tük", -1, -1, "", methodObject), 
    new Among("dık", -1, -1, "", methodObject), 
    new Among("tık", -1, -1, "", methodObject), 
    new Among("dim", -1, -1, "", methodObject), 
    new Among("tim", -1, -1, "", methodObject), 
    new Among("dum", -1, -1, "", methodObject), 
    new Among("tum", -1, -1, "", methodObject), 
    new Among("düm", -1, -1, "", methodObject), 
    new Among("tüm", -1, -1, "", methodObject), 
    new Among("dım", -1, -1, "", methodObject), 
    new Among("tım", -1, -1, "", methodObject), 
    new Among("din", -1, -1, "", methodObject), 
    new Among("tin", -1, -1, "", methodObject), 
    new Among("dun", -1, -1, "", methodObject), 
    new Among("tun", -1, -1, "", methodObject), 
    new Among("dün", -1, -1, "", methodObject), 
    new Among("tün", -1, -1, "", methodObject), 
    new Among("dın", -1, -1, "", methodObject), 
    new Among("tın", -1, -1, "", methodObject), 
    new Among("du", -1, -1, "", methodObject), 
    new Among("tu", -1, -1, "", methodObject), 
    new Among("dü", -1, -1, "", methodObject), 
    new Among("tü", -1, -1, "", methodObject), 
    new Among("dı", -1, -1, "", methodObject), 
    new Among("tı", -1, -1, "", methodObject) };
  

  private static final Among[] a_21 = {
    new Among("sa", -1, -1, "", methodObject), 
    new Among("se", -1, -1, "", methodObject), 
    new Among("sak", -1, -1, "", methodObject), 
    new Among("sek", -1, -1, "", methodObject), 
    new Among("sam", -1, -1, "", methodObject), 
    new Among("sem", -1, -1, "", methodObject), 
    new Among("san", -1, -1, "", methodObject), 
    new Among("sen", -1, -1, "", methodObject) };
  

  private static final Among[] a_22 = {
    new Among("miş", -1, -1, "", methodObject), 
    new Among("muş", -1, -1, "", methodObject), 
    new Among("müş", -1, -1, "", methodObject), 
    new Among("mış", -1, -1, "", methodObject) };
  

  private static final Among[] a_23 = {
    new Among("b", -1, 1, "", methodObject), 
    new Among("c", -1, 2, "", methodObject), 
    new Among("d", -1, 3, "", methodObject), 
    new Among("ğ", -1, 4, "", methodObject) };
  

  private static final char[] g_vowel = { '\021', 'A', '\020', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', ' ', '\b', '\000''\000''\000''\000''\000''\000''\001' };
  
  private static final char[] g_U = { '\001', '\020', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\b', '\000''\000''\000''\000''\000''\000''\001' };
  
  private static final char[] g_vowel1 = { '\001', '@', '\020', '\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\001' };
  
  private static final char[] g_vowel2 = { '\021', '\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''' };
  
  private static final char[] g_vowel3 = { '\001', '\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\000''\001' };
  
  private static final char[] g_vowel4 = { '\021' };
  
  private static final char[] g_vowel5 = { 'A' };
  
  private static final char[] g_vowel6 = { 'A' };
  private boolean B_continue_stemming_noun_suffixes;
  private int I_strlen;
  
  public turkishStemmer() {}
  
  private void copy_from(turkishStemmer other) { B_continue_stemming_noun_suffixes = B_continue_stemming_noun_suffixes;
    I_strlen = I_strlen;
    super.copy_from(other);
  }
  












  private boolean r_check_vowel_harmony()
  {
    int v_1 = limit - cursor;
    


    for (;;)
    {
      int v_2 = limit - cursor;
      
      if (in_grouping_b(g_vowel, 97, 305))
      {


        cursor = (limit - v_2);
        break;
      }
      cursor = (limit - v_2);
      if (cursor <= limit_backward)
      {
        return false;
      }
      cursor -= 1;
    }
    
    int v_2;
    
    int v_3 = limit - cursor;
    


    if (eq_s_b(1, "a"))
    {

      for (;;)
      {


        int v_4 = limit - cursor;
        
        if (in_grouping_b(g_vowel1, 97, 305))
        {


          cursor = (limit - v_4);
          break label922;
        }
        cursor = (limit - v_4);
        if (cursor <= limit_backward) {
          break;
        }
        
        cursor -= 1;
      }
    }
    
    cursor = (limit - v_3);
    


    if (eq_s_b(1, "e"))
    {

      for (;;)
      {


        int v_5 = limit - cursor;
        
        if (in_grouping_b(g_vowel2, 101, 252))
        {


          cursor = (limit - v_5);
          break label922;
        }
        cursor = (limit - v_5);
        if (cursor <= limit_backward) {
          break;
        }
        
        cursor -= 1;
      }
    }
    
    cursor = (limit - v_3);
    


    if (eq_s_b(1, "ı"))
    {

      for (;;)
      {


        int v_6 = limit - cursor;
        
        if (in_grouping_b(g_vowel3, 97, 305))
        {


          cursor = (limit - v_6);
          break label922;
        }
        cursor = (limit - v_6);
        if (cursor <= limit_backward) {
          break;
        }
        
        cursor -= 1;
      }
    }
    
    cursor = (limit - v_3);
    


    if (eq_s_b(1, "i"))
    {

      for (;;)
      {


        int v_7 = limit - cursor;
        
        if (in_grouping_b(g_vowel4, 101, 105))
        {


          cursor = (limit - v_7);
          break label922;
        }
        cursor = (limit - v_7);
        if (cursor <= limit_backward) {
          break;
        }
        
        cursor -= 1;
      }
    }
    
    cursor = (limit - v_3);
    


    if (eq_s_b(1, "o"))
    {

      for (;;)
      {


        int v_8 = limit - cursor;
        
        if (in_grouping_b(g_vowel5, 111, 117))
        {


          cursor = (limit - v_8);
          break label922;
        }
        cursor = (limit - v_8);
        if (cursor <= limit_backward) {
          break;
        }
        
        cursor -= 1;
      }
    }
    
    cursor = (limit - v_3);
    


    if (eq_s_b(1, "ö"))
    {

      for (;;)
      {


        int v_9 = limit - cursor;
        
        if (in_grouping_b(g_vowel6, 246, 252))
        {


          cursor = (limit - v_9);
          break label922;
        }
        cursor = (limit - v_9);
        if (cursor <= limit_backward) {
          break;
        }
        
        cursor -= 1;
      }
    }
    
    cursor = (limit - v_3);
    


    if (eq_s_b(1, "u"))
    {

      for (;;)
      {


        int v_10 = limit - cursor;
        
        if (in_grouping_b(g_vowel5, 111, 117))
        {


          cursor = (limit - v_10);
          break label922;
        }
        cursor = (limit - v_10);
        if (cursor <= limit_backward) {
          break;
        }
        
        cursor -= 1;
      }
    }
    
    cursor = (limit - v_3);
    

    if (!eq_s_b(1, "ü"))
    {
      return false;
    }
    
    for (;;)
    {
      int v_11 = limit - cursor;
      
      if (in_grouping_b(g_vowel6, 246, 252))
      {


        cursor = (limit - v_11);
        break;
      }
      cursor = (limit - v_11);
      if (cursor <= limit_backward)
      {
        return false;
      }
      cursor -= 1;
    }
    label922:
    cursor = (limit - v_1);
    return true;
  }
  









  private boolean r_mark_suffix_with_optional_n_consonant()
  {
    int v_1 = limit - cursor;
    



    int v_2 = limit - cursor;
    
    if (eq_s_b(1, "n"))
    {


      cursor = (limit - v_2);
      
      if (cursor > limit_backward)
      {


        cursor -= 1;
        

        int v_3 = limit - cursor;
        if (in_grouping_b(g_vowel, 97, 305))
        {


          cursor = (limit - v_3);
          break label261;
        } } }
    cursor = (limit - v_1);
    



    int v_4 = limit - cursor;
    


    int v_5 = limit - cursor;
    
    if (eq_s_b(1, "n"))
    {


      cursor = (limit - v_5);
      return false;
    }
    cursor = (limit - v_4);
    

    int v_6 = limit - cursor;
    

    if (cursor <= limit_backward)
    {
      return false;
    }
    cursor -= 1;
    

    int v_7 = limit - cursor;
    if (!in_grouping_b(g_vowel, 97, 305))
    {
      return false;
    }
    cursor = (limit - v_7);
    cursor = (limit - v_6);
    label261:
    return true;
  }
  









  private boolean r_mark_suffix_with_optional_s_consonant()
  {
    int v_1 = limit - cursor;
    



    int v_2 = limit - cursor;
    
    if (eq_s_b(1, "s"))
    {


      cursor = (limit - v_2);
      
      if (cursor > limit_backward)
      {


        cursor -= 1;
        

        int v_3 = limit - cursor;
        if (in_grouping_b(g_vowel, 97, 305))
        {


          cursor = (limit - v_3);
          break label263;
        } } }
    cursor = (limit - v_1);
    



    int v_4 = limit - cursor;
    


    int v_5 = limit - cursor;
    
    if (eq_s_b(1, "s"))
    {


      cursor = (limit - v_5);
      return false;
    }
    cursor = (limit - v_4);
    

    int v_6 = limit - cursor;
    

    if (cursor <= limit_backward)
    {
      return false;
    }
    cursor -= 1;
    

    int v_7 = limit - cursor;
    if (!in_grouping_b(g_vowel, 97, 305))
    {
      return false;
    }
    cursor = (limit - v_7);
    cursor = (limit - v_6);
    label263:
    return true;
  }
  









  private boolean r_mark_suffix_with_optional_y_consonant()
  {
    int v_1 = limit - cursor;
    



    int v_2 = limit - cursor;
    
    if (eq_s_b(1, "y"))
    {


      cursor = (limit - v_2);
      
      if (cursor > limit_backward)
      {


        cursor -= 1;
        

        int v_3 = limit - cursor;
        if (in_grouping_b(g_vowel, 97, 305))
        {


          cursor = (limit - v_3);
          break label263;
        } } }
    cursor = (limit - v_1);
    



    int v_4 = limit - cursor;
    


    int v_5 = limit - cursor;
    
    if (eq_s_b(1, "y"))
    {


      cursor = (limit - v_5);
      return false;
    }
    cursor = (limit - v_4);
    

    int v_6 = limit - cursor;
    

    if (cursor <= limit_backward)
    {
      return false;
    }
    cursor -= 1;
    

    int v_7 = limit - cursor;
    if (!in_grouping_b(g_vowel, 97, 305))
    {
      return false;
    }
    cursor = (limit - v_7);
    cursor = (limit - v_6);
    label263:
    return true;
  }
  









  private boolean r_mark_suffix_with_optional_U_vowel()
  {
    int v_1 = limit - cursor;
    



    int v_2 = limit - cursor;
    if (in_grouping_b(g_U, 105, 305))
    {


      cursor = (limit - v_2);
      
      if (cursor > limit_backward)
      {


        cursor -= 1;
        

        int v_3 = limit - cursor;
        if (out_grouping_b(g_vowel, 97, 305))
        {


          cursor = (limit - v_3);
          break label271;
        } } }
    cursor = (limit - v_1);
    



    int v_4 = limit - cursor;
    


    int v_5 = limit - cursor;
    if (in_grouping_b(g_U, 105, 305))
    {


      cursor = (limit - v_5);
      return false;
    }
    cursor = (limit - v_4);
    

    int v_6 = limit - cursor;
    

    if (cursor <= limit_backward)
    {
      return false;
    }
    cursor -= 1;
    

    int v_7 = limit - cursor;
    if (!out_grouping_b(g_vowel, 97, 305))
    {
      return false;
    }
    cursor = (limit - v_7);
    cursor = (limit - v_6);
    label271:
    return true;
  }
  

  private boolean r_mark_possessives()
  {
    if (find_among_b(a_0, 10) == 0)
    {
      return false;
    }
    

    if (!r_mark_suffix_with_optional_U_vowel())
    {
      return false;
    }
    return true;
  }
  

  private boolean r_mark_sU()
  {
    if (!r_check_vowel_harmony())
    {
      return false;
    }
    if (!in_grouping_b(g_U, 105, 305))
    {
      return false;
    }
    

    if (!r_mark_suffix_with_optional_s_consonant())
    {
      return false;
    }
    return true;
  }
  

  private boolean r_mark_lArI()
  {
    if (find_among_b(a_1, 2) == 0)
    {
      return false;
    }
    return true;
  }
  

  private boolean r_mark_yU()
  {
    if (!r_check_vowel_harmony())
    {
      return false;
    }
    if (!in_grouping_b(g_U, 105, 305))
    {
      return false;
    }
    

    if (!r_mark_suffix_with_optional_y_consonant())
    {
      return false;
    }
    return true;
  }
  

  private boolean r_mark_nU()
  {
    if (!r_check_vowel_harmony())
    {
      return false;
    }
    
    if (find_among_b(a_2, 4) == 0)
    {
      return false;
    }
    return true;
  }
  

  private boolean r_mark_nUn()
  {
    if (!r_check_vowel_harmony())
    {
      return false;
    }
    
    if (find_among_b(a_3, 4) == 0)
    {
      return false;
    }
    

    if (!r_mark_suffix_with_optional_n_consonant())
    {
      return false;
    }
    return true;
  }
  

  private boolean r_mark_yA()
  {
    if (!r_check_vowel_harmony())
    {
      return false;
    }
    
    if (find_among_b(a_4, 2) == 0)
    {
      return false;
    }
    

    if (!r_mark_suffix_with_optional_y_consonant())
    {
      return false;
    }
    return true;
  }
  

  private boolean r_mark_nA()
  {
    if (!r_check_vowel_harmony())
    {
      return false;
    }
    
    if (find_among_b(a_5, 2) == 0)
    {
      return false;
    }
    return true;
  }
  

  private boolean r_mark_DA()
  {
    if (!r_check_vowel_harmony())
    {
      return false;
    }
    
    if (find_among_b(a_6, 4) == 0)
    {
      return false;
    }
    return true;
  }
  

  private boolean r_mark_ndA()
  {
    if (!r_check_vowel_harmony())
    {
      return false;
    }
    
    if (find_among_b(a_7, 2) == 0)
    {
      return false;
    }
    return true;
  }
  

  private boolean r_mark_DAn()
  {
    if (!r_check_vowel_harmony())
    {
      return false;
    }
    
    if (find_among_b(a_8, 4) == 0)
    {
      return false;
    }
    return true;
  }
  

  private boolean r_mark_ndAn()
  {
    if (!r_check_vowel_harmony())
    {
      return false;
    }
    
    if (find_among_b(a_9, 2) == 0)
    {
      return false;
    }
    return true;
  }
  

  private boolean r_mark_ylA()
  {
    if (!r_check_vowel_harmony())
    {
      return false;
    }
    
    if (find_among_b(a_10, 2) == 0)
    {
      return false;
    }
    

    if (!r_mark_suffix_with_optional_y_consonant())
    {
      return false;
    }
    return true;
  }
  

  private boolean r_mark_ki()
  {
    if (!eq_s_b(2, "ki"))
    {
      return false;
    }
    return true;
  }
  

  private boolean r_mark_ncA()
  {
    if (!r_check_vowel_harmony())
    {
      return false;
    }
    
    if (find_among_b(a_11, 2) == 0)
    {
      return false;
    }
    

    if (!r_mark_suffix_with_optional_n_consonant())
    {
      return false;
    }
    return true;
  }
  

  private boolean r_mark_yUm()
  {
    if (!r_check_vowel_harmony())
    {
      return false;
    }
    
    if (find_among_b(a_12, 4) == 0)
    {
      return false;
    }
    

    if (!r_mark_suffix_with_optional_y_consonant())
    {
      return false;
    }
    return true;
  }
  

  private boolean r_mark_sUn()
  {
    if (!r_check_vowel_harmony())
    {
      return false;
    }
    
    if (find_among_b(a_13, 4) == 0)
    {
      return false;
    }
    return true;
  }
  

  private boolean r_mark_yUz()
  {
    if (!r_check_vowel_harmony())
    {
      return false;
    }
    
    if (find_among_b(a_14, 4) == 0)
    {
      return false;
    }
    

    if (!r_mark_suffix_with_optional_y_consonant())
    {
      return false;
    }
    return true;
  }
  

  private boolean r_mark_sUnUz()
  {
    if (find_among_b(a_15, 4) == 0)
    {
      return false;
    }
    return true;
  }
  

  private boolean r_mark_lAr()
  {
    if (!r_check_vowel_harmony())
    {
      return false;
    }
    
    if (find_among_b(a_16, 2) == 0)
    {
      return false;
    }
    return true;
  }
  

  private boolean r_mark_nUz()
  {
    if (!r_check_vowel_harmony())
    {
      return false;
    }
    
    if (find_among_b(a_17, 4) == 0)
    {
      return false;
    }
    return true;
  }
  

  private boolean r_mark_DUr()
  {
    if (!r_check_vowel_harmony())
    {
      return false;
    }
    
    if (find_among_b(a_18, 8) == 0)
    {
      return false;
    }
    return true;
  }
  

  private boolean r_mark_cAsInA()
  {
    if (find_among_b(a_19, 2) == 0)
    {
      return false;
    }
    return true;
  }
  

  private boolean r_mark_yDU()
  {
    if (!r_check_vowel_harmony())
    {
      return false;
    }
    
    if (find_among_b(a_20, 32) == 0)
    {
      return false;
    }
    

    if (!r_mark_suffix_with_optional_y_consonant())
    {
      return false;
    }
    return true;
  }
  

  private boolean r_mark_ysA()
  {
    if (find_among_b(a_21, 8) == 0)
    {
      return false;
    }
    

    if (!r_mark_suffix_with_optional_y_consonant())
    {
      return false;
    }
    return true;
  }
  

  private boolean r_mark_ymUs_()
  {
    if (!r_check_vowel_harmony())
    {
      return false;
    }
    
    if (find_among_b(a_22, 4) == 0)
    {
      return false;
    }
    

    if (!r_mark_suffix_with_optional_y_consonant())
    {
      return false;
    }
    return true;
  }
  

  private boolean r_mark_yken()
  {
    if (!eq_s_b(3, "ken"))
    {
      return false;
    }
    

    if (!r_mark_suffix_with_optional_y_consonant())
    {
      return false;
    }
    return true;
  }
  











  private boolean r_stem_nominal_verb_suffixes()
  {
    ket = cursor;
    
    B_continue_stemming_noun_suffixes = true;
    

    int v_1 = limit - cursor;
    



    int v_2 = limit - cursor;
    

    if (!r_mark_ymUs_())
    {




      cursor = (limit - v_2);
      

      if (!r_mark_yDU())
      {




        cursor = (limit - v_2);
        

        if (!r_mark_ysA())
        {




          cursor = (limit - v_2);
          
          if (!r_mark_yken())
          {





            cursor = (limit - v_1);
            


            if (r_mark_cAsInA())
            {





              int v_3 = limit - cursor;
              

              if (!r_mark_sUnUz())
              {




                cursor = (limit - v_3);
                

                if (!r_mark_lAr())
                {




                  cursor = (limit - v_3);
                  

                  if (!r_mark_yUm())
                  {




                    cursor = (limit - v_3);
                    

                    if (!r_mark_sUn())
                    {




                      cursor = (limit - v_3);
                      

                      if (!r_mark_yUz())
                      {




                        cursor = (limit - v_3); }
                    }
                  } } }
              if (r_mark_ymUs_()) {}

            }
            else
            {

              cursor = (limit - v_1);
              


              if (r_mark_lAr())
              {



                bra = cursor;
                
                slice_del();
                
                int v_4 = limit - cursor;
                


                ket = cursor;
                


                int v_5 = limit - cursor;
                

                if (!r_mark_DUr())
                {




                  cursor = (limit - v_5);
                  

                  if (!r_mark_yDU())
                  {




                    cursor = (limit - v_5);
                    

                    if (!r_mark_ysA())
                    {




                      cursor = (limit - v_5);
                      
                      if (!r_mark_ymUs_())
                      {
                        cursor = (limit - v_4);
                      }
                    }
                  }
                }
                
                B_continue_stemming_noun_suffixes = false;
              }
              else {
                cursor = (limit - v_1);
                


                if (r_mark_nUz())
                {





                  int v_6 = limit - cursor;
                  

                  if (!r_mark_yDU())
                  {




                    cursor = (limit - v_6);
                    
                    if (r_mark_ysA()) {}
                  }
                  

                }
                else
                {
                  cursor = (limit - v_1);
                  




                  int v_7 = limit - cursor;
                  

                  if (!r_mark_sUnUz())
                  {




                    cursor = (limit - v_7);
                    

                    if (!r_mark_yUz())
                    {




                      cursor = (limit - v_7);
                      

                      if (!r_mark_sUn())
                      {




                        cursor = (limit - v_7);
                        
                        if (!r_mark_yUm()) {
                          break label548;
                        }
                      }
                    }
                  }
                  bra = cursor;
                  
                  slice_del();
                  
                  int v_8 = limit - cursor;
                  


                  ket = cursor;
                  
                  if (!r_mark_ymUs_())
                  {
                    cursor = (limit - v_8);
                    
                    break label717;
                    
                    label548:
                    
                    cursor = (limit - v_1);
                    

                    if (!r_mark_DUr())
                    {
                      return false;
                    }
                    
                    bra = cursor;
                    
                    slice_del();
                    
                    int v_9 = limit - cursor;
                    


                    ket = cursor;
                    


                    int v_10 = limit - cursor;
                    

                    if (!r_mark_sUnUz())
                    {




                      cursor = (limit - v_10);
                      

                      if (!r_mark_lAr())
                      {




                        cursor = (limit - v_10);
                        

                        if (!r_mark_yUm())
                        {




                          cursor = (limit - v_10);
                          

                          if (!r_mark_sUn())
                          {




                            cursor = (limit - v_10);
                            

                            if (!r_mark_yUz())
                            {




                              cursor = (limit - v_10); }
                          }
                        } } }
                    if (!r_mark_ymUs_())
                    {
                      cursor = (limit - v_9); }
                  }
                }
              }
            } } } } }
    label717:
    bra = cursor;
    
    slice_del();
    return true;
  }
  












  private boolean r_stem_suffix_chain_before_ki()
  {
    ket = cursor;
    
    if (!r_mark_ki())
    {
      return false;
    }
    


    int v_1 = limit - cursor;
    


    if (r_mark_DA())
    {



      bra = cursor;
      
      slice_del();
      
      int v_2 = limit - cursor;
      


      ket = cursor;
      

      int v_3 = limit - cursor;
      


      if (r_mark_lAr())
      {



        bra = cursor;
        
        slice_del();
        
        int v_4 = limit - cursor;
        


        if (!r_stem_suffix_chain_before_ki())
        {
          cursor = (limit - v_4);
        }
        
      }
      else
      {
        cursor = (limit - v_3);
        

        if (!r_mark_possessives())
        {
          cursor = (limit - v_2);
        }
        else
        {
          bra = cursor;
          
          slice_del();
          
          int v_5 = limit - cursor;
          


          ket = cursor;
          
          if (!r_mark_lAr())
          {
            cursor = (limit - v_5);
          }
          else
          {
            bra = cursor;
            
            slice_del();
            
            if (!r_stem_suffix_chain_before_ki())
            {
              cursor = (limit - v_5);
            }
          }
        }
      }
    }
    else
    {
      cursor = (limit - v_1);
      


      if (r_mark_nUn())
      {



        bra = cursor;
        
        slice_del();
        
        int v_6 = limit - cursor;
        


        ket = cursor;
        

        int v_7 = limit - cursor;
        


        if (r_mark_lArI())
        {



          bra = cursor;
          
          slice_del();
        }
        else {
          cursor = (limit - v_7);
          


          ket = cursor;
          

          int v_8 = limit - cursor;
          

          if (!r_mark_possessives())
          {




            cursor = (limit - v_8);
            
            if (!r_mark_sU()) {}

          }
          else
          {

            bra = cursor;
            
            slice_del();
            
            int v_9 = limit - cursor;
            


            ket = cursor;
            
            if (!r_mark_lAr())
            {
              cursor = (limit - v_9);
              
              break label689;
            }
            bra = cursor;
            
            slice_del();
            
            if (r_stem_suffix_chain_before_ki())
              break label689;
            cursor = (limit - v_9);
            

            break label689;
          }
          
          cursor = (limit - v_7);
          

          if (!r_stem_suffix_chain_before_ki())
          {
            cursor = (limit - v_6);
          }
          
        }
      }
      else
      {
        cursor = (limit - v_1);
        

        if (!r_mark_ndA())
        {
          return false;
        }
        


        int v_10 = limit - cursor;
        


        if (r_mark_lArI())
        {



          bra = cursor;
          
          slice_del();
        }
        else {
          cursor = (limit - v_10);
          



          if (r_mark_sU())
          {



            bra = cursor;
            
            slice_del();
            
            int v_11 = limit - cursor;
            


            ket = cursor;
            
            if (!r_mark_lAr())
            {
              cursor = (limit - v_11);
            }
            else
            {
              bra = cursor;
              
              slice_del();
              
              if (!r_stem_suffix_chain_before_ki())
              {
                cursor = (limit - v_11);
              }
            }
          }
          else
          {
            cursor = (limit - v_10);
            

            if (!r_stem_suffix_chain_before_ki())
            {
              return false; }
          }
        } } }
    label689:
    return true;
  }
  





























  private boolean r_stem_noun_suffixes()
  {
    int v_1 = limit - cursor;
    


    ket = cursor;
    
    if (r_mark_lAr())
    {



      bra = cursor;
      
      slice_del();
      
      int v_2 = limit - cursor;
      


      if (!r_stem_suffix_chain_before_ki())
      {
        cursor = (limit - v_2);
      }
      
    }
    else
    {
      cursor = (limit - v_1);
      


      ket = cursor;
      
      if (r_mark_ncA())
      {



        bra = cursor;
        
        slice_del();
        
        int v_3 = limit - cursor;
        



        int v_4 = limit - cursor;
        


        ket = cursor;
        
        if (r_mark_lArI())
        {



          bra = cursor;
          
          slice_del();
        }
        else {
          cursor = (limit - v_4);
          


          ket = cursor;
          

          int v_5 = limit - cursor;
          

          if (!r_mark_possessives())
          {




            cursor = (limit - v_5);
            
            if (!r_mark_sU()) {}

          }
          else
          {

            bra = cursor;
            
            slice_del();
            
            int v_6 = limit - cursor;
            


            ket = cursor;
            
            if (!r_mark_lAr())
            {
              cursor = (limit - v_6);
              
              break label1791;
            }
            bra = cursor;
            
            slice_del();
            
            if (r_stem_suffix_chain_before_ki())
              break label1791;
            cursor = (limit - v_6);
            

            break label1791;
          }
          
          cursor = (limit - v_4);
          

          ket = cursor;
          
          if (!r_mark_lAr())
          {
            cursor = (limit - v_3);
          }
          else
          {
            bra = cursor;
            
            slice_del();
            
            if (!r_stem_suffix_chain_before_ki())
            {
              cursor = (limit - v_3);
            }
          }
        }
      }
      else
      {
        cursor = (limit - v_1);
        


        ket = cursor;
        


        int v_7 = limit - cursor;
        

        if (!r_mark_ndA())
        {




          cursor = (limit - v_7);
          
          if (!r_mark_nA()) {}


        }
        else
        {


          int v_8 = limit - cursor;
          


          if (r_mark_lArI())
          {



            bra = cursor;
            
            slice_del();
            break label1791;
          }
          cursor = (limit - v_8);
          


          if (r_mark_sU())
          {



            bra = cursor;
            
            slice_del();
            
            int v_9 = limit - cursor;
            


            ket = cursor;
            
            if (!r_mark_lAr())
            {
              cursor = (limit - v_9);
              
              break label1791;
            }
            bra = cursor;
            
            slice_del();
            
            if (r_stem_suffix_chain_before_ki())
              break label1791;
            cursor = (limit - v_9);
            

            break label1791;
          }
          
          cursor = (limit - v_8);
          

          if (r_stem_suffix_chain_before_ki()) {
            break label1791;
          }
        }
        


        cursor = (limit - v_1);
        


        ket = cursor;
        


        int v_10 = limit - cursor;
        

        if (!r_mark_ndAn())
        {




          cursor = (limit - v_10);
          
          if (!r_mark_nU()) {}


        }
        else
        {


          int v_11 = limit - cursor;
          


          if (r_mark_sU())
          {



            bra = cursor;
            
            slice_del();
            
            int v_12 = limit - cursor;
            


            ket = cursor;
            
            if (!r_mark_lAr())
            {
              cursor = (limit - v_12);
              
              break label1791;
            }
            bra = cursor;
            
            slice_del();
            
            if (r_stem_suffix_chain_before_ki())
              break label1791;
            cursor = (limit - v_12);
            

            break label1791;
          }
          
          cursor = (limit - v_11);
          

          if (r_mark_lArI()) {
            break label1791;
          }
        }
        


        cursor = (limit - v_1);
        


        ket = cursor;
        
        if (r_mark_DAn())
        {



          bra = cursor;
          
          slice_del();
          
          int v_13 = limit - cursor;
          


          ket = cursor;
          


          int v_14 = limit - cursor;
          


          if (r_mark_possessives())
          {



            bra = cursor;
            
            slice_del();
            
            int v_15 = limit - cursor;
            


            ket = cursor;
            
            if (!r_mark_lAr())
            {
              cursor = (limit - v_15);
            }
            else
            {
              bra = cursor;
              
              slice_del();
              
              if (!r_stem_suffix_chain_before_ki())
              {
                cursor = (limit - v_15);
              }
            }
          }
          else
          {
            cursor = (limit - v_14);
            


            if (r_mark_lAr())
            {



              bra = cursor;
              
              slice_del();
              
              int v_16 = limit - cursor;
              


              if (!r_stem_suffix_chain_before_ki())
              {
                cursor = (limit - v_16);
              }
              
            }
            else
            {
              cursor = (limit - v_14);
              

              if (!r_stem_suffix_chain_before_ki())
              {
                cursor = (limit - v_13);
              }
            }
          }
        }
        else
        {
          cursor = (limit - v_1);
          


          ket = cursor;
          

          int v_17 = limit - cursor;
          

          if (!r_mark_nUn())
          {




            cursor = (limit - v_17);
            
            if (!r_mark_ylA()) {}

          }
          else
          {

            bra = cursor;
            
            slice_del();
            
            int v_18 = limit - cursor;
            



            int v_19 = limit - cursor;
            


            ket = cursor;
            
            if (r_mark_lAr())
            {



              bra = cursor;
              
              slice_del();
              
              if (r_stem_suffix_chain_before_ki()) {
                break label1791;
              }
            }
            

            cursor = (limit - v_19);
            


            ket = cursor;
            

            int v_20 = limit - cursor;
            

            if (!r_mark_possessives())
            {




              cursor = (limit - v_20);
              
              if (!r_mark_sU()) {}

            }
            else
            {

              bra = cursor;
              
              slice_del();
              
              int v_21 = limit - cursor;
              


              ket = cursor;
              
              if (!r_mark_lAr())
              {
                cursor = (limit - v_21);
                
                break label1791;
              }
              bra = cursor;
              
              slice_del();
              
              if (r_stem_suffix_chain_before_ki())
                break label1791;
              cursor = (limit - v_21);
              

              break label1791;
            }
            
            cursor = (limit - v_19);
            
            if (r_stem_suffix_chain_before_ki())
              break label1791;
            cursor = (limit - v_18);
            

            break label1791;
          }
          

          cursor = (limit - v_1);
          


          ket = cursor;
          
          if (r_mark_lArI())
          {



            bra = cursor;
            
            slice_del();
          }
          else {
            cursor = (limit - v_1);
            


            if (!r_stem_suffix_chain_before_ki())
            {




              cursor = (limit - v_1);
              


              ket = cursor;
              

              int v_22 = limit - cursor;
              

              if (!r_mark_DA())
              {




                cursor = (limit - v_22);
                

                if (!r_mark_yU())
                {




                  cursor = (limit - v_22);
                  
                  if (!r_mark_yA()) {
                    break label1653;
                  }
                }
              }
              
              bra = cursor;
              
              slice_del();
              
              int v_23 = limit - cursor;
              


              ket = cursor;
              


              int v_24 = limit - cursor;
              


              if (r_mark_possessives())
              {



                bra = cursor;
                
                slice_del();
                
                int v_25 = limit - cursor;
                


                ket = cursor;
                
                if (!r_mark_lAr())
                {
                  cursor = (limit - v_25);
                }
                
              }
              else
              {
                cursor = (limit - v_24);
                
                if (!r_mark_lAr())
                {
                  cursor = (limit - v_23);
                  
                  break label1791;
                }
              }
              bra = cursor;
              
              slice_del();
              
              ket = cursor;
              
              if (!r_stem_suffix_chain_before_ki())
              {
                cursor = (limit - v_23);
                
                break label1791;
                
                label1653:
                
                cursor = (limit - v_1);
                

                ket = cursor;
                

                int v_26 = limit - cursor;
                

                if (!r_mark_possessives())
                {




                  cursor = (limit - v_26);
                  
                  if (!r_mark_sU())
                  {
                    return false;
                  }
                }
                
                bra = cursor;
                
                slice_del();
                
                int v_27 = limit - cursor;
                


                ket = cursor;
                
                if (!r_mark_lAr())
                {
                  cursor = (limit - v_27);
                }
                else
                {
                  bra = cursor;
                  
                  slice_del();
                  
                  if (!r_stem_suffix_chain_before_ki())
                  {
                    cursor = (limit - v_27); }
                }
              }
            } } } } }
    label1791:
    return true;
  }
  


  private boolean r_post_process_last_consonants()
  {
    ket = cursor;
    
    int among_var = find_among_b(a_23, 4);
    if (among_var == 0)
    {
      return false;
    }
    
    bra = cursor;
    switch (among_var) {
    case 0: 
      return false;
    

    case 1: 
      slice_from("p");
      break;
    

    case 2: 
      slice_from("ç");
      break;
    

    case 3: 
      slice_from("t");
      break;
    

    case 4: 
      slice_from("k");
    }
    
    return true;
  }
  
















  private boolean r_append_U_to_stems_ending_with_d_or_g()
  {
    int v_1 = limit - cursor;
    


    int v_2 = limit - cursor;
    

    if (!eq_s_b(1, "d"))
    {




      cursor = (limit - v_2);
      
      if (!eq_s_b(1, "g"))
      {
        return false;
      }
    }
    cursor = (limit - v_1);
    

    int v_3 = limit - cursor;
    


    int v_4 = limit - cursor;
    


    for (;;)
    {
      int v_5 = limit - cursor;
      
      if (in_grouping_b(g_vowel, 97, 305))
      {


        cursor = (limit - v_5);
        break;
      }
      cursor = (limit - v_5);
      if (cursor <= limit_backward) {
        break label253;
      }
      
      cursor -= 1;
    }
    
    int v_5;
    int v_6 = limit - cursor;
    

    if (!eq_s_b(1, "a"))
    {




      cursor = (limit - v_6);
      
      if (!eq_s_b(1, "ı")) {}

    }
    else
    {
      cursor = (limit - v_4);
      

      int c = cursor;
      insert(cursor, cursor, "ı");
      cursor = c;
      break label817;
    }
    label253:
    cursor = (limit - v_3);
    


    int v_7 = limit - cursor;
    


    for (;;)
    {
      int v_8 = limit - cursor;
      
      if (in_grouping_b(g_vowel, 97, 305))
      {


        cursor = (limit - v_8);
        break;
      }
      cursor = (limit - v_8);
      if (cursor <= limit_backward) {
        break label442;
      }
      
      cursor -= 1;
    }
    
    int v_8;
    int v_9 = limit - cursor;
    

    if (!eq_s_b(1, "e"))
    {




      cursor = (limit - v_9);
      
      if (!eq_s_b(1, "i")) {}

    }
    else
    {
      cursor = (limit - v_7);
      

      int c = cursor;
      insert(cursor, cursor, "i");
      cursor = c;
      break label817;
    }
    label442:
    cursor = (limit - v_3);
    


    int v_10 = limit - cursor;
    


    for (;;)
    {
      int v_11 = limit - cursor;
      
      if (in_grouping_b(g_vowel, 97, 305))
      {


        cursor = (limit - v_11);
        break;
      }
      cursor = (limit - v_11);
      if (cursor <= limit_backward) {
        break label632;
      }
      
      cursor -= 1;
    }
    
    int v_11;
    int v_12 = limit - cursor;
    

    if (!eq_s_b(1, "o"))
    {




      cursor = (limit - v_12);
      
      if (!eq_s_b(1, "u")) {}

    }
    else
    {
      cursor = (limit - v_10);
      

      int c = cursor;
      insert(cursor, cursor, "u");
      cursor = c;
      break label817;
    }
    label632:
    cursor = (limit - v_3);
    

    int v_13 = limit - cursor;
    


    for (;;)
    {
      int v_14 = limit - cursor;
      
      if (in_grouping_b(g_vowel, 97, 305))
      {


        cursor = (limit - v_14);
        break;
      }
      cursor = (limit - v_14);
      if (cursor <= limit_backward)
      {
        return false;
      }
      cursor -= 1;
    }
    
    int v_14;
    int v_15 = limit - cursor;
    

    if (!eq_s_b(1, "ö"))
    {




      cursor = (limit - v_15);
      
      if (!eq_s_b(1, "ü"))
      {
        return false;
      }
    }
    cursor = (limit - v_13);
    

    int c = cursor;
    insert(cursor, cursor, "ü");
    cursor = c;
    
    label817:
    return true;
  }
  



  private boolean r_more_than_one_syllable_word()
  {
    int v_1 = cursor;
    


    int v_2 = 2;
    int v_3;
    for (;;)
    {
      v_3 = cursor;
      





      while (!in_grouping(g_vowel, 97, 305))
      {




        if (cursor >= limit) {
          break label60;
        }
        
        cursor += 1;
      }
      v_2--;
    }
    label60:
    cursor = v_3;
    

    if (v_2 > 0)
    {
      return false;
    }
    
    cursor = v_1;
    return true;
  }
  





  private boolean r_is_reserved_word()
  {
    int v_1 = cursor;
    

    int v_2 = cursor;
    





    while (!eq_s(2, "ad"))
    {




      if (cursor >= limit) {
        break label75;
      }
      
      cursor += 1;
    }
    
    I_strlen = 2;
    
    if (I_strlen == limit)
    {


      cursor = v_2;
    } else {
      label75:
      cursor = v_1;
      
      int v_4 = cursor;
      





      while (!eq_s(5, "soyad"))
      {




        if (cursor >= limit)
        {
          return false;
        }
        cursor += 1;
      }
      
      I_strlen = 5;
      
      if (I_strlen != limit)
      {
        return false;
      }
      cursor = v_4;
    }
    return true;
  }
  





  private boolean r_postlude()
  {
    int v_1 = cursor;
    


    if (r_is_reserved_word())
    {


      return false;
    }
    cursor = v_1;
    

    limit_backward = cursor;cursor = limit;
    

    int v_2 = limit - cursor;
    

    if (!r_append_U_to_stems_ending_with_d_or_g()) {}
    



    cursor = (limit - v_2);
    
    int v_3 = limit - cursor;
    

    if (!r_post_process_last_consonants()) {}
    



    cursor = (limit - v_3);
    cursor = limit_backward;return true;
  }
  




  public boolean stem()
  {
    if (!r_more_than_one_syllable_word())
    {
      return false;
    }
    

    limit_backward = cursor;cursor = limit;
    

    int v_1 = limit - cursor;
    

    if (!r_stem_nominal_verb_suffixes()) {}
    



    cursor = (limit - v_1);
    
    if (!B_continue_stemming_noun_suffixes)
    {
      return false;
    }
    
    int v_2 = limit - cursor;
    

    if (!r_stem_noun_suffixes()) {}
    



    cursor = (limit - v_2);
    cursor = limit_backward;
    if (!r_postlude())
    {
      return false;
    }
    return true;
  }
  
  public boolean equals(Object o) {
    return o instanceof turkishStemmer;
  }
  
  public int hashCode() {
    return turkishStemmer.class.getName().hashCode();
  }
}
