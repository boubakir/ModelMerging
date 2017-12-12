package compare;

import java.util.ArrayList;


import mach.CompareAndMatchResult;
import mach.Match;
import mach.PairOfElement;
import matcher.words.linguistic.LinguisticMatching;
import matcher.words.typographic.NGramDistance;
import model.Element;
import model.Model;
import util.Configuration;

/**
 * This class allows to perform all comparison operations
 * It allows for example to compare two models, two elements, and two sets of elements, etc. 
 * @author Boubakir
 *
 */

public class Compare {
	
	
	
	
	/**
	 * allows to compare two models 
	 * @param M1: first model
	 * @param M2: second model
	 * @return
	 */
	public static CompareAndMatchResult CompareAndMatch(Model M1, Model M2){
		CompareAndMatchResult res = new CompareAndMatchResult(M1, M2);
		
		//Otenir la liste des éléments de chaque modèles
		ArrayList<Element> E1 = M1.getElements("Class");
		ArrayList<Element> E2 = M2.getElements("Class");
		
		//Comparer les éléments des deux modèles
		ArrayList<Match> matches = new ArrayList<Match>(); 
		float SimDeg = Compare.compare(E1, E2, null, null, "Class", matches);
		//SUPP
		//System.out.println(SimDeg + " ------------------");
		//SUPP
		ArrayList<Match> matches2 =  Match.confirmaMatches(matches);
				
		res.setOptimalMatch(matches2);
		res.setSimDeg(SimDeg);
		res.createUnmatchedElements();
		
		
		
		return res;
	}
	
	
	/**
	 * This function is applicable on two set of elements having the same role. 
	 * It permits to identify the matched pairs of elements and return a value between 0 and 1,
	 * which corresponds to the similarity degree between them.
	 * @param E1: first set of elements
	 * @param E2: second set of elements
	 * @param parent1: the parent element of E1
	 * @param parent2: the parent element of E2
	 * @param role: the role of E1 and E2.
	 * @param matches: the set of matches identified between the elements of E1 and those of E2
	 * @return
	 */
	public static float compare(ArrayList<Element> E1, ArrayList<Element> E2, Element parent1, Element parent2, String role, ArrayList<Match> matches ){
		

		float similarityDagree = 0;
		
if(Element.getCategory(role)==1 || Element.getCategory(role)==3){
		

	
	
		//Create the cost matrix
		int n = E1.size(); int m = E2.size();
		int dim = n;	if(n<m) dim = m;
		float[][] matrix = new float[dim][dim];
		
	boolean isAtomic = false;
	
	//traiter le cas des ensembles vides
	if((E1.size()==0)){
		if(E2.size()==0){
			return 1; //deux ensemble vide sont considéré identique
		}else{
			if(E2.get(0).isAtomic()) isAtomic = true;
		}
	}else{//if((E1.size()==0)){
			if(E1.get(0).isAtomic()) isAtomic = true;
	}

	//if the element of E1 and E2 are atomic
	if(isAtomic){
		for(int i = 0; i< E1.size(); i++){
			Element e1 = E1.get(i);
			for(int j = 0; j< E2.size(); j++){
				Element e2 = E2.get(j);
				float s = Compare.compare2Atomic(e1, e2);

				//New
				//if(s>=Element.getThreshold(role)) matrix[i][j] = s;
				//-New
				matrix[i][j] = s;		
			}//------------for(int j = 0; j< elements2.size(); j++){
		}//----------------for(int i = 0; i< elements1.size(); i++){
	}else{//------------------------------if(E1.get(0).isAtomic()){
		//if the compared elements are compound	
		for(int i = 0; i< E1.size(); i++){
			Element e1 = E1.get(i);
			for(int j = 0; j< E2.size(); j++){	
				Element e2 = E2.get(j);
				matrix[i][j] = compare2compund(e1, e2, matches);
				
				
				//New
				//float s = compare2compund(e1, e2, matches);
				//if(s>=Element.getThreshold(role)) matrix[i][j] = s;
				//-New
			}//------------for(int j = 0; j< elements2.size(); j++){
		}//----------------for(int i = 0; i< elements1.size(); i++){					
	}//------------------------------else if(E1.get(0).isAtomic()){
				
	
	//NEW
	//manipulate values that are below the threshold
	for(int i = 0; i< E1.size(); i++){
		for(int j = 0; j< E2.size(); j++){
			if(matrix[i][j] < Configuration.getThreshold(role)){
				matrix[i][j] = 0;
			}
		}
	}
	//DEP ----------------------- orignal1
	//-NEW
	
	if(Element.getCategory(role)==1){
		
		
		//PRGINAL ----------------------- orignal1
		
		Match match = new Match(E1, E2, parent1, parent2, matrix);
		match.hungarianMatch();
		
		//A SUPRIMER
		//match.disp();
		//-A SUPRIMER
		
		if(match.getPairs().size()!=0) matches.add(match);
		//matches.add(match);
		
		//New 09/04/17
		//similarityDagree = match.getWeight()/Math.max(E1.size(), E2.size());
		
		similarityDagree = match.getWeight()/(E1.size()+E2.size()-match.getCard()  );
		
		//New 09/04/17

		//A SUPRIMER
		
		//System.out.println(match.getCard()+"   E1 "+E1.size()+"   E2 "+E2.size());
		//-A SUPRIMER
		
		
		match.setWeight(similarityDagree);
			
		return similarityDagree;
	
	}else{//Third category ------------------------------------------------------
		
		
		//System.out.println(" Third category ");
		Match match = new Match(E1, E2, parent1, parent2, matrix);
		match.maxMatch();
		matches.add(match);
		//A SUPP
		//match.disp3();
		//System.out.println(matches.size());
		//-A SUPP
		similarityDagree = match.getWeight();
		
		
		return similarityDagree;
		
	}
				
		 

		
//-------------------------------------------------------------------------	
}else{//if(Element.getCategory(role)==1 || Element.getCategory(role)==2){
	//The second category

	
	

//Match match = new Match(parent1, parent2);
Match match = new Match(E1, E2, parent1, parent2);

boolean isAtomic = false;
	
	if(E1.size()==E2.size()){
		//Continuer
	}else{
		return 0;
	}
	
	//traiter le cas des ensembles vides
	
	if((E1.size()==0)){
		return 1; //deux ensemble vide sont considéré identique

	}else{//if((E1.size()==0)){
			if(E1.get(0).isAtomic()) isAtomic = true;
	}

	
	
	//if the element of E1 and E2 are atomic
	if(isAtomic){
		for(int i = 0; i< E1.size(); i++){
			Element e1 = E1.get(i);
			Element e2 = E2.get(i);

			float s = Compare.compare2Atomic(e1, e2);
			
			
			
			if(s>=Configuration.getThreshold(role)){
				
				PairOfElement pair = new PairOfElement(e1, e2, s);
				match.addPairOfElement(pair);
				similarityDagree =similarityDagree + s;
			}else{
				//match = new Match(parent1, parent2);
				return 0;
			}
		}//----------------for(int i = 0; i< elements1.size(); i++){
	}else{//------------------------------if(E1.get(0).isAtomic()){
		//if the compared elements are compound	
		for(int i = 0; i< E1.size(); i++){
			Element e1 = E1.get(i);
			Element e2 = E2.get(i);
			float s = compare2compund(e1, e2, matches);
			
			if(s>=Configuration.getThreshold(role)){
				PairOfElement pair = new PairOfElement(e1, e2, s);
				match.addPairOfElement(pair);
				similarityDagree =similarityDagree + s;
			}else{
				//System.out.println(" ----------------------------------    "+role);
				//match = new Match(parent1, parent2);
				return 0;
			}
		}//----------------for(int i = 0; i< elements1.size(); i++){					
	}//------------------------------else if(E1.get(0).isAtomic()){

		matches.add(match);
		similarityDagree = similarityDagree/E1.size();
		return similarityDagree; 



}//else


	
}//compare()
	
	
	
	
	/**
	 * Return the similarity degree between two atomic elements
	 * @param e1: first element
	 * @param e2: second element
	 * @return: similarity degree
	 */
	public static float compare2Atomic(Element e1, Element e2){
		
		if(e1.getType().equals("String")){
			return comare2String(e1.getValue(), e2.getValue());
		}else{
			return 0; //------------------A compléter
		}
		
	}
	
