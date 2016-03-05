package controller;

import model.Card;
import model.Character;

import java.util.List;
import java.util.Map;

/**
 * Created by Gandalf on 3/3/2016.
 */
public class LeagueCreatorMediator implements Mediator {
    private FirstController parentController;
    private LeagueCreatorController subController;

    public void setControllers(MediableController parentController, MediableController subController) {
        this.parentController = (FirstController) parentController;
        this.subController = (LeagueCreatorController) subController;
    }

    public MediableController getFirstController() {
        return this.parentController;
    }

    public MediableController getSecondController() {
        return this.subController;
    }

    public void setSelectedCharacters(String leagueName, List<Character> characters) {
        this.parentController.createLeague(leagueName, characters);
    }

    @Override
    public void printMediator() {

    }
}