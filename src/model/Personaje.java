package model;

import java.util.*;

public class Personaje extends PersonajeAbstracto {
	private String nombreReal;
	private Hashtable<String,Double> atributos;
	
	public Personaje(String real,String ficticio){
		this.setNombreReal(real);
		super.setNombreFicticio(ficticio);
		this.atributos = new Hashtable<String,Double>();
		
	}
	
	public void agregarAtributo(String nombre,double atrib){
		Double valor = new Double(atrib);
		this.atributos.put(nombre,valor); 
	}
	
	public double getAtributo(String atrib) {
		
		if(this.atributos.containsKey(atrib)){
			return (double)this.atributos.get(atrib);
		}
		else{
			return 0.0;
		}
	}
	
	public String getNombreReal() {
		return nombreReal;
	}
	
	public void setNombreReal(String nombreReal) {
		this.nombreReal = nombreReal;
	}
	
}