	/**
	 * Calculate the similarity degree between two compound elements
	 * @param e1: first element
	 * @param e2: second element
	 * @return: similarity degree
	 */
	public static float compare2compund(Element e1, Element e2, ArrayList<Match> matches ){
	
		float s = -1;
		
		if(e1.getType().equals("Class")){
			
			ArrayList<Element> N1 = e1.getSubElements("ClassName");
			ArrayList<Element> N2 = e2.getSubElements("ClassName");
			
			ArrayList<Element> A1 = e1.getSubElements("ClassAttribute");
			ArrayList<Element> A2 = e2.getSubElements("ClassAttribute");
			
			ArrayList<Element> O1 = e1.getSubElements("ClassOperation");
			ArrayList<Element> O2 = e2.getSubElements("ClassOperation");
			
			
			//A Modifier
			//s = compare(N1, N2, e1, e2, "ClassName",  matches);	
			
			
//s = 0.40f*compare(N1, N2, e1, e2, "ClassName",  matches)+0.3f*compare(A1, A2, e1, e2,"ClassAttribute", matches)+0.3f*compare(O1, O2, e1, e2, "ClassOperation", matches);
/*
float s1 = Configuration.getWeight("ClassName")*compare(N1, N2, e1, e2, "ClassName",  matches);
float s2 = Configuration.getWeight("ClassAttribute")*compare(A1, A2, e1, e2,"ClassAttribute", matches);
float s3= Configuration.getWeight("ClassOperation")*compare(O1, O2, e1, e2, "ClassOperation", matches);
s = s1+s2+s3;
*/
float s1 = compare(N1, N2, e1, e2, "ClassName",  matches);
float s2 = compare(A1, A2, e1, e2,"ClassAttribute", matches);
float s3=  compare(O1, O2, e1, e2, "ClassOperation", matches);
s = s1*Configuration.getWeight("ClassName")+
	s2*Configuration.getWeight("ClassAttribute")+
	s3*Configuration.getWeight("ClassOperation");
			
			
			
			
			
		}//if(e1.getType().equals("Class")){
		
if(e1.getType().equals("Attribute")){
			
			ArrayList<Element> N1 = e1.getSubElements("AttributeName");
			ArrayList<Element> N2 = e2.getSubElements("AttributeName");
			
			ArrayList<Element> T1 = e1.getSubElements("AttributeType");
			ArrayList<Element> T2 = e2.getSubElements("AttributeType");
			
//s = 0.50f*compare(N1, N2, e1, e2, "AttributeName", matches)+0.50f*compare(T1, T2, e1, e2, "AttributeType", matches);
s = Configuration.getWeight("AttributeName")*compare(N1, N2, e1, e2, "AttributeName", matches)
+Configuration.getWeight("AttributeType")*compare(T1, T2, e1, e2, "AttributeType", matches);
	
//System.out.println(" MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM "+s);

}
		
if(e1.getType().equals("Operation")){
	
	
			
			ArrayList<Element> N1 = e1.getSubElements("OperationName");
			ArrayList<Element> N2 = e2.getSubElements("OperationName");
			
			ArrayList<Element> T1 = e1.getSubElements("OperationType");
			ArrayList<Element> T2 = e2.getSubElements("OperationType");
			
			ArrayList<Element> P1 = e1.getSubElements("Parameter");
			ArrayList<Element> P2 = e2.getSubElements("Parameter");
			
			
			
			
s = Configuration.getWeight("OperationName")*compare(N1, N2, e1, e2, "OperationName", matches)
+Configuration.getWeight("OperationType")*compare(T1, T2, e1, e2, "OperationType", matches)
+Configuration.getWeight("Parameter")*compare(P1, P2, e1, e2, "Parameter", matches);
		}
if(e1.getType().equals("Parameter")){

			ArrayList<Element> N1 = e1.getSubElements("ParameterName");
			ArrayList<Element> N2 = e2.getSubElements("ParameterName");
			
			ArrayList<Element> T1 = e1.getSubElements("ParameterType");
			ArrayList<Element> T2 = e2.getSubElements("ParameterType");
			
			s = Configuration.getWeight("ParameterName")*compare(N1, N2, e1, e2, "ParameterName", matches)
					+Configuration.getWeight("ParameterType")*compare(T1, T2, e1, e2, "ParameterType", matches);

		}
	
	return s;
}//
	

	
	
	
	/**
	 * Compare tow String values
	 * @param: first string
	 * @param: second string
	 * @return: similarity degree
	 */
	public static float comare2String(String s1, String s2){
		float x1 = Compare.typographicMatching(s1, s2, Configuration.getNforNgram());
		float x2 = Compare.linguisticMatching(s1, s2);
		float res = Math.max(x1, x2);
		return res;
		
	}
	
