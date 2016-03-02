package controller;

import model.AbstractCharacter;

import java.util.List;

/**
 * Created by Gandalf on 29/2/2016.
 */
public class CardCreatorMediator implements Mediator {

    private FirstController parentController;
    private CardCreatorController subController;

    public void setControllers(MediableController parentController, MediableController subController) {
        this.parentController = (FirstController) parentController;
        this.subController = (CardCreatorController) subController;
    }

    public MediableController getFirstController() {
        return this.parentController;
    }

    public MediableController getSecondController() {
        return this.subController;
    }

    public void createCard(AbstractCharacter selectedCharacter, List<String> attributes) {
        this.parentController.createCard(selectedCharacter, attributes);
    }

    @Override
    public void printMediator() {

    }
}
