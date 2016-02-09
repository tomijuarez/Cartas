package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;

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

    private List<TextField> listedTextFields = new Vector<>();

    private String mazo;
    private List<String> userNames = new Vector<>();

    private int playersNum = 0;

    private ConfigurationMediator mediator;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        this.listTextFields();

        this.setTextFieldsEvents();

        this.addPlayerButton.setOnAction((event)->{
            this.addPlayer();
        });

        this.acceptButton.setOnAction((event) -> {
            if( this.verifyInputs() ) {
                this.mediator.rootControllerSetData(this.userNames);
                this.context.close();
            }
        });
    }

    @Override
    public void setMediator(Mediator mediator) {
        this.mediator = (ConfigurationMediator) mediator;
    }

    private void setTextFieldsEvents() {
        for (TextField field: this.listedTextFields) {
            field.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    setNormalStateInput(field);
                }
            });
        }
    }

    private void setNormalStateInput(TextField input) {
        input.getStyleClass().clear();
        input.getStyleClass().add("defaultStateTextInput");
    }

    private void setErrorStateInput(TextField input) {
        input.getStyleClass().clear();
        input.getStyleClass().add("errorStateTextInput");
    }

    private boolean verifyInputs() {
        boolean passed = true;
        for(int i = 0; i < this.playersNum; i++) {
            TextField field = this.listedTextFields.get(i);
            String name = field.getText();
            if (name.isEmpty()) {
                this.setErrorStateInput(field);
                passed = false;
                this.throwUIError("El nombre de un jugador no puede ser nulo.");
            }
            else
                this.userNames.add(name);
        }
        return passed;
    }

    private void throwUIError(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Lo sentimos");
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }

    private void listTextFields() {
        this.listedTextFields.add(this.playerInputName1);
        this.listedTextFields.add(this.playerInputName2);
        this.listedTextFields.add(this.playerInputName3);
        this.listedTextFields.add(this.playerInputName4);
    }

    private void addPlayer() {
        if ( this.playersNum < 4 ) {
            this.listedTextFields.get(this.playersNum).setDisable(false);
            this.playersNum++;
            return;
        }

        throwUIError("No se pueden añadir más de cuatro jugadores por partida.");
    }
}
