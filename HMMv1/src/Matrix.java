public class Matrix{
    private int row;
    private int col;
    private double[][] M;



    public Matrix(int r, int c){
        row= r;
        col = c;
        M = new double[row][col];
    }


    /** Constucts the matrix
     * arguments:
     * row, column, array of elements
     * **/
    public Matrix(int r, int c, double[] arr){
        row = r;
        col = c;
        M = new double[row][col];
        int count = 0;
        //double[][] m = new double[row][col];
        for(int i=0;i<r;i++){
            for(int j = 0;j<c;j++){
                this.M[i][j] = arr[count];
                count++;
            }
        }
    }

    /** method inserts value in given indices
     * **/
    public void putValue(int i, int j, double value){
        this.M[i][j] = value;
    }

    /**method prints out the matrix
     * **/
    public void printMatrix(){

        for(int i = 0;i<this.row;i++){
            for(int j=0;j<this.col;j++){
                System.out.print(this.M[i][j]+" ");
            }
            System.out.print("\n");
        }
    }

    /** method prints out as the required format of HMM0
     * **/
    public void printMatrixHMM0(){
        System.out.print(this.row+" "+this.col+" ");

        for(int i = 0;i<this.row;i++){
            for(int j=0;j<this.col;j++){
                System.out.print(this.M[i][j]+" ");
            }
            System.out.print("\n");
        }
    }

    public void printMatrixHMM3(){
        System.out.print(this.row+" "+this.col+" ");

        for(int i = 0;i<this.row;i++){
            for(int j=0;j<this.col;j++){
                System.out.print(this.M[i][j]+" ");
            }
            //System.out.print("\n");
        }
    }


    /** method return nr of rows of matrix
     * **/
    public int getRow(){
        return this.row;
    }

    /** method returns nr of columns of matrix
     * **/
    public int getCol(){
        return this.col;
    }


    /** method return element given indices
     * */
    public double get(int i, int j){
        return this.M[i][j];
    }

    /** Method multiplies matrix with vector
     * arguments:
     * vector, matrix
     * output:
     * matrix
     **/

    public Matrix mulMatrixVector(Matrix arr){//, double[][] matrix){
        int colM = this.col;
        int rowVec = arr.getRow();
        Matrix newM = null;//new Matrix(this.row, arr.getCol());//new double [rowM][colM];
        if(colM==rowVec){
            double value = 0;
            newM = new Matrix(this.row, arr.getCol());//new double [rowM][colM];
            for (int i = 0; i < arr.getCol(); i++) {
                for (int j = 0; j < rowVec; j++) {
                    //System.out.println(88);

                    value += this.M[0][j] * arr.get(j,i);
                }
//                System.out.println(value);
                newM.putValue(0,i,value);
                value=0;
            }
        }

        return newM;
    }


    /**method transposes vector
     * **/
    public Matrix transposeVect(){
        Matrix newV = new Matrix(this.col, this.row);
        for(int i=0;i<this.row;i++){
            for(int j =0;j<this.col;j++){
                newV.putValue(j,i,this.get(i,j));
            }
        }
        return newV;
    }

    public double sum(){
        double res = 0;
        for(int i =0;i<this.row; i++){
            for(int j=0;j<this.col;j++){
                res += this.M[i][j];
            }
        }
        return res;
    }

    public double[] gettingRow(int r){
        return this.M[r];
    }
}