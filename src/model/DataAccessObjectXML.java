package model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.io.File;

import com.thoughtworks.xstream.XStream;

/*Implementacion del DAO usando XStream*/
public class DataAccessObjectXML implements DataAccessObject{

    private static DataAccessObjectXML _instance = null;

    private static final String CARDS_PATH = "resources/data/cards/";
    private static final String DECKS_PATH = "resources/data/decks/";
    private static  final String CHARACTER_PATH = "resources/data/characters/";
    private static  final String LEAGUES_PATH = "resources/data/leagues/";
    private static final String ATTRIBUTES_PATH = "resources/data/";
    private static final String NAME_FILE_ATTRIBUTTES = "ListAttributes";

    private Hashtable<String,AbstractCharacter> all = new Hashtable<>();
    private Hashtable<String, Card> cards;
    private int charactersCount = 0;
    private boolean charactersLoaded = false;
    private boolean leaguesLoaded = false;
    private boolean cardsLoaded = false;

    private XMLDataParser dpFile;

    private DataAccessObjectXML(){
        this.dpFile = new XMLDataParser(new XStream());
    }

    public static DataAccessObject getInstance() {
        if (_instance == null)
        _instance = new DataAccessObjectXML();
        return _instance;
    }

    @Override
    public Hashtable<String, Character> getCharacters() {
        Hashtable<String, Character> characters = new Hashtable<>();


        for(int i=1; i <= this.dpFile.numberFiles(DataAccessObjectXML.CHARACTER_PATH) ; i++){
            Character aux = (Character) this.dpFile.getData(DataAccessObjectXML.CHARACTER_PATH,String.valueOf(i));
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
    public LinkedHashMap<String, League> getLeagues() {
        LinkedHashMap<String, League> leagues = new LinkedHashMap<>();
        if(this.charactersLoaded){
            for(int i=1; i <= this.dpFile.numberFiles(DataAccessObjectXML.LEAGUES_PATH) ; i++) {
                LeagueSave aux = (LeagueSave) this.dpFile.getData(DataAccessObjectXML.LEAGUES_PATH, String.valueOf(i));
                if (aux != null) {
                    League newLeague = new League(aux.getFictitiousName());
                    for(String s : aux.getIdCharacters()){
                        newLeague.addCharacter(this.all.get(s));
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
            return null;
        }

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
    public Hashtable<String, Card> getCards() {

        Hashtable<String, Card> cards = new Hashtable<>();

        for(int i=1; i <= this.dpFile.numberFiles(DataAccessObjectXML.CARDS_PATH) ; i++){
            CardSave auxCard = (CardSave) this.dpFile.getData(DataAccessObjectXML.CARDS_PATH, String.valueOf(i));
            if(auxCard != null) {
                AbstractCharacter aux = this.all.get(auxCard.getIdCharacter());
                Card newCard = new Card(aux);
                for(String c : auxCard.getAttributes()){
                    newCard.addAttribute(c);
                }
                cards.put(String.valueOf(i),newCard);
            }
        }
        this.cardsLoaded = true;
        this.cards = cards;
        return cards;
    }

    @Override
    public ArrayList<MainDeck> getDecks() {
        if(this.cardsLoaded) {
            ArrayList<MainDeck> decks = new ArrayList<>();

            /**Cargado de Mazos**/
            Object obj = this.dpFile.getData(DataAccessObjectXML.DECKS_PATH, "deckNames");

            if (obj != null) {
                List<String> list = (List<String>) obj;

                for (String n : list) {
                    DeckSave auxDeck = (DeckSave) this.dpFile.getData(DataAccessObjectXML.DECKS_PATH, n);
                    MainDeck newDeck = new MainDeck(auxDeck.getName());
                    for (String id : auxDeck.getIds()) {
                        newDeck.addCard(cards.get(id));
                    }
                    newDeck.setAttribute(auxDeck.getAttributes());
                    decks.add(newDeck);
                }
            }
            return decks;
        }else{
            System.out.print("Primero cargar las Cartas");
            return null;
        }
    }

    @Override
    public ArrayList<String> getAttributes() {
        return (ArrayList)this.dpFile.getData(DataAccessObjectXML.ATTRIBUTES_PATH,DataAccessObjectXML.NAME_FILE_ATTRIBUTTES);
    }


    @Override
    public void saveData(Hashtable<String, Character> characters, LinkedHashMap<String, League> leagues, ArrayList<String> attributes, ArrayList<MainDeck> decks, Hashtable<String, Card> cards) {
        charactersCount = 0;
        /*Limpiar Directorios*/
        this.cleanDirectory(new File(DataAccessObjectXML.CARDS_PATH));
        this.cleanDirectory(new File(DataAccessObjectXML.CHARACTER_PATH));
        this.cleanDirectory(new File(DataAccessObjectXML.DECKS_PATH));
        this.cleanDirectory(new File(DataAccessObjectXML.LEAGUES_PATH));
        saveAttributes(attributes);
        saveCharacters(characters);
        saveLeagues(leagues);
        saveCards(cards);
        saveDecks(decks);

    }

    private void cleanDirectory(File directory){
        File[] files = directory.listFiles();
        if(files!=null){
            for(File f: files) {
                if(f.isDirectory()) {
                    cleanDirectory(f);
                } else {
                    f.delete();
                }
            }
        }
    }

    private void saveAttributes(ArrayList<String> attributes) {
        /**Guardar Lista de atributos**/
        this.dpFile.saveData(DataAccessObjectXML.ATTRIBUTES_PATH,DataAccessObjectXML.NAME_FILE_ATTRIBUTTES,attributes);
    }

    private void saveCharacters(Hashtable<String, Character> characters) {
        /**Guardar Personajes**/

        int id = 1;
        for(Character character : characters.values()){
            character.setId(id);
            this.dpFile.saveData(DataAccessObjectXML.CHARACTER_PATH,String.valueOf(id),character);
            charactersCount++;
            id++;
        }
    }

    private void saveLeagues(LinkedHashMap<String, League> leagues){
        int id = 1;
        for(League league : leagues.values()){
            List<String> characters = new ArrayList<>();
            for(AbstractCharacter c : league.getCharacters()){
                characters.add(String.valueOf(c.getId()));
            }
            league.setId(charactersCount+id);
            LeagueSave leagueSave = new LeagueSave(league.getFictitiousName(),characters);
            this.dpFile.saveData(DataAccessObjectXML.LEAGUES_PATH,String.valueOf(id),leagueSave);
            id++;
        }
    }

    private void saveCards(Hashtable<String, Card> cards) {
        /**Guardar Cards**/
        for (int i = 1; i <= cards.size(); i++) {
            Card m = cards.get(String.valueOf(i));
            m.setId(i);
            CardSave cSave = new CardSave(m.getAttributes(),String.valueOf(m.getCharacter().getId()));
            this.dpFile.saveData(DataAccessObjectXML.CARDS_PATH, String.valueOf(i),cSave);
        }
    }

    private void saveDecks(List<MainDeck> decks) {
        /**Guardar Decks**/
        List<String> names = new ArrayList<>();
        for (MainDeck m : decks) {
            names.add(m.getName());
            this.dpFile.saveData(DataAccessObjectXML.DECKS_PATH, m.getName(), new DeckSave(m.getName(), this.getIds(m), m.getAtrib()));
        }

        /**Guardo listado de nombre de mazos**/
        this.dpFile.saveData(DataAccessObjectXML.DECKS_PATH, "deckNames", names);
    }

    private List<String> getIds(Deck m) {
        List<String> ids = new ArrayList<>();
        List<Card> cards = m.getCards();

        for (Card c : cards) {
            String id = String.valueOf(c.getId());
            ids.add(id);
        }

        return ids;
    }

}
