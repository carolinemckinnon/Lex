package org.tartarus.snowball.ext;

import org.tartarus.snowball.Among;
import org.tartarus.snowball.SnowballStemmer;







public class hungarianStemmer
  extends SnowballStemmer
{
  private static final long serialVersionUID = 1L;
  private static final hungarianStemmer methodObject = new hungarianStemmer();
  
  private static final Among[] a_0 = {
    new Among("cs", -1, -1, "", methodObject), 
    new Among("dzs", -1, -1, "", methodObject), 
    new Among("gy", -1, -1, "", methodObject), 
    new Among("ly", -1, -1, "", methodObject), 
    new Among("ny", -1, -1, "", methodObject), 
    new Among("sz", -1, -1, "", methodObject), 
    new Among("ty", -1, -1, "", methodObject), 
    new Among("zs", -1, -1, "", methodObject) };
  

  private static final Among[] a_1 = {
    new Among("á", -1, 1, "", methodObject), 
    new Among("é", -1, 2, "", methodObject) };
  

  private static final Among[] a_2 = {
    new Among("bb", -1, -1, "", methodObject), 
    new Among("cc", -1, -1, "", methodObject), 
    new Among("dd", -1, -1, "", methodObject), 
    new Among("ff", -1, -1, "", methodObject), 
    new Among("gg", -1, -1, "", methodObject), 
    new Among("jj", -1, -1, "", methodObject), 
    new Among("kk", -1, -1, "", methodObject), 
    new Among("ll", -1, -1, "", methodObject), 
    new Among("mm", -1, -1, "", methodObject), 
    new Among("nn", -1, -1, "", methodObject), 
    new Among("pp", -1, -1, "", methodObject), 
    new Among("rr", -1, -1, "", methodObject), 
    new Among("ccs", -1, -1, "", methodObject), 
    new Among("ss", -1, -1, "", methodObject), 
    new Among("zzs", -1, -1, "", methodObject), 
    new Among("tt", -1, -1, "", methodObject), 
    new Among("vv", -1, -1, "", methodObject), 
    new Among("ggy", -1, -1, "", methodObject), 
    new Among("lly", -1, -1, "", methodObject), 
    new Among("nny", -1, -1, "", methodObject), 
    new Among("tty", -1, -1, "", methodObject), 
    new Among("ssz", -1, -1, "", methodObject), 
    new Among("zz", -1, -1, "", methodObject) };
  

  private static final Among[] a_3 = {
    new Among("al", -1, 1, "", methodObject), 
    new Among("el", -1, 2, "", methodObject) };
  

  private static final Among[] a_4 = {
    new Among("ba", -1, -1, "", methodObject), 
    new Among("ra", -1, -1, "", methodObject), 
    new Among("be", -1, -1, "", methodObject), 
    new Among("re", -1, -1, "", methodObject), 
    new Among("ig", -1, -1, "", methodObject), 
    new Among("nak", -1, -1, "", methodObject), 
    new Among("nek", -1, -1, "", methodObject), 
    new Among("val", -1, -1, "", methodObject), 
    new Among("vel", -1, -1, "", methodObject), 
    new Among("ul", -1, -1, "", methodObject), 
    new Among("nál", -1, -1, "", methodObject), 
    new Among("nél", -1, -1, "", methodObject), 
    new Among("ból", -1, -1, "", methodObject), 
    new Among("ról", -1, -1, "", methodObject), 
    new Among("tól", -1, -1, "", methodObject), 
    new Among("bõl", -1, -1, "", methodObject), 
    new Among("rõl", -1, -1, "", methodObject), 
    new Among("tõl", -1, -1, "", methodObject), 
    new Among("ül", -1, -1, "", methodObject), 
    new Among("n", -1, -1, "", methodObject), 
    new Among("an", 19, -1, "", methodObject), 
    new Among("ban", 20, -1, "", methodObject), 
    new Among("en", 19, -1, "", methodObject), 
    new Among("ben", 22, -1, "", methodObject), 
    new Among("képpen", 22, -1, "", methodObject), 
    new Among("on", 19, -1, "", methodObject), 
    new Among("ön", 19, -1, "", methodObject), 
    new Among("képp", -1, -1, "", methodObject), 
    new Among("kor", -1, -1, "", methodObject), 
    new Among("t", -1, -1, "", methodObject), 
    new Among("at", 29, -1, "", methodObject), 
    new Among("et", 29, -1, "", methodObject), 
    new Among("ként", 29, -1, "", methodObject), 
    new Among("anként", 32, -1, "", methodObject), 
    new Among("enként", 32, -1, "", methodObject), 
    new Among("onként", 32, -1, "", methodObject), 
    new Among("ot", 29, -1, "", methodObject), 
    new Among("ért", 29, -1, "", methodObject), 
    new Among("öt", 29, -1, "", methodObject), 
    new Among("hez", -1, -1, "", methodObject), 
    new Among("hoz", -1, -1, "", methodObject), 
    new Among("höz", -1, -1, "", methodObject), 
    new Among("vá", -1, -1, "", methodObject), 
    new Among("vé", -1, -1, "", methodObject) };
  

  private static final Among[] a_5 = {
    new Among("án", -1, 2, "", methodObject), 
    new Among("én", -1, 1, "", methodObject), 
    new Among("ánként", -1, 3, "", methodObject) };
  

  private static final Among[] a_6 = {
    new Among("stul", -1, 2, "", methodObject), 
    new Among("astul", 0, 1, "", methodObject), 
    new Among("ástul", 0, 3, "", methodObject), 
    new Among("stül", -1, 2, "", methodObject), 
    new Among("estül", 3, 1, "", methodObject), 
    new Among("éstül", 3, 4, "", methodObject) };
  

  private static final Among[] a_7 = {
    new Among("á", -1, 1, "", methodObject), 
    new Among("é", -1, 2, "", methodObject) };
  

  private static final Among[] a_8 = {
    new Among("k", -1, 7, "", methodObject), 
    new Among("ak", 0, 4, "", methodObject), 
    new Among("ek", 0, 6, "", methodObject), 
    new Among("ok", 0, 5, "", methodObject), 
    new Among("ák", 0, 1, "", methodObject), 
    new Among("ék", 0, 2, "", methodObject), 
    new Among("ök", 0, 3, "", methodObject) };
  

  private static final Among[] a_9 = {
    new Among("éi", -1, 7, "", methodObject), 
    new Among("áéi", 0, 6, "", methodObject), 
    new Among("ééi", 0, 5, "", methodObject), 
    new Among("é", -1, 9, "", methodObject), 
    new Among("ké", 3, 4, "", methodObject), 
    new Among("aké", 4, 1, "", methodObject), 
    new Among("eké", 4, 1, "", methodObject), 
    new Among("oké", 4, 1, "", methodObject), 
    new Among("áké", 4, 3, "", methodObject), 
    new Among("éké", 4, 2, "", methodObject), 
    new Among("öké", 4, 1, "", methodObject), 
    new Among("éé", 3, 8, "", methodObject) };
  

  private static final Among[] a_10 = {
    new Among("a", -1, 18, "", methodObject), 
    new Among("ja", 0, 17, "", methodObject), 
    new Among("d", -1, 16, "", methodObject), 
    new Among("ad", 2, 13, "", methodObject), 
    new Among("ed", 2, 13, "", methodObject), 
    new Among("od", 2, 13, "", methodObject), 
    new Among("ád", 2, 14, "", methodObject), 
    new Among("éd", 2, 15, "", methodObject), 
    new Among("öd", 2, 13, "", methodObject), 
    new Among("e", -1, 18, "", methodObject), 
    new Among("je", 9, 17, "", methodObject), 
    new Among("nk", -1, 4, "", methodObject), 
    new Among("unk", 11, 1, "", methodObject), 
    new Among("ánk", 11, 2, "", methodObject), 
    new Among("énk", 11, 3, "", methodObject), 
    new Among("ünk", 11, 1, "", methodObject), 
    new Among("uk", -1, 8, "", methodObject), 
    new Among("juk", 16, 7, "", methodObject), 
    new Among("ájuk", 17, 5, "", methodObject), 
    new Among("ük", -1, 8, "", methodObject), 
    new Among("jük", 19, 7, "", methodObject), 
    new Among("éjük", 20, 6, "", methodObject), 
    new Among("m", -1, 12, "", methodObject), 
    new Among("am", 22, 9, "", methodObject), 
    new Among("em", 22, 9, "", methodObject), 
    new Among("om", 22, 9, "", methodObject), 
    new Among("ám", 22, 10, "", methodObject), 
    new Among("ém", 22, 11, "", methodObject), 
    new Among("o", -1, 18, "", methodObject), 
    new Among("á", -1, 19, "", methodObject), 
    new Among("é", -1, 20, "", methodObject) };
  

  private static final Among[] a_11 = {
    new Among("id", -1, 10, "", methodObject), 
    new Among("aid", 0, 9, "", methodObject), 
    new Among("jaid", 1, 6, "", methodObject), 
    new Among("eid", 0, 9, "", methodObject), 
    new Among("jeid", 3, 6, "", methodObject), 
    new Among("áid", 0, 7, "", methodObject), 
    new Among("éid", 0, 8, "", methodObject), 
    new Among("i", -1, 15, "", methodObject), 
    new Among("ai", 7, 14, "", methodObject), 
    new Among("jai", 8, 11, "", methodObject), 
    new Among("ei", 7, 14, "", methodObject), 
    new Among("jei", 10, 11, "", methodObject), 
    new Among("ái", 7, 12, "", methodObject), 
    new Among("éi", 7, 13, "", methodObject), 
    new Among("itek", -1, 24, "", methodObject), 
    new Among("eitek", 14, 21, "", methodObject), 
    new Among("jeitek", 15, 20, "", methodObject), 
    new Among("éitek", 14, 23, "", methodObject), 
    new Among("ik", -1, 29, "", methodObject), 
    new Among("aik", 18, 26, "", methodObject), 
    new Among("jaik", 19, 25, "", methodObject), 
    new Among("eik", 18, 26, "", methodObject), 
    new Among("jeik", 21, 25, "", methodObject), 
    new Among("áik", 18, 27, "", methodObject), 
    new Among("éik", 18, 28, "", methodObject), 
    new Among("ink", -1, 20, "", methodObject), 
    new Among("aink", 25, 17, "", methodObject), 
    new Among("jaink", 26, 16, "", methodObject), 
    new Among("eink", 25, 17, "", methodObject), 
    new Among("jeink", 28, 16, "", methodObject), 
    new Among("áink", 25, 18, "", methodObject), 
    new Among("éink", 25, 19, "", methodObject), 
    new Among("aitok", -1, 21, "", methodObject), 
    new Among("jaitok", 32, 20, "", methodObject), 
    new Among("áitok", -1, 22, "", methodObject), 
    new Among("im", -1, 5, "", methodObject), 
    new Among("aim", 35, 4, "", methodObject), 
    new Among("jaim", 36, 1, "", methodObject), 
    new Among("eim", 35, 4, "", methodObject), 
    new Among("jeim", 38, 1, "", methodObject), 
    new Among("áim", 35, 2, "", methodObject), 
    new Among("éim", 35, 3, "", methodObject) };
  

  private static final char[] g_v = { '\021', 'A', '\020', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\000', '\001', '\021', '4', '\016' };
  private int I_p1;
  
  public hungarianStemmer() {}
  
  private void copy_from(hungarianStemmer other) { I_p1 = I_p1;
    super.copy_from(other);
  }
  



  private boolean r_mark_regions()
  {
    I_p1 = limit;
    

    int v_1 = cursor;
    

    if (in_grouping(g_v, 97, 252))
    {


      for (;;)
      {

        int v_2 = cursor;
        
        if (out_grouping(g_v, 97, 252))
        {


          cursor = v_2;
          break;
        }
        cursor = v_2;
        if (cursor >= limit) {
          break label151;
        }
        
        cursor += 1;
      }
      
      int v_2;
      int v_3 = cursor;
      

      if (find_among(a_0, 8) == 0)
      {




        cursor = v_3;
        
        if (cursor < limit)
        {


          cursor += 1;
        }
      } else {
        I_p1 = cursor;
        break label222; } }
    label151:
    cursor = v_1;
    
    if (!out_grouping(g_v, 97, 252))
    {
      return false;
    }
    



    while (!in_grouping(g_v, 97, 252))
    {




      if (cursor >= limit)
      {
        return false;
      }
      cursor += 1;
    }
    
    I_p1 = cursor;
    label222:
    return true;
  }
  
  private boolean r_R1() {
    if (I_p1 > cursor)
    {
      return false;
    }
    return true;
  }
  


  private boolean r_v_ending()
  {
    ket = cursor;
    
    int among_var = find_among_b(a_1, 2);
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
      slice_from("a");
      break;
    

    case 2: 
      slice_from("e");
    }
    
    return true;
  }
  


  private boolean r_double()
  {
    int v_1 = limit - cursor;
    
    if (find_among_b(a_2, 23) == 0)
    {
      return false;
    }
    cursor = (limit - v_1);
    return true;
  }
  

  private boolean r_undouble()
  {
    if (cursor <= limit_backward)
    {
      return false;
    }
    cursor -= 1;
    
    ket = cursor;
    

    int c = cursor - 1;
    if ((limit_backward > c) || (c > limit))
    {
      return false;
    }
    cursor = c;
    

    bra = cursor;
    
    slice_del();
    return true;
  }
  


  private boolean r_instrum()
  {
    ket = cursor;
    
    int among_var = find_among_b(a_3, 2);
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
      if (!r_double())
      {
        return false;
      }
      

      break;
    case 2: 
      if (!r_double())
      {
        return false;
      }
      break;
    }
    
    slice_del();
    
    if (!r_undouble())
    {
      return false;
    }
    return true;
  }
  

  private boolean r_case()
  {
    ket = cursor;
    
    if (find_among_b(a_4, 44) == 0)
    {
      return false;
    }
    
    bra = cursor;
    
    if (!r_R1())
    {
      return false;
    }
    
    slice_del();
    
    if (!r_v_ending())
    {
      return false;
    }
    return true;
  }
  


  private boolean r_case_special()
  {
    ket = cursor;
    
    int among_var = find_among_b(a_5, 3);
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
      slice_from("e");
      break;
    

    case 2: 
      slice_from("a");
      break;
    

    case 3: 
      slice_from("a");
    }
    
    return true;
  }
  


  private boolean r_case_other()
  {
    ket = cursor;
    
    int among_var = find_among_b(a_6, 6);
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
      slice_del();
      break;
    

    case 3: 
      slice_from("a");
      break;
    

    case 4: 
      slice_from("e");
    }
    
    return true;
  }
  


  private boolean r_factive()
  {
    ket = cursor;
    
    int among_var = find_among_b(a_7, 2);
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
      if (!r_double())
      {
        return false;
      }
      

      break;
    case 2: 
      if (!r_double())
      {
        return false;
      }
      break;
    }
    
    slice_del();
    
    if (!r_undouble())
    {
      return false;
    }
    return true;
  }
  


  private boolean r_plural()
  {
    ket = cursor;
    
    int among_var = find_among_b(a_8, 7);
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
      slice_from("a");
      break;
    

    case 2: 
      slice_from("e");
      break;
    

    case 3: 
      slice_del();
      break;
    

    case 4: 
      slice_del();
      break;
    

    case 5: 
      slice_del();
      break;
    

    case 6: 
      slice_del();
      break;
    

    case 7: 
      slice_del();
    }
    
    return true;
  }
  


  private boolean r_owned()
  {
    ket = cursor;
    
    int among_var = find_among_b(a_9, 12);
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
      slice_from("e");
      break;
    

    case 3: 
      slice_from("a");
      break;
    

    case 4: 
      slice_del();
      break;
    

    case 5: 
      slice_from("e");
      break;
    

    case 6: 
      slice_from("a");
      break;
    

    case 7: 
      slice_del();
      break;
    

    case 8: 
      slice_from("e");
      break;
    

    case 9: 
      slice_del();
    }
    
    return true;
  }
  


  private boolean r_sing_owner()
  {
    ket = cursor;
    
    int among_var = find_among_b(a_10, 31);
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
      slice_del();
      break;
    

    case 5: 
      slice_from("a");
      break;
    

    case 6: 
      slice_from("e");
      break;
    

    case 7: 
      slice_del();
      break;
    

    case 8: 
      slice_del();
      break;
    

    case 9: 
      slice_del();
      break;
    

    case 10: 
      slice_from("a");
      break;
    

    case 11: 
      slice_from("e");
      break;
    

    case 12: 
      slice_del();
      break;
    

    case 13: 
      slice_del();
      break;
    

    case 14: 
      slice_from("a");
      break;
    

    case 15: 
      slice_from("e");
      break;
    

    case 16: 
      slice_del();
      break;
    

    case 17: 
      slice_del();
      break;
    

    case 18: 
      slice_del();
      break;
    

    case 19: 
      slice_from("a");
      break;
    

    case 20: 
      slice_from("e");
    }
    
    return true;
  }
  


  private boolean r_plur_owner()
  {
    ket = cursor;
    
    int among_var = find_among_b(a_11, 42);
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
      slice_del();
      break;
    

    case 5: 
      slice_del();
      break;
    

    case 6: 
      slice_del();
      break;
    

    case 7: 
      slice_from("a");
      break;
    

    case 8: 
      slice_from("e");
      break;
    

    case 9: 
      slice_del();
      break;
    

    case 10: 
      slice_del();
      break;
    

    case 11: 
      slice_del();
      break;
    

    case 12: 
      slice_from("a");
      break;
    

    case 13: 
      slice_from("e");
      break;
    

    case 14: 
      slice_del();
      break;
    

    case 15: 
      slice_del();
      break;
    

    case 16: 
      slice_del();
      break;
    

    case 17: 
      slice_del();
      break;
    

    case 18: 
      slice_from("a");
      break;
    

    case 19: 
      slice_from("e");
      break;
    

    case 20: 
      slice_del();
      break;
    

    case 21: 
      slice_del();
      break;
    

    case 22: 
      slice_from("a");
      break;
    

    case 23: 
      slice_from("e");
      break;
    

    case 24: 
      slice_del();
      break;
    

    case 25: 
      slice_del();
      break;
    

    case 26: 
      slice_del();
      break;
    

    case 27: 
      slice_from("a");
      break;
    

    case 28: 
      slice_from("e");
      break;
    

    case 29: 
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
    

    if (!r_instrum()) {}
    



    cursor = (limit - v_2);
    
    int v_3 = limit - cursor;
    

    if (!r_case()) {}
    



    cursor = (limit - v_3);
    
    int v_4 = limit - cursor;
    

    if (!r_case_special()) {}
    



    cursor = (limit - v_4);
    
    int v_5 = limit - cursor;
    

    if (!r_case_other()) {}
    



    cursor = (limit - v_5);
    
    int v_6 = limit - cursor;
    

    if (!r_factive()) {}
    



    cursor = (limit - v_6);
    
    int v_7 = limit - cursor;
    

    if (!r_owned()) {}
    



    cursor = (limit - v_7);
    
    int v_8 = limit - cursor;
    

    if (!r_sing_owner()) {}
    



    cursor = (limit - v_8);
    
    int v_9 = limit - cursor;
    

    if (!r_plur_owner()) {}
    



    cursor = (limit - v_9);
    
    int v_10 = limit - cursor;
    

    if (!r_plural()) {}
    



    cursor = (limit - v_10);
    cursor = limit_backward;return true;
  }
  
  public boolean equals(Object o) {
    return o instanceof hungarianStemmer;
  }
  
  public int hashCode() {
    return hungarianStemmer.class.getName().hashCode();
  }
}
