package edu.ucla.sspace.common;

import edu.ucla.sspace.util.ReflectionUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;






















































































public class ArgOptions
{
  private final List<String> positionalArgs;
  private final Map<Option, String> optionToValue;
  private final Map<String, Option> longNameToOption;
  private final Map<Character, Option> shortNameToOption;
  private final Map<String, Set<Option>> groupToOptions;
  
  public ArgOptions()
  {
    positionalArgs = new ArrayList();
    optionToValue = new HashMap();
    longNameToOption = new HashMap();
    shortNameToOption = new HashMap();
    
    groupToOptions = new LinkedHashMap();
  }
  














  public void addOption(char shortName, String longName, String description)
  {
    addOption(shortName, longName, description, false, null, null);
  }
  




















  public void addOption(char shortName, String longName, String description, boolean hasValue, String valueName)
  {
    addOption(shortName, longName, description, hasValue, valueName, null);
  }
  


























  public void addOption(char shortName, String longName, String description, boolean hasValue, String valueName, String optionGroupName)
  {
    if ((longName != null) && (longName.length() == 1)) {
      throw new IllegalArgumentException(
        "long name must be at least two characters");
    }
    
    if ((hasValue) && (valueName == null)) {
      throw new IllegalArgumentException(
        "value name must be supposed");
    }
    
    Option o = new Option(shortName, longName, description, 
      valueName, optionGroupName);
    
    if ((shortNameToOption.containsKey(Character.valueOf(shortName))) || (
      (longName != null) && (longNameToOption.containsKey(longName)))) {
      throw new IllegalArgumentException(
        "Already specified value with the name: " + longName);
    }
    
    shortNameToOption.put(Character.valueOf(shortName), o);
    if (longName != null) {
      longNameToOption.put(longName, o);
    }
    Set<Option> groupMembers = (Set)groupToOptions.get(optionGroupName);
    if (groupMembers == null) {
      groupMembers = new TreeSet();
      groupToOptions.put(optionGroupName, groupMembers);
    }
    groupMembers.add(o);
  }
  






  public void removeOption(char shortName)
  {
    Option option = (Option)shortNameToOption.remove(Character.valueOf(shortName));
    if (option == null) {
      return;
    }
    String longName = longName;
    if (longName != null) {
      longNameToOption.remove(longName);
    }
    Set<Option> groupOptions = (Set)groupToOptions.get(optionGroupName);
    groupOptions.remove(option);
  }
  





  public void removeOption(String longName)
  {
    Option option = (Option)longNameToOption.remove(longName);
    if (option == null) {
      return;
    }
    char shortName = shortName;
    shortNameToOption.remove(Character.valueOf(shortName));
    
    Set<Option> groupOptions = (Set)groupToOptions.get(optionGroupName);
    groupOptions.remove(option);
  }
  






  public void parseOptions(String[] commandLine)
  {
    for (int i = 0; i < commandLine.length; i++) {
      String s = commandLine[i];
      
      if (s.startsWith("--"))
      {
        int index = s.indexOf("=");
        if (index > 0) {
          String optionName = s.substring(2, index);
          String value = s.substring(index + 1);
          if (value.length() == 0) {
            throw new Error("no value specified for " + optionName);
          }
          Option o = (Option)longNameToOption.get(optionName);
          if (o == null) {
            throw new IllegalArgumentException(
              "unrecognizedOption: " + s);
          }
          if (!o.hasValue()) {
            throw new Error("Option " + optionName + " does not " + 
              "take a value");
          }
          optionToValue.put(o, value);

        }
        else
        {

          String optionName = s.substring(2);
          Option o = (Option)longNameToOption.get(optionName);
          if (o == null) {
            throw new IllegalArgumentException(
              "unrecognizedOption: " + s);
          }
          if (o.hasValue())
          {


            if (i + 1 >= commandLine.length) {
              throw new Error("no value specified for " + 
                optionName);
            }
            
            String value = commandLine[(++i)];
            optionToValue.put(o, value);

          }
          else
          {
            optionToValue.put(o, null);

          }
          

        }
        

      }
      else if (s.startsWith("-"))
      {
        for (int j = 1; j < s.length(); j++)
        {
          char optionName = s.charAt(j);
          Option o = (Option)shortNameToOption.get(Character.valueOf(optionName));
          if (o == null) {
            throw new IllegalArgumentException(
              "unrecognizedOption: " + s);
          }
          if (o.hasValue())
          {


            if (j + 1 < s.length()) {
              String value = s.substring(j + 1);
              optionToValue.put(o, value);
              break;
            }
            




            if (i + 1 == commandLine.length) {
              throw new Error("no value specified for " + o);
            }
            String value = commandLine[(++i)];
            optionToValue.put(o, value);
            

            break;
          }
          
          optionToValue.put(o, null);
        }
        

      }
      else
      {

        positionalArgs.add(s);
      }
    }
  }
  


