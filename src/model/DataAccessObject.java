package model;

import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Gandalf on 26/2/2016.
 */
public abstract class DataAccessObject {

    /**Cargar**/
    public abstract Hashtable<String, Card> getCards(Hashtable<String, AbstractCharacter> character);
    public abstract List<Deck> getDecks(Hashtable<String, Card> cards);
    public abstract List<String> getAttributes();
    public abstract Hashtable<String,Character> getCharacters();
    public abstract LinkedHashMap<String,League> getLeagues(Hashtable<String,Character> characters);
    public abstract Hashtable<String,AbstractCharacter> getAll();


    /**Guardar**/
    public abstract  void saveData(Hashtable<String, Character> characters,LinkedHashMap<String, League> leagues, List<String> attributes, List<Deck> decks, Hashtable<String, Card> cards);
    public abstract void saveAttributes(List<String> attributes);
    public abstract void saveCharacters(Hashtable<String, Character> characters);
    public abstract void saveLeagues(LinkedHashMap<String,League> leagues);
    public abstract void saveCards(Hashtable<String, Card> cards);
    public abstract void saveDecks(List<Deck> decks);




}
