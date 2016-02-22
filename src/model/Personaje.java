package model;

import java.io.IOException;
import java.util.*;

import java.nio.file.*;

public class Personaje extends PersonajeAbstracto {
	private String nombreReal;
	private Hashtable<String,Double> atributos;
	
	public Personaje(String ficticio,String real,String imagePath){
		this.setNombreReal(real);
		super.setNombreFicticio(ficticio);
		this.atributos = new Hashtable<String, Double>();
		Path pathOrigin = Paths.get(imagePath);
		Path pathTarget = Paths.get(super.getImagePath());
		try {
			Files.copy( pathOrigin, pathTarget,StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			System.out.printf("Error al guardar Imagen");
		}
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
