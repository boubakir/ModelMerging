package hungarian;

import java.util.ArrayList;
import model.Element;

public class Matrix {



public static void dispMatrix(float[][] matrix){
		
		System.out.print("      |   ");
		
	for(int i =0; i < matrix.length; i++){
		System.out.print("b"+(i+1)+"       ");
	}
	System.out.println();
	for(int i =0; i < matrix.length; i++){
		System.out.print("----------");
	}
	
	System.out.println();
		for(int i =0; i < matrix.length; i++){
			System.out.print("a"+(i+1)+"    |   ");
	
			for(int j =0; j < matrix.length; j++){
				
				
				java.text.DecimalFormat df = new java.text.DecimalFormat("0.##");
				float nombre = matrix[i][j];
				if(nombre == 0) System.out.print("0        ");  else System.out.print(df.format(nombre)+"     ");
				
				//System.out.print(matrix[i][j]+"          ");
				
			}
			System.out.println();
			
		}
		
		
		
	}
	


public static void dispMatrix(float[][] matrix, ArrayList<Element> E1, ArrayList<Element> E2){
		
	/*
	if((E1.size()==0) || E1.size()==0){
		return;
	}
	*/
	
		System.out.print("      |   ");
		
	for(int i =0; i < matrix.length; i++){
		
		
		if(i < E2.size()){
			System.out.print(E2.get(i).getId()+"       ");
		}else{
			System.out.print("#       ");
		}
	
	}
	System.out.println();
	for(int i =0; i < matrix.length; i++){
		System.out.print("----------");
	}
	
	System.out.println();
		for(int i =0; i < matrix.length; i++){
			
			
			
			if(i < E1.size()){
				System.out.print(E1.get(i).getId()+"    |   ");
			}else{
				System.out.print("#    |   ");
			}
	
			for(int j =0; j < matrix.length; j++){
				
				
				java.text.DecimalFormat df = new java.text.DecimalFormat("0.##");
				float nombre = matrix[i][j];
				if(nombre == 0) System.out.print("0        ");  else System.out.print(df.format(nombre)+"     ");
				
				//System.out.print(matrix[i][j]+"          ");
				
			}
			System.out.println();
			
		}
		
		
		
	}
	

	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
