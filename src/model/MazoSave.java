package model;

import java.util.Hashtable;
import java.util.List;


public class MazoSave {
	private String nombre;
	private List<String> ids;
	private Hashtable<String,Boolean> atributos;
	
	public MazoSave(String n,List<String> i, Hashtable<String,Boolean> a){
		this.ids = i;
		this.atributos = a;
		this.nombre = n;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<String> getIds() {
		List<String> id = this.ids;
		return id;
	}

	public void setIds(List<String> ids) {
		this.ids = ids;
	}

	public Hashtable<String, Boolean> getAtributos() {
		Hashtable<String, Boolean> a = this.atributos;
		return a;
	}

	public void setAtributos(Hashtable<String, Boolean> atributos) {
		this.atributos = atributos;
	}
	
	
}
