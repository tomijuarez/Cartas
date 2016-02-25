package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guillermo on 25/2/2016.
 */
public class MainDeck extends Deck{

    public MainDeck(String name){
        super(name);
    }

    public List<DeckPlayer> distributeCards(int numPlayers){

        List<DeckPlayer> decks = new ArrayList<>();
        for(int i=0; i < numPlayers; i++){
            DeckPlayer cj = new DeckPlayer();
            decks.add(i, cj);
        }

        while(!this.cards.isEmpty()){
            for(int i=0;i < numPlayers; i++){
                DeckPlayer m = decks.remove(i);
                m.addCard(this.cards.remove(0));
                decks.add(m);
            }
        }
        //notificar que se repartieron las cartas en el metodo que invoco a esta funcion
        return decks;

    }
}
