package model;

import java.util.Hashtable;
import java.util.List;


public class InstanceDirector {
	private HeroesBuilder builder;
	
	public InstanceDirector(HeroesBuilder b){
		this.builder = b;
	}
	
	public List<Deck> getDecks(Hashtable<String, Card> c){
		return this.builder.getDecks(c);
	}
	
	public Hashtable<String,Card> getCards(){
		return this.builder.getCards();
	}
	
}
