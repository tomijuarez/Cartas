package model;

import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Gandalf on 26/2/2016.
 */
public interface DataAccessObject {

    /**Cargar**/
    public Hashtable<String, Card> getCards();
    public List<MainDeck> getDecks();
    public List<String> getAttributes();
    public Hashtable<String,Character> getCharacters();
    public LinkedHashMap<String,League> getLeagues();
    public Hashtable<String,AbstractCharacter> getAll();


    /**Guardar**/
    public void saveData(Hashtable<String, Character> characters,LinkedHashMap<String, League> leagues, List<String> attributes, List<MainDeck> decks, Hashtable<String, Card> cards);




}
