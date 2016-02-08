package model;

import model.PersonajeAbstracto;

import java.util.*;
import java.util.Observable;

public class Carta extends Observable {
	
    static int contador = 0;
    private int id ;
    private PersonajeAbstracto personaje;
	private List<String> atributos;
	
	public Carta(PersonajeAbstracto pers){
		contador += 1;
		setId(contador);
		this.personaje = pers;
		this.atributos = new ArrayList<>();	
	}
	
	public int getId() {
		return id;
	}

	public PersonajeAbstracto getPersonaje() {
		return personaje;
	}

	public void agregarAtributo(String atrib){
		if(this.personaje.getAtributo(atrib) == 0.0){
			System.out.println("El personaje no contiene el atributo solicitado");
			//notifico que el atributo no se encuentra
			this.notifyObservers("no_atributo");
		}
		else{
			this.atributos.add(atrib);
			//notifico que el atributo fue agregado
			this.notifyObservers("atrib_agregado");
		}
	}
	
	public double getAtributo(String atrib){
		if(!this.atributos.contains(atrib)){
			//notifico que el atributo solicitado no pertenece a la carta
			this.notifyObservers("no_atributo_solic");
			return 0;
		}
		else{
			return this.personaje.getAtributo(atrib);
		}
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
