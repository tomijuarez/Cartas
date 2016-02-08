package controller;

/**
 * Created by Tomás on 30/12/2015.
 */
public class RegisterMediator implements Mediator {
    private FirstController controlador1;
    private SecondController controlador2;

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
        System.out.println("RegisterMediator");
    }

    @Override
    public void setControllers(MediableController controller1, MediableController controller2) {
        this.controlador1 = (FirstController) controller1;
        this.controlador2 = (SecondController) controller2;
    }
}
