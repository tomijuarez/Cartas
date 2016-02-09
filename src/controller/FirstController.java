package controller;

import controller.MediableController;
import controller.Mediator;
import controller.RegisterMediator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Jugador;
import model.Mazo;
import view.Context;
import view.GameWindow;
import view.Modal;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Tomás on 30/12/2015.
 */
public class FirstController extends MediableController implements Initializable {
    @FXML
    private Button comenzarJuego;
    @FXML
    private Button crearMazo;
    @FXML
    private Button crearCartas;

    private Mediator mediator;

    private Mazo mazo;
    private List<Jugador> players;

    @Override
    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comenzarJuego.setOnAction((event)-> {
            Modal subWindow = new Modal("layouts/configurationView.fxml", new ConfigurationController(), new Stage(), this.context);
            this.context.setChild(subWindow, new ConfigurationMediator());
        });

        crearMazo.setOnAction((event)->{
            Modal mazoWindow = new Modal("layouts/secondView.fxml", new SecondController(), new Stage(), this.context);
            this.context.setChild(mazoWindow, new RegisterMediator());
        });
        /*
        crearCartas.setOnAction((event)->{
            Modal subWindow = new Modal("layouts/secondView.fxml", new SecondController(), new Stage(), event);
            this.context.setChild(subWindow, this.mediator);
        });*/
    }

    public void initGameUI() {
        Modal subWindow = new Modal("layouts/GameView.fxml", new GameController(), new Stage(), this.context);
        this.context.setChild(subWindow, new GameMediator());
    }

    public void setGameArtifacts(List<Jugador> players, Mazo mazo) {
        this.players = players;
        this.mazo = mazo;
    }

    private void showCardsCreatorPane() {

    }

    public void showText(String text) {
        //ji
    }
}