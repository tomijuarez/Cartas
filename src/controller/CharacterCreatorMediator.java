package controller;

/**
 * Created by Gandalf on 25/2/2016.
 */
public class CharacterCreatorMediator implements Mediator {
    private FirstController parentController;
    private CharacterCreatorController subController;

    public void setControllers(MediableController parentController, MediableController subController) {
        this.parentController = (FirstController) parentController;
        this.subController = (CharacterCreatorController) subController;
    }

    public MediableController getFirstController() {
        return this.parentController;
    }

    public MediableController getSecondController() {
        return this.subController;
    }

    @Override
    public void printMediator() {

    }
}
