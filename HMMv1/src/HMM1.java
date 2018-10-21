import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.lang.Math;




public class HMM1 {

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        double[] transition = stringToDouble(sc.nextLine().trim());
        double[] emission = stringToDouble(sc.nextLine());
        double[] initialState = stringToDouble(sc.nextLine());
        int[] seqEmissions = stringToInt(sc.nextLine());

        int transitionRow = (int) getRow(transition);
        int transitionCol = (int) getCol(transition);
        double[] transitionArr = getArr(transition);

        int emissionRow = (int) getRow(emission);
        int emissionCol = (int) getCol(emission);
        double[] emissionArr = getArr(emission);

        int initialRow = (int) getRow(initialState);
        int initialCol = (int) getCol(initialState);
        double[] initialArr = getArr(initialState);

        int seqEmissionsLen = (int) getRow(seqEmissions);
        int[] seqEmissionsArr = getArrInt(seqEmissions);

        Matrix transitionM = new Matrix(transitionRow, transitionCol,transitionArr);
        Matrix emissionM = new Matrix(emissionRow, emissionCol, emissionArr);
        Matrix initialM = new Matrix(initialRow, initialCol, initialArr);



        //HMM3

        //Step 1
        int maxIters = 500;
        int iters = 0;
        double oldLogProb = Double.NEGATIVE_INFINITY;


//        System.out.println("Transition:");
//        transitionM.printMatrix();
//        System.out.println("Emission:");
//        emissionM.printMatrix();
//        System.out.println("initial:");
//        initialM.printMatrix();




        //Step 2
        wrapper wrap = initAlpha(initialM, transitionM, emissionM, seqEmissionsArr);
        Matrix alpha = wrap.getAlpha();
        double []c = wrap.getC();
        wrapper boxes = alphas(initialM , emissionM, transitionM, seqEmissionsArr,  alpha, c);
        Matrix alphaN = boxes.getAlpha();
//        System.out.println("alpha:");
//        alphaN.printMatrix();
        double[] cN = boxes.getC();


        //step 3
        Matrix beta = initBeta(initialM, transitionM, emissionM, seqEmissionsArr, cN);
//        System.out.println("Beta:");
//        beta.printMatrix();

        //step 4
        wrapper gammas = initGamma(transitionM, emissionM, beta, alphaN, seqEmissionsArr, initialM, cN);
        double[][] gamma = gammas.getGamma();
        double[][][] gamma1 = gammas.getGamma1();
//        System.out.println("gamma: ");
//        printArr2(gamma);



        //step 5
        wrapper newV = restimate(initialM, transitionM, emissionM, gamma, gamma1, seqEmissionsArr);
        transitionM = newV.getA();
        emissionM = newV.getB();
        initialM = newV.getPi();

        //step 6
        double logProb = computeLog(c, seqEmissionsArr);


