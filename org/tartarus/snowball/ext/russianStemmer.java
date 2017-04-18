package org.tartarus.snowball.ext;

import org.tartarus.snowball.Among;
import org.tartarus.snowball.SnowballStemmer;







public class russianStemmer
  extends SnowballStemmer
{
  private static final long serialVersionUID = 1L;
  private static final russianStemmer methodObject = new russianStemmer();
  
  private static final Among[] a_0 = {
    new Among("в", -1, 1, "", methodObject), 
    new Among("ив", 0, 2, "", methodObject), 
    new Among("ыв", 0, 2, "", methodObject), 
    new Among("вши", -1, 1, "", methodObject), 
    new Among("ивши", 3, 2, "", methodObject), 
    new Among("ывши", 3, 2, "", methodObject), 
    new Among("вшись", -1, 1, "", methodObject), 
    new Among("ившись", 6, 2, "", methodObject), 
    new Among("ывшись", 6, 2, "", methodObject) };
  

  private static final Among[] a_1 = {
    new Among("ее", -1, 1, "", methodObject), 
    new Among("ие", -1, 1, "", methodObject), 
    new Among("ое", -1, 1, "", methodObject), 
    new Among("ые", -1, 1, "", methodObject), 
    new Among("ими", -1, 1, "", methodObject), 
    new Among("ыми", -1, 1, "", methodObject), 
    new Among("ей", -1, 1, "", methodObject), 
    new Among("ий", -1, 1, "", methodObject), 
    new Among("ой", -1, 1, "", methodObject), 
    new Among("ый", -1, 1, "", methodObject), 
    new Among("ем", -1, 1, "", methodObject), 
    new Among("им", -1, 1, "", methodObject), 
    new Among("ом", -1, 1, "", methodObject), 
    new Among("ым", -1, 1, "", methodObject), 
    new Among("его", -1, 1, "", methodObject), 
    new Among("ого", -1, 1, "", methodObject), 
    new Among("ему", -1, 1, "", methodObject), 
    new Among("ому", -1, 1, "", methodObject), 
    new Among("их", -1, 1, "", methodObject), 
    new Among("ых", -1, 1, "", methodObject), 
    new Among("ею", -1, 1, "", methodObject), 
    new Among("ою", -1, 1, "", methodObject), 
    new Among("ую", -1, 1, "", methodObject), 
    new Among("юю", -1, 1, "", methodObject), 
    new Among("ая", -1, 1, "", methodObject), 
    new Among("яя", -1, 1, "", methodObject) };
  

  private static final Among[] a_2 = {
    new Among("ем", -1, 1, "", methodObject), 
    new Among("нн", -1, 1, "", methodObject), 
    new Among("вш", -1, 1, "", methodObject), 
    new Among("ивш", 2, 2, "", methodObject), 
    new Among("ывш", 2, 2, "", methodObject), 
    new Among("щ", -1, 1, "", methodObject), 
    new Among("ющ", 5, 1, "", methodObject), 
    new Among("ующ", 6, 2, "", methodObject) };
  

  private static final Among[] a_3 = {
    new Among("сь", -1, 1, "", methodObject), 
    new Among("ся", -1, 1, "", methodObject) };
  

  private static final Among[] a_4 = {
    new Among("ла", -1, 1, "", methodObject), 
    new Among("ила", 0, 2, "", methodObject), 
    new Among("ыла", 0, 2, "", methodObject), 
    new Among("на", -1, 1, "", methodObject), 
    new Among("ена", 3, 2, "", methodObject), 
    new Among("ете", -1, 1, "", methodObject), 
    new Among("ите", -1, 2, "", methodObject), 
    new Among("йте", -1, 1, "", methodObject), 
    new Among("ейте", 7, 2, "", methodObject), 
    new Among("уйте", 7, 2, "", methodObject), 
    new Among("ли", -1, 1, "", methodObject), 
    new Among("или", 10, 2, "", methodObject), 
    new Among("ыли", 10, 2, "", methodObject), 
    new Among("й", -1, 1, "", methodObject), 
    new Among("ей", 13, 2, "", methodObject), 
    new Among("уй", 13, 2, "", methodObject), 
    new Among("л", -1, 1, "", methodObject), 
    new Among("ил", 16, 2, "", methodObject), 
    new Among("ыл", 16, 2, "", methodObject), 
    new Among("ем", -1, 1, "", methodObject), 
    new Among("им", -1, 2, "", methodObject), 
    new Among("ым", -1, 2, "", methodObject), 
    new Among("н", -1, 1, "", methodObject), 
    new Among("ен", 22, 2, "", methodObject), 
    new Among("ло", -1, 1, "", methodObject), 
    new Among("ило", 24, 2, "", methodObject), 
    new Among("ыло", 24, 2, "", methodObject), 
    new Among("но", -1, 1, "", methodObject), 
    new Among("ено", 27, 2, "", methodObject), 
    new Among("нно", 27, 1, "", methodObject), 
    new Among("ет", -1, 1, "", methodObject), 
    new Among("ует", 30, 2, "", methodObject), 
    new Among("ит", -1, 2, "", methodObject), 
    new Among("ыт", -1, 2, "", methodObject), 
    new Among("ют", -1, 1, "", methodObject), 
    new Among("уют", 34, 2, "", methodObject), 
    new Among("ят", -1, 2, "", methodObject), 
    new Among("ны", -1, 1, "", methodObject), 
    new Among("ены", 37, 2, "", methodObject), 
    new Among("ть", -1, 1, "", methodObject), 
    new Among("ить", 39, 2, "", methodObject), 
    new Among("ыть", 39, 2, "", methodObject), 
    new Among("ешь", -1, 1, "", methodObject), 
    new Among("ишь", -1, 2, "", methodObject), 
    new Among("ю", -1, 2, "", methodObject), 
    new Among("ую", 44, 2, "", methodObject) };
  

  private static final Among[] a_5 = {
    new Among("а", -1, 1, "", methodObject), 
    new Among("ев", -1, 1, "", methodObject), 
    new Among("ов", -1, 1, "", methodObject), 
    new Among("е", -1, 1, "", methodObject), 
    new Among("ие", 3, 1, "", methodObject), 
    new Among("ье", 3, 1, "", methodObject), 
    new Among("и", -1, 1, "", methodObject), 
    new Among("еи", 6, 1, "", methodObject), 
    new Among("ии", 6, 1, "", methodObject), 
    new Among("ами", 6, 1, "", methodObject), 
    new Among("ями", 6, 1, "", methodObject), 
    new Among("иями", 10, 1, "", methodObject), 
    new Among("й", -1, 1, "", methodObject), 
    new Among("ей", 12, 1, "", methodObject), 
    new Among("ией", 13, 1, "", methodObject), 
    new Among("ий", 12, 1, "", methodObject), 
    new Among("ой", 12, 1, "", methodObject), 
    new Among("ам", -1, 1, "", methodObject), 
    new Among("ем", -1, 1, "", methodObject), 
    new Among("ием", 18, 1, "", methodObject), 
    new Among("ом", -1, 1, "", methodObject), 
    new Among("ям", -1, 1, "", methodObject), 
    new Among("иям", 21, 1, "", methodObject), 
    new Among("о", -1, 1, "", methodObject), 
    new Among("у", -1, 1, "", methodObject), 
    new Among("ах", -1, 1, "", methodObject), 
    new Among("ях", -1, 1, "", methodObject), 
    new Among("иях", 26, 1, "", methodObject), 
    new Among("ы", -1, 1, "", methodObject), 
    new Among("ь", -1, 1, "", methodObject), 
    new Among("ю", -1, 1, "", methodObject), 
    new Among("ию", 30, 1, "", methodObject), 
    new Among("ью", 30, 1, "", methodObject), 
    new Among("я", -1, 1, "", methodObject), 
    new Among("ия", 33, 1, "", methodObject), 
    new Among("ья", 33, 1, "", methodObject) };
  

  private static final Among[] a_6 = {
    new Among("ост", -1, 1, "", methodObject), 
    new Among("ость", -1, 1, "", methodObject) };
  

  private static final Among[] a_7 = {
    new Among("ейше", -1, 1, "", methodObject), 
    new Among("н", -1, 2, "", methodObject), 
    new Among("ейш", -1, 1, "", methodObject), 
    new Among("ь", -1, 3, "", methodObject) };
  

  private static final char[] g_v = { '!', 'A', '\b', 'è' };
  private int I_p2;
  private int I_pV;
  
  public russianStemmer() {}
  
  private void copy_from(russianStemmer other) { I_p2 = I_p2;
    I_pV = I_pV;
    super.copy_from(other);
  }
  

  private boolean r_mark_regions()
  {
    I_pV = limit;
    I_p2 = limit;
    
    int v_1 = cursor;
    





    while (!in_grouping(g_v, 1072, 1103))
    {




      if (cursor >= limit) {
        break label209;
      }
      
      cursor += 1;
    }
    
    I_pV = cursor;
    



    while (!out_grouping(g_v, 1072, 1103))
    {




      if (cursor >= limit) {
        break label209;
      }
      
      cursor += 1;
    }
    



    while (!in_grouping(g_v, 1072, 1103))
    {




      if (cursor >= limit) {
        break label209;
      }
      
      cursor += 1;
    }
    



    while (!out_grouping(g_v, 1072, 1103))
    {




      if (cursor >= limit) {
        break label209;
      }
      
      cursor += 1;
    }
    
    I_p2 = cursor;
    label209:
    cursor = v_1;
    return true;
  }
  
  private boolean r_R2() {
    if (I_p2 > cursor)
    {
      return false;
    }
    return true;
  }
  



  private boolean r_perfective_gerund()
  {
    ket = cursor;
    
    int among_var = find_among_b(a_0, 9);
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
      

      if (!eq_s_b(1, "а"))
      {




        cursor = (limit - v_1);
        
        if (!eq_s_b(1, "я"))
        {
          return false;
        }
      }
      
      slice_del();
      break;
    

    case 2: 
      slice_del();
    }
    
    return true;
  }
  


  private boolean r_adjective()
  {
    ket = cursor;
    
    int among_var = find_among_b(a_1, 26);
    if (among_var == 0)
    {
      return false;
    }
    
    bra = cursor;
    switch (among_var) {
    case 0: 
      return false;
    

    case 1: 
      slice_del();
    }
    
    return true;
  }
  




  private boolean r_adjectival()
  {
    if (!r_adjective())
    {
      return false;
    }
    
    int v_1 = limit - cursor;
    


    ket = cursor;
    
    int among_var = find_among_b(a_2, 8);
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
        int v_2 = limit - cursor;
        

        if (!eq_s_b(1, "а"))
        {




          cursor = (limit - v_2);
          
          if (!eq_s_b(1, "я"))
          {
            cursor = (limit - v_1);
            break;
          }
        }
        
        slice_del();
        break;
      

      case 2: 
        slice_del();
      }
      
    }
    return true;
  }
  


  private boolean r_reflexive()
  {
    ket = cursor;
    
    int among_var = find_among_b(a_3, 2);
    if (among_var == 0)
    {
      return false;
    }
    
    bra = cursor;
    switch (among_var) {
    case 0: 
      return false;
    

    case 1: 
      slice_del();
    }
    
    return true;
  }
  



  private boolean r_verb()
  {
    ket = cursor;
    
    int among_var = find_among_b(a_4, 46);
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
      

      if (!eq_s_b(1, "а"))
      {




        cursor = (limit - v_1);
        
        if (!eq_s_b(1, "я"))
        {
          return false;
        }
      }
      
      slice_del();
      break;
    

    case 2: 
      slice_del();
    }
    
    return true;
  }
  


  private boolean r_noun()
  {
    ket = cursor;
    
    int among_var = find_among_b(a_5, 36);
    if (among_var == 0)
    {
      return false;
    }
    
    bra = cursor;
    switch (among_var) {
    case 0: 
      return false;
    

    case 1: 
      slice_del();
    }
    
    return true;
  }
  


  private boolean r_derivational()
  {
    ket = cursor;
    
    int among_var = find_among_b(a_6, 2);
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
    }
    
    return true;
  }
  


  private boolean r_tidy_up()
  {
    ket = cursor;
    
    int among_var = find_among_b(a_7, 4);
    if (among_var == 0)
    {
      return false;
    }
    
    bra = cursor;
    switch (among_var) {
    case 0: 
      return false;
    

    case 1: 
      slice_del();
      
      ket = cursor;
      
      if (!eq_s_b(1, "н"))
      {
        return false;
      }
      
      bra = cursor;
      
      if (!eq_s_b(1, "н"))
      {
        return false;
      }
      
      slice_del();
      break;
    

    case 2: 
      if (!eq_s_b(1, "н"))
      {
        return false;
      }
      
      slice_del();
      break;
    

    case 3: 
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
    
    if (cursor < I_pV)
    {
      return false;
    }
    cursor = I_pV;
    int v_3 = limit_backward;
    limit_backward = cursor;
    cursor = (limit - v_2);
    

    int v_4 = limit - cursor;
    



    int v_5 = limit - cursor;
    

    if (!r_perfective_gerund())
    {




      cursor = (limit - v_5);
      

      int v_6 = limit - cursor;
      

      if (!r_reflexive())
      {
        cursor = (limit - v_6);
      }
      



      int v_7 = limit - cursor;
      

      if (!r_adjectival())
      {




        cursor = (limit - v_7);
        

        if (!r_verb())
        {




          cursor = (limit - v_7);
          
          if (r_noun()) {}
        }
      }
    }
    


    cursor = (limit - v_4);
    
    int v_8 = limit - cursor;
    


    ket = cursor;
    
    if (!eq_s_b(1, "и"))
    {
      cursor = (limit - v_8);
    }
    else
    {
      bra = cursor;
      
      slice_del();
    }
    
    int v_9 = limit - cursor;
    

    if (!r_derivational()) {}
    



    cursor = (limit - v_9);
    
    int v_10 = limit - cursor;
    

    if (!r_tidy_up()) {}
    



    cursor = (limit - v_10);
    limit_backward = v_3;
    cursor = limit_backward;return true;
  }
  
  public boolean equals(Object o) {
    return o instanceof russianStemmer;
  }
  
  public int hashCode() {
    return russianStemmer.class.getName().hashCode();
  }
}