  public int numPositionalArgs()
  {
    return positionalArgs.size();
  }
  


  public int numProvidedOptions()
  {
    return optionToValue.size();
  }
  



  public String getPositionalArg(int argNum)
  {
    return (String)positionalArgs.get(argNum);
  }
  


  public List<String> getPositionalArgs()
  {
    return positionalArgs;
  }
  
  private Option getOption(char shortName)
  {
    Option o = (Option)shortNameToOption.get(Character.valueOf(shortName));
    if (o == null) {
      throw new IllegalArgumentException(
        "no such option name: " + shortName);
    }
    return o;
  }
  
  private Option getOption(String longName) {
    Option o = (Option)longNameToOption.get(longName);
    if (o == null) {
      throw new IllegalArgumentException(
        "no such option name: " + longName);
    }
    return o;
  }
  





  public double getDoubleOption(char shortName)
  {
    Option o = getOption(shortName);
    if (optionToValue.containsKey(o)) {
      return Double.parseDouble((String)optionToValue.get(o));
    }
    
    throw new IllegalArgumentException(
      "Option -" + shortName + " does not have a value");
  }
  




  public double getDoubleOption(char shortName, double defaultValue)
  {
    Option o = getOption(shortName);
    return optionToValue.containsKey(o) ? 
      Double.parseDouble((String)optionToValue.get(o)) : 
      defaultValue;
  }
  





  public double getDoubleOption(String optionName)
  {
    Option o = getOption(optionName);
    if (optionToValue.containsKey(o)) {
      return Double.parseDouble((String)optionToValue.get(o));
    }
    
    throw new IllegalArgumentException(
      "Option " + optionName + " does not have a value");
  }
  




  public double getDoubleOption(String optionName, double defaultValue)
  {
    Option o = getOption(optionName);
    return optionToValue.containsKey(o) ? 
      Double.parseDouble((String)optionToValue.get(o)) : 
      defaultValue;
  }
  





  public long getLongOption(char shortName)
  {
    Option o = getOption(shortName);
    if (optionToValue.containsKey(o)) {
      return Long.parseLong((String)optionToValue.get(o));
    }
    
    throw new IllegalArgumentException(
      "Option -" + shortName + " does not have a value");
  }
  




  public long getLongOption(char shortName, long defaultValue)
  {
    Option o = getOption(shortName);
    return optionToValue.containsKey(o) ? 
      Long.parseLong((String)optionToValue.get(o)) : 
      defaultValue;
  }
  





  public long getLongOption(String optionName)
  {
    Option o = getOption(optionName);
    if (optionToValue.containsKey(o)) {
      return Long.parseLong((String)optionToValue.get(o));
    }
    
    throw new IllegalArgumentException(
      "Option " + optionName + " does not have a value");
  }
  




  public long getLongOption(String optionName, long defaultValue)
  {
    Option o = getOption(optionName);
    return optionToValue.containsKey(o) ? 
      Long.parseLong((String)optionToValue.get(o)) : 
      defaultValue;
  }
  





