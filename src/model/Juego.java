package model;

import java.util.*;

import com.thoughtworks.xstream.*;
import controller.events.*;

public class Juego extends Observable {

    private static final String CARDS_PATH = "resources/data/cards/";
    private static final String DECKS_PATH = "resources/data/decks/";

    private Boolean hayEmpate;
    private XStream xstream;
    private Queue<Jugador> turnos;
    private Mazo mazo;
    private List<Jugador> jugadores = new Vector<>();
    private Jugador ganador;
    private List<Jugador> empatados;
    private List<Jugador> perdedores;
    private List<Mazo> mazos;
    private Hashtable<String, Carta> cartas;
    private Enfrentamiento enfrentamiento;
    private InstanceDirector director;

    private static final String cardsDirectory = "";
    private static final String decksDirecory = "";

    private String currentAttribute;
    private MazoJugador currentAccumulatorDeck;

    //serializador de datos
    private DataParser dpFile;

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    private void verificarJugadores() {
        for (int i = 0; i < this.jugadores.size(); i++) {
            if (this.jugadores.get(i).cantCartasJugador() == 0) {
                this.notifyObservers(this.jugadores.get(i));// notifico el jugador que perdio, y se lo envio al observador
                this.perdedores.add(this.jugadores.remove(i));// lo agrego a la cola de perdedores
            }
        }
    }

    /**
     * Devuelve el jugador con el turno actual.
     *
     * @return
     */
    public Jugador currentPlayer() {
        return this.turnos.remove();
    }

    public Jugador getGanadorRonda(List<Jugador> jugadoresEnJuego, String atrib) {
        return this.enfrentamiento.obtenerGanadorDuelo(jugadoresEnJuego, this.empatados, atrib);
    }

    public List<Jugador> getEmpatados() {
        return empatados;
    }

    public List<Jugador> getPerdedores() {
        return perdedores;
    }

    private Jugador desempatarRonda(String atrib, MazoJugador mazoEnJuego) {
        this.handleCardsSelection(this.empatados);
        this.ganador = this.getGanadorRonda(this.empatados, atrib);//obtengo lso ganadores del la ronda desempate

        //El juego sigue en empate
        if (this.ganador == null) {
            //return this.desempatarRonda(atrib, mazoEnJuego);
        }

        return this.ganador;

    }

    /**
     * Métodos de eventos.
     */
    private void handleDeckList(List<Mazo> decks) {
        setChanged();
        this.notifyObservers(new ListDecks(decks));
    }

    private void handleCardsList(List<Carta> cards) {
        setChanged();
        this.notifyObservers(new ListCards(cards));
    }


    private void handleShiftTurn(Jugador currentPlayer) {
        setChanged();
        this.notifyObservers(new ShiftTurn(currentPlayer));// me devuelve el jugador con el atributo que selecciono en la VISTA...metodos: elegirAtributo() y obtenerAtributo(),
    }

    private void handleCardsSelection(List<Jugador> players) {
        setChanged();
        this.notifyObservers(new CardsSelection(players));
    }

    private void handleDeadHeatRound() {
        setChanged();
        this.notifyObservers(new DeadHeatRound());
    }

    private void handleWinRound(Jugador winner, MazoJugador accumulator) {
        setChanged();
        this.notifyObservers(new WinRound(winner, accumulator));
    }

    /**
     * Recepción de eventos
     */

    public void receiveSelectedAttribute(String attr) {
        this.currentAttribute = attr;
    }

    public void receiveAccumulatorDeck(MazoJugador accumulator) {
        this.currentAccumulatorDeck = accumulator;
    }

    /**
     * Método de comienzo de partida.
     */
    public void comenzarPartida() {

        while (this.turnos.size() == 1) {
            this.handleShiftTurn(this.currentPlayer());
            // notifico el comienzo de la seleccion de cartas de cada uno de los jugadores en juego
            this.handleCardsSelection(this.jugadores);

            this.ganador = this.getGanadorRonda(this.jugadores, this.currentAttribute);

            //Verifico si la ronda esta empatada.
            if (this.ganador == null) {
                // notifico a la VISTA que se produjo un empate
                this.handleDeadHeatRound();
                this.ganador = this.desempatarRonda(this.currentAttribute, this.currentAccumulatorDeck);
            }

            //Notifico quién es el ganador de la ronda.
            this.handleWinRound(this.ganador, this.currentAccumulatorDeck);

            //Reintegro el jugador actual al final de la cola de turnos.
            this.turnos.add(this.currentPlayer());

            //actualizo seteo cada una de las estructuras involucradas en la partida
            this.empatados.clear();
            this.ganador = null;
            this.currentAccumulatorDeck.vaciar();

            this.verificarJugadores();//verifico el estado de cada uno de los jugadores
            //actualizo VISTA con informacion de la partida
            this.notifyObservers(this.jugadores);//notifico al la VISTA de la actualizacion
        }
    }

    public List<Carta> getCards() {
        return new Vector<Carta>(this.cartas.values());
    }

    public List<Mazo> getDecks() {
        return this.mazos;
    }

    public Juego() {

        /**Creo cartas y mazos**/
        this.director = new model.InstanceDirector(new model.HeroesBuilder());
        this.cartas = this.director.getCartas();
        this.mazos = this.director.getMazos(this.cartas);

        /**imprimir mazos**/

        /**creo las cartas y un mazo**/
        this.crearEstPrueba();


        //
        //
        //
        //      LOGICA DEL JUEGO
        //
        //
        //

        /**Guardar Datos**/
        	this.guardarDatos();


        /***************************************************IMPRESIONES**********************************************************/
        //imprimir cartas
	/*	
		for(int i = 1; i < 48 ; i++){
			System.out.println("Id de la carta: "+this.cartas.get(String.valueOf(i)).getId());
		}
		
		//obtengo mazo y le pido las cartas que tiene
		
		model.Mazo m = this.mazos.get(0);
		List<model.Carta> cart = m.getCartas();
		System.out.println("\nNombre de mazo: "+m.getNombre());
		for(model.Carta c : cart){
			System.out.println("model.Carta: "+ c.getId());
			System.out.println("Atributo Fuerza: "+ c.getAtributo("Fuerza"));
		}
		
		System.out.println("Atributos de mazo: ");
		for(String c : m.getAtributos()){
			System.out.println(c);
		}
	*/

        for(Mazo m : this.mazos){
            System.out.println("MAZO: " + m.getNombre());
            int i = 1;
            for(Carta c : m.getCartas()){
                System.out.println(c.getPersonaje().getNombreFicticio() + " numero: " + i);
                i++;
            }
        }


    }

        /**Guardar Masos**/
    public void guardarDatos(){

        //**serializador**/
        this.dpFile = new XMLDataParser(new XStream());

        /**Guardar Cartas**/
        for(int i = 1; i <= this.cartas.size(); i++){
            Carta m = this.cartas.get(String.valueOf(i));
            m.setId(i);
            this.dpFile.saveData(this.CARDS_PATH,String.valueOf(i),m);
        }


        /**Guardar Masos**/
        List<String> nombres = new ArrayList<>();
        for(Mazo m : this.mazos){
            nombres.add(m.getNombre());
            this.dpFile.saveData(this.DECKS_PATH,m.getNombre(),new MazoSave(m.getNombre(),this.getIds(m),m.getAtrib()));
        }

        /**Guardo listado de nombre de mazos**/
        this.dpFile.saveData(this.DECKS_PATH,"nombresMazos",nombres);


    }


