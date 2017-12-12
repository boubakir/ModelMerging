package util;

import java.util.ArrayList;

public class Configuration {
	
	/**
	 * returns the threshold correspending to a giving role
	 * @param role
	 * @return
	 */
	


	
	public static int getNforNgram(){
		String path = "conf/ngram.txt"; 
		int res = 0;
		ArrayList<String> words = TextFile.getLines(path);
		String s = words.get(0);
		res = Integer.valueOf(s);
		return res;
	}
	
	public static float getWeight(String role){
		String path = "conf/criteria.txt"; 
		float res = 0;
		ArrayList<String> words = TextFile.getLines(path);
		
		int i = 0;
		while(i< words.size()){
			String word = words.get(i);
			String aRole = MyString.readUntil(word, ' ');
			String w = MyString.readFromToEnd(word, ' ');
			float weight = Float.valueOf(w);
			
			if(aRole.equals(role)){
				res = weight;
				i = words.size();
			}
			i++;
			
		}
		//ArrayList<String> words = MyString.
		
		return res;
		
	}

	
	public static float getThreshold(String role){
		String path = "conf/threshold.txt"; 
		float res = 0;
		ArrayList<String> words = TextFile.getLines(path);
		
		int i = 0;
		while(i< words.size()){
			String word = words.get(i);
			String aRole = MyString.readUntil(word, ' ');
			String t = MyString.readFromToEnd(word, ' ');
			float threshold = Float.valueOf(t);
			
			if(aRole.equals(role)){
				res = threshold;
				i = words.size();
			}
			i++;
			
		}
		//ArrayList<String> words = MyString.
		
		return res;
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println("Ngram");
		System.out.println(Configuration.getNforNgram());
		
		System.out.println("crterion weight");
		System.out.println(getWeight("ClassName"));
		System.out.println(getWeight("ClassAttribute"));
		System.out.println(getWeight("ClassOperation"));
		System.out.println(getWeight("AttributeName"));
		System.out.println(getWeight("AttributeType"));
		System.out.println(getWeight("OperationName"));
		System.out.println(getWeight("OperationType"));
		System.out.println(getWeight("Parameter"));
		System.out.println(getWeight("ParameterName"));
		System.out.println(getWeight("ParameterType"));
		
		System.out.println("thresholed");
		System.out.println(getThreshold("ClassName"));
		System.out.println(getThreshold("ClassAttribute"));
		System.out.println(getThreshold("ClassOperation"));
		System.out.println(getThreshold("AttributeName"));
		System.out.println(getThreshold("AttributeType"));
		System.out.println(getThreshold("OperationName"));
		System.out.println(getThreshold("OperationType"));
		System.out.println(getThreshold("Parameter"));
		System.out.println(getThreshold("ParameterName"));
		System.out.println(getThreshold("ParameterType"));
			
		
	}

}
