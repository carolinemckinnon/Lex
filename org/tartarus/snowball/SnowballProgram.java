package org.tartarus.snowball;

import java.lang.reflect.InvocationTargetException;

public class SnowballProgram {
  protected StringBuffer current;
  
  protected SnowballProgram() { current = new StringBuffer();
    setCurrent("");
  }
  



  public void setCurrent(String value)
  {
    current.replace(0, current.length(), value);
    cursor = 0;
    limit = current.length();
    limit_backward = 0;
    bra = cursor;
    ket = limit;
  }
  



  public String getCurrent()
  {
    String result = current.toString();
    





    current = new StringBuffer();
    return result;
  }
  

  protected int cursor;
  
  protected int limit;
  
  protected int limit_backward;
  
  protected int bra;
  protected int ket;
  protected void copy_from(SnowballProgram other)
  {
    current = current;
    cursor = cursor;
    limit = limit;
    limit_backward = limit_backward;
    bra = bra;
    ket = ket;
  }
  
  protected boolean in_grouping(char[] s, int min, int max)
  {
    if (cursor >= limit) return false;
    char ch = current.charAt(cursor);
    if ((ch > max) || (ch < min)) return false;
    ch = (char)(ch - min);
    if ((s[(ch >> '\003')] & '\001' << (ch & 0x7)) == 0) return false;
    cursor += 1;
    return true;
  }
  
  protected boolean in_grouping_b(char[] s, int min, int max)
  {
    if (cursor <= limit_backward) return false;
    char ch = current.charAt(cursor - 1);
    if ((ch > max) || (ch < min)) return false;
    ch = (char)(ch - min);
    if ((s[(ch >> '\003')] & '\001' << (ch & 0x7)) == 0) return false;
    cursor -= 1;
    return true;
  }
  
  protected boolean out_grouping(char[] s, int min, int max)
  {
    if (cursor >= limit) return false;
    char ch = current.charAt(cursor);
    if ((ch > max) || (ch < min)) {
      cursor += 1;
      return true;
    }
    ch = (char)(ch - min);
    if ((s[(ch >> '\003')] & '\001' << (ch & 0x7)) == 0) {
      cursor += 1;
      return true;
    }
    return false;
  }
  
  protected boolean out_grouping_b(char[] s, int min, int max)
  {
    if (cursor <= limit_backward) return false;
    char ch = current.charAt(cursor - 1);
    if ((ch > max) || (ch < min)) {
      cursor -= 1;
      return true;
    }
    ch = (char)(ch - min);
    if ((s[(ch >> '\003')] & '\001' << (ch & 0x7)) == 0) {
      cursor -= 1;
      return true;
    }
    return false;
  }
  
  protected boolean in_range(int min, int max)
  {
    if (cursor >= limit) return false;
    char ch = current.charAt(cursor);
    if ((ch > max) || (ch < min)) return false;
    cursor += 1;
    return true;
  }
  
  protected boolean in_range_b(int min, int max)
  {
    if (cursor <= limit_backward) return false;
    char ch = current.charAt(cursor - 1);
    if ((ch > max) || (ch < min)) return false;
    cursor -= 1;
    return true;
  }
  
  protected boolean out_range(int min, int max)
  {
    if (cursor >= limit) return false;
    char ch = current.charAt(cursor);
    if ((ch <= max) && (ch >= min)) return false;
    cursor += 1;
    return true;
  }
  
  protected boolean out_range_b(int min, int max)
  {
    if (cursor <= limit_backward) return false;
    char ch = current.charAt(cursor - 1);
    if ((ch <= max) && (ch >= min)) return false;
    cursor -= 1;
    return true;
  }
  
  protected boolean eq_s(int s_size, String s)
  {
    if (limit - cursor < s_size) { return false;
    }
    for (int i = 0; i != s_size; i++) {
      if (current.charAt(cursor + i) != s.charAt(i)) return false;
    }
    cursor += s_size;
    return true;
  }
  
  protected boolean eq_s_b(int s_size, String s)
  {
    if (cursor - limit_backward < s_size) { return false;
    }
    for (int i = 0; i != s_size; i++) {
      if (current.charAt(cursor - s_size + i) != s.charAt(i)) return false;
    }
    cursor -= s_size;
    return true;
  }
  
  protected boolean eq_v(CharSequence s)
  {
    return eq_s(s.length(), s.toString());
  }
  
  protected boolean eq_v_b(CharSequence s) {
    return eq_s_b(s.length(), s.toString());
  }
  
