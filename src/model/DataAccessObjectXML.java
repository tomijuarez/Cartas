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
    private static  final String LEAGUES_PATH = "resources/data/leagues/";
    private static final String ATTRIBUTES_PATH = "resources/data/";
    private static final String NAME_FILE_ATTRIBUTTES = "ListAttributes";

    private Hashtable<String,AbstractCharacter> all = new Hashtable<>();
    private int charactersCount = 0;
    private boolean charactersLoaded = false;
    private boolean leaguesLoaded = false;

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
                this.charactersCount++;
                aux.setId(i);
                characters.put(String.valueOf(i), aux);
                all.put(String.valueOf(i), aux);
            }
        }

        this.charactersLoaded = true;

        return characters;
    }

    @Override
    public Hashtable<String, League> getLeagues(Hashtable<String,Character> characters) {
        Hashtable<String, League> leagues = new Hashtable<>();
        if(this.charactersLoaded){
            for(int i=1; i <= this.dpFile.numberFiles(this.LEAGUES_PATH) ; i++) {
                LeagueSave aux = (LeagueSave) this.dpFile.getData(this.LEAGUES_PATH, String.valueOf(i));
                if (aux != null) {
                    League newLeague = new League(aux.getFictitiousName());
                    for(String s : aux.getIdCharacters()){
                        newLeague.addCharacter(characters.get(s));
                    }
                    newLeague.setId(i+charactersCount);
                    leagues.put(String.valueOf(i+charactersCount),newLeague);
                    all.put(String.valueOf(i+charactersCount), newLeague);
                }
            }
            this.leaguesLoaded = true;
            return leagues;
        }else{
            System.out.print("Primero cargar los Personajes");
        }
            return null;
    }

    @Override
    public Hashtable<String, AbstractCharacter> getAll() {
        if(this.charactersLoaded && this.leaguesLoaded){
            return this.all;
        }else{
            System.out.print("Primero cargar los Personajes y las Ligas");
            return null;
        }
    }


    @Override
    public Hashtable<String, Card> getCards(Hashtable<String, AbstractCharacter> characters) {

        Hashtable<String, Card> cards = new Hashtable<>();

        for(int i=1; i <= this.dpFile.numberFiles(this.CARDS_PATH) ; i++){
            CardSave auxCard = (CardSave) this.dpFile.getData(this.CARDS_PATH, String.valueOf(i));
            if(auxCard != null) {
                AbstractCharacter aux = characters.get(auxCard.getIdCharacter());
                Card newCard = new Card(aux);
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
    public void saveData(Hashtable<String, Character> characters,Hashtable<String, League> leagues, List<String> attributes, List<Deck> decks, Hashtable<String, Card> cards) {
        charactersCount = 0;
        saveAttributes(attributes);
        saveCharacters(characters);
        saveLeagues(leagues);
        saveCards(cards);
        saveDecks(decks);

    }

    @Override
    public void saveAttributes(List<String> attributes) {
        /**Guardar Lista de atributos**/
        this.dpFile.saveData(this.ATTRIBUTES_PATH,this.NAME_FILE_ATTRIBUTTES,attributes);
    }

    @Override
    public void saveCharacters(Hashtable<String, Character> characters) {
        /**Guardar Personajes**/

        int id = 1;
        for(Character character : characters.values()){
            character.setId(id);
            this.dpFile.saveData(this.CHARACTER_PATH,String.valueOf(id),character);
            charactersCount++;
            id++;
        }
    }

    @Override
    public void saveLeagues(Hashtable<String, League> leagues) {
        int id = 1;
        for(League league : leagues.values()){
            List<String> characters = new ArrayList<String>();
            for(Character c : league.getCharacters()){
                characters.add(String.valueOf(c.getId()));
            }
            league.setId(charactersCount+id);
            LeagueSave leagueSave = new LeagueSave(league.getFictitiousName(),characters);
            this.dpFile.saveData(this.LEAGUES_PATH,String.valueOf(id),leagueSave);
            id++;
        }
    }

    @Override
    public void saveCards(Hashtable<String, Card> cards) {
        /**Guardar Cards**/
        for (int i = 1; i <= cards.size(); i++) {
            Card m = cards.get(String.valueOf(i));
            m.setId(i);
            CardSave cSave = new CardSave(m.getAttributes(),String.valueOf(m.getCharacter().getId()));
            this.dpFile.saveData(this.CARDS_PATH, String.valueOf(i),cSave);
        }
    }

    @Override
    public void saveDecks(List<Deck> decks) {
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
