package edu.ucla.sspace.matrix;

import edu.ucla.sspace.vector.DenseVector;
import edu.ucla.sspace.vector.DoubleVector;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;












































class ListMatrix<T extends DoubleVector>
  implements Matrix, Serializable
{
  private static final long serialVersionUID = 1L;
  protected List<T> vectors;
  protected int columns;
  
  public ListMatrix(List<T> vectors)
  {
    if (vectors.size() == 0) {
      throw new IllegalArgumentException(
        "Must provide at least one vector");
    }
    this.vectors = new ArrayList(vectors.size());
    columns = ((DoubleVector)vectors.get(0)).length();
    for (T t : vectors) {
      if (t.length() != columns)
        throw new IllegalArgumentException(
          "Cannot create ragged matrix from list of vectors");
      this.vectors.add(t);
    }
  }
  
  public ListMatrix(List<T> vectors, int columns) {
    if (vectors.size() == 0) {
      throw new IllegalArgumentException(
        "Must provide at least one vector");
    }
    this.vectors = new ArrayList(vectors);
    this.columns = columns;
  }
  


  public double get(int row, int column)
  {
    return ((DoubleVector)vectors.get(row)).get(column);
  }
  


  public double[] getColumn(int column)
  {
    int i = 0;
    double[] columnValues = new double[vectors.size()];
    
    for (DoubleVector vector : vectors)
      columnValues[(i++)] = vector.get(column);
    return columnValues;
  }
  


  public DoubleVector getColumnVector(int column)
  {
    int i = 0;
    DoubleVector columnValues = new DenseVector(vectors.size());
    
    for (DoubleVector vector : vectors)
      columnValues.set(i++, vector.get(column));
    return columnValues;
  }
  


  public double[] getRow(int row)
  {
    return ((DoubleVector)vectors.get(row)).toArray();
  }
  


  public T getRowVector(int row)
  {
    return (DoubleVector)vectors.get(row);
  }
  


  public int columns()
  {
    return columns;
  }
  


  public int rows()
  {
    return vectors.size();
  }
  


  public double[][] toDenseArray()
  {
    double[][] result = new double[vectors.size()][columns];
    int row = 0;
    for (DoubleVector vector : vectors) {
      for (int i = 0; i < vector.length(); i++)
        result[row][i] = vector.get(i);
      row++;
    }
    return result;
  }
  


  public void set(int row, int column, double value)
  {
    T vector = (DoubleVector)vectors.get(row);
    vector.set(column, value);
  }
  


  public void setColumn(int column, double[] values)
  {
    int i = 0;
    for (DoubleVector vector : vectors) {
      vector.set(column, values[(i++)]);
    }
  }
  

  public void setColumn(int column, DoubleVector values)
  {
    int i = 0;
    for (DoubleVector vector : vectors) {
      vector.set(column, values.get(i++));
    }
  }
  

  public void setRow(int row, double[] values)
  {
    T v = (DoubleVector)vectors.get(row);
    for (int i = 0; i < values.length; i++) {
      v.set(i, values[i]);
    }
  }
  

  public void setRow(int row, DoubleVector values)
  {
    T v = (DoubleVector)vectors.get(row);
    for (int i = 0; i < values.length(); i++) {
      v.set(i, values.get(i));
    }
  }
}
