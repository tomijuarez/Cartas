package model;

import java.util.List;


/*Clase para representar el mazo de juego de los jugadores*/
public class DeckPlayer extends Deck {

    public DeckPlayer(){

    }

    /*Sacar la proxima carta del mazo*/
    public Card getCard(){
        return this.cards.remove(0);
    }

    /*Agregar cartas de un mazo*/
    public void addCards(DeckPlayer accumulator){

        List<Card> collectedLetters = accumulator.getCards();
        while(!collectedLetters.isEmpty()){
            this.cards.add(collectedLetters.remove(0));
        }

    }

    /*Agregar una carta al mazo*/
    public void addCard(Card c){
        this.cards.add(c);
    }

    public void clear(){
        this.cards.clear();
    }

}
