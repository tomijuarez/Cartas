package model;

/*Clase para respresentar a los jugadores del juego*/

public class Player {

    private Strategy strategy;
    private String userName;
    private Card currentCard;
    private DeckPlayer deck;
    private String selectCurrentAttribute;

    public Player(Strategy e, String nU, DeckPlayer m) {
        this.strategy = e;
        this.userName = nU;
        this.deck = m;
    }

    /*Obtener la carta actual del jugador*/
    public Card getCurrentCard() {
        return this.currentCard;
    }

    /*Tomar una nueva carta del mazo*/
    public void takeCard() {
        this.currentCard = this.deck.getCard();
    }

    /*Obtener cantidad de cartas que le quedan al jugador en su mazo*/
    public int numberCards() {
        return this.deck.getNumberCards();
    }

    /*Seleccionar un atributo apra jugar*/
    public void selectAttribute() {
        this.selectCurrentAttribute = this.strategy.getAttribute(this.currentCard);
    }

    /*Obtener el atributo elegido actualmente*/
    public String nameCurrentAttribute() {
        return this.selectCurrentAttribute;
    }

    /*Obtener el valor para el atributo en la carta actual del jugador*/
    public double getAttribute(String a) {
        return this.currentCard.getAttribute(a);
    }

    /*Entregar al jugador un mazo de cartas (Pozo) para que lo agregue a su mazo de juego*/
    public void addAccumulatorWinner(DeckPlayer accumulator) {
        this.deck.addCards(accumulator);
    }

    /*Obtener el nombre del jugador*/
    public String getName(){
        return this.userName;
    }

    /*Dar una carta al jugador para que la agregue a su mazo de juego*/
    public void giveCard(Card c){
        this.deck.addCard(c);
    }

    /*Indicar al jugador que debe mezclar su mazo de juego*/
    public void riffleDeck(int times){
        this.deck.riffle(times);
    }
}