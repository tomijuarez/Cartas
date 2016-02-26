package model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.thoughtworks.xstream.XStream;


public class HeroesBuilder implements Builder {
	//serializador de datos
	private static final String CARDS_PATH = "resources/data/cards/";
	private static final String DECKS_PATH = "resources/data/decks/";
	private DataParser dpFile;
	
	public HeroesBuilder(){
		this.dpFile = new XMLDataParser(new XStream());
	}
	@Override
	public Hashtable<String, Card> getCards() {
		// TODO Auto-generated method stub
		
		Hashtable<String, Card> cards = new Hashtable<>();
		
		for(int i=1; i <= this.dpFile.numberFiles(this.CARDS_PATH) ; i++){
			cards.put(String.valueOf(i),(Card) this.dpFile.getData(this.CARDS_PATH,String.valueOf(i)));
		}
		
		return cards;
	}
		

	@Override
	public List<Deck> getDecks(Hashtable<String,Card> cards) {
		List<Deck> decks = new ArrayList<>();
		
		/**Cargado de Mazos**/
		Object obj = this.dpFile.getData(this.DECKS_PATH,"nombresMazos");

		if(obj != null){
			List<String> list = (List<String>) obj;

			for (String n : list) {
				Object Odeck = this.dpFile.getData(this.DECKS_PATH, n);
				DeckSave m = (DeckSave) Odeck;
				decks.add(this.getDeck(m, cards));

			}
		}
		return decks;
	}
	
	private Deck getDeck(DeckSave m, Hashtable<String,Card> cards){
		
		Deck deck = new Deck(m.getName());
		
		for(String id : m.getIds()){
			deck.addCard(cards.get(id));
		}
		
		deck.setAttribute(m.getAttributes());
		return deck;
	}
	
}
