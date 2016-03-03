package controller;

import model.Deck;
import model.Card;
import java.util.List;
import java.util.Map;

/**
 * Created by Gandalf on 23/2/2016.
 */
public class DeckCreatorMediator implements Mediator {
    private FirstController parentController;
    private DeckCreatorController subController;

    public void setControllers(MediableController parentController, MediableController subController) {
        this.parentController = (FirstController) parentController;
        this.subController = (DeckCreatorController) subController;
    }

    public MediableController getFirstController() {
        return this.parentController;
    }

    public MediableController getSecondController() {
        return this.subController;
    }

    public void createDeck(List<Card> cards, String name,Map<String,Boolean> attributes){
        this.parentController.createDeck(cards,name,attributes);
    }

    @Override
    public void printMediator() {

    }
}
