package Jama.util;

public class Maths
{
  public Maths() {}
  
  public static double hypot(double a, double b) {
    double r;
    if (Math.abs(a) > Math.abs(b)) {
      double r = b / a;
      r = Math.abs(a) * Math.sqrt(1.0D + r * r);
    } else if (b != 0.0D) {
      double r = a / b;
      r = Math.abs(b) * Math.sqrt(1.0D + r * r);
    } else {
      r = 0.0D;
    }
    return r;
  }
}
