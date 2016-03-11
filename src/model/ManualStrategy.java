package model;

import controller.GameController;

/*Implementacion de la estrategia manual que interactua con la interfaz de usuario*/
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
