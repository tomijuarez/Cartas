package controller;

import model.Character;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void createCharacter(String characterName, String realName, Map<String, String> attributes) {
        Map<String, Double> parsedAttributes = new HashMap<>();
        for (Map.Entry<String, String> attribute : attributes.entrySet()) {
            parsedAttributes.put(attribute.getKey(), Double.parseDouble(attribute.getValue()));
        }
        this.parentController.createCharacter(characterName, realName, parsedAttributes);
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
