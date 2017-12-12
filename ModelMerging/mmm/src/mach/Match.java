package mach;

import java.util.ArrayList;

import hungarian.Hungarian;
import model.Element;

/**
 * represents a matching
 * @author Boubakir
 *
 */
public class Match {

	
	private ArrayList<Element> elemnts1;
	private ArrayList<Element> elemnts2;
	
	private ArrayList<PairOfElement> pairs;
	private ArrayList<Element> elements;

	private Element parentElement1;
	private Element parentElement2;
	
	private float[][] matrix;
	private float weight;
	

//Constructors ----------------------------------------------------------------
	


	/**
	 * construct a matching
	 * @param E1: first set of elements
	 * @param E2: second  set of elements
	 * @param parent1 : element parent of E1
	 * @param parent2 : element parent of E2
	 */
	public Match(ArrayList<Element> E1, ArrayList<Element> E2, Element parent1, Element parent2) {
		this.elemnts1 = E1;
		this.elemnts2 = E2;
		this.parentElement1 = parent1;
		this.parentElement2 = parent2;
		this.pairs = new ArrayList<PairOfElement>();
		this.elements = new ArrayList<Element>();

	}
	
	
	/**
	 * 
	 * construct a matching
	 * @param E1: first set of elements
	 * @param E2: second  set of elements
	 * @param parent1: element parent of E1
	 * @param parent2: element parent of E2
	 * @param matrix: cost matrix 
	 */
	public Match(ArrayList<Element> E1, ArrayList<Element> E2, Element parent1, Element parent2, float[][] matrix) {	
		//A SUPP
		//System.out.println(E1.size()+"  "+E2.size()+"  ---- ");
		//E1.get(0).disp(" ....  ");
		//E2.get(0).disp(" ****  ");
		//-A SUPP
		this(E1, E2, parent1, parent2);
		this.matrix = matrix; 
	
	}
	


	
//Guetters Setters ---------------------------------------------------------
	
	public ArrayList<Element> getElemnts1() {
		return elemnts1;
	}


	public ArrayList<Element> getElemnts2() {
		return elemnts2;
	}


	public int getLayer(){
		if(this.parentElement1==null)	return 1;
		else {
			//System.out.println("  0000000000 "+this.parentElement1.getRole());
			return this.parentElement1.getLevel()+1;
			
		}
	}
		
	
	public ArrayList<PairOfElement> getPairs() {
		return pairs;
	}


	public void setMatch(ArrayList<PairOfElement> match) {
		this.pairs = match;
	}
	
	
	
	
	public Element getParentElement1() {
		return parentElement1;
	}


	public Element getParentElement2() {
		return parentElement2;
	}

	
	
	
public ArrayList<Element> getElements() {
		return elements;
	}

	//-----------------------Methodes----------------------------------------------
	/**
	 * allows to perform a match using the huangarian method
	 */
public void hungarianMatch(){
	
	Hungarian h  = new Hungarian(matrix, false);
	h.execute();
	int[][] result = h.getAssignment();
	h.calculateOptimalValue();
	
	this.weight = h.getOptimalValue();
	
	
	for(int i = 0; i < result.length; i++){
		
		
		int letfEltNum = result[i][0];
		int rightEltNum = result[i][1];
		
		if(letfEltNum<this.elemnts1.size()  &&  rightEltNum< this.elemnts2.size() ){
			
			float simDeg = h.getOriginalCostsMatrix()[letfEltNum][rightEltNum];
			
			if(simDeg > 0){
				PairOfElement pair = new PairOfElement(elemnts1.get(letfEltNum), elemnts2.get(rightEltNum), simDeg);
				this.pairs.add(pair);
			}	
				
		}

	}//for(int i = 0; i < result.length; i++){

}//hungarianMatch(){------------------------------------------------------

/**
 * returns the match having the largest weight value	
 */
	
	public void maxMatch(){
		
		
		float max = 0;
		for(int i = 0; i < this.elemnts1.size(); i++){
			for(int j = 0; j < this.elemnts2.size(); j++){
				if(matrix[i][j]>max){
					max = matrix[i][j]; 
				}
			}//for(int j = 0; j < this.elemnts2.size(); j++){
			
		}//for(int i = 0; i < this.elemnts1.size(); i++){
		this.elements.addAll(this.elemnts1);
		this.elements.addAll(this.elemnts2);
	this.weight = max;
	}


	public void setMatches(ArrayList<PairOfElement> matches) {
		this.pairs = matches;
	}

/**
 * get the weight of the matching
 * @return
 */
	public float getWeight() {
		return weight;
	}


	public void setWeight(float weight) {
		this.weight = weight;
	}

