package jnt.FFT;




public class ComplexDouble2DFFT
{
  int nrows;
  


  int ncols;
  


  ComplexDoubleFFT rowFFT;
  

  ComplexDoubleFFT colFFT;
  


  public ComplexDouble2DFFT(int nrows, int ncols)
  {
    if ((nrows <= 0) || (ncols <= 0))
      throw new IllegalArgumentException("The array dimensions >=0 : " + nrows + "," + ncols);
    this.nrows = nrows;
    this.ncols = ncols;
    rowFFT = new ComplexDoubleFFT_Mixed(ncols);
    colFFT = (nrows == ncols ? rowFFT : new ComplexDoubleFFT_Mixed(nrows));
  }
  
  protected void checkData(double[] data, int rowspan) {
    if (rowspan < 2 * ncols)
      throw new IllegalArgumentException("The row span " + rowspan + 
        "is shorter than the row length " + 2 * ncols);
    if (nrows * rowspan > data.length) {
      throw new IllegalArgumentException("The data array is too small for " + 
        nrows + "x" + rowspan + " data.length=" + data.length);
    }
  }
  
  public void transform(double[] data)
  {
    transform(data, 2 * ncols);
  }
  

  public void transform(double[] data, int rowspan)
  {
    checkData(data, rowspan);
    for (int i = 0; i < nrows; i++)
      rowFFT.transform(data, i * rowspan, 2);
    for (int j = 0; j < ncols; j++) {
      colFFT.transform(data, 2 * j, rowspan);
    }
  }
  
  public double[] toWraparoundOrder(double[] data) {
    return data;
  }
  


  public double[] toWraparoundOrder(double[] data, int rowspan)
  {
    if (rowspan == 2 * ncols) return data;
    double[] newdata = new double[2 * nrows * ncols];
    for (int i = 0; i < nrows; i++)
      for (int j = 0; j < ncols; j++) {
        newdata[(i * 2 * ncols + 2 * j)] = data[(i * rowspan + 2 * j)];
        newdata[(i * 2 * ncols + 2 * j + 1)] = data[(i * rowspan + 2 * j + 1)]; }
    return newdata;
  }
  
  public void backtransform(double[] data) {
    backtransform(data, 2 * ncols);
  }
  
  public void backtransform(double[] data, int rowspan) {
    checkData(data, rowspan);
    for (int j = 0; j < ncols; j++)
      colFFT.backtransform(data, 2 * j, rowspan);
    for (int i = 0; i < nrows; i++) {
      rowFFT.backtransform(data, i * rowspan, 2);
    }
  }
  
  public double normalization() {
    return 1.0D / (nrows * ncols);
  }
  
  public void inverse(double[] data) {
    inverse(data, 2 * ncols);
  }
  
  public void inverse(double[] data, int rowspan) {
    backtransform(data, rowspan);
    double norm = normalization();
    for (int i = 0; i < nrows; i++) {
      for (int j = 0; j < ncols; j++) {
        data[(i * rowspan + 2 * j)] *= norm;
        data[(i * rowspan + 2 * j + 1)] *= norm;
      }
    }
  }
}
