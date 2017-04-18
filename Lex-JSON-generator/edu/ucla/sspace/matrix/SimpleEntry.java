package edu.ucla.sspace.matrix;

import java.io.Serializable;

























class SimpleEntry
  implements MatrixEntry, Serializable
{
  private static final long serialVersionUID = 1L;
  private final int row;
  private final int column;
  private final double value;
  
  public SimpleEntry(int row, int column, double value)
  {
    this.row = row;
    this.column = column;
    this.value = value;
  }
  


  public int column()
  {
    return column;
  }
  


  public int row()
  {
    return row;
  }
  


  public double value()
  {
    return value;
  }
  
  public String toString() {
    return "(" + row + "," + column + ":" + value + ")";
  }
}
