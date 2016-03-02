package model;

import com.thoughtworks.xstream.XStream;
import controller.events.*;

import java.util.*;

/**
 * Created by Guillermo on 25/2/2016.
 */
public class Game extends Observable {

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
    private Confrontation confrontation;

    private Hashtable<String, AbstractCharacter> all;
    private Hashtable<String, Character> characters;
    private Hashtable<String, League> leagues;
    private List<String> attributes;

    /*****/
    private InstanceDirector director;


    private String currentAttribute;
    private DeckPlayer currentAccumulatorDeck;

    //serializador de datos
    private DataAccessObject daoXML = new DataAccessObjectXML();

    public List<Player> getPlayers() {
        return this.players;
    }


    public Game() {

        //this.crearEstPrueba();


        this.characters = this.daoXML.getCharacters();
        this.leagues = this.daoXML.getLeagues(this.characters);
        this.all =this.daoXML.getAll();
        this.cards = this.daoXML.getCards(this.all);
        this.decks = this.daoXML.getDecks(this.cards);
        this.attributes = this.daoXML.getAttributes();

        /**Guardar Datos**/
        //this.daoXML.saveData(this.characters,this.attributes,this.decks,this.cards);

        for(String aux : this.attributes){
            System.out.println(aux);
        }
    }

    /**Operaciones**/
    //public abstract void deleteCharacter();
    //public abstract void deleteCard();
    //public abstract void deleteDeck();

