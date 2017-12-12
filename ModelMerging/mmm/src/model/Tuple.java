package model;

import java.util.ArrayList;

import compare.Compare;
import mach.Match;
import mach.PairOfElement;
import util.Configuration;

/**
 * represents a tuple of elements
 * @author Boubakir
 *
 */
public class Tuple {
	
	private String id;
	private ArrayList<Element> elements;
	private ArrayList<Integer> distribution;
	private float weight;
	
	private ArrayList<PairOfElement> pairs = new ArrayList<PairOfElement>();
	
	
	
	public Tuple(Element elt, int Layer) {
		this.elements = new ArrayList<Element>();
		for(int i = 0; i  < elt.getOriginals().size(); i++){
			if(elt.getOriginals().get(i).getLevel()==1){
				this.elements.add(elt.getOriginals().get(i));
			}
		}
				
		this.id = elt.getId();
	}
	
	
	public Tuple(ArrayList<Element> elements) {
		this.elements = elements;
	}



	public ArrayList<Element> getElements() {
		return elements;
	}



	public void setElements(ArrayList<Element> elements) {
		this.elements = elements;
	}

	
	
	public String getId() {
		return id;
	}


	public float getWeight() {
		return weight;
	}


	/**
	 * -------------------------------------
	 */
	public void disp() {
		
		System.out.println();
		System.out.print(this.id+" orig(");
		for(int i = 0; i < this.elements.size(); i++){
			if(i<this.elements.size()-1){
				System.out.print(this.elements.get(i).getId()+", ");
			}else{
				System.out.println(this.elements.get(i).getId()+")");
			}
		}
		
		
	}

	/**
	 * Create distribution-------------------------------------
	 */
	
private void  createDistribution(String role){
		
		ArrayList<Element> l1 = new ArrayList<Element>();
		//ArrayList<String> l1 = new ArrayList<String>();
		ArrayList<Integer> l2 = new ArrayList<Integer>();
		ArrayList<Integer> l3 = new ArrayList<Integer>();
		
		int max = 1; //Le max des apparaission des propriété
		
		for(int i =0; i < this.elements.size(); i++){
			Element elt = this.elements.get(i);
			
			for(int k = 0; k < elt.getSubElements().size(); k++ ){
				Element property =  elt.getSubElements().get(k);
				//String property =  elt.properties.get(k).getName();
				if(property.getRole()==role){
					boolean b = false;	int m = 0;
					
					while(m<l1.size() && !b){
						float s = isSimilair(l1.get(m), property);
						if(s==-1){
							m++;
						}else{//S'il sont similaires
							b = true;
						}
					}//while(m<l1.size() && !b){
					
					if(b){
						int card = l2.get(m);
						card++;
						Integer x = new Integer(card);
						if(x>max) max = x;
						l2.set(m, x);
					}else{
						l1.add(property);
						l2.add(new Integer(1));
					}
				}//if
					
			}//for(int k = 0; k < elt.properties.size(); k++ ){
			
			
			
		}
	
		for(int i = 0; i <= max; i++ ){
			l3.add(new Integer(0));
		}
	
	
		
		for(int i = 0; i < l2.size(); i++ ){
			int x = l2.get(i);
			int card = l3.get(x);
			card++;
			Integer y = new Integer(card);
			l3.set(x, y);			
		}
		
		
		
		
		
		this.distribution = l3;
		
		//Supp
		/*System.out.println(" **************************** ");
		for(int i = 0; i < this.distribution.size(); i++){
			System.out.println("dist : "+i+"  "+this.distribution.get(i));
			System.out.println(" -------------- ");
		}*/
		//-Supp
		
		
	}
	
	
	/**
	 * -----------------------------------------------------
	 * @param j
	 * @return
	 */
	private int np(int j){
		if(j<this.distribution.size()){
			return this.distribution.get(j);
		}
		return 0;
	}
	
	private int pi(){
		int p = 0;
		

		
		for(int i = 1; i < distribution.size(); i++){
			p = p + distribution.get(i);
		}
		
		return p;
	}
	
	
	/**
	 * Vérifier si deux éléments sont similaires
	 * @param elt1
	 * @param elt2
	 * @return
	 */
	
