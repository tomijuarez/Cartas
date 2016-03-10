package model;

import controller.GameController;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Gandalf on 21/2/2016.
 */
public class ManualStrategy implements Strategy {

    GameController controller;

    public ManualStrategy(GameController controller) {
        this.controller = controller;
    }

    @Override
    public String getAttribute(Card c) {
        String t = this.controller.requestAttribute(c);
        System.out.println("LLEGO F");
        return t;
    }

}
