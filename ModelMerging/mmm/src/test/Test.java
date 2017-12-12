package test;

import java.util.ArrayList;

import merge.Merger;
import model.Model;

public class Test {
	
	
	
	public static void main(String[] args) {
	
	ArrayList<Model> models = Model.getModelsFromRep("models/simple/");
	//ArrayList<Model> models = Model.getModelsFromRep("models/GPL/");
	
	
	System.out.println("Displaying models");
	for(int i=0; i < models.size(); i++){
		models.get(i).disp();
	}
	
	System.out.println("Merging models");
	Model model = Merger.mmm(models);
	
	System.out.println("Displaying the result");
	
	System.out.println("Saving the result in models/simple/res.xmi");
	model.save("models/simple/res.xmi");
	
	/*System.out.println("Saving the result in models/GPL/res.xmi");
	model.save("models/simple/GPL.xmi");*/
	
	
	
	
	System.out.println("END");
		
	}
	
	
}
