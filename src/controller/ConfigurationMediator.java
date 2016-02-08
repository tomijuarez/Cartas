package controller;

/**
 * Created by Gandalf on 8/2/2016.
 */
public class ConfigurationMediator implements Mediator {
    private FirstController controller1;
    private ConfigurationController controller2;

    public void setControllers(MediableController controller1, MediableController controller2) {
        this.controller1 = (FirstController) controller1;
        this.controller2 = (ConfigurationController) controller2;
    }

    public MediableController getFirstController() {
        return this.controller1;
    }

    public MediableController getSecondController() {
        return this.controller2;
    }

    public void printMediator() {

    }
}
