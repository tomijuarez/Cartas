package model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.StringBufferConverter;
import model.XMLDataParser;
/**
 * Created by Gandalf on 26/2/2016.
 */
public class DataAccessObjectXML extends DataAccessObject{

    private static final String CARDS_PATH = "resources/data/cards/";
    private static final String DECKS_PATH = "resources/data/decks/";
    private static  final String CHARACTER_PATH = "resources/data/characters/";
    private static final String ATTRIBUTES_PATH = "resources/data/";
    private static final String NAME_FILE_ATTRIBUTTES = "ListAttributes";

    private XMLDataParser dpFile;

    public DataAccessObjectXML(){
        this.dpFile = new XMLDataParser(new XStream());
    }

    @Override
    public Hashtable<String, Card> getCards(Hashtable<String, Character> character) {

        Hashtable<String, Card> cards = new Hashtable<>();

        for(int i=1; i <= this.dpFile.numberFiles(this.CHARACTER_PATH) ; i++){
            CardSave aux = (CardSave) this.dpFile.getData(this.CARDS_PATH, String.valueOf(i));
            cards.put(String.valueOf(i),new Card(character.get(String.valueOf(i))));
        }
        return cards;
    }

    @Override
    public List<Deck> getDecks(Hashtable<String, Card> cards) {
        List<Deck> decks = new ArrayList<>();

        /**Cargado de Mazos**/
        Object obj = this.dpFile.getData(this.DECKS_PATH,"deckNames");

        if(obj != null){
            List<String> list = (List<String>) obj;

            for (String n : list) {
                Object oDeck = this.dpFile.getData(this.DECKS_PATH, n);
                DeckSave m = (DeckSave) oDeck;
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

    @Override
    public List<String> getAttributes() {
        return (List)this.dpFile.getData(this.ATTRIBUTES_PATH,this.NAME_FILE_ATTRIBUTTES);
    }

    @Override
    public Hashtable<String, Character> getCharacters() {
        Hashtable<String, Character> characters = new Hashtable<>();

        for(int i=1; i <= this.dpFile.numberFiles(this.CHARACTER_PATH) ; i++){
            characters.put(String.valueOf(i),(Character) this.dpFile.getData(this.CHARACTER_PATH,String.valueOf(i)));
        }

        return characters;
    }

    @Override
    public void saveData(Hashtable<String, Character> characters, List<String> attributes, List<Deck> decks, Hashtable<String, Card> cards) {
        /**Guardar Lista de atributos**/
        this.dpFile.saveData(this.ATTRIBUTES_PATH,this.NAME_FILE_ATTRIBUTTES,attributes);

        /**Guardar Personajes**/

        int id = 1;
        for(AbstractCharacter character : characters.values()){
            this.dpFile.saveData(this.CHARACTER_PATH,String.valueOf(id),character);
            id++;
        }

        /**Guardar Cards**/
        for (int i = 1; i <= cards.size(); i++) {
            Card m = cards.get(String.valueOf(i));
            m.setId(i);
            this.dpFile.saveData(this.CARDS_PATH, String.valueOf(i), new CardSave(m.getAttributes(),m.getCharacter().getFictitiousName()));
        }


        /**Guardar Decks**/
        List<String> names = new ArrayList<>();
        for (Deck m : decks) {
            names.add(m.getName());
            this.dpFile.saveData(this.DECKS_PATH, m.getName(), new DeckSave(m.getName(), this.getIds(m), m.getAtrib()));
        }

        /**Guardo listado de nombre de mazos**/
        this.dpFile.saveData(this.DECKS_PATH, "deckNames", names);
    }

    private List<String> getIds(Deck m) {
        List<String> ids = new ArrayList<String>();
        List<Card> cards = m.getCards();

        for (Card c : cards) {
            String id = String.valueOf(c.getId());
            ids.add(id);
        }

        return ids;
    }

}
