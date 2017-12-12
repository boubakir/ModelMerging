package model;

import java.util.ArrayList;


/**
 * represents a model element
 * @author Boubakir
 *
 */
public class Element {
	
	private String id;
	private String type;
	private String role;
	private String value;
	private int order = -1;
	private ArrayList<Element> subElements;
	
	
	//these two attribute represents orginal elemnts
	//used to create the curent element using merging
	private Element original1;
	private Element original2;
	
	private ArrayList<Element> originals;
	
//Constructeur -------------------------------------------------------	
	/**
	 * Constructeur pour les types compund
	 * @param id
	 * @param type
	 * @param role
	 */
	public Element(String id, String type, String role) {
		this.id = id;
		this.type = type;
		this.role = role;
		this.value = "";
		this.subElements = new ArrayList<Element>();
		this.originals = new ArrayList<Element>();
		this.originals.add(this);
	}
	
	public Element(String id, String type, String role, String value) {
		this(id, type, role);
		this.value = value;	
	}

	public Element(String id, String type, String role, String value, int order) {
		this(id, type, role, value);
		this.order = order;	
	}
	

	

	public Element(Element original1, Element original2) {
		this.original1 = original1;
		this.original2 = original2;
		
		this.id = this.original1.id+"-"+this.original2.id;
		this.type = this.original1.type;
		this.role = this.original1.role; 
		this.order = this.original1.order;
		this.value = this.original1.value;
		
		
		
		
		this.subElements = new ArrayList<Element>();
		
		this.originals = new ArrayList<Element>();
		this.originals.addAll(original1.originals);
		this.originals.addAll(original2.originals);
		
	}

	
	public Element(Element original) {
		this.original1 = original;
		this.original2 = original;
		
		this.id = original.id;
		this.type = original.type;
		this.role = original.role; 
		this.order = original.order;
		this.value = original.value;
		
		
		this.subElements = new ArrayList<Element>();
		
		this.originals = new ArrayList<Element>();
		this.originals.addAll(original.originals);
		
	}
	
	
//Getters and Setters -------------------------------------------------------	
	
	public Element getOriginal1() {
		return original1;
	}

