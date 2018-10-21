import java.util.LinkedList;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author norash1995
 */
public class Matrix {

    private double[][] matrix;
    private int rows;
    private int cols;

    public Matrix(int rows, int cols, double[] values){
        this.rows = rows;
        this.cols = cols;
        this.matrix = new double[rows][cols];
        for(int i = 0; i<rows; i++){
            for(int j = 0; j<cols; j++){
                matrix[i][j] = values[j+i*cols];
            }
        }
    }

    public Matrix(int rows, int cols, double[][] matrix){
        this.rows = rows;
        this.cols = cols;
        this.matrix = matrix;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i<rows; i++){
            for(int j = 0; j<cols; j++){
                sb.append(matrix[i][j]+" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    
    public Matrix getColNr(double k){
    	int mRows = this.rows;
    	int mCols = 1;
    	
    	double[][] matrix = new double [mRows][mCols];
    	for(int i =0; i<this.cols;i++){
    		if(i==k){
    			
    			for(int j = 0;j<mRows; j++){
    				matrix[j][0] = this.matrix[j][i]; 
    			}
    		}
    	}
    	return new Matrix(mRows, mCols, matrix);
    }
    

    public int getRow(){
    	return this.rows;
    }
    
    public int getCol(){
    	return this.cols;
    }
    
    /***********************************************************HMM1*******************************************************/
    public Matrix alphaZ(Matrix pi, Matrix b, Matrix em){
        //compute
        int n = pi.getRow();
        double a0[] = new double[n];
        double c0 = 0;
        for(int i = 0; i<n;i++){
            a0[i] = pi.get(i, 0)*b.getColNr(em.get(0, 0)).get(i, 0);
            c0 += a0[i];
        }
        //scale
        return new Matrix(n,1,a0);
    }


    
    public double alphaT(Matrix pi, Matrix b, Matrix em, Matrix a){
        int t = em.getCol();
        int n = pi.getRow();
        double ct = 0;
        Matrix a0 = pi.alphaZ(pi, b, em);
        
        double at[]= new double[n]; 
        double att[]= new double[n];
        for(int T =1; T< t; T++){
            ct = 0;
            for(int i = 0;i<n;i++){
                at[i]=0;
                for(int j = 0;j<n;j++){
                    at[i]= at[i]+ a0.get(j, 0)*a.get(j, i);
                    
                }

                at[i] = at[i]*b.getColNr(em.get(0, T)).get(i, 0);
                ct += at[i];
                att[i]= at[i];
            }
            a0 = new Matrix(n,1, at);
            
        }
        return ct;
    }
    
    //*******************************************************************************************************************************************************//
    //**************************************************************HMM2*************************************************************************************/
    
    
    public LinkedList<Integer> viterbi(Matrix pi, Matrix B, Matrix A, Matrix obs){
        //nr of observations
        int T = obs.getCol();
        //nr of rows in A, state graph of len N
        int N = B.getRow();
        //nr of rows in A column
        //int M = B.getCol();
        double tmp;
        int tMax =0;
        double vitMax = 0;
        double vit[][] = new double[T][N+2];
        int [][]backPointer = new int[T][N+2];
        for(int i = 0;i<N; i++){
            vit[0][i] = pi.get(0, i)*B.getColNr(obs.get(0, 0)).get(i, 0);
        }
        

        for(int t =1;t<T;t++){
            for(int i= 0;i<N; i++){
            	//System.out.println("[t: "+t+"] [i: "+i+"]");
                vit[t][i] = 0;
                for(int j =0; j<N; j++){
                    tmp = vit[t-1][j]*A.get(j, i);
                    if(tmp > vit[t][i]){
                        vit[t][i] = tmp;
                        backPointer[t][i] = j;
                    }
                }
                
                vit[t][i] *= B.getColNr(obs.get(0, t)).get(i, 0); 
            }
        }
        //System.out.println("vit[][]");
        for(int i=0; i<N;i++){
            //System.out.print("XXX "+vit[N-1][1]+"\n" );
        }
        for(int t =0;t<N;t++){
            if(vit[T-1][t] > vitMax){
                
                tMax = t;
                vitMax = vit[T-1][t];
                
            }
        }
        LinkedList<Integer> tags = new LinkedList<Integer>();
        tags.push(tMax);
        int i = T-1;
        //System.out.println("T: "+T);
        //int tags[] = new int[T];
        while(i>0){
            
            
            //tags[i] = tMax;
            //System.out.println("i: "+i+"  tags: "+tags[i]);
            tMax = backPointer[i][tMax];
            tags.push(tMax);
            i--;
        }
    	
    	/**for(int e=0;e<N;e++){
    		for(int j =0;j<N;j++){
    			System.out.print(" "+vit[j][e]+" ");
    		}
    		System.out.print("\n");
    	}
    	
    	/**System.out.println("Backpointer");
    	for(int e=0;e<N;e++){
    		for(int j =0;j<N;j++){
    			System.out.print(" "+backPointer[e][j]+" ");
    		}
    		System.out.print("\n");
    	}**/
    	
    	return tags;
    	
    	
    }
    

    public Matrix times(Matrix other){
        int resRows = this.rows;
        int resCols = other.cols;
        double[][] resMatrix = new double[resRows][resCols];
        for(int i = 0; i<other.cols; i++ ){
            for(int j = 0; j<this.rows; j++){
                double ji = 0;
                for(int k = 0; k<this.cols; k++){
                    ji+=this.matrix[j][k]*other.matrix[k][i];
                }
                resMatrix[j][i] = ji;
            }
        }
        return new Matrix(resRows,resCols,resMatrix);
    }

    public double get(int i, int j){
        return this.matrix[i][j];
    }
    



    public static void main(String[] args){
        //Matrix m = new Matrix(4,1,new double[]{0.9,0,0,0.1});
        //Matrix m1 = new Matrix(4,1,new double[]{1,0,0,0});
        
    }
}