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
import model.Strategy;
import model.Game;
import model.Player;
import model.Deck;
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
    private Button initGameButton;
    @FXML
    private Button createCharacterButton;
    @FXML
    private Button createDeckButton;
    @FXML
    private Button createCardButton;

    private Mediator mediator;

    private Strategy selectedStrategy;
    private List<String> playerNames;
    private List<Boolean> managedManually;

    private Game game;

    public FirstController(Game game) {
        game.addObserver(this);
        this.game = game;
    }

    @Override
    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initGameButton.setOnAction((event)-> {
            System.out.println("pase a configurar los nombres");
            Modal subWindow = new Modal("layouts/configurationView.fxml", new ConfigurationController(), new Stage(), this.context);
            this.context.setChild(subWindow, new ConfigurationMediator());
        });

        createDeckButton.setOnAction((event)->{
            Modal deckCreatorWindow = new Modal("layouts/deckCreatorView.fxml", new DeckCreatorController(this.game.getCards()), new Stage(), this.context);
            this.context.setChild(deckCreatorWindow, new DeckCreatorMediator());
        });
        /*
        crearCartas.setOnAction((event)->{
            Modal subWindow = new Modal("layouts/secondView.fxml", new SecondController(), new Stage(), event);
            this.context.setChild(subWindow, this.mediator);
        });*/
    }

    public void initDeckSelectorUI() {
        Modal malletSelector = new Modal("layouts/deckSelector.fxml", new DeckSelectorController(this.game.getDecks()), new Stage(), this.context);
        this.context.setChild(malletSelector, new DeckSelectorMediator());
    }

    public void initGameUI() {/*
        Modal subWindow = new Modal("layouts/GameView.fxml", new GameController(), new Stage(), this.context);
        this.context.setChild(subWindow, new GameMediator());*/
    }

    public void setGameArtifacts(List<String> playerNames, List<Boolean> managedManually, Strategy selectedStrategy) {

        this.playerNames = playerNames;
        this.managedManually = managedManually;
        this.selectedStrategy = selectedStrategy;
        this.initDeckSelectorUI();
    }

    public void setGameDeck(Deck deck) {
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