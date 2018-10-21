/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.LinkedList;
import java.util.Scanner;

public class HMMtesting{
	
	public static void main (String []args){
		Kattio io = new Kattio(System.in, System.out);
		 
		
		
		int row = io.getInt();
		int col = io.getInt();
		
		
		double a[]= new double[col*row];
		for(int i =0; i<col*row; i++){
			a[i]= io.getDouble();
		}
		
		Matrix transitionM = new Matrix(row, col, a);
		
		
		int row1 = io.getInt();
		int col1 = io.getInt();
		
		
		
		double a1[]= new double[col1*row1];
		
		
		for(int i =0; i<col1*row1; i++){
			a1[i]= io.getDouble();
		}
		Matrix observationM = new Matrix(row1, col1, a1);

		
		//String piMatrix = sc.nextLine();
		
		//String[] piNrs = piMatrix.split("\\s+");
		
		
		int rowPi = io.getInt();
		int colPi = io.getInt();
				
		
		double pi[]= new double[colPi*rowPi];
		for(int i =0; i<colPi*rowPi; i++){
			pi[i]= io.getDouble();
		}
		Matrix Pi = new Matrix(rowPi, colPi, pi);		

        Matrix piCol = new Matrix(colPi, rowPi, pi);
                
	
		int m= io.getInt();
		
		
		double ems[]= new double[m];
		for(int i =0;i<m; i++){
			ems[i]= io.getDouble();
		}
		
		
		
		Matrix observations = new Matrix(1, m, ems);
		
		
		/**io.print("pi:  \n"+Pi);
        io.print("pi in column:  \n"+piCol);
		io.print("A:   \n"+transitionM);
		io.print("B:   \n"+observationM);
		io.print("Realizations:   \n"+observations);
		//System.out.println(Pi.alphaT(piCol, observationM, observations,transitionM));**/
		LinkedList<Integer> res= Pi.viterbi(Pi, observationM, transitionM, observations);
		//io.print(res.size()+"\n");
		for(int i =0; i<res.size();i++){
			
			io.print(res.get(i)+" ");	
		}
		//System.out.print("\n");
		//Pi.viterbi(Pi, observationM, transitionM, observations);
		//Pi.viterbi(Pi, observationM, transitionM, observations);
		io.close();
                
    }
}
