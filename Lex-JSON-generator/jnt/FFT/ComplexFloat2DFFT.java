package jnt.FFT;




public class ComplexFloat2DFFT
{
  int nrows;
  


  int ncols;
  


  ComplexFloatFFT rowFFT;
  

  ComplexFloatFFT colFFT;
  


  public ComplexFloat2DFFT(int nrows, int ncols)
  {
    this.nrows = nrows;
    this.ncols = ncols;
    rowFFT = new ComplexFloatFFT_Mixed(ncols);
    colFFT = (nrows == ncols ? rowFFT : new ComplexFloatFFT_Mixed(nrows));
  }
  
  protected void checkData(float[] data, int rowspan) {
    if (rowspan < 2 * ncols)
      throw new IllegalArgumentException("The row span " + rowspan + 
        "is shorter than the row length " + 2 * ncols);
    if (nrows * rowspan > data.length) {
      throw new IllegalArgumentException("The data array is too small for " + 
        nrows + "x" + rowspan + " data.length=" + data.length);
    }
  }
  
  public void transform(float[] data)
  {
    transform(data, 2 * ncols);
  }
  

  public void transform(float[] data, int rowspan)
  {
    checkData(data, rowspan);
    for (int i = 0; i < nrows; i++)
      rowFFT.transform(data, i * rowspan, 2);
    for (int j = 0; j < ncols; j++) {
      colFFT.transform(data, 2 * j, rowspan);
    }
  }
  
  public float[] toWraparoundOrder(float[] data) {
    return data;
  }
  


  public float[] toWraparoundOrder(float[] data, int rowspan)
  {
    if (rowspan == 2 * ncols) return data;
    float[] newdata = new float[2 * nrows * ncols];
    for (int i = 0; i < nrows; i++)
      for (int j = 0; j < ncols; j++) {
        newdata[(i * 2 * ncols + 2 * j)] = data[(i * rowspan + 2 * j)];
        newdata[(i * 2 * ncols + 2 * j + 1)] = data[(i * rowspan + 2 * j + 1)]; }
    return newdata;
  }
  
  public void backtransform(float[] data) {
    backtransform(data, 2 * ncols);
  }
  
  public void backtransform(float[] data, int rowspan) {
    checkData(data, rowspan);
    for (int i = 0; i < nrows; i++)
      rowFFT.backtransform(data, i * rowspan, 2);
    for (int j = 0; j < ncols; j++) {
      colFFT.backtransform(data, 2 * j, rowspan);
    }
  }
  
  public float normalization() {
    return 1.0F / (nrows * ncols);
  }
  
  public void inverse(float[] data) {
    inverse(data, 2 * ncols);
  }
  
  public void inverse(float[] data, int rowspan) {
    backtransform(data, rowspan);
    float norm = normalization();
    for (int i = 0; i < nrows; i++) {
      for (int j = 0; j < ncols; j++) {
        data[(i * rowspan + 2 * j)] *= norm;
        data[(i * rowspan + 2 * j + 1)] *= norm;
      }
    }
  }
}
