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
        this.selectCurrentAttribute = this.strategy.getAttribute(this.currentCard);// la estrategia interactua con la vista
    }

    public String nameCurrentAttribute() {
        String aux = this.selectCurrentAttribute;
        return aux;
    }

    public double getAttribute(String a) {
        return this.currentCard.getAttribute(a);
    }

    public void addAccumulatorWinner(DeckPlayer accumulator) {
        this.deck.addCards(accumulator);
    }

    public String getName(){
        return this.userName;
    }
}