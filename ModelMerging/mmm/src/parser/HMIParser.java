package parser;

import java.io.*;
import org.jdom2.*;
import org.jdom2.input.*;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import model.Model;
import util.MyString;

import java.util.List;
import java.util.Iterator;

/**
 * allows to parse the XMI files using DOM
 * @author Boubakir
 *
 */
public class HMIParser{
	
	private org.jdom2.Document document;
	private Element racine;
	private int curentElemntId = 0;
	private String modelName;
	
	private Model resultingModel = new Model();


	public Model getModel() {
		return resultingModel;
	}

	public org.jdom2.Document getDocument() {
		return document;
	}

	public void setDocument(org.jdom2.Document document) {
		this.document = document;
	}

	public Element getRacine() {
		return racine;
	}

	public void setRacine(Element racine) {
		this.racine = racine;
	}

	public int getCurentElemntId() {
		return curentElemntId;
	}

	public void setCurentElemntId(int curentElemntId) {
		this.curentElemntId = curentElemntId;
	}

	public String getEltId(){
		curentElemntId++;
		return this.modelName+curentElemntId;
	}

   /**
    * Parsser le document XMI
 * @throws DataConversionException 
    */
	
	
	/**
	 * Parcer le document
	 * @throws DataConversionException
	 */
   @SuppressWarnings("rawtypes")
private void parse() throws DataConversionException{
	   

      
         List classes = racine.getChildren("classes");
         //List classes = packagee.getChildren("classes");
         
         Iterator c = classes.iterator();
         
        //parcourir la liste des classes
         while(c.hasNext()){
        	 
        	 
        	 Element classeXMI = (Element)c.next();
        	
        	 //Créer un élément de type classe
        	 model.Element classe = new model.Element(this.getEltId(),"Class","Class");
        	 resultingModel.addElement(classe);
        	 
        	//Créer un élément de type nom de classe
        	 for(int i = 0; i < classeXMI.getAttributes().size(); i++){
	        	 //String classeNameName = classeXMI.getAttributes().get(i).getName();
	        	 String classeNameValue = classeXMI.getAttributes().get(i).getValue();      	 
	        	 model.Element classeName = new model.Element(this.getEltId(),"String","ClassName", classeNameValue);
	        	 
	        	 classe.addSubElement(classeName);
	        	 resultingModel.addElement(classeName);
        	 }
        	 
        	 
        	//parcourir la liste des attributs
        	 List attributs = classeXMI.getChildren("attributes");
             Iterator a = attributs.iterator();
             while(a.hasNext()){
            	Element attXMI = (Element)a.next();
            	

           	 
            	
            	 //Créer un élément de type attribut
           	 	model.Element attribute = new model.Element(this.getEltId(),"Attribute","ClassAttribute");
           	 	classe.addSubElement(attribute);
           	 	resultingModel.addElement(attribute);
            	
           	 	for(int i = 0; i < attXMI.getAttributes().size(); i++){
           	 	
	            	 //String attNameName = attXMI.getAttributes().get(i).getName();
	            	 String attNameValue = attXMI.getAttributes().get(i).getValue();      	 
	            	 
	            	 //String attTypeName = attXMI.getAttributes().get(i+1).getName();
	            	 //String attTypeValue = attXMI.getAttributes().get(i+1).getValue();      	 
	            	 
	            	 
	            	 
	            	 model.Element attName = new model.Element(this.getEltId(),"String","AttributeName", attNameValue);
	            	 //model.Element attType = new model.Element(this.getEltId(),"String","AttributeType", attTypeValue);
	            	 
	            	 attribute.addSubElement(attName);
	            	 //attribute.addSubElement(attType);
	            	 
	            	 this.resultingModel.addElement(attName);
	            	 //this.resultingModel.addElement(attType);
	            	//System.out.println(" %%%%%%%%%%%%%%%%%%%%%%%%%%%%   "+attName.getId());
	            	//System.out.println(" ???????????????????????????   "+attType.getId());
           	 	}
             }//while(a.hasNext()){	 
        	 
        	 
           //parcourir la liste des -OPERATIONS--------------------
        	 List operations = classeXMI.getChildren("operations");
             Iterator o = operations.iterator();
             while(o.hasNext()){
            	Element opXMI = (Element)o.next();
            	
            	 //Créer un élément de type operation
            	 
           	 	model.Element operation = new model.Element(this.getEltId(),"Operation","ClassOperation");
           	 	classe.addSubElement(operation);
           	 	this.resultingModel.addElement(operation);
            	
           	 	for(int i = 0; i < opXMI.getAttributes().size(); i++){
	            	 //String opNameName = opXMI.getAttributes().get(i).getName();
	            	 String opNameValue = opXMI.getAttributes().get(i).getValue();      	 
	            	 
	            	 //String opTypeName = opXMI.getAttributes().get(i+1).getName();
	            	 //String opTypeValue = opXMI.getAttributes().get(i+1).getValue();      	 
	            	 
	            	 
	            	 
	            	 model.Element opName = new model.Element(this.getEltId(),"String","OperationName", opNameValue);
	            	 //model.Element opType = new model.Element(this.getEltId(),"String","OperationType", opTypeValue);
	            	 
	            	 operation.addSubElement(opName);
	            	 //operation.addSubElement(opType);
	            	 
	            	 this.resultingModel.addElement(opName);
	            	 //this.resultingModel.addElement(opType);
             }
            	 
            	//parcourir la liste des parametres PARAMETER --------------------------
            	 List parameters = opXMI.getChildren("parameters");
                 Iterator par = parameters.iterator();
                 while(par.hasNext()){
                	Element parXMI = (Element)par.next();
                	
                	 //Créer un élément de type parametre
                	 
               	 	model.Element parameter = new model.Element(this.getEltId(),"Parameter","Parameter");
               	 	operation.addSubElement(parameter);
               	 	this.resultingModel.addElement(parameter);
                	
               	 for(int i = 0; i < parXMI.getAttributes().size()-1; i=i+2){
                	
	                	 //String parNameName = parXMI.getAttributes().get(i).getName();
	                	 String parNameValue = parXMI.getAttributes().get(i).getValue();      	 
	                	 
	                	 //String parTypeName = parXMI.getAttributes().get(i+1).getName();
	                	 String parTypeValue = parXMI.getAttributes().get(i+1).getValue();      	 
	                	 
	                	 
	                	 
	                	 model.Element parName = new model.Element(this.getEltId(),"String","ParameterName", parNameValue);
	                	 model.Element parType = new model.Element(this.getEltId(),"String","ParameterType", parTypeValue);
	                	 
	                	 parameter.addSubElement(parName);
	                	 parameter.addSubElement(parType);
	                	 
	                	 this.resultingModel.addElement(parName);
	                	 this.resultingModel.addElement(parType);
                 	}	 
                	 
                 }//while(par.hasNext()){	   --------------PARAMETER 
            	 
            	 
            	 
            	 
            	 
             }//while(o.hasNext()){	------------------------OPERATION 
        	
             
        	 
        	
         
         
         
         
         
         }//while(c.hasNext()){
         
         //On affiche le nom de l’élément courant
         //System.out.println(courant.getChild("attributes").getText());
      
      
      
      //}//while(p.hasNext()){
   
     
   
   }
   
