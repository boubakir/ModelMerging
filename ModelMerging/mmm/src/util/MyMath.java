package util;

import java.util.ArrayList;
import java.util.Random;

public class MyMath {
	/**
	 * Convertir un nombre décimal en base 2, le résultat est une liste de 0 et 1
	 * @param x
	 * @return
	 */
	public static ArrayList<Integer> decToBin(int x){
		ArrayList<Integer> result = new ArrayList<Integer>();
			
			while(x>=2){
				Integer rest = new Integer(mod(x, 2));
				x = x/2;
				result.add(rest);
			}
			result.add(new Integer(x));
		return result;
	}
	
	
	
	public static ArrayList<Integer> decToBinLong(long x){
		ArrayList<Integer> result = new ArrayList<Integer>();
			
			while(x>=2){
				Integer rest = new Integer((int) modLong(x, 2));
				x = x/2;
				result.add(rest);
			}
			result.add(new Integer((int) x));
		return result;
	}
	
	
	/**
	 * Calcule la puissance d'un nombre
	 * @param x
	 * @param y
	 * @return
	 */
		
	public static int puiss(int x, int y){
		int res = 1;
		for(int i = 0; i < y; i++){
			res = res * x;
		}
		return res;
	}
	
	public static long puissLong(long x, long y){
		long res = 1;
		for(int i = 0; i < y; i++){
			res = res * x;
		}
		return res;
	}
	
	
	
	public static int mod(int x, int y){
		int z = x/y;
		return x-(z*y);
	}

	
	public static long modLong(long x, long y){
		long z = x/y;
		return x-(z*y);
	}
	
	public static void main(String[] args) {
		test4();
	}
	
	
	
	public static int getRandomNumber() {

			Random r = new Random();
			int x = 0 + r.nextInt(101 - 0);
			return x;
	}
	
	public static int getRandomNumber(int min, int max) {

		Random r = new Random();
		int x = min + r.nextInt(max+1 - min);
		return x;
}
	
	
	public static void test1() {
		ArrayList<Integer> liste = MyMath.decToBin(6);
		for(int i = 0; i < liste.size(); i++){
			int x = liste.get(i);
			System.out.print(x+"   ");
		}
		System.out.println("");
		System.out.println("2 puiss 0 = "+puiss(2, 0));
		System.out.println("2 puiss 1 = "+puiss(2, 1));
		System.out.println("2 puiss 3 = "+puiss(2, 3));
		System.out.println("3 puiss 1 = "+puiss(3, 1));
		System.out.println("3 puiss 2 = "+puiss(3, 2));
		System.out.println("3 puiss 3 = "+puiss(3, 3));
	}
	
	public static void test2(){
		for(int i = 0; i < 100; i++){
			System.out.println(getRandomNumber());
		}
	}
	
	public static void test3(){
		System.out.println("BEGIN...");
		for(int i = 0; i < 100000; i++){
			int x = getRandomNumber(10, 20);
			//if(x < 10 || x > 20){
				System.out.println(x);
			//}
			
		}
		System.out.println("END.");
	}
	
	
	
	
	public static void test4(){
		long x = MyMath.puissLong(2, 39);
		System.out.println(x);
		System.out.println("END.");
	}
	
	
	


}

