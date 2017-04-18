package edu.ucla.sspace.util.primitive;

import java.util.Collection;
import java.util.Random;
































public final class PrimitiveCollections
{
  private static final Random RANDOM = new Random();
  


  private static final IntSet EMPTY_INT_SET = new UnmodifiableIntSet(new TroveIntSet());
  
  public PrimitiveCollections() {}
  
  public static IntSet emptyIntSet()
  {
    return EMPTY_INT_SET;
  }
  


  public static void shuffle(int[] arr)
  {
    shuffle(arr, RANDOM);
  }
  


  public static void shuffle(int[] arr, Random rand)
  {
    int size = arr.length;
    for (int i = size; i > 1; i--) {
      int tmp = arr[(i - 1)];
      int r = rand.nextInt(i);
      arr[(i - 1)] = arr[r];
      arr[r] = tmp;
    }
  }
  


  public static void shuffle(double[] arr)
  {
    shuffle(arr, RANDOM);
  }
  


  public static void shuffle(double[] arr, Random rand)
  {
    int size = arr.length;
    for (int i = size; i > 1; i--) {
      double tmp = arr[(i - 1)];
      int r = rand.nextInt(i);
      arr[(i - 1)] = arr[r];
      arr[r] = tmp;
    }
  }
  


  public static void shuffle(long[] arr)
  {
    shuffle(arr, RANDOM);
  }
  


  public static void shuffle(long[] arr, Random rand)
  {
    int size = arr.length;
    for (int i = size; i > 1; i--) {
      long tmp = arr[(i - 1)];
      int r = rand.nextInt(i);
      arr[(i - 1)] = arr[r];
      arr[r] = tmp;
    }
  }
  


  public static IntSet unmodifiableSet(IntSet s)
  {
    return new UnmodifiableIntSet(s);
  }
  
  private static class UnmodifiableIntSet extends AbstractIntSet
  {
    private final IntSet set;
    
    public UnmodifiableIntSet(IntSet set)
    {
      this.set = set;
    }
    
    public boolean contains(Integer i) {
      return set.contains(i);
    }
    
    public boolean contains(int i) {
      return set.contains(i);
    }
    
    public boolean containsAll(IntSet c) {
      return set.containsAll(c);
    }
    
    public boolean containsAll(Collection<?> c) {
      return set.containsAll(c);
    }
    
    public boolean isEmpty() {
      return set.isEmpty();
    }
    
    public IntIterator iterator() {
      return new PrimitiveCollections.UnmodifiableIntIterator(set.iterator());
    }
    
    public int size() {
      return set.size();
    }
  }
  
  private static class UnmodifiableIntIterator implements IntIterator
  {
    private final IntIterator iter;
    
    public UnmodifiableIntIterator(IntIterator iter) {
      this.iter = iter;
    }
    
    public boolean hasNext() {
      return iter.hasNext();
    }
    
    public int nextInt() {
      return iter.nextInt();
    }
    
    public Integer next() {
      return (Integer)iter.next();
    }
    
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }
}
