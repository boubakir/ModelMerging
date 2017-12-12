package util;

import java.util.ArrayList;


public class MyString {

	
	/**
	 * Vérifier si deux listes de string sont identiques
	 * @param list1
	 * @param list2
	 * @return
	 */
	public static boolean identicLists(ArrayList<String> list1, ArrayList<String> list2){
		boolean result = false;
		if(list1.size()==list2.size()){
			result = true;	int i = 0;
			while( i< list1.size()){
				if(!list2.contains(list1.get(i))){
					result = false;
				}
				i++;
			}
			
			
		}
		
		
		return result;
	}
	
	/**
	 * rendre la sous chaine qui commence du caractère qui vien après car juqu'au dernier
	 * @param source
	 * @param c
	 * @return
	 */
	public static String readFromToEnd(String source, char car){
		String result = "";
		int i = 0;	boolean read = false;
		while(i < source.length()){
			if(source.charAt(i)==car){
				read = true;
			}else{
				if(read) result = result + source.charAt(i); 
			}
			i++;
		}
		return result;
	}

	/**
	 * Permet de rendre une chaine qui commence du premier caractère jusqu'à le caractère donné en paramètre
	 * Ce dernier n'est pas pris 
	 * @param source
	 * @param car
	 * @return
	 */
	public static String readUntil(String source, char car){
		String result = "";
		int i = 0;	boolean read = true;
		while( (i < source.length()) && (read)){
			if(source.charAt(i)==car){
				read = false;
			}else{
				result = result + source.charAt(i);
				i++;
			}
		}
		return result;
	}
	
	/**
	 * Vérrifier si str1 contient str2
	 * Si la valeur de retour = -1, str1 ne contient pas str1
	 * * Si la valeur de retour = -2, str1 plusieurs ocurence de str1
	 * Si la valeur de retour >0 , indique l'index du premier caractère
	 * @param str1
	 * @param str2
	 * @return 
	 * @return
	 */
	
	public static int containts(String str1, String str2){
		if((str1.equals(""))||(str2.equals(""))) return -1;
		int first = -1;		boolean find = false;		
		int i = 0;	int j = 0;
		while(( i < str1.length())&&(!find)){
			
					
			
			if(first==-1){
				
				if(str2.length() > str1.length() - i) return -1;
				
				if(str1.charAt(i) == str2.charAt(0)){	
					first = i;	
					j=1;	
				}
				i++;
			}else{
				while((j < str2.length())&&(first!=-1)){
					if(str1.charAt(i) == str2.charAt(j)){
						j++;	i++;
					}else{
						i = first + 1;
						first = -1;
					}
				}//while j
				
				
				if(j==str2.length()) { find = true;  }
			}//else
			
			
		}//while i
		
		if(find)	return first;
		else return -1;
		
	}
	
	
	
	
	/**
	 * 
	 * @param source
	 * @param separator
	 * @return
	 */
	
	
	public static ArrayList<String> getWords(String source, char separator){
		ArrayList<String> res = new ArrayList<String>();
		String word = "";
		
		for(int i = 0; i < source.length(); i++){
			
			if(source.charAt(i)==separator){
				if(!word.equals(""))	res.add(word);
				word = "";
			}else{
				word = word + source.charAt(i); 
			}
		}//for
		
		if(!word.equals(""))	res.add(word);
		return res;
	}
	
	public static ArrayList<String> getWords(String source, char separator1, char separator2){
		ArrayList<String> res = new ArrayList<String>();
		String word = "";
		
		for(int i = 0; i < source.length(); i++){
			
			if( (source.charAt(i)==separator1) || (source.charAt(i)==separator2) ){
				if(!word.equals(""))	res.add(word);
				word = "";
			}else{
				word = word + source.charAt(i); 
			}
		}//for
		
		if(!word.equals(""))	res.add(word);
		return res;
	}
	
	public static ArrayList<String> intersection(ArrayList<String> liste1, ArrayList<String> liste2){
		ArrayList<String> res = new ArrayList<String>();
		for(int i = 0; i< liste1.size(); i++){
			for(int j = 0; j< liste2.size(); j++){
				if(liste1.get(i).equals(liste2.get(j))){
					res.add(liste1.get(i));
				}
			}
		}
		return   res;
	}

	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	
	/*String s1 = "	BonjourToutle     monde  fghjhg";
	String s2 = " m ll ll llqegqerg qergqerg qergqergqergqer";
	String s3 = ".Toutle.monde";
	
	String s4 = ".Toutele.monde";
	String s5 = "mondeddmonde";*/
	
	
	//System.out.println(readUntil(s1, ' '));
	//System.out.println(readUntil(s2, ' '));
	//System.out.println(readUntil(s3, ' '));
	
	//ArrayList<String> list  = getWords(s1, '	');
	
		ArrayList<String> list1  = new ArrayList<String>();
		ArrayList<String> list2  = new ArrayList<String>();
		list1.add("abc");	list1.add("abcd");	list1.add("abcde");
		list2.add("abcd");	list2.add("abc");	list2.add("abcde");	list2.add("abcde");
	
if(identicLists(list1, list2))
	System.out.println("1111111111111111111111111111111");
	else System.out.println("00000000000000000000000");


	
	
	}

}
