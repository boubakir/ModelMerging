package merge;

import java.util.ArrayList;

import Edmond.jicos.WeightedMatch;
import compare.Compare;
import mach.CompareAndMatchResult;
import mach.Match;
import mach.PairOfElement;
import model.Element;
import model.Model;

/**
 * performs model merging
 * @author Boubakir
 *
 */
public class Merger {
	
	
	/**
	 * performs an iteration of 3M algorithm
	 * @param models: a set of models
	 * @return
	 */
	public static ArrayList<Model> iterate(ArrayList<Model> models){
		ArrayList<Model> models2 = new ArrayList<Model>();
		
		int[][] weightMatrix = new int[models.size()][models.size()];
		CompareAndMatchResult[][] comparetMatrix = new CompareAndMatchResult[models.size()][models.size()];
	
		//Comparer chaque paire de modèles
		for(int i = 0; i < models.size(); i++){
			//System.out.println("*************");
			for(int j = 0; j < models.size(); j++){
			
				if(i==j){
					weightMatrix[i][j] = 0;
				}else{
CompareAndMatchResult cAResult = Compare.CompareAndMatch(models.get(i), models.get(j));
							
					
					float x = cAResult.getSimDeg()*10000;
					
					weightMatrix[i][j] = (int)x;
					comparetMatrix[i][j] = cAResult;
					
				}//if(i==j){
				
				
				
			}//for(int j = 0; j < models.size(); j++){
		}//for(int i = 0; i < models.size(); i++){
		
		
		//WeightedMatch.afficher(weightMatrix, "       ");
		
		//System.out.println(" Les pairs de modèles a merger ");
		
		ArrayList<int[]> result = WeightedMatch.getResult(weightMatrix);
		for ( int i = 0; i < result.size(); i++ ){
       	// System.out.println(result.get(i)[0]+"   "+result.get(i)[1]);
        
     	//System.out.println("Merger les modèles suivants :");
       	 if(result.get(i)[1]==-1){
       		 
       		Model m1 = models.get(result.get(i)[0]);
       		models2.add(m1);
       		 
       	 }else{
       		 
       		Model m1 = models.get(result.get(i)[0]);
       		Model m2 = models.get(result.get(i)[1]);
       		//System.out.println(m1.getId()+"   "+m2.getId()); 
       		
       		CompareAndMatchResult cAResult = comparetMatrix[result.get(i)[0]][result.get(i)[1]];
       				
       		//CompareAndMatchResult compareAndMatchResult = Compare.CompareAndMatch(m1, m2);
    		Model model = Merger.merge(m1, m2, cAResult);
    		models2.add(model);
    		
       	 }//else
       	}//for ( int i = 0; i < result.size(); i++ ){
		
		return models2; 
	}

	
	/**
	 * performs the merging of a set of models, this methods implements 3M algorithm
	 * @param models: the set of in put models
	 * @return
	 */
	public static Model mmm(ArrayList<Model> models){
		
		while(models.size()>1){
			models = Merger.iterate(models);	
		}
		
		return models.get(0);
	}
	
		
/**
 * merging a pair of models
 * @param M1: first model
 * @param M2: second model
 * @param compareAndMatchResult: the result of comparing M1 and M2
 * @return
 */
	public static Model merge(Model M1, Model M2, CompareAndMatchResult compareAndMatchResult) {
		
		//Créer un nouveau modèles
		Model resultModel = new Model();	resultModel.setId(M1.getId()+"-"+M2.getId());
		
		ArrayList<Match> matches = compareAndMatchResult.getOptimalMatch();

		int layer = 1;
		while(layer <=5){
			
			for(int i =0; i < matches.size(); i++ ){
				Match match = matches.get(i);
				
				ArrayList<PairOfElement> pairs2 =  match.getPairs();
				
				//System.out.println(match.getLayer()+"  "+layer+" ------------Je suis dans Merger");
				
				if(match.getLayer()==layer){
					if(match.getCategory()==3){
						
resultModel.addElement(match.getElemnts1(), match.getElemnts2(), match.getParentElement1(), match.getParentElement2());
//this.resultModel.addElement(match.getElemnts2(), match.getParentElement1(), match.getParentElement2());
					
					}else{
						for(int j = 0; j < pairs2.size(); j++ ){
							PairOfElement pair = pairs2.get(j);
							Element elt = new Element(pair.getFirst(), pair.getSecond()) ;
							resultModel.addElement(elt, match.getParentElement1(), match.getParentElement2());
						}//for(int j = 0; j < pairs2.size(); i++ ){
						
						
					}//if(match.getCategory()==3){
					
					
				}//if(match.getLayer()==layer){
			}//for(int i =0; i < matches.size(); i++ ){
			
			
							
			
			layer++;
		}//while(layer <=5){	
	
		
		
		layer = 1;
		while(layer <=5){	
		
		//Ajouter les éléments unmatched
		
		
			for(int i = 0; i < compareAndMatchResult.getUnmatched().size(); i++){
				
				Element elt = compareAndMatchResult.getUnmatched().get(i);
				if(elt.getLevel()==layer){

					resultModel.addUnmatchedElement(elt);
					//elt.disp(" ++++++++++++++++ ");
					
				
				}
				
			}//for(int i = 0; i < compareAndMatchResult.getUnmatched().size(); i++){
			
			layer++;
		}//while(layer <=5){	
		
		
		
	return resultModel;
	}



	
	

public static void main(String[] args) {
	//ArrayList<Model> models = Model.getModelsFromRep("models/simple/");
	ArrayList<Model> models = Model.getModelsFromRep("models/GPL/");
	//ArrayList<Model> models = Model.getModelsFromRep("models/simple/");
	
	System.out.println("Displaying models");
	for(int i=0; i < models.size(); i++){
		models.get(i).disp();
	}
	
	System.out.println("Merging models");
	Model model = Merger.mmm(models);
	
	System.out.println("Displaying the result");
	
	System.out.println("Saving the result in models/simile/res.xmi");
	model.save("models/simple/res.xmi");
	
	/*System.out.println("Saving the result in models/GPL/res.xmi");
	model.save("models/simple/GPL.xmi");*/
	
	
	/*System.out.println("Saving the result in models/Notepad/res.xmi");
	model.save("models/Notepad/res.xmi");*/
	
	
	
	
	System.out.println("END");
		
	}
	
	
}
