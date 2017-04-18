package jnt.FFT;
import java.io.PrintStream;

class Test { Test() {}
  public static void main(String[] args) { RealDoubleFFT_Radix2 ffter = new RealDoubleFFT_Radix2(32);
    double[] cat = { 0.1151D, -0.1175D, -0.0573D, -0.0733D, -0.0406D, -0.0332D, -0.3583D, 0.0166D, -0.1998D, -0.1076D, -0.0756D, -0.258D, 0.0614D, -0.22D, -0.0827D, 0.0026D, 0.085D, -0.4051D, -0.0536D, 0.0355D, -0.0947D, -0.0242D, 0.0421D, 0.1048D, -0.1097D, -0.0729D, 0.002D, -0.0699D, -0.1137D, 0.0702D, 0.1843D, -0.1336D };
    double[] are = { 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D };
    ffter.transform(cat, 0, 1);
    for (int i = 0; i < 32; i++) {
      System.out.println(cat[i]);
    }
  }
}