    public void createDeck(List<Card> cards, String name, List<Map.Entry<String, Boolean>> attributes) {
        Deck newDeck = new MainDeck(name);
        for (model.Card c : cards) {
            newDeck.addCard(c);
        }
        for (Map.Entry<String, Boolean> p : attributes) {
            newDeck.addAttribute(p.getKey(), p.getValue());
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

    public List<Player> getLosers() {
        return this.losers;
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
            // notifico el comienzo de la seleccion de Cards de cada uno de los jugadores en juego
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

    public List<String> getAttributes(){
        return this.attributes;
    }

    public List<AbstractCharacter> getCharacters() {
        return new ArrayList<AbstractCharacter>(this.all.values());
    }

    public void addPlayer(Player player) {
        this.players.add(player);
        this.turns.add(player);
    }

    public void createCard(Character character, List<String> selectedAttributes) {
        Card card = new Card(character);
        card.setAttributes(selectedAttributes);
        this.cards.put(String.valueOf(this.cards.size()), card);
    }

    public void createCharacter(String characterName, String realName, Map<String, Double> selectedAttributes) {
       // Character character = new Character(characterName, realName,selectedAttributes);
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

    private void crearEstPrueba() {

        this.cards = new Hashtable<String, Card>();
        this.decks = new ArrayList<Deck>();
        this.characters = new Hashtable<String, Character>();
        this.attributes = new ArrayList<String>();

        //cargo atributos predeterminados
        this.attributes.add("Fuerza");
        this.attributes.add("Velocidad");
        this.attributes.add("Maldad");
        this.attributes.add("Destreza");
        this.attributes.add("Inteligencia");
        this.attributes.add("Peso");
        this.attributes.add("Bondad");

        //creo Cards...

        /**Card N°1**/
        Character p1 = new Character("Superman", "Clark Kent");
        Map<String,Double>  attributtes1 = new Hashtable<>();
        this.characters.put(p1.getFictitiousName(),p1);
        Card c1 = new Card(p1);
        c1.addAttribute("Fuerza");
        c1.addAttribute("Velocidad");
        c1.addAttribute("Maldad");
        c1.addAttribute("Destreza");
        c1.addAttribute("Inteligencia");
        c1.addAttribute("Peso");
        c1.addAttribute("Bondad");

        this.cards.put( String.valueOf(1 ), c1);

        /**Card N°2**/
        Character p2 = new Character("Batman", "Bruce Wane");
        p2.addAttribute("Fuerza", 400.0);
        p2.addAttribute("Velocidad", 85.0);
        p2.addAttribute("Maldad", 10.0);
        p2.addAttribute("Destreza", 8.0);
        p2.addAttribute("Inteligencia", 85.0);
        p2.addAttribute("Peso", 106.0);
        p2.addAttribute("Bondad", 70.0);
        this.characters.put(p2.getFictitiousName(),p2);
        Card c2 = new Card(p2);
        c2.addAttribute("Fuerza");
        c2.addAttribute("Velocidad");
        c2.addAttribute("Maldad");
        c2.addAttribute("Destreza");
        c2.addAttribute("Inteligencia");
        c2.addAttribute("Peso");
        c2.addAttribute("Bondad");

        this.cards.put(String.valueOf(2), c2);

        /**Card N°3**/
        Character p3 = new Character("Flash", "Jay Garrick");
        p3.addAttribute("Fuerza", 840.0);
        p3.addAttribute("Velocidad", 800000.0);
        p3.addAttribute("Maldad", 0.0);
        p3.addAttribute("Destreza", 9.0);
        p3.addAttribute("Inteligencia", 75.5);
        p3.addAttribute("Peso", 90.0);
        p3.addAttribute("Bondad", 95.5);
        this.characters.put(p3.getFictitiousName(),p3);
        Card c3 = new Card(p3);
        c3.addAttribute("Fuerza");
        c3.addAttribute("Velocidad");
        c3.addAttribute("Maldad");
        c3.addAttribute("Destreza");
        c3.addAttribute("Inteligencia");
        c3.addAttribute("Peso");
        c3.addAttribute("Bondad");

        this.cards.put(String.valueOf(3), c3);

        /**Card N°4**/
        Character p4 = new Character("Mujer Maravilla", "Princesa Diana");
        p4.addAttribute("Fuerza", 830.0);
        p4.addAttribute("Velocidad", 70.0);
        p4.addAttribute("Maldad", 0.0);
        p4.addAttribute("Destreza", 9.5);
        p4.addAttribute("Inteligencia", 90.0);
        p4.addAttribute("Peso", 62.0);
        p4.addAttribute("Bondad", 97.0);
        this.characters.put(p4.getFictitiousName(),p4);
        Card c4 = new Card(p4);
        c4.addAttribute("Fuerza");
        c4.addAttribute("Velocidad");
        c4.addAttribute("Maldad");
        c4.addAttribute("Destreza");
        c4.addAttribute("Inteligencia");
        c4.addAttribute("Peso");
        c4.addAttribute("Bondad");

        this.cards.put(String.valueOf(4), c4);

        /**Card N°5**/
        Character p5 = new Character("Batichica", "Betty Kane");
        p5.addAttribute("Fuerza", 85.0);
        p5.addAttribute("Velocidad", 50.0);
        p5.addAttribute("Maldad", 8.0);
        p5.addAttribute("Destreza", 8.0);
        p5.addAttribute("Inteligencia", 90.0);
        p5.addAttribute("Peso", 60.0);
        p5.addAttribute("Bondad", 97.5);
        this.characters.put(p5.getFictitiousName(),p5);
        Card c5 = new Card(p5);
        c5.addAttribute("Fuerza");
        c5.addAttribute("Velocidad");
        c5.addAttribute("Maldad");
        c5.addAttribute("Destreza");
        c5.addAttribute("Inteligencia");
        c5.addAttribute("Peso");
        c5.addAttribute("Bondad");

        this.cards.put(String.valueOf(5), c5);

        /**Card N°6**/
        Character p6 = new Character("Robin", "Dick Grayson");
        p6.addAttribute("Fuerza", 200.0);
        p6.addAttribute("Velocidad", 80.0);
        p6.addAttribute("Maldad", 0.0);
        p6.addAttribute("Destreza", 8.5);
        p6.addAttribute("Inteligencia", 80.0);
        p6.addAttribute("Peso", 65.0);
        p6.addAttribute("Bondad", 98.0);
        this.characters.put(p6.getFictitiousName(),p6);
        Card c6 = new Card(p6);
        c6.addAttribute("Fuerza");
        c6.addAttribute("Velocidad");
        c6.addAttribute("Maldad");
        c6.addAttribute("Destreza");
        c6.addAttribute("Inteligencia");
        c6.addAttribute("Peso");
        c6.addAttribute("Bondad");

        this.cards.put(String.valueOf(6), c6);

        /**Card N°7**/
        Character p7 = new Character("Chica Halcon", "Shiera Sanders");
        p7.addAttribute("Fuerza", 340.0);
        p7.addAttribute("Velocidad", 300.0);
        p7.addAttribute("Maldad", 0.0);
        p7.addAttribute("Destreza", 8.5);
        p7.addAttribute("Inteligencia", 95.5);
        p7.addAttribute("Peso", 55.0);
        p7.addAttribute("Bondad", 97.0);
        this.characters.put(p7.getFictitiousName(),p7);
        Card c7 = new Card(p7);
        c7.addAttribute("Fuerza");
        c7.addAttribute("Velocidad");
        c7.addAttribute("Maldad");
        c7.addAttribute("Destreza");
        c7.addAttribute("Inteligencia");
        c7.addAttribute("Peso");
        c7.addAttribute("Bondad");

        this.cards.put(String.valueOf(7), c7);

        /**Card N°8**/
        Character p8 = new Character("Linterna Verde", "Alan Scott");
        p8.addAttribute("Fuerza", 830.0);
        p8.addAttribute("Velocidad", 340.0);
        p8.addAttribute("Maldad", 0.0);
        p8.addAttribute("Destreza", 7.5);
        p8.addAttribute("Inteligencia", 90.0);
        p8.addAttribute("Peso", 99.0);
        p8.addAttribute("Bondad", 98.5);
        this.characters.put(p8.getFictitiousName(),p8);
        Card c8 = new Card(p8);
        c8.addAttribute("Fuerza");
        c8.addAttribute("Velocidad");
        c8.addAttribute("Maldad");
        c8.addAttribute("Destreza");
        c8.addAttribute("Inteligencia");
        c8.addAttribute("Peso");
        c8.addAttribute("Bondad");

        this.cards.put(String.valueOf(8), c8);

        /**Card N°9**/
        Character p9 = new Character("Aquaman", "Arthur Curry");
        p9.addAttribute("Fuerza", 700.0);
        p9.addAttribute("Velocidad", 220.0);
        p9.addAttribute("Maldad", 0.0);
        p9.addAttribute("Destreza", 9.5);
        p9.addAttribute("Inteligencia", 94.0);
        p9.addAttribute("Peso", 86.0);
        p9.addAttribute("Bondad", 99.0);
        this.characters.put(p9.getFictitiousName(),p9);
        Card c9 = new Card(p9);

        c9.addAttribute("Fuerza");
        c9.addAttribute("Velocidad");
        c9.addAttribute("Maldad");
        c9.addAttribute("Destreza");
        c9.addAttribute("Inteligencia");
        c9.addAttribute("Peso");
        c9.addAttribute("Bondad");

        this.cards.put(String.valueOf(9), c9);

        /**Card N°10**/
        Character p10 = new Character("Flecha Verde", "Oliver Jonas Queen");
        p10.addAttribute("Fuerza", 710.0);
        p10.addAttribute("Velocidad", 220.0);
        p10.addAttribute("Maldad", 0.0);
        p10.addAttribute("Destreza", 8.5);
        p10.addAttribute("Inteligencia", 95.5);
        p10.addAttribute("Peso", 86.5);
        p10.addAttribute("Bondad", 98.5);
        this.characters.put(p10.getFictitiousName(),p10);
        Card c10 = new Card(p10);
        c10.addAttribute("Fuerza");
        c10.addAttribute("Velocidad");
        c10.addAttribute("Maldad");
        c10.addAttribute("Destreza");
        c10.addAttribute("Inteligencia");
        c10.addAttribute("Peso");
        c10.addAttribute("Bondad");

        this.cards.put(String.valueOf(10), c10);

        /**Card N°11**/
        Character p11 = new Character("El Atomo", "Al Pratt");
        p11.addAttribute("Fuerza", 800.0);
        p11.addAttribute("Velocidad", 110.0);
        p11.addAttribute("Maldad", 0.0);
        p11.addAttribute("Destreza", 9.0);
        p11.addAttribute("Inteligencia", 95.5);
        p11.addAttribute("Peso", 1.5);
        p11.addAttribute("Bondad", 99.0);
        this.characters.put(p11.getFictitiousName(),p11);
        Card c11 = new Card(p11);
        c11.addAttribute("Fuerza");
        c11.addAttribute("Velocidad");
        c11.addAttribute("Maldad");
        c11.addAttribute("Destreza");
        c11.addAttribute("Inteligencia");
        c11.addAttribute("Peso");
        c11.addAttribute("Bondad");

        this.cards.put(String.valueOf(11), c11);

        /**Card N°12**/
        Character p12 = new Character("Canario Negro", "Dinah Drake");
        p12.addAttribute("Fuerza", 100.0);
        p12.addAttribute("Velocidad", 110.0);
        p12.addAttribute("Maldad", 2.0);
        p12.addAttribute("Destreza", 9.5);
        p12.addAttribute("Inteligencia", 92.0);
        p12.addAttribute("Peso", 55.0);
        p12.addAttribute("Bondad", 96.0);
        this.characters.put(p12.getFictitiousName(),p12);
        Card c12 = new Card(p12);
        c12.addAttribute("Fuerza");
        c12.addAttribute("Velocidad");
        c12.addAttribute("Maldad");
        c12.addAttribute("Destreza");
        c12.addAttribute("Inteligencia");
        c12.addAttribute("Peso");
        c12.addAttribute("Bondad");

        this.cards.put(String.valueOf(12), c12);

        /**Card N°13**/
        Character p13 = new Character("Manhunter", "Dan Richards");
        p13.addAttribute("Fuerza", 400.0);
        p13.addAttribute("Velocidad", 85.0);
        p13.addAttribute("Maldad", 0.0);
        p13.addAttribute("Destreza", 7.0);
        p13.addAttribute("Inteligencia", 97.0);
        p13.addAttribute("Peso", 86.0);
        p13.addAttribute("Bondad", 98.5);
        this.characters.put(p13.getFictitiousName(),p13);
        Card c13 = new Card(p13);
        c13.addAttribute("Fuerza");
        c13.addAttribute("Velocidad");
        c13.addAttribute("Maldad");
        c13.addAttribute("Destreza");
        c13.addAttribute("Inteligencia");
        c13.addAttribute("Peso");
        c13.addAttribute("Bondad");

        this.cards.put(String.valueOf(13), c13);

        /**Card N°14**/
        Character p14 = new Character("Raven", "Rachel Roth");
        p14.addAttribute("Fuerza", 85.0);
        p14.addAttribute("Velocidad", 85.0);
        p14.addAttribute("Maldad", 8.0);
        p14.addAttribute("Destreza", 9.0);
        p14.addAttribute("Inteligencia", 97.0);
        p14.addAttribute("Peso", 55.0);
        p14.addAttribute("Bondad", 75.0);
        this.characters.put(p14.getFictitiousName(),p14);
        Card c14 = new Card(p14);
        c14.addAttribute("Fuerza");
        c14.addAttribute("Velocidad");
        c14.addAttribute("Maldad");
        c14.addAttribute("Destreza");
        c14.addAttribute("Inteligencia");
        c14.addAttribute("Peso");
        c14.addAttribute("Bondad");

        this.cards.put(String.valueOf(14), c14);

        /**Card N°15**/
        Character p15 = new Character("Starfire", "Koriand'r");
        p15.addAttribute("Fuerza", 200.0);
        p15.addAttribute("Velocidad", 110.0);
        p15.addAttribute("Maldad", 0.0);
        p15.addAttribute("Destreza", 9.0);
        p15.addAttribute("Inteligencia", 89.0);
        p15.addAttribute("Peso", 57.0);
        p15.addAttribute("Bondad", 98.5);
        this.characters.put(p15.getFictitiousName(),p15);
        Card c15 = new Card(p15);
        c15.addAttribute("Fuerza");
        c15.addAttribute("Velocidad");
        c15.addAttribute("Maldad");
        c15.addAttribute("Destreza");
        c15.addAttribute("Inteligencia");
        c15.addAttribute("Peso");
        c15.addAttribute("Bondad");

        this.cards.put(String.valueOf(15), c15);

        /**Card N°16**/
        Character p16 = new Character("Cyborg", "Victor 'Vic' Stone");
        p16.addAttribute("Fuerza", 1100.0);
        p16.addAttribute("Velocidad", 150.0);
        p16.addAttribute("Maldad", 0.0);
        p16.addAttribute("Destreza", 6.0);
        p16.addAttribute("Inteligencia", 95.5);
        p16.addAttribute("Peso", 110.0);
        p16.addAttribute("Bondad", 97.0);
        this.characters.put(p16.getFictitiousName(),p16);
        Card c16 = new Card(p16);
        c16.addAttribute("Fuerza");
        c16.addAttribute("Velocidad");
        c16.addAttribute("Maldad");
        c16.addAttribute("Destreza");
        c16.addAttribute("Inteligencia");
        c16.addAttribute("Peso");
        c16.addAttribute("Bondad");

        this.cards.put(String.valueOf(16), c16);

        /**Card N°17**/
        Character p17 = new Character("Guason", "Desconocido");
        p17.addAttribute("Fuerza", 45.0);
        p17.addAttribute("Velocidad", 45.0);
        p17.addAttribute("Maldad", 98.0);
        p17.addAttribute("Destreza", 8.5);
        p17.addAttribute("Inteligencia", 97.0);
        p17.addAttribute("Peso", 67.0);
        p17.addAttribute("Bondad", 5.0);
        this.characters.put(p17.getFictitiousName(),p17);
        Card c17 = new Card(p17);
        c17.addAttribute("Fuerza");
        c17.addAttribute("Velocidad");
        c17.addAttribute("Maldad");
        c17.addAttribute("Destreza");
        c17.addAttribute("Inteligencia");
        c17.addAttribute("Peso");
        c17.addAttribute("Bondad");

        this.cards.put(String.valueOf(17), c17);

        /**Card N°18**/
        Character p18 = new Character("El Pingüino", "Oswald Chesterfield Cobblepot");
        p18.addAttribute("Fuerza", 30.0);
        p18.addAttribute("Velocidad", 15.0);
        p18.addAttribute("Maldad", 100.0);
        p18.addAttribute("Destreza", 4.0);
        p18.addAttribute("Inteligencia", 99.0);
        p18.addAttribute("Peso", 90.0);
        p18.addAttribute("Bondad", 0.5);
        this.characters.put(p18.getFictitiousName(),p18);
        Card c18 = new Card(p18);
        c18.addAttribute("Fuerza");
        c18.addAttribute("Velocidad");
        c18.addAttribute("Maldad");
        c18.addAttribute("Destreza");
        c18.addAttribute("Inteligencia");
        c18.addAttribute("Peso");
        c18.addAttribute("Bondad");

        this.cards.put(String.valueOf(18), c18);

        /**Card N°19**/
        Character p19 = new Character("Acertijo", "Edward Nigma");
        p19.addAttribute("Fuerza", 625.0);
        p19.addAttribute("Velocidad", 45.0);
        p19.addAttribute("Maldad", 98.0);
        p19.addAttribute("Destreza", 8.0);
        p19.addAttribute("Inteligencia", 90.0);
        p19.addAttribute("Peso", 62.0);
        p19.addAttribute("Bondad", 0.0);
        this.characters.put(p19.getFictitiousName(),p19);
        Card c19 = new Card(p19);
        c19.addAttribute("Fuerza");
        c19.addAttribute("Velocidad");
        c19.addAttribute("Maldad");
        c19.addAttribute("Destreza");
        c19.addAttribute("Inteligencia");
        c19.addAttribute("Peso");
        c19.addAttribute("Bondad");

        this.cards.put(String.valueOf(19), c19);

        /**Card N°20**/
        Character p20 = new Character("Gatubela", "Selina Kyle");
        p20.addAttribute("Fuerza", 80.0);
        p20.addAttribute("Velocidad", 35.0);
        p20.addAttribute("Maldad", 99.0);
        p20.addAttribute("Destreza", 9.5);
        p20.addAttribute("Inteligencia", 90.0);
        p20.addAttribute("Peso", 60.5);
        p20.addAttribute("Bondad", 2.0);
        this.characters.put(p20.getFictitiousName(),p20);
        Card c20 = new Card(p20);
        c20.addAttribute("Fuerza");
        c20.addAttribute("Velocidad");
        c20.addAttribute("Maldad");
        c20.addAttribute("Destreza");
        c20.addAttribute("Inteligencia");
        c20.addAttribute("Peso");
        c20.addAttribute("Bondad");

        this.cards.put(String.valueOf(20), c20);

        /**Card N°21**/
        Character p21 = new Character("Lex Luthor", "Alexander Joseph Luthor");
        p21.addAttribute("Fuerza", 415.0);
        p21.addAttribute("Velocidad", 90.0);
        p21.addAttribute("Maldad", 99.0);
        p21.addAttribute("Destreza", 7.5);
        p21.addAttribute("Inteligencia", 98.0);
        p21.addAttribute("Peso", 95.0);
        p21.addAttribute("Bondad", 0.0);
        this.characters.put(p21.getFictitiousName(),p21);
        Card c21 = new Card(p21);
        c21.addAttribute("Fuerza");
        c21.addAttribute("Velocidad");
        c21.addAttribute("Maldad");
        c21.addAttribute("Destreza");
        c21.addAttribute("Inteligencia");
        c21.addAttribute("Peso");
        c21.addAttribute("Bondad");

        this.cards.put(String.valueOf(21), c21);

        /**Card N°22**/
        Character p22 = new Character("La Cosa del Pantano", "Alec Holland");
        p22.addAttribute("Fuerza", 100.0);
        p22.addAttribute("Velocidad", 40.0);
        p22.addAttribute("Maldad", 95.0);
        p22.addAttribute("Destreza", 6.5);
        p22.addAttribute("Inteligencia", 89.5);
        p22.addAttribute("Peso", 90.0);
        p22.addAttribute("Bondad", 1.0);
        this.characters.put(p22.getFictitiousName(),p22);
        Card c22 = new Card(p22);
        c22.addAttribute("Fuerza");
        c22.addAttribute("Velocidad");
        c22.addAttribute("Maldad");
        c22.addAttribute("Destreza");
        c22.addAttribute("Inteligencia");
        c22.addAttribute("Peso");
        c22.addAttribute("Bondad");

        this.cards.put(String.valueOf(22), c22);

        /**Card N°23**/
        Character p23 = new Character("Sangre", "Hermano Sangre");
        p23.addAttribute("Fuerza", 1195.0);
        p23.addAttribute("Velocidad", 145.0);
        p23.addAttribute("Maldad", 90.0);
        p23.addAttribute("Destreza", 5.0);
        p23.addAttribute("Inteligencia", 97.0);
        p23.addAttribute("Peso", 77.0);
        p23.addAttribute("Bondad", 0.5);
        this.characters.put(p23.getFictitiousName(),p23);
        Card c23 = new Card(p23);
        c23.addAttribute("Fuerza");
        c23.addAttribute("Velocidad");
        c23.addAttribute("Maldad");
        c23.addAttribute("Destreza");
        c23.addAttribute("Inteligencia");
        c23.addAttribute("Peso");
        c23.addAttribute("Bondad");

        this.cards.put(String.valueOf(23), c23);

        /**Card N°24**/
        Character p24 = new Character("Mr. Frio", "Victor Fries");
        p24.addAttribute("Fuerza", 1000.0);
        p24.addAttribute("Velocidad", 60.0);
        p24.addAttribute("Maldad", 96.0);
        p24.addAttribute("Destreza", 6.0);
        p24.addAttribute("Inteligencia", 97.0);
        p24.addAttribute("Peso", 80.0);
        p24.addAttribute("Bondad", 0.0);
        this.characters.put(p24.getFictitiousName(),p24);
        Card c24 = new Card(p24);
        c24.addAttribute("Fuerza");
        c24.addAttribute("Velocidad");
        c24.addAttribute("Maldad");
        c24.addAttribute("Destreza");
        c24.addAttribute("Inteligencia");
        c24.addAttribute("Peso");
        c24.addAttribute("Bondad");

        this.cards.put(String.valueOf(24), c24);

        /**Card N°25**/
        Character p25 = new Character("Thor", "Thor Odinson");
        p25.addAttribute("Fuerza", 1100.0);
        p25.addAttribute("Velocidad", 250.0);
        p25.addAttribute("Maldad", 10.0);
        p25.addAttribute("Destreza", 8.5);
        p25.addAttribute("Inteligencia", 95.0);
        p25.addAttribute("Peso", 103.0);
        p25.addAttribute("Bondad", 99.5);
        this.characters.put(p25.getFictitiousName(),p25);
        Card c25 = new Card(p25);
        c25.addAttribute("Fuerza");
        c25.addAttribute("Velocidad");
        c25.addAttribute("Maldad");
        c25.addAttribute("Destreza");
        c25.addAttribute("Inteligencia");
        c25.addAttribute("Peso");
        c25.addAttribute("Bondad");

        this.cards.put(String.valueOf(25), c25);

        /**Card N°26**/
        Character p26 = new Character("Ciclope", "Scott Summers");
        p26.addAttribute("Fuerza", 600.0);
        p26.addAttribute("Velocidad", 90.0);
        p26.addAttribute("Maldad", 0.0);
        p26.addAttribute("Destreza", 8.0);
        p26.addAttribute("Inteligencia", 96.0);
        p26.addAttribute("Peso", 74.0);
        p26.addAttribute("Bondad", 98.5);
        this.characters.put(p26.getFictitiousName(),p26);
        Card c26 = new Card(p26);
        c26.addAttribute("Fuerza");
        c26.addAttribute("Velocidad");
        c26.addAttribute("Maldad");
        c26.addAttribute("Destreza");
        c26.addAttribute("Inteligencia");
        c26.addAttribute("Peso");
        c26.addAttribute("Bondad");

        this.cards.put(String.valueOf(26), c26);

        /**Card N°27**/
        Character p27 = new Character("Bestia", "Henry Philip McCoy");
        p27.addAttribute("Fuerza", 1000.0);
        p27.addAttribute("Velocidad", 86.0);
        p27.addAttribute("Maldad", 0.0);
        p27.addAttribute("Destreza", 8.5);
        p27.addAttribute("Inteligencia", 95.0);
        p27.addAttribute("Peso", 112.0);
        p27.addAttribute("Bondad", 99.0);
        this.characters.put(p27.getFictitiousName(),p27);
        Card c27 = new Card(p27);
        c27.addAttribute("Fuerza");
        c27.addAttribute("Velocidad");
        c27.addAttribute("Maldad");
        c27.addAttribute("Destreza");
        c27.addAttribute("Inteligencia");
        c27.addAttribute("Peso");
        c27.addAttribute("Bondad");

        this.cards.put(String.valueOf(27), c27);

        /**Card N°28**/
        Character p28 = new Character("Wolverine", "James Hudson Howlett");
        p28.addAttribute("Fuerza", 900.0);
        p28.addAttribute("Velocidad", 215.0);
        p28.addAttribute("Maldad", 2.0);
        p28.addAttribute("Destreza", 9.0);
        p28.addAttribute("Inteligencia", 96.0);
        p28.addAttribute("Peso", 81.0);
        p28.addAttribute("Bondad", 98.0);
        this.characters.put(p28.getFictitiousName(),p28);
        Card c28 = new Card(p28);
        c28.addAttribute("Fuerza");
        c28.addAttribute("Velocidad");
        c28.addAttribute("Maldad");
        c28.addAttribute("Destreza");
        c28.addAttribute("Inteligencia");
        c28.addAttribute("Peso");
        c28.addAttribute("Bondad");

        this.cards.put(String.valueOf(28), c28);

        /**Card N°29**/
        Character p29 = new Character("Storm", "Ororo Iqadi Munroe");
        p29.addAttribute("Fuerza", 120.0);
        p29.addAttribute("Velocidad", 110.0);
        p29.addAttribute("Maldad", 0.0);
        p29.addAttribute("Destreza", 9.5);
        p29.addAttribute("Inteligencia", 97.0);
        p29.addAttribute("Peso", 66.0);
        p29.addAttribute("Bondad", 98.0);
        this.characters.put(p29.getFictitiousName(),p29);
        Card c29 = new Card(p29);
        c29.addAttribute("Fuerza");
        c29.addAttribute("Velocidad");
        c29.addAttribute("Maldad");
        c29.addAttribute("Destreza");
        c29.addAttribute("Inteligencia");
        c29.addAttribute("Peso");
        c29.addAttribute("Bondad");

        this.cards.put(String.valueOf(29), c29);

        /**Card N°30**/
        Character p30 = new Character("Prof. Xavier", "Bruce Wane");
        p30.addAttribute("Fuerza", 150.0);
        p30.addAttribute("Velocidad", 50.0);
        p30.addAttribute("Maldad", 0.0);
        p30.addAttribute("Destreza", 60.5);
        p30.addAttribute("Inteligencia", 100.0);
        p30.addAttribute("Peso", 90.0);
        p30.addAttribute("Bondad", 99.0);
        this.characters.put(p30.getFictitiousName(),p30);
        Card c30 = new Card(p30);
        c30.addAttribute("Fuerza");
        c30.addAttribute("Velocidad");
        c30.addAttribute("Maldad");
        c30.addAttribute("Destreza");
        c30.addAttribute("Inteligencia");
        c30.addAttribute("Peso");
        c30.addAttribute("Bondad");

        this.cards.put(String.valueOf(30), c30);

        /**Card N°31**/
        Character p31 = new Character("Spider-Man", "Peter Benjamin Parker Fitzpatrick");
        p31.addAttribute("Fuerza", 500.0);
        p31.addAttribute("Velocidad", 150.0);
        p31.addAttribute("Maldad", 10.0);
        p31.addAttribute("Destreza", 10.0);
        p31.addAttribute("Inteligencia", 98.0);
        p31.addAttribute("Peso", 83.0);
        p31.addAttribute("Bondad", 99.0);
        this.characters.put(p31.getFictitiousName(),p31);
        Card c31 = new Card(p31);
        c31.addAttribute("Fuerza");
        c31.addAttribute("Velocidad");
        c31.addAttribute("Maldad");
        c31.addAttribute("Destreza");
        c31.addAttribute("Inteligencia");
        c31.addAttribute("Peso");
        c31.addAttribute("Bondad");

        this.cards.put(String.valueOf(31), c31);

        /**Card N°32**/
        Character p32 = new Character("Iron Man", "Anthony Edward 'Tony' Stark");
        p32.addAttribute("Fuerza", 930.0);
        p32.addAttribute("Velocidad", 197.0);
        p32.addAttribute("Maldad", 0.0);
        p32.addAttribute("Destreza", 8.5);
        p32.addAttribute("Inteligencia", 99.0);
        p32.addAttribute("Peso", 587.0);
        p32.addAttribute("Bondad", 90.0);
        this.characters.put(p32.getFictitiousName(),p32);
        Card c32 = new Card(p32);
        c32.addAttribute("Fuerza");
        c32.addAttribute("Velocidad");
        c32.addAttribute("Maldad");
        c32.addAttribute("Destreza");
        c32.addAttribute("Inteligencia");
        c32.addAttribute("Peso");
        c32.addAttribute("Bondad");

        this.cards.put(String.valueOf(32), c32);

        /**Card N°33**/
        Character p33 = new Character("Antorcha Humana", "Johnny Storm");
        p33.addAttribute("Fuerza", 150.0);
        p33.addAttribute("Velocidad", 160.0);
        p33.addAttribute("Maldad", 0.0);
        p33.addAttribute("Destreza", 8.0);
        p33.addAttribute("Inteligencia", 80.0);
        p33.addAttribute("Peso", 76.5);
        p33.addAttribute("Bondad", 85.0);
        this.characters.put(p33.getFictitiousName(),p33);
        Card c33 = new Card(p33);
        c33.addAttribute("Fuerza");
        c33.addAttribute("Velocidad");
        c33.addAttribute("Maldad");
        c33.addAttribute("Destreza");
        c33.addAttribute("Inteligencia");
        c33.addAttribute("Peso");
        c33.addAttribute("Bondad");

        this.cards.put(String.valueOf(33), c33);

        /**Card N°34**/
        Character p34 = new Character("Mujer Invisible", "Susan Storm");
        p34.addAttribute("Fuerza", 250.0);
        p34.addAttribute("Velocidad", 80.0);
        p34.addAttribute("Maldad", 0.0);
        p34.addAttribute("Destreza", 8.0);
        p34.addAttribute("Inteligencia", 97.0);
        p34.addAttribute("Peso", 55.0);
        p34.addAttribute("Bondad", 98.5);
        this.characters.put(p34.getFictitiousName(),p34);
        Card c34 = new Card(p34);
        c34.addAttribute("Fuerza");
        c34.addAttribute("Velocidad");
        c34.addAttribute("Maldad");
        c34.addAttribute("Destreza");
        c34.addAttribute("Inteligencia");
        c34.addAttribute("Peso");
        c34.addAttribute("Bondad");

        this.cards.put(String.valueOf(34), c34);

        /**Card N°35**/
        Character p35 = new Character("La Mole", "Ben Grimm");
        p35.addAttribute("Fuerza", 1800.0);
        p35.addAttribute("Velocidad", 80.0);
        p35.addAttribute("Maldad", 0.0);
        p35.addAttribute("Destreza", 5.0);
        p35.addAttribute("Inteligencia", 90.0);
        p35.addAttribute("Peso", 430.0);
        p35.addAttribute("Bondad", 98.0);
        this.characters.put(p35.getFictitiousName(),p35);
        Card c35 = new Card(p35);
        c35.addAttribute("Fuerza");
        c35.addAttribute("Velocidad");
        c35.addAttribute("Maldad");
        c35.addAttribute("Destreza");
        c35.addAttribute("Inteligencia");
        c35.addAttribute("Peso");
        c35.addAttribute("Bondad");

        this.cards.put(String.valueOf(35), c35);

        /**Card N°36**/
        Character p36 = new Character("Hombre Elastico", "Reed Richards");
        p36.addAttribute("Fuerza", 120.0);
        p36.addAttribute("Velocidad", 85.0);
        p36.addAttribute("Maldad", 0.0);
        p36.addAttribute("Destreza", 9.0);
        p36.addAttribute("Inteligencia", 100.0);
        p36.addAttribute("Peso", 81.0);
        p36.addAttribute("Bondad", 98.5);
        this.characters.put(p36.getFictitiousName(),p36);
        Card c36 = new Card(p36);
        c36.addAttribute("Fuerza");
        c36.addAttribute("Velocidad");
        c36.addAttribute("Maldad");
        c36.addAttribute("Destreza");
        c36.addAttribute("Inteligencia");
        c36.addAttribute("Peso");
        c36.addAttribute("Bondad");

        this.cards.put(String.valueOf(36), c36);

        /**Card N°37**/
        Character p37 = new Character("Hulk", "Robert Bruce Banner");
        p37.addAttribute("Fuerza", 2200.0);
        p37.addAttribute("Velocidad", 130.0);
        p37.addAttribute("Maldad", 25.0);
        p37.addAttribute("Destreza", 7.0);
        p37.addAttribute("Inteligencia", 80.0);
        p37.addAttribute("Peso", 450.0);
        p37.addAttribute("Bondad", 60.0);
        this.characters.put(p37.getFictitiousName(),p37);
        Card c37 = new Card(p37);
        c37.addAttribute("Fuerza");
        c37.addAttribute("Velocidad");
        c37.addAttribute("Maldad");
        c37.addAttribute("Destreza");
        c37.addAttribute("Inteligencia");
        c37.addAttribute("Peso");
        c37.addAttribute("Bondad");

        this.cards.put(String.valueOf(37), c37);

        /**Card N°38**/
        Character p38 = new Character("Surfista Plateado", "Norrin Radd");
        p38.addAttribute("Fuerza", 750.0);
        p38.addAttribute("Velocidad", 245.0);
        p38.addAttribute("Maldad", 0.0);
        p38.addAttribute("Destreza", 8.5);
        p38.addAttribute("Inteligencia", 95.0);
        p38.addAttribute("Peso", 86.0);
        p38.addAttribute("Bondad", 100.0);
        this.characters.put(p38.getFictitiousName(),p38);
        Card c38 = new Card(p38);
        c38.addAttribute("Fuerza");
        c38.addAttribute("Velocidad");
        c38.addAttribute("Maldad");
        c38.addAttribute("Destreza");
        c38.addAttribute("Inteligencia");
        c38.addAttribute("Peso");
        c38.addAttribute("Bondad");

        this.cards.put(String.valueOf(38), c38);

        /**Card N°39**/
        Character p39 = new Character("Dr. Doom", "Victor von Doom");
        p39.addAttribute("Fuerza", 1000.0);
        p39.addAttribute("Velocidad", 185.0);
        p39.addAttribute("Maldad", 100.0);
        p39.addAttribute("Destreza", 8.0);
        p39.addAttribute("Inteligencia", 95.5);
        p39.addAttribute("Peso", 115.0);
        p39.addAttribute("Bondad", 0.0);
        this.characters.put(p39.getFictitiousName(),p39);
        Card c39 = new Card(p39);
        c39.addAttribute("Fuerza");
        c39.addAttribute("Velocidad");
        c39.addAttribute("Maldad");
        c39.addAttribute("Destreza");
        c39.addAttribute("Inteligencia");
        c39.addAttribute("Peso");
        c39.addAttribute("Bondad");

        this.cards.put(String.valueOf(39), c39);

        /**Card N°40**/
        Character p40 = new Character("Hombre de Arena", "Flint Marko");
        p40.addAttribute("Fuerza", 1760.0);
        p40.addAttribute("Velocidad", 150.0);
        p40.addAttribute("Maldad", 75.0);
        p40.addAttribute("Destreza", 9.0);
        p40.addAttribute("Inteligencia", 80.0);
        p40.addAttribute("Peso", 205.0);
        p40.addAttribute("Bondad", 25.0);
        this.characters.put(p40.getFictitiousName(),p40);
        Card c40 = new Card(p40);
        c40.addAttribute("Fuerza");
        c40.addAttribute("Velocidad");
        c40.addAttribute("Maldad");
        c40.addAttribute("Destreza");
        c40.addAttribute("Inteligencia");
        c40.addAttribute("Peso");
        c40.addAttribute("Bondad");

        this.cards.put(String.valueOf(40), c40);

        /**Card N°41**/
        Character p41 = new Character("Rinho", "Aleksei Mikhailovich Sytsevich");
        p41.addAttribute("Fuerza", 1600.0);
        p41.addAttribute("Velocidad", 190.0);
        p41.addAttribute("Maldad", 99.0);
        p41.addAttribute("Destreza", 6.0);
        p41.addAttribute("Inteligencia", 50.0);
        p41.addAttribute("Peso", 320.0);
        p41.addAttribute("Bondad", 0.0);
        this.characters.put(p41.getFictitiousName(),p41);
        Card c41 = new Card(p41);
        c41.addAttribute("Fuerza");
        c41.addAttribute("Velocidad");
        c41.addAttribute("Maldad");
        c41.addAttribute("Destreza");
        c41.addAttribute("Inteligencia");
        c41.addAttribute("Peso");
        c41.addAttribute("Bondad");

        this.cards.put(String.valueOf(41), c41);

        /**Card N°42**/
        Character p42 = new Character("Dr. Octopus", "Otto Octavius");
        p42.addAttribute("Fuerza", 750.0);
        p42.addAttribute("Velocidad", 42.0);
        p42.addAttribute("Maldad", 90.0);
        p42.addAttribute("Destreza", 9.0);
        p42.addAttribute("Inteligencia", 98.0);
        p42.addAttribute("Peso", 90.0);
        p42.addAttribute("Bondad", 40.0);
        this.characters.put(p42.getFictitiousName(),p42);
        Card c42 = new Card(p42);
        c42.addAttribute("Fuerza");
        c42.addAttribute("Velocidad");
        c42.addAttribute("Maldad");
        c42.addAttribute("Destreza");
        c42.addAttribute("Inteligencia");
        c42.addAttribute("Peso");
        c42.addAttribute("Bondad");

        this.cards.put(String.valueOf(42), c42);

        /**Card N°43**/
        Character p43 = new Character("Escorpion", "Mac Gargan");
        p43.addAttribute("Fuerza", 1300.0);
        p43.addAttribute("Velocidad", 103.0);
        p43.addAttribute("Maldad", 99.0);
        p43.addAttribute("Destreza", 8.5);
        p43.addAttribute("Inteligencia", 95.5);
        p43.addAttribute("Peso", 100.0);
        p43.addAttribute("Bondad", 0.0);
        this.characters.put(p43.getFictitiousName(),p43);
        Card c43 = new Card(p43);
        c43.addAttribute("Fuerza");
        c43.addAttribute("Velocidad");
        c43.addAttribute("Maldad");
        c43.addAttribute("Destreza");
        c43.addAttribute("Inteligencia");
        c43.addAttribute("Peso");
        c43.addAttribute("Bondad");

        this.cards.put(String.valueOf(43), c43);

        /**Card N°44**/
        Character p44 = new Character("Conmocionador", "Herman Schultz");
        p44.addAttribute("Fuerza", 400.0);
        p44.addAttribute("Velocidad", 43.0);
        p44.addAttribute("Maldad", 80.0);
        p44.addAttribute("Destreza", 7.0);
        p44.addAttribute("Inteligencia", 95.5);
        p44.addAttribute("Peso", 79.0);
        p44.addAttribute("Bondad", 2.0);
        this.characters.put(p44.getFictitiousName(),p44);
        Card c44 = new Card(p44);
        c44.addAttribute("Fuerza");
        c44.addAttribute("Velocidad");
        c44.addAttribute("Maldad");
        c44.addAttribute("Destreza");
        c44.addAttribute("Inteligencia");
        c44.addAttribute("Peso");
        c44.addAttribute("Bondad");

        this.cards.put(String.valueOf(44), c44);

        /**Card N°45**/
        Character p45 = new Character("Mysterio", "Quentin Beck");
        p45.addAttribute("Fuerza", 500.0);
        p45.addAttribute("Velocidad", 45.0);
        p45.addAttribute("Maldad", 95.0);
        p45.addAttribute("Destreza", 7.5);
        p45.addAttribute("Inteligencia", 97.0);
        p45.addAttribute("Peso", 79.0);
        p45.addAttribute("Bondad", 1.0);
        this.characters.put(p45.getFictitiousName(),p45);
        Card c45 = new Card(p45);
        c45.addAttribute("Fuerza");
        c45.addAttribute("Velocidad");
        c45.addAttribute("Maldad");
        c45.addAttribute("Destreza");
        c45.addAttribute("Inteligencia");
        c45.addAttribute("Peso");
        c45.addAttribute("Bondad");

        this.cards.put(String.valueOf(45), c45);

        /**Card N°46**/
        Character p46 = new Character("Duende Verde", "Norman Osborn");
        p46.addAttribute("Fuerza", 830.0);
        p46.addAttribute("Velocidad", 205.0);
        p46.addAttribute("Maldad", 95.0);
        p46.addAttribute("Destreza", 9.0);
        p46.addAttribute("Inteligencia", 98.0);
        p46.addAttribute("Peso", 60.0);
        p46.addAttribute("Bondad", 10.0);
        this.characters.put(p46.getFictitiousName(),p46);
        Card c46 = new Card(p46);
        c46.addAttribute("Fuerza");
        c46.addAttribute("Velocidad");
        c46.addAttribute("Maldad");
        c46.addAttribute("Destreza");
        c46.addAttribute("Inteligencia");
        c46.addAttribute("Peso");
        c46.addAttribute("Bondad");

        this.cards.put(String.valueOf(46), c46);

        /**Card N°47**/
        Character p47 = new Character("Loki", "Loki Laufeyson");
        p47.addAttribute("Fuerza", 1000.0);
        p47.addAttribute("Velocidad", 190.0);
        p47.addAttribute("Maldad", 10.0);
        p47.addAttribute("Destreza", 8.0);
        p47.addAttribute("Inteligencia", 96.0);
        p47.addAttribute("Peso", 100.0);
        p47.addAttribute("Bondad", 10.0);
        this.characters.put(p47.getFictitiousName(),p47);
        Card c47 = new Card(p47);
        c47.addAttribute("Fuerza");
        c47.addAttribute("Velocidad");
        c47.addAttribute("Maldad");
        c47.addAttribute("Destreza");
        c47.addAttribute("Inteligencia");
        c47.addAttribute("Peso");
        c47.addAttribute("Bondad");

        this.cards.put(String.valueOf(47), c47);

        /**Card N°48**/
        Character p48 = new Character("Iron Monger", "Obadiah Stane");
        p48.addAttribute("Fuerza", 1800.0);
        p48.addAttribute("Velocidad", 280.0);
        p48.addAttribute("Maldad", 100.0);
        p48.addAttribute("Destreza", 5.0);
        p48.addAttribute("Inteligencia", 90.0);
        p48.addAttribute("Peso", 218.0);
        p48.addAttribute("Bondad", 0.0);
        this.characters.put(p48.getFictitiousName(),p48);
        Card c48 = new Card(p48);
        c48.addAttribute("Fuerza");
        c48.addAttribute("Velocidad");
        c48.addAttribute("Maldad");
        c48.addAttribute("Destreza");
        c48.addAttribute("Inteligencia");
        c48.addAttribute("Peso");
        c48.addAttribute("Bondad");

        this.cards.put(String.valueOf(48), c48);


    }

}