	public Element getOriginal2() {
		return original2;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public ArrayList<Element> getSubElements() {
		return subElements;
	}

	public void addSubElement(Element subElement) {

		this.subElements.add(subElement);
	}
	
	public void setSubElements(ArrayList<Element> subElements) {
		this.subElements = subElements;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
	
	
	
	
//Methodes -------------------------------------------------------
	


	public ArrayList<Element> getOriginals() {
		return originals;
	}

	/**
	 * allows to display the element
	 * @param tab: Tabulation
	 */
	public void dispAll(String tab){
		System.out.println(tab+"id = "+this.id);
		System.out.println(tab+"type = "+this.type);
		System.out.println(tab+"role = "+this.role);
		if(this.value!=null) System.out.println(tab+"value = "+this.value);
		System.out.println();
		
		for(int i = 0; i < this.subElements.size(); i++ ){
			this.subElements.get(i).dispAll(tab+tab);
		}
		
	}
	

/**
 * allows to display the element
 * @param tab: Tabulation
 */
	public void dispAll2(String tab){
		if(this.original1==null){
			System.out.println(tab+"id = "+this.id);
			System.out.println(tab+"type = "+this.type);
			System.out.println(tab+"role = "+this.role);
			System.out.println(tab+"value = "+this.value);
			System.out.println();
		}else{
			System.out.println(tab+"id = "+this.original1.id+" "+this.original2.id);
			System.out.println(tab+"type = "+this.original1.type+" "+this.original2.type);
			System.out.println(tab+"role = "+this.original1.role+" "+this.original2.role);
			System.out.println(tab+"value = "+this.original1.value+" "+this.original2.value);
			System.out.println();
		}
		
		
		
		for(int i = 0; i < this.subElements.size(); i++ ){
			this.subElements.get(i).dispAll2(tab+tab);
		}
		
	}
	/**
	 * allows to display the element
	 * @param tab: Tabulation
	 */
	public void dispAll3(String tab){
		if(this.original1==null){
			System.out.println(tab+"id = "+this.id);
			System.out.println(tab+"type = "+this.type);
			System.out.println(tab+"role = "+this.role);
			System.out.println(tab+"value = "+this.value);
			System.out.println();
		}else{
			System.out.println(tab+"id = "+this.id+"**"+this.original1.id+" "+this.original2.id);
			System.out.println(tab+"type = "+this.original1.type+" "+this.original2.type);
			System.out.println(tab+"role = "+this.original1.role+" "+this.original2.role);
			System.out.println(tab+"value = "+this.original1.value+" "+this.original2.value);
			System.out.println();
		}
		
		
		
		for(int i = 0; i < this.subElements.size(); i++ ){
			this.subElements.get(i).dispAll3(tab+tab);
		}
		
	}
	
	
	/**
	 * allows to display the element
	 * @param tab: Tabulation
	 */
	public void disp(String tab){
		System.out.println(tab+"id = "+this.id);
		System.out.println(tab+"type = "+this.type);
		System.out.println(tab+"role = "+this.role);
		if(this.value!=null) System.out.println(tab+"value = "+this.value);
		System.out.println();

		
	}
	
/**
 * The level of the element in the tree that represents the structure of the model
 * @return
 */
public int getLevel(){
		
		if(this.getRole().equals("Class")){
			return 1;
		}
		if(this.getRole().equals("ClassAttribute")
				||this.getRole().equals("ClassOperation")
				||this.getRole().equals("ClassName")
				){
			return 2;
		}
		
		if(this.getRole().equals("AttributeName")
				||this.getRole().equals("AttributeType")
				||this.getRole().equals("OperationName")
				||this.getRole().equals("OperationType")
				||this.getRole().equals("Parameter")
				
				){
			return 3;
		}
		
		if(this.getRole().equals("ParameterName")
				||this.getRole().equals("ParameterType")
				){
			return 4;
		}
		
		return 0;
		
	}

	/**
	 * check whether the element is atomic
	 * @return
	 */
	public boolean isAtomic(){
		
		return this.type.equals("String")||this.type.equals("Reference");
		
	}

	public int getCategory(){
		return Element.getCategory(this.role);
	}

	
	public static int getCategory(String role){

		if(role.equals("Parameter")){
			return 2;
		}
		if(role.equals("ClassName")
				||role.equals("AttributeName")
				||role.equals("AttributeType")
				||role.equals("OperationName")
				||role.equals("OperationType")
				||role.equals("ParameterName")
				||role.equals("ParameterType")){
			return 3;
		}
		return 1;
	}
	
	
		
	
	/*
	public static float getThreshold(String role){
		
		return 0.25f;
		//return 0;

	
		
	}*/
	
public void removeSubElements(String role) {
		
		ArrayList<Element> liste = new ArrayList<Element>();
	
		for(int i = 0; i < this.subElements.size(); i++){
			
			
			if(this.subElements.get(i).getRole().equals(role)){
			}else{
				liste.add(this.subElements.get(i));
			}
		}
		this.subElements = liste;
		
		for(int i = 0; i < this.subElements.size(); i++){
			
			
			this.subElements.get(i).removeSubElements(role); 
		}
	
	}
	
	
	
	public static Element merge(Element e1, Element e2){
		
		return new Element(e1.getId()+e2.getId(), e1.getType()+e2.getType(), e1.getRole()+e2.getRole(), e1.getValue()+e1.getValue());
		
		
	}
	
	/**
	 * check whether the element is the parent of an other one
	 * @param elt
	 * @return
	 */
	public boolean isParent(Element elt){
		
		
	
		
		boolean res = false;
		
		int i = 0;
		while(i<this.subElements.size() && !res){
			if(elt==this.subElements.get(i)){		
				res = true;		
			}
			i++;
		}//while(){
		
		return res;
	}
	
	/**
	 * Gets all sub elements which have a giving role
	 * @param role
	 * @return
	 */
	public ArrayList<Element> getSubElements(String role){
		
		ArrayList<Element> res = new ArrayList<Element>();
		
		for(int i = 0; i < this.subElements.size(); i++){
			if(this.subElements.get(i).getRole().equals(role)){
				res.add(this.subElements.get(i));
			}
		}
		
		
		return res;
	}
	
	
	public static void main(String[] args) {
		
	}

	
}