  public int getIntOption(char shortName)
  {
    Option o = getOption(shortName);
    if (optionToValue.containsKey(o)) {
      return Integer.parseInt((String)optionToValue.get(o));
    }
    
    throw new IllegalArgumentException(
      "Option -" + shortName + " does not have a value");
  }
  




  public int getIntOption(char shortName, int defaultValue)
  {
    Option o = getOption(shortName);
    return optionToValue.containsKey(o) ? 
      Integer.parseInt((String)optionToValue.get(o)) : 
      defaultValue;
  }
  





  public int getIntOption(String optionName)
  {
    Option o = getOption(optionName);
    if (optionToValue.containsKey(o)) {
      return Integer.parseInt((String)optionToValue.get(o));
    }
    
    throw new IllegalArgumentException(
      "Option " + optionName + " does not have a value");
  }
  




  public int getIntOption(String optionName, int defaultValue)
  {
    Option o = getOption(optionName);
    return optionToValue.containsKey(o) ? 
      Integer.parseInt((String)optionToValue.get(o)) : 
      defaultValue;
  }
  





  public boolean getBooleanOption(char shortName)
  {
    Option o = getOption(shortName);
    if (optionToValue.containsKey(o)) {
      return Boolean.parseBoolean((String)optionToValue.get(o));
    }
    
    throw new IllegalArgumentException(
      "Option -" + shortName + " does not have a value");
  }
  




  public boolean getBooleanOption(char shortName, boolean defaultValue)
  {
    Option o = getOption(shortName);
    return optionToValue.containsKey(o) ? 
      Boolean.parseBoolean((String)optionToValue.get(o)) : 
      defaultValue;
  }
  





  public boolean getBooleanOption(String optionName)
  {
    Option o = getOption(optionName);
    if (optionToValue.containsKey(o)) {
      return Boolean.parseBoolean((String)optionToValue.get(o));
    }
    
    throw new IllegalArgumentException(
      "Option " + optionName + " does not have a value");
  }
  




  public boolean getBooleanOption(String optionName, boolean defaultValue)
  {
    Option o = getOption(optionName);
    return optionToValue.containsKey(o) ? 
      Boolean.parseBoolean((String)optionToValue.get(o)) : 
      defaultValue;
  }
  





  public String getStringOption(char shortName)
  {
    Option o = getOption(shortName);
    if (optionToValue.containsKey(o)) {
      return (String)optionToValue.get(o);
    }
    
    throw new IllegalArgumentException(
      "Option -" + shortName + " does not have a value");
  }
  




  public String getStringOption(char shortName, String defaultValue)
  {
    Option o = getOption(shortName);
    return optionToValue.containsKey(o) ? 
      (String)optionToValue.get(o) : 
      defaultValue;
  }
  





  public String getStringOption(String optionName)
  {
    Option o = getOption(optionName);
    if (optionToValue.containsKey(o)) {
      return (String)optionToValue.get(o);
    }
    
    throw new IllegalArgumentException(
      "Option " + optionName + " does not have a value");
  }
  







  public <T> T getObjectOption(char optionName, T defaultValue)
  {
    Option o = getOption(optionName);
    if (optionToValue.containsKey(o))
      return ReflectionUtil.getObjectInstance((String)optionToValue.get(o));
    return defaultValue;
  }
  






  public <T> T getObjectOption(String optionName, T defaultValue)
  {
    Option o = getOption(optionName);
    if (optionToValue.containsKey(o))
      return ReflectionUtil.getObjectInstance((String)optionToValue.get(o));
    return defaultValue;
  }
  



  public String getStringOption(String optionName, String defaultValue)
  {
    Option o = getOption(optionName);
    return optionToValue.containsKey(o) ? 
      (String)optionToValue.get(o) : 
      defaultValue;
  }
  


  public boolean hasOption(String optionName)
  {
    Option o = (Option)longNameToOption.get(optionName);
    return o == null ? false : optionToValue.containsKey(o);
  }
  


