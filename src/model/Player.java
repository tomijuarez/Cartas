package model;

import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Guillermo on 25/2/2016.
 */
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

    public Card getCurrentCard() {
        return this.currentCard;
    }

    public void takeCard(){this.currentCard = this.deck.getCard();}

    public int numberCards() {
        return this.deck.getNumberCards();
    }


    public void selectAttribute() {
        Map<String,Double> attributes = new Hashtable<>();

        for(String s : this.deck.getAttributes()){
            attributes.put(s,new Double(this.currentCard.getAttribute(s)));
            System.out.println("Entre a agrego atributo: "+ s);
        }


        this.selectCurrentAttribute = this.strategy.getAttribute(attributes);// la estrategia interactua con la vista
    }

    public String nameCurrentAttribute() {
        String aux = this.selectCurrentAttribute;
        return aux;
    }

    /* Con esta funcion ya no es necesario obtener obtener la cartaActual,
    primero obtengo la carta,despues pido el atributo con la estrategia,
    y por ultimo, obtengo el valor de ese atributo
    */
    public double getAttribute(String attrib) {
        return this.currentCard.getAttribute(attrib);
    }

    public void addAccumulatorWinner(DeckPlayer accumulator) {
        this.deck.addCards(accumulator);
    }

    public String getName(){
        return this.userName;
    }
}