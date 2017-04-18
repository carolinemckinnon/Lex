package edu.ucla.sspace.util;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;















































public class TimeSpan
{
  private static final Pattern TIME_SPAN_PATTERN = Pattern.compile("\\d+[a-zA-Z]");
  





  private final int years;
  





  private final int months;
  




  private final int weeks;
  




  private final int days;
  




  private final int hours;
  





  public TimeSpan(String timespan)
  {
    Matcher matcher = TIME_SPAN_PATTERN.matcher(timespan);
    



    int unitBitFlags = 0;
    


    int y = 0;
    int m = 0;
    int w = 0;
    int d = 0;
    int h = 0;
    
    int prevEnd = 0;
    while (matcher.find())
    {


      if (matcher.start() != prevEnd) {
        throw new IllegalArgumentException(
          "invalid time unit format: " + timespan);
      }
      prevEnd = matcher.end();
      String lengthStr = 
        timespan.substring(matcher.start(), matcher.end() - 1);
      int length = Integer.parseInt(lengthStr);
      checkDuration(length);
      char timeUnit = timespan.charAt(matcher.end() - 1);
      

      switch (timeUnit) {
      case 'y': 
        checkSetTwice(unitBitFlags, 0, "years");
        unitBitFlags |= 0x1;
        y = length;
        break;
      case 'm': 
        checkSetTwice(unitBitFlags, 1, "months");
        unitBitFlags |= 0x2;
        m = length;
        break;
      case 'w': 
        checkSetTwice(unitBitFlags, 2, "weeks");
        unitBitFlags |= 0x4;
        w = length;
        break;
      case 'd': 
        checkSetTwice(unitBitFlags, 3, "days");
        unitBitFlags |= 0x8;
        d = length;
        break;
      case 'h': 
        checkSetTwice(unitBitFlags, 4, "hours");
        unitBitFlags |= 0x10;
        h = length;
        break;
      default: 
        throw new IllegalArgumentException(
          "Unknown time unit: " + timeUnit);
      }
      
    }
    
    years = y;
    months = m;
    weeks = w;
    days = d;
    hours = h;
  }
  










  public TimeSpan(int years, int months, int weeks, int days, int hours)
  {
    checkDuration(years);
    checkDuration(months);
    checkDuration(weeks);
    checkDuration(days);
    checkDuration(hours);
    
    this.years = years;
    this.months = months;
    this.weeks = weeks;
    this.days = days;
    this.hours = hours;
  }
  

































  private static void checkSetTwice(int bitFlag, int index, String field)
  {
    if ((bitFlag & 1 << index) != 0) {
      throw new IllegalArgumentException(field + " is set twice");
    }
  }
  


  private static void checkDuration(int duration)
  {
    if (duration < 0) {
      throw new IllegalArgumentException(
        "Duration must be non-negative");
    }
  }
  






  public int getDays()
  {
    return days;
  }
  







  public int getHours()
  {
    return hours;
  }
  







  public int getMonths()
  {
    return months;
  }
  







  public int getWeeks()
  {
    return weeks;
  }
  







  public int getYears()
  {
    return years;
  }
  






  public boolean insideRange(Calendar startDate, Calendar endDate)
  {
    Calendar mutableStartDate = (Calendar)startDate.clone();
    return isInRange(mutableStartDate, endDate);
  }
  











  private boolean isInRange(Calendar mutableStartDate, Calendar endDate)
  {
    if (endDate.before(mutableStartDate)) {
      return false;
    }
    
    Calendar tsEnd = mutableStartDate;
    tsEnd.add(1, years);
    tsEnd.add(2, months);
    tsEnd.add(3, weeks);
    tsEnd.add(6, days);
    tsEnd.add(10, hours);
    
    return endDate.before(tsEnd);
  }
  



  public boolean insideRange(Date startDate, Date endDate)
  {
    Calendar c1 = Calendar.getInstance();
    Calendar c2 = Calendar.getInstance();
    c1.setTime(startDate);
    c2.setTime(endDate);
    return isInRange(c1, c2);
  }
  



  public boolean insideRange(long startDate, long endDate)
  {
    Calendar c1 = Calendar.getInstance();
    Calendar c2 = Calendar.getInstance();
    c1.setTimeInMillis(startDate);
    c2.setTimeInMillis(endDate);
    return isInRange(c1, c2);
  }
  
  public String toString() {
    return String.format("TimeSpan: %dy%dm%dw%dd%dh", new Object[] { Integer.valueOf(years), Integer.valueOf(months), 
      Integer.valueOf(weeks), Integer.valueOf(days), Integer.valueOf(hours) });
  }
}