	private float isSimilair(Element elt1, Element elt2){
		float res = -1;
		int i =0;
		while(i < this.pairs.size() && res==-1){
			
			if((this.pairs.get(i).getFirst()==elt1 && 
					this.pairs.get(i).getSecond()==elt2) 
					|| (this.pairs.get(i).getFirst()==elt2 && 
					this.pairs.get(i).getSecond()==elt1)
														){
				res = (this.pairs.get(i).getSimDeg());
				
			}//if
			i++;
		}//while
		
		if(res < Configuration.getThreshold(elt1.getRole())) res = -1;
		
		return res;
	}
	
	/**
	 * Comparer les élements du tuple
	 */
	private void compareElements(){
		
		ArrayList<Match> matches2 = new ArrayList<Match>();
		ArrayList<Match> matches3 = new ArrayList<Match>();
		for(int i = 0; i < this.elements.size(); i++){
			Element e1 = this.elements.get(i);
			for(int j = i+1; j < this.elements.size(); j++){
				Element e2 = this.elements.get(j);
				
				ArrayList<Match> matches1 = new ArrayList<Match>();
				
				ArrayList<Element> E1 = e1.getSubElements("ClassName");
				ArrayList<Element> E2 = e2.getSubElements("ClassName"); 
				Compare.compare(E1, E2, null, null, "ClassName", matches1);
				
				ArrayList<Element> E3 = e1.getSubElements("ClassAttribute");
				ArrayList<Element> E4 = e2.getSubElements("ClassAttribute"); 
				Compare.compare(E3, E4, null, null, "ClassAttribute", matches1);
				
				
				ArrayList<Element> E5 = e1.getSubElements("ClassOperation");
				ArrayList<Element> E6 = e2.getSubElements("ClassOperation"); 
				Compare.compare(E5, E6, null, null, "ClassOperation", matches1);
				
				matches2 =  Match.confirmaMatches(matches1);
				matches3.addAll(matches2);
				
				
				
				
				
			}//for(int j = i+1; j < this.elements.size(); j++){
			
		}//for(int i = 0; i < this.elements.size(); i++){
		
	
		
		
		
		for(int i = 0; i < matches3.size(); i++){
			//matches3.get(i).disp2();
			Match match = matches3.get(i);
			if(match.getCategory()==3){
PairOfElement pair = new PairOfElement(match.getElements().get(0), match.getElements().get(1), match.getWeight());
				this.pairs.add(pair);
			}else{
				this.pairs.addAll(match.getPairs());
			}
			
			
			 //System.out.println(" ----  "+this.matches.get(i).getLayer());
		}
		
		
/*

for(int i = 0; i < this.pairs.size(); i++){
	this.pairs.get(i).disp2();
}*/
		
		

	//Créer la liste des pairs de modèles similaires
	
	}
	
	/**
	 * calculates the weight of the tuple
	 * @param n
	 */
	public void calculWeight(int n){

		float w1 = this.calculWeight(n, "ClassName");
		float w2 = this.calculWeight(n, "ClassAttribute");
		//float w3 = this.calculWeight(n, "ClassOperation");
		
		this.weight = w1*Configuration.getWeight("ClassName")+
				w2*Configuration.getWeight("ClassAttribute");
		
		
	}
	
	/**
	 * Calculer le poids du typle pour un role donné
	 */
	private float calculWeight(int n, String role){
		
		this.compareElements();
		
		//this.createDistribution("ClassName");
		//this.createDistribution("ClassAttribute");
		this.createDistribution(role);
		
		//SUPP
		/*System.out.println("   distribution  ----------");
		for(int i =0; i<this.distribution.size(); i++){
			System.out.println(i+"  "+this.distribution.get(i));
			
		}*/
		//-SUPP
		
		

		int m = this.elements.size(); //size of the tuple;
		
		
		float x = 0;
		for(int j = 2; j <=m; j++){
			x = x + j*j*np(j);
		}
		
		float y = n*n*pi();
		//this.weight = x/y;
		return x/y;
		
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

}
