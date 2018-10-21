import java.util.Scanner;
import java.util.Arrays;

public class main {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        double[] transition = stringToDouble(sc.nextLine());
        double[] emission = stringToDouble(sc.nextLine());
        double[] initialState = stringToDouble(sc.nextLine());

        int transitionRow = (int) getRow(transition);
        int transitionCol = (int) getCol(transition);
        double[] transitionArr = getArr(transition);

        int emissionRow = (int) getRow(emission);
        int emissionCol = (int) getCol(emission);
        double[] emissionArr = getArr(emission);

        int initialRow = (int) getRow(initialState);
        int initialCol = (int) getCol(initialState);
        double[] initialArr = getArr(initialState);

        Matrix transitionM = new Matrix(transitionRow, transitionCol,transitionArr);
        Matrix emissionM = new Matrix(emissionRow, emissionCol, emissionArr);
        Matrix initialM = new Matrix(initialRow, initialCol, initialArr);

//        System.out.println("Transition:");
//        transitionM.printMatrix();
//        System.out.println("Emission:");
//        emissionM.printMatrix();
//        System.out.println("initial:");
//        initialM.printMatrix();

        Matrix res = initialM.mulMatrixVector(transitionM);
//        res.printMatrix();

        Matrix resF = res.mulMatrixVector(emissionM);
        resF.printMatrixHMM0();

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
            //System.out.print(value+" ");
            res[counter] = Double.parseDouble(value);
            counter++;
        }
        return res;

    }

    /** prints array
     * **/
    public static void printArr(double[] array){
        for(int i=0;i<array.length;i++){
            System.out.println(array[i]+ " ");
        }
    }


    /** return the row of array
     * **/
    public static double getRow(double []arr){
        return arr[0];
    }


    /** return the column of array
     * **/
    public static double getCol(double []arr){
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

}
