package controller;

import javafx.scene.control.TextField;
import model.*;

import javax.xml.soap.Text;
import java.util.List;
import java.util.Vector;

/**
 * Created by Gandalf on 8/2/2016.
 */
public class ConfigurationMediator implements Mediator {
    private FirstController parentController;
    private ConfigurationController subController;

    public void setControllers(MediableController parentController, MediableController subController) {
        this.parentController = (FirstController) parentController;
        this.subController = (ConfigurationController) subController;
    }

    public MediableController getFirstController() {
        return this.parentController;
    }

    public MediableController getSecondController() {
        return this.subController;
    }

    public void rootControllerSetData(List<String> userNames, List<Boolean> managedManually, Strategy selectedStrategy) {
        this.parentController.setGameArtifacts(userNames, managedManually, selectedStrategy);
    }

    public void printMediator() {

    }
}
