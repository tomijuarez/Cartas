package controller;

/**
 * Created by Tomás on 30/12/2015.
 */
public interface Mediator {
    public void setControllers(MediableController controller1, MediableController controller2);
    public MediableController getFirstController();
    public MediableController getSecondController();
    public void printMediator();
}