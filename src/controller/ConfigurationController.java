package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Gandalf on 8/2/2016.
 */
public class ConfigurationController extends MediableController implements Initializable {

    @FXML
    private Button addPlayerButton;
    @FXML
    private TextField playerInputName1;
    @FXML
    private TextField playerInputName2;
    @FXML
    private TextField playerInputName3;
    @FXML
    private TextField playerInputName4;
    @FXML
    private Button acceptButton;

    private ConfigurationMediator mediator;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.acceptButton.setOnAction((event) -> {
            System.out.println("Continuar al juego.");
        });
    }

    @Override
    public void setMediator(Mediator mediator) {
        this.mediator = (ConfigurationMediator) mediator;
    }
}
