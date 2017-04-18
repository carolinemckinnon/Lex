package edu.ucla.sspace.util;

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.AbstractSet;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;























































public class TrieMap<V>
  extends AbstractMap<String, V>
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private final RootNode<V> rootNode;
  private int size = 0;
  



  public TrieMap()
  {
    rootNode = new RootNode();
    size = 0;
  }
  


  public TrieMap(Map<String, ? extends V> m)
  {
    this();
    if (m == null) {
      throw new NullPointerException("map cannot be null");
    }
    putAll(m);
  }
  




  private void checkKey(Object key)
  {
    if (key == null) {
      throw new NullPointerException("keys cannot be null");
    }
    if (!(key instanceof String)) {
      throw new ClassCastException("key not an instance of String");
    }
  }
  


  public void clear()
  {
    rootNode.clear();
    size = 0;
  }
  






  public boolean containsKey(Object key)
  {
    if (key == null) {
      throw new NullPointerException("key cannot be null");
    }
    if ((key instanceof String)) {
      Node<V> n = lookup((String)key);
      return (n != null) && (n.isTerminal());
    }
    
    throw new ClassCastException("The provided key does not implement String: " + 
      key);
  }
  



  public Set<Map.Entry<String, V>> entrySet()
  {
    return new EntryView(null);
  }
  



  public V get(Object key)
  {
    checkKey(key);
    
    String cs = (String)key;
    Node<V> n = lookup(cs);
    return n == null ? null : value;
  }
  


  public Set<String> keySet()
  {
    return new KeyView(null);
  }
  



  private Node<V> lookup(String key)
  {
    if (key == null) {
      throw new NullPointerException("key cannot be null");
    }
    
    int keyLength = key.length();
    

    Node<V> n = rootNode;
    


    for (int curKeyIndex = 0; curKeyIndex <= keyLength; curKeyIndex++)
    {
      CharSequence nodePrefix = n.getPrefix();
      





      if (nodePrefix.length() > 0) {
        int charOverlap = countOverlap(key, curKeyIndex, nodePrefix, 0);
        int prefixLength = nodePrefix.length();
        


        if (charOverlap < prefixLength) {
          return null;
        }
        



        curKeyIndex += prefixLength;
      }
      


      if (curKeyIndex == keyLength) {
        return n;
      }
      





      Node<V> child = n.getChild(key.charAt(curKeyIndex));
      


      if (child == null) {
        return null;
      }
      



      n = child;
    }
    




    if (!$assertionsDisabled) throw new AssertionError();
    return null;
  }
  









  public V put(String key, V value)
  {
    if ((key == null) || (value == null)) {
      throw new NullPointerException("keys and values cannot be null");
    }
    
    int keyLength = key.length();
    Node<V> n = rootNode;
    
    for (int curKeyIndex = 0; curKeyIndex <= keyLength; curKeyIndex++)
    {
      CharSequence nodePrefix = n.getPrefix();
      
      int nextCharIndex = curKeyIndex + 1;
      


      if (nodePrefix.length() > 0) {
        int charOverlap = countOverlap(key, curKeyIndex, nodePrefix, 0);
        int prefixLength = nodePrefix.length();
        


        if (charOverlap < prefixLength) {
          addIntermediateNode(n, 
            charOverlap, 
            key, 
            curKeyIndex, 
            value);
          size += 1;
          return null;
        }
        



        curKeyIndex += prefixLength;
        nextCharIndex = curKeyIndex + 1;
      }
      


      if (curKeyIndex == keyLength) {
        return replaceValue(n, value);
      }
      




      Node<V> child = n.getChild(key.charAt(curKeyIndex));
      




      if (child == null) {
        addChildNode(n, key, curKeyIndex, value);
        return null;
      }
      


      n = child;
    }
    




    return null;
  }
  















  private int countOverlap(CharSequence c1, int start1, CharSequence c2, int start2)
  {
    int maxOverlap = Math.min(c1.length() - start1, c2.length() - start2);
    for (int overlap = 0; 
        overlap < maxOverlap; overlap++) {
      if (c1.charAt(overlap + start1) != c2.charAt(overlap + start2)) {
        break;
      }
    }
    return overlap;
  }
  









  public V remove(Object key)
  {
    checkKey(key);
    
    String cs = (String)key;
    Node<V> n = lookup(cs);
    if ((n != null) && (n.isTerminal())) {
      V old = value;
      value = null;
      size -= 1;
      return old;
    }
    
    return n == null ? null : value;
  }
  










  private V replaceValue(Node<V> node, V newValue)
  {
    if (node.isTerminal()) {
      V old = value;
      value = newValue;
      return old;
    }
    


    value = newValue;
    size += 1;
    return null;
  }
  



  public int size()
  {
    return size;
  }
  













  private void addChildNode(Node<V> parent, String key, int transitionCharIndex, V value)
  {
    char transitionChar = key.charAt(transitionCharIndex);
    Node<V> child = new Node(key, transitionCharIndex + 1, value);
    parent.addChild(transitionChar, child);
    size += 1;
  }
  





















  private void addIntermediateNode(Node<V> original, int numOverlappingCharacters, String key, int indexOfStartOfOverlap, V value)
  {
    char[] originalPrefix = prefix;
    



    char distinguishing = originalPrefix[numOverlappingCharacters];
    char[] remainingPrefix = 
      Arrays.copyOfRange(originalPrefix, numOverlappingCharacters + 1, 
      originalPrefix.length);
    char[] overlappingPrefix = 
      Arrays.copyOfRange(originalPrefix, 0, numOverlappingCharacters);
    



    Node<V> child = new Node(remainingPrefix, value);
    
    children = children;
    

    prefix = overlappingPrefix;
    children = new CharMap();
    original.addChild(distinguishing, child);
    



    int remainingKeyChars = key.length() - indexOfStartOfOverlap;
    


    if (numOverlappingCharacters == remainingKeyChars)
    {
      value = value;

    }
    else
    {

      int prefixStart = indexOfStartOfOverlap + 
        numOverlappingCharacters + 1;
      char mappingKey = key.charAt(indexOfStartOfOverlap + 
        numOverlappingCharacters);
      char[] remainingKey = new char[key.length() - prefixStart];
      for (int i = 0; i < remainingKey.length; i++) {
        remainingKey[i] = key.charAt(prefixStart + i);
      }
      Node<V> newMapping = new Node(remainingKey, value);
      original.addChild(mappingKey, newMapping);
      
      value = null;
    }
  }
  


  public Collection<V> values()
  {
    return new ValueView(null);
  }
  







  private static class Node<V>
    implements Serializable
  {
    private static final long serialVersionUID = 1L;
    






    private char[] prefix;
    






    private V value;
    





    protected Map<Character, Node<V>> children;
    






    Node(String seq, int prefixStart, V value)
    {
      this(toArray(seq, prefixStart), value);
    }
    



    Node(char[] prefix, V value)
    {
      this.prefix = prefix;
      this.value = value;
      children = null;
    }
    
    public Node(String prefix, V value) {
      this(prefix, 0, value);
    }
    
    public void addChild(char c, Node<V> child) {
      if (children == null) {
        children = new CharMap();
      }
      children.put(Character.valueOf(c), child);
    }
    
    public Node<V> getChild(char c) {
      return children == null ? null : (Node)children.get(Character.valueOf(c));
    }
    
    public Map<Character, Node<V>> getChildren() {
      return children == null ? 
        new HashMap() : children;
    }
    
    public CharSequence getPrefix() {
      return new TrieMap.ArraySequence(prefix);
    }
    
    public boolean isTerminal() {
      return value != null;
    }
    
    void setTail(String seq) {
      prefix = toArray(seq);
    }
    
    public V setValue(V newValue) {
      if (newValue == null) {
        throw new NullPointerException("TrieMap values cannot be null");
      }
      V old = value;
      value = newValue;
      return old;
    }
    
    boolean prefixMatches(String seq) {
      if (seq.length() == prefix.length) {
        for (int i = 0; i < prefix.length; i++) {
          if (seq.charAt(i) != prefix[i]) {
            return false;
          }
        }
        return true;
      }
      return false;
    }
    
    private static char[] toArray(String seq) {
      return toArray(seq, 0);
    }
    
    private static char[] toArray(String seq, int start) {
      char[] arr = new char[seq.length() - start];
      for (int i = 0; i < arr.length; i++) {
        arr[i] = seq.charAt(i + start);
      }
      return arr;
    }
    
    public String toString() {
      return 
      
        "(" + (prefix.length == 0 ? "\"\"" : new String(prefix)) + ": " + value + ", children: " + children + ")";
    }
  }
  

  private static class RootNode<V>
    extends TrieMap.Node<V>
  {
    private static final long serialVersionUID = 1L;
    

    public RootNode()
    {
      super(null);
      children = new CharMap();
    }
    
    public void clear() {
      children.clear();
    }
    
    void setTail(String seq) {
      throw new IllegalStateException("cannot set tail on root node");
    }
    
    public V setValue(V newValue) {
      return super.setValue(newValue);
    }
    
    boolean tailMatches(String seq) {
      return seq.length() == 0;
    }
  }
  


  private static class ArraySequence
    implements CharSequence, Serializable
  {
    private static final long serialVersionUID = 1L;
    

    private final char[] sequence;
    

    public ArraySequence(char[] sequence)
    {
      this.sequence = sequence;
    }
    
    public char charAt(int i) {
      return sequence[i];
    }
    






    public boolean equals(Object o)
    {
      if ((o instanceof String)) {
        String cs = (String)o;
        if (cs.length() != sequence.length) {
          return false;
        }
        for (int i = 0; i < sequence.length; i++) {
          if (cs.charAt(i) != sequence[i]) {
            return false;
          }
        }
        return true;
      }
      return false;
    }
    
    public int hashCode() {
      return Arrays.hashCode(sequence);
    }
    
    public int length() {
      return sequence.length;
    }
    
    public CharSequence subSequence(int start, int end) {
      return new ArraySequence(Arrays.copyOfRange(sequence, start, end));
    }
    
    public String toString() {
      return new String(sequence);
    }
  }
  








  private static class AnnotatedNode<V>
  {
    private final String prefix;
    







    private final TrieMap.Node<V> node;
    







    public AnnotatedNode(TrieMap.Node<V> node, String prefix)
    {
      this.prefix = prefix;
      this.node = node;
    }
    
    public String toString() {
      return node.toString();
    }
  }
  





  private abstract class TrieIterator<E>
    implements Iterator<E>
  {
    private final Deque<TrieMap.AnnotatedNode<V>> dfsFrontier;
    




    private Map.Entry<String, V> next;
    



    private Map.Entry<String, V> prev;
    




    public TrieIterator()
    {
      dfsFrontier = new ArrayDeque();
      
      Iterator localIterator = rootNode.getChildren().entrySet().iterator();
      while (localIterator.hasNext()) {
        Map.Entry<Character, TrieMap.Node<V>> child = (Map.Entry)localIterator.next();
        dfsFrontier.offer(
          new TrieMap.AnnotatedNode((TrieMap.Node)child.getValue(), 
          ((Character)child.getKey()).toString())); }
      next = null;
      prev = null;
      

      advance();
    }
    





    private void advance()
    {
      TrieMap.AnnotatedNode<V> n = (TrieMap.AnnotatedNode)dfsFrontier.pollFirst();
      


      while ((n != null) && (!node.isTerminal()))
      {




        Map.Entry[] reversed = 
        
          new Map.Entry[node.getChildren().size()];
        int i = 1;
        
        Iterator localIterator1 = node.getChildren().entrySet().iterator();
        while (localIterator1.hasNext()) {
          Map.Entry<Character, TrieMap.Node<V>> child = (Map.Entry)localIterator1.next();
          reversed[(reversed.length - i)] = child;
          i++;
        }
        
        for (Map.Entry<Character, TrieMap.Node<V>> child : reversed) {
          dfsFrontier.push(new TrieMap.AnnotatedNode(
            (TrieMap.Node)child.getValue(), prefix + 
            node.getPrefix() + child.getKey()));
        }
        n = (TrieMap.AnnotatedNode)dfsFrontier.pollFirst();
      }
      
      if (n == null) {
        next = null;
      }
      else {
        next = createEntry(n);
        




        Map.Entry[] reversed = 
        
          new Map.Entry[node.getChildren().size()];
        int i = 1;
        
        Iterator localIterator2 = node.getChildren().entrySet().iterator();
        while (localIterator2.hasNext()) {
          Map.Entry<Character, TrieMap.Node<V>> child = (Map.Entry)localIterator2.next();
          reversed[(reversed.length - i)] = child;
          i++;
        }
        
        for (Map.Entry<Character, TrieMap.Node<V>> child : reversed) {
          dfsFrontier.push(new TrieMap.AnnotatedNode(
            (TrieMap.Node)child.getValue(), prefix + 
            node.getPrefix() + child.getKey()));
        }
      }
    }
    






    private Map.Entry<String, V> createEntry(TrieMap.AnnotatedNode<V> node)
    {
      String key = prefix + node.getPrefix();
      return new TrieMap.TrieEntry(key, node);
    }
    


    public boolean hasNext()
    {
      return next != null;
    }
    


    public Map.Entry<String, V> nextEntry()
    {
      if (next == null) {
        throw new NoSuchElementException("no further elements");
      }
      prev = next;
      advance();
      return prev;
    }
    







    public void remove()
    {
      if (prev == null) {
        throw new IllegalStateException();
      }
      remove(prev.getKey());
      prev = null;
    }
  }
  
  private class EntryIterator extends TrieMap<V>.TrieIterator<Map.Entry<String, V>> {
    private EntryIterator() { super(); }
    
    public Map.Entry<String, V> next()
    {
      return nextEntry();
    }
  }
  
  private class KeyIterator extends TrieMap<V>.TrieIterator<String> {
    private KeyIterator() { super(); }
    
    public String next() {
      return (String)nextEntry().getKey();
    }
  }
  
  private class ValueIterator extends TrieMap<V>.TrieIterator<V> {
    private ValueIterator() { super(); }
    
    public V next() {
      return nextEntry().getValue();
    }
  }
  


  private static class TrieEntry<V>
    extends AbstractMap.SimpleEntry<String, V>
  {
    private static final long serialVersionUID = 1L;
    

    private final TrieMap.Node<V> node;
    

    public TrieEntry(String key, TrieMap.Node<V> node)
    {
      super(value);
      this.node = node;
    }
    
    public V getValue() {
      return node.value;
    }
    
    public V setValue(V newValue) {
      return node.setValue(newValue);
    }
  }
  
  private class KeyView extends AbstractSet<String>
  {
    private KeyView() {}
    
    public void clear()
    {
      TrieMap.this.clear();
    }
    
    public boolean contains(Object o) {
      return containsKey(o);
    }
    
    public Iterator<String> iterator() {
      return new TrieMap.KeyIterator(TrieMap.this, null);
    }
    
    public boolean remove(Object o) {
      return remove(o) != null;
    }
    
    public int size() {
      return size;
    }
  }
  
  private class ValueView extends AbstractCollection<V>
  {
    private ValueView() {}
    
    public void clear()
    {
      TrieMap.this.clear();
    }
    
    public boolean contains(Object o) {
      return containsValue(o);
    }
    
    public Iterator<V> iterator() {
      return new TrieMap.ValueIterator(TrieMap.this, null);
    }
    
    public int size() {
      return size;
    }
  }
  
  private class EntryView extends AbstractSet<Map.Entry<String, V>>
  {
    private EntryView() {}
    
    public void clear()
    {
      TrieMap.this.clear();
    }
    
    public boolean contains(Object o) {
      if ((o instanceof Map.Entry)) {
        Map.Entry e = (Map.Entry)o;
        Object key = e.getKey();
        Object val = e.getValue();
        Object mapVal = get(key);
        return (mapVal == val) || ((val != null) && (val.equals(mapVal)));
      }
      return false;
    }
    
    public Iterator<Map.Entry<String, V>> iterator() {
      return new TrieMap.EntryIterator(TrieMap.this, null);
    }
    
    public int size() {
      return size;
    }
  }
}
