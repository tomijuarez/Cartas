package model;

import java.util.ArrayList;
import java.util.List;

/*Clase para representar el mazo principal del juego*/
public class MainDeck extends Deck{
    protected String name;

    public MainDeck(String name){
        this.setName(name);
    }

    /*Repartir el mazo en mazos mas pequeños para los jugadores*/
    public List<DeckPlayer> share(int numbersPlayers){

        List<DeckPlayer> decks = new ArrayList<>();
        for(int i=0; i < numbersPlayers; i++){
            DeckPlayer cj = new DeckPlayer();
            decks.add(i, cj);
        }

        List<Card> auxiliar = new ArrayList<>();

        while(!this.cards.isEmpty()){
            for(int i=0;i < numbersPlayers; i++){
                DeckPlayer m = decks.remove(i);

                Card c = this.cards.remove(0);
                auxiliar.add(c);

                m.addCard(c);
                decks.add(i,m);
            }
        }

        this.cards = auxiliar;

        return decks;

    }

    /*Obtener nombre del mazo*/
    public String getName() {
        return this.name;
    }

    /*Setear nombre del mazo*/
    public void setName(String name) {
        this.name = name;
    }
}
