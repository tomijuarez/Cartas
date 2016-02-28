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
    public Hashtable<String, Character> getCharacters() {
        Hashtable<String, Character> characters = new Hashtable<>();

        for(int i=1; i <= this.dpFile.numberFiles(this.CHARACTER_PATH) ; i++){
            Character aux = (Character) this.dpFile.getData(this.CHARACTER_PATH,String.valueOf(i));
            if(aux != null) {
                characters.put(String.valueOf(i), aux);
            }
        }

        return characters;
    }


    @Override
    public Hashtable<String, Card> getCards(Hashtable<String, Character> characters) {

        Hashtable<String, Card> cards = new Hashtable<>();

        for(int i=1; i <= this.dpFile.numberFiles(this.CHARACTER_PATH) ; i++){
            CardSave auxCard = (CardSave) this.dpFile.getData(this.CARDS_PATH, String.valueOf(i));
            if(auxCard != null) {
                Card newCard = new Card(characters.get(String.valueOf(i)));
                for(String c : auxCard.getAttributes()){
                    newCard.addAttribute(c);
                }
                cards.put(String.valueOf(i),newCard);
            }
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
                DeckSave auxDeck = (DeckSave) this.dpFile.getData(this.DECKS_PATH, n);
                if(obj!=null){
                    Deck newDeck = new Deck(auxDeck.getName());
                    for(String id : auxDeck.getIds()){
                        newDeck.addCard(cards.get(id));
                    }
                    newDeck.setAttribute(auxDeck.getAttributes());
                    decks.add(newDeck);

                }

            }
        }
        return decks;
    }

    @Override
    public List<String> getAttributes() {
        return (List)this.dpFile.getData(this.ATTRIBUTES_PATH,this.NAME_FILE_ATTRIBUTTES);
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
