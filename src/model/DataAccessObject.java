package model;

import java.util.Hashtable;
import java.util.List;

/**
 * Created by Gandalf on 26/2/2016.
 */
public abstract class DataAccessObject {

    /**Cargar**/
    public abstract  Hashtable<String, Card> getCards(Hashtable<String, Character> character);
    public abstract List<Deck> getDecks(Hashtable<String, Card> cards);
    public abstract  List<String> getAttributes();
    public abstract Hashtable<String,Character> getCharacters();


    /**Guardar**/
    public abstract  void saveData(Hashtable<String, Character> characters, List<String> attributes, List<Deck> decks, Hashtable<String, Card> cards);
}
