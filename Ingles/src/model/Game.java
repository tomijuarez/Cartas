package model;

import com.thoughtworks.xstream.XStream;
import controller.events.*;

import java.util.*;

/**
 * Created by Guillermo on 25/2/2016.
 */
public class Game extends Observable {

    private static final String CARDS_PATH = "resources/data/cards/";
    private static final String DECKS_PATH = "resources/data/decks/";
    private static final int CARDS_LIMIT = 0;
    private static final int CANT_MIN_PLAYERS = 2;

    private Boolean deadHead;
    private XStream xstream;
    private Queue<Player> turns;
    private Deck deck;
    private List<Player> players = new Vector<>();
    private Player winner;
    private List<Player> deadHeadList;
    private List<Player> losers;
    private List<Deck> decks;
    private Hashtable<String, Card> cards;
    private Confrontation confrontation;                                                                   /*****/
    private InstanceDirector director;

    private static final String cardsDirectory = "";
    private static final String decksDirecory = "";

    private String currentAttribute;
    private DeckPlayer currentAccumulatorDeck;

    //serializador de datos
    private DataParser dpFile;

    public List<Player> getPlayers() {
        return this.players;
    }


    public void createDeck(List<Card> cards, String name, List<Map.Entry<String,Boolean> >attributes){
        Deck newDeck = new MainDeck(name);
        for(model.Card c : cards){
            newDeck.addCard(c);
        }
        for(Map.Entry<String,Boolean> p : attributes){
            newDeck.addAttribute(p.getKey(),p.getValue());
        }
        this.decks.add(newDeck);
    }

    private void checkPlayers() {
        for (int i = 0; i < this.players.size(); i++) {
            if (this.players.get(i).numberCards() == this.CARDS_LIMIT) {
                // notifico el jugador que perdio, y se lo envio al observador
                this.losers.add(this.players.remove(i));// lo agrego a la cola de perdedores
                this.turns.remove(this.players.get(i));//elimino al perdedor de la lista de turnos
            }
        }
    }

    /**
     * Devuelve el jugador con el turno actual.
     *
     * @return
     */
    public Player currentPlayer() {
        return this.turns.remove();
    }

    public Player getRoundWinner(List<Player> gamePlayers, String attrib) {
        return this.confrontation.getWinnerRound(gamePlayers, this.deadHeadList, attrib);             /********/
    }

    public List<Player> getDeadHeadList() {
        return this.deadHeadList;
    }

    public List<Player> getLosers() {return this.losers;
    }

    private Player tieBreakRound(String attrib, DeckPlayer gameDeck) {
        this.handleCardsSelection(this.deadHeadList);
        this.winner = this.getRoundWinner(this.deadHeadList, attrib);//obtengo lso ganadores del la ronda desempate

        //El juego sigue en empate
        if (this.winner == null) {
            return this.tieBreakRound(attrib, gameDeck);
        }

        return this.winner;

    }

    /**
     * Métodos de eventos.
     */
    private void handleDeckList(List<Deck> decks) {
        setChanged();
        this.notifyObservers(new ListDecks(decks));
    }

    private void handleCardsList(List<Card> cards) {
        setChanged();
        this.notifyObservers(new ListCards(cards));
    }


    private void handleShiftTurn(Player currentPlayer) {
        setChanged();
        this.notifyObservers(new ShiftTurn(currentPlayer));// me devuelve el jugador con el atributo que selecciono en la VISTA...metodos: elegirAtributo() y obtenerAtributo(),
    }

    private void handleCardsSelection(List<Player> players) {
        setChanged();
        this.notifyObservers(new CardsSelection(players));
    }

    private void handleDeadHeatRound() {
        setChanged();
        this.notifyObservers(new DeadHeatRound());
    }

    private void handleWinRound(Player winner, DeckPlayer accumulator) {
        setChanged();
        this.notifyObservers(new WinRound(winner, accumulator));
    }

    /**
     * Recepción de eventos
     */

    public void receiveSelectedAttribute(String attr) {
        this.currentAttribute = attr;
    }

    public void receiveAccumulatorDeck(DeckPlayer accumulator) {
        this.currentAccumulatorDeck = accumulator;
    }

    /**
     * Método de comienzo de partida.
     */
    public void startGame() {

        while (this.turns.size() >= CANT_MIN_PLAYERS) {
            this.handleShiftTurn(this.currentPlayer());
            // notifico el comienzo de la seleccion de cartas de cada uno de los jugadores en juego
            this.handleCardsSelection(this.players);

            this.winner = this.getRoundWinner(this.players, this.currentAttribute);

            //Verifico si la ronda esta empatada.
            if (this.winner == null) {
                // notifico a la VISTA que se produjo un empate
                this.handleDeadHeatRound();
                this.winner = this.tieBreakRound(this.currentAttribute, this.currentAccumulatorDeck);
            }

            //Notifico quién es el ganador de la ronda.
            this.handleWinRound(this.winner, this.currentAccumulatorDeck);

            //Reintegro el jugador actual al final de la cola de turnos.
            this.turns.add(this.currentPlayer());

            //actualizo seteo cada una de las estructuras involucradas en la partida
            this.deadHeadList.clear();
            this.winner = null;
            this.currentAccumulatorDeck.clear();

            this.checkPlayers();//verifico el estado de cada uno de los jugadores
            //actualizo VISTA con informacion de la partida
            //notifico al la VISTA de la actualizacion
        }
    }

    public List<Card> getCards() {
        return new Vector<Card>(this.cards.values());
    }

    public List<Deck> getDecks() {
        return this.decks;
    }

    public Game() {

        /**Creo cartas y mazos**/
        this.director = new model.InstanceDirector(new model.HeroesBuilder());
        this.cards = this.director.getCards();
        this.decks = this.director.getDecks(this.cards);



        /**Guardar Datos**/
        this.saveDate();

    }

    /**Guardar Masos**/
    public void saveDate(){

        //**serializador**/
        this.dpFile = new XMLDataParser(new XStream());

        /**Guardar Cartas**/
        for(int i = 1; i <= this.cards.size(); i++){
            Card m = this.cards.get(String.valueOf(i));
            m.setId(i);
            this.dpFile.saveData(this.CARDS_PATH,String.valueOf(i),m);
        }


        /**Guardar Masos**/
        List<String> names = new ArrayList<>();
        for(Deck m : this.decks){
            names.add(m.getName());
            this.dpFile.saveData(this.DECKS_PATH,m.getName(),new DeckSave(m.getName(),this.getIds(m),m.getAtrib()));
        }

        /**Guardo listado de nombre de mazos**/
        this.dpFile.saveData(this.DECKS_PATH,"nombresMazos",names);


    }


    private List<String> getIds(Deck m){
        List<String> ids = new ArrayList<String>();
        List<Card> cards = m.getCards();

        for(Card c : cards)
        {
            String id = String.valueOf(c.getId());
            ids.add(id);
        }

        return ids;
    }


    public void addPlayer(Player player) {
        this.players.add(player);
        this.turns.add(player);
    }

    public void createPlayers(List<String> playerNames, List<Boolean> managedManually, Strategy selectedStrategy, Deck deck) {
        this.deck = deck;

        for (int i = 0; i < playerNames.size(); i++) {

            Strategy strategy = (managedManually.get(i))
                    ? new ManualStrategy()
                    : selectedStrategy;


            this.addPlayer(
                    new Player(
                            strategy,
                            playerNames.get(i),
                            new DeckPlayer()
                    )
            );
        }
    }




}
