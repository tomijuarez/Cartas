package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import view.Context;
import view.GameWindow;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Tomás on 30/12/2015.
 */
public class SecondController extends MediableController implements Initializable {

    private RegisterMediator mediator;

    public SecondController() {
        super();
    }

    @Override
    public void setContext(GameWindow context) {
        this.context = context;
    }

    @Override
    public void setMediator(Mediator mediator) {
        this.mediator = (RegisterMediator) mediator;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}
