package controller;

import controller.events.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import model.*;
import view.Context;
import view.GameWindow;

import java.net.URL;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

/**
 * Created by Gandalf on 30/12/2015.
 */
public class GameController extends MediableController implements Initializable, Observer, GameEventVisitor {
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

    /**
     * Model
     */

    private Juego game;

    public GameController(Juego game) {
        super();
        this.game = game;
    }


    @Override
    public void setMediator(Mediator mediator) {
        this.mediator = (GameMediator) mediator;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("OLA K TAL?");
    }

    @Override
    public void visit(InitGame event) {

    }

    @Override
    public void visit(ShiftTurn event) {
        Jugador currentPlayer = event.getCurrentPlayer();
        currentPlayer.nombreAtributoSeleccionado();
    }

    @Override
    public void visit(CardsSelection event) {
        List<Jugador> players = event.getPlayers();
        MazoJugador accumulatorDeck = new MazoJugador();

        for(Jugador player: players) {
            accumulatorDeck.agregarCarta(player.getCurrentCard());
        }
    }

    @Override
    public void visit(DeadHeatRound event) {

    }

    @Override
    public void visit(WinRound event) {

    }

    @Override
    public void update(Observable object, Object src) {

    }
}