        //step 7
        for(int iter =0; iter<maxIters;iter++){
            if(logProb>oldLogProb){
//                System.out.println("log: "+logProb);
//                System.out.println("oldLog: "+oldLogProb);
                iters +=1;
                oldLogProb = logProb;
                //Step 2
                wrap = initAlpha(initialM, transitionM, emissionM, seqEmissionsArr);
                alpha = wrap.getAlpha();
                c = wrap.getC();
                boxes = alphas(initialM , emissionM, transitionM, seqEmissionsArr,  alpha, c);
                alphaN = boxes.getAlpha();
                cN = boxes.getC();

                //step 3
                beta = initBeta(initialM, transitionM, emissionM, seqEmissionsArr, cN);

                //step 4
                gammas = initGamma(transitionM, emissionM, beta, alphaN, seqEmissionsArr, initialM, cN);
                gamma = gammas.getGamma();
                gamma1 = gammas.getGamma1();

                //step 5
                newV = restimate(initialM, transitionM, emissionM, gamma, gamma1, seqEmissionsArr);
                transitionM = newV.getA();
                emissionM = newV.getB();
                initialM = newV.getPi();

                //step 6
                logProb = computeLog(c, seqEmissionsArr);
            }
            else{
//                System.out.print("Breaking");
                break;
            }
        }
        transitionM.printMatrixHMM3();
        System.out.print("\n");
        emissionM.printMatrixHMM3();
//        System.out.println("new:");
//        initialM.printMatrix();
//        System.out.println("transition:");
//        transitionM.printMatrix();
//        System.out.println("emission:");
//        emissionM.printMatrix();



//        if(iters<maxIters && logProb>oldLogProb){
//
//        }
//        else{
//            initialM.printMatrix();
//            transitionM.printMatrix();
//            emissionM.printMatrix();
//        }



    }




    public static wrapper initAlpha(Matrix initS, Matrix transition, Matrix em, int[] seqEmissionsArr){
        double[] c = new double[seqEmissionsArr.length];
        c[0] = 0;
        //double[] alpha = new double[initS.getCol()];
        Matrix alpha = new Matrix(seqEmissionsArr.length, initS.getCol());
        for(int i=0;i<initS.getCol(); i++){
            double value = initS.get(0,i)*em.get(i, seqEmissionsArr[0]);
            //alpha[i]= value;
            alpha.putValue(0,i,value);
            c[0] += alpha.get(0,i);
        }

        c[0] = 1/c[0];
        for(int i=0; i<initS.getCol(); i++){
            //double value =c[0]*alpha[i];
            double value =c[0]*alpha.get(0,i);
            alpha.putValue(0 , i, value);
        }

        return new wrapper(alpha,c);

    }



    public static wrapper alphas(Matrix initialState ,Matrix emission, Matrix transition, int[] seqEm, Matrix alpha, double[] c){
        for(int t =1;t< seqEm.length; t++){
            for(int i = 0;i< initialState.getCol(); i++){
                alpha.putValue(t,i, 0);
                for(int j=0 ; j< initialState.getCol();j++){
                    double value = alpha.get(t,i)+ alpha.get(t-1, j)*transition.get(j,i);
                    alpha.putValue(t,i,value);
                }

                double value1 = alpha.get(t,i)*emission.get(i,seqEm[t]);
                alpha.putValue(t,i,value1);
                c[t] += alpha.get(t,i);
            }

            c[t] = 1/c[t];
            for(int i =0;i< initialState.getCol();i++){
                double value2 = c[t]*alpha.get(t,i);
                alpha.putValue(t,i,value2);
            }

        }
        return new wrapper(alpha,c);//arrSum(alpha.gettingRow(alpha.getRow()-1));
    }


    public static Matrix initBeta(Matrix pi, Matrix a, Matrix b, int[] seqEm, double[] c){
        Matrix beta  = new Matrix(seqEm.length, pi.getCol());
        int T = seqEm.length;
        for(int i=0;i<pi.getCol();i++){
            beta.putValue(T-1,i, c[T-1]);
        }

        for(int t = T-2; t>=0;t--){
            for(int i=0; i<pi.getCol();i++){
                beta.putValue(t,i,0);
                for(int j=0; j<pi.getCol();j++){
                    double v1 = beta.get(t,i)+ a.get(i,j)*b.get(j, seqEm[t+1])*beta.get(t+1, j);
                    beta.putValue(t,i,v1);
                }
                beta.putValue(t,i,c[t]*beta.get(t,i));
            }
        }
        return beta;

    }

    public static wrapper initGamma(Matrix a, Matrix b, Matrix beta, Matrix alpha, int[] seqEm, Matrix pi, double[] c) {
        int T = seqEm.length;
        int N = pi.getCol();

        double[][] gamma = new double[T][N];
        double[][][] gamma1 = new double[T][N][N];
        for (int t = 0; t<=T-2; t++) {
            double denom = 0;
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    denom += alpha.get(t, i) * a.get(i, j) * b.get(j, seqEm[t + 1]) * beta.get(t + 1, j);
                }
            }

            for (int i = 0; i < N; i++) {
                gamma[t][i] = 0;
                for (int j = 0; j < N; j++) {
                    gamma1[t][i][j] = (alpha.get(t, i) * a.get(i, j) * b.get(j, seqEm[t + 1])*beta.get(t+1,j)) / denom;
                    gamma[t][i] = gamma[t][i] + gamma1[t][i][j];
                }
            }
        }


        double denom = 0;
        for (int i = 0; i < N; i++) {
            denom = denom + alpha.get(T - 1, i);
        }

        for (int i = 0; i < N; i++) {
            gamma[T - 1][i] = alpha.get(T - 1, i) / denom;
        }

        return new wrapper(gamma, gamma1);

    }

    public static wrapper restimate(Matrix pi, Matrix a, Matrix b, double[][] gamma, double[][][] gamma1, int[] seqEm) {

        int T = seqEm.length;
        int N = pi.getCol();
        //double[] piNew = new double[N];
        for (int i = 0; i < N; i++) {
            pi.putValue(0, i, gamma[0][i]);
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                double numer = 0;
                double denom = 0;

                for (int t = 0; t <= T - 2; t++) {
                    numer = numer + gamma1[t][i][j];
                    denom = denom + gamma[t][i];
                }
                a.putValue(i, j, numer / denom);
            }
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < b.getCol(); j++) {
                double numer = 0;
                double denom = 0;
                for (int t = 0; t <= T-1; t++) {
                    if (seqEm[t] == j) {
                        numer = numer + gamma[t][i];
                    }
                    denom = denom + gamma[t][i];

                }
                b.putValue(i, j, numer / denom);

            }
        }
        return new wrapper(a, b, pi);

    }

    public static double computeLog(double[] c, int[] seqEm){
        int T = seqEm.length;
        double logProb = 0;
        for(int i=0; i<=T-1; i++){
            logProb = logProb + Math.log(c[i]);
        }
        logProb = -logProb;

        return logProb;

    }


    public static double arrSum(double []arr){
        double sum = 0;
        for(int i =0;i<arr.length;i++){
            sum+=arr[i];
        }
        return sum;
    }

    /** method converts string elements to double
     * input: string array
     * output: double array
     * **/
    public static double[] stringToDouble(String arr){
        String[] values = arr.split("\\s");
        double[] res = new double[values.length];
        int counter=0;
        for(String value:values){
            res[counter] = Double.parseDouble(value);
            counter++;
        }
        return res;

    }

    public static int[] stringToInt(String arr){
        String[] values = arr.split("\\s");
        int[] res = new int[values.length];
        int counter=0;
        for(String value:values){
            //System.out.print(value+" ");
            res[counter] = Integer.parseInt(value);
            counter++;
        }
        return res;

    }

    /** prints array
     * **/
    public static void printArrInt(int[] array){
        for(int i=0;i<array.length;i++){
            System.out.println(array[i]+ " ");
        }
    }

    /** prints array
     * **/
    public static void printArr(double[] array){
        for(int i=0;i<array.length;i++){
            System.out.println(array[i]+ " ");
        }
    }

    public static void printArr2(double[][] array){
        for(int i=0;i<array.length;i++){
            for(int j =0; j<array[0].length;j++){
                System.out.print(array[i][j]+ " ");
            }
            System.out.print("\n");

        }
    }






    /** return the row of array
     * **/
    public static double getRow(double []arr){
        return arr[0];
    }

    public static double getRow(int []arr){
        return arr[0];
    }


    /** return the column of array
     * **/
    public static double getCol(double []arr){
        return arr[1];
    }

    public static double getCol(int []arr){
        return arr[1];
    }


    /** method return only the array from the input
     * input: raw array
     * output: array
     * **/
    public static double[] getArr(double []arr){
        double[] res = Arrays.copyOfRange(arr, 2, arr.length);
        return res;
    }


    public static int[] getArrInt(int []arr){
        int[] res = Arrays.copyOfRange(arr, 1, arr.length);
        return res;
    }


}
