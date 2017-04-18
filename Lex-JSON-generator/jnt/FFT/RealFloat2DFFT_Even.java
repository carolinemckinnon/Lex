package jnt.FFT;







public class RealFloat2DFFT_Even
{
  int nrows;
  




  int ncols;
  




  int rowspan;
  




  ComplexFloatFFT rowFFT;
  




  ComplexFloatFFT colFFT;
  





  public RealFloat2DFFT_Even(int nrows, int ncols)
  {
    this.nrows = nrows;
    this.ncols = ncols;
    rowspan = (ncols + 2);
    if (ncols % 2 != 0)
      throw new Error("The number of columns must be even!");
    rowFFT = new ComplexFloatFFT_Mixed(ncols / 2);
    colFFT = (nrows == ncols / 2 ? rowFFT : new ComplexFloatFFT_Mixed(nrows));
  }
  
  protected void checkData(float[] data, int rowspan) {
    if (rowspan < ncols + 2)
      throw new IllegalArgumentException("The row span " + rowspan + 
        "is not long enough for ncols=" + ncols);
    if (nrows * rowspan > data.length)
      throw new IllegalArgumentException("The data array is too small for " + 
        nrows + "x" + rowspan + " data.length=" + data.length);
  }
  
  public void transform(float[] data) {
    transform(data, ncols + 2);
  }
  
  public void transform(float[] data, int rowspan) {
    checkData(data, rowspan);
    for (int i = 0; i < nrows; i++)
    {
      rowFFT.transform(data, i * rowspan, 2);
      
      shuffle(data, i * rowspan, 1);
    }
    int nc = ncols / 2 + 1;
    for (int j = 0; j < nc; j++) {
      colFFT.transform(data, 2 * j, rowspan);
    }
  }
  
  public float[] toWraparoundOrder(float[] data, int rowspan) {
    float[] newdata = new float[2 * nrows * ncols];
    int nc = ncols / 2;
    for (int i = 0; i < nrows; i++) {
      int i0 = 2 * i * ncols;
      int k0 = i * rowspan;
      int r0 = i == 0 ? 0 : (nrows - i) * 2 * ncols;
      newdata[i0] = data[k0];
      newdata[(i0 + 1)] = data[(k0 + 1)];
      newdata[(i0 + ncols)] = data[(k0 + ncols)];
      newdata[(i0 + ncols + 1)] = data[(k0 + ncols + 1)];
      for (int j = 1; j < nc; j++) {
        newdata[(i0 + 2 * j)] = data[(k0 + 2 * j)];
        newdata[(i0 + 2 * j + 1)] = data[(k0 + 2 * j + 1)];
        newdata[(r0 + 2 * (ncols - j))] = data[(k0 + 2 * j)];
        newdata[(r0 + 2 * (ncols - j) + 1)] = (-data[(k0 + 2 * j + 1)]); } }
    return newdata;
  }
  
  public void backtransform(float[] data) {
    backtransform(data, ncols + 2);
  }
  
  public void backtransform(float[] data, int rowspan) {
    checkData(data, rowspan);
    
    int nc = ncols / 2 + 1;
    for (int j = 0; j < nc; j++)
      colFFT.backtransform(data, 2 * j, rowspan);
    for (int i = 0; i < nrows; i++)
    {
      shuffle(data, i * rowspan, -1);
      
      rowFFT.backtransform(data, i * rowspan, 2);
    } }
  
  private void shuffle(float[] data, int i0, int sign) { int nh = ncols / 2;
    int nq = ncols / 4;
    float c1 = 0.5F;float c2 = -0.5F * sign;
    double theta = sign * 3.141592653589793D / nh;
    float wtemp = (float)Math.sin(0.5D * theta);
    float wpr = -2.0F * wtemp * wtemp;
    float wpi = (float)-Math.sin(theta);
    float wr = 1.0F + wpr;
    float wi = wpi;
    for (int i = 1; i < nq; i++) {
      int i1 = i0 + 2 * i;
      int i3 = i0 + ncols - 2 * i;
      float h1r = c1 * (data[i1] + data[i3]);
      float h1i = c1 * (data[(i1 + 1)] - data[(i3 + 1)]);
      float h2r = -c2 * (data[(i1 + 1)] + data[(i3 + 1)]);
      float h2i = c2 * (data[i1] - data[i3]);
      data[i1] = (h1r + wr * h2r - wi * h2i);
      data[(i1 + 1)] = (h1i + wr * h2i + wi * h2r);
      data[i3] = (h1r - wr * h2r + wi * h2i);
      data[(i3 + 1)] = (-h1i + wr * h2i + wi * h2r);
      wtemp = wr;
      wr += wtemp * wpr - wi * wpi;
      wi += wtemp * wpi + wi * wpr; }
    float d0 = data[i0];
    if (sign == 1) {
      data[i0] = (d0 + data[(i0 + 1)]);
      data[(i0 + ncols)] = (d0 - data[(i0 + 1)]);
      data[(i0 + 1)] = 0.0F;
      data[(i0 + ncols + 1)] = 0.0F;
    } else {
      data[i0] = (c1 * (d0 + data[(i0 + ncols)]));
      data[(i0 + 1)] = (c1 * (d0 - data[(i0 + ncols)]));
      data[(i0 + ncols)] = 0.0F;
      data[(i0 + ncols + 1)] = 0.0F; }
    if (ncols % 4 == 0) {
      data[(i0 + nh + 1)] *= -1.0F;
    }
  }
  
  public float normalization()
  {
    return 2.0F / (nrows * ncols);
  }
  
  public void inverse(float[] data) {
    inverse(data, ncols + 2);
  }
  
  public void inverse(float[] data, int rowspan) {
    backtransform(data, rowspan);
    float norm = normalization();
    for (int i = 0; i < nrows; i++) {
      for (int j = 0; j < ncols; j++) {
        data[(i * rowspan + j)] *= norm;
      }
    }
  }
}
