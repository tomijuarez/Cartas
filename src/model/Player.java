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

    public Deck getDeck(){
        return this.deck;
    }

    public Card getCurrentCard() {
        this.currentCard = this.deck.getCard();
        return this.currentCard;
    }

    public void takeCard(){this.currentCard = this.deck.getCard();}

    public int numberCards() {
        return this.deck.getNumberCards();
    }

<<<<<<< HEAD
    public void selectAttribute(Card c){
        this.selectCurrentAttribute = this.strategy.getAttribute(c);// la estrategia interactua con la vista
=======

    public void selectAttribute() {
        Map<String,Double> attributes = new Hashtable<>();

        for(String s : this.deck.getAttributes()){
            attributes.put(s,new Double(this.currentCard.getAttribute(s)));
            System.out.println("Entre a agrego atributo: "+ s);
        }


        this.selectCurrentAttribute = this.strategy.getAttribute(attributes);// la estrategia interactua con la vista
>>>>>>> origin/master
    }

    public String nameCurrentAttribute() {
        String aux = this.selectCurrentAttribute;
        return aux;
    }


    public void addAccumulatorWinner(DeckPlayer accumulator) {
        this.deck.addCards(accumulator);
    }

    public String getName(){
        return this.userName;
    }

    public double getAttribute(String a){
       return this.currentCard.getAttribute(a);
    }
}