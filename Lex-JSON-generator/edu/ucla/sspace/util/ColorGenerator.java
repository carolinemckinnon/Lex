package edu.ucla.sspace.util;

import java.awt.Color;
import java.util.Iterator;
import java.util.Random;





























public class ColorGenerator
  implements Iterator<Color>
{
  private final Random rand;
  private final Color seed;
  
  public ColorGenerator()
  {
    this(null);
  }
  
  public ColorGenerator(Color seed) {
    this.seed = seed;
    rand = new Random();
  }
  


  public boolean hasNext()
  {
    return true;
  }
  


  public Color next()
  {
    int r = rand.nextInt(256);
    int g = rand.nextInt(256);
    int b = rand.nextInt(256);
    return seed == null ? 
      new Color(r, g, b) : 
      new Color((r + seed.getRed()) / 2, 
      (g + seed.getGreen()) / 2, 
      (b + seed.getBlue()) / 2);
  }
  


  public void remove()
  {
    throw new UnsupportedOperationException("Cannot remove colors");
  }
}
