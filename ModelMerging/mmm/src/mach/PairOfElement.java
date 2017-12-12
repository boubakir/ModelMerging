package mach;

import model.Element;
/**
 * represents a pair of elements
 * @author Boubakir
 *
 */
public class PairOfElement {


	private Element first;
	private Element second;
	private float simDeg;
//Constructors---------------------------------------------------------
	public PairOfElement(Element first, Element second, float simDeg) {
		super();
		this.first = first;
		this.second = second;
		this.simDeg = simDeg;
	}
	
	public PairOfElement(Element first, Element second) {
		this(first, second, -1);
	}

//Setters Guetters---------------------------------------------------------------	
	
	public Element getFirst() {
		return first;
	}

	public void setFirst(Element first) {
		this.first = first;
	}

	public Element getSecond() {
		return second;
	}

	public void setSecond(Element second) {
		this.second = second;
	}

	public float getSimDeg() {
		return simDeg;
	}

	public void setSimDeg(float simDeg) {
		this.simDeg = simDeg;
	}

//------------------------------------------------------------------------------
	
	public void disp(){
		System.out.println("pair : " + this.first.getId()+"  "+this.second.getId()+ "  simDeg = "+this.simDeg);
	}
	
	public void disp2(){
		System.out.println("pair : (" +this.first.getId()+"  "+ this.first.getValue()+"), ("+this.second.getId()+"  "+this.second.getValue()+ ")  simDeg = "+this.simDeg);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
