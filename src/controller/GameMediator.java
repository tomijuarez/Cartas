package controller;

/**
 * Created by Gandalf on 4/2/2016.
 */

public class GameMediator implements Mediator {
    private FirstController controlador1;
    private GameController controlador2;

    @Override
    public MediableController getFirstController() {
        return this.controlador1;
    }

    @Override
    public MediableController getSecondController() {
        return this.controlador2;
    }

    @Override
    public void printMediator() {
        System.out.println("GameMediator");
    }

    @Override
    public void setControllers(MediableController controller1, MediableController controller2) {
        this.controlador1 = (FirstController) controller1;
        this.controlador2 = (GameController) controller2;
    }
}
