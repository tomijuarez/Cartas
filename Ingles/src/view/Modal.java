package view;

import controller.MediableController;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import javax.swing.*;

/**
 * Created by Gandalf on 4/2/2016.
 */
public class Modal extends GameWindow {

    private Stage stage;

    public Modal(String view, MediableController controller, Stage stage, GameWindow parent) {
        super(view, controller, stage);
        this.stage = stage;
        stage.initModality(Modality.APPLICATION_MODAL);
        this.blockParent(parent.getStage());
    }

    public void blockParent(Stage stage) {
        this.stage.initOwner(stage);
    }

}