    private List<String> getIds(Mazo m){
        List<String> ids = new ArrayList<String>();
        List<Carta> cartas = m.getCartas();

        for(Carta c : cartas)
        {
            String id = String.valueOf(c.getId());
            ids.add(id);
        }

        return ids;
    }


    public void agregarJugador(Jugador player) {
        this.jugadores.add(player);
    }

    public void createPlayers(List<String> playerNames, List<Boolean> managedManually, Estrategia selectedStrategy, Mazo deck) {
        this.mazo = deck;

        for (int i = 0; i < playerNames.size(); i++) {

            Estrategia strategy = (managedManually.get(i))
                    ? new ManualStrategy()
                    : selectedStrategy;


            this.agregarJugador(
                    new Jugador(
                            strategy,
                            playerNames.get(i),
                            new MazoJugador()
                    )
            );
        }
    }


    private void crearEstPrueba() {

        this.cartas = new Hashtable<String, Carta>();
        this.mazos = new ArrayList<Mazo>();

        //creo cartas...

        /**CARTA N°1**/
        Personaje p1 = new Personaje("Superman", "Clark Kent","resources/images.old/superman.png");
        p1.agregarAtributo("Fuerza", 2000.0);
        p1.agregarAtributo("Velocidad", 400.0);
        p1.agregarAtributo("Maldad", 0.0);
        p1.agregarAtributo("Destreza", 7.0);
        p1.agregarAtributo("Inteligencia", 80.0);
        p1.agregarAtributo("Peso", 110.0);
        p1.agregarAtributo("Bondad", 100.0);
        Carta c1 = new Carta(p1);
        c1.agregarAtributo("Fuerza");
        c1.agregarAtributo("Velocidad");
        c1.agregarAtributo("Maldad");
        c1.agregarAtributo("Destreza");
        c1.agregarAtributo("Inteligencia");
        c1.agregarAtributo("Peso");
        c1.agregarAtributo("Bondad");

        this.cartas.put(String.valueOf(1), c1);

        /**CARTA N°2**/
        Personaje p2 = new Personaje("Batman", "Bruce Wane","resources/images.old/batman.png");
        p2.agregarAtributo("Fuerza", 400.0);
        p2.agregarAtributo("Velocidad", 85.0);
        p2.agregarAtributo("Maldad", 10.0);
        p2.agregarAtributo("Destreza", 8.0);
        p2.agregarAtributo("Inteligencia", 85.0);
        p2.agregarAtributo("Peso", 106.0);
        p2.agregarAtributo("Bondad", 70.0);
        Carta c2 = new Carta(p2);
        c2.agregarAtributo("Fuerza");
        c2.agregarAtributo("Velocidad");
        c2.agregarAtributo("Maldad");
        c2.agregarAtributo("Destreza");
        c2.agregarAtributo("Inteligencia");
        c2.agregarAtributo("Peso");
        c2.agregarAtributo("Bondad");

        this.cartas.put(String.valueOf(2), c2);

        /**CARTA N°3**/
        Personaje p3 = new Personaje("Flash", "Jay Garrick","resources/images.old/flash.png");
        p3.agregarAtributo("Fuerza", 840.0);
        p3.agregarAtributo("Velocidad", 800000.0);
        p3.agregarAtributo("Maldad", 0.0);
        p3.agregarAtributo("Destreza", 9.0);
        p3.agregarAtributo("Inteligencia", 75.5);
        p3.agregarAtributo("Peso", 90.0);
        p3.agregarAtributo("Bondad", 95.5);
        Carta c3 = new Carta(p3);
        c3.agregarAtributo("Fuerza");
        c3.agregarAtributo("Velocidad");
        c3.agregarAtributo("Maldad");
        c3.agregarAtributo("Destreza");
        c3.agregarAtributo("Inteligencia");
        c3.agregarAtributo("Peso");
        c3.agregarAtributo("Bondad");

        this.cartas.put(String.valueOf(3), c3);

        /**CARTA N°4**/
        Personaje p4 = new Personaje("Mujer Maravilla", "Princesa Diana","resources/images.old/mujer_maravilla.png");
        p4.agregarAtributo("Fuerza", 830.0);
        p4.agregarAtributo("Velocidad", 70.0);
        p4.agregarAtributo("Maldad", 0.0);
        p4.agregarAtributo("Destreza", 9.5);
        p4.agregarAtributo("Inteligencia", 90.0);
        p4.agregarAtributo("Peso", 62.0);
        p4.agregarAtributo("Bondad", 97.0);
        Carta c4 = new Carta(p4);
        c4.agregarAtributo("Fuerza");
        c4.agregarAtributo("Velocidad");
        c4.agregarAtributo("Maldad");
        c4.agregarAtributo("Destreza");
        c4.agregarAtributo("Inteligencia");
        c4.agregarAtributo("Peso");
        c4.agregarAtributo("Bondad");

        this.cartas.put(String.valueOf(4), c4);

        /**CARTA N°5**/
        Personaje p5 = new Personaje("Batichica", "Betty Kane","resources/images.old/batichica.png");
        p5.agregarAtributo("Fuerza", 85.0);
        p5.agregarAtributo("Velocidad", 50.0);
        p5.agregarAtributo("Maldad", 8.0);
        p5.agregarAtributo("Destreza", 8.0);
        p5.agregarAtributo("Inteligencia", 90.0);
        p5.agregarAtributo("Peso", 60.0);
        p5.agregarAtributo("Bondad", 97.5);
        Carta c5 = new Carta(p5);
        c5.agregarAtributo("Fuerza");
        c5.agregarAtributo("Velocidad");
        c5.agregarAtributo("Maldad");
        c5.agregarAtributo("Destreza");
        c5.agregarAtributo("Inteligencia");
        c5.agregarAtributo("Peso");
        c5.agregarAtributo("Bondad");

        this.cartas.put(String.valueOf(5), c5);

        /**CARTA N°6**/
        Personaje p6 = new Personaje("Robin", "Dick Grayson","resources/images.old/robin.png");
        p6.agregarAtributo("Fuerza", 200.0);
        p6.agregarAtributo("Velocidad", 80.0);
        p6.agregarAtributo("Maldad", 0.0);
        p6.agregarAtributo("Destreza", 8.5);
        p6.agregarAtributo("Inteligencia", 80.0);
        p6.agregarAtributo("Peso", 65.0);
        p6.agregarAtributo("Bondad", 98.0);
        Carta c6 = new Carta(p6);
        c6.agregarAtributo("Fuerza");
        c6.agregarAtributo("Velocidad");
        c6.agregarAtributo("Maldad");
        c6.agregarAtributo("Destreza");
        c6.agregarAtributo("Inteligencia");
        c6.agregarAtributo("Peso");
        c6.agregarAtributo("Bondad");

        this.cartas.put(String.valueOf(6), c6);

        /**CARTA N°7**/
        Personaje p7 = new Personaje("Chica Halcon", "Shiera Sanders","resources/images.old/chica_halcon.png");
        p7.agregarAtributo("Fuerza", 340.0);
        p7.agregarAtributo("Velocidad", 300.0);
        p7.agregarAtributo("Maldad", 0.0);
        p7.agregarAtributo("Destreza", 8.5);
        p7.agregarAtributo("Inteligencia", 95.5);
        p7.agregarAtributo("Peso", 55.0);
        p7.agregarAtributo("Bondad", 97.0);
        Carta c7 = new Carta(p7);
        c7.agregarAtributo("Fuerza");
        c7.agregarAtributo("Velocidad");
        c7.agregarAtributo("Maldad");
        c7.agregarAtributo("Destreza");
        c7.agregarAtributo("Inteligencia");
        c7.agregarAtributo("Peso");
        c7.agregarAtributo("Bondad");

        this.cartas.put(String.valueOf(7), c7);

        /**CARTA N°8**/
        Personaje p8 = new Personaje("Linterna Verde", "Alan Scott","resources/images.old/linterna_verde.png");
        p8.agregarAtributo("Fuerza", 830.0);
        p8.agregarAtributo("Velocidad", 340.0);
        p8.agregarAtributo("Maldad", 0.0);
        p8.agregarAtributo("Destreza", 7.5);
        p8.agregarAtributo("Inteligencia", 90.0);
        p8.agregarAtributo("Peso", 99.0);
        p8.agregarAtributo("Bondad", 98.5);
        Carta c8 = new Carta(p8);
        c8.agregarAtributo("Fuerza");
        c8.agregarAtributo("Velocidad");
        c8.agregarAtributo("Maldad");
        c8.agregarAtributo("Destreza");
        c8.agregarAtributo("Inteligencia");
        c8.agregarAtributo("Peso");
        c8.agregarAtributo("Bondad");

        this.cartas.put(String.valueOf(8), c8);

        /**CARTA N°9**/
        Personaje p9 = new Personaje("Aquaman", "Arthur Curry","resources/images.old/aquaman.png");
        p9.agregarAtributo("Fuerza", 700.0);
        p9.agregarAtributo("Velocidad", 220.0);
        p9.agregarAtributo("Maldad", 0.0);
        p9.agregarAtributo("Destreza", 9.5);
        p9.agregarAtributo("Inteligencia", 94.0);
        p9.agregarAtributo("Peso", 86.0);
        p9.agregarAtributo("Bondad", 99.0);
        Carta c9 = new Carta(p9);
        c9.agregarAtributo("Fuerza");
        c9.agregarAtributo("Velocidad");
        c9.agregarAtributo("Maldad");
        c9.agregarAtributo("Destreza");
        c9.agregarAtributo("Inteligencia");
        c9.agregarAtributo("Peso");
        c9.agregarAtributo("Bondad");

        this.cartas.put(String.valueOf(9), c9);

        /**CARTA N°10**/
        Personaje p10 = new Personaje("Flecha Verde", "Oliver Jonas Queen","resources/images.old/flecha_verde.png");
        p10.agregarAtributo("Fuerza", 710.0);
        p10.agregarAtributo("Velocidad", 220.0);
        p10.agregarAtributo("Maldad", 0.0);
        p10.agregarAtributo("Destreza", 8.5);
        p10.agregarAtributo("Inteligencia", 95.5);
        p10.agregarAtributo("Peso", 86.5);
        p10.agregarAtributo("Bondad", 98.5);
        Carta c10 = new Carta(p10);
        c10.agregarAtributo("Fuerza");
        c10.agregarAtributo("Velocidad");
        c10.agregarAtributo("Maldad");
        c10.agregarAtributo("Destreza");
        c10.agregarAtributo("Inteligencia");
        c10.agregarAtributo("Peso");
        c10.agregarAtributo("Bondad");

        this.cartas.put(String.valueOf(10), c10);

        /**CARTA N°11**/
        Personaje p11 = new Personaje("El Atomo", "Al Pratt","resources/images.old/el_atomo.png");
        p11.agregarAtributo("Fuerza", 800.0);
        p11.agregarAtributo("Velocidad", 110.0);
        p11.agregarAtributo("Maldad", 0.0);
        p11.agregarAtributo("Destreza", 9.0);
        p11.agregarAtributo("Inteligencia", 95.5);
        p11.agregarAtributo("Peso", 1.5);
        p11.agregarAtributo("Bondad", 99.0);
        Carta c11 = new Carta(p11);
        c11.agregarAtributo("Fuerza");
        c11.agregarAtributo("Velocidad");
        c11.agregarAtributo("Maldad");
        c11.agregarAtributo("Destreza");
        c11.agregarAtributo("Inteligencia");
        c11.agregarAtributo("Peso");
        c11.agregarAtributo("Bondad");

        this.cartas.put(String.valueOf(11), c11);

        /**CARTA N°12**/
        Personaje p12 = new Personaje("Canario Negro", "Dinah Drake","resources/images.old/canario_negro.png");
        p12.agregarAtributo("Fuerza", 100.0);
        p12.agregarAtributo("Velocidad", 110.0);
        p12.agregarAtributo("Maldad", 2.0);
        p12.agregarAtributo("Destreza", 9.5);
        p12.agregarAtributo("Inteligencia", 92.0);
        p12.agregarAtributo("Peso", 55.0);
        p12.agregarAtributo("Bondad", 96.0);
        Carta c12 = new Carta(p12);
        c12.agregarAtributo("Fuerza");
        c12.agregarAtributo("Velocidad");
        c12.agregarAtributo("Maldad");
        c12.agregarAtributo("Destreza");
        c12.agregarAtributo("Inteligencia");
        c12.agregarAtributo("Peso");
        c12.agregarAtributo("Bondad");

        this.cartas.put(String.valueOf(12), c12);

        /**CARTA N°13**/
        Personaje p13 = new Personaje("Manhunter", "Dan Richards","resources/images.old/manhunter.png");
        p13.agregarAtributo("Fuerza", 400.0);
        p13.agregarAtributo("Velocidad", 85.0);
        p13.agregarAtributo("Maldad", 0.0);
        p13.agregarAtributo("Destreza", 7.0);
        p13.agregarAtributo("Inteligencia", 97.0);
        p13.agregarAtributo("Peso", 86.0);
        p13.agregarAtributo("Bondad", 98.5);
        Carta c13 = new Carta(p13);
        c13.agregarAtributo("Fuerza");
        c13.agregarAtributo("Velocidad");
        c13.agregarAtributo("Maldad");
        c13.agregarAtributo("Destreza");
        c13.agregarAtributo("Inteligencia");
        c13.agregarAtributo("Peso");
        c13.agregarAtributo("Bondad");

        this.cartas.put(String.valueOf(13), c13);

        /**CARTA N°14**/
        Personaje p14 = new Personaje("Raven", "Rachel Roth","resources/images.old/raven.png");
        p14.agregarAtributo("Fuerza", 85.0);
        p14.agregarAtributo("Velocidad", 85.0);
        p14.agregarAtributo("Maldad", 8.0);
        p14.agregarAtributo("Destreza", 9.0);
        p14.agregarAtributo("Inteligencia", 97.0);
        p14.agregarAtributo("Peso", 55.0);
        p14.agregarAtributo("Bondad", 75.0);
        Carta c14 = new Carta(p14);
        c14.agregarAtributo("Fuerza");
        c14.agregarAtributo("Velocidad");
        c14.agregarAtributo("Maldad");
        c14.agregarAtributo("Destreza");
        c14.agregarAtributo("Inteligencia");
        c14.agregarAtributo("Peso");
        c14.agregarAtributo("Bondad");

        this.cartas.put(String.valueOf(14), c14);

        /**CARTA N°15**/
        Personaje p15 = new Personaje("Starfire", "Koriand'r","resources/images.old/starfire.png");
        p15.agregarAtributo("Fuerza", 200.0);
        p15.agregarAtributo("Velocidad", 110.0);
        p15.agregarAtributo("Maldad", 0.0);
        p15.agregarAtributo("Destreza", 9.0);
        p15.agregarAtributo("Inteligencia", 89.0);
        p15.agregarAtributo("Peso", 57.0);
        p15.agregarAtributo("Bondad", 98.5);
        Carta c15 = new Carta(p15);
        c15.agregarAtributo("Fuerza");
        c15.agregarAtributo("Velocidad");
        c15.agregarAtributo("Maldad");
        c15.agregarAtributo("Destreza");
        c15.agregarAtributo("Inteligencia");
        c15.agregarAtributo("Peso");
        c15.agregarAtributo("Bondad");

        this.cartas.put(String.valueOf(15), c15);

        /**CARTA N°16**/
        Personaje p16 = new Personaje("Cyborg", "Victor 'Vic' Stone","resources/images.old/cyborg.png");
        p16.agregarAtributo("Fuerza", 1100.0);
        p16.agregarAtributo("Velocidad", 150.0);
        p16.agregarAtributo("Maldad", 0.0);
        p16.agregarAtributo("Destreza", 6.0);
        p16.agregarAtributo("Inteligencia", 95.5);
        p16.agregarAtributo("Peso", 110.0);
        p16.agregarAtributo("Bondad", 97.0);
        Carta c16 = new Carta(p16);
        c16.agregarAtributo("Fuerza");
        c16.agregarAtributo("Velocidad");
        c16.agregarAtributo("Maldad");
        c16.agregarAtributo("Destreza");
        c16.agregarAtributo("Inteligencia");
        c16.agregarAtributo("Peso");
        c16.agregarAtributo("Bondad");

        this.cartas.put(String.valueOf(16), c16);

        /**CARTA N°17**/
        Personaje p17 = new Personaje("Guason", "Desconocido","resources/images.old/el_guason.png");
        p17.agregarAtributo("Fuerza", 45.0);
        p17.agregarAtributo("Velocidad", 45.0);
        p17.agregarAtributo("Maldad", 98.0);
        p17.agregarAtributo("Destreza", 8.5);
        p17.agregarAtributo("Inteligencia", 97.0);
        p17.agregarAtributo("Peso", 67.0);
        p17.agregarAtributo("Bondad", 5.0);
        Carta c17 = new Carta(p17);
        c17.agregarAtributo("Fuerza");
        c17.agregarAtributo("Velocidad");
        c17.agregarAtributo("Maldad");
        c17.agregarAtributo("Destreza");
        c17.agregarAtributo("Inteligencia");
        c17.agregarAtributo("Peso");
        c17.agregarAtributo("Bondad");

        this.cartas.put(String.valueOf(17), c17);

        /**CARTA N°18**/
        Personaje p18 = new Personaje("El Pingüino", "Oswald Chesterfield Cobblepot","resources/images.old/el_pinguino.png");
        p18.agregarAtributo("Fuerza", 30.0);
        p18.agregarAtributo("Velocidad", 15.0);
        p18.agregarAtributo("Maldad", 100.0);
        p18.agregarAtributo("Destreza", 4.0);
        p18.agregarAtributo("Inteligencia", 99.0);
        p18.agregarAtributo("Peso", 90.0);
        p18.agregarAtributo("Bondad", 0.5);
        Carta c18 = new Carta(p18);
        c18.agregarAtributo("Fuerza");
        c18.agregarAtributo("Velocidad");
        c18.agregarAtributo("Maldad");
        c18.agregarAtributo("Destreza");
        c18.agregarAtributo("Inteligencia");
        c18.agregarAtributo("Peso");
        c18.agregarAtributo("Bondad");

        this.cartas.put(String.valueOf(18), c18);

        /**CARTA N°19**/
        Personaje p19 = new Personaje("Acertijo", "Edward Nigma","resources/images.old/acertijo.png");
        p19.agregarAtributo("Fuerza", 625.0);
        p19.agregarAtributo("Velocidad", 45.0);
        p19.agregarAtributo("Maldad", 98.0);
        p19.agregarAtributo("Destreza", 8.0);
        p19.agregarAtributo("Inteligencia", 90.0);
        p19.agregarAtributo("Peso", 62.0);
        p19.agregarAtributo("Bondad", 0.0);
        Carta c19 = new Carta(p19);
        c19.agregarAtributo("Fuerza");
        c19.agregarAtributo("Velocidad");
        c19.agregarAtributo("Maldad");
        c19.agregarAtributo("Destreza");
        c19.agregarAtributo("Inteligencia");
        c19.agregarAtributo("Peso");
        c19.agregarAtributo("Bondad");

        this.cartas.put(String.valueOf(19), c19);

        /**CARTA N°20**/
        Personaje p20 = new Personaje("Gatubela", "Selina Kyle","resources/images.old/gatubela.png");
        p20.agregarAtributo("Fuerza", 80.0);
        p20.agregarAtributo("Velocidad", 35.0);
        p20.agregarAtributo("Maldad", 99.0);
        p20.agregarAtributo("Destreza", 9.5);
        p20.agregarAtributo("Inteligencia", 90.0);
        p20.agregarAtributo("Peso", 60.5);
        p20.agregarAtributo("Bondad", 2.0);
        Carta c20 = new Carta(p20);
        c20.agregarAtributo("Fuerza");
        c20.agregarAtributo("Velocidad");
        c20.agregarAtributo("Maldad");
        c20.agregarAtributo("Destreza");
        c20.agregarAtributo("Inteligencia");
        c20.agregarAtributo("Peso");
        c20.agregarAtributo("Bondad");

        this.cartas.put(String.valueOf(20), c20);

        /**CARTA N°21**/
        Personaje p21 = new Personaje("Lex Luthor", "Alexander Joseph Luthor","resources/images.old/lex_luthor.png");
        p21.agregarAtributo("Fuerza", 415.0);
        p21.agregarAtributo("Velocidad", 90.0);
        p21.agregarAtributo("Maldad", 99.0);
        p21.agregarAtributo("Destreza", 7.5);
        p21.agregarAtributo("Inteligencia", 98.0);
        p21.agregarAtributo("Peso", 95.0);
        p21.agregarAtributo("Bondad", 0.0);
        Carta c21 = new Carta(p21);
        c21.agregarAtributo("Fuerza");
        c21.agregarAtributo("Velocidad");
        c21.agregarAtributo("Maldad");
        c21.agregarAtributo("Destreza");
        c21.agregarAtributo("Inteligencia");
        c21.agregarAtributo("Peso");
        c21.agregarAtributo("Bondad");

        this.cartas.put(String.valueOf(21), c21);

        /**CARTA N°22**/
        Personaje p22 = new Personaje("La Cosa del Pantano", "Alec Holland","resources/images.old/cosa_pantano.png");
        p22.agregarAtributo("Fuerza", 100.0);
        p22.agregarAtributo("Velocidad", 40.0);
        p22.agregarAtributo("Maldad", 95.0);
        p22.agregarAtributo("Destreza", 6.5);
        p22.agregarAtributo("Inteligencia", 89.5);
        p22.agregarAtributo("Peso", 90.0);
        p22.agregarAtributo("Bondad", 1.0);
        Carta c22 = new Carta(p22);
        c22.agregarAtributo("Fuerza");
        c22.agregarAtributo("Velocidad");
        c22.agregarAtributo("Maldad");
        c22.agregarAtributo("Destreza");
        c22.agregarAtributo("Inteligencia");
        c22.agregarAtributo("Peso");
        c22.agregarAtributo("Bondad");

        this.cartas.put(String.valueOf(22), c22);

        /**CARTA N°23**/
        Personaje p23 = new Personaje("Sangre", "Hermano Sangre","resources/images.old/sangre.png");
        p23.agregarAtributo("Fuerza", 1195.0);
        p23.agregarAtributo("Velocidad", 145.0);
        p23.agregarAtributo("Maldad", 90.0);
        p23.agregarAtributo("Destreza", 5.0);
        p23.agregarAtributo("Inteligencia", 97.0);
        p23.agregarAtributo("Peso", 77.0);
        p23.agregarAtributo("Bondad", 0.5);
        Carta c23 = new Carta(p23);
        c23.agregarAtributo("Fuerza");
        c23.agregarAtributo("Velocidad");
        c23.agregarAtributo("Maldad");
        c23.agregarAtributo("Destreza");
        c23.agregarAtributo("Inteligencia");
        c23.agregarAtributo("Peso");
        c23.agregarAtributo("Bondad");

        this.cartas.put(String.valueOf(23), c23);

        /**CARTA N°24**/
        Personaje p24 = new Personaje("Mr. Frio", "Victor Fries","resources/images.old/mr_frio.png");
        p24.agregarAtributo("Fuerza", 1000.0);
        p24.agregarAtributo("Velocidad", 60.0);
        p24.agregarAtributo("Maldad", 96.0);
        p24.agregarAtributo("Destreza", 6.0);
        p24.agregarAtributo("Inteligencia", 97.0);
        p24.agregarAtributo("Peso", 80.0);
        p24.agregarAtributo("Bondad", 0.0);
        Carta c24 = new Carta(p24);
        c24.agregarAtributo("Fuerza");
        c24.agregarAtributo("Velocidad");
        c24.agregarAtributo("Maldad");
        c24.agregarAtributo("Destreza");
        c24.agregarAtributo("Inteligencia");
        c24.agregarAtributo("Peso");
        c24.agregarAtributo("Bondad");

        this.cartas.put(String.valueOf(24), c24);

        /**CARTA N°25**/
        Personaje p25 = new Personaje("Thor", "Thor Odinson","resources/images.old/thor.png");
        p25.agregarAtributo("Fuerza", 1100.0);
        p25.agregarAtributo("Velocidad", 250.0);
        p25.agregarAtributo("Maldad", 10.0);
        p25.agregarAtributo("Destreza", 8.5);
        p25.agregarAtributo("Inteligencia", 95.0);
        p25.agregarAtributo("Peso", 103.0);
        p25.agregarAtributo("Bondad", 99.5);
        Carta c25 = new Carta(p25);
        c25.agregarAtributo("Fuerza");
        c25.agregarAtributo("Velocidad");
        c25.agregarAtributo("Maldad");
        c25.agregarAtributo("Destreza");
        c25.agregarAtributo("Inteligencia");
        c25.agregarAtributo("Peso");
        c25.agregarAtributo("Bondad");

        this.cartas.put(String.valueOf(25), c25);

        /**CARTA N°26**/
        Personaje p26 = new Personaje("Ciclope", "Scott Summers","resources/images.old/cyclope.png");
        p26.agregarAtributo("Fuerza", 600.0);
        p26.agregarAtributo("Velocidad", 90.0);
        p26.agregarAtributo("Maldad", 0.0);
        p26.agregarAtributo("Destreza", 8.0);
        p26.agregarAtributo("Inteligencia", 96.0);
        p26.agregarAtributo("Peso", 74.0);
        p26.agregarAtributo("Bondad", 98.5);
        Carta c26 = new Carta(p26);
        c26.agregarAtributo("Fuerza");
        c26.agregarAtributo("Velocidad");
        c26.agregarAtributo("Maldad");
        c26.agregarAtributo("Destreza");
        c26.agregarAtributo("Inteligencia");
        c26.agregarAtributo("Peso");
        c26.agregarAtributo("Bondad");

        this.cartas.put(String.valueOf(26), c26);

        /**CARTA N°27**/
        Personaje p27 = new Personaje("Bestia", "Henry Philip McCoy","resources/images.old/bestia.png");
        p27.agregarAtributo("Fuerza", 1000.0);
        p27.agregarAtributo("Velocidad", 86.0);
        p27.agregarAtributo("Maldad", 0.0);
        p27.agregarAtributo("Destreza", 8.5);
        p27.agregarAtributo("Inteligencia", 95.0);
        p27.agregarAtributo("Peso", 112.0);
        p27.agregarAtributo("Bondad", 99.0);
        Carta c27 = new Carta(p27);
        c27.agregarAtributo("Fuerza");
        c27.agregarAtributo("Velocidad");
        c27.agregarAtributo("Maldad");
        c27.agregarAtributo("Destreza");
        c27.agregarAtributo("Inteligencia");
        c27.agregarAtributo("Peso");
        c27.agregarAtributo("Bondad");

        this.cartas.put(String.valueOf(27), c27);

        /**CARTA N°28**/
        Personaje p28 = new Personaje("Wolverine", "James Hudson Howlett","resources/images.old/wolverine.png");
        p28.agregarAtributo("Fuerza", 900.0);
        p28.agregarAtributo("Velocidad", 215.0);
        p28.agregarAtributo("Maldad", 2.0);
        p28.agregarAtributo("Destreza", 9.0);
        p28.agregarAtributo("Inteligencia", 96.0);
        p28.agregarAtributo("Peso", 81.0);
        p28.agregarAtributo("Bondad", 98.0);
        Carta c28 = new Carta(p28);
        c28.agregarAtributo("Fuerza");
        c28.agregarAtributo("Velocidad");
        c28.agregarAtributo("Maldad");
        c28.agregarAtributo("Destreza");
        c28.agregarAtributo("Inteligencia");
        c28.agregarAtributo("Peso");
        c28.agregarAtributo("Bondad");

        this.cartas.put(String.valueOf(28), c28);

        /**CARTA N°29**/
        Personaje p29 = new Personaje("Storm", "Ororo Iqadi Munroe","resources/images.old/storm.png");
        p29.agregarAtributo("Fuerza", 120.0);
        p29.agregarAtributo("Velocidad", 110.0);
        p29.agregarAtributo("Maldad", 0.0);
        p29.agregarAtributo("Destreza", 9.5);
        p29.agregarAtributo("Inteligencia", 97.0);
        p29.agregarAtributo("Peso", 66.0);
        p29.agregarAtributo("Bondad", 98.0);
        Carta c29 = new Carta(p29);
        c29.agregarAtributo("Fuerza");
        c29.agregarAtributo("Velocidad");
        c29.agregarAtributo("Maldad");
        c29.agregarAtributo("Destreza");
        c29.agregarAtributo("Inteligencia");
        c29.agregarAtributo("Peso");
        c29.agregarAtributo("Bondad");

        this.cartas.put(String.valueOf(29), c29);

        /**CARTA N°30**/
        Personaje p30 = new Personaje("Prof. Xavier", "Bruce Wane","resources/images.old/prof_xavier.png");
        p30.agregarAtributo("Fuerza", 150.0);
        p30.agregarAtributo("Velocidad", 50.0);
        p30.agregarAtributo("Maldad", 0.0);
        p30.agregarAtributo("Destreza", 60.5);
        p30.agregarAtributo("Inteligencia", 100.0);
        p30.agregarAtributo("Peso", 90.0);
        p30.agregarAtributo("Bondad", 99.0);
        Carta c30 = new Carta(p30);
        c30.agregarAtributo("Fuerza");
        c30.agregarAtributo("Velocidad");
        c30.agregarAtributo("Maldad");
        c30.agregarAtributo("Destreza");
        c30.agregarAtributo("Inteligencia");
        c30.agregarAtributo("Peso");
        c30.agregarAtributo("Bondad");

        this.cartas.put(String.valueOf(30), c30);

        /**CARTA N°31**/
        Personaje p31 = new Personaje("Spider-Man", "Peter Benjamin Parker Fitzpatrick","resources/images.old/spiderman.png");
        p31.agregarAtributo("Fuerza", 500.0);
        p31.agregarAtributo("Velocidad", 150.0);
        p31.agregarAtributo("Maldad", 10.0);
        p31.agregarAtributo("Destreza", 10.0);
        p31.agregarAtributo("Inteligencia", 98.0);
        p31.agregarAtributo("Peso", 83.0);
        p31.agregarAtributo("Bondad", 99.0);
        Carta c31 = new Carta(p31);
        c31.agregarAtributo("Fuerza");
        c31.agregarAtributo("Velocidad");
        c31.agregarAtributo("Maldad");
        c31.agregarAtributo("Destreza");
        c31.agregarAtributo("Inteligencia");
        c31.agregarAtributo("Peso");
        c31.agregarAtributo("Bondad");

        this.cartas.put(String.valueOf(31), c31);

        /**CARTA N°32**/
        Personaje p32 = new Personaje("Iron Man", "Anthony Edward 'Tony' Stark","resources/images.old/ironman.png");
        p32.agregarAtributo("Fuerza", 930.0);
        p32.agregarAtributo("Velocidad", 197.0);
        p32.agregarAtributo("Maldad", 0.0);
        p32.agregarAtributo("Destreza", 8.5);
        p32.agregarAtributo("Inteligencia", 99.0);
        p32.agregarAtributo("Peso", 587.0);
        p32.agregarAtributo("Bondad", 90.0);
        Carta c32 = new Carta(p32);
        c32.agregarAtributo("Fuerza");
        c32.agregarAtributo("Velocidad");
        c32.agregarAtributo("Maldad");
        c32.agregarAtributo("Destreza");
        c32.agregarAtributo("Inteligencia");
        c32.agregarAtributo("Peso");
        c32.agregarAtributo("Bondad");

        this.cartas.put(String.valueOf(32), c32);

        /**CARTA N°33**/
        Personaje p33 = new Personaje("Antorcha Humana", "Johnny Storm","resources/images.old/hombre_antorcha.png");
        p33.agregarAtributo("Fuerza", 150.0);
        p33.agregarAtributo("Velocidad", 160.0);
        p33.agregarAtributo("Maldad", 0.0);
        p33.agregarAtributo("Destreza", 8.0);
        p33.agregarAtributo("Inteligencia", 80.0);
        p33.agregarAtributo("Peso", 76.5);
        p33.agregarAtributo("Bondad", 85.0);
        Carta c33 = new Carta(p33);
        c33.agregarAtributo("Fuerza");
        c33.agregarAtributo("Velocidad");
        c33.agregarAtributo("Maldad");
        c33.agregarAtributo("Destreza");
        c33.agregarAtributo("Inteligencia");
        c33.agregarAtributo("Peso");
        c33.agregarAtributo("Bondad");

        this.cartas.put(String.valueOf(33), c33);

        /**CARTA N°34**/
        Personaje p34 = new Personaje("Mujer Invisible", "Susan Storm","resources/images.old/mujer_invisible.png");
        p34.agregarAtributo("Fuerza", 250.0);
        p34.agregarAtributo("Velocidad", 80.0);
        p34.agregarAtributo("Maldad", 0.0);
        p34.agregarAtributo("Destreza", 8.0);
        p34.agregarAtributo("Inteligencia", 97.0);
        p34.agregarAtributo("Peso", 55.0);
        p34.agregarAtributo("Bondad", 98.5);
        Carta c34 = new Carta(p34);
        c34.agregarAtributo("Fuerza");
        c34.agregarAtributo("Velocidad");
        c34.agregarAtributo("Maldad");
        c34.agregarAtributo("Destreza");
        c34.agregarAtributo("Inteligencia");
        c34.agregarAtributo("Peso");
        c34.agregarAtributo("Bondad");

        this.cartas.put(String.valueOf(34), c34);

        /**CARTA N°35**/
        Personaje p35 = new Personaje("La Mole", "Ben Grimm","resources/images.old/la_mole.png");
        p35.agregarAtributo("Fuerza", 1800.0);
        p35.agregarAtributo("Velocidad", 80.0);
        p35.agregarAtributo("Maldad", 0.0);
        p35.agregarAtributo("Destreza", 5.0);
        p35.agregarAtributo("Inteligencia", 90.0);
        p35.agregarAtributo("Peso", 430.0);
        p35.agregarAtributo("Bondad", 98.0);
        Carta c35 = new Carta(p35);
        c35.agregarAtributo("Fuerza");
        c35.agregarAtributo("Velocidad");
        c35.agregarAtributo("Maldad");
        c35.agregarAtributo("Destreza");
        c35.agregarAtributo("Inteligencia");
        c35.agregarAtributo("Peso");
        c35.agregarAtributo("Bondad");

        this.cartas.put(String.valueOf(35), c35);

        /**CARTA N°36**/
        Personaje p36 = new Personaje("Hombre Elastico", "Reed Richards","resources/images.old/hombre_elastico.png");
        p36.agregarAtributo("Fuerza", 120.0);
        p36.agregarAtributo("Velocidad", 85.0);
        p36.agregarAtributo("Maldad", 0.0);
        p36.agregarAtributo("Destreza", 9.0);
        p36.agregarAtributo("Inteligencia", 100.0);
        p36.agregarAtributo("Peso", 81.0);
        p36.agregarAtributo("Bondad", 98.5);
        Carta c36 = new Carta(p36);
        c36.agregarAtributo("Fuerza");
        c36.agregarAtributo("Velocidad");
        c36.agregarAtributo("Maldad");
        c36.agregarAtributo("Destreza");
        c36.agregarAtributo("Inteligencia");
        c36.agregarAtributo("Peso");
        c36.agregarAtributo("Bondad");

        this.cartas.put(String.valueOf(36), c36);

        /**CARTA N°37**/
        Personaje p37 = new Personaje("Hulk", "Robert Bruce Banner","resources/images.old/hulk.png");
        p37.agregarAtributo("Fuerza", 2200.0);
        p37.agregarAtributo("Velocidad", 130.0);
        p37.agregarAtributo("Maldad", 25.0);
        p37.agregarAtributo("Destreza", 7.0);
        p37.agregarAtributo("Inteligencia", 80.0);
        p37.agregarAtributo("Peso", 450.0);
        p37.agregarAtributo("Bondad", 60.0);
        Carta c37 = new Carta(p37);
        c37.agregarAtributo("Fuerza");
        c37.agregarAtributo("Velocidad");
        c37.agregarAtributo("Maldad");
        c37.agregarAtributo("Destreza");
        c37.agregarAtributo("Inteligencia");
        c37.agregarAtributo("Peso");
        c37.agregarAtributo("Bondad");

        this.cartas.put(String.valueOf(37), c37);

        /**CARTA N°38**/
        Personaje p38 = new Personaje("Surfista Plateado", "Norrin Radd","resources/images.old/silver_surfer.png");
        p38.agregarAtributo("Fuerza", 750.0);
        p38.agregarAtributo("Velocidad", 245.0);
        p38.agregarAtributo("Maldad", 0.0);
        p38.agregarAtributo("Destreza", 8.5);
        p38.agregarAtributo("Inteligencia", 95.0);
        p38.agregarAtributo("Peso", 86.0);
        p38.agregarAtributo("Bondad", 100.0);
        Carta c38 = new Carta(p38);
        c38.agregarAtributo("Fuerza");
        c38.agregarAtributo("Velocidad");
        c38.agregarAtributo("Maldad");
        c38.agregarAtributo("Destreza");
        c38.agregarAtributo("Inteligencia");
        c38.agregarAtributo("Peso");
        c38.agregarAtributo("Bondad");

        this.cartas.put(String.valueOf(38), c38);

        /**CARTA N°39**/
        Personaje p39 = new Personaje("Dr. Doom", "Victor von Doom","resources/images.old/dr_doom.png");
        p39.agregarAtributo("Fuerza", 1000.0);
        p39.agregarAtributo("Velocidad", 185.0);
        p39.agregarAtributo("Maldad", 100.0);
        p39.agregarAtributo("Destreza", 8.0);
        p39.agregarAtributo("Inteligencia", 95.5);
        p39.agregarAtributo("Peso", 115.0);
        p39.agregarAtributo("Bondad", 0.0);
        Carta c39 = new Carta(p39);
        c39.agregarAtributo("Fuerza");
        c39.agregarAtributo("Velocidad");
        c39.agregarAtributo("Maldad");
        c39.agregarAtributo("Destreza");
        c39.agregarAtributo("Inteligencia");
        c39.agregarAtributo("Peso");
        c39.agregarAtributo("Bondad");

        this.cartas.put(String.valueOf(39), c39);

        /**CARTA N°40**/
        Personaje p40 = new Personaje("Hombre de Arena", "Flint Marko","resources/images.old/hombre_arena.png");
        p40.agregarAtributo("Fuerza", 1760.0);
        p40.agregarAtributo("Velocidad", 150.0);
        p40.agregarAtributo("Maldad", 75.0);
        p40.agregarAtributo("Destreza", 9.0);
        p40.agregarAtributo("Inteligencia", 80.0);
        p40.agregarAtributo("Peso", 205.0);
        p40.agregarAtributo("Bondad", 25.0);
        Carta c40 = new Carta(p40);
        c40.agregarAtributo("Fuerza");
        c40.agregarAtributo("Velocidad");
        c40.agregarAtributo("Maldad");
        c40.agregarAtributo("Destreza");
        c40.agregarAtributo("Inteligencia");
        c40.agregarAtributo("Peso");
        c40.agregarAtributo("Bondad");

        this.cartas.put(String.valueOf(40), c40);

        /**CARTA N°41**/
        Personaje p41 = new Personaje("Rinho", "Aleksei Mikhailovich Sytsevich","resources/images.old/rinho.png");
        p41.agregarAtributo("Fuerza", 1600.0);
        p41.agregarAtributo("Velocidad", 190.0);
        p41.agregarAtributo("Maldad", 99.0);
        p41.agregarAtributo("Destreza", 6.0);
        p41.agregarAtributo("Inteligencia", 50.0);
        p41.agregarAtributo("Peso", 320.0);
        p41.agregarAtributo("Bondad", 0.0);
        Carta c41 = new Carta(p41);
        c41.agregarAtributo("Fuerza");
        c41.agregarAtributo("Velocidad");
        c41.agregarAtributo("Maldad");
        c41.agregarAtributo("Destreza");
        c41.agregarAtributo("Inteligencia");
        c41.agregarAtributo("Peso");
        c41.agregarAtributo("Bondad");

        this.cartas.put(String.valueOf(41), c41);

        /**CARTA N°42**/
        Personaje p42 = new Personaje("Dr. Octopus", "Otto Octavius","resources/images.old/dr_octopus.png");
        p42.agregarAtributo("Fuerza", 750.0);
        p42.agregarAtributo("Velocidad", 42.0);
        p42.agregarAtributo("Maldad", 90.0);
        p42.agregarAtributo("Destreza", 9.0);
        p42.agregarAtributo("Inteligencia", 98.0);
        p42.agregarAtributo("Peso", 90.0);
        p42.agregarAtributo("Bondad", 40.0);
        Carta c42 = new Carta(p42);
        c42.agregarAtributo("Fuerza");
        c42.agregarAtributo("Velocidad");
        c42.agregarAtributo("Maldad");
        c42.agregarAtributo("Destreza");
        c42.agregarAtributo("Inteligencia");
        c42.agregarAtributo("Peso");
        c42.agregarAtributo("Bondad");

        this.cartas.put(String.valueOf(42), c42);

        /**CARTA N°43**/
        Personaje p43 = new Personaje("Escorpion", "Mac Gargan","resources/images.old/scorpion.png");
        p43.agregarAtributo("Fuerza", 1300.0);
        p43.agregarAtributo("Velocidad", 103.0);
        p43.agregarAtributo("Maldad", 99.0);
        p43.agregarAtributo("Destreza", 8.5);
        p43.agregarAtributo("Inteligencia", 95.5);
        p43.agregarAtributo("Peso", 100.0);
        p43.agregarAtributo("Bondad", 0.0);
        Carta c43 = new Carta(p43);
        c43.agregarAtributo("Fuerza");
        c43.agregarAtributo("Velocidad");
        c43.agregarAtributo("Maldad");
        c43.agregarAtributo("Destreza");
        c43.agregarAtributo("Inteligencia");
        c43.agregarAtributo("Peso");
        c43.agregarAtributo("Bondad");

        this.cartas.put(String.valueOf(43), c43);

        /**CARTA N°44**/
        Personaje p44 = new Personaje("Conmocionador", "Herman Schultz","resources/images.old/conmocionador.png");
        p44.agregarAtributo("Fuerza", 400.0);
        p44.agregarAtributo("Velocidad", 43.0);
        p44.agregarAtributo("Maldad", 80.0);
        p44.agregarAtributo("Destreza", 7.0);
        p44.agregarAtributo("Inteligencia", 95.5);
        p44.agregarAtributo("Peso", 79.0);
        p44.agregarAtributo("Bondad", 2.0);
        Carta c44 = new Carta(p44);
        c44.agregarAtributo("Fuerza");
        c44.agregarAtributo("Velocidad");
        c44.agregarAtributo("Maldad");
        c44.agregarAtributo("Destreza");
        c44.agregarAtributo("Inteligencia");
        c44.agregarAtributo("Peso");
        c44.agregarAtributo("Bondad");

        this.cartas.put(String.valueOf(44), c44);

        /**CARTA N°45**/
        Personaje p45 = new Personaje("Mysterio", "Quentin Beck","resources/images.old/mysterio.png");
        p45.agregarAtributo("Fuerza", 500.0);
        p45.agregarAtributo("Velocidad", 45.0);
        p45.agregarAtributo("Maldad", 95.0);
        p45.agregarAtributo("Destreza", 7.5);
        p45.agregarAtributo("Inteligencia", 97.0);
        p45.agregarAtributo("Peso", 79.0);
        p45.agregarAtributo("Bondad", 1.0);
        Carta c45 = new Carta(p45);
        c45.agregarAtributo("Fuerza");
        c45.agregarAtributo("Velocidad");
        c45.agregarAtributo("Maldad");
        c45.agregarAtributo("Destreza");
        c45.agregarAtributo("Inteligencia");
        c45.agregarAtributo("Peso");
        c45.agregarAtributo("Bondad");

        this.cartas.put(String.valueOf(45), c45);

        /**CARTA N°46**/
        Personaje p46 = new Personaje("Duende Verde", "Norman Osborn","resources/images.old/duende_verde.png");
        p46.agregarAtributo("Fuerza", 830.0);
        p46.agregarAtributo("Velocidad", 205.0);
        p46.agregarAtributo("Maldad", 95.0);
        p46.agregarAtributo("Destreza", 9.0);
        p46.agregarAtributo("Inteligencia", 98.0);
        p46.agregarAtributo("Peso", 60.0);
        p46.agregarAtributo("Bondad", 10.0);
        Carta c46 = new Carta(p46);
        c46.agregarAtributo("Fuerza");
        c46.agregarAtributo("Velocidad");
        c46.agregarAtributo("Maldad");
        c46.agregarAtributo("Destreza");
        c46.agregarAtributo("Inteligencia");
        c46.agregarAtributo("Peso");
        c46.agregarAtributo("Bondad");

        this.cartas.put(String.valueOf(46), c46);

        /**CARTA N°47**/
        Personaje p47 = new Personaje("Loki", "Loki Laufeyson","resources/images.old/loki.png");
        p47.agregarAtributo("Fuerza", 1000.0);
        p47.agregarAtributo("Velocidad", 190.0);
        p47.agregarAtributo("Maldad", 10.0);
        p47.agregarAtributo("Destreza", 8.0);
        p47.agregarAtributo("Inteligencia", 96.0);
        p47.agregarAtributo("Peso", 100.0);
        p47.agregarAtributo("Bondad", 10.0);
        Carta c47 = new Carta(p47);
        c47.agregarAtributo("Fuerza");
        c47.agregarAtributo("Velocidad");
        c47.agregarAtributo("Maldad");
        c47.agregarAtributo("Destreza");
        c47.agregarAtributo("Inteligencia");
        c47.agregarAtributo("Peso");
        c47.agregarAtributo("Bondad");

        this.cartas.put(String.valueOf(47), c47);

        /**CARTA N°48**/
        Personaje p48 = new Personaje("Iron Monger", "Obadiah Stane","resources/images.old/iron_monger.png");
        p48.agregarAtributo("Fuerza", 1800.0);
        p48.agregarAtributo("Velocidad", 280.0);
        p48.agregarAtributo("Maldad", 100.0);
        p48.agregarAtributo("Destreza", 5.0);
        p48.agregarAtributo("Inteligencia", 90.0);
        p48.agregarAtributo("Peso", 218.0);
        p48.agregarAtributo("Bondad", 0.0);
        Carta c48 = new Carta(p48);
        c48.agregarAtributo("Fuerza");
        c48.agregarAtributo("Velocidad");
        c48.agregarAtributo("Maldad");
        c48.agregarAtributo("Destreza");
        c48.agregarAtributo("Inteligencia");
        c48.agregarAtributo("Peso");
        c48.agregarAtributo("Bondad");

        this.cartas.put(String.valueOf(48), c48);

        //maso de 6 cartas...
        Mazo m = new MazoPrincipal("Los campeones");
        m.agregarCarta(this.cartas.get(String.valueOf(1)));
        m.agregarCarta(this.cartas.get(String.valueOf(2)));
        m.agregarCarta(this.cartas.get(String.valueOf(3)));
        m.agregarCarta(this.cartas.get(String.valueOf(4)));
        m.agregarCarta(this.cartas.get(String.valueOf(5)));
        m.agregarCarta(this.cartas.get(String.valueOf(6)));
        m.agregarCarta(this.cartas.get(String.valueOf(7)));
        m.agregarCarta(this.cartas.get(String.valueOf(8)));
        m.agregarCarta(this.cartas.get(String.valueOf(9)));
        m.agregarCarta(this.cartas.get(String.valueOf(10)));
        m.agregarCarta(this.cartas.get(String.valueOf(11)));
        m.agregarCarta(this.cartas.get(String.valueOf(12)));
        this.mazos.add(m);

        Mazo m2 = new MazoPrincipal("Los campeones2");
        m2.agregarCarta(this.cartas.get(String.valueOf(13)));
        m2.agregarCarta(this.cartas.get(String.valueOf(14)));
        m2.agregarCarta(this.cartas.get(String.valueOf(15)));
        m2.agregarCarta(this.cartas.get(String.valueOf(16)));
        m2.agregarCarta(this.cartas.get(String.valueOf(17)));
        m2.agregarCarta(this.cartas.get(String.valueOf(18)));
        m2.agregarCarta(this.cartas.get(String.valueOf(19)));
        m2.agregarCarta(this.cartas.get(String.valueOf(20)));
        m2.agregarCarta(this.cartas.get(String.valueOf(21)));
        m2.agregarCarta(this.cartas.get(String.valueOf(22)));
        m2.agregarCarta(this.cartas.get(String.valueOf(23)));
        m2.agregarCarta(this.cartas.get(String.valueOf(24)));
        this.mazos.add(m2);

        Mazo m3 = new MazoPrincipal("Los campeones3");
        m3.agregarCarta(this.cartas.get(String.valueOf(25)));
        m3.agregarCarta(this.cartas.get(String.valueOf(26)));
        m3.agregarCarta(this.cartas.get(String.valueOf(27)));
        m3.agregarCarta(this.cartas.get(String.valueOf(28)));
        m3.agregarCarta(this.cartas.get(String.valueOf(29)));
        m3.agregarCarta(this.cartas.get(String.valueOf(30)));
        m3.agregarCarta(this.cartas.get(String.valueOf(31)));
        m3.agregarCarta(this.cartas.get(String.valueOf(32)));
        m3.agregarCarta(this.cartas.get(String.valueOf(33)));
        m3.agregarCarta(this.cartas.get(String.valueOf(34)));
        m3.agregarCarta(this.cartas.get(String.valueOf(35)));
        m3.agregarCarta(this.cartas.get(String.valueOf(36)));
        this.mazos.add(m3);

        Mazo m4 = new MazoPrincipal("Los campeones4");
        m4.agregarCarta(this.cartas.get(String.valueOf(37)));
        m4.agregarCarta(this.cartas.get(String.valueOf(38)));
        m4.agregarCarta(this.cartas.get(String.valueOf(39)));
        m4.agregarCarta(this.cartas.get(String.valueOf(40)));
        m4.agregarCarta(this.cartas.get(String.valueOf(41)));
        m4.agregarCarta(this.cartas.get(String.valueOf(42)));
        m4.agregarCarta(this.cartas.get(String.valueOf(43)));
        m4.agregarCarta(this.cartas.get(String.valueOf(44)));
        m4.agregarCarta(this.cartas.get(String.valueOf(45)));
        m4.agregarCarta(this.cartas.get(String.valueOf(46)));
        m4.agregarCarta(this.cartas.get(String.valueOf(47)));
        m4.agregarCarta(this.cartas.get(String.valueOf(48)));
        this.mazos.add(m4);
    }

}