	/**
	 * allows to add a pair of elements
	 * @param pair
	 */
	public void addPairOfElement(PairOfElement pair) {
		this.pairs.add(pair);
	}
	
	/**
	 * allows to display the matching
	 */
	public void disp() {
		for(int i = 0; i< this.pairs.size(); i++){
			this.pairs.get(i).disp();
		}
		System.out.println("weight = "+this.weight);
	}
	
	/**
	 * allows to display the matching
	 */
	public void disp2() {
		System.out.println();
		if(this.parentElement1==null){
			System.out.println("Match : Classe ");
		}else{
			System.out.println("Match : ("+this.parentElement1.getId()+"), ("+this.parentElement2.getId()+")");
		}
		
		//System.out.println(this.pairs.size()+" -----*");
		for(int i = 0; i< this.pairs.size(); i++){
			
			this.pairs.get(i).disp2();
		}
		
		//third categorie
		for(int i = 0; i< this.elements.size(); i++){
			
			//this.elements.get(i).disp(" ££££££££  ");
			System.out.println(this.elements.get(i).getId()+"    "+this.elements.get(i).getValue());
		}
		
		System.out.println("weight = "+this.weight);
	}


	/**
	 * rturns the category of the elements of the matching
	 * @return
	 */
	public int getCategory(){
		
		int cat = 0;
		if(elemnts1.size()==0){
			if(elemnts2.size()==0){
			}else cat = Element.getCategory(elemnts2.get(0).getRole());
		}else cat = Element.getCategory(elemnts1.get(0).getRole());

		return cat;
	}
	
	
	/**
	 * allows to check whether a match belongs to a pair 
	 * @param pairs
	 * @return
	 */
	private boolean belongs(PairOfElement pair){
		
		if(this.parentElement1!=null && this.parentElement2!=null ){
			return (this.parentElement1 == pair.getFirst())
				&&(this.parentElement2 == pair.getSecond());
			
		}else return false;
	}
	
	private boolean subMatch(Match match){
		
		int i = 0; 	boolean res = false;
		
		while(i< match.pairs.size() && !res){
			if(this.belongs(match.pairs.get(i))){
				res = true;
			}else{
				i++;
			}
		}//while(i< this.pairs.size() && !res){

		return res;
	}
	

	/**
	 * allows to select the matching
	 * @param matches
	 * @return
	 */
	public static ArrayList<Match> confirmaMatches(ArrayList<Match> matches){
	
		ArrayList<Match> res = new ArrayList<Match>();
		ArrayList<Match> matches2 = new ArrayList<Match>();
		
		
		//Mettre les éléments du premier orderes comme des classes
		//dans res
		for(int i =0; i < matches.size(); i++){
			
			Match match = matches.get(i);
			//if the elements of the match are first layer lake
			//classes for example
			if(match.parentElement1==null) {
				
				res.add(match);
			}else{
				matches2.add(match);
			}
			
			
		}//for(int i =0; i < matches.size(); i++){
		
		matches = matches2;

	boolean repeat = true;
while(repeat){		

		matches2 =  new ArrayList<Match>();
		
		for(int i =0; i < matches.size(); i++){
			for(int j =0; j < res.size(); j++){
		
					Match match1 = matches.get(i);
					Match match2 = res.get(j);
					
					
					
					if(match1.subMatch(match2)){		
						matches2.add(match1);
						//matches2.add(match2);
						
					}else{
					}//
			
			}//for(int j =i+1; j < matches.size(); j++){
		}//for(int i =0; i < matches.size(); i++){
		
		
		
		for(int i =0; i < matches2.size(); i++){
			matches.remove(matches2.get(i));
		}
		
		res.addAll(matches2);
		
		if(matches2.size()==0){
			repeat = false;
		}
			
			
		
	}//while
		
		
		
		
		
		return res;
	}
	
	
	/**
	 * allows to check whether a matching contains an element
	 * @param: an element
	 * @return
	 */
	public boolean contains(Element elt){
		boolean res = false;
		
		if(elt.getCategory()==3){
			
			int i = 0;
			while( i < this.elements.size() && !false){
				
				if(elt == this.elements.get(i)  ){
					
					res = true;
				}
				i++;
			}//while{
			
		}else{
			int i = 0;
			while( i < this.pairs.size() && !false){
				
				if(elt == this.pairs.get(i).getFirst() || elt == this.pairs.get(i).getSecond()  ){
					res = true;
				}
				i++;
			}//while{
		}
		return res;
		
	}

/**
 * returns the number of pairs in the matching
 * @return
 */
	public int getCard(){
		return this.pairs.size();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		


	}

}