	/**
	 * performs the typographic matching
	 * @param s1: first element
	 * @param s2: second element
	 * @param N: The number of their identical substrings of length N.
	 * (3 is usually used)
	 * @return: typographic similarity
	 */
	public static float typographicMatching(String s1, String s2, int N){
		
		NGramDistance ng = new NGramDistance(N);
		return ng.getDistance(s1.toLowerCase(), s2.toLowerCase()); 
	}
	
	/**
	 * performs the linguistic matching
	 * @param s1: first element
	 * @param s2: second element
	 * @return: linguistic similarity
	 */
	public static float linguisticMatching(String s1, String s2){
		double distance = LinguisticMatching.compute(s1.toLowerCase(), s2.toLowerCase());
		
		return (float) distance;
	}
	
	
	
	/*
	public static float comare2String(String s1, String s2){
		NGramDistance ng = new NGramDistance();
		float s = ng.getDistance(s1.toLowerCase(), s2.toLowerCase());
		return s;
		
	}*/
	
/**
 * allows to compare two elements
 * @param e1: first element
 * @param e2: second element
 * @param match: the set of matchings
 * @return: similarity degree
 */
	public static float compare2Element(Element e1, Element e2, ArrayList<Match> match){
		
		
		if(e1.isAtomic()){
			return Compare.compare2Atomic(e1, e2);
		}else{
			return Compare.compare2compund(e1, e2, match);
		}
		

		
	}
		
		


	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}
		
	

}
