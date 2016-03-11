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

    private XStream xstream;
    private LinkedList<Player> turns;
    private MainDeck deck;
    private List<Player> players = new Vector<>();
    private Player winner;
    private List<Player> deadHeatList = new Vector<>();
    private List<Player> losers = new ArrayList<>();
    private List<MainDeck> decks;
    private Hashtable<String, Card> cards;
    private Confrontation confrontation;
    private Player currentPlayer;
    private Player gameWinner;

    private Hashtable<String, AbstractCharacter> all;
    private Hashtable<String, Character> characters;
    private LinkedHashMap<String, League> leagues;
    private List<String> attributes;


    private String currentAttribute;
    private DeckPlayer currentAccumulatorDeck = new DeckPlayer();

    //serializador de datos
    private DataAccessObject daoXML = DataAccessObjectXML.getInstance();

    public List<Player> getPlayers() {
        return this.players;
    }

    public Player getCurrentPalyer(){ //Devuelve el poseedor actual del turno
        return this.currentPlayer;
    }


    public Game() {

        //this.crearEstPrueba();

        this.turns = new LinkedList<>();
        this.characters = this.daoXML.getCharacters();
        this.leagues = this.daoXML.getLeagues();
        this.all =this.daoXML.getAll();
        this.cards = this.daoXML.getCards();
        this.decks = this.daoXML.getDecks();
        this.attributes = this.daoXML.getAttributes();
        this.confrontation = new HeroesConfrontation();

        for(League l : this.leagues.values()){
            System.out.print(l.getFictitiousName() + "\n");
            for(AbstractCharacter c : l.getCharacters()){
                System.out.print(c.getFictitiousName() + "\n");
            }
        }

        /**Guardar Datos**/
      this.daoXML.saveData(this.characters,this.leagues,this.attributes,this.decks,this.cards);

    }


    public void createDeck(List<Card> cards, String name,Map<String,Boolean> attributes) {
        MainDeck newDeck = new MainDeck(name);
        for (model.Card c : cards) {
            newDeck.addCard(c);
        }
        for (String p : attributes.keySet()) {
            newDeck.addAttribute(p, attributes.get(p));
        }
        this.decks.add(newDeck);
    }

    public void createLeague(String leagueName, List<AbstractCharacter> characters) {
        League newLeague = new League(leagueName);
        newLeague.setCharacters(characters);
        this.leagues.put(leagueName, newLeague);
        this.all.put(leagueName, newLeague);
    }

    private void checkPlayers() {
        for (int i = 0; i < this.turns.size(); i++) {
            if (this.turns.get(i).numberCards() == this.CARDS_LIMIT) {
                System.out.println("PERDIO EL JUGADOR: " + this.turns.get(i).getName());
                this.losers.add(this.turns.get(i));// lo agrego a la cola de perdedores
                if(this.deadHeatList.contains(this.turns.get(i))){ //elimino al perdedor de la lista de desempate si es que esta
                    this.deadHeatList.remove(this.turns.get(i));
                }
                this.turns.remove(this.turns.get(i));//elimino al perdedor de la lista de turnos
            }
        }

        //Verifico cuantos jugadores quedaron con cartas
        if(this.turns.size() == 1){
            this.gameWinner = this.turns.getFirst();
        }
    }

    /**
     * Devuelve el jugador con el turno actual.
     *
     * @return
     */
    public Player selectCurrentPlayer() {
        Player  p = this.turns.removeFirst();
        this.turns.addLast(p);
        return p;
    }

    public Player getRoundWinner(List<Player> gamePlayers, String attrib) {
        this.handleConfrontationEvent(gamePlayers);
        return this.confrontation.getWinnerRound(gamePlayers, this.deadHeatList, attrib,this.deck.getComparisonType(attrib));             /********/
    }

    public List<Player> getDeadHeadList() {
        return this.deadHeatList;
    }

    public List<Player> getLosers() {
        return this.losers;
    }

    private void tieBreakRound(String attrib) {
        /*Cada jugador empatado saca una carta*/
        List<Player> copiedDeadHeat = new Vector<>();
        for(Player p : this.deadHeatList ){
            p.takeCard();
            copiedDeadHeat.add(p);
        }
        this.handleTieBreakCardsSelection(this.deadHeatList);

        /*OJO CUANDO SE LLAMA CON this.deadHeatList como primer parametro en el desempate hay que hacer una copia de la lista
        ya que la funcion la modifica*/

        this.winner = this.getRoundWinner(copiedDeadHeat, attrib);
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

    private void handleTieBreakCardsSelection(List<Player> players) {
        setChanged();
        this.notifyObservers(new TieBreakCardsSelection(players));
    }

    private void handleDeadHeatRound() {
        setChanged();
        this.notifyObservers(new DeadHeatRound());
    }

    private void handleWinRound(Player winner) {
        setChanged();
        this.notifyObservers(new WinRound(winner));
    }

    private void handleConfrontationEvent(List<Player> players) {
        setChanged();
        this.notifyObservers(new ConfrontationEvent(players));
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

    public void tieBreak(){
        //Desempatar
        while (this.winner == null) {
            //this.handleDeadHeatRound();

            //Verificar que nadie se haya quedado sin cartas en la ronda

            this.checkPlayers();

            //Verificar que los empatados no hayan perdido en la verificacion de cartas
            if (this.deadHeatList.size() < 2) {
                if (this.deadHeatList.size() == 1) { // Se le entregan las cartas al único empatado que quedo
                    System.out.println("El ganador del desempate es: " + this.deadHeatList.get(0).getName());
                    this.handleWinRound(this.winner);
                    this.winner = this.deadHeatList.get(0);
                    this.winner.addAccumulatorWinner(this.currentAccumulatorDeck);
                    this.currentAccumulatorDeck.clear();

                }else { //Si pierden todos los empatados al mismo tiempo, el ganador de la proxima ronda se lleva
                    System.out.println("No hay ganador de la ronda: el proximo ganador se lleva el pozo");
                    this.handleWinRound(null);
                }

            } else {
                //Guardar en una lista local los jugadores que van a desempatar
                List<Player> localListTie = new ArrayList<>();
                for(Player p : this.deadHeatList){
                    localListTie.add(p);
                }

                //Realizar el desempate entre los empatados
                this.tieBreakRound(this.currentAttribute);

                //Si hay un ganador entregarle las cartas que se usaron para desempatar y el acumulador
                if(this.winner != null){
                    System.out.println("El ganador del desempate es : " + this.winner.getName());
                    this.handleWinRound(this.winner);
                    for(Player p : localListTie){
                        this.winner.giveCard(p.getCurrentCard());
                    }
                    this.winner.addAccumulatorWinner(this.currentAccumulatorDeck);
                    this.currentAccumulatorDeck.clear();
                    //Si no hay un ganador agregar las cartas usadas para desempatar al acumulador
                }else{
                    for(Player p : localListTie){
                        this.currentAccumulatorDeck.addCard(p.getCurrentCard());
                    }
                }
            }

        }
    }

    public void nextRound() {

       if (this.turns.size() >= CANT_MIN_PLAYERS) {
           /*Cada Jugador en turns (Jugadores en juego, tienen al menos una carta) Sacan una carta de su mazo*/

           for(Player p : this.turns)
               p.takeCard();

           this.currentPlayer = this.selectCurrentPlayer();

           this.handleShiftTurn(this.currentPlayer);

           this.handleCardsSelection(this.players);

           //El jugador actual elige el atributo*/
           this.currentPlayer.selectAttribute();
           System.out.println("Jugador Actual: " + this.currentPlayer.getName());
           this.currentAttribute = currentPlayer.nameCurrentAttribute();
           System.out.println("Atributo Elegido: " + this.currentAttribute);

           //Realizamos la comparacion entre las cartas de los jugadores
           this.winner = this.getRoundWinner(this.turns, this.currentAttribute);

           //Ver posible empate
           if(this.winner == null) {

               //Poner las cartas de los jugadores en el acumulador
               for (Player p : this.turns) {
                   this.currentAccumulatorDeck.addCard(p.getCurrentCard());
               }
               //Desempatar
               this.tieBreak();
           }else{
               System.out.println("El ganador de la ronda es : " + this.winner.getName());
               this.handleWinRound(this.winner);
               //Entregar las cartas al ganador
               for(Player p : this.turns){
                   this.winner.giveCard(p.getCurrentCard());
               }
               //Si hubo un pozo sin reclamar en la ronda anterior entregar tambien
               if(this.currentAccumulatorDeck.getNumberCards() > 0){
                   this.winner.addAccumulatorWinner(this.currentAccumulatorDeck);
                   this.currentAccumulatorDeck.clear();
               }
           }

           this.checkPlayers();//Verifico el estado de cada uno de los jugadores
           this.deadHeatList.clear();//Limpiamos la lista de empate
           this.winner = null;//Limpiamos el ganador de ronda
       }
       else
           //Hay un ganador del juego
           System.out.println("EL GANADOR DEL JUEGO ES: " + this.gameWinner.getName());
    }

    public List<Card> getCards() {
        return new Vector<Card>(this.cards.values());
    }

    public List<MainDeck> getDecks() {
        return this.decks;
    }

    public List<String> getAttributes(){
        return this.attributes;
    }

    public List<AbstractCharacter> getCharacters() {
        return new ArrayList<AbstractCharacter>(this.all.values());
    }

    public List<Character> getOnlyCharacters() {
        return new ArrayList<>(this.characters.values());
    }

    public void addPlayer(Player player) {
        this.players.add(player);
        this.turns.add(player);
    }

    public void createCard(AbstractCharacter character, List<String> selectedAttributes) {
        Card card = new Card(character);
        card.setAttributes(selectedAttributes);
        this.cards.put(String.valueOf(this.cards.size()), card);
    }

    public void createCharacter(String characterName, String realName, Map<String, Double> selectedAttributes) {
        Character character = new Character(characterName, realName);
        character.setAttributes(selectedAttributes);
        character.setId(this.all.size()+1);
        this.characters.put(String.valueOf(character.getId()),character);
        this.all.put(String.valueOf(character.getId()),character);
    }

    /*Repartimos el mazo acá o al iniciar startGame????*/
    public void createPlayers(List<String> playerNames, List<Strategy> strategies, MainDeck deck) {
        this.players.clear();
        this.turns.clear();
        this.deck = deck;

        List<DeckPlayer> decksPlayers = this.deck.share(playerNames.size());

        for (int i = 0; i < playerNames.size(); i++)
            this.addPlayer(new Player(
                    strategies.get(i),
                    playerNames.get(i),
                    decksPlayers.get(i))
            );
    }

    private void crearEstPrueba() {

        this.all = new Hashtable<String, AbstractCharacter>();
        this.cards = new Hashtable<String, Card>();
        this.decks = new ArrayList<MainDeck>();
        this.characters = new Hashtable<String, Character>();
        this.leagues = new LinkedHashMap<String, League>();
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
        p1.addAttribute("Fuerza", 2500.0);
        p1.addAttribute("Velocidad", 120.0);
        p1.addAttribute("Maldad", 0.0);
        p1.addAttribute("Destreza", 9.0);
        p1.addAttribute("Inteligencia", 90.0);
        p1.addAttribute("Peso", 110.0);
        p1.addAttribute("Bondad", 99.0);

        this.characters.put(String.valueOf(1),p1);
        this.all.put(String.valueOf(1),p1);
        Card c1 = new Card(p1);
        c1.addAttribute("Fuerza");
        c1.addAttribute("Velocidad");
        c1.addAttribute("Maldad");
        c1.addAttribute("Destreza");
        c1.addAttribute("Inteligencia");

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
        this.characters.put(String.valueOf(2),p2);
        this.all.put(String.valueOf(2),p2);
        Card c2 = new Card(p2);
        c2.addAttribute("Fuerza");
        c2.addAttribute("Velocidad");
        c2.addAttribute("Maldad");
        c2.addAttribute("Destreza");
        c2.addAttribute("Inteligencia");

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
        this.characters.put(String.valueOf(3),p3);
        this.all.put(String.valueOf(3),p3);
        Card c3 = new Card(p3);
        c3.addAttribute("Fuerza");
        c3.addAttribute("Velocidad");
        c3.addAttribute("Maldad");
        c3.addAttribute("Destreza");
        c3.addAttribute("Inteligencia");

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
        this.characters.put(String.valueOf(4),p4);
        this.all.put(String.valueOf(4),p4);
        Card c4 = new Card(p4);
        c4.addAttribute("Fuerza");
        c4.addAttribute("Velocidad");
        c4.addAttribute("Maldad");
        c4.addAttribute("Destreza");
        c4.addAttribute("Inteligencia");

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
        this.characters.put(String.valueOf(5),p5);
        this.all.put(String.valueOf(5),p5);
        Card c5 = new Card(p5);
        c5.addAttribute("Fuerza");
        c5.addAttribute("Velocidad");
        c5.addAttribute("Maldad");
        c5.addAttribute("Destreza");
        c5.addAttribute("Inteligencia");

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
        this.characters.put(String.valueOf(6),p6);
        this.all.put(String.valueOf(6),p6);
        Card c6 = new Card(p6);
        c6.addAttribute("Fuerza");
        c6.addAttribute("Velocidad");
        c6.addAttribute("Maldad");
        c6.addAttribute("Destreza");
        c6.addAttribute("Inteligencia");

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
        this.characters.put(String.valueOf(7),p7);
        this.all.put(String.valueOf(7),p7);
        Card c7 = new Card(p7);
        c7.addAttribute("Fuerza");
        c7.addAttribute("Velocidad");
        c7.addAttribute("Maldad");
        c7.addAttribute("Destreza");
        c7.addAttribute("Inteligencia");

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
        this.characters.put(String.valueOf(8),p8);
        this.all.put(String.valueOf(8),p8);
        Card c8 = new Card(p8);
        c8.addAttribute("Fuerza");
        c8.addAttribute("Velocidad");
        c8.addAttribute("Maldad");
        c8.addAttribute("Destreza");
        c8.addAttribute("Inteligencia");

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
        this.characters.put(String.valueOf(9),p9);
        this.all.put(String.valueOf(9),p9);
        Card c9 = new Card(p9);

        c9.addAttribute("Fuerza");
        c9.addAttribute("Velocidad");
        c9.addAttribute("Maldad");
        c9.addAttribute("Destreza");
        c9.addAttribute("Inteligencia");

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
        this.characters.put(String.valueOf(10),p10);
        this.all.put(String.valueOf(10),p10);
        Card c10 = new Card(p10);
        c10.addAttribute("Fuerza");
        c10.addAttribute("Velocidad");
        c10.addAttribute("Maldad");
        c10.addAttribute("Destreza");
        c10.addAttribute("Inteligencia");

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
        this.characters.put(String.valueOf(11),p11);
        this.all.put(String.valueOf(11),p11);
        Card c11 = new Card(p11);
        c11.addAttribute("Fuerza");
        c11.addAttribute("Velocidad");
        c11.addAttribute("Maldad");
        c11.addAttribute("Destreza");
        c11.addAttribute("Inteligencia");

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
        this.characters.put(String.valueOf(12),p12);
        this.all.put(String.valueOf(12),p12);
        Card c12 = new Card(p12);
        c12.addAttribute("Fuerza");
        c12.addAttribute("Velocidad");
        c12.addAttribute("Maldad");
        c12.addAttribute("Destreza");
        c12.addAttribute("Inteligencia");

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
        this.characters.put(String.valueOf(13),p13);
        this.all.put(String.valueOf(13),p13);
        Card c13 = new Card(p13);
        c13.addAttribute("Fuerza");
        c13.addAttribute("Velocidad");
        c13.addAttribute("Maldad");
        c13.addAttribute("Destreza");
        c13.addAttribute("Inteligencia");

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
        this.characters.put(String.valueOf(14),p14);
        this.all.put(String.valueOf(14),p14);
        Card c14 = new Card(p14);
        c14.addAttribute("Fuerza");
        c14.addAttribute("Velocidad");
        c14.addAttribute("Maldad");
        c14.addAttribute("Destreza");
        c14.addAttribute("Inteligencia");

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
        this.characters.put(String.valueOf(15),p15);
        this.all.put(String.valueOf(15),p15);
        Card c15 = new Card(p15);
        c15.addAttribute("Fuerza");
        c15.addAttribute("Velocidad");
        c15.addAttribute("Maldad");
        c15.addAttribute("Destreza");
        c15.addAttribute("Inteligencia");

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
        this.characters.put(String.valueOf(16),p16);
        this.all.put(String.valueOf(16),p16);
        Card c16 = new Card(p16);
        c16.addAttribute("Fuerza");
        c16.addAttribute("Velocidad");
        c16.addAttribute("Maldad");
        c16.addAttribute("Destreza");
        c16.addAttribute("Inteligencia");

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
        this.characters.put(String.valueOf(17),p17);
        this.all.put(String.valueOf(17),p17);
        Card c17 = new Card(p17);
        c17.addAttribute("Fuerza");
        c17.addAttribute("Velocidad");
        c17.addAttribute("Maldad");
        c17.addAttribute("Destreza");
        c17.addAttribute("Inteligencia");

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
        this.characters.put(String.valueOf(18),p18);
        this.all.put(String.valueOf(18),p18);
        Card c18 = new Card(p18);
        c18.addAttribute("Fuerza");
        c18.addAttribute("Velocidad");
        c18.addAttribute("Maldad");
        c18.addAttribute("Destreza");
        c18.addAttribute("Inteligencia");

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
        this.characters.put(String.valueOf(19),p19);
        this.all.put(String.valueOf(19),p19);
        Card c19 = new Card(p19);
        c19.addAttribute("Fuerza");
        c19.addAttribute("Velocidad");
        c19.addAttribute("Maldad");
        c19.addAttribute("Destreza");
        c19.addAttribute("Inteligencia");

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
        this.characters.put(String.valueOf(20),p20);
        this.all.put(String.valueOf(20),p20);
        Card c20 = new Card(p20);
        c20.addAttribute("Fuerza");
        c20.addAttribute("Velocidad");
        c20.addAttribute("Maldad");
        c20.addAttribute("Destreza");
        c20.addAttribute("Inteligencia");

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
        this.characters.put(String.valueOf(21),p21);
        this.all.put(String.valueOf(21),p21);
        Card c21 = new Card(p21);
        c21.addAttribute("Fuerza");
        c21.addAttribute("Velocidad");
        c21.addAttribute("Maldad");
        c21.addAttribute("Destreza");
        c21.addAttribute("Inteligencia");

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
        this.characters.put(String.valueOf(22),p22);
        this.all.put(String.valueOf(22),p22);
        Card c22 = new Card(p22);
        c22.addAttribute("Fuerza");
        c22.addAttribute("Velocidad");
        c22.addAttribute("Maldad");
        c22.addAttribute("Destreza");
        c22.addAttribute("Inteligencia");

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
        this.characters.put(String.valueOf(23),p23);
        this.all.put(String.valueOf(23),p23);
        Card c23 = new Card(p23);
        c23.addAttribute("Fuerza");
        c23.addAttribute("Velocidad");
        c23.addAttribute("Maldad");
        c23.addAttribute("Destreza");
        c23.addAttribute("Inteligencia");

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
        this.characters.put(String.valueOf(24),p24);
        this.all.put(String.valueOf(24),p24);
        Card c24 = new Card(p24);
        c24.addAttribute("Fuerza");
        c24.addAttribute("Velocidad");
        c24.addAttribute("Maldad");
        c24.addAttribute("Destreza");
        c24.addAttribute("Inteligencia");

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
        this.characters.put(String.valueOf(25),p25);
        this.all.put(String.valueOf(25),p25);
        Card c25 = new Card(p25);
        c25.addAttribute("Fuerza");
        c25.addAttribute("Velocidad");
        c25.addAttribute("Maldad");
        c25.addAttribute("Destreza");
        c25.addAttribute("Inteligencia");

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
        this.characters.put(String.valueOf(26),p26);
        this.all.put(String.valueOf(26),p26);
        Card c26 = new Card(p26);
        c26.addAttribute("Fuerza");
        c26.addAttribute("Velocidad");
        c26.addAttribute("Maldad");
        c26.addAttribute("Destreza");
        c26.addAttribute("Inteligencia");

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
        this.characters.put(String.valueOf(27),p27);
        this.all.put(String.valueOf(27),p27);
        Card c27 = new Card(p27);
        c27.addAttribute("Fuerza");
        c27.addAttribute("Velocidad");
        c27.addAttribute("Maldad");
        c27.addAttribute("Destreza");
        c27.addAttribute("Inteligencia");

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
        this.characters.put(String.valueOf(28),p28);
        this.all.put(String.valueOf(28),p28);
        Card c28 = new Card(p28);
        c28.addAttribute("Fuerza");
        c28.addAttribute("Velocidad");
        c28.addAttribute("Maldad");
        c28.addAttribute("Destreza");
        c28.addAttribute("Inteligencia");

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
        this.characters.put(String.valueOf(29),p29);
        this.all.put(String.valueOf(29),p29);
        Card c29 = new Card(p29);
        c29.addAttribute("Fuerza");
        c29.addAttribute("Velocidad");
        c29.addAttribute("Maldad");
        c29.addAttribute("Destreza");
        c29.addAttribute("Inteligencia");

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
        this.characters.put(String.valueOf(30),p30);
        this.all.put(String.valueOf(30),p30);
        Card c30 = new Card(p30);
        c30.addAttribute("Fuerza");
        c30.addAttribute("Velocidad");
        c30.addAttribute("Maldad");
        c30.addAttribute("Destreza");
        c30.addAttribute("Inteligencia");

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
        this.characters.put(String.valueOf(31),p31);
        this.all.put(String.valueOf(31),p31);
        Card c31 = new Card(p31);
        c31.addAttribute("Fuerza");
        c31.addAttribute("Velocidad");
        c31.addAttribute("Maldad");
        c31.addAttribute("Destreza");
        c31.addAttribute("Inteligencia");

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
        this.characters.put(String.valueOf(32),p32);
        this.all.put(String.valueOf(32),p32);
        Card c32 = new Card(p32);
        c32.addAttribute("Fuerza");
        c32.addAttribute("Velocidad");
        c32.addAttribute("Maldad");
        c32.addAttribute("Destreza");
        c32.addAttribute("Inteligencia");

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
        this.characters.put(String.valueOf(33),p33);
        this.all.put(String.valueOf(33),p33);
        Card c33 = new Card(p33);
        c33.addAttribute("Fuerza");
        c33.addAttribute("Velocidad");
        c33.addAttribute("Maldad");
        c33.addAttribute("Destreza");
        c33.addAttribute("Inteligencia");

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
        this.characters.put(String.valueOf(34),p34);
        this.all.put(String.valueOf(34),p34);
        Card c34 = new Card(p34);
        c34.addAttribute("Fuerza");
        c34.addAttribute("Velocidad");
        c34.addAttribute("Maldad");
        c34.addAttribute("Destreza");
        c34.addAttribute("Inteligencia");

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
        this.characters.put(String.valueOf(35),p35);
        this.all.put(String.valueOf(35),p35);
        Card c35 = new Card(p35);
        c35.addAttribute("Fuerza");
        c35.addAttribute("Velocidad");
        c35.addAttribute("Maldad");
        c35.addAttribute("Destreza");
        c35.addAttribute("Inteligencia");

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
        this.characters.put(String.valueOf(36),p36);
        this.all.put(String.valueOf(36),p36);
        Card c36 = new Card(p36);
        c36.addAttribute("Fuerza");
        c36.addAttribute("Velocidad");
        c36.addAttribute("Maldad");
        c36.addAttribute("Destreza");
        c36.addAttribute("Inteligencia");

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
        this.characters.put(String.valueOf(37),p37);
        this.all.put(String.valueOf(37),p37);
        Card c37 = new Card(p37);
        c37.addAttribute("Fuerza");
        c37.addAttribute("Velocidad");
        c37.addAttribute("Maldad");
        c37.addAttribute("Destreza");
        c37.addAttribute("Inteligencia");

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
        this.characters.put(String.valueOf(38),p38);
        this.all.put(String.valueOf(38),p38);
        Card c38 = new Card(p38);
        c38.addAttribute("Fuerza");
        c38.addAttribute("Velocidad");
        c38.addAttribute("Maldad");
        c38.addAttribute("Destreza");
        c38.addAttribute("Inteligencia");

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
        this.characters.put(String.valueOf(39),p39);
        this.all.put(String.valueOf(39),p39);
        Card c39 = new Card(p39);
        c39.addAttribute("Fuerza");
        c39.addAttribute("Velocidad");
        c39.addAttribute("Maldad");
        c39.addAttribute("Destreza");
        c39.addAttribute("Inteligencia");

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
        this.characters.put(String.valueOf(40),p40);
        this.all.put(String.valueOf(40),p40);
        Card c40 = new Card(p40);
        c40.addAttribute("Fuerza");
        c40.addAttribute("Velocidad");
        c40.addAttribute("Maldad");
        c40.addAttribute("Destreza");
        c40.addAttribute("Inteligencia");

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
        this.characters.put(String.valueOf(41),p41);
        this.all.put(String.valueOf(41),p41);
        Card c41 = new Card(p41);
        c41.addAttribute("Fuerza");
        c41.addAttribute("Velocidad");
        c41.addAttribute("Maldad");
        c41.addAttribute("Destreza");
        c41.addAttribute("Inteligencia");

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
        this.characters.put(String.valueOf(42),p42);
        this.all.put(String.valueOf(42),p42);
        Card c42 = new Card(p42);
        c42.addAttribute("Fuerza");
        c42.addAttribute("Velocidad");
        c42.addAttribute("Maldad");
        c42.addAttribute("Destreza");
        c42.addAttribute("Inteligencia");

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
        this.characters.put(String.valueOf(43),p43);
        this.all.put(String.valueOf(43),p43);
        Card c43 = new Card(p43);
        c43.addAttribute("Fuerza");
        c43.addAttribute("Velocidad");
        c43.addAttribute("Maldad");
        c43.addAttribute("Destreza");
        c43.addAttribute("Inteligencia");

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
        this.characters.put(String.valueOf(44),p44);
        this.all.put(String.valueOf(44),p44);
        Card c44 = new Card(p44);
        c44.addAttribute("Fuerza");
        c44.addAttribute("Velocidad");
        c44.addAttribute("Maldad");
        c44.addAttribute("Destreza");
        c44.addAttribute("Inteligencia");

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
        this.characters.put(String.valueOf(45),p45);
        this.all.put(String.valueOf(45),p45);
        Card c45 = new Card(p45);
        c45.addAttribute("Fuerza");
        c45.addAttribute("Velocidad");
        c45.addAttribute("Maldad");
        c45.addAttribute("Destreza");
        c45.addAttribute("Inteligencia");

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
        this.characters.put(String.valueOf(46),p46);
        this.all.put(String.valueOf(46),p46);
        Card c46 = new Card(p46);
        c46.addAttribute("Fuerza");
        c46.addAttribute("Velocidad");
        c46.addAttribute("Maldad");
        c46.addAttribute("Destreza");
        c46.addAttribute("Inteligencia");

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
        this.characters.put(String.valueOf(47),p47);
        this.all.put(String.valueOf(47),p47);
        Card c47 = new Card(p47);
        c47.addAttribute("Fuerza");
        c47.addAttribute("Velocidad");
        c47.addAttribute("Maldad");
        c47.addAttribute("Destreza");
        c47.addAttribute("Inteligencia");

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
        this.characters.put(String.valueOf(48),p48);
        this.all.put(String.valueOf(48),p48);
        Card c48 = new Card(p48);
        c48.addAttribute("Fuerza");
        c48.addAttribute("Velocidad");
        c48.addAttribute("Maldad");
        c48.addAttribute("Destreza");
        c48.addAttribute("Inteligencia");

        this.cards.put(String.valueOf(48), c48);

        /**LIGAS**/
        League l1 = new League("Los 4 Fantasticos");
        l1.addCharacter(p33);
        l1.addCharacter(p34);
        l1.addCharacter(p35);
        l1.addCharacter(p36);
        this.leagues.put(String.valueOf(49),l1);
        this.all.put(String.valueOf(49),l1);
        Card c49 = new Card(l1);
        c49.addAttribute("Fuerza");
        c49.addAttribute("Velocidad");
        c49.addAttribute("Maldad");
        c49.addAttribute("Destreza");
        c49.addAttribute("Inteligencia");

        this.cards.put(String.valueOf(49),c49);

        League l2 = new League("X-Men");
        l2.addCharacter(p26);
        l2.addCharacter(p27);
        l2.addCharacter(p28);
        l2.addCharacter(p29);
        l2.addCharacter(p30);
        this.leagues.put(String.valueOf(50),l2);
        this.all.put(String.valueOf(50),l2);
        Card c50 = new Card(l2);
        c50.addAttribute("Fuerza");
        c50.addAttribute("Velocidad");
        c50.addAttribute("Maldad");
        c50.addAttribute("Destreza");
        c50.addAttribute("Inteligencia");
        this.cards.put(String.valueOf(50),c50);

        League l3 = new League("Batman y Robin");
        l3.addCharacter(p2);
        l3.addCharacter(p6);
        this.leagues.put(String.valueOf(51),l3);
        this.all.put(String.valueOf(51),l3);
        Card c51 = new Card(l3);
        c51.addAttribute("Fuerza");
        c51.addAttribute("Velocidad");
        c51.addAttribute("Maldad");
        c51.addAttribute("Destreza");
        c51.addAttribute("Inteligencia");
        this.cards.put(String.valueOf(51),c51);

        League l4 = new League("Los Super Amigos");
        l4.addCharacter(l3);
        l4.addCharacter(p1);
        l4.addCharacter(p3);
        l4.addCharacter(p4);
        l4.addCharacter(p7);
        l4.addCharacter(p8);
        l4.addCharacter(p9);
        this.leagues.put(String.valueOf(52),l4);
        this.all.put(String.valueOf(52),l4);
        Card c52 = new Card(l4);
        c52.addAttribute("Fuerza");
        c52.addAttribute("Velocidad");
        c52.addAttribute("Maldad");
        c52.addAttribute("Destreza");
        c52.addAttribute("Inteligencia");
        this.cards.put(String.valueOf(52),c52);

        League l5 = new League("SuperAmigosFantasticos");
        l5.addCharacter(l4);
        l5.addCharacter(l1);
        this.leagues.put(String.valueOf(53),l5);
        this.all.put(String.valueOf(53),l5);
        Card c53 = new Card(l5);
        c53.addAttribute("Fuerza");
        c53.addAttribute("Velocidad");
        c53.addAttribute("Maldad");
        c53.addAttribute("Destreza");
        c53.addAttribute("Inteligencia");
        this.cards.put(String.valueOf(53),c53);


        /**MAZOS**/
        MainDeck d1 = new MainDeck("Los Campeones 1");
        d1.addAttribute("Fuerza",true);
        d1.addAttribute("Velocidad",true);
        d1.addAttribute("Maldad",true);
        d1.addAttribute("Destreza",true);
        d1.addAttribute("Inteligencia",true);
        d1.addCard(c1);
        d1.addCard(c2);
        d1.addCard(c3);
        d1.addCard(c4);
        d1.addCard(c5);
        d1.addCard(c6);
        d1.addCard(c7);
        d1.addCard(c8);
        d1.addCard(c9);
        d1.addCard(c10);
        d1.addCard(c11);
        d1.addCard(c12);

        this.decks.add(d1);

        MainDeck d2 = new MainDeck("Los Campeones 2");
        d2.addAttribute("Fuerza",true);
        d2.addAttribute("Velocidad",true);
        d2.addAttribute("Maldad",true);
        d2.addAttribute("Destreza",true);
        d2.addAttribute("Inteligencia",true);
        d2.addCard(c13);
        d2.addCard(c14);
        d2.addCard(c15);
        d2.addCard(c16);
        d2.addCard(c17);
        d2.addCard(c18);
        d2.addCard(c19);
        d2.addCard(c20);
        d2.addCard(c21);
        d2.addCard(c22);
        d2.addCard(c23);
        d2.addCard(c24);

        this.decks.add(d2);

        MainDeck d3 = new MainDeck("Los Campeones 3");
        d3.addAttribute("Fuerza",true);
        d3.addAttribute("Velocidad",true);
        d3.addAttribute("Maldad",true);
        d3.addAttribute("Destreza",true);
        d3.addAttribute("Inteligencia",true);
        d3.addCard(c25);
        d3.addCard(c26);
        d3.addCard(c27);
        d3.addCard(c28);
        d3.addCard(c29);
        d3.addCard(c30);
        d3.addCard(c31);
        d3.addCard(c32);
        d3.addCard(c33);
        d3.addCard(c34);
        d3.addCard(c35);
        d3.addCard(c36);

        this.decks.add(d3);

        MainDeck d4 = new MainDeck("Los Campeones 4");
        d4.addAttribute("Fuerza",true);
        d4.addAttribute("Velocidad",true);
        d4.addAttribute("Maldad",true);
        d4.addAttribute("Destreza",true);
        d4.addAttribute("Inteligencia",true);
        d4.addCard(c37);
        d4.addCard(c38);
        d4.addCard(c39);
        d4.addCard(c40);
        d4.addCard(c41);
        d4.addCard(c42);
        d4.addCard(c43);
        d4.addCard(c44);
        d4.addCard(c45);
        d4.addCard(c46);
        d4.addCard(c47);
        d4.addCard(c48);

        this.decks.add(d4);


    }

}