  protected int find_among(Among[] v, int v_size)
  {
    int i = 0;
    int j = v_size;
    
    int c = cursor;
    int l = limit;
    
    int common_i = 0;
    int common_j = 0;
    
    boolean first_key_inspected = false;
    for (;;)
    {
      int k = i + (j - i >> 1);
      int diff = 0;
      int common = common_i < common_j ? common_i : common_j;
      Among w = v[k];
      
      for (int i2 = common; i2 < s_size; i2++) {
        if (c + common == l) {
          diff = -1;
          break;
        }
        diff = current.charAt(c + common) - s[i2];
        if (diff != 0) break;
        common++;
      }
      if (diff < 0) {
        j = k;
        common_j = common;
      } else {
        i = k;
        common_i = common;
      }
      if (j - i <= 1) {
        if ((i > 0) || 
          (j == i)) {
          break;
        }
        


        if (first_key_inspected) break;
        first_key_inspected = true;
      }
    }
    do {
      Among w = v[i];
      if (common_i >= s_size) {
        cursor = (c + s_size);
        if (method == null) return result;
        boolean res;
        try {
          Object resobj = method.invoke(methodobject, 
            new Object[0]);
          res = resobj.toString().equals("true");
        } catch (InvocationTargetException e) { boolean res;
          res = false;
        } catch (IllegalAccessException e) {
          boolean res;
          res = false;
        }
        
        cursor = (c + s_size);
        if (res) return result;
      }
      i = substring_i;
    } while (i >= 0); return 0;
  }
  


  protected int find_among_b(Among[] v, int v_size)
  {
    int i = 0;
    int j = v_size;
    
    int c = cursor;
    int lb = limit_backward;
    
    int common_i = 0;
    int common_j = 0;
    
    boolean first_key_inspected = false;
    for (;;)
    {
      int k = i + (j - i >> 1);
      int diff = 0;
      int common = common_i < common_j ? common_i : common_j;
      Among w = v[k];
      
      for (int i2 = s_size - 1 - common; i2 >= 0; i2--) {
        if (c - common == lb) {
          diff = -1;
          break;
        }
        diff = current.charAt(c - 1 - common) - s[i2];
        if (diff != 0) break;
        common++;
      }
      if (diff < 0) {
        j = k;
        common_j = common;
      } else {
        i = k;
        common_i = common;
      }
      if (j - i <= 1) {
        if ((i > 0) || 
          (j == i) || 
          (first_key_inspected)) break;
        first_key_inspected = true;
      }
    }
    do {
      Among w = v[i];
      if (common_i >= s_size) {
        cursor = (c - s_size);
        if (method == null) return result;
        boolean res;
        try
        {
          Object resobj = method.invoke(methodobject, 
            new Object[0]);
          res = resobj.toString().equals("true");
        } catch (InvocationTargetException e) { boolean res;
          res = false;
        } catch (IllegalAccessException e) {
          boolean res;
          res = false;
        }
        
        cursor = (c - s_size);
        if (res) return result;
      }
      i = substring_i;
    } while (i >= 0); return 0;
  }
  




  protected int replace_s(int c_bra, int c_ket, String s)
  {
    int adjustment = s.length() - (c_ket - c_bra);
    current.replace(c_bra, c_ket, s);
    limit += adjustment;
    if (cursor >= c_ket) { cursor += adjustment;
    } else if (cursor > c_bra) cursor = c_bra;
    return adjustment;
  }
  
  protected void slice_check()
  {
    if ((bra < 0) || 
      (bra > ket) || 
      (ket > limit) || 
      (limit > current.length()))
    {
      System.err.println("faulty slice operation");
    }
  }
  






  protected void slice_from(String s)
  {
    slice_check();
    replace_s(bra, ket, s);
  }
  
  protected void slice_from(CharSequence s)
  {
    slice_from(s.toString());
  }
  
  protected void slice_del()
  {
    slice_from("");
  }
  
  protected void insert(int c_bra, int c_ket, String s)
  {
    int adjustment = replace_s(c_bra, c_ket, s);
    if (c_bra <= bra) bra += adjustment;
    if (c_bra <= ket) ket += adjustment;
  }
  
  protected void insert(int c_bra, int c_ket, CharSequence s)
  {
    insert(c_bra, c_ket, s.toString());
  }
  

  protected StringBuffer slice_to(StringBuffer s)
  {
    slice_check();
    int len = ket - bra;
    s.replace(0, s.length(), current.substring(bra, ket));
    return s;
  }
  

  protected StringBuilder slice_to(StringBuilder s)
  {
    slice_check();
    int len = ket - bra;
    s.replace(0, s.length(), current.substring(bra, ket));
    return s;
  }
  
  protected StringBuffer assign_to(StringBuffer s)
  {
    s.replace(0, s.length(), current.substring(0, limit));
    return s;
  }
  
  protected StringBuilder assign_to(StringBuilder s)
  {
    s.replace(0, s.length(), current.substring(0, limit));
    return s;
  }
}
