package org.tartarus.snowball.ext;

import org.tartarus.snowball.Among;
import org.tartarus.snowball.SnowballStemmer;







public class romanianStemmer
  extends SnowballStemmer
{
  private static final long serialVersionUID = 1L;
  private static final romanianStemmer methodObject = new romanianStemmer();
  
  private static final Among[] a_0 = {
    new Among("", -1, 3, "", methodObject), 
    new Among("I", 0, 1, "", methodObject), 
    new Among("U", 0, 2, "", methodObject) };
  

  private static final Among[] a_1 = {
    new Among("ea", -1, 3, "", methodObject), 
    new Among("aţia", -1, 7, "", methodObject), 
    new Among("aua", -1, 2, "", methodObject), 
    new Among("iua", -1, 4, "", methodObject), 
    new Among("aţie", -1, 7, "", methodObject), 
    new Among("ele", -1, 3, "", methodObject), 
    new Among("ile", -1, 5, "", methodObject), 
    new Among("iile", 6, 4, "", methodObject), 
    new Among("iei", -1, 4, "", methodObject), 
    new Among("atei", -1, 6, "", methodObject), 
    new Among("ii", -1, 4, "", methodObject), 
    new Among("ului", -1, 1, "", methodObject), 
    new Among("ul", -1, 1, "", methodObject), 
    new Among("elor", -1, 3, "", methodObject), 
    new Among("ilor", -1, 4, "", methodObject), 
    new Among("iilor", 14, 4, "", methodObject) };
  

  private static final Among[] a_2 = {
    new Among("icala", -1, 4, "", methodObject), 
    new Among("iciva", -1, 4, "", methodObject), 
    new Among("ativa", -1, 5, "", methodObject), 
    new Among("itiva", -1, 6, "", methodObject), 
    new Among("icale", -1, 4, "", methodObject), 
    new Among("aţiune", -1, 5, "", methodObject), 
    new Among("iţiune", -1, 6, "", methodObject), 
    new Among("atoare", -1, 5, "", methodObject), 
    new Among("itoare", -1, 6, "", methodObject), 
    new Among("ătoare", -1, 5, "", methodObject), 
    new Among("icitate", -1, 4, "", methodObject), 
    new Among("abilitate", -1, 1, "", methodObject), 
    new Among("ibilitate", -1, 2, "", methodObject), 
    new Among("ivitate", -1, 3, "", methodObject), 
    new Among("icive", -1, 4, "", methodObject), 
    new Among("ative", -1, 5, "", methodObject), 
    new Among("itive", -1, 6, "", methodObject), 
    new Among("icali", -1, 4, "", methodObject), 
    new Among("atori", -1, 5, "", methodObject), 
    new Among("icatori", 18, 4, "", methodObject), 
    new Among("itori", -1, 6, "", methodObject), 
    new Among("ători", -1, 5, "", methodObject), 
    new Among("icitati", -1, 4, "", methodObject), 
    new Among("abilitati", -1, 1, "", methodObject), 
    new Among("ivitati", -1, 3, "", methodObject), 
    new Among("icivi", -1, 4, "", methodObject), 
    new Among("ativi", -1, 5, "", methodObject), 
    new Among("itivi", -1, 6, "", methodObject), 
    new Among("icităi", -1, 4, "", methodObject), 
    new Among("abilităi", -1, 1, "", methodObject), 
    new Among("ivităi", -1, 3, "", methodObject), 
    new Among("icităţi", -1, 4, "", methodObject), 
    new Among("abilităţi", -1, 1, "", methodObject), 
    new Among("ivităţi", -1, 3, "", methodObject), 
    new Among("ical", -1, 4, "", methodObject), 
    new Among("ator", -1, 5, "", methodObject), 
    new Among("icator", 35, 4, "", methodObject), 
    new Among("itor", -1, 6, "", methodObject), 
    new Among("ător", -1, 5, "", methodObject), 
    new Among("iciv", -1, 4, "", methodObject), 
    new Among("ativ", -1, 5, "", methodObject), 
    new Among("itiv", -1, 6, "", methodObject), 
    new Among("icală", -1, 4, "", methodObject), 
    new Among("icivă", -1, 4, "", methodObject), 
    new Among("ativă", -1, 5, "", methodObject), 
    new Among("itivă", -1, 6, "", methodObject) };
  

  private static final Among[] a_3 = {
    new Among("ica", -1, 1, "", methodObject), 
    new Among("abila", -1, 1, "", methodObject), 
    new Among("ibila", -1, 1, "", methodObject), 
    new Among("oasa", -1, 1, "", methodObject), 
    new Among("ata", -1, 1, "", methodObject), 
    new Among("ita", -1, 1, "", methodObject), 
    new Among("anta", -1, 1, "", methodObject), 
    new Among("ista", -1, 3, "", methodObject), 
    new Among("uta", -1, 1, "", methodObject), 
    new Among("iva", -1, 1, "", methodObject), 
    new Among("ic", -1, 1, "", methodObject), 
    new Among("ice", -1, 1, "", methodObject), 
    new Among("abile", -1, 1, "", methodObject), 
    new Among("ibile", -1, 1, "", methodObject), 
    new Among("isme", -1, 3, "", methodObject), 
    new Among("iune", -1, 2, "", methodObject), 
    new Among("oase", -1, 1, "", methodObject), 
    new Among("ate", -1, 1, "", methodObject), 
    new Among("itate", 17, 1, "", methodObject), 
    new Among("ite", -1, 1, "", methodObject), 
    new Among("ante", -1, 1, "", methodObject), 
    new Among("iste", -1, 3, "", methodObject), 
    new Among("ute", -1, 1, "", methodObject), 
    new Among("ive", -1, 1, "", methodObject), 
    new Among("ici", -1, 1, "", methodObject), 
    new Among("abili", -1, 1, "", methodObject), 
    new Among("ibili", -1, 1, "", methodObject), 
    new Among("iuni", -1, 2, "", methodObject), 
    new Among("atori", -1, 1, "", methodObject), 
    new Among("osi", -1, 1, "", methodObject), 
    new Among("ati", -1, 1, "", methodObject), 
    new Among("itati", 30, 1, "", methodObject), 
    new Among("iti", -1, 1, "", methodObject), 
    new Among("anti", -1, 1, "", methodObject), 
    new Among("isti", -1, 3, "", methodObject), 
    new Among("uti", -1, 1, "", methodObject), 
    new Among("işti", -1, 3, "", methodObject), 
    new Among("ivi", -1, 1, "", methodObject), 
    new Among("ităi", -1, 1, "", methodObject), 
    new Among("oşi", -1, 1, "", methodObject), 
    new Among("ităţi", -1, 1, "", methodObject), 
    new Among("abil", -1, 1, "", methodObject), 
    new Among("ibil", -1, 1, "", methodObject), 
    new Among("ism", -1, 3, "", methodObject), 
    new Among("ator", -1, 1, "", methodObject), 
    new Among("os", -1, 1, "", methodObject), 
    new Among("at", -1, 1, "", methodObject), 
    new Among("it", -1, 1, "", methodObject), 
    new Among("ant", -1, 1, "", methodObject), 
    new Among("ist", -1, 3, "", methodObject), 
    new Among("ut", -1, 1, "", methodObject), 
    new Among("iv", -1, 1, "", methodObject), 
    new Among("ică", -1, 1, "", methodObject), 
    new Among("abilă", -1, 1, "", methodObject), 
    new Among("ibilă", -1, 1, "", methodObject), 
    new Among("oasă", -1, 1, "", methodObject), 
    new Among("ată", -1, 1, "", methodObject), 
    new Among("ită", -1, 1, "", methodObject), 
    new Among("antă", -1, 1, "", methodObject), 
    new Among("istă", -1, 3, "", methodObject), 
    new Among("ută", -1, 1, "", methodObject), 
    new Among("ivă", -1, 1, "", methodObject) };
  

  private static final Among[] a_4 = {
    new Among("ea", -1, 1, "", methodObject), 
    new Among("ia", -1, 1, "", methodObject), 
    new Among("esc", -1, 1, "", methodObject), 
    new Among("ăsc", -1, 1, "", methodObject), 
    new Among("ind", -1, 1, "", methodObject), 
    new Among("ând", -1, 1, "", methodObject), 
    new Among("are", -1, 1, "", methodObject), 
    new Among("ere", -1, 1, "", methodObject), 
    new Among("ire", -1, 1, "", methodObject), 
    new Among("âre", -1, 1, "", methodObject), 
    new Among("se", -1, 2, "", methodObject), 
    new Among("ase", 10, 1, "", methodObject), 
    new Among("sese", 10, 2, "", methodObject), 
    new Among("ise", 10, 1, "", methodObject), 
    new Among("use", 10, 1, "", methodObject), 
    new Among("âse", 10, 1, "", methodObject), 
    new Among("eşte", -1, 1, "", methodObject), 
    new Among("ăşte", -1, 1, "", methodObject), 
    new Among("eze", -1, 1, "", methodObject), 
    new Among("ai", -1, 1, "", methodObject), 
    new Among("eai", 19, 1, "", methodObject), 
    new Among("iai", 19, 1, "", methodObject), 
    new Among("sei", -1, 2, "", methodObject), 
    new Among("eşti", -1, 1, "", methodObject), 
    new Among("ăşti", -1, 1, "", methodObject), 
    new Among("ui", -1, 1, "", methodObject), 
    new Among("ezi", -1, 1, "", methodObject), 
    new Among("âi", -1, 1, "", methodObject), 
    new Among("aşi", -1, 1, "", methodObject), 
    new Among("seşi", -1, 2, "", methodObject), 
    new Among("aseşi", 29, 1, "", methodObject), 
    new Among("seseşi", 29, 2, "", methodObject), 
    new Among("iseşi", 29, 1, "", methodObject), 
    new Among("useşi", 29, 1, "", methodObject), 
    new Among("âseşi", 29, 1, "", methodObject), 
    new Among("işi", -1, 1, "", methodObject), 
    new Among("uşi", -1, 1, "", methodObject), 
    new Among("âşi", -1, 1, "", methodObject), 
    new Among("aţi", -1, 2, "", methodObject), 
    new Among("eaţi", 38, 1, "", methodObject), 
    new Among("iaţi", 38, 1, "", methodObject), 
    new Among("eţi", -1, 2, "", methodObject), 
    new Among("iţi", -1, 2, "", methodObject), 
    new Among("âţi", -1, 2, "", methodObject), 
    new Among("arăţi", -1, 1, "", methodObject), 
    new Among("serăţi", -1, 2, "", methodObject), 
    new Among("aserăţi", 45, 1, "", methodObject), 
    new Among("seserăţi", 45, 2, "", methodObject), 
    new Among("iserăţi", 45, 1, "", methodObject), 
    new Among("userăţi", 45, 1, "", methodObject), 
    new Among("âserăţi", 45, 1, "", methodObject), 
    new Among("irăţi", -1, 1, "", methodObject), 
    new Among("urăţi", -1, 1, "", methodObject), 
    new Among("ârăţi", -1, 1, "", methodObject), 
    new Among("am", -1, 1, "", methodObject), 
    new Among("eam", 54, 1, "", methodObject), 
    new Among("iam", 54, 1, "", methodObject), 
    new Among("em", -1, 2, "", methodObject), 
    new Among("asem", 57, 1, "", methodObject), 
    new Among("sesem", 57, 2, "", methodObject), 
    new Among("isem", 57, 1, "", methodObject), 
    new Among("usem", 57, 1, "", methodObject), 
    new Among("âsem", 57, 1, "", methodObject), 
    new Among("im", -1, 2, "", methodObject), 
    new Among("âm", -1, 2, "", methodObject), 
    new Among("ăm", -1, 2, "", methodObject), 
    new Among("arăm", 65, 1, "", methodObject), 
    new Among("serăm", 65, 2, "", methodObject), 
    new Among("aserăm", 67, 1, "", methodObject), 
    new Among("seserăm", 67, 2, "", methodObject), 
    new Among("iserăm", 67, 1, "", methodObject), 
    new Among("userăm", 67, 1, "", methodObject), 
    new Among("âserăm", 67, 1, "", methodObject), 
    new Among("irăm", 65, 1, "", methodObject), 
    new Among("urăm", 65, 1, "", methodObject), 
    new Among("ârăm", 65, 1, "", methodObject), 
    new Among("au", -1, 1, "", methodObject), 
    new Among("eau", 76, 1, "", methodObject), 
    new Among("iau", 76, 1, "", methodObject), 
    new Among("indu", -1, 1, "", methodObject), 
    new Among("ându", -1, 1, "", methodObject), 
    new Among("ez", -1, 1, "", methodObject), 
    new Among("ească", -1, 1, "", methodObject), 
    new Among("ară", -1, 1, "", methodObject), 
    new Among("seră", -1, 2, "", methodObject), 
    new Among("aseră", 84, 1, "", methodObject), 
    new Among("seseră", 84, 2, "", methodObject), 
    new Among("iseră", 84, 1, "", methodObject), 
    new Among("useră", 84, 1, "", methodObject), 
    new Among("âseră", 84, 1, "", methodObject), 
    new Among("iră", -1, 1, "", methodObject), 
    new Among("ură", -1, 1, "", methodObject), 
    new Among("âră", -1, 1, "", methodObject), 
    new Among("ează", -1, 1, "", methodObject) };
  

  private static final Among[] a_5 = {
    new Among("a", -1, 1, "", methodObject), 
    new Among("e", -1, 1, "", methodObject), 
    new Among("ie", 1, 1, "", methodObject), 
    new Among("i", -1, 1, "", methodObject), 
    new Among("ă", -1, 1, "", methodObject) };
  

  private static final char[] g_v = { '\021', 'A', '\020', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\002', ' ', '\000''\000''\004' };
  private boolean B_standard_suffix_removed;
  private int I_p2;
  private int I_p1;
  private int I_pV;
  
  public romanianStemmer() {}
  
  private void copy_from(romanianStemmer other) { B_standard_suffix_removed = B_standard_suffix_removed;
    I_p2 = I_p2;
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
      

      if (in_grouping(g_v, 97, 259))
      {



        bra = cursor;
        

        int v_3 = cursor;
        


        if (eq_s(1, "u"))
        {



          ket = cursor;
          if (in_grouping(g_v, 97, 259))
          {



            slice_from("U");
            break label141;
          } }
        cursor = v_3;
        

        if (eq_s(1, "i"))
        {



          ket = cursor;
          if (in_grouping(g_v, 97, 259))
          {



            slice_from("I");
            label141:
            cursor = v_2;
            break;
          } } }
      cursor = v_2;
      if (cursor >= limit) {
        break label181;
      }
      
      cursor += 1;
    }
    label181:
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
    

    if (in_grouping(g_v, 97, 259))
    {




      int v_3 = cursor;
      

      if (out_grouping(g_v, 97, 259))
      {


        for (;;)
        {


          if (in_grouping(g_v, 97, 259)) {
            break label319;
          }
          


          if (cursor >= limit) {
            break;
          }
          
          cursor += 1;
        }
      }
      
      cursor = v_3;
      
      if (in_grouping(g_v, 97, 259))
      {


        for (;;)
        {


          if (out_grouping(g_v, 97, 259)) {
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
    
    if (out_grouping(g_v, 97, 259))
    {




      int v_6 = cursor;
      

      if (out_grouping(g_v, 97, 259))
      {


        for (;;)
        {


          if (in_grouping(g_v, 97, 259)) {
            break label319;
          }
          


          if (cursor >= limit) {
            break;
          }
          
          cursor += 1;
        }
      }
      
      cursor = v_6;
      
      if (in_grouping(g_v, 97, 259))
      {



        if (cursor < limit)
        {


          cursor += 1;
          
          label319:
          
          I_pV = cursor;
        } } }
    cursor = v_1;
    
    int v_8 = cursor;
    





    while (!in_grouping(g_v, 97, 259))
    {




      if (cursor >= limit) {
        break label522;
      }
      
      cursor += 1;
    }
    



    while (!out_grouping(g_v, 97, 259))
    {




      if (cursor >= limit) {
        break label522;
      }
      
      cursor += 1;
    }
    
    I_p1 = cursor;
    



    while (!in_grouping(g_v, 97, 259))
    {




      if (cursor >= limit) {
        break label522;
      }
      
      cursor += 1;
    }
    



    while (!out_grouping(g_v, 97, 259))
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
        slice_from("i");
        break;
      

      case 2: 
        slice_from("u");
        break;
      

      case 3: 
        if (cursor >= limit) {
          break label118;
        }
        
        cursor += 1;
      }
      
    }
    label118:
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
  



  private boolean r_step_0()
  {
    ket = cursor;
    
    int among_var = find_among_b(a_1, 16);
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
      slice_del();
      break;
    

    case 2: 
      slice_from("a");
      break;
    

    case 3: 
      slice_from("e");
      break;
    

    case 4: 
      slice_from("i");
      break;
    


    case 5: 
      int v_1 = limit - cursor;
      

      if (eq_s_b(2, "ab"))
      {


        return false;
      }
      cursor = (limit - v_1);
      

      slice_from("i");
      break;
    

    case 6: 
      slice_from("at");
      break;
    

    case 7: 
      slice_from("aţi");
    }
    
    return true;
  }
  


  private boolean r_combo_suffix()
  {
    int v_1 = limit - cursor;
    

    ket = cursor;
    
    int among_var = find_among_b(a_2, 46);
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
      slice_from("abil");
      break;
    

    case 2: 
      slice_from("ibil");
      break;
    

    case 3: 
      slice_from("iv");
      break;
    

    case 4: 
      slice_from("ic");
      break;
    

    case 5: 
      slice_from("at");
      break;
    

    case 6: 
      slice_from("it");
    }
    
    
    B_standard_suffix_removed = true;
    cursor = (limit - v_1);
    return true;
  }
  



  private boolean r_standard_suffix()
  {
    B_standard_suffix_removed = false;
    int v_1;
    do
    {
      v_1 = limit - cursor;

    }
    while (r_combo_suffix());
    




    cursor = (limit - v_1);
    


    ket = cursor;
    
    int among_var = find_among_b(a_3, 62);
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
      if (!eq_s_b(1, "ţ"))
      {
        return false;
      }
      
      bra = cursor;
      
      slice_from("t");
      break;
    

    case 3: 
      slice_from("ist");
    }
    
    
    B_standard_suffix_removed = true;
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
    
    int among_var = find_among_b(a_4, 94);
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
      int v_3 = limit - cursor;
      
      if (!out_grouping_b(g_v, 97, 259))
      {




        cursor = (limit - v_3);
        
        if (!eq_s_b(1, "u"))
        {
          limit_backward = v_2;
          return false;
        }
      }
      
      slice_del();
      break;
    

    case 2: 
      slice_del();
    }
    
    limit_backward = v_2;
    return true;
  }
  


  private boolean r_vowel_suffix()
  {
    ket = cursor;
    
    int among_var = find_among_b(a_5, 5);
    if (among_var == 0)
    {
      return false;
    }
    
    bra = cursor;
    
    if (!r_RV())
    {
      return false;
    }
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
    

    if (!r_prelude()) {}
    



    cursor = v_1;
    
    int v_2 = cursor;
    

    if (!r_mark_regions()) {}
    



    cursor = v_2;
    
    limit_backward = cursor;cursor = limit;
    

    int v_3 = limit - cursor;
    

    if (!r_step_0()) {}
    



    cursor = (limit - v_3);
    
    int v_4 = limit - cursor;
    

    if (!r_standard_suffix()) {}
    



    cursor = (limit - v_4);
    
    int v_5 = limit - cursor;
    



    int v_6 = limit - cursor;
    

    if (!B_standard_suffix_removed)
    {




      cursor = (limit - v_6);
      
      if (r_verb_suffix()) {}
    }
    



    cursor = (limit - v_5);
    
    int v_7 = limit - cursor;
    

    if (!r_vowel_suffix()) {}
    



    cursor = (limit - v_7);
    cursor = limit_backward;
    int v_8 = cursor;
    

    if (!r_postlude()) {}
    



    cursor = v_8;
    return true;
  }
  
  public boolean equals(Object o) {
    return o instanceof romanianStemmer;
  }
  
  public int hashCode() {
    return romanianStemmer.class.getName().hashCode();
  }
}
