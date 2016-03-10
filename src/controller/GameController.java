package controller;

import controller.events.*;
import controller.utils.AlertUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.*;
import view.Modal;
import view.model.CardView;

import java.net.URL;
import java.util.*;

/**
 * Created by Gandalf on 30/12/2015.
 */
public class GameController extends MediableController implements Initializable, Observer, GameEventVisitor {
    /**
     * Elementos del panel de juego.
     */

    @FXML
    private Pane firstPlayerDeck;
    @FXML
    private Pane firstPlayerCurrentCard;
    @FXML
    private Text firstPlayerName;

    @FXML
    private Pane secondPlayerDeck;
    @FXML
    private Pane secondPlayerCurrentCard;
    @FXML
    private Text secondPlayerName;

    @FXML
    private Pane thirdPlayerDeck;
    @FXML
    private Pane thirdPlayerCurrentCard;
    @FXML
    private Text thirdPlayerName;

    @FXML
    private Pane fourthPlayerDeck;
    @FXML
    private Pane fourthPlayerCurrentCard;
    @FXML
    private Text fourthPlayerName;

    @FXML
    private Pane thirdPlayerSpace;
    @FXML
    private Pane fourthPlayerSpace;

    @FXML
    private Pane globalDeckAccumulator;

    @FXML
    private Pane shadedPane;

    @FXML
    private Button dismissButton;

    @FXML
    private Button nextRoundButton;

    @FXML
    private GridPane mainContainer;
    @FXML
    private GridPane confrontationContainer;

    @FXML
    private Button confirmAttributeButton;
    @FXML
    private ListView attributesListView;
    @FXML
    private Pane attributesSelectorPane;

    private GameMediator mediator;

    private Player currentPlayer;

    AlertUtils alerts = new AlertUtils();

    private List<Player> players;

    private String selectedAttribute = null;

    /**
     * Model
     */

    private Game game;

    public GameController(Game game) {
        this.game = game;
        this.players = this.game.getPlayers();
    }

    @Override
    public void setMediator(Mediator mediator) {
        this.mediator = (GameMediator) mediator;
    }

    private List<Text> getListedPlayersNames() {
        List<Text> labels = new ArrayList<>();
        labels.add(firstPlayerName);
        labels.add(secondPlayerName);
        labels.add(thirdPlayerName);
        labels.add(fourthPlayerName);
        return labels;
    }

    private List<Pane> getListedPlayersDecks() {
        List<Pane> decks = new ArrayList<>();
        decks.add(firstPlayerDeck);
        decks.add(secondPlayerDeck);
        decks.add(thirdPlayerDeck);
        decks.add(fourthPlayerDeck);
        return decks;
    }

    private void setPlayersNames() {
        List<Text> labels = getListedPlayersNames();
        for(int i = 0; i < this.players.size(); i++)
            labels.get(i).setText(this.players.get(i).getName());
    }

    private void showSpacesIfNeeded() {
        if(this.players.size() == 3)
            this.thirdPlayerSpace.setVisible(true);
        if(this.players.size() == 4) {
            this.thirdPlayerSpace.setVisible(true);
            this.fourthPlayerSpace.setVisible(true);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.setPlayersNames();
        this.showSpacesIfNeeded();
        this.game.addObserver(this);

        this.nextRoundButton.setOnAction((event)->{
            this.shadedPane.setVisible(false);
            this.game.nextRound();
        });

        this.dismissButton.setOnAction((event)->{
            this.dismissButton.setVisible(false);
            this.shadedPane.setVisible(false);
            this.nextRoundButton.setVisible(true);
        });

        this.confirmAttributeButton.setOnAction((event)->{
            if(this.selectedAttribute != null) {
                this.attributesSelectorPane.setVisible(false);
                this.shadedPane.setVisible(false);
            }
            else
                this.alerts.throwUIError("Debes elegir un atributo para continuar.");
        });
    }

    private List<Pane> getCurrentCardsSpaces() {
        List<Pane> spaces = new ArrayList<>();
        spaces.add(this.firstPlayerCurrentCard);
        spaces.add(this.secondPlayerCurrentCard);
        spaces.add(this.thirdPlayerCurrentCard);
        spaces.add(this.fourthPlayerCurrentCard);
        return spaces;
    }

    public String requestAttribute(Card c) {
        String attribute = null;
        do {
            List<String> choices = new ArrayList<>(c.getAttributes());
            ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
            dialog.setTitle("Hey");
            dialog.setHeaderText("Elegí un atributo");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent())
                attribute = result.get();
        }
        while(attribute == null);

        return attribute;
    }

    @Override
    public void visit(ConfrontationEvent event) {
        this.shadedPane.setVisible(true);
        this.confrontationContainer.setVisible(true);
        this.dismissButton.setVisible(true);


        List<Player> players = event.getPlayers();
        for (int i = 0; i < players.size(); i++) {

            Pane space = (Pane)this.confrontationContainer.getChildren().get(i);
            List<Node> childs = space.getChildren();

            Pane cardView = (Pane)childs.get(0);
            Card card = players.get(i).getCurrentCard();
            cardView.getChildren().clear();
            cardView.getChildren().add(new CardView(card, true).getResult());

            ListView list = (ListView)childs.get(1);
            list.setItems(FXCollections.observableArrayList(card.getAttributes()));
        }
    }

    @Override
    public void visit(ShiftTurn event) {
        this.currentPlayer = event.getCurrentPlayer();
        this.alerts.throwUINotice("Es el turno de " + this.currentPlayer.getName());
    }

    @Override
    public void visit(CardsSelection event) {
        List<Pane> spaces = this.getCurrentCardsSpaces();
        for (int i = 0; i < this.players.size(); i++) {
            spaces.get(i).getChildren().clear();
            spaces.get(i).getChildren().add(
                    new CardView(
                            this.players.get(i).getCurrentCard(),
                            false
                    ).getResult()
            );
        }
    }

    @Override
    public void visit(DeadHeatRound event) {
        this.alerts.throwUINotice("Hubo un empate.");
    }

    @Override
    public void visit(WinRound event) {
        this.alerts.throwUINotice("El ganador de la ronda es: " + event.getWinner().getName());
    }

    @Override
    public void visit(TieBreakCardsSelection event) {

    }

    @Override
    public void update(Observable object, Object src) {
        ((GameEventAcceptor) src).accept(this);
    }

}
