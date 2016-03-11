package model;

import java.util.List;


/**
 * Created by Guillermo on 25/2/2016.
 */
public class DeckPlayer extends Deck {

    public DeckPlayer(){

    }

    public Card getCard(){
        return this.cards.remove(0);
    }

    public void addCards(DeckPlayer accumulator){

        List<Card> collectedLetters = accumulator.getCards();
        while(!collectedLetters.isEmpty()){
            this.cards.add(collectedLetters.remove(0));
        }

    }


    public void clear(){
        this.cards.clear();
    }
    public void addCard(Card c){
        this.cards.add(c);
    }
}
