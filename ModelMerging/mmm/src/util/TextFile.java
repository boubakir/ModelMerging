package util;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
/**
 *  Cette classe contient un ensemble de méthodes permettant la manipulations des fichiers text 
 * @author Boubakir Mohamed
 *
 *
 */
public class TextFile {

/**
 * La fonction write permet d'écrire un texte dans un fichier
 * @param text : le texte è écrire 
 * @param filePath : le chemin du fichier 
 * @param format : le codage du fichier 
 * @param overwrite : écraser ou non le fichier existant
 */

public static void write(String text, String filePath, String format, boolean overwrite){
	
	//String source = new String(text);

	if(!format.equals("")){ 
	    try {
	    	byte[] b = text.getBytes(format);
	        text = new String (b);

        } catch (UnsupportedEncodingException e1) {
		  // TODO Auto-generated catch block
	      //e1.printStackTrace();
	    }
	}
	
	try{
      File outputFile = new File(filePath);
      BufferedWriter out; 
      
      out = new BufferedWriter(new OutputStreamWriter
              (new FileOutputStream(outputFile ,!overwrite)));
      
      out.write(text);
      
      out.close();
      
    } catch (FileNotFoundException e) {
        System.err.println("FileStreamsTest: " + e);
    } catch (IOException e) {
        System.err.println("FileStreamsTest: " + e);
    }



}
//______________________________________________________________

public static String readString(String filePath, String format) {
	// TODO Auto-generated method stub

     String line = "";
     String text = "";
	  
     try{     
      File inputFile = new File(filePath);
      InputStreamReader ist;
      ist = new InputStreamReader(new FileInputStream(inputFile));
      /*if(format=="UTF-8") ist = new InputStreamReader(new FileInputStream(inputFile),"UTF8");
      else ist = new InputStreamReader(new FileInputStream(inputFile));*/

      @SuppressWarnings("resource")
	BufferedReader input = new BufferedReader(ist);
	  
	  while(true){
		line = input.readLine();
		if(line ==null)break;
		//else 	text = text +  System.getProperty("line.separator") + line;
	    else 	text = text + line;
	  
	  }
	
     } catch (FileNotFoundException e) {
          System.err.println("FileStreamsTest: " + e);
     } catch (IOException e) {
        System.err.println("FileStreamsTest: " + e);
     }
     
  
 	 if(format.equals("")){ 
 		return text; 		 
 	 }else{
 		String result = "";
        try {
	      result = new String ( text.getBytes(), format );
        } catch (UnsupportedEncodingException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace(); 
        }
        return result;
 	 }

}

public static String toString(String filePath){
	File file = new File(filePath);
	
	   InputStreamReader ist = TextFile.readStream(file.getPath());
	   BufferedReader input = new BufferedReader(ist);
	   String line = "";
	   String text = "";
	   try{
		   while(true){
	     	  line = input.readLine();
	    	  if(line == null)break;
	    	  else{  

	    		  if(!line.equals(System.getProperty("line.separator"))){
	    		     text = text + line + System.getProperty("line.separator");
	    		  }
	    		  
	    	  }
	      }
	   } catch (FileNotFoundException e) {
	    	    System.err.println("FileStreamsTest: " + e);
	   } catch (IOException e) {
	     	    System.err.println("FileStreamsTest: " + e);
	   }
	   

	 		String result = "";
	        try {
		      result = new String ( text.getBytes(), "UTF-8" );
	        } catch (UnsupportedEncodingException e) {
		      // TODO Auto-generated catch block
		      e.printStackTrace(); 
	        }
	   
return result;
}
/**
 * 
 * @param filePath
 * @return
 */
public static void setLines(ArrayList<String> list, String filePath){
	if(list.size()>0){
		write(list.get(0), filePath, "", true);
	}else{
		write("", filePath, "", true);
	}
	
	for(int i = 1; i < list.size(); i++){
		write(System.getProperty("line.separator") + list.get(i), filePath, "", false);
	}
	
}

public static ArrayList<String> getLines(String filePath){
	File file = new File(filePath);
	InputStreamReader ist = TextFile.readStream(file.getPath());
	BufferedReader input = new BufferedReader(ist);
	String line = "";
	ArrayList<String> res = new ArrayList<String>(); 
	try{
		 while(true){
	    	  line = input.readLine();
	    	  if(line == null)break;
	    	  else{  

	    		  if(!line.equals(System.getProperty("line.separator"))){
	    		     res.add(line);
	    		  }
	    		  
	    	  }
	      }
	 } catch (FileNotFoundException e) {
	    	    System.err.println("FileStreamsTest: " + e);
	 } catch (IOException e) {
	     	    System.err.println("FileStreamsTest: " + e);
	 }
	   
return res;
}






public static InputStreamReader readStream(String filePath) {
	// TODO Auto-generated method stub
	InputStreamReader ist = null; 
     try{     
      File inputFile = new File(filePath);
      ist = new InputStreamReader(new FileInputStream(inputFile));
      
  } catch (FileNotFoundException e) {
        System.err.println("FileStreamsTest: " + e);
  } 
return  ist;
}
//_______________________________________________________
public static void ANSItoUTF8(String filePathIn, String filePathOut) {
	write(readString(filePathIn, ""), filePathOut, "UTF-8", true);	

}
//_______________________________________________________
public static void UTF8toANSI(String filePathIn, String filePathOut) {
	write(readString(filePathIn, "UTF-8"), filePathOut, "", true);
}
	






public static void main(String[] args) {
String path = "files/f3.xmi/";
	
ArrayList<String> words = TextFile.getLines(path); 
System.out.println(words.size());
	for(int i =0; i < words.size(); i++){
		System.out.println(words.get(i));
	}


	//TextFile.write("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB", path, "", true);
}
}
