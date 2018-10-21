public class wrapper {

    private static Matrix alpha;
    private static double[] c;
    private static double[][] gamma;
    private static double[][][] gamma1;

    private static Matrix pi;
    private static Matrix a;
    private static Matrix b;

    public wrapper(Matrix transition, Matrix emission, Matrix initial){
        pi = initial;
        a = transition;
        b = emission;
    }

    public wrapper(Matrix alph, double[] cc){
        alpha = alph;
        c = cc;
    }

    public wrapper(double[][] g, double[][][] g1){
        gamma = g;
        gamma1 = g1;
    }

    public Matrix getAlpha(){
        return alpha;
    }

    public double[] getC(){
        return c;
    }

    public double[][] getGamma(){
        return gamma;
    }

    public double[][][] getGamma1(){
        return gamma1;
    }

    public Matrix getPi(){
        return pi;
    }

    public Matrix getA(){
        return a;
    }

    public Matrix getB(){
        return b;
    }
}
