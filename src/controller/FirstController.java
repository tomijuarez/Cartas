package controller;

import com.sun.org.apache.xpath.internal.operations.Bool;
import controller.MediableController;
import controller.Mediator;
import controller.RegisterMediator;
import controller.events.ListArtifactsEventVisitor;
import controller.events.ListCards;
import controller.events.ListDecks;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Estrategia;
import model.Juego;
import model.Jugador;
import model.Mazo;
import view.Context;
import view.GameWindow;
import view.Modal;
import view.model.DeckView;

import java.net.URL;
import java.util.*;

/**
 * Created by Tomás on 30/12/2015.
 */
public class FirstController extends MediableController implements Initializable, Observer, ListArtifactsEventVisitor {
    @FXML
    private Button comenzarJuego;
    @FXML
    private Button crearMazo;
    @FXML
    private Button crearCartas;

    private Mediator mediator;

    private Estrategia selectedStrategy;
    private List<String> playerNames;
    private List<Boolean> managedManually;

    private Juego game;

    public FirstController(Juego game) {
        game.addObserver(this);
        this.game = game;
    }

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
            /*Modal mazoWindow = new Modal("layouts/secondView.fxml", new SecondController(), new Stage(), this.context);
            this.context.setChild(mazoWindow, new RegisterMediator());*/
        });
        /*
        crearCartas.setOnAction((event)->{
            Modal subWindow = new Modal("layouts/secondView.fxml", new SecondController(), new Stage(), event);
            this.context.setChild(subWindow, this.mediator);
        });*/
    }

    public void initDeckSelectorUI() {
        Modal malletSelector = new Modal("layouts/deckSelector.fxml", new DeckSelectorController(), new Stage(), this.context);
        this.context.setChild(malletSelector, new DeckSelectorMediator());
    }

    public void initGameUI() {/*
        Modal subWindow = new Modal("layouts/GameView.fxml", new GameController(), new Stage(), this.context);
        this.context.setChild(subWindow, new GameMediator());*/
    }

    public void setGameArtifacts(List<String> playerNames, List<Boolean> managedManually, Estrategia selectedStrategy) {
        this.playerNames = playerNames;
        this.managedManually = managedManually;
        this.selectedStrategy = selectedStrategy;
        this.initDeckSelectorUI();
    }

    public void setGameDeck(Mazo deck) {
        this.game.createPlayers(this.playerNames, this.managedManually, this.selectedStrategy, deck);
    }

    private void showCardsCreatorPane() {

    }

    public void showText(String text) {
        //ji
    }

    @Override
    public void update(Observable object, Object src) {

    }

    @Override
    public void visit(ListDecks event) {

    }

    @Override
    public void visit(ListCards event) {

    }
}