  public boolean hasOption(char shortName)
  {
    Option o = (Option)shortNameToOption.get(Character.valueOf(shortName));
    return o == null ? false : optionToValue.containsKey(o);
  }
  




  public String prettyPrint()
  {
    int maxNameLength = -1;
    int maxDescLength = -1;
    int descriptionStart = -1;
    String valDesc; for (Option o : shortNameToOption.values()) {
      String longName = longName;
      valDesc = valueName;
      if (longName != null) {
        int length = valDesc == null ? 
          longName.length() : 
          longName.length() + valDesc.length();
        if (length > maxNameLength) {
          maxNameLength = length;
        }
      }
      String desc = description;
      if ((desc != null) && (maxDescLength < desc.length())) {
        maxDescLength = desc.length();
      }
    }
    
    descriptionStart = maxNameLength + 4 + 9;
    
    StringBuilder sb = new StringBuilder(100);
    

    Object regular = (Set)groupToOptions.get(null);
    

    if (regular != null) {
      printOption((Set)regular, "Options", 
        maxNameLength, descriptionStart, sb);
    }
    

    for (Map.Entry<String, Set<Option>> e : groupToOptions.entrySet()) {
      if (e.getKey() != null)
      {
        printOption((Set)e.getValue(), (String)e.getKey(), 
          maxNameLength, descriptionStart, sb);
      }
    }
    return sb.toString();
  }
  



  public String toString()
  {
    StringBuilder s = new StringBuilder(12 * optionToValue.size());
    s.append("Options[");
    for (Map.Entry<Option, String> e : optionToValue.entrySet()) {
      s.append(e.getKey()).append("=").append((String)e.getValue()).append(",");
    }
    s.setCharAt(s.length() - 1, ']');
    return s.toString();
  }
  


  private void printOption(Set<Option> options, String optionCategory, int maxNameLength, int descriptionStart, StringBuilder sb)
  {
    sb.append(optionCategory).append(":\n");
    for (Option o : options) {
      sb.append("  -").append(shortName);
      
      int spacesToAppend = -1;
      if (longName != null) {
        int length = longName.length();
        sb.append(", --").append(longName);
        if (valueName != null) {
          sb.append("=").append(valueName);
          length += valueName.length();
        }
        else
        {
          length--;
        }
        spacesToAppend = maxNameLength - length + 4;
      }
      else {
        spacesToAppend = maxNameLength + 4;
      }
      

      for (int i = 0; i < spacesToAppend; i++) {
        sb.append(" ");
      }
      
      if (description != null) {
        String[] descWords = description.split("\\s+");
        int spaceRemaining = 80 - descriptionStart;
        for (String word : descWords) {
          if (spaceRemaining < word.length()) {
            sb.append("\n");
            for (int i = 0; i < descriptionStart; i++)
              sb.append(" ");
            spaceRemaining = 80 - descriptionStart;
          }
          sb.append(word).append(" ");
          spaceRemaining -= word.length() + 1;
        }
      }
      sb.append("\n");
    }
  }
  


  private static class Option
    implements Comparable<Option>
  {
    final char shortName;
    
    final String longName;
    
    final String description;
    
    final String valueName;
    
    final String optionGroupName;
    

    public Option(char shortName, String longName, String description, String valueName, String optionGroupName)
    {
      this.shortName = shortName;
      this.longName = longName;
      this.description = description;
      this.valueName = valueName;
      this.optionGroupName = optionGroupName;
    }
    
    public int compareTo(Option o) {
      return shortName - shortName;
    }
    
    public boolean equals(Object o) {
      if ((o instanceof Option)) {
        Option p = (Option)o;
        return (shortName == shortName) || (
          (longName != longName) && (longName.equals(longName)));
      }
      return false;
    }
    
    public int hashCode() {
      return longName.hashCode();
    }
    
    public boolean hasValue() {
      return valueName != null;
    }
    
    public String toString() {
      return "-" + shortName + ", --" + longName;
    }
  }
}
