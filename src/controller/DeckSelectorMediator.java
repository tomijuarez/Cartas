package controller;

import javafx.scene.control.TextField;
import model.Player;
import model.MainDeck;

import javax.xml.soap.Text;
import java.util.List;
import java.util.Vector;

/**
 * Created by Gandalf on 8/2/2016.
 */
public class DeckSelectorMediator implements Mediator {
    private FirstController parentController;
    private DeckSelectorController subController;

    public void setControllers(MediableController parentController, MediableController subController) {
        this.parentController = (FirstController) parentController;
        this.subController = (DeckSelectorController) subController;
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

    public void setDeck(MainDeck deck) {
        this.parentController.setGameDeck(deck);
    }
}
