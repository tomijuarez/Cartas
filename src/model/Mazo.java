package model;

import java.util.*;

public class Mazo {

	protected String nombre;
	protected List<Carta> cartas;
	private Hashtable<String,Boolean> atributos;
	
	public Mazo(String n){
		this.setNombre(n);
		this.atributos  = new Hashtable<>();
		this.cartas = new ArrayList<>();
	}
	
	public void barajar(int n){
		/*abarajo n veces*/
		for(int i=0; i < n; i++){
			int posS = (int)Math.random()*100;
			int posP = (int)Math.random()*100;
			this.cartas.add(posP, this.cartas.remove(posS));
		}
	}
	
	public int getCantCartas(){
		
		return this.cartas.size();
	}
	
	public List<String> getAtributos(){
		List<String> atrib = new ArrayList<>();
		Enumeration<String> e = this.atributos.keys();
		Object clave;
		while( e.hasMoreElements() ){
		  clave = e.nextElement();
		  atrib.add((String)clave);
		  
		}
		return atrib;
	}
	
	public Hashtable<String,Boolean> getAtrib(){
		Hashtable<String,Boolean> atrib = this.atributos;
		return atrib;
	}
	
	public void agregarAtributo(String a, Boolean b){
		this.atributos.put(a,b);
	}
	
	public void setAtributo(Hashtable<String,Boolean> a){
		this.atributos = a;
	}
	public boolean getTipoComparacion(String atrib){
		return (boolean)this.atributos.get(atrib);
	}
	
	public List<Carta> getCartas(){
		List<Carta> c = this.cartas;
		return c;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public void agregarCarta(Carta c){
		this.cartas.add(c);
	}
}
