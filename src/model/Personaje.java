package model;

import java.io.IOException;
import java.util.*;

import java.nio.file.*;

public class Personaje extends PersonajeAbstracto {
	private String nombreReal;
	private Map<String,Double> atributos = new Hashtable<>();
	
	public Personaje(String ficticio,String real,String imagePath){
		super.setNombreFicticio(ficticio);
		this.nombreReal = real;
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

}
