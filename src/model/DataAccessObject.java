package model;

import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Gandalf on 26/2/2016.
 */
public interface DataAccessObject {

    /**Cargar**/
    public Hashtable<String, Card> getCards(Hashtable<String, AbstractCharacter> character);
    public List<Deck> getDecks(Hashtable<String, Card> cards);
    public List<String> getAttributes();
    public Hashtable<String,Character> getCharacters();
    public LinkedHashMap<String,League> getLeagues(Hashtable<String,Character> characters);
    public Hashtable<String,AbstractCharacter> getAll();


    /**Guardar**/
    public void saveData(Hashtable<String, Character> characters,LinkedHashMap<String, League> leagues, List<String> attributes, List<Deck> decks, Hashtable<String, Card> cards);
    public void saveAttributes(List<String> attributes);
    public void saveCharacters(Hashtable<String, Character> characters);
    public void saveLeagues(LinkedHashMap<String,League> leagues);
    public void saveCards(Hashtable<String, Card> cards);
    public void saveDecks(List<Deck> decks);




}
