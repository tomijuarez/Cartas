package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import view.Context;
import view.GameWindow;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Gandalf on 30/12/2015.
 */
public class GameController extends MediableController implements Initializable {
    /**
     * Elementos del panel de juego.
     */

    @FXML
    private Pane mazoJugador1;
    @FXML
    private Pane cartaActualJugador1;
    @FXML
    private Pane mazoJugador2;
    @FXML
    private Pane cartaActualJugador2;
    @FXML
    private Pane mazoJugador3;
    @FXML
    private Pane cartaActualJugador3;
    @FXML
    private Pane mazoJugador4;
    @FXML
    private Pane cartaActualJugador4;
    @FXML
    private Pane pozo;

    private GameMediator mediator;

    public GameController() {
        super();
    }

    @Override
    public void setMediator(Mediator mediator) {
        this.mediator = (GameMediator) mediator;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("OLA K TAL?");
    }
}
