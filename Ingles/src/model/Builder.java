package model;

import java.util.Hashtable;
import java.util.List;


public interface Builder {

	public Hashtable<String, Card> getCards();
	public List<Deck> getDecks(Hashtable<String, Card> cards);

}