   /*public static void main(String[] args) {
	   
   }*/
   public HMIParser(String path){
	   
	   	File f = new File(path);
		this.modelName = f.getName();
		this.modelName = MyString.readUntil(this.modelName, '.');
		//System.out.println(modelName);
	   
	   SAXBuilder sxb = new SAXBuilder();
	      try{
	         //On crée un nouveau document JDOM avec en argument le fichier XML
	         //Le parsing est terminé ;)
	       
	    	  this.document = sxb.build(new File(path));
	    	  //On initialise un nouvel élément racine avec l'élément racine du document.
	    	  this.racine = document.getRootElement();

	      //Méthode définie dans la partie 3.2. de cet article
	      
			parse();
		} catch (DataConversionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

 
   private static void addElement(model.Element elt, org.jdom2.Element element){
	
	   
	   
	   
	   Element XMIElt = new Element(elt.getRole());
	   element.addContent(XMIElt);
	   
	   //System.out.println(elt.getType()+"   >>>>>>>>>>>>>>>>>>>>>   "+elt.getId());
	   Attribute id = new Attribute("id",elt.getId());
	   
	   XMIElt.setAttribute(id);
	   
	   
	   
	   Attribute type = new Attribute("type",elt.getType());
	   XMIElt.setAttribute(type);
	   
	   
	   
	   if(elt.getOrder()!=-1){  Attribute order = new Attribute("order",""+elt.getOrder());
	   XMIElt.setAttribute(order);  }
	   
	   if(elt.getValue()!=""){  Attribute value = new Attribute("value",elt.getValue());
	   XMIElt.setAttribute(value);  }
	   
	   
	   
	   for(int i = 0; i < elt.getSubElements().size(); i++ ){
		   addElement(elt.getSubElements().get(i), XMIElt); 
		}
	   
	   
   }
   
   
   public static org.jdom2.Document fromModelToXMI(Model model) {
   
	    
	   
      //Nous allons commencer notre arborescence en créant la racine XML
      //qui sera ici "personnes".
      Element racine = new Element("xmi");
      racine.setAttribute(new Attribute("id",model.getId()));
      
      //On crée un nouveau Document JDOM basé sur la racine que l'on vient de créer
      org.jdom2.Document document = new Document(racine);
      //org.jdom2.Document document = new Document();

      
      //for(int i = 0; i < 1; i++ ){
      for(int i = 0; i < model.getElements().size(); i++ ){
			if(model.getElements().get(i).getLevel()==1){
				model.Element elt = model.getElements().get(i);
				addElement(elt, racine);
			}
		}
      
          
     //document.addContent(racine);
      
      //Element etl1 = new Element("preseonnes");
      //Element etl2 = new Element("preseonnes");
      
      //racine.addContent(etl1);
      //racine.addContent(etl2);
      
      
      
         //On crée un nouvel Element etudiant et on l'ajoute
         //en tant qu'Element de racine
         //Element etudiant = new Element("etudiant");
        //racine.addContent(etudiant);

         //On crée un nouvel Attribut classe et on l'ajoute à etudiant
        //grâce à la méthode setAttribute
        // Attribute classe = new Attribute("classe","P2");
         //etudiant.setAttribute(classe);

         //On crée un nouvel Element nom, on lui assigne du texte
         //et on l'ajoute en tant qu'Element de etudiant
        // Element nom = new Element("nom");
        // nom.setText("CynO");
        // etudiant.addContent(nom);

         return document;
         //save(document, "Exercice1.xml");
      
   }
 
   
   
   
   public static void save(org.jdom2.Document document, String fichier){
      try{
         //On utilise ici un affichage classique avec getPrettyFormat()
         XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
         //Remarquez qu'il suffit simplement de créer une instance de FileOutputStream
         //avec en argument le nom du fichier pour effectuer la sérialisation.
         sortie.output(document, new FileOutputStream(fichier));
      }
      catch (java.io.IOException e){}
   }
//------------------------------------------------------------------------------
	
	public static void main(String[] args) {
    
	
	
	}   
}