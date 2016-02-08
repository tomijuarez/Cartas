package model;

import java.util.Hashtable;
import java.util.List;


public class InstanceDirector {
	private HeroesBuilder builder;
	
	public InstanceDirector(HeroesBuilder b){
		this.builder = b;
	}
	
	public List<Mazo> getMazos(Hashtable<String, Carta> c){
		return this.builder.getMazos(c);
	}
	
	public Hashtable<String,Carta> getCartas(){
		return this.builder.getCartas();
	}
	
}
